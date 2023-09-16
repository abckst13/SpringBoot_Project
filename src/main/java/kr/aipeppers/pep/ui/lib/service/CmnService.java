package kr.aipeppers.pep.ui.lib.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.ConfigUtil;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.StringUtil;
import kr.aipeppers.pep.core.util.crypto.Base64Util;
import kr.aipeppers.pep.core.util.file.FileUtil;
import kr.aipeppers.pep.core.util.noti.PushNotification;
import kr.aipeppers.pep.ui.lib.domain.ReqPrflFileUldDto;
import kr.aipeppers.pep.ui.lib.domain.ReqViewPrflImgDto;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CmnService {

	@Autowired
	protected SqlSessionTemplate dao;

	/**
	 * 포로필 파일 업로드
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Box prflFileUld(ReqPrflFileUldDto reqDto) throws IOException {
		Box sBox = SessionUtil.getUserData();
		if (sBox == null || sBox.nvl("userId").equals("") ) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}

		//Multipart/form-date 는 toBox 이용 안돼어서 수동으로 작업
		Box inputBox = new Box();
		inputBox.put("mnmPrflNcknme", StringUtil.nullToStr(reqDto.getMnmPrflNcknme()));
		inputBox.put("mnmPrflImgCn", reqDto.getMnmPrflFile().getBytes()); //파일
		inputBox.put("sBox", sBox);

		Box resultBox = new Box();
		resultBox.put("cnt", dao.insert("cmn.prflInsert", inputBox));

		return resultBox;
	}

	/**
	 * 프로필 이미지 변경
	 * @Method Name : userImageUpdate
	 * @param box
	 * @return
	 */
	public Box userImageUpdate(String image) throws Exception  {
		Box resBox = new Box();
//		String txt1 = box.nvl("profile_pic");
//		String txt2 = box.nvl("profile_pic_small");

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

		byte[] pic =  Base64Util.decode(image.replaceFirst("data:image/.*;base64,", CmnConst.STR_EMPTY).getBytes());
		byte[] small = Base64Util.decode(image.replaceFirst("data:image/.*;base64,", CmnConst.STR_EMPTY).getBytes());

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
		//사용자 정보 업데이트
		resBox.put("profile_pic", "/upload/"+sysFileNm1+".png");
		resBox.put("profile_pic_small", "/upload/"+sysFileNm2+".png");

		return resBox;
	}

	/**
	 * 프로필 이미지 조회
	 * @param reqDto
	 * @param response
	 * @return
	 */
	public ResponseEntity<byte[]> viewPrflImg(ReqViewPrflImgDto reqDto) {
		ResponseEntity<byte[]> responseEntity = null;
		Box sBox = SessionUtil.getUserData();
		if (sBox == null || sBox.nvl("userId").equals("") ) {
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			String prflImgCstMngtNo = null;
			String loginMnmCstMngtNo = sBox.nvl("mnmCstMngtNo");
			String dtoMnmCstMngtNo = reqDto.getMnmCstMngtNo();

			// 파라미터로 넘어온 고객관리번호가 존재하고 로그인 사용자와 다른 사용자인 경우 - 내가 추천하여 가입한 고객인지 확인한다.
			if(StringUtil.isNotEmpty(dtoMnmCstMngtNo) && !dtoMnmCstMngtNo.equals(loginMnmCstMngtNo)) {
				Box inputBox = new Box();
				inputBox.put("mnmCstMngtNo", dtoMnmCstMngtNo); //파라미터고객번호
				inputBox.put("mnmJRcmrCstMngtNo", loginMnmCstMngtNo); //추천인고객번호-로그인사용자
				Box rcmrSelectResultBox = dao.selectOne("cmn.rcmrSelect", inputBox);
				//내가 추천한 이력에 없는 경우 - 권한 없음.
				if(rcmrSelectResultBox == null || StringUtil.isEmpty(rcmrSelectResultBox.nvl("mnmCstMngtNo"))) {
					responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				}
				//내가 추천한 고객인경우
				else {
					prflImgCstMngtNo = rcmrSelectResultBox.nvl("mnmCstMngtNo");
				}
			}
			// 파라미터로 넘어온 고객관리번호가 없거나, 로그인 사용자인 경우
			else if(StringUtil.isEmpty(dtoMnmCstMngtNo) || dtoMnmCstMngtNo.equals(loginMnmCstMngtNo)) {
				prflImgCstMngtNo = loginMnmCstMngtNo;
			}

			if(StringUtil.isNotEmpty(prflImgCstMngtNo)) {
				Box inputBox = new Box();
				inputBox.put("mnmCstMngtNo", prflImgCstMngtNo);
				Box prflResultBox = dao.selectOne("cmn.prflSelect", inputBox);

				if(prflResultBox == null) {
					responseEntity = ResponseEntity.notFound().build();

				} else {
					byte[] mnmPrflImgCn = (byte[]) prflResultBox.get("mnmPrflImgCn");

					if(mnmPrflImgCn != null) {
						//TODO: 파일자원에 대한 Header 값들 필요하면 처리 (ex last-modified, etag 등)
						responseEntity = ResponseEntity.ok(mnmPrflImgCn);

					} else {
						responseEntity = ResponseEntity.notFound().build();
					}
				}
			}
		}
		return responseEntity;
	}

	public void pushSend(String userId, String body , String type , String videoId , String postId, String commentId, String replyCommentId, String targetId) {
		// push logic 만들기
		List<Box> pushBox = new ArrayList<Box>();
		Box reqBox = new Box();
		reqBox.put("userId", userId);

		if("follow" .equals(type)) {
			followSendPush(userId,targetId,body,type); // 팔로우 신청
		}

		if("video_updates".equals(type)) {
			videoPostUpdatesPush(userId,body,type,videoId,""); // 쇼페퍼 업로드
		}

		if("post_updates".equals(type)) {
			videoPostUpdatesPush(userId,body,type,"",postId); // 포스트 업로드
		}

		if("video_comment".equals(type) && "".equals(commentId)) {
			videoCmmPush(userId,body,type,videoId,""); // 댓글
		} else if("video_comment".equals(type)&& !"".equals(commentId) && commentId != null) {
			videoReplyCmmPush(userId,body,type,videoId,"",commentId,replyCommentId); // 대댓글 대대댓글
		}

		if("post_comment".equals(type) && "".equals(commentId) || commentId == null) {
			postCmmPush(userId,body,type,"",postId,commentId); // 포스트 댓글
		} else if("post_comment".equals(type) && !"".equals(commentId) && commentId != null) {
			postReplyCmmPush(userId,body,type,"",postId,commentId,replyCommentId); // 포스트 대댓글 대대댓글
		}


	}


	/**
	 * @Method Name : followSendPush
	 * @param userId
	 * @param body
	 * @param type
	 */
	public void followSendPush (String userId, String targetId, String body , String type) { // 쇼페퍼 영상에 댓글 시 푸쉬
		// push logic 만들기
		List<Box> pushBox = new ArrayList<Box>();
		Box reqBox = new Box();
		reqBox.put("userId", userId); // 댓글 작성자 id
		reqBox.put("targetId", targetId); // 댓글 작성자 id
		Box followSendBox = dao.selectOne("cmn.followSendPush",reqBox); // 팔로우 신청 하는 사람의 정보 조회

		Box subBox = new Box();
		Box notificationBox = new Box();
		/*data mapping*/
		notificationBox.put("title" , "AIPEPPERS");
		notificationBox.put("body" , followSendBox.nvl("sendUserNm") + " " + body); // 댓글 작성자 username
		notificationBox.put("badge" , "1");
		notificationBox.put("sound" , "default");
		notificationBox.put("icon" , "");
		notificationBox.put("type" , "push_notification");
		notificationBox.put("receiver_id" , followSendBox.nvl("receiverId")); // 팔로우를 받는 사람의 아이디
		notificationBox.put("sender_id" , userId);	// 댓글을 단 사람의 아이디
		/*data setting*/
		subBox.put("to", followSendBox.nvl("deviceToken"));
		subBox.put("priority", "high");
		subBox.put("notification", notificationBox);
		subBox.put("data", notificationBox);
		subBox.getBox("data").put("sender_id", userId);
		pushBox.add(subBox);
		PushNotification.pushNotification(pushBox); // PUSH 실행

		Box isrtBox = new Box();
		isrtBox.put("string",body);
		isrtBox.put("type", type);
		isrtBox.put("senderId", userId);
		isrtBox.put("receiverId", followSendBox.nvl("receiverId"));
		dao.insert("cmn.pushInfoInsert", isrtBox);
	}

	/**
	 * @Method Name : videoCmmPush
	 * @param userId
	 * @param body
	 * @param type
	 * @param videoId
	 * @param postId
	 * @param commentId
	 */
	public void videoCmmPush (String userId, String body , String type , String videoId , String postId) { // 쇼페퍼 영상에 댓글 시 푸쉬
		// push logic 만들기
		List<Box> pushBox = new ArrayList<Box>();
		Box reqBox = new Box();
		reqBox.put("videoId", videoId); // 댓글 작성 비디오 id
		reqBox.put("userId", userId); // 댓글 작성자 id
		Box writUsrBox = dao.selectOne("cmn.videoUsrCmnPush",reqBox); // video 등록자의 video id 로 회원  id 를 조회 후 그 사람에게 push 보내기

		Box subBox = new Box();
		Box notificationBox = new Box();
		/*data mapping*/
		notificationBox.put("title" , "AIPEPPERS");
		notificationBox.put("body" , writUsrBox.nvl("sendUserNm") + " " + body); // 댓글 작성자 username
		notificationBox.put("badge" , "1");
		notificationBox.put("sound" , "default");
		notificationBox.put("icon" , "");
		notificationBox.put("type" , "push_notification");
		notificationBox.put("receiver_id" , writUsrBox.nvl("reciveId")); // 비디오 게시글 작성자의 id
		notificationBox.put("sender_id" , userId);	// 댓글을 단 사람의 아이디
		/*data setting*/
		subBox.put("to", writUsrBox.nvl("deviceToken"));
		subBox.put("priority", "high");
		subBox.put("notification", notificationBox);
		subBox.put("data", notificationBox);
		subBox.getBox("data").put("sender_id", userId);
		pushBox.add(subBox);
		PushNotification.pushNotification(pushBox); // PUSH 실행

		Box isrtBox = new Box();
		isrtBox.put("string",body);
		isrtBox.put("type", type);
		isrtBox.put("videoId", Integer.parseInt(videoId));
		isrtBox.put("senderId", userId);
		isrtBox.put("receiverId", writUsrBox.nvl("reciveId"));
		dao.insert("cmn.pushInfoInsert", isrtBox);
	}

	/**
	 * @Method Name : videoReplyCmmPush
	 * @param userId
	 * @param body
	 * @param type
	 * @param videoId
	 * @param postId
	 * @param commentId
	 * @param replyCommentId
	 */
	public void videoReplyCmmPush (String userId, String body , String type , String videoId , String postId, String commentId, String replyCommentId) {  // 쇼페퍼 영상에 대댓글 , 대대댓글 시 푸쉬
		// push logic 만들기
		List<Box> pushBox = new ArrayList<Box>();
		Box reqBox = new Box();
		Box writUsrBox = new Box();
		reqBox.put("videoId", videoId); // 댓글 작성 비디오 id
		reqBox.put("userId", userId); // 댓글 작성자 id
		reqBox.put("commentId", commentId); // 대댓글 id
		reqBox.put("replyCommentId", replyCommentId); // 대대댓글 id
		if("".equals(replyCommentId)) {
			writUsrBox = dao.selectOne("cmn.videoUsrReplyCmnPush",reqBox); // 댓글 id 로 회원  최초 댓글의 작성자에게 push 보내기
			if (writUsrBox == null) {
				throw new BizException("E135", new String[] {"push 파라미터 값을"}); // {0} 확인해 주세요.
			}
		} else {
			writUsrBox = dao.selectOne("cmn.videoUsrSecReplyCmnPush",reqBox); // 대댓글 아이디 로 조회 후 그 사람에게 push 보내기
			if (writUsrBox == null) {
				throw new BizException("E135", new String[] {"push 파라미터 값을"}); // {0} 확인해 주세요.
			}
		}

		Box subBox = new Box();
		Box notificationBox = new Box();
		/*data mapping*/
		notificationBox.put("title" , "AIPEPPERS");
		notificationBox.put("body" , writUsrBox.nvl("sendUserNm") + " " + body); // 댓글 작성자 username
		notificationBox.put("badge" , "1");
		notificationBox.put("sound" , "default");
		notificationBox.put("icon" , "");
		notificationBox.put("type" , "push_notification");
		notificationBox.put("receiver_id" , writUsrBox.nvl("reciveId")); // 비디오 게시글 작성자의 id
		notificationBox.put("sender_id" , userId);	// 댓글을 단 사람의 아이디
		/*data setting*/
		subBox.put("to", writUsrBox.nvl("deviceToken"));
		subBox.put("priority", "high");
		subBox.put("notification", notificationBox);
		subBox.put("data", notificationBox);
		subBox.getBox("data").put("sender_id", userId);
		pushBox.add(subBox);
		PushNotification.pushNotification(pushBox); // PUSH 실행

		Box isrtBox = new Box();
		isrtBox.put("string",body);
		isrtBox.put("type", type);
		isrtBox.put("videoId", Integer.parseInt(videoId));
		isrtBox.put("senderId", userId);
		isrtBox.put("receiverId", writUsrBox.nvl("reciveId"));
		dao.insert("cmn.pushInfoInsert", isrtBox);
	}

	/**
	 * @Method Name : postCmmPush
	 * @param userId
	 * @param body
	 * @param type
	 * @param videoId
	 * @param postId
	 * @param commentId
	 */
	public void postCmmPush (String userId, String body , String type , String videoId , String postId, String commentId) {
		// push logic 만들기
		List<Box> pushBox = new ArrayList<Box>();
		Box reqBox = new Box();
		reqBox.put("postId", postId); // 댓글 작성 비디오 id
		reqBox.put("userId", userId); // 댓글 작성자 id
		Box writUsrBox = dao.selectOne("cmn.postUsrCmnPush",reqBox); // video 등록자의 video id 로 회원  id 를 조회 후 그 사람에게 push 보내기

		Box subBox = new Box();
		Box notificationBox = new Box();
		/*data mapping*/
		notificationBox.put("title" , "AIPEPPERS");
		notificationBox.put("body" , writUsrBox.nvl("sendUserNm") + " " + body); // 댓글 작성자 username
		notificationBox.put("badge" , "1");
		notificationBox.put("sound" , "default");
		notificationBox.put("icon" , "");
		notificationBox.put("type" , "push_notification");
		notificationBox.put("receiver_id" , writUsrBox.nvl("reciveId")); // 비디오 게시글 작성자의 id
		notificationBox.put("sender_id" , userId);	// 댓글을 단 사람의 아이디
		/*data setting*/
		subBox.put("to", writUsrBox.nvl("deviceToken"));
		subBox.put("priority", "high");
		subBox.put("notification", notificationBox);
		subBox.put("data", notificationBox);
		subBox.getBox("data").put("sender_id", userId);
		pushBox.add(subBox);
		PushNotification.pushNotification(pushBox); // PUSH 실행

		Box isrtBox = new Box();
		isrtBox.put("string",body);
		isrtBox.put("type", type);
		isrtBox.put("postId", Integer.parseInt(postId));
		isrtBox.put("senderId", userId);
		isrtBox.put("receiverId", writUsrBox.nvl("reciveId"));
		dao.insert("cmn.pushInfoInsert", isrtBox);

	}

	/**
	 * @Method Name : postReplyCmmPush
	 * @param userId
	 * @param body
	 * @param type
	 * @param videoId
	 * @param postId
	 * @param commentId
	 * @param replyCommentId
	 */
	public void postReplyCmmPush (String userId, String body , String type , String videoId , String postId, String commentId, String replyCommentId) {
		// push logic 만들기
		List<Box> pushBox = new ArrayList<Box>();
		Box reqBox = new Box();
		Box writUsrBox = new Box();
		reqBox.put("postId", postId); // 댓글 작성 비디오 id
		reqBox.put("userId", userId); // 댓글 작성자 id
		reqBox.put("commentId", commentId); // 대댓글 id
		reqBox.put("replyCommentId", replyCommentId); // 대대댓글 id
		if("".equals(replyCommentId)) {
			writUsrBox = dao.selectOne("cmn.postUsrReplyCmnPush",reqBox); // 댓글 id 로 회원  최초 댓글의 작성자에게 push 보내기
			if (writUsrBox == null) {
				throw new BizException("E135", new String[] {"push 파라미터 값을"}); // {0} 확인해 주세요.
			}
		} else {
			writUsrBox = dao.selectOne("cmn.postUsrSecReplyCmnPush",reqBox); // 대댓글 아이디 로 조회 후 그 사람에게 push 보내기
			if (writUsrBox == null) {
				throw new BizException("E135", new String[] {"push 파라미터 값을"}); // {0} 확인해 주세요.
			}
		}

		Box subBox = new Box();
		Box notificationBox = new Box();
		/*data mapping*/
		notificationBox.put("title" , "AIPEPPERS");
		notificationBox.put("body" , writUsrBox.nvl("sendUserNm") + " " + body); // 댓글 작성자 username
		notificationBox.put("badge" , "1");
		notificationBox.put("sound" , "default");
		notificationBox.put("icon" , "");
		notificationBox.put("type" , "push_notification");
		notificationBox.put("receiver_id" , writUsrBox.nvl("reciveId")); // 비디오 게시글 작성자의 id
		notificationBox.put("sender_id" , userId);	// 댓글을 단 사람의 아이디
		/*data setting*/
		subBox.put("to", writUsrBox.nvl("deviceToken"));
		subBox.put("priority", "high");
		subBox.put("notification", notificationBox);
		subBox.put("data", notificationBox);
		subBox.getBox("data").put("sender_id", userId);
		pushBox.add(subBox);
		PushNotification.pushNotification(pushBox); // PUSH 실행

		Box isrtBox = new Box();
		isrtBox.put("string",body);
		isrtBox.put("type", type);
		isrtBox.put("postId", Integer.parseInt(postId));
		isrtBox.put("senderId", userId);
		isrtBox.put("receiverId", writUsrBox.nvl("reciveId"));
		dao.insert("cmn.pushInfoInsert", isrtBox);
	}


	/**
	 * @Method Name : followPush
	 * @param userId
	 * @param body
	 * @param type
	 * @param videoId
	 * @param postId
	 */
	public void videoPostUpdatesPush (String userId, String body , String type , String videoId , String postId) {
		// push logic 만들기
		List<Box> pushBox = new ArrayList<Box>();
		Box reqBox = new Box();
		reqBox.put("userId", userId);
		List<Box> followBox = dao.selectList("cmn.pushNotiFollowList",reqBox);
		for(Box innerBox : followBox) { // PUSH 데이터 SETTING
			Box subBox = new Box();
			Box notificationBox = new Box();
			/*data mapping*/
			notificationBox.put("title" , "AIPEPPERS");
			notificationBox.put("body" , innerBox.nvl("senderUserName") + " " + body);
			notificationBox.put("badge" , "1");
			notificationBox.put("sound" , "default");
			notificationBox.put("icon" , "");
			notificationBox.put("type" , "push_notification");
			notificationBox.put("receiver_id" , userId); // 팔로우 받은 본인
			notificationBox.put("sender_id" , "");	// 나에게 팔로우를 보낸 사람들
			/*data setting*/
			subBox.put("to", innerBox.nvl("deviceToken"));
			subBox.put("priority", "high");
			subBox.put("notification", notificationBox);
			subBox.put("data", notificationBox);
			subBox.getBox("data").put("sender_id", innerBox.nvl("senderId"));
			pushBox.add(subBox);
		}
		PushNotification.pushNotification(pushBox); // PUSH 실행

		for (Box innerBox : followBox) {
			Box isrtBox = new Box();
			isrtBox.put("string",body);
			isrtBox.put("type", type);
			if("".equals(videoId) || videoId == null) {
				isrtBox.put("videoId", null);
			} else {
				isrtBox.put("videoId", Integer.parseInt(videoId));
			}
			if("".equals(postId) || postId == null) {
				isrtBox.put("postId", null);
			} else {
				isrtBox.put("postId", Integer.parseInt(postId));
			}
			isrtBox.put("senderId", innerBox.nvl("receiverId"));
			isrtBox.put("receiverId", innerBox.nvl("senderId"));
			dao.insert("cmn.pushInfoInsert", isrtBox);
		}
	}

	/**
	 * @Method Name : setFilterView
	 * @return
	 */
	public String setFilterView() {
		return dao.selectOne("cmn.setFilterView");
	}
}
