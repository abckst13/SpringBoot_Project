package kr.aipeppers.pep.ui.mypage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.aipeppers.pep.core.annotation.ReqInfo;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResIntDto;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResPageDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.mypage.component.MyinfoComponent;
import kr.aipeppers.pep.ui.search.controller.SrchController.ReqUISCIF001Dto;
import kr.aipeppers.pep.ui.search.controller.SrchController.ResUISCIF001HashtageDto;
import kr.aipeppers.pep.ui.search.controller.SrchController.ResUISCIF001UserDto;
import kr.aipeppers.pep.ui.search.controller.SrchController.ResUISCIF001VideoDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * ＃마이페이지 > 회원정보
 *
 * @author 김성태(Y5003979)
 */
@Slf4j
@RestController
@RequestMapping("restapi/mypage/myinfo")
@Api(tags = {"마이페이지>회원정보"}, description = "MyinfoController")
public class MyinfoController {

	@Autowired
	protected MyinfoComponent myinfoComp;

	@ApiOperation(value="팔로우/언팔로우")
	@PostMapping(value = "followOnOff") // v
	public ResIntDto followOnOff(@RequestBody ReqBOMPMI002Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.followOnOffSave(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="팔로워 리스트")
	@PostMapping(value="followerList")// v
	public ResListDto<ResBOMPMI003Dto> followerList(@RequestBody ReqBOMPMI003Dto reqDto) throws Exception {
		return new ResListDto<ResBOMPMI003Dto>(BeanUtil.convertList(myinfoComp.followerListView(BoxUtil.toBox(reqDto)), ResBOMPMI003Dto.class));
	}

	@ApiOperation(value="팔로잉 리스트")
	@PostMapping(value="followingList")// v
	public ResListDto<ResBOMPMI004Dto> followingList(@RequestBody ReqBOMPMI004Dto reqDto) throws Exception {
		return new ResListDto<ResBOMPMI004Dto>(BeanUtil.convertList(myinfoComp.followingListView(BoxUtil.toBox(reqDto)), ResBOMPMI004Dto.class));
	}

	@ApiOperation(value="친구 추천")
	@PostMapping(value="referFriendList") // v
	public ResListDto<ResBOMPMI005Dto> referFriendList(@RequestBody ReqBOMPMI005Dto reqDto) throws Exception {
		return new ResListDto<ResBOMPMI005Dto>(BeanUtil.convertList(myinfoComp.referFriendList(BoxUtil.toBox(reqDto)), ResBOMPMI005Dto.class));
	}

	@ApiOperation(value="선수 추천")
	@PostMapping(value="referPlayerList")  // v
	public ResListDto<ResBOMPMI006Dto> referPlayerList(@RequestBody ReqBOMPMI006Dto reqDto) throws Exception {
		return new ResListDto<ResBOMPMI006Dto>(BeanUtil.convertList(myinfoComp.referPlayerList(BoxUtil.toBox(reqDto)), ResBOMPMI006Dto.class));
	}

	@ApiOperation(value="추천친구삭제")
	@PostMapping(value="updateBlockreccomandUser") // v
	public ResResultDto<ResBOMPMI007Dto> updateBlockreccomandUser(@RequestBody ReqBOMPMI007Dto reqDto) throws Exception {
		return new ResResultDto<ResBOMPMI007Dto>(BeanUtil.convert(myinfoComp.referFriendSave(BoxUtil.toBox(reqDto)), ResBOMPMI007Dto.class));
	}

	@ApiOperation(value="현재 사진 삭제")
	@PostMapping(value="deleteProfile") // v
	public ResResultDto<ResBOMPMI008Dto> deleteProfile(@RequestBody ReqBOMPMI008Dto reqDto) throws Exception {
		return new ResResultDto<ResBOMPMI008Dto>(BeanUtil.convert(myinfoComp.profileImageDelete(BoxUtil.toBox(reqDto)), ResBOMPMI008Dto.class));
	}

	@ApiOperation(value="유저 차단 /차단 해제")
	@PostMapping(value="userblocking") // v
	public ResResultDto<ResBOMPMI009Dto> userBlockSave(@RequestBody ReqBOMPMI009Dto reqDto) throws Exception {
		return new ResResultDto<ResBOMPMI009Dto>(BeanUtil.convert(myinfoComp.userBlockSave(BoxUtil.toBox(reqDto)), ResBOMPMI009Dto.class));
	}

	@ApiOperation(value="나의 차단 내역")
	@PostMapping(value="getBlockUser") // v
	public ResListDto<ResBOMPMI010Dto> myUserBlockList(@RequestBody ReqBOMPMI010Dto reqDto) throws Exception {
		return new ResListDto<ResBOMPMI010Dto>(BeanUtil.convertList(myinfoComp.myUserBlockList(BoxUtil.toBox(reqDto)), ResBOMPMI010Dto.class));
	}

	@ApiOperation(value="닉네임 변경")
	@PostMapping(value="changeUsername")
	@ReqInfo(validForm="mypage.myinfo.changeUsername") // v
	public ResIntDto userNameUpdate(@RequestBody ReqBOMPMI011Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.userNameUpdate(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="패스워드 변경")
	@PostMapping(value="changePassword")
	@ReqInfo(validForm="mypage.myinfo.changePassword") // v
	public ResIntDto myPwdUpdate(@RequestBody ReqBOMPMI012Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.myPwdUpdate(BoxUtil.toBox(reqDto)), "I202");
	}

	@ApiOperation(value="마이페이지 기본데이터")
	@PostMapping(value = "myPageDefaultData") // v
	public ResResultDto<ResBOMPMI014Dto> myPageDefaultData(@RequestBody ReqBOMPMI014Dto reqDto) throws Exception {
		return new ResResultDto<ResBOMPMI014Dto>(BeanUtil.convert(myinfoComp.userView(BoxUtil.toBox(reqDto)), ResBOMPMI014Dto.class));
	}

	@ApiOperation(value="포스트 등록")
//	@PostMapping(value="writePost")
	public ResIntDto myPostInsert(@RequestBody ReqBOMPMI016Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.myPostInsert(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="회원 탈퇴")
	@PostMapping(value="withdrawUser")
	public ResIntDto myUserSecessionUpdate(@RequestBody ReqBOMPMI017Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.myUserSecessionUpdate(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="프로필 이미지 변경")
	@PostMapping(value="addUserImage") // 개발 필요
	@ReqInfo(validForm="mypage.myinfo.addUserImage") // v
	public ResResultDto<ResBOMPMI033Dto> userImageUpdate(@RequestBody ReqBOMPMI033Dto reqDto) throws Exception {
		return new ResResultDto<ResBOMPMI033Dto>(BeanUtil.convert(myinfoComp.userImageUpdate(BoxUtil.toBox(reqDto)), ResBOMPMI033Dto.class));
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value="포스트 댓글, 대댓글, 대대댓글 등록")
	@PostMapping(value="writePostComment") // 개발 필요
	@ReqInfo(validForm="mypage.myinfo.postCmmValidation") // v
	public <T> ResResultDto<T> writePostComment (@RequestBody ReqUIMPMI053Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		T resBox = null ;
		if ("comment".equals(reqDto.getType())) {
			resBox = (T) BeanUtil.convert(myinfoComp.writePostInsert(BoxUtil.toBox(reqDto)), ResUIMPMI053Dto.class);
		} else if ("reply".equals(reqDto.getType())) {
			resBox = (T) BeanUtil.convert(myinfoComp.writePostInsert(BoxUtil.toBox(reqDto)), ResUIMPMI053_ReDto.class);
		} else if ("replyreply".equals(reqDto.getType())) {
			resBox = (T)  BeanUtil.convert(myinfoComp.writePostInsert(BoxUtil.toBox(reqDto)), ResUIMPMI053_ReDto.class);
		}
		return new ResResultDto<T>(resBox, "I207");
	}

	/**
	 * @Method Name : getMyEvents
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "적립(PS), 사용(PU) 내역 리스트")
	@PostMapping(value = "searchPointHistory") // v
	public ResListDto<ResUIMPMI018Dto> searchPointHistory(@RequestBody ReqUIMPMI018Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI018Dto>(BeanUtil.convertList(myinfoComp.myPointHistoryList(box), ResUIMPMI018Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : searchMyInterparkCodeHistory
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "티켓 할인코드 구매 리스트 (인터파크 구매내역 )")
	@PostMapping(value = "searchMyInterparkCodeHistory")
	public ResListDto<ResUIMPMI019Dto> searchMyInterparkCodeHistory(@RequestBody ReqUIMPMI019Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI019Dto>(BeanUtil.convertList(myinfoComp.myTicketHistoryList(box), ResUIMPMI019Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}



	/**
	 * @Method Name : showVideosAgainstUserID
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "마이페이지>쇼페퍼 리스트(나 또는 다른 사람의 마이페이지 에서 보이는 쇼페퍼)")
	@PostMapping(value = "showVideosAgainstUserID")  // v
	public ResListDto<ResUIMPMI021Dto> showVideosAgainstUserID(@RequestBody ReqUIMPMI021Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI021Dto>(BeanUtil.convertList(myinfoComp.myFavouriteVideosList(box), ResUIMPMI021Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : showFavouriteHashtags
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "북마크>해시태그 리스트")
	@PostMapping(value = "showFavouriteHashtags") // V
	public ResListDto<ResUIMPMI022Dto> showFavouriteHashtags(@RequestBody ReqUIMPMI022Dto reqDto) throws Exception {
		return new  ResListDto<ResUIMPMI022Dto>(
				BeanUtil.convertList(myinfoComp.myFavouriteHashtagsList(BoxUtil.toBox(reqDto)), ResUIMPMI022Dto.class));
	}

	/**
	 * @Method Name : myComments
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = " 나의 댓글 리스트")
	@PostMapping(value = "myComments")  // v
	public ResListDto<ResUIMPMI023Dto> myComments(@RequestBody ReqUIMPMI023Dto reqDto) throws Exception {
		return new  ResListDto<ResUIMPMI023Dto>(
				BeanUtil.convertList(myinfoComp.myCommentsList(BoxUtil.toBox(reqDto)), ResUIMPMI023Dto.class));
	}

	/**
	 * @Method Name : showUserLikedVideos
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "좋아요 영상 리스트")
	@PostMapping(value = "showUserLikedVideos") // v
	public ResListDto<ResUIMPMI024Dto> showUserLikedVideos(@RequestBody ReqUIMPMI024Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI024Dto>(
				BeanUtil.convertList(myinfoComp.myLikedVideosList(box), ResUIMPMI024Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	@ApiOperation(value = "포스트 삭제")
	@PostMapping(value = "deletePostComment")
	public ResResultDto<ResUIMPMI060Dto> deletePostComment(@RequestBody ReqUIMPMI060Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResResultDto<ResUIMPMI060Dto>(BeanUtil.convert(myinfoComp.postCmnDel(box), ResUIMPMI060Dto.class));
	}



	/**
	 * @Method Name : myFanPost
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "내가 작성한 팬포스트")
	@PostMapping(value = "myFanPost")
	public ResListDto<ResUIMPMI025Dto> myFanPost(@RequestBody ReqUIMPMI025Dto reqDto) throws Exception {
		return new  ResListDto<ResUIMPMI025Dto>(
				BeanUtil.convertList(myinfoComp.myFanPostList(BoxUtil.toBox(reqDto)), ResUIMPMI025Dto.class));
	}

	@ApiOperation(value = "포스트(Fan) 리스트")
	@PostMapping(value = "fanpostList")
	public ResListDto<ResUIMPMI025Dto> fanpostList(@RequestBody ReqUIMPMI025Dto reqDto) throws Exception {
		return new  ResListDto<ResUIMPMI025Dto>(
				BeanUtil.convertList(myinfoComp.fanpostList(BoxUtil.toBox(reqDto)), ResUIMPMI025Dto.class));
	}



	/**
	 * @Method Name : getMyEvents
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "이벤트 응모내역")
	@PostMapping(value = "getMyEvents")
	public ResListDto<ResUIMPMI026Dto> getMyEvents(@RequestBody ReqUIMPMI026Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI026Dto>(BeanUtil.convertList(myinfoComp.myEventList(box), ResUIMPMI026Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : getFaqList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "FAQ 리스트")
	@PostMapping(value = "getFaqList")
	public ResListDto<ResUIMPMI027Dto> getFaqList(@RequestBody ReqUIMPMI027Dto reqDto) throws Exception {
		return new  ResListDto<ResUIMPMI027Dto>(
				BeanUtil.convertList(myinfoComp.faqList(BoxUtil.toBox(reqDto)), ResUIMPMI027Dto.class));
	}

	/**
	 * @Method Name : getHtmlPage
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "이용약관 개인정보 처리방침")
	@PostMapping(value = "getHtmlPage")
	public ResListDto<ResUIMPMI028Dto> getHtmlPage(@RequestBody ReqUIMPMI028Dto reqDto) throws Exception {
		return new  ResListDto<ResUIMPMI028Dto>(
				BeanUtil.convertList(myinfoComp.myHtmlPageView(BoxUtil.toBox(reqDto)), ResUIMPMI028Dto.class));
	}

	/**
	 * @Method Name : getMyPoint
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "나의 보유포인트 사용 내역")
	@PostMapping(value = "getMyPoint")
	public ResResultDto<ResUIMPMI049Dto> getMyPoint(@RequestBody ReqUIMPMI033Dto reqDto) throws Exception {
		return new  ResResultDto<ResUIMPMI049Dto>(BeanUtil.convert(myinfoComp.myPointHisList(BoxUtil.toBox(reqDto)), ResUIMPMI049Dto.class));
	}

	/**
	 * @Method Name : searchEventApplyHistory
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="카테고리 이벤트 내역 조회")
	@PostMapping(value = "searchEventApplyHistory")
	@ReqInfo(validForm = "mypage.myinfo.searchMenu")
	public ResListDto<ResUIMPMI050Dto> searchEventApplyHistory(@RequestBody ReqUIMPMI034Dto reqDto) throws Exception {
		return new ResListDto<ResUIMPMI050Dto>(BeanUtil.convertList(myinfoComp.eventApplyList(BoxUtil.toBox(reqDto)), ResUIMPMI050Dto.class));
	}

	/**
	 * @Method Name : showVideoComments
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "비디오 댓글 리스트")
	@PostMapping(value = "showVideoComments")
	public ResListDto<ResUIMPMI040Dto> showVideoComments(@RequestBody ReqUIMPMI040Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI040Dto>(BeanUtil.convertList(myinfoComp.showVideoCommentsList(box), ResUIMPMI040Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}


	/**
	 * @Method Name : showVideoDetail
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "선택한 해당 영상")
	@PostMapping(value = "showVideoDetail")
	public ResResultDto<ResUIMPMI044Dto> showVideoDetail(@RequestBody ReqUIMPMI044Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMPMI044Dto>(BeanUtil.convert(myinfoComp.showVideoDetailView(BoxUtil.toBox(reqDto)), ResUIMPMI044Dto.class));
	}

	/**
	 * @Method Name : showVideosAgainstHashtag
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "북마크 영상 리스트")
	@PostMapping(value = "showFavouriteVideos") // v
	public ResListDto<ResUIMPMI045Dto> showFavouriteVideos(@RequestBody ReqUIMPMI045Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI045Dto>(BeanUtil.convertList(myinfoComp.showFavouriteVideosList(box), ResUIMPMI045Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : showVideosAgainstHashtag
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "해시태그 영상 리스트")
	@PostMapping(value = "showVideosAgainstHashtag")
	public ResListDto<ResUIMPMI046Dto> showVideosAgainstHashtag(@RequestBody ReqUIMPMI046Dto reqDto) throws Exception {
		return new  ResListDto<ResUIMPMI046Dto>(
				BeanUtil.convertList(myinfoComp.showVideosAgainstHashtagList(BoxUtil.toBox(reqDto)), ResUIMPMI046Dto.class));
	}
	/**
	 * @Method Name : writePost
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "포스트 작성")
	@PostMapping(value = "writePost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	// @PostMapping(value="contentImgFileUld", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResIntDto writePost(ReqUIMPMI047Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.writePost(reqDto));
	}

	@ApiOperation(value = "포스트 댓글 List 조회")
	@PostMapping(value = "getPostComment")
	public ResListDto<ResUIMPMI059Dto> getPostComment(@RequestBody ReqUIMPMI059Dto reqDto) throws Exception {
		return new ResListDto<ResUIMPMI059Dto>(BeanUtil.convertList(myinfoComp.getPostCommentList(BoxUtil.toBox(reqDto)), ResUIMPMI059Dto.class));
	}

	/**
	 * @Method Name : writePost
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "포스트 수정")
	@PostMapping(value = "editPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResIntDto editPost(ReqUIMPMI049Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.editPost(reqDto));
	}

	/**
	 * @Method Name : deletePost
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "포스트 삭제")
	@PostMapping(value = "deletePost")
	public ResIntDto deletePost(@RequestBody ReqUIMPMI050Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.deletePost(BoxUtil.toBox(reqDto)));
	}

	/**
	 * @Method Name : episodeList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "포스트 조회")
	@PostMapping(value = "episodeList")
	public ResListDto<ResUIMPMI051Dto> episodeList(@RequestBody ReqUIMPMI051Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUIMPMI051Dto>(BeanUtil.convertList(myinfoComp.episodeList(box), ResUIMPMI051Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : getPostDetail
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "포스트 상세 조회")
	@PostMapping(value = "getPostDetail")
	public ResResultDto<ResUIMPMI054Dto> getPostDetail(@RequestBody ReqUIMPMI054Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResResultDto<ResUIMPMI054Dto>(BeanUtil.convert(myinfoComp.getPostDetail(box), ResUIMPMI054Dto.class));
	}

	/**
	 * @Method Name : deleteVideo
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "비디오 삭제")
	@PostMapping(value = "deleteVideo")
	public ResIntDto deleteVideo(@RequestBody ReqUIMPMI052Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.videoDel(BoxUtil.toBox(reqDto)), "I206");
	}

	/**
	 * @Method Name : getPushNotificationList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="환경설정>알림,광고성알림 정보 가져오기")
	@PostMapping(value="getPushNotificationList")
	public ResResultDto<ResUIMPMI030Dto > getPushNotificationList(@RequestBody ReqUIMPMI030Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMPMI030Dto >(BeanUtil.convert(myinfoComp.myPushNotificationView(BoxUtil.toBox(reqDto)), ResUIMPMI030Dto .class));
	}

	/**
	 * @Method Name : updatePushNotificationSettings
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="알림 변경")
	@PostMapping(value="updatePushNotificationSettings")
	public ResIntDto updatePushNotificationSettings(@RequestBody ReqUIMPMI029Dto reqDto) throws Exception {
		return new ResIntDto(myinfoComp.pushNotificationUpdate(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="포스트 좋아요")
	@PostMapping(value="postLike")
	public ResResultDto<ResUIMPMI055Dto> postLike(@RequestBody ReqUIMPMI055Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMPMI055Dto>(BeanUtil.convert(myinfoComp.postLikeSave(BoxUtil.toBox(reqDto)), ResUIMPMI055Dto.class));
	}

	@ApiOperation(value="포스트 댓글 좋아요")
	@PostMapping(value="postCommentLike")
	public ResResultDto<ResUIMPMI056Dto> postCommentLike(@RequestBody ReqUIMPMI056Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMPMI056Dto>(BeanUtil.convert(myinfoComp.postCommentLikeSave(BoxUtil.toBox(reqDto)), ResUIMPMI056Dto.class));
	}

	@ApiOperation(value="포스트 대댓글 , 대대댓글 좋아요")
	@PostMapping(value="postCommentReplyLike")
	public ResResultDto<ResUIMPMI057Dto> postCommentReplyLike(@RequestBody ReqUIMPMI057Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMPMI057Dto>(BeanUtil.convert(myinfoComp.postCommentReplyLikeSave(BoxUtil.toBox(reqDto)), ResUIMPMI057Dto.class));
	}

	/**
	 * @Method Name : buyInterparkCode
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "인터파크 할인코드 구매")
	@PostMapping(value = "buyInterparkCode")
	public ResResultDto<ResUIMPMI058Dto> buyInterparkCode(@RequestBody ReqUIMPMI058Dto reqDto) throws Exception {
		return new  ResResultDto<ResUIMPMI058Dto>(BeanUtil.convert(myinfoComp.buyInterparkCode(BoxUtil.toBox(reqDto)), ResUIMPMI058Dto.class));
	}

// ================================REQDTO=========================================================================

	@Data
	@ApiModel
	public static class ReqUIMPMI059Dto {
		@Schema(description = "포스트 아이디", example="0")
		@JsonAlias("postId")
		private String post_id;
		@Schema(description = "create ==> 최근 순 , like ==> 좋아요 순", example="create")
		private String order;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI055Dto {
		@Schema(description = "포스트 아이디", example="0")
		@JsonAlias("postId")
		private int post_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI056Dto {
		@Schema(description = "댓글 아이디", example="0")
		@JsonAlias("commentId")
		private int comment_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI057Dto {
		@Schema(description = "댓글 아이디", example="0")
		@JsonAlias("commentId")
		private int comment_id;
	}


	@Data
	@ApiModel
	public static class ReqUIMPMI058Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMPMI060Dto {
		@Schema(description = "댓글 타입 (comment: 댓글, reply: 대댓글(대대댓글))")
		private String type;
		@Schema(description = "댓글 / 대댓글 아이디")
		private String comment_id;
		@Schema(description = "대대댓글 아이디")
		private String reply_comment_id;

	}

	@Data
	@ApiModel
	public static class ReqBOMPMI014Dto {
		@Schema(description = "조회 할 상대 user Id", example="EEDxEpxj43YCI7EictWjCA==")
		@JsonAlias("otherUserId(aes  암호화 상태로 값 )")
		private String other_user_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI018Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "포인트 타입", example="PS")
		@JsonAlias("pointType")
		private String point_type;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI019Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "타입", example="")
		private String type;
	}



	@Data
	@ApiModel
	public static class ReqUIMPMI021Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "리로딩 여부", example = "false")
		private boolean again;
		@Schema(description = "조회 할 상대 user Id", example="GKJM/194V/GE/Tk74SbuXQ==")
		@JsonAlias("otherUserId")
		private String other_user_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI022Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMPMI023Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMPMI024Dto {
		@Schema(description = "페이지")
		private int page;
		@Schema(description = "전체 조회 여부")
		private Boolean again;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI053Dto {
	   @Schema(description = "일반댓글일 때 :comment, 대댓글일 때 : reply ,대대댓글일 때 : replyreply", example = "")
	   private String type;
	   @Schema(description = "포스트 내용", example = "테스트 포스트 내용입니다")
	   private String comment ;
	   @Schema(description = "포스트 아이디(댓글)", example = "\"0\"")
	   @JsonAlias("postId")
	   private String post_id ;
	   @Schema(description = "댓글 아이디(대댓글)", example = "\"0\"")
	   @JsonAlias("commentId")
	   private String comment_id ;
	   @Schema(description = "대댓글 아이디(대대댓글)", example = "\"0\"")
	   @JsonAlias("replyCommentId")
	   private String reply_comment_id ;
	}


	@Data
	@ApiModel
	public static class ReqUIMPMI025Dto {
		@Schema(description = "RECENT: 최근, LIKE:좋아요순", example = "RECENT")
		private String order;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI026Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI027Dto {
		@Schema(description = "faq 구분코드 (type_cd : FAQ01 (회원), FAQ02 (이벤트), FAQ03 (포인트), FAQ04 (티켓))", example = "FAQ01")
		@JsonAlias("typeCd")
		private String type_cd;
	}


	@Data
	@ApiModel
	public static class ReqUIMPMI028Dto {
		@Schema(description = "privacyPolicy: 개인정보, termsConditons: 이용약관", example = "privacyPolicy")
		private String name;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI033Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMPMI034Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMPMI040Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "조회할 video Id", example="150")
		@JsonAlias("videoId")
		private int video_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI044Dto {
		@Schema(description = "비디오 ID", example = "\"1\"")
		private String video_id;

	}

	@Data
	@ApiModel
	public static class ReqUIMPMI045Dto {
		@Schema(description = "페이지", example = "\"1\"")
		private int page;
		@Schema(description = "again", example = "\"false\"")
		private String again;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI046Dto {
		@Schema(description = "해시태그 아이디", example = "\"8\"")
		@JsonAlias("hashtagId")
		private String hashtag_id;
	}

	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = false)
	@ApiModel("이미지 파일 업로드 요청 Dto")
	public static class ReqUIMPMI047Dto {

		@Schema(description = "플레이어/유저 마이페이지 들어갈 때 필요한 User ID")
		private String targetId;
		@Schema(description = "제목", example = "테스트제목")
		private String title;
		@Schema(description = "내용", example = "테스트를 위한 내용입니다. 내용을 작성해주세요.")
		private String contents;

		@NotNull
		@JsonProperty("image")
		@JsonPropertyDescription("포스트 이미지")
		@Schema(description = "포스트 이미지")
		@Valid
		private MultipartFile[] image;
	}

	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = false)
	@ApiModel("이미지 파일 업로드 요청 Dto")
	public static class ReqUIMPMI049Dto {
		@Schema(description = "포스트 아이디", example = "아이디를 입력하세요.")
		private String post_id;
		@Schema(description = "제목", example = "테스트제목2")
		private String title;
		@Schema(description = "내용", example = "테스트를 위한 내용입니다. 내용을 작성해주세요.2")
		private String contents;
		@JsonAlias("image_ids")
		@Schema(description = "이미지 파일 목록", example = "1,2,3,4,5")
		private String image_ids;

		@NotNull
		@JsonProperty("image")
		@JsonPropertyDescription("포스트 이미지")
		@Schema(description = "포스트 이미지")
		@Valid
		private MultipartFile[] image;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI048Dto {
		@Schema(description = "아이디", example = "\"8\"")
		private String id;
		@Schema(description = "제목")
		private String title;
		@Schema(description = "내용")
		private String contents;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI050Dto {
		@Schema(description = "아이디", example = "\"8\"")
		private String id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI051Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "정렬", example = "regDt")
		private String order;
		@Schema(description = "플레이어/유저 마이페이지 들어갈 때 필요한 User ID", example = "GKJM/194V/GE/Tk74SbuXQ==")
		private String target_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI052Dto {
		@Schema(description = "비디오 아이디", example = "\"8\"")
		@JsonAlias("videoId")
		private String video_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI054Dto {
		@Schema(description = "포스트 아이디", example = "아이디를 입력하세요.")
		private String post_id;
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI030Dto {
	}

	@Data
	@ApiModel
	public static class ReqUIMPMI029Dto {
		@Schema(description = "일반 알림 여부" , example = "0")
		private String notice;
		@Schema(description = "이벤트 알림 여부" , example = "0")
		private String event;
	}

	//==================== RES =======================

	@Data
	@ApiModel
	public static class ResUIMPMI018Dto {
		@Schema(description = "포인트 히스토리 키")
		private String id;
		@Schema(description = "사용자 ID")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "포인트??")
		private String point;
		@Schema(description = "사용 포인트??")
		@JsonAlias("usePoint")
		private String use_point;
		@Schema(description = "포인트 타입 ( PS : point save, PU : point use )")
		@JsonAlias("pointType")
		private String point_type;
		@Schema(description = "포인트 타입 명 ( PS : point save, PU : point use )")
		@JsonAlias("pointTypeNm")
		private String point_type_nm;
		@Schema(description = "설명")
		private String description;
		@JsonAlias("regDt")
		private String reg_dt;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI019Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "코드")
		private String code;
		@Schema(description = "사용자 ID")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "사용여부")
		@JsonAlias("usedYn")
		private String used_yn;
		@Schema(description = "쓴 날짜")
		@JsonAlias("usedDate")
		private String used_date;
		@Schema(description = "")
		@JsonAlias("expiredDate")
		private String expired_date;
		@Schema(description = "")
		@JsonAlias("createdAt")
		private String created_at;
		@Schema(description = "등록자")
		@JsonAlias("regId")
		private String reg_id;
		@Schema(description = "등록일")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "수정자")
		@JsonAlias("modId")
		private String mod_id;
		@Schema(description = "수정일")
		@JsonAlias("modDt")
		private String mod_dt;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI021Dto {
		@JsonProperty("Video")
		@JsonAlias("video")
		private Video video;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;

		@Data
		@ApiModel
		public static class Video {
			@JsonProperty("hashtags")
			@JsonAlias("hashtags")
			private String[] hashtags;

			@Schema(description = "아이디")
			private String id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "해시코드(영상설명)")
			private String description;
			@Schema(description = "비디오 url")
			private String videoUrl;
			@Schema(description = "url")
			private String thum;
			@Schema(description = "썸네일 url")
			private String gif;
			@Schema(description = "섹션 여부")
			private String section;
			@Schema(description = "활성화 여부")
			@JsonAlias("videoActive")
			private String video_active;
			@Schema(description = "조회수")
			private int view;
			@Schema(description = "사운드 아이디")
			@JsonAlias("soundId")
			private int sound_id;
			@Schema(description = "공개여부")
			@JsonAlias("privacyType")
			private String privacy_type;
			@Schema(description = "댓글 허용/비허용")
			@JsonAlias("allowComments")
			private String allow_comments;
			@Schema(description = "")
			private int block;
			@Schema(description = "영상길이")
			private float duration;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "좋아요")
			private String like;
			@Schema(description = "")
			private String favourite;
			@Schema(description = "댓글 수")
			@JsonAlias("commentCount")
			private String comment_count;
			@Schema(description = "좋아요 수")
			@JsonAlias("likeCount")
			private String like_count;

//			@Data
//			@ApiModel
//			public static class Hashtags {
//				@Schema(description = "해시태그")
//				private String[] hashtags;
//			}
		}
		@Data
		@ApiModel
		public static class User {
			@JsonProperty("PushNotification")
			@JsonAlias("pushNotification")
			private PushNotification pushNotification;
			@JsonProperty("PrivacySetting")
			@JsonAlias("privacySetting")
			private List<PrivacySetting> privacySetting;

			@Schema(description = "회원 아이디")
			private String id;
			@Schema(description = "auth 토큰")
			@JsonAlias("authToken")
			private String auth_token;
			@Schema(description = "이메일")
			private String email;
			@Schema(description = "핸드폰번호")
			private String phone;
			@Schema(description = "사용자 이름")
			private String username;
			@Schema(description = "권한")
			private String role;
			@Schema(description = "성")
			private String first_name;
			@Schema(description = "이름")
			private String last_name;
			@Schema(description = "프로필 1( ios )")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필2 (안드로이드)")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;

			@Data
			@ApiModel
			public static class PushNotification {
				@Schema(description = "사용자 id와 매핑")
				private String id;
				@Schema(description = "좋아요 알람 이거로 검사")
				private String likes;
				@Schema(description = "댓글알람")
				private String comments;
				@Schema(description = "팔로우 알람")
				@JsonAlias("newFollowers")
				private String new_followers;
				@Schema(description = "멘션 알람")
				private String mentions;
				@Schema(description = "다이렉트 메세지")
				@JsonAlias("directMessages")
				private String direct_messages;
				@Schema(description = "비디오 알람")
				@JsonAlias("videoUpdates")
				private String video_updates;
				@Schema(description = "포스트 알람")
				@JsonAlias("postUpdate")
				private String post_update;
				@Schema(description = "광고 알람")
				private String event;
			}

			@Data
			@ApiModel
			public static class PrivacySetting {

			}
		}

	}

	@Data
	@ApiModel
	public static class ResUIMPMI022Dto {
		@JsonProperty("Hashtag")
		@JsonAlias("hashtag")
		private Hashtag hashtag;
		@JsonProperty("HashtagFavourite")
		@JsonAlias("hashtagFavourite")
		private HashtagFavourite hashtagFavourite;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;

		@Data
		@ApiModel
		public static class Hashtag {
			@Schema(description = "해시태그 아이디")
			private String id;
			@Schema(description = "해시태그 명")
			private String name;
			@Schema(description = "해시태그에 있는 비디오의 cnt ")
			@JsonAlias("videosCount")
			private int videos_count;
			@Schema(description = "해시태그에 있는 비디오 영상 view 수")
			private int views;
		}

		@Data
		@ApiModel
		public static class HashtagFavourite {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "해시태그 아이디")
			@JsonAlias("hashtagId")
			private String hashtag_id;
			@Schema(description = "생성 일자")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class User {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "user 탈퇴 유무")
			private boolean active;
			@Schema(description = "사용자 이름")
			private String username;
			@Schema(description = "권한")
			private String role;
			@Schema(description = "회원등급")
			private String grade;
			@Schema(description = "프로필 사진")
			private String profile_pic;
			@Schema(description = "썸네일")
			private String profile_pic_small;
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI023Dto {
		@JsonProperty("ParentContent")
		@JsonAlias("parentContent")
		private ParentContent parentContent;

		@Schema(description = "포스트 ID")
		private String id;
		@Schema(description = "등록자 ID")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "포스트 ID")
		@JsonAlias("postId")
		private String post_id;
		@Schema(description = "포스트 comment")
		private String contents;
		@Schema(description = "등록일자")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "포스트 삭제 여부")
		@JsonAlias("commentActive")
		private String comment_active;
		@Schema(description = "비디오 아이디")
		@JsonAlias("videoId")
		private String video_id;
		@Schema(description = "좋아요 여부")
		private String isLike;
		@Schema(description = "좋아요 총 카운트 수")
		@JsonAlias("likeCount")
		private String like_count;
		@Schema(description = "댓글 타입")
		@JsonAlias("commentType")
		private String comment_type;
		@Schema(description = "댓글(comment), 대댓글(reply), 대대댓글 여부(reply_reply)")
		@JsonAlias("commentReplyComfirm")
		private String comment_reply_comfirm;
		@Schema(description = "댓글작성 기간")
		@JsonAlias("diffDate")
		private String diff_date;

		@Data
		@ApiModel
		public static class ParentContent {
			@JsonProperty("User")
			@JsonAlias("user")
			private User user;

			@Schema(description = "포스트 ID")
			private String id;
			@Schema(description = "포스트 타입 (show_pepper일 경우 비디오)")
			private String type;
			@Schema(description = "POST 내용")
			private String contents;
			@Schema(description = "소유자 유저 ID")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "포스티 삭제 여부")
			@JsonAlias("postActive")
			private String post_active;
			@Schema(description = "비디오 삭제 여부")
			@JsonAlias("videoActive")
			private String video_active;
			@Schema(description = "타겟 아이디")
			@JsonAlias("targetId")
			private String target_id;
			@Schema(description = "제목")
			private String title;
			@Schema(description = "영상내용")
			private String description;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "등록자 탈퇴 여부")
			private String result;
			@Data
			@ApiModel
			public static class User {
				@Schema(description = "아이디")
				private String id;
				@Schema(description = "등록자 이름")
				private String username;
				@Schema(description = "유저 권한 ( user, player )")
				private String role;
				@Schema(description = "프로필1 (IOS)")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "프로필2 (안드로이드)")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
			}
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI024Dto { // dto
		@Schema(description = "비디오 키")
		private String id;
		@Schema(description = "비디오 작성자 ID??")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "utf8mb4_unicode_ci(영상내용)")
		private String description;
		@Schema(description = "비디오 경로")
		private String video;
		@Schema(description = "썸네일?")
		private String thum;
		@Schema(description = "썸네일 경로")
		private String gif;
		@Schema(description = "활성화여부 디폴트 1")
		private String video_active;
		@Schema(description = "시청건수")
		private String view;
		@Schema(description = "?? 디폴트 0")
		private String section;
		@Schema(description = "사운드 아이디? 디폴트 0")
		@JsonAlias("soundId")
		private String sound_id;
		@Schema(description = "public or private / defalut public")
		@JsonAlias("privacyType")
		private String privacy_type;
		@Schema(description = "true=allow  and false=not allowed 디폴트 true")
		@JsonAlias("allowComments")
		private String allow_comments;
		@Schema(description = "0 or 1  1=active 디폴트 0")
		@JsonAlias("allowDuet")
		private String allow_duet;
		@Schema(description = "0= video is active 1= video is not active and hide from public")
		private String block;
		@Schema(description = "듀엣 하는 비디오 아이디?")
		@JsonAlias("duetVideoId")
		private String duet_video_id;
		@Schema(description = "??")
		@JsonAlias("oldVideoId")
		private String old_video_id;
		@Schema(description = "영상시간")
		private String duration;
		@Schema(description = "홍보?")
		private String promote;
		@Schema(description = "등록자")
		@JsonAlias("regId")
		private String reg_id;
		@Schema(description = "등록일")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "수정자")
		@JsonAlias("modId")
		private String mod_id;
		@Schema(description = "수정일")
		@JsonAlias("modDt")
		private String mod_dt;
	}


	@Data
	@ApiModel
	public static class ResUIMPMI025Dto {
		@JsonProperty("Post")
		@JsonAlias("post")
		private Post post;
		@JsonProperty("PostImage")
		@JsonAlias("postImage")
		private List<PostImage> postImage;


		@Data
		@ApiModel
		public static class Post {
			@JsonProperty("Player")
			@JsonAlias("player")
			private Player player;

			@Schema(description = "포스트 키")
			private String id;
			@Schema(description = "포스트 타입")
			private String type;
			@Schema(description = "소유자 유저 ID")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "플레이어/유저 마이페이지 들어갈 때 필요한 User ID")
			@JsonAlias("targetId")
			private String target_id;
			@Schema(description = "제목")
			private String title;
			@Schema(description = "내용")
			private String contents;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "삭제 시 : 0")
			@JsonAlias("postActive")
			private String post_active;
			@Schema(description = "비활성화 한 시간, active, block에서 사용")
			@JsonAlias("postActiveDate")
			private String post_active_date;
			@Schema(description = "신고로 인한 차단시 : 1")
			private String block;

			@Data
			@ApiModel
			public static class Player {
				@Schema(description = "아이디")
				private String id;
				@Schema(description = "등록자 이름")
				private String username;
				@Schema(description = "유저 권한 ( user, player )")
				private String role;
				@Schema(description = "프로필1 (IOS)")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "프로필2 (안드로이드)")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
				@Schema(description = "active")
				private String active;

			}


			@Schema(description = "댓글 개수")
			@JsonAlias("commentCnt")
			private String comment_cnt;
			@Schema(description = "좋아요 개수")
			@JsonAlias("likeCnt")
			private String like_cnt;
			@Schema(description = "글등록후지난시간")
			@JsonAlias("diffDate")
			private String diff_date;
			@Schema(description = "좋아요 여부")
			@JsonAlias("likeYn")
			private String like_yn;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "수정자")
			@JsonAlias("modId")
			private String mod_id;
			@Schema(description = "수정일")
			@JsonAlias("modDt")
			private String mod_dt;
		}

		@Data
		@ApiModel
		public static class PostImage {
			@Schema(description = "포스트이미지 ID")
			private String id;
			@Schema(description = "포스트 id")
			@JsonAlias("postId")
			private String post_id;
			@Schema(description = "이미지 url")
			@JsonAlias("imageUrl")
			private String image_url;
			@Schema(description = "이미지 사이즈")
			@JsonAlias("imageSize")
			private String image_size;
			@Schema(description = "가로")
			private String width;
			@Schema(description = "높이")
			private String height;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "수정자")
			@JsonAlias("modId")
			private String mod_id;
			@Schema(description = "수정일")
			@JsonAlias("modDt")
			private String mod_dt;
		}
	}



	@Data
	@ApiModel
	public static class ResUIMPMI026Dto {
		@Schema(description = "이벤트 응모내역 키")
		private String id;
		@Schema(description = "응모한 사용자 ID")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "해당 이벤트 ID")
		@JsonAlias("eventRelId")
		private String event_rel_id;
		@Schema(description = "이벤트 타입 유형")
		@JsonAlias("eventType")
		private String event_type;
		@Schema(description = "이벤트 설명")
		@JsonAlias("eventDesc")
		private String event_desc;
		@Schema(description = "등록자")
		@JsonAlias("regId")
		private String reg_id;
		@Schema(description = "등록일")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "수정자")
		@JsonAlias("modId")
		private String mod_id;
		@Schema(description = "수정일")
		@JsonAlias("modDt")
		private String mod_dt;
	}

		@Data
	@ApiModel
	public static class ResUIMPMI027Dto {
		@Schema(description = "faq 키")
		private String id;
		@Schema(description = "faq 타입 코드")
		@JsonAlias("typeCd")
		private String type_cd;
		@Schema(description = "질문")
		private String title;
		@Schema(description = "답변")
		private String content;
		@Schema(description = "조회수")
		private String hits;
		@Schema(description = "삭제여부")
		@JsonAlias("delYn")
		private String del_yn;
		@Schema(description = "사용여부")
		@JsonAlias("useYn")
		private String use_yn;
		@Schema(description = "등록자")
		@JsonAlias("regId")
		private String reg_id;
		@Schema(description = "등록일")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "수정자")
		@JsonAlias("modId")
		private String mod_id;
		@Schema(description = "수정일")
		@JsonAlias("modDt")
		private String mod_dt;
	}


	@Data
	@ApiModel
	public static class ResUIMPMI028Dto {
		@Schema(description = "html_page 키")
		private String id;
		@Schema(description = "html_page 이름")
		private String name;
		@Schema(description = "내용")
		private String text;
		@Schema(description = "등록일")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "등록자")
		@JsonAlias("regId")
		private String reg_id;
		@Schema(description = "수정자")
		@JsonAlias("modId")
		private String mod_id;
		@Schema(description = "수정일")
		@JsonAlias("modDt")
		private String mod_dt;
		@Schema(description = "사용여부")
		private String useYn;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI040Dto {
		@JsonProperty("User")
		@JsonAlias("user")
		private UserInfo user;
		@JsonProperty("VideoCommentReply")
		@JsonAlias("videoCommentReply")
		private List<VideoCommentReply> videoCommentReply;

		@Schema(description = "댓글 아이디")
		private String id;
		@Schema(description = "사용자 ID")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "비디오 ID")
		@JsonAlias("videoId")
		private String video_id;
		@Schema(description = "댓글 내용")
		private String comment;
		@Schema(description = "댓글 활성 여부")
		@JsonAlias("commentActive")
		private String comment_active;
		@Schema(description = "댓글 작성 시간")
		@JsonAlias("diffDate")
		private String diff_date;
		@Schema(description = "좋아요 여부")
		private String isLike;
		@Schema(description = "총 좋아요 수")
		@JsonAlias("likeCount")
		private String like_count;

		@Data
		@ApiModel
		public static class UserInfo {
			@Schema(description = "user_id 값")
			private String id;
			@Schema(description = "닉네임")
			private String username;
			@Schema(description = "성")
			@JsonAlias("firstName")
			private String first_name;
			@Schema(description = "이름")
			@JsonAlias("lastName")
			private String last_name;
			@Schema(description = "aod 이미지")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "ios 이미지")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
			@Schema(description = "활동 여부")
			private String active;
			@Schema(description = "권한")
			private String role;
		}

		@Data
		@ApiModel
		public static class VideoCommentReply {
			@JsonProperty("User")
			@JsonAlias("user")
			private UserReInfo userRe;

			@Schema(description = "아이디")
			private String id;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			private String video_id;
			@Schema(description = "등록자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "댓글 아이디")
			@JsonAlias("commentId")
			private String comment_id;
			@Schema(description = "대대댓글 등록시 대댓글 아이디")
			@JsonAlias("replyCommentId")
			private String reply_comment_id;
			@Schema(description = "댓글 내용")
			private String comment;
			@Schema(description = "등록 일시")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "댓글 사용 여부")
			@JsonAlias("commentActive")
			private String comment_active;
			@Schema(description = "")
			@JsonAlias("replyReply")
			private String reply_reply;
			@Schema(description = "등록 일시")
			@JsonAlias("diffDate")
			private String diff_date;
			@Schema(description = "좋아요 여부")
			private String isLike;
			@Schema(description = "총 좋아요 카운트 수")
			@JsonAlias("likeCnt")
			private String like_cnt;

			@Data
			@ApiModel
			public static class UserReInfo {
				@Schema(description = "user_id 값")
				private String id;
				@Schema(description = "닉네임")
				private String username;
				@Schema(description = "aod 이미지")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "ios 이미지")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
				@Schema(description = "활동 여부")
				private String active;
				@Schema(description = "권한")
				private String role;
			}
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI044Dto {
		@JsonProperty("Video")
		@JsonAlias("video")
		private Video video;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("Sound")
		@JsonAlias("sound")
		private Sound sound;
		@JsonProperty("VideoComment")
		@JsonAlias("videoComment")
		private List<VideoComment> videoComment;

		@Data
		@ApiModel
		public static class Video {
			@Schema(description = "비디오 키")
			private String id;
			@Schema(description = "비디오 작성자 ID??")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "utf8mb4_unicode_ci(영상내용)")
			private String description;
			@Schema(description = "비디오 경로")
			private String video;
			@Schema(description = "썸네일?")
			private String thum;
			@Schema(description = "썸네일 경로")
			private String gif;
			@Schema(description = "시청건수")
			private String view;
			@Schema(description = "?? 디폴트 0")
			private String section;
			@Schema(description = "사운드 아이디? 디폴트 0")
			@JsonAlias("soundId")
			private String sound_id;
			@Schema(description = "public or private / defalut public")
			@JsonAlias("privacyType")
			private String privacy_type;
			@Schema(description = "true=allow  and false=not allowed 디폴트 true")
			@JsonAlias("allowComments")
			private String allow_comments;
			@Schema(description = "0 or 1  1=active 디폴트 0")
			@JsonAlias("allowDuet")
			private String allow_duet;
			@Schema(description = "0= video is active 1= video is not active and hide from public")
			private String block;
			@Schema(description = "듀엣 하는 비디오 아이디?")
			@JsonAlias("duetVideoId")
			private String duet_video_id;
			@Schema(description = "??")
			@JsonAlias("oldVideoId")
			private String old_video_id;
			@Schema(description = "영상시간")
			private String duration;
			@Schema(description = "홍보?")
			private String promote;
			@Schema(description = "활성화여부 디폴트 1")
			private String video_active;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String reg_dt;

			@Schema(description = "좋아요 여부?")
			private int like;
			@Schema(description = "북마크?..")
			private int favourite;
			@Schema(description = "좋아요 개수")
			@JsonAlias("likeCount")
			private int like_count;
			@Schema(description = "댓글 개수")
			@JsonAlias("commentCount")
			private int comment_count;
		}

		@Data
		@ApiModel
		public static class User {
			@JsonProperty("PushNotification")
			@JsonAlias("pushNotification")
			private PushNotification pushNotification;
			@JsonProperty("PrivacySetting")
			@JsonAlias("privacySetting")
			private PrivacySetting privacySetting;

			@Schema(description = "아이디")
	        private String id;
			@Schema(description = "닉네임")
	        private String username;
			@Schema(description = "이메일")
	        private String email;
			@Schema(description = "이미지 1")
			@JsonAlias("profilePic")
	        private String profile_pic;
			@Schema(description = "이미지 2")
			@JsonAlias("profilePicSmall")
	        private String profile_pic_small;
			@Schema(description = "사용자 탈퇴 여부")
	        private String active;
			@Schema(description = "사용자 권한")
	        private String role;
			@Schema(description = "디바이스 토큰값")
			@JsonAlias("deviceToken")
	        private String device_token;
			@Schema(description = "팔로우 버튼 여부")
			private String button;

			@Data
			@ApiModel
			public static class PushNotification {
				@Schema(description = "사용자 id와 매핑")
				private String id;
				@Schema(description = "좋아요 알람 이거로 검사")
				private String likes;
				@Schema(description = "댓글알람")
				private String comments;
				@Schema(description = "팔로우 알람")
				@JsonAlias("newFollowers")
				private String new_followers;
				@Schema(description = "멘션 알람")
				private String mentions;
				@Schema(description = "다이렉트 메세지")
				@JsonAlias("directMessages")
				private String direct_messages;
				@Schema(description = "비디오 알람")
				@JsonAlias("videoUpdates")
				private String video_updates;
				@Schema(description = "포스트 알람")
				@JsonAlias("postUpdate")
				private String post_update;
				@Schema(description = "광고 알람")
				private String event;
			}

			@Data
			@ApiModel
			public static class PrivacySetting {
			}
		}

		@Data
		@ApiModel
		public static class Sound {
			@Schema(description = "사운드 id")
			private String id;
//			@Schema(description = "??")
//			private String audio;
//			@Schema(description = "??")
//			private String duration;
//			@Schema(description = "??")
//			private String name;
//			@Schema(description = "??")
//			private String description;
//			@Schema(description = "??")
//			private String thum;
//			@Schema(description = "??")
//			@JsonAlias("soundSectionId")
//			private String sound_section_id;
//			@Schema(description = "??")
//			@JsonAlias("uploadedBy")
//			private String uploaded_by;
//			@Schema(description = "??")
//			private String publish;
//			@Schema(description = "등록자")
//			@JsonAlias("regId")
//			private String reg_id;
//			@Schema(description = "등록일")
//			@JsonAlias("regDt")
//			private String reg_dt;
//			@Schema(description = "수정자")
//			@JsonAlias("modId")
//			private String mod_id;
//			@Schema(description = "수정일")
//			@JsonAlias("modDt")
//			private String mod_dt;
		}

		@Data
		@ApiModel
		public static class VideoComment {
			@JsonProperty("User")
			@JsonAlias("user")
			private UserInfo user;

			@Schema(description = "회원 아이디")
			private String id ;
			@Schema(description = "등록자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			private String video_id;
			@Schema(description = "댓글 내용")
			private String comment;
			@Schema(description = "생성일자")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "댓글 활성화")
			@JsonAlias("commentActive")
			private String comment_active;

			@Data
			@ApiModel
			public static class UserInfo {
				@Schema(description = "회원 아이디")
				private String id;
				@Schema(description = "디바이스 토큰값")
				@JsonAlias("deviceToken")
				private String device_token;
				@Schema(description = "auth 토큰")
				@JsonAlias("authToken")
				private String auth_token;
				@Schema(description = "이메일")
				private String email;
				@Schema(description = "비밀번호")
				private String password;
				@Schema(description = "핸드폰번호")
				private String phone;
				@Schema(description = "활동여부")
				private String active;
				@Schema(description = "cd_mst 탈퇴 사유")
				@JsonAlias("activeReason")
				private String active_reason;
				@Schema(description = "cd_mst 탈퇴 상세 이유")
				@JsonAlias("activeReasonDetail")
				private String active_reason_detail;
				@Schema(description = "사용자 이름")
				private String username;
				@Schema(description = "권한")
				private String role;
				@Schema(description = "포지션")
				private String point;
				@Schema(description = "학력")
				private String grade;
				@Schema(description = "성")
				private String first_name;
				@Schema(description = "이름")
				private String last_name;
				@Schema(description = "성별")
				private String gender;
				@Schema(description = "웹사이트")
				private String website;
				@Schema(description = "생년월일")
				private String dob;
				@Schema(description = "소셜 로그인 정보")
				@JsonAlias("socialId")
				private String social_id;
				@Schema(description = "프로필 1( ios )")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "프로필2 (안드로이드)")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
				@JsonAlias("passChnYn")
				private String pass_chn_yn;
				@JsonAlias("passModifyDatetime")
				private String pass_modify_datetime;
				@Schema(description = "소셜 로그인 정보")
				private String social;
				@Schema(description = "토큰값")
				private String token;
				@Schema(description = "생성날짜")
				@JsonAlias("regDt")
				private String reg_dt;
			}
		}

	}

	@Data
	@ApiModel
	public static class ResUIMPMI045Dto {
		@JsonProperty("Video")
		@JsonAlias("video")
		private VideoInfo video;

		@JsonProperty("VideoFavourite")
		@JsonAlias("videoFavourite")
		private VideoFavourite videoFavourite;

		@Data
		@ApiModel
		public static class VideoFavourite {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "비디오아이디")
			@JsonAlias("videoId")
			private String video_id;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class VideoInfo {
			@JsonProperty("User")
			@JsonAlias("user")
			private User user;

			@Schema(description = "아이디")
			private String id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "해시코드(영상설명)")
			private String description;
			@Schema(description = "비디오 url")
			private String video;
			@Schema(description = "url")
			private String thum;
			@Schema(description = "썸네일 url")
			private String gif;
			@Schema(description = "섹션 여부")
			private String section;
			@Schema(description = "활성화 여부")
			@JsonAlias("videoActive")
			private boolean video_active;
			@Schema(description = "조회수")
			private String view;
			@Schema(description = "사운드 아이디")
			@JsonAlias("soundId")
			private String sound_id;
			@Schema(description = "공개여부")
			@JsonAlias("privacyType")
			private String privacy_type;
			@Schema(description = "댓글 허용/비허용")
			@JsonAlias("allowComments")
			private String allow_comments;
			@Schema(description = "")
			private String block;
			@Schema(description = "영상길이")
			private String duration;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "해시대그비디오아이디")
			@JsonAlias("hvId")
			private String hv_id;

			@Schema(description = "좋아요")
			private String like;
			@Schema(description = "북마크 cnt")
			private String favourite;
            @Schema(description = "댓글 cnt")
			@JsonAlias("comment_count")
			private String comment_count;
            @Schema(description = "좋아요숫자")
			@JsonAlias("like_count")
            private String like_count;

			@Data
			@ApiModel
			public static class User {
				@JsonProperty("PushNotification")
				@JsonAlias("pushNotification")
				private PushNotification pushNotification;

				@JsonProperty("PrivacySetting")
				@JsonAlias("privacySetting")
				private PrivacySetting privacySetting;

				@Schema(description = "회원 아이디")
				private String id;
//				@Schema(description = "디바이스 토큰값")
//				@JsonAlias("deviceToken")
//				private String device_token;
//				@Schema(description = "auth 토큰")
//				@JsonAlias("authToken")
//				private String auth_token;
				@Schema(description = "이메일")
				private String email;
				@Schema(description = "비밀번호")
				private String password;
				@Schema(description = "핸드폰번호")
				private String phone;
				@Schema(description = "활동여부")
				private String active;
				@Schema(description = "cd_mst 탈퇴 사유")
				@JsonAlias("activeReason")
				private String active_reason;
				@Schema(description = "cd_mst 탈퇴 상세 이유")
				@JsonAlias("activeReasonDetail")
				private String active_reason_detail;
				@Schema(description = "사용자 이름")
				private String username;
				@Schema(description = "권한")
				private String role;
				@Schema(description = "포지션")
				private String point;
				@Schema(description = "학력")
				private String grade;
				@Schema(description = "성")
				private String first_name;
				@Schema(description = "이름")
				private String last_name;
				@Schema(description = "성별")
				private String gender;
				@Schema(description = "웹사이트")
				private String website;
				@Schema(description = "생년월일")
				private String dob;
				@Schema(description = "프로필 1( ios )")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "프로필2 (안드로이드)")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
				@Schema(description = "토큰값")
				private String token;
				@Schema(description = "생성날짜")
				@JsonAlias("regDt")
				private String reg_dt;
				@Schema(description = "팔로우 버튼")
				private String button;

				@Data
				@ApiModel
				public static class PrivacySetting {

				}

				@Data
				@ApiModel
				public static class PushNotification {
					@Schema(description = "사용자 id와 매핑")
					private String id;
					@Schema(description = "좋아요 알람 이거로 검사")
					private String likes;
					@Schema(description = "댓글알람")
					private String comments;
					@Schema(description = "팔로우 알람")
					@JsonAlias("newFollowers")
					private String new_followers;
					@Schema(description = "멘션 알람")
					private String mentions;
					@Schema(description = "다이렉트 메세지")
					@JsonAlias("directMessages")
					private String direct_messages;
					@Schema(description = "비디오 알람")
					@JsonAlias("videoUpdates")
					private String video_updates;
					@Schema(description = "포스트 알람")
					@JsonAlias("postUpdate")
					private String post_update;
					@Schema(description = "광고 알람")
					private String event;
				}
			}
		}


//		@Schema(description = "비디오 키")
//		private String id;
//
//		@Schema(description = "비디오 작성자 ID??")
//		@JsonAlias("userId")
//		private String user_id;
//
//		@Schema(description = "utf8mb4_unicode_ci(영상내용)")
//		private String description;
//
//		@Schema(description = "비디오 경로")
//		private String video;
//
//		@Schema(description = "썸네일?")
//		private String thum;
//
//		@Schema(description = "썸네일 경로")
//		private String gif;
//
//		@Schema(description = "활성화여부 디폴트 1")
//		private String video_active;
//
//		@Schema(description = "시청건수")
//		private String view;
//
//		@Schema(description = "?? 디폴트 0")
//		private String section;
//
//		@Schema(description = "사운드 아이디? 디폴트 0")
//		@JsonAlias("soundId")
//		private String sound_id;
//
//		@Schema(description = "public or private / defalut public")
//		@JsonAlias("privacyType")
//		private String privacy_type;
//
//		@Schema(description = "true=allow  and false=not allowed 디폴트 true")
//		@JsonAlias("allowComments")
//		private String allow_comments;
//
//		@Schema(description = "0 or 1  1=active 디폴트 0")
//		@JsonAlias("allowDuet")
//		private String allow_duet;
//
//		@Schema(description = "0= video is active 1= video is not active and hide from public")
//		private String block;
//
//		@Schema(description = "듀엣 하는 비디오 아이디?")
//		@JsonAlias("duetVideoId")
//		private String duet_video_id;
//
//		@Schema(description = "??")
//		@JsonAlias("oldVideoId")
//		private String old_video_id;
//
//		@Schema(description = "영상시간")
//		private String duration;
//
//		@Schema(description = "홍보?")
//		private String promote;
//
//		@Schema(description = "등록자")
//		@JsonAlias("regId")
//		private String reg_id;
//		@Schema(description = "등록일")
//		@JsonAlias("regDt")
//		private String reg_dt;
//		@Schema(description = "수정자")
//		@JsonAlias("modId")
//		private String mod_id;
//		@Schema(description = "수정일")
//		@JsonAlias("modDt")
//		private String mod_dt;

	}


	@Data
	@ApiModel
	public static class ResUIMPMI046Dto {
		@Schema(description = "비디오 키")
		private String id;
		@Schema(description = "비디오 작성자 ID??")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "utf8mb4_unicode_ci(영상내용)")
		private String description;
		@Schema(description = "비디오 경로")
		private String video;
		@Schema(description = "썸네일?")
		private String thum;
		@Schema(description = "썸네일 경로")
		private String gif;
		@Schema(description = "활성화여부 디폴트 1")
		private String video_active;
		@Schema(description = "시청건수")
		private String view;
		@Schema(description = "?? 디폴트 0")
		private String section;
		@Schema(description = "사운드 아이디? 디폴트 0")
		@JsonAlias("soundId")
		private String sound_id;
		@Schema(description = "public or private / defalut public")
		@JsonAlias("privacyType")
		private String privacy_type;
		@Schema(description = "true=allow  and false=not allowed 디폴트 true")
		@JsonAlias("allowComments")
		private String allow_comments;
		@Schema(description = "0 or 1  1=active 디폴트 0")
		@JsonAlias("allowDuet")
		private String allow_duet;
		@Schema(description = "0= video is active 1= video is not active and hide from public")
		private String block;
		@Schema(description = "듀엣 하는 비디오 아이디?")
		@JsonAlias("duetVideoId")
		private String duet_video_id;
		@Schema(description = "??")
		@JsonAlias("oldVideoId")
		private String old_video_id;
		@Schema(description = "영상시간")
		private String duration;
		@Schema(description = "홍보?")
		private String promote;
		@Schema(description = "등록자")
		@JsonAlias("regId")
		private String reg_id;
		@Schema(description = "등록일")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "수정자")
		@JsonAlias("modId")
		private String mod_id;
		@Schema(description = "수정일")
		@JsonAlias("modDt")
		private String mod_dt;

	}


	@Data
	@ApiModel
	public static class ResBOMPMI014Dto {
		@Schema(description = "마이페이지 사용자 정보")
		@JsonProperty("Profile")
		@JsonAlias("profile")
		private Profile profile;
		@Schema(description = "팔로잉갯수")
		@JsonProperty("FollowingCount")
		@JsonAlias("followingCount")
		private int followingCount;
		@Schema(description = "팔로워갯수")
		@JsonProperty("FollowerCount")
		@JsonAlias("followerCount")
		private int followerCount;
		@JsonProperty("FavoruiteCount")
		@JsonAlias("favoruiteCount")
		private int favoruiteCount;
		@Schema(description = "내가 등록한 게시글 갯수 (post,showpepper)")
		@JsonProperty("MyBoardCount")
		@JsonAlias("myBoardCount")
		private int myBoardCount;
		@Schema(description = "차단여부")
		private String isBlock;
		@Schema(description = "버튼노출여부")
		private String button;

		@Data
		public static class Profile {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "닉네임")
			private String username;
			@Schema(description = "유저 권한 ( user, player )")
			private String role;
			@Schema(description = "성(영어 이름)")
			@JsonAlias("firstName")
			private String first_name;
			@Schema(description = "이름(영어 이름)")
			@JsonAlias("lastName")
			private String last_name;
			@Schema(description = "성별")
			private String gender;
			@Schema(description = "생년월일")
			private String dob;
			@Schema(description = "이메일 주소")
			private String email;
			@Schema(description = "핸드폰 번호")
			private String phone;
			@Schema(description = "프로필1 (IOS)")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필2 (안드로이드)")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
			@Schema(description = "생성 일자")
			private String regDt;
		}
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI002Dto {
		@Schema(description = "조회 할 Id", example="peYLRghBvfPyA13A9ET46w==")
		@JsonAlias("targetId")
		private String target_id;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI003Dto {
		@Schema(description = "조회 할 Id (없으면 본인 팔로워 리스트 조회) ", example="")
		private String other_user_id;
	}

	@Data
	@ApiModel
	public static class ResBOMPMI003Dto {
		@Schema(description = "팔로우 키")
		@JsonAlias("followId")
		private String followId;
		@Schema(description = "유저 키")
		private String id;
		@Schema(description = "유저 권한 ( user, player )")
		private String role;
		@Schema(description = "성(영어 이름)")
		@JsonAlias("firstName")
		private String first_name;
		@Schema(description = "이름(영어 이름)")
		@JsonAlias("lastName")
		private String last_name;
		@Schema(description = "이메일 주소")
		private String email;
		@Schema(description = "닉네임")
		private String username;
		@Schema(description = "프로필1 (IOS)")
		@JsonAlias("profilePic")
		private String profile_pic;
		@Schema(description = "프로필2 (안드로이드)")
		@JsonAlias("profilePicSmall")
		private String profile_pic_small;
		@Schema(description = "팔로우 여부??")
		private String isFollowing;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI004Dto {
		@Schema(description = "조회 할 Id (본인 : ''(공백) , 다른사람 : srchId)", example="")
		private String other_user_id;
	}

	@Data
	@ApiModel
	public static class ResBOMPMI004Dto {
		@Schema(description = "팔로우 키")
		@JsonAlias("followId")
		private String followId;
		@Schema(description = "유저 키")
		private String id;
		@Schema(description = "유저 권한 ( user, player )")
		private String role;
		@Schema(description = "성(영어 이름)")
		@JsonAlias("firstName")
		private String first_name;
		@Schema(description = "이름(영어 이름)")
		@JsonAlias("lastName")
		private String last_name;
		@Schema(description = "이메일 주소")
		private String email;
		@Schema(description = "닉네임")
		private String username;
		@Schema(description = "프로필1 (IOS)")
		@JsonAlias("profilePic")
		private String profile_pic;
		@Schema(description = "프로필2 (안드로이드)")
		@JsonAlias("profilePicSmall")
		private String profile_pic_small;
		@Schema(description = "팔로우 여부??")
		private String isFollowing;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI005Dto {

	}

	@Data
	@ApiModel
	public static class ResBOMPMI005Dto {
		@Schema(description = "유저 키")
		private String id;
		@Schema(description = "닉네임")
		private String username;
		@Schema(description = "유저 권한 ( user, player )")
		private String role;
		@Schema(description = "성(영어 이름)")
		@JsonAlias("firstName")
		private String first_name;
		@Schema(description = "이름(영어 이름)")
		@JsonAlias("lastName")
		private String last_name;
		@Schema(description = "이메일 주소")
		private String email;
		@Schema(description = "프로필1 (IOS)")
		@JsonAlias("profilePic")
		private String profile_pic;
		@Schema(description = "프로필2 (안드로이드)")
		@JsonAlias("profilePicSmall")
		private String profile_pic_small;
		@Schema(description = "휴대폰")
		private String phone;
		@Schema(description = "???")
		private String dob;
		@Schema(description = "생성일자?")
		private String regDt;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI006Dto {

	}

	@Data
	@ApiModel
	public static class ResBOMPMI006Dto {
		@Schema(description = "유저 키")
		private String id;
		@Schema(description = "닉네임")
		private String username;
		@Schema(description = "유저 권한 ( user, player )")
		private String role;
		@Schema(description = "성(영어 이름)")
		@JsonAlias("firstName")
		private String first_name;
		@Schema(description = "이름(영어 이름)")
		@JsonAlias("lastName")
		private String last_name;
		@Schema(description = "이메일 주소")
		private String email;
		@Schema(description = "프로필1 (IOS)")
		@JsonAlias("profilePic")
		private String profile_pic;
		@Schema(description = "프로필2 (안드로이드)")
		@JsonAlias("profilePicSmall")
		private String profile_pic_small;
		@Schema(description = "휴대폰")
		private String phone;
		@Schema(description = "???")
		private String dob;
		@Schema(description = "생성일자?")
		private String regDt;
		@Schema(description = "팔로우 여부")
		private Boolean isFollow;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI007Dto {
		@Schema(description = "삭제 할 id", example="20")
		private String block_id;
	}

	@Data
	@ApiModel
	public static class ResBOMPMI007Dto {

	}

	@Data
	@ApiModel
	public static class ReqBOMPMI008Dto {

	}

	@Data
	@ApiModel
	public static class ResBOMPMI008Dto {

	}

	@Data
	@ApiModel
	public static class ReqBOMPMI009Dto {
		@Schema(description = "삭제 할 id", example="20")
		private String block_id;
	}

	@Data
	@ApiModel
	public static class ResBOMPMI009Dto {
		@Schema(description = "결과 메세지")
		private String resultMsg;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI010Dto {

	}

	@Data
	@ApiModel
	public static class ResBOMPMI010Dto {
		@JsonProperty("BlockUser")
		@JsonAlias("blockUser")
		private BlockUser blockUser;
		@JsonProperty("TagetUser")
		@JsonAlias("tagetUser")
		private TagetUser tagetUser;

		@Data
		@ApiModel
		public static class BlockUser {
			@Schema(description = "블럭 id")
			private String id;
			@Schema(description = "사용자 id")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "차단 유저 id")
			@JsonAlias("blockUserId")
			private String block_user_id;
			@Schema(description = "생성일자")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class TagetUser {
			@Schema(description = "차단 유저 id")
			private String id;
			@Schema(description = "차단 사용자닉네임")
			@JsonAlias("username")
			private String username;
			@Schema(description = "차단 사용자닉네임")
			@JsonAlias("username")
			private String role;
			@Schema(description = "프로필1 (IOS)")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필2 (안드로이드)")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
		}
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI011Dto {
		@Schema(description = "닉네임", example="대머리")
		private String username;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI012Dto {
		@Schema(description = "이전 비밀번호", example="/EIIlzx6NrnKJE9RKrEnxg==")
		private String old_password;
		@Schema(description = "새 비밀번호", example="HuyJ3Fq6Dxz65AVvd2b2UQ==")
		private String new_password;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI016Dto {
		@Schema(description = "파일 명", example="테스트")
		private String name;
		@JsonProperty("file")
		@Valid
		private MultipartFile file;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI017Dto {
		@Schema(description = "비밀번호", example="/EIIlzx6NrnKJE9RKrEnxg==")
		private String password;
		@Schema(description = "탈퇴사유", example="RES01,RES02,RES05 (,넣고 넘겨주면 됨 )")
		private String active_reason;
		@Schema(description = "탈퇴사유 상세", example="상세내용")
		private String active_reason_detail;
	}

	@Data
	@ApiModel
	public static class ReqBOMPMI033Dto {
		@Schema(description = "IOS 프로필", example="/9j/4AAQSkZJRgABAQEBLAEsAAD/4QBSRXhpZgAASUkqAAgAAAABAA4BAgAwAAAAGgAAAAAAAABzYW1wbGUgc2lnbi4gc2FtcGxlIHNxdWFyZSBzcGVlY2ggYnViYmxlLiBzYW1wbGX/4QVNaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIj4KCTxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CgkJPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczpJcHRjNHhtcENvcmU9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBDb3JlLzEuMC94bWxucy8iICAgeG1sbnM6R2V0dHlJbWFnZXNHSUZUPSJodHRwOi8veG1wLmdldHR5aW1hZ2VzLmNvbS9naWZ0LzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGx1cz0iaHR0cDovL25zLnVzZXBsdXMub3JnL2xkZi94bXAvMS4wLyIgIHhtbG5zOmlwdGNFeHQ9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBFeHQvMjAwOC0wMi0yOS8iIHhtbG5zOnhtcFJpZ2h0cz0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3JpZ2h0cy8iIHBob3Rvc2hvcDpDcmVkaXQ9IkdldHR5IEltYWdlcy9pU3RvY2twaG90byIgR2V0dHlJbWFnZXNHSUZUOkFzc2V0SUQ9IjExNjEzNTI0ODAiIHhtcFJpZ2h0czpXZWJTdGF0ZW1lbnQ9Imh0dHBzOi8vd3d3LmlzdG9ja3Bob3RvLmNvbS9sZWdhbC9saWNlbnNlLWFncmVlbWVudD91dG1fbWVkaXVtPW9yZ2FuaWMmYW1wO3V0bV9z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")
		private String profile_pic;
		@Schema(description = "안드로이드 프로필", example="/9j/4AAQSkZJRgABAQEBLAEsAAD/4QBSRXhpZgAASUkqAAgAAAABAA4BAgAwAAAAGgAAAAAAAABzYW1wbGUgc2lnbi4gc2FtcGxlIHNxdWFyZSBzcGVlY2ggYnViYmxlLiBzYW1wbGX/4QVNaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIj4KCTxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CgkJPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczpJcHRjNHhtcENvcmU9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBDb3JlLzEuMC94bWxucy8iICAgeG1sbnM6R2V0dHlJbWFnZXNHSUZUPSJodHRwOi8veG1wLmdldHR5aW1hZ2VzLmNvbS9naWZ0LzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGx1cz0iaHR0cDovL25zLnVzZXBsdXMub3JnL2xkZi94bXAvMS4wLyIgIHhtbG5zOmlwdGNFeHQ9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBFeHQvMjAwOC0wMi0yOS8iIHhtbG5zOnhtcFJpZ2h0cz0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3JpZ2h0cy8iIHBob3Rvc2hvcDpDcmVkaXQ9IkdldHR5IEltYWdlcy9pU3RvY2twaG90byIgR2V0dHlJbWFnZXNHSUZUOkFzc2V0SUQ9IjExNjEzNTI0ODAiIHhtcFJpZ2h0czpXZWJTdGF0ZW1lbnQ9Imh0dHBzOi8vd3d3LmlzdG9ja3Bob3RvLmNvbS9sZWdhbC9saWNlbnNlLWFncmVlbWVudD91dG1fbWVkaXVtPW9yZ2FuaWMmYW1wO3V0bV9z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")
		private String profile_pic_small;
	}

	@Data
	@ApiModel
	public static class ResBOMPMI033Dto {
		@Schema(description = "IOS 프로필")
		private String profile_pic;
		@Schema(description = "안드로이드 프로필")
		private String profile_pic_small;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI049Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "등록자 이름")
		private String username;
		@Schema(description = "유저 권한 ( user, player )")
		private String role;
		@Schema(description = "프로필1 (IOS)")
		@JsonAlias("profilePic")
		private String profile_pic;
		@Schema(description = "프로필2 (안드로이드)")
		@JsonAlias("profilePicSmall")
		private String profile_pic_small;
		@Schema(description = "회원 등급코드")
		private String grade;
		@Schema(description = "회원 등급 명")
		@JsonAlias("gradeNm")
		private String grade_nm;
		@Schema(description = "포인트")
		private String point;
	}


	@Data
	@ApiModel
	public static class ResUIMPMI050Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "사용자 아이디")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "이벤트 REL 아이디")
		@JsonAlias("eventRelId")
		private String event_rel_id;
		@Schema(description = "이벤트 타입")
		@JsonAlias("eventType")
		private String event_type;
		@Schema(description = "이벤트 설명")
		@JsonAlias("eventDesc")
		private String event_desc;
		@Schema(description = "이벤트 타입 명")
		@JsonAlias("eventTypeNm")
		private String event_type_nm;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI051Dto {

		@JsonProperty("Post")
		@JsonAlias("post")
		private Post post;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("PostImage")
		@JsonAlias("postImage")
		private List<PostImage> postImage;

		@Data
		@ApiModel
		public static class Post {
			@Schema(description = "포스트 키")
			private String id;
			@Schema(description = "포스트 타입")
			private String type;
			@Schema(description = "소유자 유저 ID")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "플레이어/유저 마이페이지 들어갈 때 필요한 User ID")
			@JsonAlias("targetId")
			private String target_id;
			@Schema(description = "제목")
			private String title;
			@Schema(description = "내용")
			private String contents;
			@Schema(description = "등록일")
			@JsonAlias("created")
			private String created;
			@Schema(description = "삭제 시 : 0")
			@JsonAlias("postActive")
			private String post_active;
			@Schema(description = "비활성화 한 시간, active, block에서 사용")
			@JsonAlias("postActiveDate")
			private String post_active_date;
			@Schema(description = "신고로 인한 차단시 : 1")
			private String block;
			@Schema(description = "댓글 갯수")
			@JsonAlias("commentCnt")
			private String comment_cnt;
			@Schema(description = "작성시간")
			@JsonAlias("diffDate")
			private String diff_date;
			@Schema(description = "좋아요 갯수")
			@JsonAlias("likeCnt")
			private String like_cnt;
			@Schema(description = "좋아요 여부")
			@JsonAlias("likeYn")
			private String like_yn;
		}

		@Data
		@ApiModel
		public static class User {
			@Schema(description = "등록자 이름")
			private String username;
			@Schema(description = "유저 권한 ( user, player )")
			private String role;
			@Schema(description = "프로필1 (IOS)")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필2 (안드로이드)")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
		}

		@Data
		@ApiModel
		public static class PostImage {
			@Schema(description = "포스트이미지 ID")
			private String id;
			@Schema(description = "포스트 id")
			@JsonAlias("postId")
			private String post_id;
			@Schema(description = "이미지 url")
			@JsonAlias("imageUrl")
			private String image_url;
			@Schema(description = "이미지 사이즈")
			@JsonAlias("imageSize")
			private String image_size;
			@Schema(description = "가로")
			private String width;
			@Schema(description = "높이")
			private String height;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String created;
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI030Dto {
		@Schema(description = "좋아요 알림 여부")
		private String notice;
		@Schema(description = "이벤트 알림 여부")
		private String event;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI053Dto {
		@JsonProperty("PostComment")
		@JsonAlias("postComment")
		private PostComment postComment;

		@Data
		@ApiModel
		public static class PostComment {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "등록자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "등록 포스트 아이디")
			@JsonAlias("postId")
			private int post_id;
			@Schema(description = "내용")
			private String comment;
			@Schema(description = "등록 일시")
			private String reg_dt;
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI053_ReDto {
		@JsonProperty("PostCommentReply")
		@JsonAlias("postCommentReply")
		private PostCommentReply postCommentReply;

		@Data
		@ApiModel
		public static class PostCommentReply {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "등록자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "등록 포스트 아이디")
			@JsonAlias("postId")
			private int post_id;
			@Schema(description = "댓글 아이디")
			@JsonAlias("commentId")
			private String comment_id;
			@Schema(description = "대댓글 아이디")
			@JsonAlias("replyCommentId")
			private String reply_comment_id;
			@Schema(description = "내용")
			private String comment;
			@Schema(description = "등록 일시")
			private String reg_dt;
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI054Dto {

		@JsonProperty("Post")
		@JsonAlias("post")
		private Post post;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("PostImage")
		@JsonAlias("postImage")
		private List<PostImage> postImage;
		@JsonProperty("Comment")
		@JsonAlias("comment")
		private List<Comment> comment;

		@Data
		@ApiModel
		public static class Post {
			@Schema(description = "포스트 키")
			private String id;
			@Schema(description = "포스트 타입")
			private String type;
			@Schema(description = "소유자 유저 ID")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "플레이어/유저 마이페이지 들어갈 때 필요한 User ID")
			@JsonAlias("targetId")
			private String target_id;
			@Schema(description = "제목")
			private String title;
			@Schema(description = "내용")
			private String contents;
			@Schema(description = "등록일")
			@JsonAlias("created")
			private String created;
			@Schema(description = "삭제 시 : 0")
			@JsonAlias("postActive")
			private String post_active;
			@Schema(description = "비활성화 한 시간, active, block에서 사용")
			@JsonAlias("postActiveDate")
			private String post_active_date;
			@Schema(description = "신고로 인한 차단시 : 1")
			private String block;
			@Schema(description = "댓글 갯수")
			@JsonAlias("commentCnt")
			private String comment_cnt;
			@Schema(description = "작성시간")
			@JsonAlias("diffDate")
			private String diff_date;
			@Schema(description = "좋아요 갯수")
			@JsonAlias("likeCnt")
			private String like_cnt;
			@Schema(description = "좋아요 여부")
			@JsonAlias("likeYn")
			private String like_yn;
		}

		@Data
		@ApiModel
		public static class User {
			@Schema(description = "등록자 이름")
			private String username;
			@Schema(description = "유저 권한 ( user, player )")
			private String role;
			@Schema(description = "프로필1 (IOS)")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필2 (안드로이드)")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
		}

		@Data
		@ApiModel
		public static class PostImage {
			@Schema(description = "포스트이미지 ID")
			private String id;
			@Schema(description = "포스트 id")
			@JsonAlias("postId")
			private String post_id;
			@Schema(description = "이미지 url")
			@JsonAlias("imageUrl")
			private String image_url;
			@Schema(description = "이미지 사이즈")
			@JsonAlias("imageSize")
			private String image_size;
			@Schema(description = "가로")
			private String width;
			@Schema(description = "높이")
			private String height;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String created;
		}

		@Data
		@ApiModel
		public static class Comment {
			@Schema(description = "코멘트 ID")
			private String id;
			@Schema(description = "소유자 유저 ID")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "포스트 id")
			@JsonAlias("postId")
			private String post_id;
			@Schema(description = "코멘트")
			@JsonAlias("comment")
			private String comment;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String regDt;
			@Schema(description = "코멘트 삭제여부")
			@JsonAlias("commentActive")
			private String comment_active;
			@JsonProperty("User")
			@JsonAlias("user")
			private User user;
			@Schema(description = "작성시간")
			@JsonAlias("diffDate")
			private String diff_date;
			@Schema(description = "좋아요 여부")
			@JsonAlias("isLike")
			private String isLike;
			@Schema(description = "좋아요 갯수")
			@JsonAlias("likeCnt")
			private String like_count;
			@JsonProperty("PostCommentReply")
			@JsonAlias("postCommentReply")
			private List<PostCommentReply> PostCommentReply;
		}

		@Data
		@ApiModel
		public static class PostCommentReply {
			@Schema(description = "대댓글 ID")
			private String id;
			@Schema(description = "소유자 유저 ID")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "포스트 id")
			@JsonAlias("postId")
			private String post_id;
			@Schema(description = "코멘트")
			@JsonAlias("commentId")
			private String comment_id;
			@Schema(description = "코멘트")
			@JsonAlias("comment")
			private String comment;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String regDt;
			@Schema(description = "코멘트 삭제여부")
			@JsonAlias("commentActive")
			private String comment_active;
			@Schema(description = "코멘트 삭제 일시")
			@JsonAlias("commentActiveDate")
			private String comment_active_date;
			@JsonProperty("User")
			@JsonAlias("user")
			private User user;
			@JsonProperty("reply_reply")
			@JsonAlias("replyReply")
			private Reply_reply reply_reply;
			@Schema(description = "작성시간")
			@JsonAlias("diffDate")
			private String diff_date;
			@Schema(description = "좋아요 여부")
			@JsonAlias("isLike")
			private String isLike;
			@Schema(description = "좋아요 갯수")
			@JsonAlias("likeCnt")
			private String like_count;
		}

		@Data
		@ApiModel
		public static class Reply_reply {
			@Schema(description = "대대댓글 ID")
			private String id;
			@Schema(description = "유저 역할")
			@JsonAlias("role")
			private String role;
			@Schema(description = "유저이름")
			@JsonAlias("userName")
			private String userName;
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI055Dto {
		@Schema(description = "좋아요 여부")
		private String is_like;
		@Schema(description = "좋아요 수")
		private int like_cnt;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI056Dto {
		@Schema(description = "좋아요 여부")
		private String is_like;
		@Schema(description = "좋아요 수")
		private int like_cnt;
	}
	@Data
	@ApiModel
	public static class ResUIMPMI057Dto {
		@Schema(description = "좋아요 여부")
		private String is_like;
		@Schema(description = "좋아요 수")
		private int like_cnt;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI058Dto {
		@Schema(description = "코드")
		private String code;
	}

	@Data
	@ApiModel
	public static class ResUIMPMI059Dto {
		@JsonProperty("PostComment")
		@JsonAlias("postComment")
		private PostComment postComment;

		@Data
		@ApiModel
		public static class PostComment {
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("Reply")
		@JsonAlias("reply")
		private List<Reply> reply;


		@Schema(description = "아이디")
        private String id;
		@Schema(description = "등록자 아이디")
		@JsonAlias("userId")
        private String user_id;
		@Schema(description = "포스트 아이디")
		@JsonAlias("postId")
        private String post_id;
		@Schema(description = "댓글 내용")
        private String comment;
		@Schema(description = "등록 일자")
		@JsonAlias("regDt")
        private String reg_dt;
		@Schema(description = "댓글 활성 여부")
		@JsonAlias("commentActive")
        private String comment_active;
		@Schema(description = "좋아요 여부")
        private String isLike;
		@Schema(description = "")
		@JsonAlias("likeCount")
        private String like_count;
		@Schema(description = "등록 시간")
		@JsonAlias("diffDate")
        private String diff_date;

			@Data
			@ApiModel
			public static class User {
				@Schema(description = "사용자 아이디")
				private String id;
				@Schema(description = "닉네임")
				private String username;
				@Schema(description = "사용자 권한")
				private String role;
				@Schema(description = "프로필 이미지")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "썸네일 이미지")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
			}

			@Data
			@ApiModel
			public static class Reply {
				@JsonProperty("User")
				@JsonAlias("user")
				private UserInfo user;

				@Schema(description = "아이디")
		        private String id;
				@Schema(description = "등록자 아이디")
				@JsonAlias("userId")
		        private String user_id;
				@Schema(description = "대댓글 아이디")
				@JsonAlias("commentId")
		        private String comment_id;
				@Schema(description = "대대댓글 아이디")
				@JsonAlias("replyCommentId")
				private String reply_comment_id;
				@Schema(description = "댓글 내용")
		        private String comment;
				@Schema(description = "등록 일자")
				@JsonAlias("regDt")
		        private String reg_dt;
				@Schema(description = "댓글 활성 여부")
				@JsonAlias("commentActive")
		        private String comment_active;
				@Schema(description = "좋아요 여부")
		        private String isLike;
				@Schema(description = "")
				@JsonAlias("likeCount")
		        private String like_count;
				@Schema(description = "등록 시간")
				@JsonAlias("diffDate")
		        private String diff_date;

					@Data
					@ApiModel
					public static class UserInfo {
						@Schema(description = "사용자 아이디")
						private String id;
						@Schema(description = "닉네임")
						private String username;
						@Schema(description = "사용자 권한")
						private String role;
						@Schema(description = "프로필 이미지")
						@JsonAlias("profilePic")
						private String profile_pic;
						@Schema(description = "썸네일 이미지")
						@JsonAlias("profilePicSmall")
						private String profile_pic_small;
					}

//					@Data
//					@ApiModel
//					public static class ReplyInfo {
//						@JsonProperty("User")
//						@JsonAlias("user")
//						private UserReInfo user;
//
//						@Schema(description = "아이디")
//				        private String id;
//						@Schema(description = "등록자 아이디")
//						@JsonAlias("userId")
//				        private String user_id;
//						@Schema(description = "포스트 아이디")
//						@JsonAlias("commentId")
//				        private String comment_id;
//						@Schema(description = "댓글 내용")
//				        private String comment;
//						@Schema(description = "등록 일자")
//						@JsonAlias("regDt")
//				        private String reg_dt;
//						@Schema(description = "댓글 활성 여부")
//						@JsonAlias("commentActive")
//				        private String comment_active;
//						@Schema(description = "좋아요 여부")
//				        private String isLike;
//						@Schema(description = "")
//						@JsonAlias("likeCount")
//				        private String like_count;
//						@Schema(description = "등록 시간")
//						@JsonAlias("diffDate")
//				        private String diff_date;
//
//							@Data
//							@ApiModel
//							public static class UserReInfo {
//								@Schema(description = "사용자 아이디")
//								private String id;
//								@Schema(description = "닉네임")
//								private String username;
//								@Schema(description = "사용자 권한")
//								private String role;
//								@Schema(description = "프로필 이미지")
//								@JsonAlias("profilePic")
//								private String profile_pic;
//								@Schema(description = "썸네일 이미지")
//								@JsonAlias("profilePicSmall")
//								private String profile_pic_small;
//							}
//					}
			}
		}
	}

	@Data
	@ApiModel
	public static class ResUIMPMI060Dto {
		@Schema(description = "아이디")
		private String id ;
		@Schema(description = "댓글 삭제여부")
		@JsonAlias("commentActive")
		private int comment_active;
	}
}