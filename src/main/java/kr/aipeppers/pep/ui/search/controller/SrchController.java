package kr.aipeppers.pep.ui.search.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import kr.aipeppers.pep.core.domain.ReqPageDto;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResPageDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.search.component.SrchComponent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@RestController
@RequestMapping("/restapi/srch/info")
@Api(tags = { "검색>정보" }, description = "SrchController")
public class SrchController {

	@Autowired
	protected SrchComponent srchCompo;

	/**
	 * @param <T>
	 * @Method Name : search
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "검색")
	@PostMapping(value = "search") //
	public <T> ResListDto<T> search(@RequestBody ReqUISCIF001Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		List<T> list = new ArrayList<T>();
		if ("user".equals(reqDto.getType())) {
			list = (List<T>)BeanUtil.convertList(srchCompo.searchList(box), ResUISCIF001UserDto.class);
		} else if ("video".equals(reqDto.getType())) {
			list = (List<T>)BeanUtil.convertList(srchCompo.searchList(box), ResUISCIF001VideoDto.class);
		} else if ("hashtag".equals(reqDto.getType())) {
			list = (List<T>)BeanUtil.convertList(srchCompo.searchList(box), ResUISCIF001HashtageDto.class);
		}
		return new ResListDto<T>(list, BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : showDiscoverySections
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "배너 & 해시테그조회")
	@PostMapping(value = "showDiscoverySections") //
	public ResListDto<ResUISCIF002Dto> showDiscoverySections(@RequestBody ReqUISCIF002Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResUISCIF002Dto>(BeanUtil.convertList(srchCompo.hashtagAllList(box), ResUISCIF002Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

//	/**
//	 * @Method Name : userHashtagList
//	 * @param reqDto
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation(value = "사용자 검색")
//	@PostMapping(value = "showVideosAgainstUserID") //
//	public ResListDto<ResUISCIF005Dto> showVideosAgainstUserID(@RequestBody ReqUISCIF005Dto reqDto) throws Exception {
//		Box box = BoxUtil.toBox(reqDto);
//		return new ResListDto<ResUISCIF005Dto>(BeanUtil.convertList(srchCompo.userHashtagList(box), ResUISCIF005Dto.class));
//	}
//
//	@ApiOperation(value = "showPepper 영상 리스트")
//	@PostMapping(value = "showRelatedVideos") //
//	public ResListDto<ResUISCIF006Dto> showRelatedVideos(@RequestBody ReqUISCIF006Dto reqDto) throws Exception {
//		Box box = BoxUtil.toBox(reqDto);
//		return new ResListDto<ResUISCIF006Dto>(BeanUtil.convertList(srchCompo.showPeperVideoList(box), ResUISCIF006Dto.class));
//	}

	/**
	 * @Method Name : showVideosAgainstHashtag
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "태그 영상 상세 페이지")
	@PostMapping(value = "showVideosAgainstHashtag") //
	public ResListDto<ResUISCIF003Dto> showVideosAgainstHashtag(@RequestBody ReqUISCIF003Dto reqDto) throws Exception {
		return new ResListDto<ResUISCIF003Dto>(BeanUtil.convertList(srchCompo.hashtagDetailList(BoxUtil.toBox(reqDto)), ResUISCIF003Dto.class));
	}

	@ApiOperation(value = "해시태크 북마크 설정/해제")
	@PostMapping(value = "addHashtagFavourite")
	@ReqInfo(validForm = "srch.info.favouriteChk")
	public ResResultDto<ResUISCIF004Dto> addHashtagFavourite(@RequestBody ReqUISCIF004Dto reqDto) throws Exception {
		return new ResResultDto<ResUISCIF004Dto>(BeanUtil.convert(srchCompo.hashfavourSave(BoxUtil.toBox(reqDto)), ResUISCIF004Dto.class));
	}

	// -------------------------- reqDto -----------------------------------------------------------
	@Data
	@ApiModel
	public static class ReqUISCIF001Dto{
		@Schema(description = "검색어" , example = "대")
		private String keyword;
		@Schema(description = "페이지" , example = "1")
		private int page;
		@Schema(description = "타입" , example = "user")
		private String type;
	}

	@Data
	@ApiModel
	public static class ReqUISCIF002Dto {
		@Schema(description = "page" , example = "0")
		private int page;
		@Schema(description = "pageUnit(몇개씩보여줄지)" , example = "10")
		private int pageUnit;
	}

	@Data
	@ApiModel
	public static class ReqUISCIF003Dto {
		@Schema(description = "hashtag" , example = "고양이")
		private String hashtag;
	}

	@Data
	@ApiModel
	public static class ReqUISCIF004Dto {
		@Schema(description = "해시태그 아이디" , example = "아이디 ")
		@JsonAlias("hashtagId")
		private String hashtag_id;
	}

	@Data
	@ApiModel
	public static class ReqUISCIF005Dto {
		@Schema(description = "검색어", example = "성")
		private String keyword;
	}

	@Data
	@ApiModel
	public static class ReqUISCIF006Dto {
		@Schema(description = "검색어", example = "성")
		private String keyword;
	}


	// -------------------------- resDto -----------------------------------------------------------
	@Data
	@ApiModel
	public static class ResUISCIF001UserDto {
		@Schema(description = "아이디")
		private String	id;
		@Schema(description = "이메일")
		private String	email;
		@Schema(description = "패스워드")
		private String	password;
		@Schema(description = "활동 여부")
		private String	active;
		@Schema(description = "사용자 이름")
		private String	username;
		@Schema(description = "권한")
		private String	role;
		@Schema(description = "프로필 사진")
		@JsonAlias("profilePic")
		private String	profile_pic;
		@Schema(description = "프로필 사진")
		@JsonAlias("profilePicSmall")
		private String	profile_pic_small;
		@Schema(description = "회원가입일")
		@JsonAlias("regDt")
		private String	reg_dt;
		@Schema(description = "팔로우 수")
		@JsonAlias("followersCount")
		private String	followers_count;
		@Schema(description = "비디오 등록 수")
		@JsonAlias("videosCount")
		private String	videos_count;
	}

	@Data
	@ApiModel
	public static class ResUISCIF001VideoDto {
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
			private String id;
			@Schema(description = "등록자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "비디오 설명")
			private String description;
			@Schema(description = "비디오URL")
			private String video;
			@Schema(description = "썸네일 이미지1")
			private String thum;
			@Schema(description = "썸네일 이미지2")
			private String gif;
			@Schema(description = "비디오 활성화 여부")
			@JsonAlias("videoActive")
			private String video_active;
			@Schema(description = "뷰 수")
			private String view;
			@Schema(description = "섹션")
			private String section;
			@Schema(description = "사운드 아이디")
			@JsonAlias("soundId")
			private String sound_id;
			@Schema(description = "프라이빗 여부")
			@JsonAlias("privacyType")
			private String privacy_type;
			@Schema(description = "댓글 사용 여부")
			@JsonAlias("allowComments")
			private String allow_comments;
			@Schema(description = "듀엣 유무")
			@JsonAlias("allowDuet")
			private String allow_duet;
			@Schema(description = "블럭 유무")
			private String block;
			@Schema(description = "듀엣 비디오 아이디")
			@JsonAlias("duetVideoId")
			private String duet_video_id;
			@Schema(description = "이전 비디오 아이디")
			@JsonAlias("oldVideoId")
			private String old_video_id;
			@Schema(description = "duration")
			private String duration;
			@Schema(description = "promote")
			private String promote;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "수정자")
			@JsonAlias("modId")
			private String mod_id;
			@Schema(description = "수정날짜")
			@JsonAlias("modDt")
			private String mod_dt;
			@Schema(description = "카운트")
			@JsonAlias("likeCount")
			private String like_count;
		}
		@Data
		@ApiModel
		public static class User {
			@JsonProperty("PushNotification")
			@JsonAlias("pushNotification")
			private PushNotification pushNotification;

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
			@Schema(description = "소셜 로그인 정보")
			private String social;
			@Schema(description = "토큰값")
			private String token;
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
	public static class ResUISCIF001HashtageDto {
		@Schema(description = "해시태그 아이디")
		private String id;
		@Schema(description = "해시태그 명")
		private String name;
		@Schema(description = "시청 건수")
		private String views;
		@Schema(description = "해시테그의 비디오 개수")
		@JsonAlias("videosCount")
		private String videos_count;
		@Schema(description = "북마크")
		private String favourite;
		@JsonAlias("hCnt")
		@Schema(description = "확인용")
		private String h_cnt;
	}



	@Data
	@ApiModel
	public static class ResUISCIF002Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "해시태그 이름")
		private String name;
		@Schema(description = "해시태그 총 게시글")
		@JsonAlias("hCnt")
		private String h_cnt;
		@Schema(description = "해시코드전체 영상 cnt")
		@JsonAlias("totalViews")
		private String total_views;
		@Schema(description = "해시코드전체 영상 cnt")
		@JsonAlias("views")
		private String views;
		@JsonProperty("Video")
		@JsonAlias("video")
		private List<Video> video;


		@Data
		@ApiModel
		public static class Video {
			private User user;
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
			@Schema(description = "")
			private int block;
			@Schema(description = "영상길이")
			private float duration;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "해시대그비디오아이디")
			@JsonAlias("hvId")
			private String hv_id;

			@Schema(description = "좋아요")
			private int like;
			@Schema(description = "북마크 cnt")
			private int favourite;
            @Schema(description = "댓글 cnt")
			@JsonAlias("comment_count")
			private int comment_count;
            @Schema(description = "좋아요숫자")
			@JsonAlias("like_count")
            private int like_count;

            @JsonProperty("Hashtag")
    		@JsonAlias("hashtag")
    		private Hashtag hashtag;
            @JsonProperty("HashtagVideo")
    		@JsonAlias("hashtagVideo")
    		private HashtagVideo hashtagVideo;

            @Data
    		@ApiModel
    		public static class HashtagVideo {
    			@Schema(description = "pk 값")
    			private String id ;
    			@Schema(description = "해시태그 아이디")
    			@JsonAlias("hashtagId")
    			private String hashtag_id ;
    			@Schema(description = "비디오 아이디")
    			@JsonAlias("videoId")
    			private String video_id ;
    		}

    		@Data
    		@ApiModel
    		public static class Hashtag {
    			@Schema(description = "pk 값")
    			private String id ;
    			@Schema(description = "해시태그 명")
    			private String name ;
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
	}

	@Data
	@ApiModel
	public static class ResUISCIF003Dto {
		@JsonProperty("HashtagVideo")
		@JsonAlias("hashtagVideo")
		private HashtagVideo hashtagVideo;
		@JsonProperty("Hashtag")
		@JsonAlias("hashtag")
		private Hashtag hashtag;
		@JsonProperty("Video")
		@JsonAlias("video")
		private Video video;

		@Schema(description = "비디오 전체 조회 수")
		private int views;
		@Schema(description = "비디오 전체 개수")
		@JsonAlias("videosCount")
		private int videos_count;

		@JsonProperty("PrivacySetting")
		@JsonAlias("privacySetting")
		private PrivacySetting privacySetting;
		@Data
		@ApiModel
		public static class PrivacySetting {

		}

		@Data
		@ApiModel
		public static class HashtagVideo {
			@Schema(description = "pk 값")
			private String id ;
			@Schema(description = "해시태그 아이디")
			@JsonAlias("hashtagId")
			private String hashtag_id ;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			private String video_id ;
		}

		@Data
		@ApiModel
		public static class Hashtag {
			@Schema(description = "pk 값")
			private String id ;
			@Schema(description = "해시태그 명")
			private String name ;
			@Schema(description = "북마크 cnt")
			private String favorite ;
		}

		@Data
		@ApiModel
		public static class Video {
			private User user;
			private Sound sound;

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
			@Schema(description = "")
			private int block;
			@Schema(description = "영상길이")
			private float duration;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "좋아요 true/false ")
			private int like;
			@Schema(description = "북마크 cnt")
			private int favourite;
            @Schema(description = "댓글 cnt")
			@JsonAlias("commentCount")
			private int comment_count;
            @Schema(description = "")
			@JsonAlias("likeCount")
            private int like_count;
			@Data
			@ApiModel
			public static class User {
				@JsonProperty("PushNotification")
				@JsonAlias("pushNotification")
				private PushNotification pushNotification;

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
				@Schema(description = "소셜 로그인 정보")
				private String social;
				@Schema(description = "토큰값")
				private String token;
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
	}

	@Data
	@ApiModel
	public static class ResUISCIF004Dto {
		@JsonProperty("HashtagFavourite")
		@JsonAlias("hashtagFavourite")
		private HashtagFavourite hashtagFavourite;
		@JsonProperty("Hashtag")
		@JsonAlias("hashtag")
		private HashtagInfo hashtag;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;

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
			@Schema(description = "생성일자")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "생성자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "수정일자")
			@JsonAlias("modDt")
			private String mod_dt;
			@Schema(description = "수정자")
			@JsonAlias("modId")
			private String mod_id;
			@Schema(description = "삭제시 메세지")
			private String msg;
		}

		@Data
		@ApiModel
		public static class HashtagInfo {
			@Schema(description = "hashtag아이디")
			private String id;
			@Schema(description = "해시태그 이름")
			private String name;
		}

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
			@Schema(description = "소셜 로그인 정보")
			private String social;
			@Schema(description = "토큰값")
			private String token;
			@Schema(description = "생성날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}
	}

	@Data
	@ApiModel
	public static class ResUISCIF005Dto {
		@Schema(description = "사용자 아이디")
		private String id;
		@Schema(description = "사용자 닉네임")
		private String username;
		@Schema(description = "팔로워 수")
		private String follower;
		@Schema(description = "올린 비디오 개수")
		private String videoCnt;
	}

	@Data
	@ApiModel
	public static class ResUISCIF006Dto {
		@JsonProperty("Video")
		@JsonAlias("video")
		private Video video;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@JsonProperty("Sound")
		@JsonAlias("sound")
		private Sound sound;

		@Data
		@ApiModel
		public static class Video {
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
			private String comment_count;
			@Schema(description = "좋아요 수")
			private String like_count;
		}
		@Data
		@ApiModel
		public static class User {
			@JsonProperty("PushNotification")
			@JsonAlias("pushNotification")
			private PushNotification pushNotification;

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
			@Schema(description = "소셜 로그인 정보")
			private String social;
			@Schema(description = "토큰값")
			private String token;
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
}
