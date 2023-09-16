package kr.aipeppers.pep.ui.mypage.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.ConfigUtil;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.crypto.AutoCrypto;
import kr.aipeppers.pep.core.util.crypto.Base64Util;
import kr.aipeppers.pep.core.util.crypto.DigestUtil;
import kr.aipeppers.pep.core.util.file.FileUtil;
import kr.aipeppers.pep.core.util.mask.MaskUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import kr.aipeppers.pep.ui.lib.service.CmnRestService;
import kr.aipeppers.pep.ui.lib.service.CmnService;
import kr.aipeppers.pep.ui.mypage.controller.MyinfoController.ReqUIMPMI047Dto;
import kr.aipeppers.pep.ui.mypage.controller.MyinfoController.ReqUIMPMI049Dto;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 1
 *
 */
@Slf4j
@Service
public class MyinfoService {

	@Autowired
	protected SqlSessionTemplate dao;


	@Autowired
	private Paginate paginate;

	@Autowired
	CmnService cmnService;

//	@Autowired
//	private ValidatorUtil validatorUtil;

//	@Autowired
//	private Paginate paginate;

	/**
	 * 마이페이지 기본 데이터 조회
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box userView(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		if("".equals(box.nvl("other_user_id"))) {
			box.put("otherUserId", box.getBox("sBox").nvl("id"));
		} else {
			box.put("otherUserId", AutoCrypto.aesDecrypt(box.nvl("other_user_id")));
		}
		Box userBox = dao.selectOne("mypage.myinfo.userView", box);
		if(userBox == null || userBox.isEmpty()) {
			throw new BizException("E100"); // 사용자 정보가 존재하지 않습니다.
		}
		userBox.put("email", MaskUtil.maskEmail(AutoCrypto.seedDecrypt(userBox.nvl("email"))));
		userBox.put("phone", MaskUtil.maskPhoneNumber(AutoCrypto.seedDecrypt(userBox.nvl("phone"))));
		Box resBox = new Box();
		resBox.put("profile", userBox); //내 ID or 선택한 사람의 ID 값 으로 회원 정보 조회
		resBox.put("followingCount", dao.selectOne("mypage.myinfo.followingCnt", box)); //following 전체 cnt setting
		resBox.put("followerCount", dao.selectOne("mypage.myinfo.followerCnt", box)); //follower 전체 cnt setting
		resBox.put("myBoardCount", dao.selectOne("mypage.myinfo.myBoardCnt", box)); //게시글 전체 cnt setting
		resBox.put("favoruiteCount", dao.selectOne("mypage.myinfo.favoruiteCount", box)); //favoruiteCount 전체 cnt setting

		/** isBlock value setting
			flag = 내가 직접 차단 : 1 / 타인에 의한 차단 : 0
			1)그사람이 나를 블럭 했는지 조회 블럭이면 api 끝
			2)내가 그사람을 블럭 했는지
			isBlock value--> 1= 블럭 / 0 = x
		**/
		int myBlock = dao.selectOne("mypage.myinfo.myBlockChk", box); // 내가 차단한 내역
		int otherBlock = dao.selectOne("mypage.myinfo.otherBlockChk", box); // 상대가 나를 차단한 내역
		resBox.put("isBlock", (myBlock == 1 && otherBlock == 1) ? "1" : "0");

		/** button value setting
			1 = 1  ==> Friends
			1 = 0  ==> following
			0 = 1  ==> follow back
			0 = 0  ==> follow
		**/
		int followingCheck = dao.selectOne("mypage.myinfo.followingChk", box); //팔로잉 checking
		int followerCheck = dao.selectOne("mypage.myinfo.followerChk", box); //팔로워 checking
		String buttonStr = "";
		if (followingCheck == 1 && followerCheck == 1) { // 둘다 follow를 했을 때
			buttonStr = "Friends";
		} else if (followingCheck == 1 && followerCheck == 0) { //내가 상대방을 following 했을 때
			buttonStr = "following";
		} else if (followingCheck == 0 && followerCheck == 1) { // 상대방이 나를 following 을 했을 때
			buttonStr = "follow back";
		} else { // 둘다 following 을 안했을 때
			buttonStr = "follow";
		}
		resBox.put("button", buttonStr);
//		log.debug("resBox: {}", resBox);
		return resBox;
	}

	/**
	 * 팔로우/언팔로우
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int followOnOffSave(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		box.put("targetId", AutoCrypto.aesDecrypt(box.nvl("target_id")));
		int blockChk = dao.selectOne("mypage.myinfo.blockUsrChk", box); // block user  인지 확인한다.
		if (blockChk > 0) {
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}
		int userBox  = dao.selectOne("mypage.myinfo.followChk", box); // TODO - 팔로우 언팔로우 여부를 확인한다
		if (userBox <= 0) {
			dao.insert("mypage.myinfo.followInsert", box);
			cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_FOLLOW  , CmnConst.PUSH_TYPE_FOLLOW , "" , "", "", "" , box.nvl("targetId"));
			return 1;
		} else {
			return dao.delete("mypage.myinfo.followDelete", box);
		}
	}

	/**
	 * 팔로워 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> followerListView(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		if ("".equals(box.nvl("other_user_id"))) { // srchId 없으면 == 본인 마이페이지 진입 시 본인 id setting
			box.put("other_user_id", box.getBox("sBox").nvl("id"));
		}else {
			box.put("other_user_id", AutoCrypto.aesDecrypt(box.nvl("other_user_id")));

		}
		int block = dao.selectOne("mypage.myinfo.blockChk", box);//유저 블럭 된지 확인
		if(block != 0 ) {//블럭 인 경우 에러 떨구기
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}else {
			return dao.selectList("mypage.myinfo.followerList", box);
		}
	}


	/**
	 * 팔로잉 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */

	public List<Box> followingListView(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		if ("".equals(box.nvl("other_user_id"))) { // srchId 없으면 == 본인 마이페이지 진입 시 본인 id setting
			box.put("other_user_id", box.getBox("sBox").nvl("id"));
		}else {
			box.put("other_user_id", AutoCrypto.aesDecrypt(box.nvl("other_user_id")));
		}
		int block = dao.selectOne("mypage.myinfo.blockChk", box);//유저 블럭 된지 확인
		if(block != 0 ) {//블럭 인 경우 에러 떨구기
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}else {
			return dao.selectList("mypage.myinfo.followingList", box);
		}
	}

	/**
	 * 추천친구
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> referFriendList(Box box) throws Exception {
		List<Box> resBox = new ArrayList<>();
		box.put("sBox", SessionUtil.getUserData());
		List<Box> userList = dao.selectList("mypage.myinfo.referFriendList", box);
//		if(userList.size() <=  0) {
//			userList = dao.selectList("mypage.myinfo.referPlayerList", box);
//		}
		return userList ;
	}

	/**
	 * 선수 추천
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> referPlayerList(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		List<Box> playerList = dao.selectList("mypage.myinfo.referPlayerList", box);
		for (Box innerBox : playerList ) {

			box.put("otherUserId", innerBox.nvl("id"));
			int followingCheck = dao.selectOne("mypage.myinfo.followingChk", box); //팔로잉 checking
			int followerCheck = dao.selectOne("mypage.myinfo.followerChk", box); //팔로워 checking
			Boolean followInfo = false;
			if (followingCheck == 1 && followerCheck == 1) { // 둘다 follow를 했을 때
				followInfo = true;
			} else if (followingCheck == 1 && followerCheck == 0) { //내가 상대방을 following 했을 때
				followInfo = true;
			} else if (followingCheck == 0 && followerCheck == 1) { // 상대방이 나를 following 을 했을 때
				followInfo = true;
			} else { // 둘다 following 을 안했을 때
				followInfo = false;
			}
			innerBox.put("isFollow" , followInfo);

		}
		return playerList;
	}

	/**
	 * @Method Name : showVideoDetailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box showVideoDetailView(Box box) throws Exception {
//		box.put("id", box.nvl("id")); // 이미 앞에서 아이디 값을 받았기 때문에 또다시 box에 담을 필요 없음
		/* 마이페이지 > 쇼페퍼 리스트 --> 내가 선택한 영상의 VIDEO 정보 조회시 해당 게시물이 블럭 유저의 게시글일 경우
		 * 존재하지 않는 게시글 //	E107	{0} 존재하지 않습니다. 라는 에러 메세지 보내는 로직 추가 필요
		 * 마이페이지에서 session 값은 box.put("sBox", SessionUtil.getUserData()); 세팅 후
		 * 쿼리에서는 ex) A.ID = #{sBox.id} 로 로그인 된 user 의 아이디를 사용 함.
		 * 좋아요 카운트 / 좋아요 유무 / 코멘트 카운트 / 작성자 이름 누락 ( showVideoDetailView 쿼리에서 누락된 정보 ) ==> 참고 showpepper pakage 에서 쇼페퍼에 관한 쿼리 확인 필요
		 */
		Box resBox = new Box();
		box.put("videoId", box.nvl("video_id"));
		List<Box> emptyBox= new ArrayList<Box>();
		resBox.put("video", dao.selectOne("mypage.myinfo.showVideoDetailView",box)); // 비디오 디테일 정보 조회
		if( resBox.getBox("video") == null ) {
			throw new BizException("E107", new String[] {"비디오가"}); //{0} 존재하지 않습니다.
		}

		box.put("userId", resBox.getBox("video").nvl("userId")); // 비디오 등록한 사람의 userId 셋업
		resBox.put("user", dao.selectOne("mypage.myinfo.userInfoView",box)); // user 조회 ( 비디오 등록자 의 정보 )
		resBox.getBox("user").put("pushNotification", dao.selectOne("mypage.myinfo.pushNotificationView",box)); // user > pushnotification 조회
		resBox.getBox("user").put("pivacySetting", emptyBox); // user > pushnotification 조회

		if (SessionUtil.getUserData() != null ) { // follow check
			Box followerBox = new Box(); // 내가 그사람을
			followerBox.put("sendU", box.getBox("sBox").nvl("id"));
			followerBox.put("receiveU", box.nvl("userId"));
			log.debug("aaaaaaaaaaaaaaaaaaaaaaaaaa>: {}", followerBox);

			int followingChk = dao.selectOne("mypage.myinfo.followingChk" , followerBox);
			Box followingBox = new Box(); // 그사람이 나를
			followingBox.put("sendU", box.nvl("userId"));
			followingBox.put("receiveU",  box.getBox("sBox").nvl("id"));
			int followerChk = dao.selectOne("mypage.myinfo.followerChk" , followingBox);
			if (followingChk >0 && followerChk > 0) {
				resBox.getBox("user").put("button", "Friends"); // 둘다 follow
			} else if (followingChk < 1 && followerChk > 0) {
				resBox.getBox("user").put("button", "following"); // 내가 그사람을
			} else if (followingChk > 0 ) {
				resBox.getBox("user").put("button", "follow back"); // 그사람이 나를
			} else {
				resBox.getBox("user").put("button", "follow"); // 둘다 x
			}
		} else {
			resBox.getBox("user").put("button", "follow"); // 로그인 정보가 없을 시 무조건 follow
		}
		resBox.put("sound", dao.selectOne("mypage.myinfo.soundView", resBox.getBox("video")));// 사운드 아이디 정보 set 필요
		List<Box> commList = dao.selectList("mypage.myinfo.commentList", box);
		if (commList != null) {
			for (Box innerBox : commList) {
				Box userBox = dao.selectOne("mypage.myinfo.userInfoView", innerBox);
				if (userBox != null) {
					innerBox.put("user", userBox);
				}
			}
		}
		resBox.put("videoComment", commList);// 댓글 까지만의 정보 필요
		return resBox;
	}

	/**
	 * @Method Name : showFavouriteVideosList
	 * @param box
	 * @return
	 */
	public List<Box> showFavouriteVideosList(Box box)  throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		List<Box> resBox = new ArrayList<>();

		if("true".equals(box.nvl("again"))) {
			int pageCnt = 18;
			int page = box.getInt("page");

			if(page != 0) {
				pageCnt += 18;
			}
			box.put("pageUnit", page*pageCnt);
			box.put("page", 0);
		}else {
			box.put("pageUnit", 18);
		}

		int totCnt = dao.selectOne("mypage.myinfo.showFavouriteVideosCnt", box);

		if(totCnt == 0) {
			throw new BizException("E107", new String[] {"검색 내역이"}); // {0} 존재하지 않습니다.
		}
		paginate.init(box, totCnt);
		List<Box> keyList = dao.selectList("mypage.myinfo.showFavouriteFavouriteList",box);
		for(Box data:keyList ) {
			data.put("sBox", SessionUtil.getUserData());
			Box innerBox = new Box();
//			Box favourite = data;
			innerBox.put("videoFavourite", data);
			innerBox.put("video", dao.selectOne("mypage.myinfo.showFavouriteVideos",data));
			if (innerBox.getBox("video") != null ) {
				innerBox.getBox("video").put("user", dao.selectOne("mypage.myinfo.userInfoView",data));
				if (innerBox.getBox("video").getBox("user") != null) {
					Box emptyBox = new Box();
					innerBox.getBox("video").getBox("user").put("pushNotification", dao.selectOne("mypage.myinfo.pushNotificationView",data));
					innerBox.getBox("video").getBox("user").put("privacySetting", emptyBox);
					int followingCheck = dao.selectOne("mypage.myinfo.followingChk", box); //팔로잉 checking
					int followerCheck = dao.selectOne("mypage.myinfo.followerChk", box); //팔로워 checking
					String followInfo = "";
					if (followingCheck == 1 && followerCheck == 1) { // 둘다 follow를 했을 때
						followInfo = "Friends";
					} else if (followingCheck == 1 && followerCheck == 0) { //내가 상대방을 following 했을 때
						followInfo = "following";
					} else if (followingCheck == 0 && followerCheck == 1) { // 상대방이 나를 following 을 했을 때
						followInfo = "follow back";
					} else { // 둘다 following 을 안했을 때
						followInfo = "follow";
					}
					innerBox.getBox("video").getBox("user").put("button", followInfo);

				}
			}
			resBox.add(innerBox);
		}

		return resBox;
	}

	/**
	 * @Method Name : myFavouriteVideosList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myFavouriteVideosList(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		if ("".equals(box.nvl("other_user_id"))) {
			box.put("otherUserId", box.getBox("sBox").nvl("id"));
		} else {
			box.put("otherUserId", AutoCrypto.aesDecrypt(box.nvl("other_user_id")));
			int userChk = dao.selectOne("mypage.myinfo.userInfoChk", box); // 탈퇴회원 체크
			if (userChk > 0 ) {
				throw new BizException("E124"); // 찾을 수 없는 사용자 입니다.\n메인 페이지로 이동합니다.
			}
			box.put("id", box.getBox("sBox").nvl("id")); // 블럭체크를 위한 값 세팅
			box.put("srchId", box.get("other_user_id"));
			int blockChk = dao.selectOne("mypage.myinfo.blockChk", box); // 블럭 체크
			if (blockChk  > 0) { // 블럭 , 탈퇴 회원일 경우
				throw new BizException("E124"); // 찾을 수 없는 사용자 입니다.\n메인 페이지로 이동합니다.
			}
		}

		List<Box> resBox = new ArrayList<>();
		int totCnt = dao.selectOne("mypage.myinfo.myFavouriteVideosCnt", box);
		List<Box> emptyBox = new ArrayList<Box>();
		if (box.getBoolean("again")) {
			box.put("pageUnit", box.getInt("page")* 10); // again 이 true 일 경우
		}
		paginate.init(box, totCnt);
		resBox = dao.selectList("mypage.myinfo.myFavouriteVideosList",box);
		for (Box innerBox : resBox ) {
			innerBox.put("video", dao.selectOne("mypage.myinfo.myFavouriteVideosView", innerBox));
			List<String> videoHashBox = dao.selectList("mypage.myinfo.videoHashtagList",innerBox);
			innerBox.getBox("video").put("hashtags", videoHashBox);
			innerBox.put("user", dao.selectOne("mypage.myinfo.userInfoView", innerBox));
			innerBox.getBox("user").put("pushNotification", dao.selectOne("mypage.myinfo.pushNotificationView", innerBox));
			innerBox.put("privacySetting", emptyBox);

//			/** button value setting
//				1 = 1  ==> Friends
//				1 = 0  ==> following
//				0 = 1  ==> follow back
//				0 = 0  ==> follow
//			**/
//			int followingCheck = dao.selectOne("mypage.myinfo.followingChk", box); //팔로잉 checking
//			int followerCheck = dao.selectOne("mypage.myinfo.followerChk", box); //팔로워 checking
//			String buttonStr = "";
//			if (followingCheck == 1 && followerCheck == 1) { // 둘다 follow를 했을 때
//				buttonStr = "Friends";
//			} else if (followingCheck == 1 && followerCheck == 0) { //내가 상대방을 following 했을 때
//				buttonStr = "following";
//			} else if (followingCheck == 0 && followerCheck == 1) { // 상대방이 나를 following 을 했을 때
//				buttonStr = "follow back";
//			} else { // 둘다 following 을 안했을 때
//				buttonStr = "follow";
//			}
//			Box emptyBox = new Box();
//			innerBox.getBox("user").put("button", buttonStr);
//			innerBox.getBox("user").put("likesCount", dao.selectOne("mypage.myinfo.userLikeAllCnt", box));
//			innerBox.getBox("user").put("videoCount", dao.selectOne("mypage.myinfo.videoCntView", box));
//			innerBox.getBox("user").put("privacySetting", emptyBox);
		}

		return resBox;
	}

	/**
	 * @Method Name : myFavouriteHashtagsList
	 * @param box
	 * @return
	 */
	public List<Box> myFavouriteHashtagsList(Box box) throws Exception {
		List<Box> resBox = new ArrayList<Box>();
		box.put("sBox", SessionUtil.getUserData());
		List<Box> myLikedVideosList = dao.selectList("mypage.myinfo.showVideosAgainstHashtagList",box);

		for (Box innerBox : myLikedVideosList) {
			Box reqBox = new Box();
			reqBox.put("hashtag", dao.selectOne("mypage.myinfo.myHashtagInfoView", innerBox));
			reqBox.put("hashtagFavourite", dao.selectOne("mypage.myinfo.hashtagFavouriteView", innerBox));
			reqBox.put("user", dao.selectOne("mypage.myinfo.myUserView", innerBox));

			resBox.add(reqBox);
		}
		log.debug("resBox: {}", resBox);
		return resBox ;
	}

	/**
	 * @Method Name : myCommentsList
	 * @param box
	 * @return
	 */
	public List<Box> myCommentsList(Box box) {
		List<Box> reciveBox = new ArrayList<>();
		box.put("sBox", SessionUtil.getUserData());

		List<Box> resultList = dao.selectList("mypage.myinfo.myCommentsList",box);
		for(Box rowBox: resultList) {
			if (!"".equals(rowBox.nvl("postId"))) {
				rowBox.put("parentContent", dao.selectOne("mypage.myinfo.postInfoView", rowBox));
				Box userBox =  dao.selectOne("mypage.myinfo.comentUserInfo",rowBox.getBox("parentContent").nvl("userId"));
				rowBox.getBox("parentContent").put("user",userBox);
				reciveBox.add(rowBox);
			} else {
				rowBox.put("parentContent", dao.selectOne("mypage.myinfo.videoInfoView", rowBox));
				Box userBox =  dao.selectOne("mypage.myinfo.comentUserInfo",rowBox.getBox("parentContent").nvl("userId"));
				rowBox.getBox("parentContent").put("user",userBox);
				reciveBox.add(rowBox);
			}
		}
		return reciveBox;
	}

	/**
	 * @Method Name : showVideoCommentsList
	 * @param box
	 * @return
	 */
	public List<Box> showVideoCommentsList(Box box) throws Exception {
		List<Box> videoList = new ArrayList<Box>();
		box.put("sBox", SessionUtil.getUserData());
		box.put("videoId", box.get("video_id"));
		int totCnt = dao.selectOne("mypage.myinfo.showVideoCommentsCnt", box);
		paginate.init(box, totCnt);
		videoList = dao.selectList("mypage.myinfo.showVideoCommentsList",box);

		for(Box innerBox : videoList) {
			Box emptyBox = new Box();
			 Box userBox = dao.selectOne("mypage.myinfo.userInfoView", innerBox);
			 innerBox.put("user",userBox);
			List<Box> videoCommentReplyList = dao.selectList("mypage.myinfo.videoReCommList", innerBox);
			innerBox.put("videoCommentReply", videoCommentReplyList);
			if(innerBox.getBox("videoCommentReply") != null) {
				for (Box inerBox : videoCommentReplyList) {
					Box emptyReBox = new Box();
					 Box userReBox = dao.selectOne("mypage.myinfo.userInfoView",inerBox);
					 inerBox.put("userRe", userReBox);
				}
			}
		}

		return videoList;
	}


	/**
	 * @Method Name : myLikedVideosList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myLikedVideosList(Box box) throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		if (box.getBoolean("again") == true) {
			box.put("pageUnit", box.getInt("page") * 10);
		}
		int totCnt = dao.selectOne("mypage.myinfo.myLikedVideosCnt",box);
		paginate.init(box, totCnt);
		List<Box> myLikedVideosList = dao.selectList("mypage.myinfo.myLikedVideosList",box);
		return myLikedVideosList ;
	}

	/**
	 * @Method Name : showVideosAgainstHashtagList
	 * @param box
	 * @return
	 */
	public List<Box> showVideosAgainstHashtagList(Box box) {
		box.put("sBox", SessionUtil.getUserData());
		box.put("hashtagId", box.get("hashtag_id"));
		List<Box> myLikedVideosList = dao.selectList("mypage.myinfo.showVideosAgainstHashtagList",box);
		return myLikedVideosList ;
	}

	/**
	 * @Method Name : myFanPostList
	 * @param box
	 * @return
	 */
	public List<Box> myFanPostList(Box box) {
		List<Box> reciveBox = new ArrayList<>();
		box.put("sBox", SessionUtil.getUserData());
		box.put("order", box.get("order")); // 최신순 좋아요순 정렬
		box.put("type", "my_post");
		List<Box> resultList = dao.selectList("mypage.myinfo.myFanPostList",box);
		for(Box rowBox: resultList) {
			box.put("postId", rowBox.nvl("id"));
			box.put("targetId", rowBox.nvl("targetId"));
			box.put("id", rowBox.nvl("id"));
			rowBox.put("post", dao.selectOne("mypage.myinfo.myFanPostView", box));
			rowBox.getBox("post").put("player", dao.selectOne("mypage.myinfo.myFanPostPlayerView", box));
			rowBox.put("postImage", dao.selectList("mypage.myinfo.myFanPostImageList", box));
			reciveBox.add(rowBox);
		}
		return reciveBox ;
	}

	/**
	 * @Method Name : faqList
	 * @param box
	 * @return
	 */
	public List<Box> faqList(Box box) throws Exception  {
		box.put("typeCd", box.get("type_cd"));
		List<Box> faqList = dao.selectList("mypage.myinfo.faqList",box);
		return faqList ;
	}

	/**
	 * @Method Name : myPointHistoryList
	 * @param box
	 * @return
	 */
	public List<Box> myPointHistoryList(Box box)  throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		box.put("pointType", box.get("point_type"));

		List<Box> resBox = new ArrayList<>();
		int totCnt = dao.selectOne("mypage.myinfo.myPointHistoryCnt", box);
		paginate.init(box, totCnt);
		resBox = dao.selectList("mypage.myinfo.myPointHistoryList",box);
		return resBox;
	}
	/**
	 * @Method Name : myTicketHistoryList
	 * @param box
	 * @return
	 */
	public List<Box> myTicketHistoryList(Box box)  throws Exception {
		box.put("sBox", SessionUtil.getUserData());
		box.put("type", box.get("type"));

		List<Box> resBox = new ArrayList<>();
		int totCnt = dao.selectOne("mypage.myinfo.myTicketHistoryCnt", box);
		paginate.init(box, totCnt);
		resBox = dao.selectList("mypage.myinfo.myTicketHistoryList",box);
		return resBox;
	}


	/**
	 * @Method Name : myEventList
	 * @param box
	 * @return
	 */
	public List<Box> myEventList(Box box) throws Exception{
		box.put("sBox", SessionUtil.getUserData());

		List<Box> resBox = new ArrayList<>();
		int totCnt = dao.selectOne("mypage.myinfo.myEventCnt", box);
		paginate.init(box, totCnt);
		resBox = dao.selectList("mypage.myinfo.myEventList",box);
		return resBox;
	}


	/**
	 * @Method Name : myHtmlPageView
	 * @param box
	 * @return
	 */
	public List<Box> myHtmlPageView(Box box) {
		box.put("name", box.get("name"));
		return dao.selectList("mypage.myinfo.myHtmlPageView",box);
	}


	/**
	 * 추천친구삭제
	 * @param box
	 * @return
	 */
	public Box referFriendSave(Box box) throws Exception {
//		//로그인 체크
//		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
//			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
//		}
//		box.put("id", session.getAttribute(CmnConst.SES_USER_ID));

		/*
		 * insert / delete / update 같은경우에는 ResIntDto 를 사용하여 처리 하므로
		 * return 값으로 넘겨줄 데이터가 없을 시 return type 을 Box --> int 로 변경 후
		 * return 값을 resBox 말고 0, 1 로 숫자로 넘겨주면 됨.
		 *
		 */
		box.put("sBox", SessionUtil.getUserData());
		Box resBox = new Box();
		int chk = dao.selectOne("mypage.myinfo.referFriendInsertChk",box);
		if(chk == 0) {
			dao.insert("mypage.myinfo.referFriendInsert",box);
		} else {
			// 이미 삭제된 추천 친구 입니다.
		}

		return resBox;
	}

	/**
	 * @Method Name : myPointHisList
	 * @param box
	 * @return
	 */
	public Box myPointHisList(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());

		Box myPointHislist = dao.selectOne("mypage.myinfo.myPointHisList",box);
		if(myPointHislist.isEmpty()) {
			throw new BizException("E107", new String[] {"포인트 내역이"}); // {0} 존재하지 않습니다.
		}
		return myPointHislist;
	}

	/**
	 * @Method Name : eventApplyList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> eventApplyList(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		List<Box> eventApplyList = dao.selectList("mypage.myinfo.eventApplyList",box);//상위 메뉴 조회
		if(eventApplyList.isEmpty()) {
			throw new BizException("E107", new String[] {"이벤트 내역이"}); // {0} 존재하지 않습니다.
		}
		return eventApplyList;
	}

	/**
	 * 현재사진삭제
	 * @Method Name : profileImageDelete
	 * @param box
	 * @return
	 */
	public Box profileImageDelete(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		int user = dao.selectOne("mypage.myinfo.profileImageDeleteChk",box);

		if(user == 0 ) {//사용자 정보 없는 경우
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}

		int chk = dao.update("mypage.myinfo.profileImageDelete", box);

		if(chk == 0) {	//업데이트 안 된 경우 에러 메세지
			throw new BizException("E101");
		}

		return null;
	}

	/**
	 * 프로필 이미지 변경
	 * @Method Name : userImageUpdate
	 * @param box
	 * @return
	 */
	public Box userImageUpdate(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData()); // user session 정보 이걸로 대체
		String txt1 = box.nvl("profile_pic");
		String txt2 = box.nvl("profile_pic_small");

//		Pattern pattern = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
//        Matcher matcher1 = pattern.matcher(txt1);
//        Matcher matcher2 = pattern.matcher(txt2);
//
//        if(!matcher1.find()) {
//        	throw new BizException("F112", new String[] {"인코딩 데이터가 없습니다."} );
//        }
//        if(!matcher2.find()) {
//        	throw new BizException("F112", new String[] {"인코딩 데이터가 없습니다."} );
//        }

		byte[] pic =  Base64Util.decode(box.nvl("profile_pic").replaceFirst("data:image/.*;base64,", CmnConst.STR_EMPTY).getBytes());
		byte[] small = Base64Util.decode(box.nvl("profile_pic_small").replaceFirst("data:image/.*;base64,", CmnConst.STR_EMPTY).getBytes());

		boolean succFlag = true;
		String filePath = ConfigUtil.getString("file.upload.img.path");
		String sysFileNm1 = UUID.randomUUID().toString().replace("-", "");
		String sysFileNm2 = UUID.randomUUID().toString().replace("-", "");
		FileUtil.makeDirectory(filePath);

		File picFile = new File(filePath, sysFileNm1+".png");
		File smallFile = new File(filePath, sysFileNm2+".png");

		try {
			picFile.createNewFile();
			smallFile.createNewFile();
		} catch (IOException e) {
			log.error("{}", e);
			succFlag = false;
			throw new BizException("E102"); //첨부파일 등록이 실패하였습니다.
		}

		try {
			FileOutputStream fileOutputStream1 = new FileOutputStream(picFile);
			FileOutputStream fileOutputStream2 = new FileOutputStream(smallFile);

			fileOutputStream1.write(pic);
			fileOutputStream1.close();

			fileOutputStream2.write(small);
			fileOutputStream2.close();
		} catch (Exception e) {
			log.error("{}", e);
			succFlag = false;
			throw new BizException("E102"); //첨부파일 등록이 실패하였습니다.
		}

		if(!succFlag) {
			throw new BizException("E102"); //첨부파일 등록이 실패하였습니다.
		}
//		/app/webroot/uploads/
		//사용자 정보 업데이트
		box.put("profile_pic", "/app/webroot/upload/images/"+sysFileNm1+".png");
		box.put("profile_pic_small", "/app/webroot/uploads/images/"+sysFileNm2+".png");

		dao.update("mypage.myinfo.userImageUpdate", box);

		return box;
	}

	/**
	 * 유저 차단 / 차단해제
	 * @Method Name : userBlockSave
	 * @param box
	 * @return
	 */
	public Box userBlockSave(Box box) throws  Exception  {
		box.put("sBox", SessionUtil.getUserData());
		Box resBox = new Box();
		if(box.nvl("block_id").equals("")) {	//차단 사용자 없는 경우 에러
			throw new BizException("F112", new String[] {"차단 대상이 없습니다."} ); //F112	{0} - 필수 항목 오류
		}

		int user = dao.selectOne("mypage.myinfo.userStateChk", box);	//블럭될 사용자가 존재하는지 확인

		if(user == 0) {
			throw new BizException("F112", new String[] {"차단 대상이 없습니다."} );
		}

		Box info = dao.selectOne("mypage.myinfo.userBlockSaveChk", box);

		if(info == null ) {	//차단 내역이 없는 경우 차단
			Box userInfo = new Box();
			userInfo.put("id", box.getBox("sBox").nvl("id"));
			userInfo.put("block_id", box.get("block_id"));
			userInfo.put("my_block", "1");
			dao.insert("mypage.myinfo.userBlockInsert", userInfo);
			dao.delete("mypage.myinfo.userBlockFollowerDelete", userInfo);

			userInfo.put("id", box.get("block_id"));
			userInfo.put("block_id", box.getBox("sBox").nvl("id"));
			userInfo.put("my_block", "0");
			dao.insert("mypage.myinfo.userBlockInsert", userInfo);
			dao.delete("mypage.myinfo.userBlockFollowerDelete", userInfo);
			resBox.put("resultMsg", "block");
		}else {	//차단 내역이 있는 경우 차단 해제
			Box userInfo = new Box();
			userInfo.put("id", box.getBox("sBox").nvl("id"));
			userInfo.put("block_id", box.get("block_id"));
			dao.delete("mypage.myinfo.userBlockDelete", userInfo);

			userInfo.put("id", box.get("block_id"));
			userInfo.put("block_id", box.getBox("sBox").nvl("id"));
			dao.delete("mypage.myinfo.userBlockDelete", userInfo);
			resBox.put("resultMsg", "unblock");
		}

		return resBox;
	}

	/**
	 * 나의 차단 목록 조회
	 * @Method Name : myUserBlockList
	 * @param box
	 * @return
	 */
	public List<Box> myUserBlockList(Box box) throws  Exception {
		box.put("sBox", SessionUtil.getUserData());
		List<Box> resBox = new ArrayList<Box>();
		List<Box> returnBox = dao.selectList("mypage.myinfo.myUserBlockList", box);
		if (returnBox.size() == 0 ) {
			throw new BizException("E137", new String[] {"나의 차단내역을"}); //{0} 찾을 수 없습니다.
		}
		for (Box innerBox : returnBox) {
			Box inerBox = new Box();
			inerBox.put("blockUser", innerBox);
			Box secBox = dao.selectOne("mypage.myinfo.tagetUserView", innerBox);
			inerBox.put("tagetUser", secBox);
			resBox.add(inerBox);
		}
//		log.debug("resBox: {}", resBox);
		return resBox;
	}

	/**
	 * 닉네임 변경
	 * @Method Name : userNameUpdate
	 * @param box
	 * @return
	 */
	public int userNameUpdate(Box box) throws  Exception {
		box.put("sBox", SessionUtil.getUserData());
		box.put("username", box.getString("username").replaceAll(" ", ""));//공백제거
		int chk = dao.selectOne("mypage.myinfo.userNameCheck", box);
		if(chk >= 1) {	//사용중인 경우
			throw new BizException("F112", new String[] {"이미 사용중입니다."} );
		}
		dao.update("mypage.myinfo.userNameUpdate", box);
		return 1;
	}

	/**
	 * 패스워드 변경
	 * @Method Name : myPwdUpdate
	 * @param box
	 * @return
	 */
	public int myPwdUpdate(Box box)throws  Exception  {
		box.put("sBox", SessionUtil.getUserData());
//		기존 비밀번호 강제 업데이트용 ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae
		if(!MaskUtil.passwordChk(AutoCrypto.aesDecrypt(box.nvl("new_password")))) {
			throw new BizException("E123"); // password는 8 ~ 16자 이내로 영문, 숫자, 특수문자가 포함되어야 합니다.
		}
		//비밀번호 셋팅
		String oldPwd = DigestUtil.sha256ToStr(CmnConst.PASSWORD_KEY+AutoCrypto.aesDecrypt(box.nvl("old_password")));
		String newPwd = DigestUtil.sha256ToStr(CmnConst.PASSWORD_KEY+AutoCrypto.aesDecrypt(box.nvl("new_password")));
		box.put("oldPwd", oldPwd);
		box.put("newPwd", newPwd);

		Box tmpBox = dao.selectOne("mypage.myinfo.myPwdCheck", box);

		if(tmpBox == null) {//틀린 경우 메세지
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}else {	//비밀번호 맞는 경우 비밀번호 변경
			if(oldPwd.equals(newPwd)) { 	//현재 비밀번호 와 이전 비밀번호가 동일  합니다.
				throw new BizException("E126"); //
			}else {// 일치 아닌 경우 비밀번호 변경
				dao.update("mypage.myinfo.myPwdUpdate", box);
			}
		}
		return 1;
	}

	/**
	 * 쇼페퍼 등록
	 * @Method Name : myShowPerpeperInsert
	 * @param box
	 * @return
	 */
	public Box myShowPerpeperInsert(Box box) throws  Exception {
		box.put("sBox", SessionUtil.getUserData());

//		box.put("id", session.getAttribute(CmnConst.SES_USER_ID));

		return null;
	}

	/**
	 * 회원탈퇴
	 * @Method Name : myUserSecessionUpdate
	 * @param box
	 * @return
	 */
	public int myUserSecessionUpdate(Box box) throws  Exception {
		box.put("sBox", SessionUtil.getUserData());
		//사용자 정보 확인
		int user = dao.selectOne("mypage.myinfo.profileImageDeleteChk", box);

		if(user == 0 ) {
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}
		//비밀번호 확인
		if("".equals(box.nvl("password"))) {
			throw new BizException("F112", new String[] {"비밀번호가 없습니다."} );
		}
		//탈퇴 사유 확인
		if("".equals(box.nvl("active_reason"))) {
			throw new BizException("F112", new String[] {"탈퇴사유가 없습니다."} );
		}
		String[] reason = box.get("active_reason").toString().split(",");
		//탈퇴 사유 확인
		for(String tmp : reason) {
			if(tmp != null &&tmp.contains("RES") ) {
				if(tmp.equals("RES5")) {
					if("".equals(box.nvl("active_reason_detail"))) {
						throw new BizException("F112", new String[] {"기타 사유가 없습니다."} );
					}
				}
			} else {
				throw new BizException("F112", new String[] {"올바른 값을 입력해주세요."} );
			}
		}

		box.put("oldPwd", DigestUtil.sha256ToStr(AutoCrypto.aesDecrypt(box.nvl("password"))));

		Box tmpBox = dao.selectOne("mypage.myinfo.myPwdCheck", box);

		//비밀번호 체크
		if(tmpBox == null) {//틀린 경우 메세지
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}else {	//비밀번호 맞는 경우 탈퇴 처리
			box.put("active", "0");

			//탈퇴 상태  업데이트
			dao.update("mypage.myinfo.myUserSecessionUpdate", box);
		}

		return 1;
	}


	/**
	 * @Method Name : myPushNotificationView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box myPushNotificationView(Box box)  throws  Exception {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		Box resultBox = dao.selectOne("mypage.myinfo.notificationChk", box);
		if ( resultBox == null ) {
			box.put("id", box.getBox("sBox").nvl("id"));
			box.put("likes", "1");
			box.put("new_followers", "1");
			box.put("comments", "1");
			box.put("mentions", "1");
			box.put("direct_messages", "1");
			box.put("video_updates", "1");
			box.put("post_update", "1");
			box.put("event", "1");
			dao.insert("mypage.myinfo.pushNotificationInsrt", box);
			Box dataBox = dao.selectOne("mypage.myinfo.notificationChk", box);
			resBox.put("notice", dataBox.nvl("like"));
			resBox.put("event", dataBox.nvl("event"));
		} else {
			if ( "0".equals(resultBox.nvl("likes"))) {
				resBox.put("notice", "0");
			} else {
				resBox.put("notice", 1);
			}
			resBox.put("notice", resultBox.nvl("likes"));
			resBox.put("event", resultBox.nvl("event"));
		}

		return resBox;
	}

	/**
	 * 포스트 등록
	 * @Method Name : myPostInsert
	 * @param box
	 * @return
	 */
	public int myPostInsert(Box box) throws  Exception {
		box.put("sBox", SessionUtil.getUserData());

		return 1;
	}

	/**
	 * @Method Name : writePost
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int writePost(ReqUIMPMI047Dto reqDto)  throws  Exception {
		Box sBox = SessionUtil.getUserData();
		Box box = new Box();
		box.put("title", reqDto.getTitle());
		box.put("contents", reqDto.getContents());
		box.put("targetId", reqDto.getTargetId());
		box.put("sBox", sBox);
		dao.insert("mypage.myinfo.postInsert", box); // post 글 작성
		int postId = dao.selectOne("mypage.myinfo.postInsertKey", sBox); // 글 id 저장

		MultipartFile[] mFileList = (MultipartFile[]) reqDto.getImage(); // 이미지 파일 배열
		Box fileUploadResult = CmnRestService.fileUpload(mFileList);
		List<Box> fileList = (List<Box>) fileUploadResult.get("list");
		for (Box imageInsertBox : fileList) { // 이미지파일 배열을 반복문으로 insert 처리
			imageInsertBox.put("sBox", sBox);
			imageInsertBox.put("postId", postId);
			dao.insert("mypage.myinfo.postImgInsert", imageInsertBox);
		}

		// cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_VIDEO_CMT , CmnConst.PUSH_TYPE_VIDEO_CMN , box.nvl("videoId") , "", "", "", "");

		return 1;
	}

	/**
	 * @Method Name : editPost
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int editPost(ReqUIMPMI049Dto reqBox)  throws  Exception {
		Box sBox = SessionUtil.getUserData();
		Box box = new Box();
		box.put("sBox", sBox);
		box.put("postId", reqBox.getPost_id());

		String[] imageArray = reqBox.getImage_ids().split(",");

		List<Box> postImgList = dao.selectList("mypage.myinfo.postImageList", box);

		// imageArray에 있는 이미지 id와 postImgList값을 조회해서 DB에 저장된 id값을 비교 후
		// imageArray에 없는
		for(Box imageRow : postImgList) {
			int isImageDelete = 0;
			for(String imageArrayElement : imageArray) {
				if(imageRow.nvl("id").equals(imageArrayElement)) {
					isImageDelete++;
				}
			}
			if(isImageDelete == 0) {
				dao.delete("mypage.myinfo.postImageDelete", imageRow);
			}
		}
		postImgList = dao.selectList("mypage.myinfo.postImageList", box);
		int imageCnt = postImgList.size();

		MultipartFile[] mFileList = (MultipartFile[]) reqBox.getImage(); // 이미지 파일 배열

		if(mFileList != null & mFileList.length > imageCnt) {

			MultipartFile[] arr2 = new MultipartFile[mFileList.length - imageCnt];

			for(int i = 0; i < imageArray.length; i++) {
				arr2[i] = mFileList[i + imageCnt];
			}
			Box fileUploadResult = CmnRestService.fileUpload(arr2);
			List<Box> fileList = (List<Box>) fileUploadResult.get("list");
			for (Box imageInsertBox : fileList) { // 이미지파일 배열을 반복문으로 insert 처리
				imageInsertBox.put("sBox", sBox);
				imageInsertBox.put("postId", reqBox.getPost_id());
				dao.insert("mypage.myinfo.postImgInsert", imageInsertBox);
			}
		}

		dao.update("mypage.myinfo.postUpdate", box);
		return 1;
	}
	/**
	 * @Method Name : deletePost
	 * @param box';
	 * @return
	 * @throws Exception
	 */
	public int deletePost(Box box)  throws  Exception {
		Box sBox = SessionUtil.getUserData();
		box.put("sBox", sBox);

		// 대댓글 삭제
		dao.update("mypage.myinfo.postCommentReplyDelete", box);
		// 댓글 삭제
		dao.update("mypage.myinfo.postCommentDelete", box);
		// 이미지 삭제
		List<Box> postImgList = dao.selectList("mypage.myinfo.postImageList", box);
		dao.delete("mypage.myinfo.postImageDelete", postImgList);

		// 포스트 삭제
		dao.insert("mypage.myinfo.postDelete", box);
		return 1;
	}

	/**
	 * @Method Name : episodeList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> episodeList(Box box)  throws  Exception {
		Box sBox = SessionUtil.getUserData();
		int totCnt = dao.selectOne("mypage.myinfo.postListCnt", box);
		paginate.init(box, totCnt);
		box.put("target_id", AutoCrypto.aesDecrypt(box.nvl("target_id")));
		box.put("sBox", sBox);
		box.put("order", box.get("order")); // 최신순 좋아요순 정렬
		List<Box> postList = dao.selectList("mypage.myinfo.postList", box);
		List<Box> resultListBox = new ArrayList<>();
		for(Box postIdList : postList) {
			Box rowBox = new Box();

			rowBox.put("post", postIdList);

			// 유저 정보입력 (username, profile_pic, profile_pic_small, role)
			Box userInfoBox = dao.selectOne("mypage.myinfo.postUserView", box);
			rowBox.put("user", userInfoBox);

			// 포스트 이미지 조회
			Box tempBox = new Box();
			tempBox.put("postId", postIdList.nvl("id"));
			List<Box> postImgList = dao.selectList("mypage.myinfo.postImageList", tempBox);
			rowBox.put("postImage", postImgList);

			resultListBox.add(rowBox);
		}

		return resultListBox;
	}

	/**
	 * @Method Name : videoDel
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int videoDel(Box box)  throws  Exception {
		box.put("sBox", SessionUtil.getUserData());
		box.put("videoId", box.nvl("video_id"));
		int result = dao.update("mypage.myinfo.videoDel", box);
		if ( result != 1) {
			throw new BizException("E125", new String[] {"이미 해당영상이"}); // {0} 삭제 되었습니다.
		}

		return result;
	}

	public Box getPostDetail(Box box)   throws  Exception {

		Box sBox = SessionUtil.getUserData();

		box.put("sBox", sBox);
		box.put("postId", AutoCrypto.aesDecrypt(box.nvl("post_id")));

		// 포스트 상세 조회
		Box postView = dao.selectOne("mypage.myinfo.postView", box);

		// 최종 박스
		Box resultPostViewBox = new Box();

		resultPostViewBox.put("post", postView);

		// 유저 정보입력 (username, profile_pic, profile_pic_small, role)
		Box userInfoBox = dao.selectOne("mypage.myinfo.postUserView", postView);
		resultPostViewBox.put("user", userInfoBox);

		// 포스트 이미지 조회
		Box tempBox = new Box();
		tempBox.put("postId", postView.nvl("id"));
		List<Box> postImgList = dao.selectList("mypage.myinfo.postImageList", tempBox);
		resultPostViewBox.put("postImage", postImgList);

		// 댓글 조회 for문
		List<Box> postCmtList = dao.selectList("mypage.myinfo.postCmtList", tempBox);

		// 댓글 리스트
		List<Box> postCmtListBox = new ArrayList<>();

		// 대댓글 조회 for문 (이중for문)
		for(Box postCmtRow : postCmtList) {

			// 유저 정보입력 (username, profile_pic, profile_pic_small, role)
			Box replyUserInfoBox = dao.selectOne("mypage.myinfo.postUserView", postCmtRow);
			postCmtRow.put("User", replyUserInfoBox);

			Box postCmtReply = new Box();
			postCmtReply.put("commentId", postCmtRow.nvl("id"));

			// 대댓글 리스트
			List<Box> postCmtReplyList = dao.selectList("mypage.myinfo.postCmtReplyList", postCmtReply);

			// 대댓글 리스트
			List<Box> postCmtReplyListBox = new ArrayList<>();

			for(Box postCmtReplyListRow : postCmtReplyList) {

				Box postCmtReplyUserBox = new Box();
				postCmtReplyUserBox.put("userId", postCmtReplyListRow.nvl("userId"));

				// 유저 정보입력 (username, profile_pic, profile_pic_small, role)
				postCmtReplyUserBox = dao.selectOne("mypage.myinfo.postUserView", postCmtReplyUserBox);

				postCmtReplyListRow.put("User", postCmtReplyUserBox);


				// 대대댓글 리스트
				Box postCmtReplyReplyList = dao.selectOne("mypage.myinfo.postCmtReplyReply", postCmtReplyListRow);

				postCmtReplyListRow.put("replyReply", postCmtReplyReplyList);

				postCmtReplyListBox.add(postCmtReplyListRow);
			}
			postCmtRow.put("PostCommentReply", postCmtReplyListBox);

			postCmtListBox.add(postCmtRow);

		}

		resultPostViewBox.put("Comment", postCmtListBox);

		return resultPostViewBox;
	}

	/**
	 * @Method Name : pushNotificationUpdate
	 * @param box
	 * @return
	 */
	public int pushNotificationUpdate(Box box) {
		box.put("sBox", SessionUtil.getUserData());
		if (box.getInt("notice") == 0) {
			box.put("likes", "0");
			box.put("newFollowers", "0");
			box.put("comments", "0");
			box.put("mentions", "0");
			box.put("directMessages", "0");
			box.put("videoUpdates", "0");
			box.put("postUpdate", "0");
		} else if (box.getInt("notice") == 1) {
			box.put("likes", "1");
			box.put("newFollowers", "1");
			box.put("comments", "1");
			box.put("mentions", "1");
			box.put("directMessages", "1");
			box.put("videoUpdates", "1");
			box.put("postUpdate", "1");
		} else {
			throw new BizException("E135", new String[] {"push 값을"}); // {0} 확인해 주세요.
		}
		dao.update("mypage.myinfo.updateNotification", box);
		return 1;
	}

	/**
	 * @Method Name : writePostInsert
	 * @param box
	 * @return
	 */
	public Box writePostInsert (Box box) {
		box.put("sBox", SessionUtil.getUserData());
		Box resBox = new Box();
		if ("comment".equals(box.nvl("type"))) { // 댓글
			box.put("postId", box.nvl("post_id"));
			Box postChk = dao.selectOne("mypage.myinfo.postUseChk", box); //POST 유무 확인
			if (postChk == null) {
				throw new BizException("E137", new String[] {"포스트를"}); // {0} 찾을 수 없습니다.
			}
			if ( postChk != null) {
				dao.insert("mypage.myinfo.postCmmInsert",box);
				resBox.put("postComment", dao.selectOne("mypage.myinfo.postCmmView", box));
				cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_POST_CMT , CmnConst.PUSH_TYPE_POST_CMT , "" , box.nvl("post_id"), "" , "", "");
			}
		} else if ("reply".equals(box.nvl("type"))) { // 대댓글
			box.put("postId", box.nvl("post_id"));
			box.put("commentId", box.nvl("comment_id"));
			if ("".equals(box.nvl("commentId"))) {
				throw new BizException("E135" , new String[] {"commentId 값을"}); // {0} 확인해 주세요
			}
			Box postChk = dao.selectOne("mypage.myinfo.postUseChk", box); //POST 유무 확인
			if (postChk == null) {
				throw new BizException("E137", new String[] {"포스트를"}); // {0} 찾을 수 없습니다.
			}
			if ( postChk != null) {
				Box postCmmChk = dao.selectOne("mypage.myinfo.postCmmChk", box); //POST comment 유무 확인
				if (postCmmChk == null) {
					throw new BizException("E137", new String[] {"댓글을"}); // {0} 찾을 수 없습니다.
				}
				if (postCmmChk != null) {
					dao.insert("mypage.myinfo.postReCmmInsert",box);
					resBox.put("PostCommentReply", dao.selectOne("mypage.myinfo.postCmmReView", box));
					cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_POST_CMT , CmnConst.PUSH_TYPE_POST_CMT , "" , box.nvl("post_id"), box.nvl("commentId") , "", "");
				}
			}
		} else if ("replyreply".equals(box.nvl("type"))) { // 대대댓글
			box.put("postId", box.nvl("post_id"));
			box.put("commentId", box.nvl("comment_id"));
			box.put("replyCommentId", box.nvl("reply_comment_id"));
			if ("".equals(box.nvl("commentId"))) {
				throw new BizException("E135" , new String[] {"commentId 값을"}); // {0} 확인해 주세요
			}
			if ("".equals(box.nvl("replyCommentId"))) {
				throw new BizException("E135" , new String[] {"replyCommentId 값을"}); // {0} 확인해 주세요
			}
			Box postChk = dao.selectOne("mypage.myinfo.postUseChk", box); //POST 유무 확인
			if (postChk == null) {
				throw new BizException("E137", new String[] {"포스트를"}); // {0} 찾을 수 없습니다.
			}
			if ( postChk != null) {
				Box postCmmChk = dao.selectOne("mypage.myinfo.postCmmChk", box); //POST comment 유무 확인
				if (postCmmChk == null) {
					throw new BizException("E137", new String[] {"댓글을"}); // {0} 찾을 수 없습니다.
				}
				if (postCmmChk !=  null) {
					Box postReCmmChk = dao.selectOne("mypage.myinfo.postReCmmChk", box); //POST comment 유무 확인
					if (postReCmmChk == null) {
						throw new BizException("E137", new String[] {"댓글을"}); // {0} 찾을 수 없습니다.
					}
					if ( postReCmmChk !=  null) {
						dao.insert("mypage.myinfo.postReCmmInsert",box);
						resBox.put("PostCommentReply", dao.selectOne("mypage.myinfo.postCmmReView", box));
						cmnService.pushSend(box.getBox("sBox").nvl("id"), CmnConst.PUSH_POST_CMT , CmnConst.PUSH_TYPE_POST_CMT , "" , box.nvl("post_id"), box.nvl("commentId") , box.nvl("replyCommentId"), "");
					}
				}
			}
		}
		return resBox;
	}

	/**
	 * @Method Name : postLikeSave
	 * @param box
	 * @return
	 */
	public Box postLikeSave(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		box.put("postId", box.getInt("post_id"));
		Box likeBox = dao.selectOne("mypage.myinfo.postLikeChk",box);
		if (likeBox == null ) {
			dao.insert("mypage.myinfo.postLikeIst",box);
		} else {
			dao.insert("mypage.myinfo.postLikeDel",box);
		}
		Box result = dao.selectOne("mypage.myinfo.postLikeChk",box);
		if (result == null ) {
			resBox.put("is_like", "N");
		} else {
			resBox.put("is_like", "Y");
		}
		resBox.put("like_cnt", dao.selectOne("mypage.myinfo.postLikeCnt",box));
		return resBox;
	}

	/**
	 * @Method Name : postCommentLikeSave
	 * @param box
	 * @return
	 */
	public Box postCommentLikeSave(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		box.put("commentId", box.getInt("comment_id"));
		Box likeBox = dao.selectOne("mypage.myinfo.postCmmLikeChk",box);
		if (likeBox == null ) {
			dao.insert("mypage.myinfo.postCmmLikeIst",box);
		} else {
			dao.insert("mypage.myinfo.postCmmLikeDel",box);
		}
		Box result = dao.selectOne("mypage.myinfo.postCmmLikeChk",box);
		if (result == null ) {
			resBox.put("is_like", "N");
		} else {
			resBox.put("is_like", "Y");
		}
		resBox.put("like_cnt", dao.selectOne("mypage.myinfo.postCmmLikeCnt",box));
		return resBox;
	}

	/**
	 * @Method Name : postCommentReplyLikeSave
	 * @param box
	 * @return
	 */
	public Box postCommentReplyLikeSave(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		box.put("commentId", box.getInt("comment_id"));
		Box likeBox = dao.selectOne("mypage.myinfo.postReCmmLikeChk",box);
		if (likeBox == null ) {
			dao.insert("mypage.myinfo.postReCmmLikeIst",box);
		} else {
			dao.insert("mypage.myinfo.postReCmmLikeDel",box);
		}
		Box result = dao.selectOne("mypage.myinfo.postReCmmLikeChk",box);
		if (result == null ) {
			resBox.put("is_like", "N");
		} else {
			resBox.put("is_like", "Y");
		}
		resBox.put("like_cnt", dao.selectOne("mypage.myinfo.postReCmmLikeCnt",box));
		return resBox;
	}


	/**
	 * @Method Name : buyInterparkCode
	 * @param box
	 * @return
	 */
	public Box buyInterparkCode(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		Box setBox = new Box();
		Box resBox = new Box();
		int userPoint = dao.selectOne("mypage.myinfo.getUserPoint", box);
		if(userPoint < 5000) {
			throw new BizException("E144", new String[] {"포인트가"}); // 부족합니다.
		}

		Box interparkCode = dao.selectOne("mypage.myinfo.getInterparkCode",box);
		if(interparkCode.isEmpty()) {
			throw new BizException("E145");
		}
		resBox.put("code", interparkCode.nvl("code"));
		// 사용자 포인트 차감
		dao.update("mypage.myinfo.usrPointUpdate", box); //사용자 포인트 수정

		// 코드리스트 업데이트
		setBox.put("id", interparkCode.nvl("id"));
		dao.update("mypage.myinfo.interparkCodeListUpdate", setBox);

		// 포인트 히스토리 업데이트
		dao.insert("mypage.myinfo.pointHistoryInsert", box); //포인트 히스토리 수정

		return resBox;
	}

	/**
	 * @Method Name : getPostCommentList
	 * @param box
	 * @return
	 */
	public List<Box> getPostCommentList(Box box) {
		box.put("postId", box.nvl("post_id"));
		List<Box> resBox = dao.selectList("mypage.myinfo.getPostCommentList",box);
		for (Box innerBox : resBox ) {
			innerBox.put("postComment", dao.selectOne("mypage.myinfo.postCommentView",innerBox));
			Box usrBox = dao.selectOne("mypage.myinfo.postUserView",innerBox);
			if (usrBox != null ) {
				innerBox.getBox("postComment").put("user", usrBox);
			}
			List<Box> replyList = dao.selectList("mypage.myinfo.cmnReList", innerBox);
			for ( Box inrBox : replyList ) {
				Box userBox = dao.selectOne("mypage.myinfo.postUserView",inrBox);
				if (userBox != null ) {
					inrBox.put("user", userBox);
				}
			}
			innerBox.getBox("postComment").put("reply", replyList);
		}
		return resBox;
	}

	/**
	 * @Method Name : postCmnDel
	 * @param box
	 * @return
	 */
	public Box postCmnDel(Box box) {
		box.put("sBox", SessionUtil.getUserData());

		if (SessionUtil.getUserData() == null ) {
			throw new BizException("E153", new String[] {"필수 정보가"}); // {0} 없습니다.
		}
		if ("comment".equals(box.nvl("type")) && "".equals(box.nvl("comment_id"))) {
			throw new BizException("E153", new String[] {"댓글 아이디가"}); // {0} 없습니다.
		}

		if ("reply".equals(box.nvl("type")) && "".equals(box.nvl("comment_id"))) {
			throw new BizException("E153", new String[] {"댓글 아이디가"}); // {0} 없습니다.
		}

//		if ("reply".equals(box.nvl("type")) && !"".equals(box.nvl("comment_id")) && "".equals(box.nvl("reply_comment_id"))) {
//			throw new BizException("E153", new String[] {"대댓글 아이디가 "}); // {0} 없습니다.
//		}

		box.put("commentId", box.nvl("comment_id"));
		box.put("replyCommentId", box.nvl("reply_comment_id"));

		Box postChk = dao.selectOne("mypage.myinfo.postIdChk", box);
		if (postChk == null) {
			throw new BizException("E153", new String[] {"게시물 아이디가"}); // {0} 없습니다.
		}

		Box resBox = new Box();
		if ("comment".equals(box.nvl("type"))) {
			box.put("commentId", box.nvl("comment_id"));
			int cmnBox = dao.selectOne("mypage.myinfo.cmnPostChk", box);
			if (cmnBox != 0 ) {
				dao.delete("mypage.myinfo.postCmnDel", box);
			} else {
				throw new BizException("E153", new String[] {"필수 정보가"}); // {0} 없습니다.
			}
		} else if ("reply".equals(box.nvl("type"))) {
			box.put("commentId", box.nvl("comment_id"));
			box.put("replyCommentId", box.nvl("reply_comment_id"));
			int cmnReBox = dao.selectOne("mypage.myinfo.cmnRePostChk", box);
			if (cmnReBox != 0 ) {
				dao.delete("mypage.myinfo.postReCmnDel", box);
			} else {
				throw new BizException("E153", new String[] {"필수 정보가"}); // {0} 없습니다.
			}
		}
		return resBox;
	}

	public List<Box> fanpostList(Box box) {
		box.put("sBox", SessionUtil.getUserData());
		box.put("type", "fan_post");
		List<Box> resBox = dao.selectList("mypage.myinfo.postList", box);
		for (Box innerBox : resBox ) {
			innerBox.put("user", dao.selectOne("mypage.myinfo.postUserView", innerBox));
		}
		return resBox;
	}
}
