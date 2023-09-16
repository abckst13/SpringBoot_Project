package kr.aipeppers.pep.ui.mypage.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.mypage.controller.MyinfoController.ReqUIMPMI047Dto;
import kr.aipeppers.pep.ui.mypage.controller.MyinfoController.ReqUIMPMI049Dto;
import kr.aipeppers.pep.ui.mypage.service.MyinfoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyinfoComponent {

	@Autowired
	private MyinfoService myinfoService;

	/**
	 * 마이페이지 기본 데이터 조회
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box userView(Box box) throws Exception {
		return myinfoService.userView(box);
	}

	/**
	 * 팔로우/언팔로우
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int followOnOffSave(Box box) throws Exception {
		return myinfoService.followOnOffSave(box);
	}
	/*
	 * 팔로워 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> followerListView(Box box) throws Exception {
		return myinfoService.followerListView(box);
	}

	/*
	 * 팔로잉 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> followingListView(Box box) throws Exception {
		return myinfoService.followingListView(box);
	}

	/*
	 * 친구 추천
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> referFriendList(Box box) throws Exception {
		return myinfoService.referFriendList(box);
	}

	/*
	 * 선수 추천
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> referPlayerList(Box box) throws Exception {
		return myinfoService.referPlayerList(box);
	}

	/**
	 * 쇼페퍼 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myFavouriteVideosList(Box box) throws Exception{

		return myinfoService.myFavouriteVideosList(box);
	}

	/**
	 * @Method Name : videoDel
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int videoDel(Box box) throws Exception{

		return myinfoService.videoDel(box);
	}

	/**
	 * 해시태그 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myFavouriteHashtagsList(Box box) throws Exception{

		return myinfoService.myFavouriteHashtagsList(box);
	}

	/**
	 * 비디오 댓글 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> showVideoCommentsList(Box box) throws Exception{

		return myinfoService.showVideoCommentsList(box);
	}


	/**
	 * 나의 댓글 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myCommentsList(Box box) throws Exception{

		return myinfoService.myCommentsList(box);
	}


	/**
	 * 좋아요 영상 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myLikedVideosList(Box box) throws Exception{

		return myinfoService.myLikedVideosList(box);
	}

	/**
	 * 선택한 해당 영상
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box showVideoDetailView(Box box) throws Exception{

		return myinfoService.showVideoDetailView(box);
	}

	/**
	 * @Method Name : postCmnDel
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box postCmnDel(Box box) throws Exception{
		
		return myinfoService.postCmnDel(box);
	}

	/**
	 * 북마크 영상 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> showFavouriteVideosList(Box box) throws Exception{

		return myinfoService.showFavouriteVideosList(box);
	}

	/**
	 * 해시태그 영상 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> showVideosAgainstHashtagList(Box box) throws Exception{

		return myinfoService.showVideosAgainstHashtagList(box);
	}

	/**
	 * 내가 작성한 팬포스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myFanPostList(Box box) throws Exception{

		return myinfoService.myFanPostList(box);
	}

	public List<Box> fanpostList(Box box) throws Exception{
		
		return myinfoService.fanpostList(box);
	}

	/**
	 * FAQ 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> faqList(Box box) throws Exception{

		return myinfoService.faqList(box);
	}

	/**
	 * 적립 사용 내역 리스트
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myPointHistoryList(Box box) throws Exception{

		return myinfoService.myPointHistoryList(box);
	}

	/**
	 * 이벤트 응모내역
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myEventList(Box box) throws Exception{

		return myinfoService.myEventList(box);
	}

	/**
	 * 이용약관
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myHtmlPageView(Box box) throws Exception{

		return myinfoService.myHtmlPageView(box);
	}

	/**
	 * 추천친구삭제
	 * @param box
	 * @return
	 */
	public Box referFriendSave(Box box) throws Exception {
		return myinfoService.referFriendSave(box);
	}

	/**
	 * @Method Name : myPointHisList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box myPointHisList(Box box) throws Exception {
		return myinfoService.myPointHisList(box);
	}
	/**
	 * @Method Name : myTicketHistoryList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> myTicketHistoryList(Box box) throws Exception {
		return myinfoService.myTicketHistoryList(box);
	}

	/**
	 * @Method Name : eventApplyList
	 * @param box
	 * @return
	 */
	public List<Box> eventApplyList(Box box) throws Exception {
		return myinfoService.eventApplyList(box);
	}

	/**
	 * 현재사진삭제
	 * @Method Name : profileImageDelete
	 * @param box
	 * @return
	 */
	public Box profileImageDelete(Box box) throws Exception {
		return myinfoService.profileImageDelete(box);
	}

	/**
	 * 프로필 이미지 변경
	 * @Method Name : userImageUpdate
	 * @param box
	 * @return
	 */
	public Box userImageUpdate(Box box) throws Exception  {
		return myinfoService.userImageUpdate(box);
	}

	/**
	 * 유저 차단/ 차단해제
	 * @Method Name : userBlockSave
	 * @param box
	 * @return
	 */
	public Box userBlockSave(Box box) throws Exception  {
		return myinfoService.userBlockSave(box);
	}

	/**
	 * 나의 차단내역 조회
	 * @Method Name : myUserBlockList
	 * @param box
	 * @return
	 */
	public List<Box> myUserBlockList(Box box)throws Exception   {
		return myinfoService.myUserBlockList(box);
	}

	/**
	 * 닉네임 변경
	 * @Method Name : userNameUpdate
	 * @param box
	 * @return
	 */
	public int userNameUpdate(Box box)throws Exception   {
		return myinfoService.userNameUpdate(box);
	}

	/**
	 * 패스워드 변경
	 * @Method Name : myPwdUpdate
	 * @param box
	 * @return
	 */
	public int myPwdUpdate(Box box) throws  Exception {
		return myinfoService.myPwdUpdate(box);
	}

	/**
	 * 쇼페퍼 등록
	 * @Method Name : myShowPerpeperInsert
	 * @param box
	 * @return
	 */
	public Box myShowPerpeperInsert(Box box) throws  Exception {
		return myinfoService.myShowPerpeperInsert(box);
	}

	/**
	 * 회원탈퇴
	 * @Method Name : myUserSecessionUpdate
	 * @param box
	 * @return
	 */
	public int myUserSecessionUpdate(Box box)  throws  Exception {
		return myinfoService.myUserSecessionUpdate(box);
	}

	/**
	 * 포스트 등록
	 * @Method Name : myPostInsert
	 * @param box
	 * @return
	 */
	public int myPostInsert(Box box)  throws  Exception {
		return myinfoService.myPostInsert(box);
	}

	/**
	 * @Method Name : writePost
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int writePost(ReqUIMPMI047Dto reqDto)  throws  Exception {
		return myinfoService.writePost(reqDto);
	}

	/**
	 * @Method Name : editPost
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int editPost(ReqUIMPMI049Dto box)  throws  Exception {
		return myinfoService.editPost(box);


	}

	/**
	 * @Method Name : deletePost
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int deletePost(Box box)  throws  Exception {
		return myinfoService.deletePost(box);
	}

	/**
	 * @Method Name : episodeList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> episodeList(Box box)  throws  Exception {
		return myinfoService.episodeList(box);
	}

	/**
	 * @Method Name : getPostDetail
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box getPostDetail(Box box)  throws  Exception {
		return myinfoService.getPostDetail(box);
	}

	/**
	 * @Method Name : myPushNotificationView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box myPushNotificationView(Box box)  throws  Exception {
		return myinfoService.myPushNotificationView(box);
	}

	/**
	 * @Method Name : pushNotificationUpdate
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int pushNotificationUpdate(Box box)  throws  Exception {
		return myinfoService.pushNotificationUpdate(box);
	}

	/**
	 * @Method Name : writePostInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box writePostInsert(Box box)  throws  Exception {
		return myinfoService.writePostInsert(box);
	}

	/**
	 * @Method Name : getPostCommentList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> getPostCommentList(Box box)  throws  Exception {
		return myinfoService.getPostCommentList(box);
	}

	/**
	 * @Method Name : postLikeSave
	 * @param box
	 * @return
	 */
	public Box postLikeSave(Box box) {
		return myinfoService.postLikeSave(box);
	}

	/**
	 * @Method Name : postCommentLikeSave
	 * @param box
	 * @return
	 */
	public Box postCommentLikeSave(Box box) {
		return myinfoService.postCommentLikeSave(box);
	}

	/**
	 * @Method Name : postCommentReplyLikeSave
	 * @param box
	 * @return
	 */
	public Box postCommentReplyLikeSave(Box box) {
		return myinfoService.postCommentReplyLikeSave(box);
	}


	/**
	 * @Method Name : buyInterparkCode
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box buyInterparkCode(Box box) throws Exception {
		return myinfoService.buyInterparkCode(box);
	}
}