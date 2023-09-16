package kr.aipeppers.pep.ui.showpepper.service;

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
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.core.util.JsonUtil;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.crypto.AutoCrypto;
import kr.aipeppers.pep.core.util.mask.FilterUtil;
import kr.aipeppers.pep.core.util.noti.PushNotification;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import kr.aipeppers.pep.ui.lib.domain.ReqPushNotiDto;
import kr.aipeppers.pep.ui.lib.service.CmnService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ShowPepperService {

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	private Paginate paginate;

	@Autowired
	CmnService cmnService;

	/**
	 * @Method Name : showPeperVideoList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> showPeperVideoList(Box box) throws Exception {
		List<Box> resBox = new ArrayList<>();
		List<Box> resultList = new ArrayList<>();
		box.put("pageUnit", 20);
		if (SessionUtil.getUserData() != null) {
			box.put("sBox", SessionUtil.getUserData());
		} else {
			box.put("chkUser", "N"); //db 에서 이 값을 가지고 user 정보 가 없으면 나머지 조건들 누락 시킴
		}
		int totalCnt = dao.selectOne("showpepper.info.videoCnt", box); // showpepper cnt
		paginate.init(box, totalCnt); // 페이징 처리
		resultList = dao.selectList("showpepper.info.videoList", box); // showpepper list 조회
		for(Box innerBox : resultList ) {
			List<Box> emptyBox = new ArrayList<Box>();
			innerBox.put("myUserId", innerBox.nvl("userId"));
			Box inerUsrBox = dao.selectOne("showpepper.info.userView", innerBox);
			innerBox.put("user", inerUsrBox); // 게시글 올린 사용자의 user 정보 조회
			if (inerUsrBox != null) {
				innerBox.getBox("user").put("pushNotification", dao.selectOne("showpepper.info.pushNotificationView", innerBox));// video 게시글을 올린 user 의 user_id 로 pushNotification 정보 조회
			}
			innerBox.getBox("user").put("privacySetting",emptyBox);
			if (SessionUtil.getUserData() != null ) { // follow check
				Box followerBox = new Box(); // 내가 그사람을
				followerBox.put("sendU", box.getBox("sBox").nvl("id"));
				followerBox.put("receiveU", innerBox.nvl("userId"));
				int followingChk = dao.selectOne("showpepper.info.followingChk" , followerBox);
				Box followingBox = new Box(); // 그사람이 나를
				followingBox.put("sendU", innerBox.nvl("userId"));
				followingBox.put("receiveU",  box.getBox("sBox").nvl("id"));
				int followerChk = dao.selectOne("showpepper.info.followerChk" , followingBox);
				if (followingChk >0 && followerChk > 0) {
					innerBox.getBox("user").put("button", "Friends"); // 둘다 follow
				} else if (followingChk < 1 && followerChk > 0) {
					innerBox.getBox("user").put("button", "following"); // 내가 그사람을
				} else if (followingChk > 0 ) {
					innerBox.getBox("user").put("button", "follow back"); // 그사람이 나를
				} else {
					innerBox.getBox("user").put("button", "follow"); // 둘다 x
				}
			} else {
				innerBox.getBox("user").put("button", "follow"); // 로그인 정보가 없을 시 무조건 follow
			}
			innerBox.put("videoId", innerBox.nvl("id"));
			innerBox.put("video", dao.selectOne("showpepper.info.videoInfoView", innerBox)); // 해당 비디오의 정보조회
			innerBox.put("sound", dao.selectOne("showpepper.info.soundView", innerBox)); // 사운드 정보 조회
			resBox.add(innerBox);
		}
		return resBox;
	}

	/**
	 * @Method Name : showPepperVideoSave
	 * @param box
	 * @return
	 */
	public Box showPepperVideoSave(Box box) {
		Box resBox = new Box();
		int resultInsert = 0;

		box.put("videoId", box.nvl("video_id")); // userId  세팅
		if (SessionUtil.getUserData() != null) { // 로그인 시
			box.put("sBox", SessionUtil.getUserData());
			box.put("myUserId", box.getBox("sBox").nvl("id")); // userId  세팅
			box.put("deviceId", box.nvl("device_id"));
			resBox.put("user", dao.selectOne("showpepper.info.userView", box));
			Box resultBox = dao.selectOne("showpepper.info.watchVideoInfo",box);
			if(resultBox == null ) {
				dao.insert("showpepper.info.watchVideoInsert",box); // 최초 1번 insert
				dao.update("showpepper.info.videoViewCntUpdate", box);
			}
			resBox.put("videoWatch", dao.selectOne("showpepper.info.watchVideoInfo",box));
			resBox.put("video", dao.selectOne("showpepper.info.videoInfoView",box));
			resBox.put("device", dao.selectOne("showpepper.info.deivceView",box));
			if(resBox.getBox("video") == null ) {
				throw new BizException("E137" , new String[] {"영상을"}); // {0} 찾을 수 없습니다.
			}
			box.put("userId", resBox.getBox("video").nvl("userId"));// 비디오 등록 user 아이디
			resBox.getBox("video").put("user", dao.selectOne("showpepper.info.crdtUserInfo",box));
		} else if (SessionUtil.getUserData() == null) { // 비 로그인 시
			Box videoBox = dao.selectOne("showpepper.info.videoInfoView",box);
			if( videoBox == null ) {
				throw new BizException("E137" , new String[] {"영상을"}); // {0} 찾을 수 없습니다.
			}
			resBox.put("video", videoBox);
			box.put("userId", resBox.getBox("video").nvl("userId"));// 비디오 등록 user 아이디
			resBox.getBox("video").put("user", dao.selectOne("showpepper.info.crdtUserInfo",box));
		}
		return resBox;
	}

	/**
	 * @Method Name : showSoundsList
	 * @param box
	 * @return
	 */
	public List<Box> showSoundsList(Box box) { // sound 정 보 가져오는 api
		List<Box> resBox = new ArrayList<Box>();
		resBox = dao.selectList("showpepper.info.showSoundsList", box);
		return resBox;
	}

	/**
	 * @Method Name : videoCommentInsert
	 * @param box
	 * @return
	 */
	public Box videoCommentInsert(Box box) { //showpepper  의 댓글, 대댓글, 대대댓글 달기
		Box resBox = new Box();
		String filter = cmnService.setFilterView();
		Boolean result = FilterUtil.filterText(box.nvl("comment"),filter); // video description 욕설 필터
		if (result == true ) {
			Boolean resultVal = false;
			box.put("sBox", SessionUtil.getUserData());
			box.put("videoId", box.nvl("video_id")); // userId  세팅
			box.put("commentId", box.nvl("comment_id")); // commentId  세팅
			box.put("replyCommentId", box.nvl("reply_comment_id")); // replyCommentId  세팅

			if("".equals(box.nvl("comment_id"))) {// 댓글일 경우
				log.debug("댓글");
				box.put("CommentId", null); // CommentId  null 세팅
				box.put("replyCommentId", null); // replyCommentId  null 세팅
				resultVal = contentsChk(box,"F"); // validation chk 로직
				if ( resultVal == false ) {
					dao.insert("showpepper.info.videoCommInsert", box);
					cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_VIDEO_CMT , CmnConst.PUSH_TYPE_VIDEO_CMN , box.nvl("videoId") , "", "", "", "");
				}
				resBox.put("user", dao.selectOne("showpepper.info.userInfoView", box));
				resBox.put("video", dao.selectOne("showpepper.info.videoInfoView", box));
				resBox.put("videoComment", dao.selectOne("showpepper.info.newVideoCommentView", box));
				resBox.put("commentId",resBox.getBox("videoComment").nvl("id"));
				resBox.put("VideoCommentReply", dao.selectOne("showpepper.info.videoReplyCommList", box));
			} else if(!"".equals(box.nvl("comment_id")) && box.nvl("comment_id") != null && "".equals(box.nvl("reply_comment_id")) || box.nvl("reply_comment_id") == null) {// 대댓글일 경우
				log.debug("대댓글");
				box.put("replyCommentId", null); // replyCommentId  세팅
				resultVal = contentsChk(box,"S"); // validation chk 로직
				if ( resultVal == false ) {
					dao.insert("showpepper.info.videoCommReplyInsert", box);
					cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_REPLY_CMT , CmnConst.PUSH_TYPE_VIDEO_CMN , box.nvl("videoId") , "", box.nvl("comment_id"), "", "");
					resBox.put("videoCommentReply", dao.selectOne("showpepper.info.replyCommView", box));
					resBox.put("resultMsg", "댓글이 작성 되었습니다.");
				}
			} else if(!"".equals(box.nvl("comment_id")) && box.nvl("comment_id") != null && !"".equals(box.nvl("reply_comment_id")) && box.nvl("reply_comment_id") != null) {// 대대댓글일 경우
				log.debug("대대댓글");
				resultVal = contentsChk(box,"T"); // validation chk 로직
				if ( resultVal == false ) {
					dao.insert("showpepper.info.videoCommReplyInsert", box);
					cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_REPLY_CMT , CmnConst.PUSH_TYPE_VIDEO_CMN , box.nvl("videoId") , "", box.nvl("comment_id"), box.nvl("reply_comment_id"), "");
					resBox.put("resultMsg", "댓글이 작성 되었습니다.");
				}
			}
		}
		return resBox;
	}

	/**
	 * @Method Name : videoCommentDel
	 * @param box
	 * @return
	 */
	public Box videoCommentDel(Box box) {
		Box resBox = new Box();
		int result = 0 ;
		box.put("sBox", SessionUtil.getUserData());
		if (box.getBox("sBox") == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		box.put("videoId", box.nvl("video_id")); // userId  세팅
		box.put("commentId", box.nvl("comment_id")); // commentId  세팅
		box.put("replyCommentId", box.nvl("reply_comment_id")); // userId  세팅
		if ("".equals(box.nvl("replyCommentId"))) {
			result = dao.selectOne("showpepper.info.videoCommChk", box);
		} else {
			result = dao.selectOne("showpepper.info.videoReCommChk", box);
		}

		if (result == 0 ) {
			throw new BizException("E137", new String[] {"댓글을"}); //	{0} 찾을 수 없습니다.
		}

		if(!"".equals(box.nvl("comment_id")) && box.nvl("comment_id") != null) {// 댓글일 경우
			dao.update("showpepper.info.videoCommDel", box);
		} else {
			dao.update("showpepper.info.videoReplyCommDel", box);
		}
		return resBox;
	}

	/**
	 * @Method Name : contentsChk
	 * @param box
	 * @param div
	 * @return
	 */
	public Boolean contentsChk(Box box, String div) {
		Boolean result = false ;
		if ("F".equals(div)) {
			int relSeceUserVal = dao.selectOne("showpepper.info.secessUsrChk" , box); // 탈퇴 회원 체크
			int relVal = dao.selectOne("showpepper.info.delVideoChk", box); // 비디오 삭제되었는지 체크
			result = (relSeceUserVal == 0 && relVal == 0) ? false : true;
			if (result) {
				if(relSeceUserVal > 0 ) { // 탈퇴한 회원
					throw new BizException("E121"); // 탈퇴한 회원입니다.
				}
				if(relVal > 0 ) { // 게시물 삭제
					throw new BizException("E137", new String[] {"Video를"}); // {0} 찾을 수 없습니다.
				}
			}
		}

		if ("S".equals(div)) {
			int relSeceUserVal = dao.selectOne("showpepper.info.secessUsrChk" , box); // 탈퇴 회원 체크
			int relVal = dao.selectOne("showpepper.info.delVideoChk", box); // 비디오 삭제되었는지 체크
			int blockUsrChk = dao.selectOne("showpepper.info.blockUsrChk", box); // 나를 차단한 유저인지 체크
			int commDelChk = dao.selectOne("showpepper.info.commDelChk", box); // 상위 댓글이 삭제되었는지 체크
			result = (relSeceUserVal == 0 && relVal == 0 && blockUsrChk == 0 && commDelChk == 0) ? false : true;
			log.debug("result: {}", result);
			if ( result == true ) {
				if(relSeceUserVal > 0 ) { // 탈퇴한 회원
					throw new BizException("E138", new String[] {"탈퇴한 사용자의"}); // {0} 댓글입니다.
				}
				if(relVal > 0 ) { // 게시물 삭제
					throw new BizException("E137", new String[] {"Video를"}); // {0} 찾을 수 없습니다.
				}
				if(blockUsrChk > 0 ) { // 블럭유저 일 경우
					throw new BizException("E124"); //  찾을 수 없는 사용자 입니다.\n메인 페이지로 이동합니다.
				}
				if(commDelChk > 0 ) { // 삭제된 댓글 일 경우
					throw new BizException("E125", new String[] {"댓글이"}); // {0} 삭제 되었습니다.
				}
			}
		}

		if ("T".equals(div)) { // 파라미터 : comment_id / video_id / user_id /
			int relSeceUserVal = dao.selectOne("showpepper.info.secessUsrChk" , box); // 탈퇴 회원 체크 - video_id / comment_id / replyCommentId
			int relVal = dao.selectOne("showpepper.info.delVideoChk", box); // 비디오 삭제되었는지 체크 - videoId
			int blockUsrChk = dao.selectOne("showpepper.info.blockUsrChk", box); // 나를 차단 유저인지 체크 - userId
			int commDelChk = dao.selectOne("showpepper.info.commDelChk", box); // 상위 댓글이 삭제되었는지 체크 videoId , userId
			int replyCommDelChk = dao.selectOne("showpepper.info.replyCommDelChk", box); // 상위 대댓글이 삭제되었는지 체크 commentId( tb_video_comment 태이블 ID 값 / replyCommentId )
			result = (relSeceUserVal == 0 && relVal == 0 && blockUsrChk == 0 && commDelChk == 0 && replyCommDelChk == 0) ? false : true;
			if ( result == true ) {
				if(relSeceUserVal > 0 ) { // 탈퇴한 회원
					throw new BizException("E138", new String[] {"탈퇴한 사용자의"}); // {0} 댓글입니다.
				}
				if(relVal > 0 ) { // 게시물 삭제
					throw new BizException("E137", new String[] {"Video를"}); // {0} 찾을 수 없습니다.
				}
				if(blockUsrChk > 0 ) { // 블럭유저 일 경우
					throw new BizException("E124"); //  찾을 수 없는 사용자 입니다.\n메인 페이지로 이동합니다.
				}
				if(commDelChk > 0 ) { // 댓글이 삭제되었을 경우
					throw new BizException("E125", new String[] {"댓글이"}); // {0} 삭제 되었습니다.
				}
				if( replyCommDelChk > 0) { // 대댓글이 삭제되었을 경우
					throw new BizException("E125", new String[] {"댓글이"}); // {0} 삭제 되었습니다.
				}
			}
		}
		return result;
	}

	/**
	 * @Method Name : showVideoCommentsList
	 * @param box
	 * @return
	 */
	public List<Box> showVideoCommentsList(Box box) {
		List<Box> resBox = new ArrayList<>();
		List<Box> resSubBox = new ArrayList<>();
		box.put("sBox", SessionUtil.getUserData());
		box.put("videoId", box.nvl("video_id")); // userId  세팅
		resBox =  dao.selectList("showpepper.info.videoCommList",box);// 비디오 ID 에 대한 댓글 조회
		for(Box innerBox : resBox) { // 댓글이 삭제 되었을 때는 "삭제된 댓글입니다" 로 CONTENT를 변경
			box.put("commentId", innerBox.nvl("id"));
			box.put("commUserId", innerBox.nvl("userId"));
			innerBox.put("user", dao.selectOne("showpepper.info.videoCmmWritUsr", box)); // 댓글 작성자 정보
			innerBox.put("videoCommentReply", dao.selectList("showpepper.info.videoReplyCommList", box)); // 대댓글,대대댓글 list 데이터 셋
			for(Box nonBox : innerBox.getList("videoCommentReply")) {
				nonBox.put("commUserId", nonBox.nvl("userId"));
				nonBox.put("user", dao.selectOne("showpepper.info.videoCmmWritUsr", nonBox));
			}
		}
		// 해당 댓글의 LIKE 여부와 댓글의 좋아요 수 별도로 조회 후 셋팅
		return resBox;
	}

	/**
	 * @Method Name : likeCommenInsert
	 * @param box
	 * @return
	 */
	public int likeCommenSave(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		box.put("commentId", box.nvl("comment_id")); // commentId  세팅

		int view = dao.selectOne("showpepper.info.likeCommenChk", box);
		if(view > 0) {
			dao.delete("showpepper.info.videoCommUnLike", box);
		}else if (view == 0) {
			dao.insert("showpepper.info.videoCommLike", box);
		}
		Box infoBox = dao.selectOne("showpepper.info.commentIsLike",box);
		resBox.put("isLike",infoBox.nvl("isLike"));
		resBox.put("likeCount",infoBox.nvl("likeCount"));
		resBox.put("listCnt", view);
		return 0;
	}

	/**
	 * @Method Name : likeCommentReplyInsert
	 * @param box
	 * @return
	 */
	public Box likeCommentReplySave(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		box.put("commentId", box.nvl("comment_id")); // commentId  세팅
		int view = dao.selectOne("showpepper.info.likeReplyCommenChk", box);
		if(view > 0) {
			dao.delete("showpepper.info.videoReplyCommUnLike", box);
		}else if (view == 0) {
			dao.insert("showpepper.info.videoReplyCommLike", box);
		}
		Box infoBox = dao.selectOne("showpepper.info.commentReplyIsLike",box);
		resBox.put("isLike",infoBox.nvl("isLike"));
		resBox.put("likeCount",infoBox.nvl("likeCount"));
		resBox.put("listCnt", view);
		return resBox;
	}

	/**
	 * @Method Name : likeVideo
	 * @param box
	 * @return
	 */
	public Box likeVideo(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		box.put("videoId", box.nvl("video_id")); // videoId  세팅
		int videoCnt = dao.selectOne("videoChk", box);
		if (videoCnt > 0) {
			int view = dao.selectOne("showpepper.info.likeVideoChk", box);
			if(view > 0) {
				dao.delete("showpepper.info.videoUnLike", box);
			}else if (view == 0) {
				dao.insert("showpepper.info.videoLike", box);
				resBox.put("video", dao.selectOne("showpepper.info.videoInfoView", box));
			}
		} else {
			throw new BizException("E107", new String[] {"비디오가"}); //{0}  존재하지 않습니다.
		}
		return resBox;
	}

	/**
	 * @Method Name : myShowPepperInsert
	 * @param box
	 * @return
	 */
	public int myShowPepperInsert(Box box) {
		box.put("sBox", SessionUtil.getUserData());
//		String filter = cmnService.setFilterView();
//		Boolean result = FilterUtil.filterText(box.nvl("description"),filter); // description filter
//		if (result == true ) {
//			Box hashtagBox = hashtagInsert(box); // 해시태그 저장 ( 같은게 있으면 저장 )
//			dao.insert("showpepper.info.myShowPepperInsert",box); // 비디오 insert
//			Box videoBox = dao.selectOne("showpepper.info.newVideoView", box); // 방금 등록한 영상 조회
//			videoBox.put("hashArr", hashtagBox); // 해시 태그 value setting
//			videoHashtagInsert(videoBox);// 해시 태그 와 비디오 ( tb_hashtag_video) 에 데이터 매핑
//			// 영상 올린 사람이 선수이면 push 진행
//			if ("player".equals(box.getBox("sBox").nvl("role"))) {
//			List<Box> playerFollowList = dao.selectList("showpepper.info.playerFollowList", box);// 선수이면 follower 조회
//				for (Box innerBox : playerFollowList) {
//					cmnService.pushSend(innerBox.nvl("f"), CmnConst.PUSH_NEW_VIDEO , CmnConst.PUSH_TYPE_VIDEO_UPDATE , videoBox.nvl("id") , "", "", "", ""); // 비디오 업로드 후 push
//				}
//			}
			 List<Box> result = (List<Box>) JsonUtil.toObject(box.nvl("hashtags_json"), Map.class);

			 log.debug("aaaaaaaaaaaaaaaaaaaaaaaa: {}", result.getClass());
//		}

		return 1;
	}

	/**
	 * @Method Name : videoHashtagInsert
	 * @param box
	 */
	public void videoHashtagInsert(Box box) {
		Box resBox = new Box();
		for(String hashtag : box.getBox("hashArr").getArry("hashtag")) {
			if(hashtag.replaceAll(" ", "").contains("#") == true) {
				Box innerBox = new Box();
				innerBox.put("hashtag", hashtag);
				Box resultBox = dao.selectOne("showpepper.info.hashtagIdView", innerBox); //
				resultBox.put("videoId", box.nvl("id"));
				resultBox.put("hashtag", resultBox.nvl("id"));
				resultBox.put("sBox", SessionUtil.getUserData());
				log.debug("resultBox: {}", resultBox);
				dao.insert("showpepper.info.videoHashtagInsert", resultBox);
			}
		}
		//resBox = dao.selectOne("showpepper.info.videoHashtagView", box);
	}

	/**
	 * @Method Name : hashtagInsert
	 * @param box
	 */
	public Box hashtagInsert(Box box) {
		Box returnBox = new Box();
		String[] values = box.nvl("description").split(",");
		for (String hashtag : values) {
			if(hashtag.replaceAll(" ", "").contains("#") == true) {
				int result = dao.selectOne("showpepper.info.hashtagChk", hashtag);
				if (result == 0) {
					box.put("hashtag", hashtag);
					dao.insert("showpepper.info.hashtagInsert",box);
				}
			}
		}
		returnBox.put("hashtag", values);
		return returnBox;
	}
	/**
	 * @Method Name : addVideoFavouriteSave
	 * @param box
	 * @return
	 */
	public Box addVideoFavouriteSave(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		box.put("videoId", box.nvl("video_id"));
		int favoriteCnt = dao.selectOne("showpepper.info.addVideoFavouriteSave", box);

		if(favoriteCnt > 0) {
			dao.delete("showpepper.info.deleteFavourite" , box);
		} else {
			Box emptyBox = new Box();
			log.debug("userInfo: {}", box.getBox("sBox"));
			dao.insert("showpepper.info.insertFavourite" , box);
			resBox.put("videoFavourite", dao.selectOne("showpepper.info.videoFavouriteView",box));
			resBox.put("video", dao.selectOne("showpepper.info.videoInfoView",box));
			resBox.put("user", dao.selectOne("showpepper.info.userInfoView",box));
			box.put("myUserId", box.getBox("sBox").nvl("id"));
			resBox.put("PushNotification", dao.selectOne("showpepper.info.pushNotificationView",box));
			resBox.put("PrivacySetting",emptyBox);
		}

		return resBox;
	}
}
