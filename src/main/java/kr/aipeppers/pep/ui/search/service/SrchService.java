package kr.aipeppers.pep.ui.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.MsgUtil;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class SrchService {

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	private Paginate paginate;

	@Autowired
	HttpSession session = SessionUtil.getSession();

	/**
	 * @Method Name : searchList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> searchList(Box box) throws Exception {
		List<Box> reciveBox = new ArrayList<>();
//		//로그인 체크
//		if(session.getAttribute(CmnConst.SES_USER_ID) == null || "".equals(session.getAttribute(CmnConst.SES_USER_ID))) {
//			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
//		}
		box.put("sBox", SessionUtil.getUserData());
		if (box.getBox("sBox") != null ) {
			box.put("userId", box.getBox("sBox").nvl("id"));
		}

		if("user".equals(box.nvl("type"))) { //user
			int totCnt = dao.selectOne("srch.info.userCnt", box);
			if(totCnt <= 0) {
				throw new BizException("E107", new String[] {"해당 사용자가"}); // {0} 존재하지 않습니다.
			}
			paginate.init(box, totCnt);
			List<Box> resultList =  dao.selectList("srch.info.userList", box);
			resultList.forEach(rowBox -> {
				rowBox.put("userId", rowBox.nvl("id"));
				rowBox.put("followersCount", dao.selectOne("srch.info.myFollowerCnt",rowBox));
				rowBox.put("videosCount", dao.selectOne("srch.info.myVideoCnt",rowBox));
				reciveBox.add(rowBox);
			});
		}else if("video".equals(box.nvl("type"))) { //video
			int totCnt = dao.selectOne("srch.info.videoCnt", box);
			if(totCnt <= 0) {
				throw new BizException("E107", new String[] {"해당 비디오가"}); // {0} 존재하지 않습니다.
			}
			paginate.init(box, totCnt);
			List<Box> resultList =  dao.selectList("srch.info.videoList", box);
			for(Box rowBox: resultList) {
				rowBox.put("videoActive", rowBox.nvl("videoActive"));
				rowBox.put("videoId", rowBox.nvl("id"));
				if("true".equals(rowBox.nvl("videoActive"))) {
					rowBox.put("userId", box.getBox("sBox").nvl("id"));
					rowBox.put("video", dao.selectOne("srch.info.videoView", rowBox));
					rowBox.put("user", dao.selectOne("srch.info.userView", rowBox));
					rowBox.getBox("user").put("pushNotification", dao.selectOne("srch.info.pushNotificationView", rowBox));
					rowBox.put("sound", dao.selectOne("srch.info.soundView", rowBox));
					reciveBox.add(rowBox);
				}
			}
		}else if("hashtag".equals(box.get("type"))) { //hashtag
			int cnt = dao.selectOne("srch.info.hashtagSrchListCnt", box);
			if(cnt == 0) {
				throw new BizException("E107", new String[] {"해당 해시코드가"}); // {0} 존재하지 않습니다.
			}
			paginate.init(box,cnt);

			List<Box> hashList = dao.selectList("srch.info.hashtagSrchList", box);

			for(Box data : hashList) {
				box.put("hashId", data.get("id"));
				int views = dao.selectOne("srch.info.hashtagSrchViewsCnt", box);
				int videoCnt = dao.selectOne("srch.info.hashtagSrchVideoCnt", box);
				int favourite = dao.selectOne("srch.info.hashtagSrchFavourite", box);

				data.put("views", views);
				data.put("videos_count", videoCnt);
				data.put("favourite", favourite);
			}
			return hashList;
		}

//		MsgUtil.getMsg("S200"); // 성공 코드
		return reciveBox;
	}

	/**
	 * @Method Name : bennerAndHashList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> hashtagAllList(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
//		if (box.getBox("sBox") != null ) {
//			box.put("userId", box.getBox("sBox").nvl("id")); // session userId값 세팅
//		}

		int resCnt = dao.selectOne("srch.info.hashCnt", box);
		if(resCnt == 0) {
			throw new BizException("E107", new String[] {"검색 내역이"}); // {0} 존재하지 않습니다.
		}

		paginate.init(box, resCnt);
		List<Box> hashAllList = dao.selectList("srch.info.hashList",box);
//		List<Box> hashList = new ArrayList<Box>();

		for(Box rowBox:hashAllList) {
			rowBox.put("sBox", SessionUtil.getUserData());
			List<Box> videoList = dao.selectList("srch.info.srcVideoList",rowBox);

			if(videoList.size() != 0) {
				for(Box data : videoList) {
					Box hash = new Box();
					hash.put("name", rowBox.nvl("name"));
					hash.put("id", rowBox.nvl("id"));

					Box hVideo= new Box();
					hVideo.put("video_id", data.nvl("id"));
					hVideo.put("hashtag_id", rowBox.nvl("id"));
					hVideo.put("id", data.nvl("hvId"));

					Box user= new Box();
					user.put("id", data.nvl("userId"));
					user.put("username", data.nvl("username"));
					user.put("email", data.nvl("email"));
					user.put("phone", data.nvl("phone"));
					user.put("active", data.nvl("active"));
					user.put("active_reason", data.nvl("activeReason"));
					user.put("active_reason_detail", data.nvl("activeReasonDetail"));
					user.put("role", data.nvl("role"));
					user.put("point", data.nvl("point"));
					user.put("grade", data.nvl("grade"));
					user.put("first_name", data.nvl("firstName"));
					user.put("last_name", data.nvl("lastName"));
					user.put("gender", data.nvl("gender"));
					user.put("bio", data.nvl("bio"));
					user.put("profile_pic", data.nvl("profilePic"));
					user.put("profile_pic_small", data.nvl("profilePicSmall"));
					user.put("reg_dt", data.nvl("userRegDt"));

					//없는거세팅
					user.put("PrivacySetting", new Box());

					Box noti= new Box();
					noti.put("id", data.nvl("pnId"));
					noti.put("likes", data.nvl("likes"));
					noti.put("comments", data.nvl("comments"));
					noti.put("new_followers", data.nvl("newFollowers"));
					noti.put("mentions", data.nvl("mentions"));
					noti.put("direct_messages", data.nvl("directMessages"));
					noti.put("video_updates", data.nvl("videoUpdates"));
					noti.put("post_update", data.nvl("postUpdate"));
					noti.put("event", data.nvl("event"));
					user.put("PushNotification", noti);

					data.put("user", user);	//유저
					data.put("hashtag", hash); // 해시코드 세팅
					data.put("hashtagVideo", hVideo); // 해시비디오 세팅

				}
				rowBox.put("Video", videoList);
			}

			rowBox.put("views", rowBox.nvl("totalViews")); // 전체 영상 views cnt
		}
		//MsgUtil.getMsg("S200"); // 성공  코드
		return hashAllList;
	}

	/**
	 * @Method Name : hashtagDetailList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> hashtagDetailList(Box box) throws Exception {
		List<Box> resBoxList = new ArrayList<>();
		box.put("sBox", SessionUtil.getUserData());
		if (box.getBox("sBox") != null ) {
			box.put("userId", box.getBox("sBox").nvl("id")); // session userId값 세팅
		}
		List<Box> resultList = dao.selectList("srch.info.hashtagDetailList",box);
		int totalCnt = resultList.size();
		if(resultList.size() <= 0) {
			throw new BizException("E107", new String[] {"해당 해시코드 영상이"}); // {0} 존재하지 않습니다.
		}
		for(Box rowBox : resultList) {
			rowBox.put("hashtagId", rowBox.nvl("hashtagId"));// 해시테그id 셋팅
			rowBox.put("videoId", rowBox.nvl("videoId"));// 비디오id 셋팅
			rowBox.put("userId", rowBox.nvl("userId"));// 사용자id 셋팅
			rowBox.put("myUserId", box.nvl("userId"));// 사용자id 셋팅
			rowBox.put("hashtag", dao.selectOne("srch.info.hashtagView", rowBox)); // 해시테그 정보조회
			rowBox.put("hashtagVideo", dao.selectOne("srch.info.videotagView", rowBox)); // 해시테그_video 정보조회
			rowBox.put("video", dao.selectOne("srch.info.videoInfoView", rowBox)); // video 정보조회
			rowBox.getBox("video").put("user", dao.selectOne("srch.info.userView", rowBox));// video box 안에 있는 user 정보 조회
			rowBox.getBox("video").put("sound", dao.selectOne("srch.info.soundView", rowBox));// video box 안에 있는 sound 정보 조회
			rowBox.getBox("video").getBox("user").put("pushNotification", dao.selectOne("srch.info.pushNotificationView", rowBox));// video > user box 안에 있는 pushNotification 정보 조회
//			rowBox.getBox("video").getBox("user").put("privacySetting", dao.selectList("srch.info.privacySetList"));// video > user box 안에 있는 privacySetting 정보 조회
			resBoxList.add(rowBox);
		}
			Box cntBox = new Box();
			cntBox.put("videosCount", totalCnt);
			resBoxList.add(resultList.size(), cntBox);
//			MsgUtil.getMsg("S200"); // 성공 코드
		return resBoxList;
	}

	/**
	 * @Method Name : hashfavourSave ( 북마크 )
	 * @param box
	 * @return
	 */
	public Box hashfavourSave(Box box) {
		if(session.getAttribute(CmnConst.SES_USER_ID) == null || "".equals(session.getAttribute(CmnConst.SES_USER_ID))) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		Box resBox = new Box();
		box.put("hashtagId", box.get("hashtag_id"));
		box.put("sBox", SessionUtil.getUserData());
		if (box.getBox("sBox") != null ) {
			box.put("userId", box.getBox("sBox").nvl("id")); // session userId값 세팅
		}
		if (box.getBox("sBox") == null || box.getBox("sBox").isEmpty()) {
			throw new BizException("E101"); // 로그인 정보가 존재하지 않습니다.
		}
		int resultCnt = dao.selectOne("srch.info.favouriteChk",box);
		if (resultCnt <= 0) {
			dao.insert("srch.info.favouriteInsert",box);// 북마크 추가
			Box hashtagBox = dao.selectOne("srch.info.favouriteView", box); // hashtagFavourite 에서 추가된 정보 조회
			resBox.put("hashtagFavourite", hashtagBox);
			resBox.put("Hashtag", dao.selectOne("srch.info.hashtagView", hashtagBox));
			resBox.put("user", dao.selectOne("srch.info.userView", hashtagBox));
		} else if (resultCnt > 0) {
			dao.delete("srch.info.favouriteDel",box);// 북마크 삭제
			resBox.put("msg", "unFavourite");
		}
//		MsgUtil.getMsg("S200"); // 성공 코드
		return resBox;
	}

//	/**
//	 * @Method Name : userHashtagList (사용자 찾기)
//	 * @param box
//	 * @return
//	 */
//	public List<Box> userHashtagList(Box box) {
//		List<Box> resBox = new ArrayList<>();
//		resBox = dao.selectList("srch.info.userHashtagList", box);
//		return resBox;
//	}
//
//	/**
//	 * @Method Name : showPeperVideoList
//	 * @param box
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Box> showPeperVideoList(Box box) throws Exception {
//		List<Box> resBox = new ArrayList<>();
//		int totCnt = dao.selectOne("srch.info.videoCnt", box);
//		if(totCnt <= 0) {
//			throw new BizException("E107", new String[] {"해당 비디오가"}); // {0} 존재하지 않습니다.
//		}
//		paginate.init(box, totCnt);
//		List<Box> resultList =  dao.selectList("srch.info.videoList", box);
//		for(Box rowBox: resultList) {
//			rowBox.put("videoActive", rowBox.nvl("videoActive"));
//			rowBox.put("videoId", rowBox.nvl("id"));
//			rowBox.put("userId", rowBox.nvl("userId"));
//			if("true".equals(rowBox.nvl("videoActive"))) {
//				rowBox.put("video", dao.selectOne("srch.info.videoView", rowBox));
//				rowBox.getBox("video").put("like", dao.selectOne("srch.info.videoView", rowBox));
//				rowBox.getBox("video").put("favourite", dao.selectOne("srch.info.videoView", rowBox));
//				rowBox.getBox("video").put("comment_count", dao.selectOne("srch.info.videoView", rowBox));
//				rowBox.getBox("video").put("like_count", dao.selectOne("srch.info.videoView", rowBox));
//
//				rowBox.put("user", dao.selectOne("srch.info.userView", rowBox));
//				rowBox.getBox("user").put("pushNotification", dao.selectOne("srch.info.pushNotificationView", rowBox));
//				rowBox.put("sound", dao.selectOne("srch.info.soundView", rowBox));
//				resBox.add(rowBox);
//			}
//		}
//		return resBox;
//	}
}
