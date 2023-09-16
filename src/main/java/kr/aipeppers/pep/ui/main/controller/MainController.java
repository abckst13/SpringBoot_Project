package kr.aipeppers.pep.ui.main.controller;

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
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.main.component.MainComponet;
import kr.aipeppers.pep.ui.main.service.MainService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/restapi/main/info")
@Api(tags = { "메인>정보" }, description = "MainController")
public class MainController {

	@Autowired
	protected MainComponet mainComponet;
	@Autowired
	protected MainService mainService;

	//SNS api
	/**
	 * @Method Name : aipepperPotoList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "메인 SNS")
	@PostMapping(value = "aipepperMainSNSList")
	public ResResultDto<ResUIMNIF001Dto> aipepperMainSNSList(@RequestBody ReqUIMNIF001Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMNIF001Dto>(BeanUtil.convert(mainComponet.aipepperMainSNSList(BoxUtil.toBox(reqDto)) , ResUIMNIF001Dto.class));

	}

	/**
	 * @Method Name : aipepperMainList
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "메인 배구단 소식")
	@PostMapping(value = "aipepperMainInfoList")
	public ResResultDto<ResUIMNIF002Dto> aipepperMainInfoList(@RequestBody ReqUIMNIF002Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMNIF002Dto>(BeanUtil.convert(mainComponet.aipepperMainInfoList(BoxUtil.toBox(reqDto)) , ResUIMNIF002Dto.class));

	}

	@ApiOperation(value = "경기 일정")
	@PostMapping(value = "nextMatchBanner")
	public ResResultDto<NextMatchDto> nextMatchBanner(@RequestBody ReqUIMNIF002Dto reqDto) throws Exception {
		return new ResResultDto<NextMatchDto>(BeanUtil.convert(mainService.nextMatchInfoView() , NextMatchDto.class));

	}


	@ApiOperation(value = "showAppSlider")
	@PostMapping(value = "showAppSlider")
	public ResListDto<ResAppSliderDto> showAppSlider(@RequestBody ReqAppSliderDto reqDto) throws Exception {
		return new ResListDto<ResAppSliderDto>(BeanUtil.convertList(mainComponet.showAppSliderList(BoxUtil.toBox(reqDto)), ResAppSliderDto.class));
	}

//	@ApiOperation(value = "이벤트")
//	@PostMapping(value = "aipepperEventInfoList") // bannerNewsList
//	public ResListDto<ResUIMNIF005Dto> aipepperEventInfoList(@RequestBody ReqUIMNIF005Dto reqDto) throws Exception {
//		return new ResListDto<ResUIMNIF005Dto>(BeanUtil.convertList(mainComponet.aipepperEventInfoList(BoxUtil.toBox(reqDto)), ResUIMNIF005Dto.class));
//	}

	@ApiOperation(value = "알림 카운트 조회")
	@PostMapping(value = "getNotification") // notificationCnt
	public ResResultDto<ResUIMNIF003Dto> getNotification(@RequestBody ReqUIMNIF003Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMNIF003Dto>(
				BeanUtil.convert(mainComponet.notificationCnt(BoxUtil.toBox(reqDto)), ResUIMNIF003Dto.class));
	}

	@ApiOperation(value = "알림 리스트 조회")
	@PostMapping(value = "notificationList") // notificationList
	public ResListDto<ResUIMNIF004Dto> notificationList(@RequestBody ReqUIMNIF004Dto reqDto) throws Exception {
		return new ResListDto<ResUIMNIF004Dto>(
				BeanUtil.convertList(mainComponet.notificationList(BoxUtil.toBox(reqDto)), ResUIMNIF004Dto.class));
	}

	@ApiOperation(value = "팝업 이벤트")
	@PostMapping(value = "popupEventList") // notificationList
	public ResResultDto<ResUIMNIF005Dto> popupEventList(@RequestBody ReqUIMNIF005Dto reqDto) throws Exception {
		return new ResResultDto<ResUIMNIF005Dto>(BeanUtil.convert(mainComponet.popupEventView(BoxUtil.toBox(reqDto)), ResUIMNIF005Dto.class));
	}
	// -------------- reqParameter-------------------

	@Data
	@ApiModel
	public static class ReqUIMNIF001Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMNIF002Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMNIF003Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMNIF004Dto {

	}

	@Data
	@ApiModel
	public static class ReqUIMNIF005Dto {

	}


	// -------------- resParameter-------------------

	@Data
	@ApiModel
	public static class ResUIMNIF003Dto {
		@Schema(description = "안읽은 알림cnt")
		private int notificationCnt;
	}

	@Data
	@ApiModel
	public static class ReqAppSliderDto {

	}

	@Data
	@ApiModel
	public static class ResUIMNIF004Dto {


		@JsonProperty("Notification")
		@JsonAlias("notification")
		private Notification Notification;

		@JsonProperty("Sender")
		@JsonAlias("sender")
		private Sender Sender;

		@Data
		@ApiModel
		public static class Sender {
			@Schema(description = "id")
			private String id;
			@Schema(description = "닉네임")
			private String username;
			@Schema(description = "프로필")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "프로필")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
			@Schema(description = "사용자타입")
			private String role;
			@Schema(description = "사용여부")
			private String active;
		}

		@Data
		@ApiModel
		public static class Notification {
			@Schema(description = "알림 아이디")
			private String id;
			@Schema(description = "보낸 사람")
			@JsonAlias("senderId")
			private String sender_id;
			@Schema(description = "받는 사람")
			@JsonAlias("receiverId")
			private String receiver_id;
			@Schema(description = "알림 내용")
			private String string;
			@Schema(description = "게시글 타입코드")
			private String type;
			@Schema(description = "비디오 아이디")
			@JsonAlias("videoId")
			private String video_id;
			@Schema(description = "포스트 아이디")
			@JsonAlias("postId")
			private String post_id;
			@Schema(description = "작성자")
			@JsonAlias("regId")
			private int reg_id;
			@Schema(description = "작성일자")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "수정자")
			@JsonAlias("modId")
			private int mod_id;
			@Schema(description = "수정일자")
			@JsonAlias("modDt")
			private String mod_dt;
			@Schema(description = "읽음 yn")
			private int isRead;
		}


	}

	@Data
	@ApiModel
	public static class ResUIMNIF001Dto {
		@JsonProperty("PepperNaverPost")
		@JsonAlias("pepperNaverPost")
		private List<PepperNaverPost> pepperNaverPost;
		@JsonProperty("PepperNaverTv")
		@JsonAlias("pepperNaverTv")
		private List<PepperNaverTv> pepperNaverTv;
		@JsonProperty("PepperStagram")
		@JsonAlias("pepperStagram")
		private List<PepperStagram> pepperStagram;
		@JsonProperty("PepperPotoList")
		@JsonAlias("pepperPotoList")
		private List<PepperPotoList> pepperPotoList;
		@JsonProperty("PepperPlayer")
		@JsonAlias("pepperPlayer")
		private List<PepperPlayer> pepperPlayer;

		@Data
		@ApiModel
		public static class PepperNaverPost {
			@Schema(description = "게시글 아이디")
			private String id;
			@Schema(description = "게시글 타입")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "게시글 제목")
			private String title;
			@Schema(description = "게시글 컨텐트 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 컨텐트 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회 수")
			private String hits;
			@Schema(description = "삭제 여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용 여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인 노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 사용 여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private String ord;
			@Schema(description = "썸네일")
			private String thumb;
			@Schema(description = "팝엄 썸네일")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "등록 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class PepperNaverTv {
			@Schema(description = "게시글 아이디")
			private String id;
			@Schema(description = "게시글 타입")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "게시글 제목")
			private String title;
			@Schema(description = "게시글 컨텐트 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 컨텐트 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회 수")
			private String hits;
			@Schema(description = "삭제 여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용 여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인 노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 사용 여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private String ord;
			@Schema(description = "썸네일")
			private String thumb;
			@Schema(description = "팝엄 썸네일")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "등록 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class PepperStagram {
			@Schema(description = "게시글 아이디")
			private String id;
			@Schema(description = "게시글 타입")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "게시글 제목")
			private String title;
			@Schema(description = "게시글 컨텐트 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 컨텐트 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회 수")
			private String hits;
			@Schema(description = "삭제 여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용 여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인 노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 사용 여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private String ord;
			@Schema(description = "썸네일")
			private String thumb;
			@Schema(description = "팝엄 썸네일")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "등록 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class PepperPotoList {
			@Schema(description = "게시글 아이디")
			private String id;
			@Schema(description = "게시글 타입")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "게시글 제목")
			private String title;
			@Schema(description = "게시글 컨텐트 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 컨텐트 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회 수")
			private String hits;
			@Schema(description = "삭제 여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용 여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인 노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 사용 여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private String ord;
			@Schema(description = "썸네일")
			private String thumb;
			@Schema(description = "팝엄 썸네일")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "등록 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class PepperPlayer {
			@Schema(description = "선수 아이디")
			private String	id;
			@Schema(description = "선수 이름")
			@JsonAlias("playerName")
			private String	player_name;
			@Schema(description = "선수 영문 이름")
			@JsonAlias("ePlayerName")
			private String	e_player_name;
			@Schema(description = "선수 가입 아이디")
			@JsonAlias("userId")
			private String	user_id;
			@Schema(description = "선수 사진 url")
			@JsonAlias("playerPicUrl")
			private String	player_pic_url;
			@Schema(description = "선수 프로필 이미지")
			@JsonAlias("profilePic")
			private String	profile_pic;
			@Schema(description = "선수 썸네일 이미지")
			@JsonAlias("profilePicSmall")
			private String	profile_pic_small;
			@Schema(description = "선수 블로그 URL")
			@JsonAlias("bgUrl")
			private String	bg_url;
			@Schema(description = "선수 사용 여부")
			@JsonAlias("useYn")
			private String	use_yn;
			@Schema(description = "사용자가 북마크 한 YN")
			@JsonAlias("bookMarkYn")
			private String	book_mark_yn;
			@Schema(description = "선수가 담벼락 등록 YN(2틀이내 담벼락만 표시)")
			@JsonAlias("newYn")
			private String	new_yn;
			@Schema(description = "선수 입단일")
			@JsonAlias("joinDate")
			private String join_date;
			@Schema(description = "선수 포지션")
			private String position;
			@Schema(description = "선수 쿠부 url")
			@JsonAlias("kovoUrl")
			private String kovo_url;
			@Schema(description = "선수 조회 순서")
			private String ord;
			@Schema(description = "선수 백 넘버")
			@JsonAlias("backNo")
			private String back_no;
			@Schema(description = "선수 국적")
			private String nation;
			@Schema(description = "선수 생년월일")
			@JsonAlias("birthDate")
			private String birth_date;
			@Schema(description = "선수 키")
			private String height;
			@Schema(description = "선수 무게")
			private String weight;
			@Schema(description = "선수 초등학교")
			@JsonAlias("elSchool")
			private String el_school;
			@Schema(description = "선수 중학교")
			@JsonAlias("midSchool")
			private String mid_school;
			@Schema(description = "선수 고등학교")
			@JsonAlias("highSchool")
			private String high_school;
			@Schema(description = "선수 대학")
			private String university;
		}

	}

	@Data
	@ApiModel
	public static class ResUIMNIF002Dto {
		@JsonProperty("NextMatchInfoView")
		@JsonAlias("nextMatchInfoView")
		private NextMatchInfoView nextMatchInfoView;
		@JsonProperty("PopupEventList")
		@JsonAlias("popupEventList")
		private List<PopupEventList> popupEventList;
		@JsonProperty("MainYoutubeList")
		@JsonAlias("mainYoutubeList")
		private List<MainYoutubeList> mainYoutubeList;
		@JsonProperty("PepperPlayer")
		@JsonAlias("pepperPlayer")
		private List<PepperPlayer> pepperPlayer;
		@JsonProperty("PeppersNews")
		@JsonAlias("peppersNews")
		private List<PeppersNews> peppersNews;
		@JsonProperty("PepperAkaList")
		@JsonAlias("pepperAkaList")
		private List<PepperAkaList> pepperAkaList;
		@JsonProperty("PepperOnList")
		@JsonAlias("pepperOnList")
		private List<PepperOnList> pepperOnList;

		@Data
		@ApiModel
		public static class PepperOnList {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "게시글 타입코드")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "타이틀")
			private String title;
			@Schema(description = "게시글 내용 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회수")
			private int hits;
			@Schema(description = "삭제여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 노출여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private int ord;
			@Schema(description = "썸네일 url")
			private String thumb;
			@Schema(description = "팝업 썸네일url")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "생성 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class PepperAkaList {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "게시글 타입코드")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "타이틀")
			private String title;
			@Schema(description = "게시글 내용 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회수")
			private int hits;
			@Schema(description = "삭제여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 노출여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private int ord;
			@Schema(description = "썸네일 url")
			private String thumb;
			@Schema(description = "팝업 썸네일url")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "생성 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}


		@Data
		@ApiModel
		public static class MainYoutubeList {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "게시글 타입코드")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "타이틀")
			private String title;
			@Schema(description = "게시글 내용 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회수")
			private int hits;
			@Schema(description = "삭제여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 노출여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private int ord;
			@Schema(description = "썸네일 url")
			private String thumb;
			@Schema(description = "팝업 썸네일url")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "생성 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}

		@Data
		@ApiModel
		public static class PeppersNews {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "게시글 타입코드")
			@JsonAlias("typeCd")
			private String type_cd;
			@Schema(description = "타이틀")
			private String title;
			@Schema(description = "게시글 내용 타입")
			@JsonAlias("contentCd")
			private String content_cd;
			@Schema(description = "게시글 내용")
			private String content;
			@Schema(description = "링크")
			private String link;
			@Schema(description = "조회수")
			private int hits;
			@Schema(description = "삭제여부")
			@JsonAlias("delYn")
			private String del_yn;
			@Schema(description = "사용여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "메인노출 여부")
			@JsonAlias("mainYn")
			private String main_yn;
			@Schema(description = "팝업 노출여부")
			@JsonAlias("popupYn")
			private String popup_yn;
			@Schema(description = "순서")
			private int ord;
			@Schema(description = "썸네일 url")
			private String thumb;
			@Schema(description = "팝업 썸네일url")
			@JsonAlias("popThumb")
			private String pop_thumb;
			@Schema(description = "생성 날짜")
			@JsonAlias("regDt")
			private String reg_dt;
		}


		@Data
		@ApiModel
		public static class NextMatchInfoView {
			private temInfoDto homeTeam;
			private awaytemInfoDto oppTeam;

			@Schema(description = "Id Value")
			private String id;
			@Schema(description = "리그 아이디")
			@JsonAlias("leagueId")
			private String league_id;
			@Schema(description = "리그 라운드 아이디")
			@JsonAlias("leagueDetailId")
			private String league_detail_id;
			@Schema(description = "홈경기 여부")
			private String gubun;
			@Schema(description = "사용여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "수훈선수 이벤트 개설 여부")
			@JsonAlias("eventYn")
			private String event_yn;
			@Schema(description = "경기 날짜")
			@JsonAlias("matchDate")
			private String match_date;
			@Schema(description = "홈팀 아이디")
			@JsonAlias("hometeamId")
			private String hometeam_id;
			@Schema(description = "원정팀 아이디")
			@JsonAlias("awayteamId")
			private String awayteam_id;
			@Schema(description = "홈팀 스코어")
			@JsonAlias("homeScore")
			private String home_score;
			@Schema(description = "홈팀 상세 스코어")
			@JsonAlias("homeScoreDetail")
			private String home_score_detail;
			@Schema(description = "원정팀 스코어")
			@JsonAlias("awayScore")
			private String away_score;
			@Schema(description = "원정팀 상세 스코어")
			@JsonAlias("awayScoreDetail")
			private String away_score_detail;
			@Schema(description = "경기장 아이디")
			@JsonAlias("stadiumId")
			private String stadium_id;
			@Schema(description = "경기 진행 상태")
			@JsonAlias("status")
			private String status;
			@Schema(description = "티켓 URL")
			@JsonAlias("ticketingUrl")
			private String ticketing_url;
			@Schema(description = "라이브방송 URL")
			@JsonAlias("liveUrl")
			private String live_url;
			@Schema(description = "????")
			@JsonAlias("seatUrl")
			private String seat_url;
			@Schema(description = "MVP 아이디")
			@JsonAlias("mvpId")
			private String mvp_id;
			@Schema(description = "작성자")
			@JsonAlias("regId")
			private int reg_id;
			@Schema(description = "작성일자")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "수정자")
			@JsonAlias("modId")
			private int mod_id;
			@Schema(description = "수정일자")
			@JsonAlias("modDt")
			private String mod_dt;
			@Schema(description = "isApply")
			private String isApply;
			@Schema(description = "리그 정보")
			@JsonAlias("leagueInfo")
			private String league_info;
			@Schema(description = "리그 상세 정보")
			@JsonAlias("leagueInfoDetail")
			private String league_info_detail;
			@Schema(description = "라운드 명")
			@JsonAlias("roundNm")
			private String round_nm;
			@Schema(description = "경기장 명")
			private String stadium;
			@Schema(description = "최종 스코어")
			@JsonAlias("finalScore")
			private String final_score ;

			@Data
			@ApiModel
			public static class temInfoDto {
				@Schema(description = "팀 아이디")
				@JsonAlias("teamId")
				private String team_id;
				@Schema(description = "지역")
				@JsonAlias("regionName")
				private String region_name;
				@Schema(description = "오너")
				private String owner;
				@Schema(description = "팀 이름")
				@JsonAlias("teamName")
				private String team_name;
				@Schema(description = "팀 배너 사진 URL")
				@JsonAlias("teamPicUrl")
				private String team_pic_url;
				@Schema(description = "팀 사진 URL")
				@JsonAlias("teamPicUrlSub")
				private String team_pic_url_sub;
			}

			@Data
			@ApiModel
			public static class awaytemInfoDto {
				@Schema(description = "팀 아이디")
				@JsonAlias("teamId")
				private String team_id;
				@Schema(description = "지역")
				@JsonAlias("regionName")
				private String region_name;
				@Schema(description = "오너")
				private String owner;
				@Schema(description = "팀 이름")
				@JsonAlias("teamName")
				private String team_name;
				@Schema(description = "팀 배너 사진 URL")
				@JsonAlias("teamPicUrl")
				private String team_pic_url;
				@Schema(description = "팀 사진 URL")
				@JsonAlias("teamPicUrlSub")
				private String team_pic_url_sub;
			}
		}

		@Data
		@ApiModel
		public static class PopupEventList {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "이벤트 베너 url")
			private String image;
			@Schema(description = "화면 경로")
			private String url;
			@Schema(description = "아이디")
			@JsonAlias("useYn")
			private String use_yn;
		}

		@Data
		@ApiModel
		public static class PepperPlayer {
			@Schema(description = "선수 아이디")
			private String	id;
			@Schema(description = "선수 이름")
			@JsonAlias("playerName")
			private String	player_name;
			@Schema(description = "선수 영문 이름")
			@JsonAlias("ePlayerName")
			private String	e_player_name;
			@Schema(description = "선수 가입 아이디")
			@JsonAlias("userId")
			private String	user_id;
			@Schema(description = "선수 사진 url")
			@JsonAlias("playerPicUrl")
			private String	player_pic_url;
			@Schema(description = "선수 프로필 이미지")
			@JsonAlias("profilePic")
			private String	profile_pic;
			@Schema(description = "선수 썸네일 이미지")
			@JsonAlias("profilePicSmall")
			private String	profile_pic_small;
			@Schema(description = "선수 블로그 URL")
			@JsonAlias("bgUrl")
			private String	bg_url;
			@Schema(description = "선수 사용 여부")
			@JsonAlias("useYn")
			private String	use_yn;
			@Schema(description = "사용자가 북마크 한 YN")
			@JsonAlias("bookMarkYn")
			private String	book_mark_yn;
			@Schema(description = "선수가 담벼락 등록 YN(2틀이내 담벼락만 표시)")
			@JsonAlias("newYn")
			private String	new_yn;
			@Schema(description = "선수 입단일")
			@JsonAlias("joinDate")
			private String join_date;
			@Schema(description = "선수 포지션")
			private String position;
			@Schema(description = "선수 쿠부 url")
			@JsonAlias("kovoUrl")
			private String kovo_url;
			@Schema(description = "선수 조회 순서")
			private String ord;
			@Schema(description = "선수 백 넘버")
			@JsonAlias("backNo")
			private String back_no;
			@Schema(description = "선수 국적")
			private String nation;
			@Schema(description = "선수 생년월일")
			@JsonAlias("birthDate")
			private String birth_date;
			@Schema(description = "선수 키")
			private String height;
			@Schema(description = "선수 무게")
			private String weight;
			@Schema(description = "선수 초등학교")
			@JsonAlias("elSchool")
			private String el_school;
			@Schema(description = "선수 중학교")
			@JsonAlias("midSchool")
			private String mid_school;
			@Schema(description = "선수 고등학교")
			@JsonAlias("highSchool")
			private String high_school;
			@Schema(description = "선수 대학")
			private String university;
		}
	}

	@Data
	@ApiModel
	public static class ResAppSliderDto {
		private String id;
		private String image;
		private String url;
		private String use_yn;
	}

	@Data
	@ApiModel
	public static class  NextMatchDto {
		@Data
		@ApiModel
		public static class NextMatchInfoView {
			private temInfoDto homeTeam;
			private awaytemInfoDto oppTeam;

			@Schema(description = "Id Value")
			private String id;
			@Schema(description = "리그 아이디")
			@JsonAlias("leagueId")
			private String league_id;
			@Schema(description = "리그 라운드 아이디")
			@JsonAlias("leagueDetailId")
			private String league_detail_id;
			@Schema(description = "홈경기 여부")
			private String gubun;
			@Schema(description = "사용여부")
			@JsonAlias("useYn")
			private String use_yn;
			@Schema(description = "수훈선수 이벤트 개설 여부")
			@JsonAlias("eventYn")
			private String event_yn;
			@Schema(description = "경기 날짜")
			@JsonAlias("matchDate")
			private String match_date;
			@Schema(description = "홈팀 아이디")
			@JsonAlias("hometeamId")
			private String hometeam_id;
			@Schema(description = "원정팀 아이디")
			@JsonAlias("awayteamId")
			private String awayteam_id;
			@Schema(description = "홈팀 스코어")
			@JsonAlias("homeScore")
			private String home_score;
			@Schema(description = "홈팀 상세 스코어")
			@JsonAlias("homeScoreDetail")
			private String home_score_detail;
			@Schema(description = "원정팀 스코어")
			@JsonAlias("awayScore")
			private String away_score;
			@Schema(description = "원정팀 상세 스코어")
			@JsonAlias("awayScoreDetail")
			private String away_score_detail;
			@Schema(description = "경기장 아이디")
			@JsonAlias("stadiumId")
			private String stadium_id;
			@Schema(description = "경기 진행 상태")
			@JsonAlias("status")
			private String status;
			@Schema(description = "티켓 URL")
			@JsonAlias("ticketingUrl")
			private String ticketing_url;
			@Schema(description = "라이브방송 URL")
			@JsonAlias("liveUrl")
			private String live_url;
			@Schema(description = "????")
			@JsonAlias("seatUrl")
			private String seat_url;
			@Schema(description = "MVP 아이디")
			@JsonAlias("mvpId")
			private String mvp_id;
			@Schema(description = "작성자")
			@JsonAlias("regId")
			private int reg_id;
			@Schema(description = "작성일자")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "수정자")
			@JsonAlias("modId")
			private int mod_id;
			@Schema(description = "수정일자")
			@JsonAlias("modDt")
			private String mod_dt;
			@Schema(description = "isApply")
			private String isApply;
			@Schema(description = "리그 정보")
			@JsonAlias("leagueInfo")
			private String league_info;
			@Schema(description = "리그 상세 정보")
			@JsonAlias("leagueInfoDetail")
			private String league_info_detail;
			@Schema(description = "라운드 명")
			@JsonAlias("roundNm")
			private String round_nm;
			@Schema(description = "경기장 명")
			private String stadium;
			@Schema(description = "최종 스코어")
			@JsonAlias("finalScore")
			private String final_score ;

			@Data
			@ApiModel
			public static class temInfoDto {
				@Schema(description = "팀 아이디")
				@JsonAlias("teamId")
				private String team_id;
				@Schema(description = "지역")
				@JsonAlias("regionName")
				private String region_name;
				@Schema(description = "오너")
				private String owner;
				@Schema(description = "팀 이름")
				@JsonAlias("teamName")
				private String team_name;
				@Schema(description = "팀 배너 사진 URL")
				@JsonAlias("teamPicUrl")
				private String team_pic_url;
				@Schema(description = "팀 사진 URL")
				@JsonAlias("teamPicUrlSub")
				private String team_pic_url_sub;
			}

			@Data
			@ApiModel
			public static class awaytemInfoDto {
				@Schema(description = "팀 아이디")
				@JsonAlias("teamId")
				private String team_id;
				@Schema(description = "지역")
				@JsonAlias("regionName")
				private String region_name;
				@Schema(description = "오너")
				private String owner;
				@Schema(description = "팀 이름")
				@JsonAlias("teamName")
				private String team_name;
				@Schema(description = "팀 배너 사진 URL")
				@JsonAlias("teamPicUrl")
				private String team_pic_url;
				@Schema(description = "팀 사진 URL")
				@JsonAlias("teamPicUrlSub")
				private String team_pic_url_sub;
			}
		}
	}

	@Data
	@ApiModel
	public static class ResUIMNIF005Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "게시판 타입코드")
		@JsonAlias("typeCd")
		private String type_cd;
		@Schema(description = "제목")
		private String title;
		@Schema(description = "내용")
		private String content;
		@Schema(description = "링크")
		private String link;
		@Schema(description = "팝업 썸네일")
		@JsonAlias("popThumb")
		private String pop_thumb;

	}
}
