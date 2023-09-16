package kr.aipeppers.pep.ui.showpepper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

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
import kr.aipeppers.pep.ui.showpepper.component.ShowPepperComponent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@RestController
@RequestMapping("/restapi/showpepper/info")
@Api(tags = { "쇼페퍼>정보" }, description = "ShowPepperController")
public class ShowPepperController {

	@Autowired
	protected ShowPepperComponent showPepperComponent;

	/**
	 * @Method Name : showRelatedVideos
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "쇼페퍼 리스트 조회")
	@PostMapping(value = "showRelatedVideos") //
	public ResListDto<ResUISPIF001Dto>  showRelatedVideos(@RequestBody ReqUISPIF001Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResUISPIF001Dto>(BeanUtil.convertList(showPepperComponent.showPeperVideoList(box), ResUISPIF001Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : watchVideo
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value = "쇼페퍼 view Cnt update&videoWatchInfo")
	@PostMapping(value = "watchVideo") //
	public ResResultDto<ResUISPIF002Dto>  watchVideo(@RequestBody ReqUISPIF002Dto reqDto) {
		return new ResResultDto<ResUISPIF002Dto>(BeanUtil.convert(showPepperComponent.showPepperVideoSave(BoxUtil.toBox(reqDto)), ResUISPIF002Dto.class));
	}

	/**
	 * @Method Name : showSounds
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "쇼페퍼 sound 정보")
	@PostMapping(value = "showSounds") //
	public ResListDto<ResUISPIF011Dto> showSounds(@RequestBody ReqUISPIF011Dto reqDto) throws Exception {
		return new ResListDto<ResUISPIF011Dto>(BeanUtil.convertList(showPepperComponent.showSoundsList(BoxUtil.toBox(reqDto)), ResUISPIF011Dto.class));
	}

	@ApiOperation(value = "쇼페퍼 댓글 등록")
	@PostMapping(value = "postCommentOnVideo")
	@ReqInfo(validForm = "showpepper.info.validation")
	public ResResultDto<ResUISPIF009Dto> postCommentOnVideo(@RequestBody ReqUISPIF009Dto reqDto) {
		return new ResResultDto<ResUISPIF009Dto>(BeanUtil.convert(showPepperComponent.videoCommentInsert(BoxUtil.toBox(reqDto)), ResUISPIF009Dto.class));
	}

	@ApiOperation(value = "쇼페퍼 대댓글,대대댓글 등록")
	@PostMapping(value = "postCommentReply")
	@ReqInfo(validForm = "showpepper.info.validation_2")
	public ResResultDto<ResUISPIF009_1Dto> postCommentReply(@RequestBody ReqUISPIF009_1Dto reqDto) {
		return new ResResultDto<ResUISPIF009_1Dto>(BeanUtil.convert(showPepperComponent.videoCommentInsert(BoxUtil.toBox(reqDto)), ResUISPIF009_1Dto.class));
	}

	/**
	 * @Method Name : deleteComment
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value = "쇼페퍼 댓글 삭제")
	@PostMapping(value = "deleteComment")
	public ResResultDto<ResUISPIF007Dto> deleteComment(@RequestBody ReqUISPIF007Dto reqDto) {
		return new  ResResultDto<ResUISPIF007Dto>(BeanUtil.convert(showPepperComponent.videoCommentDel(BoxUtil.toBox(reqDto)), ResUISPIF007Dto.class), "I213");
	}

	@ApiOperation(value = "쇼페퍼 대댓글,대대댓글 삭제")
	@PostMapping(value = "deleteCommentReply")
	public ResResultDto<ResUISPIF007Dto> deleteCommentReply(@RequestBody ReqUISPIF007_ReplyDto reqDto) {
		return new  ResResultDto<ResUISPIF007Dto>(BeanUtil.convert(showPepperComponent.videoCommentDel(BoxUtil.toBox(reqDto)), ResUISPIF007Dto.class), "I213");
	}

	/**
	 * @Method Name : showVideoComments
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value = "비디오 댓글 리스트")
	@PostMapping(value = "showVideoComments")
	public ResListDto<ResUISPIF008Dto> showVideoComments(@RequestBody ReqUISPIF008Dto reqDto) {
		return new  ResListDto<ResUISPIF008Dto>(BeanUtil.convertList(showPepperComponent.showVideoCommentsList(BoxUtil.toBox(reqDto)), ResUISPIF008Dto.class));
	}


	/**
	 * @Method Name : likeComment
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value = "비디오 댓글 좋아요")
	@PostMapping(value = "likeComment")
	public ResResultDto<ResUISPIF005Dto> likeComment(@RequestBody ReqUISPIF006Dto reqDto) {
		//return new  ResIntDto(showPepperComponent.likeCommenSave(BoxUtil.toBox(reqDto)));
		ResResultDto<ResUISPIF005Dto> resBox = new ResResultDto<ResUISPIF005Dto>();
		Box result = showPepperComponent.likeCommentReplySave(BoxUtil.toBox(reqDto));
		if (result.getInt("listCnt") > 0) {
			resBox = new ResResultDto<ResUISPIF005Dto>(BeanUtil.convert(result, ResUISPIF005Dto.class),"I231");
		} else {
			resBox = new ResResultDto<ResUISPIF005Dto>(BeanUtil.convert(result, ResUISPIF005Dto.class),"I230");
		}
		return resBox;
	}


	/**
	 * @Method Name : likeCommentReply
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value = "비디오 대댓글,대대댓글 좋아요")
	@PostMapping(value = "likeCommentReply")
	public ResResultDto<ResUISPIF005Dto> likeCommentReply(@RequestBody ReqUISPIF005Dto reqDto) {
		//return new  ResResultDto<ResUISPIF005Dto>(BeanUtil.convert(showPepperComponent.likeCommentReplySave(BoxUtil.toBox(reqDto)), ResUISPIF005Dto.class));

		ResResultDto<ResUISPIF005Dto> resBox = new ResResultDto<ResUISPIF005Dto>();
		Box result = showPepperComponent.likeCommentReplySave(BoxUtil.toBox(reqDto));
		if (result.getInt("listCnt") > 0) {
			resBox = new ResResultDto<ResUISPIF005Dto>(BeanUtil.convert(result, ResUISPIF005Dto.class),"I231");
		} else {
			resBox = new ResResultDto<ResUISPIF005Dto>(BeanUtil.convert(result, ResUISPIF005Dto.class),"I230");
		}
		return resBox;
	}


	/**
	 * @Method Name : likeCommentReply
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value = "비디오 좋아요")
	@PostMapping(value = "likeVideo")
	public ResResultDto<ResUISPIF004Dto> likeVideo(@RequestBody ReqUISPIF004Dto reqDto) {
		ResResultDto<ResUISPIF004Dto> resBox = new ResResultDto<ResUISPIF004Dto>();
		Box result = showPepperComponent.likeVideoSave(BoxUtil.toBox(reqDto));
		if (result.getBox("video")== null) {
			resBox = new ResResultDto<ResUISPIF004Dto>(BeanUtil.convert(result, ResUISPIF004Dto.class),"I231");
		} else {
			resBox = new ResResultDto<ResUISPIF004Dto>(BeanUtil.convert(result, ResUISPIF004Dto.class),"I230");
		}
		return resBox;
	}

	/**
	 * @Method Name : postVideo
	 * @param reqDto
	 * @return
	 */
	@ApiOperation(value = "쇼페퍼 등록")
	@PostMapping(value = "postVideo")
	@ReqInfo(validForm = "showpepper.info.showpepperInsert")
	public ResIntDto postVideo(ReqUISPIF010Dto reqDto) {
		return new ResIntDto(showPepperComponent.myShowPepperInsert(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value = "쇼페퍼 북마크")
	@PostMapping(value = "addVideoFavourite")
	@ReqInfo(validForm = "showpepper.info.showFavr")
	public ResResultDto<ResUISPIF003Dto> addVideoFavourite(@RequestBody ReqUISPIF003Dto reqDto) {
		ResResultDto<ResUISPIF003Dto> resBox = new ResResultDto<ResUISPIF003Dto>();
		Box result = showPepperComponent.addVideoFavouriteSave(BoxUtil.toBox(reqDto));
		if (result.getBox("video")== null) {
			resBox = new ResResultDto<ResUISPIF003Dto>(BeanUtil.convert(result, ResUISPIF003Dto.class),"I229");
		} else {
			resBox = new ResResultDto<ResUISPIF003Dto>(BeanUtil.convert(result, ResUISPIF003Dto.class),"I228");
		}

		return resBox;
	}

//=======================ReqDto======================================

	@Data
	@ApiModel
	public static class ReqUISPIF001Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "", example = "\0\"")
		private int device_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF002Dto {
		@Schema(description = "비디오 아이디", example = "\"120\"")
		@JsonAlias("videoId")
		private String video_id;
		@Schema(description = "디바이스 아이디", example = "\"178\"")
		@JsonAlias("deviceId")
		private String device_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF003Dto {
		@Schema(description = "비디오 아이디", example =  "\"120\"")
		@JsonAlias("videoId")
		private String video_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF009Dto {
		@Schema(description = "비디오 아이디", example = "\"58\"")
		@JsonAlias("videoId")
		private String video_id;
		@Schema(description = "댓글 comment", example = "안녕하시요")
		private String comment;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF009_1Dto {
		@Schema(description = "비디오 아이디", example = "\"58\"")
		@JsonAlias("videoId")
		private String video_id;
		@Schema(description = "comment 아이디(대댓글)", example ="\"386\"")
		@JsonAlias("commentId")
		private String comment_id;
		@Schema(description = "reply_comment 아이디(대대댓글)", example = "")
		@JsonAlias("replyCommentId")
		private String reply_comment_id;
		@Schema(description = "댓글 comment", example = "안녕하시요")
		private String comment;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF007Dto {
		@Schema(description = "댓글 아이디", example = "\"0\"")
		@JsonAlias("commentId")
		private String comment_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF007_ReplyDto {
		@Schema(description = "대댓글,대대댓글 아이디", example = "\"0\"")
		@JsonAlias("replyCommentId")
		private String reply_comment_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF008Dto {
		@Schema(description = "비디오 아이디", example = "\"58\"")
		@JsonAlias("videoId")
		private String video_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF006Dto {
		@Schema(description = "댓글 아이디", example = "\"388\"")
		@JsonAlias("commentId")
		private String comment_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF005Dto {
		@Schema(description = "댓글 아이디", example = "98")
		@JsonAlias("commentId")
		private String comment_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF004Dto {
		@Schema(description = "비디오 아이디", example = "\"58\"")
		@JsonAlias("videoId")
		private String video_id;
	}

	@Data
	@ApiModel
	public static class ReqUISPIF010Dto {
		@Schema(description = "해시태그 json", example = "{name : '#비디오'},{name : '#티비'},{name : '#사진'}")
		@JsonAlias("hashtagsJson")
		private String hashtags_json;
		@Schema(description = "사용자 json", example = "{user_id : 6},{user_id : 3},{user_id : 1}")
		@JsonAlias("UsersJson")
		private String users_json;

		@Schema(description = "영상 설명", example = "#비디오#티비#사진")
		private String description;
		@Schema(description = "영상 공개 유무", example = "public")
		@JsonAlias("privacyType")
		private String privacy_type;
		@Schema(description = "댓글 허용 여부", example = "")
		@JsonAlias("allowComments")
		private String allow_comments;
		@Schema(description = "allow_duet", example = "")
		@JsonAlias("allowDuet")
		private String allow_duet;
		@Schema(description = "비디오 아이디", example = "")
		@JsonAlias("videoId")
		private String video_id;
		@Schema(description = "사운드 아이디", example = "")
		@JsonAlias("soundId")
		private String sound_id;
		@Schema(description = "duet", example = "")
		private String duet;
		@Schema(description = "lang_id", example = "")
		@JsonAlias("langId")
		private String lang_id;
		@Schema(description = "interestId", example = "")
		@JsonAlias("interestId")
		private String interest_id;
		@Schema(description = "비디오 영상 PATH", example = "")
		@JsonAlias("videoPath")
		private String video_path;
		@Schema(description = "비디오 썸네일 이미지", example = "")
		@JsonAlias("gifPath")
		private String gif_path;

		@Data
		@ApiModel
		public static class Hashtags_json {
			@Schema(description = "해시태그 데이터")
			private String name;
		}

		@Data
		@ApiModel
		public static class Users_json {
			@Schema(description = "해시태그 데이터")
			private String user_id;
		}
	}

	@Data
	@ApiModel
	public static class ReqUISPIF011Dto {

	}

	@Data
	@ApiModel
	public static class ReqUISPIF012Dto {

	}


//=======================ResDto======================================
	@Data
	@ApiModel
	public static class ResUISPIF001Dto {
		@JsonProperty("Video")
		@JsonAlias("video")
		private VideoInfo video;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("Sound")
		@JsonAlias("sound")
		private Sound sound;

		@Data
		@ApiModel
		public static class VideoInfo {
			@Schema(description = "비디오 아이디")
			private String	id;
			@Schema(description = "등록자 아이디")
			@JsonAlias("userId")
			private String	user_id;
			@Schema(description = "description")
			private String	description;
			@Schema(description = "video")
			private String	video;
			@Schema(description = "thum")
			private String	thum;
			@Schema(description = "gif")
			private String	gif;
			@Schema(description = "비디오 사용여부")
			@JsonAlias("videoActive")
			private Boolean	video_active;
			@Schema(description = "view 수")
			private String	view;
			@Schema(description = "section")
			private String	section;
			@Schema(description = "사운드 아이디")
			@JsonAlias("soundId")
			private String	sound_id;
			@Schema(description = "공개여부")
			@JsonAlias("privacyType")
			private String	privacy_type;
			@Schema(description = "댓글 여부")
			@JsonAlias("allowComments")
			private String	allow_comments;
			@Schema(description = "듀레이션 여부")
			@JsonAlias("allowDuet")
			private String	allow_duet;
			@Schema(description = "블럭")
			private String	block;
			@Schema(description = "듀레이션 비디오 아이디")
			@JsonAlias("duetVideoId")
			private String	duet_video_id;
			@Schema(description = "예전 비디오 아이디")
			@JsonAlias("oldVideoId")
			private String	old_video_id;
			@Schema(description = "duration")
			private String	duration;
			@Schema(description = "promote")
			private String	promote;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String	reg_dt;
			@Schema(description = "좋아요")
			private String	like;
			@Schema(description = "북마크")
			private int	favourite;
			@Schema(description = "댓글 수")
			@JsonAlias("commentCount")
			private int	comment_count;
			@Schema(description = "좋아요 수")
			@JsonAlias("likeCount")
			private String	like_count;
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
//			@Schema(description = "디바이스 토큰값")
//			@JsonAlias("deviceToken")
//			private String device_token;
//			@Schema(description = "auth 토큰")
//			@JsonAlias("authToken")
//			private String auth_token;
			@Schema(description = "이메일")
			private String email;
//			@Schema(description = "비밀번호")
//			private String password;
			@Schema(description = "핸드폰번호")
			private String phone;
//			@Schema(description = "활동여부")
//			private String active;
//			@Schema(description = "cd_mst 탈퇴 사유")
//			@JsonAlias("activeReason")
//			private String active_reason;
//			@Schema(description = "cd_mst 탈퇴 상세 이유")
//			@JsonAlias("activeReasonDetail")
//			private String active_reason_detail;
			@Schema(description = "사용자 이름")
			private String username;
			@Schema(description = "권한")
			private String role;
//			@Schema(description = "포인트")
//			private String point;
//			@Schema(description = "학력")
//			private String grade;
			@Schema(description = "성")
			private String first_name;
			@Schema(description = "이름")
			private String last_name;
//			@Schema(description = "성별")
//			private String gender;
//			@Schema(description = "웹사이트")
//			private String website;
//			@Schema(description = "생년월일")
//			private String dob;
//			@Schema(description = "소셜 로그인 정보")
//			@JsonAlias("socialId")
//			private String social_id;
			@Schema(description = "프로필 1( ios )")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필2 (안드로이드)")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
//			@Schema(description = "소셜 로그인 정보")
//			private String social;
//			@Schema(description = "토큰값")
//			private String token;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "팔로우 버튼 셋팅 값")
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
		@Data
		@ApiModel
		public static class Sound {
			@Schema(description = "오디오 아이디")
			private String id;
			@Schema(description = "오디오 url")
			private String audio;
			@Schema(description = "듀레이션")
			private String duration;
			@Schema(description = "사운드 명")
			private String name;
			@Schema(description = "사운드 설명")
			private String description;
			@Schema(description = "썸네일")
			private String thum;
			@Schema(description = "사운드 섹션 아이디")
			@JsonAlias("soundSectionId")
			private String sound_section_id;
			@Schema(description = "업로드")
			@JsonAlias("uploadedBy")
			private String uploaded_by;
			@Schema(description = "")
			private String publish;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String reg_dt;
		}
	}

	@Data
	@ApiModel
	public static class ResUISPIF002Dto {
		@JsonProperty("Video")
		@JsonAlias("video")
		private VideoInfo video;
		@JsonProperty("VideoWatch")
		@JsonAlias("videoWatch")
		private VideoWatch videoWatch;
		@JsonProperty("Device")
		@JsonAlias("device")
		private Device device;
		@JsonProperty("User")
		@JsonAlias("user")
		private UserInfo user;


		@Data
		@ApiModel
		public static class Device {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "key값")
			private String key;
			@Schema(description = "생성 일자")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class UserInfo {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "회원 닉네임")
			private String username;
			@Schema(description = "회원 활동여부")
			private String active;
			@Schema(description = "프로필 1( ios )")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필2 (안드로이드)")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
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
			@Schema(description = "allow_duet")
			@JsonAlias("allowDuet")
			private String allow_duet;
			@Schema(description = "")
			private int block;
			@Schema(description = "old_video_id")
			@JsonAlias("oldVideoId")
			private String old_video_id;
			@Schema(description = "duetVideoId")
			@JsonAlias("duetVideoId")
			private String duet_video_id;
			@Schema(description = "영상길이")
			private float duration;
			@Schema(description = "promote")
			private float promote;
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
			@Schema(description = "북마크 수")
			@JsonAlias("favoriteCount")
			private String favorite_count;

			@Data
			@ApiModel
			public static class User {
				@Schema(description = "회원 아이디")
				private String id;
				@Schema(description = "활동여부")
				private String active;
				@Schema(description = "사용자 이름")
				private String username;
				@Schema(description = "프로필 1( ios )")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "프로필2 (안드로이드)")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
			}
		}
		@Data
		@ApiModel
		public static class VideoWatch {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			private String video_id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "생성일자")
			@JsonAlias("regDt")
			private String reg_dt;
		}
	}

	@Data
	@ApiModel
	public static class ResUISPIF009_1Dto {
		@Schema(description = "대댓글,대대댓글 등록시 성공 메세지")
		private String resultMsg;
	}

	@Data
	@ApiModel
	public static class ResUISPIF009Dto {
		@JsonProperty("VideoComment")
		@JsonAlias("videoComment")
		private VideoComment videoComment;
		@JsonProperty("Video")
		@JsonAlias("video")
		private VideoInfo video;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("VideoCommentReply")
		@JsonAlias("videoCommentReply")
		private VideoCommentReply videoCommentReply;

		@Data
		@ApiModel
		public static class User {
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
			@Schema(description = "포인트")
			private String point;
			@Schema(description = "학력")
			private String grade;
			@Schema(description = "성")
			@JsonAlias("firstName")
			private String first_name;
			@Schema(description = "이름")
			@JsonAlias("lastName")
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
			@Schema(description = "소셜 로그인 정보")
			private String social;
			@Schema(description = "토큰값")
			private String token;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "passChnYn")
			@JsonAlias("passChnYn")
			private String pass_chn_yn;
			@Schema(description = "passModifyDatetime")
			@JsonAlias("passModifyDatetime")
			private String pass_modify_datetime;
			@Schema(description = "lockCnt")
			@JsonAlias("lockCnt")
			private String lock_cnt;
			@Schema(description = "ip")
			private String ip;
		}

		@Data
		@ApiModel
		public static class VideoComment {
			@Schema(description = "아이디")
			public String id;
			@Schema(description = "작성자 아이디")
			@JsonAlias("userId")
			public String user_id;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			public String video_id;
			@Schema(description = "댓글내용")
			public String comment;
			@Schema(description = "댓글 활성화 여부")
			@JsonAlias("commentActive")
			public String comment_active;
			@Schema(description = "댓글 활성화 여부")
			@JsonAlias("regDt")
			public String reg_dt;
		}

		@Data
		@ApiModel
		public static class VideoInfo {
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
			@Schema(description = "allow_duet")
			@JsonAlias("allowDuet")
			private String allow_duet;
			@Schema(description = "")
			private int block;
			@Schema(description = "old_video_id")
			@JsonAlias("oldVideoId")
			private String old_video_id;
			@Schema(description = "duetVideoId")
			@JsonAlias("duetVideoId")
			private String duet_video_id;
			@Schema(description = "영상길이")
			private float duration;
			@Schema(description = "promote")
			private float promote;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}
		@Data
		@ApiModel
		public static class VideoCommentReply {
			@Schema(description = "아이디")
			public String id;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			public String video_id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			public String user_id;
			@Schema(description = "댓글아이디")
			@JsonAlias("commentId")
			public String comment_id;
			@Schema(description = "대댓글/대대댓글 아이디")
			@JsonAlias("replyCommentId")
			public String reply_comment_id;
			@Schema(description = "대댓글/대대댓글 활성화 여부")
			@JsonAlias("replyCommentActive")
			public String reply_comment_active;
			@Schema(description = "댓글 내용")
			public String comment;
			@Schema(description = "작성 일자")
			@JsonAlias("regDt")
			public String reg_dt;
			@Schema(description = "좋아요 여부")
			public String isLike;
			@Schema(description = "좋아요 수")
			@JsonAlias("likeCnt")
			public String like_cnt;
			@Schema(description = "작성일자")
			@JsonAlias("diffDate")
			public String diff_date;
			@Schema(description = "replyReply")
			@JsonAlias("replyReply")
			public String reply_reply;
		}
	}

	@Data
	@ApiModel
	public static class ResUISPIF007Dto {
	}

	@Data
	@ApiModel
	public static class ResUISPIF008Dto {
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("VideoCommentReply")
		@JsonAlias("videoCommentReply")
		private List<VideoCommentReply> videoCommentReply;

		@Schema(description = "아이디")
		public String id;
		@Schema(description = "작성자 아이디")
		@JsonAlias("userId")
		public String user_id;
		@Schema(description = "비디오 아이디")
		@JsonAlias("videoId")
		public String video_id;
		@Schema(description = "댓글내용")
		public String comment;
		@Schema(description = "댓글 활성화 여부")
		@JsonAlias("commentActive")
		public String comment_active;
		@Schema(description = "작성일자")
		@JsonAlias("diffDate")
		public String diff_date;
		@Schema(description = "생성일자")
		@JsonAlias("regDt")
		public String reg_dt;
		@Schema(description = "좋아요 여부")
		public String isLike;
		@Schema(description = "좋아요 수")
		@JsonAlias("likeCount")
		public String like_count;

		@Data
		@ApiModel
		public static class User {
			@Schema(description = "아이디")
			public String id;
			@Schema(description = "사용자 이름")
			public String username;
			@Schema(description = "성")
			@JsonAlias("firstName")
			public String first_name;
			@Schema(description = "이름")
			@JsonAlias("lastName")
			public String last_name;
			@Schema(description = "탈퇴/블럭여부(1=활동,0=삭제,2=블럭)")
			public String active;
			@Schema(description = "프로필 사진")
			@JsonAlias("profilePic")
			public String profile_pic;
			@Schema(description = "썸네일 사진")
			@JsonAlias("profilePicSmall")
			public String profile_pic_small;
			@Schema(description = "사용자 권한")
			public String role;
		}

		@Data
		@ApiModel
		public static class VideoCommentReply {
			@JsonProperty("User")
			@JsonAlias("user")
			private ReplyUser user;

			@Schema(description = "아이디")
			public String id;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			public String video_id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			public String user_id;
			@Schema(description = "댓글아이디")
			@JsonAlias("commentId")
			public String comment_id;
			@Schema(description = "대댓글/대대댓글 아이디")
			@JsonAlias("replyCommentId")
			public String reply_comment_id;
			@Schema(description = "대댓글/대대댓글 활성화 여부")
			@JsonAlias("replyCommentActive")
			public String reply_comment_active;
			@Schema(description = "댓글 내용")
			public String comment;
			@Schema(description = "작성 일자")
			@JsonAlias("regDt")
			public String reg_dt;
			@Schema(description = "좋아요 여부")
			public String isLike;
			@Schema(description = "좋아요 수")
			@JsonAlias("likeCnt")
			public String like_cnt;
			@Schema(description = "작성일자")
			@JsonAlias("diffDate")
			public String diff_date;
			@Schema(description = "replyReply")
			@JsonAlias("replyReply")
			public String reply_reply;

			@Data
			@ApiModel
			public static class ReplyUser {
				@Schema(description = "아이디")
				public String id;
				@Schema(description = "사용자 이름")
				public String username;
				@Schema(description = "탈퇴/블럭여부(1=활동,0=삭제,2=블럭)")
				public String active;
				@Schema(description = "프로필 사진")
				@JsonAlias("profilePic")
				public String profile_pic;
				@Schema(description = "썸네일 사진")
				@JsonAlias("profilePicSmall")
				public String profile_pic_small;
				@Schema(description = "사용자 권한")
				public String role;
			}
		}
	}

	@Data
	@ApiModel
	public static class ResUISPIF011Dto {
		@Schema(description = "사운드 아이디")
		private String id;
		@Schema(description = "사운드")
		private String audio;
		@Schema(description = "사운드 길이")
		private String duration;
		@Schema(description = "사운드 명")
		private String name;
		@Schema(description = "사운드 설명")
		private String description;
		@Schema(description = "사운드 썸네일 이미지")
		private String thum;
		@Schema(description = "사운드 섹션 아이디")
		@JsonAlias("soundSectionId")
		private String sound_section_id;
		@Schema(description = "사운드 업로드 정보")
		@JsonAlias("uploadedBy")
		private String uploaded_by;
		@Schema(description = "PUBLISH")
		private String publish;
		@Schema(description = "등록자 아이디")
		@JsonAlias("regId")
		private String reg_id;
		@Schema(description = "등록 일자")
		@JsonAlias("regDt")
		private String reg_dt;
	}

	@Data
	@ApiModel
	public static class ResUISPIF003Dto {
		@JsonProperty("VideoFavourite")
		@JsonAlias("videoFavourite")
		private VideoFavourite videoFavourite;
		@JsonProperty("Video")
		@JsonAlias("video")
		private VideoInfo video;
		@JsonProperty("User")
		@JsonAlias("user")
		private UserInfo user;
		@JsonProperty("PushNotification")
		@JsonAlias("pushNotification")
		private PushNotification pushNotification;
		@JsonProperty("PrivacySetting")
		@JsonAlias("privacySetting")
		private PrivacySetting privacySetting;

		@Data
		@ApiModel
		public static class VideoFavourite {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "사용자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			private String video_id;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class VideoInfo {
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
			@Schema(description = "allow_duet")
			@JsonAlias("allowDuet")
			private String allow_duet;
			@Schema(description = "")
			private String block;
			@Schema(description = "old_video_id")
			@JsonAlias("oldVideoId")
			private String old_video_id;
			@Schema(description = "duetVideoId")
			@JsonAlias("duetVideoId")
			private String duet_video_id;
			@Schema(description = "영상길이")
			private String duration;
			@Schema(description = "promote")
			private String promote;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

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
			@Schema(description = "포인트")
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
			@Schema(description = "소셜 로그인 정보")
			private String social;
			@Schema(description = "IP")
			private String ip;
			@Schema(description = "토큰값")
			private String token;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "로그인 실패 카운트")
			@JsonAlias("lockCnt")
			private String lock_cnt;
			@Schema(description = "passChnYn")
			@JsonAlias("passChnYn")
			private String pass_chn_yn;
			@Schema(description = "passModifyDatetime")
			@JsonAlias("passModifyDatetime")
			private String pass_modify_datetime;
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

		@Data
		@ApiModel
		public static class PrivacySetting {
			@Schema(description = "id")
	        private String  id;
			@Schema(description = "videos_download")
			@JsonAlias("videosDownload")
	        private String  videos_download;
			@Schema(description = "direct_message")
			@JsonAlias("directMessage")
	        private String  direct_message;
			@Schema(description = "duet")
	        private String  duet;
			@Schema(description = "liked_videos")
			@JsonAlias("likedVideos")
	        private String  liked_videos;
			@Schema(description = "videoComment")
			@JsonAlias("videoComment")
            private String  video_comment;
		}
	}

	@Data
	@ApiModel
	public static class ResUISPIF004Dto {

	}

	@Data
	@ApiModel
	public static class ResUISPIF005Dto {
		@Schema(description = "내가 대댓글,대대댓글에 좋아요 여부")
		private String isLike;
		@Schema(description = "전체 대댓글 or 대대댓글에 좋아요 수")
		@JsonAlias("likeCount")
		private int like_count;

	}
}
