package kr.aipeppers.pep.ui.category.controller;

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
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResPageDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.category.component.AipeppersComponent;
import kr.aipeppers.pep.ui.mypage.controller.MyinfoController;
import kr.aipeppers.pep.ui.mypage.controller.MyinfoController.ResUIMPMI021Dto.User;
import kr.aipeppers.pep.ui.mypage.controller.MyinfoController.ResUIMPMI021Dto.Video;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/restapi/category/aipeppers")
@Api(tags = {"카테고리>AI PEPPERS"}, description = "AipeppersController")
public class AipeppersController {

	@Autowired
	protected AipeppersComponent aipeppersComp;

	/**
	 * @Method Name : playerList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="선수 소개")
	@PostMapping(value = "playerList")
	public ResListDto<ResUICAAP001Dto> playerList(@RequestBody ReqUICAAP001Dto reqDto) throws Exception {
		//return new ResListDto<ResUICAAP001Dto>(BeanUtil.convertList(dao.selectList("category.aipeppers.playerList",BoxUtil.toBox(box)), ResUICAAP001Dto.class));
		return new ResListDto<ResUICAAP001Dto>(BeanUtil.convertList(aipeppersComp.playerList(BoxUtil.toBox(reqDto)), ResUICAAP001Dto.class));
	}

	/**
	 * @Method Name : playerDtail
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="선수 상세 정보 조회")
	@PostMapping(value = "playerDtail")
	public ResResultDto<ResUICAAP009Dto> playerDtail(@RequestBody ReqUICAAP009Dto reqDto) throws Exception {
		//return new ResListDto<ResUICAAP001Dto>(BeanUtil.convertList(dao.selectList("category.aipeppers.playerList",BoxUtil.toBox(box)), ResUICAAP001Dto.class));
		return new ResResultDto<ResUICAAP009Dto>(BeanUtil.convert(aipeppersComp.playerDetailView(BoxUtil.toBox(reqDto)), ResUICAAP009Dto.class));
	}

	/**
	 * @Method Name : getMerchandiseList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="구단굿즈")
	@PostMapping(value = "getMerchandiseList")
	public ResListDto<ResUICAAP002Dto> getMerchandiseList(@RequestBody ReqUICAAP002Dto reqDto) throws Exception {
		return new ResListDto<ResUICAAP002Dto>(BeanUtil.convertList(aipeppersComp.merchandiseList(BoxUtil.toBox(reqDto)), ResUICAAP002Dto.class));
	}

	/**
	 * @Method Name : getScheduleList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="경기일정")
	@PostMapping(value = "getScheduleList")
	public ResListDto<ResUICAAP003Dto> getScheduleList(@RequestBody ReqUICAAP003Dto reqDto) throws Exception {
		return new ResListDto<ResUICAAP003Dto>(BeanUtil.convertList(aipeppersComp.leagueScheduleList(BoxUtil.toBox(reqDto)), ResUICAAP003Dto.class));
	}

	/**
	 * @Method Name : getScheduleDetail
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="경기일정 상세내용")
	@PostMapping(value = "getScheduleDetail")
	public ResResultDto<ResUICAAP010Dto> getScheduleDetail(@RequestBody ReqUICAAP010Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAAP010Dto>(BeanUtil.convert(aipeppersComp.getScheduleDetail(BoxUtil.toBox(reqDto)), ResUICAAP010Dto.class));
	}

	/**
	 * @Method Name : nextMatchBanner
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="")
	@PostMapping(value = "nextMatchBanner")
	public ResResultDto<ResUICAAP011Dto> nextMatchBanner(@RequestBody ReqUICAAP011Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAAP011Dto>(BeanUtil.convert(aipeppersComp.nextMatchBanner(BoxUtil.toBox(reqDto)), ResUICAAP011Dto.class));
	}


	/**
	 * @Method Name : getVoteMatch
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="")
	@PostMapping(value = "getVoteMatch")
	public ResResultDto<ResUICAAP012Dto> getVoteMatch(@RequestBody ReqUICAAP012Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAAP012Dto>(BeanUtil.convert(aipeppersComp.getVoteMatch(BoxUtil.toBox(reqDto)), ResUICAAP012Dto.class));
	}

	/**
	 * @Method Name : searchNoticeList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="공지사항")
	@PostMapping(value = "searchNoticeList")
	public ResListDto<ResUICAAP004Dto> searchNoticeList(@RequestBody ReqUICAAP004Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResUICAAP004Dto>(BeanUtil.convertList(aipeppersComp.noticeList(box), ResUICAAP004Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : searchNoticeDetail
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="공지사항 상세내용")
	@PostMapping(value = "searchNoticeDetail")
	public ResResultDto<ResUICAAP005Dto> searchNoticeDetail(@RequestBody ReqUICAAP005Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAAP005Dto>(BeanUtil.convert(aipeppersComp.noticeDetailView(BoxUtil.toBox(reqDto)), ResUICAAP005Dto.class));
	}

	@ApiOperation(value="리그 정보")
	@PostMapping(value = "getLeagues")
	public ResListDto<ResUICAAP006Dto> getLeagues(@RequestBody ReqUICAAP006Dto reqDto) throws Exception {
		return new ResListDto<ResUICAAP006Dto>(BeanUtil.convertList(aipeppersComp.leaguesList(BoxUtil.toBox(reqDto)), ResUICAAP006Dto.class));
	}

	/**
	 * @Method Name : eventList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="이벤트 리스트")
	@PostMapping(value = "eventList")
	public ResListDto<ResUICAAP007Dto> eventList(@RequestBody ReqUICAAP007Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResUICAAP007Dto>(BeanUtil.convertList(aipeppersComp.eventList(box), ResUICAAP007Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	@Data
	@ApiModel
	public static class ReqUICAAP001Dto {
		@Schema(description = "포지션 타입", example="OH")
		private String type;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP002Dto {
		@Schema(description = "포지션 타입", example="ALL")
		private String type;
		@Schema(description = "페이지", example="1")
		private int page;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP003Dto {
//		@Schema(description = "포지션 타입", example="ALL")
//		private String type;
//		@Schema(description = "페이지", example="0")
//		private int page;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP004Dto {
//		@Schema(description = "포지션 타입", example="ALL")
//		private String type;
		@Schema(description = "페이지", example="0")
		private int page;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP005Dto {
		@Schema(description = "게시판 아이디", example="\"132\"")
		private String id;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP006Dto {

	}

	@Data
	@ApiModel
	public static class ReqUICAAP007Dto {
		@Schema(description = "페이지", example="0")
		private int page;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP009Dto {
		@Schema(description = "선수 아이디", example="\"24\"")
		@JsonAlias("playerId")
		private String player_id;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP010Dto {
		@Schema(description = "경기 아이디", example="\"72\"")
		private String id;
	}

	@Data
	@ApiModel
	public static class ReqUICAAP011Dto {
	}

	@Data
	@ApiModel
	public static class ReqUICAAP012Dto {
		@Schema(description = "경기 아이디", example="\"72\"")
		@JsonAlias("matchId")
		private String match_id;
	}


	@Data
	@ApiModel
	public static class ResUICAAP001Dto {
		@Schema(description = "pk value")
		private String id;
		@Schema(description = "선수 이름")
		@JsonAlias("playerName")
		private String player_name;
		@Schema(description = "선수 영문 이름")
		@JsonAlias("ePlayerName")
		private String e_player_name;
		@Schema(description = "선수의 회원 아이디")
		@JsonAlias("userId")
		private int user_id;
		@Schema(description = "선수 사진 URL")
		@JsonAlias("playerPicUrl")
		private String player_pic_url;
		@Schema(description = "선수 블로그 URL")
		@JsonAlias("bgUrl")
		private String bg_url;
		@Schema(description = "입단 년도")
		@JsonAlias("joinDate")
		private String join_date;
		@Schema(description = "선수 포지션 ('Outsider Hitter', 'Opposite', 'Middle Blocker', 'Setter', 'Libero' ")
		private String position;
		@Schema(description = "포지션 명")
		@JsonAlias("positionName")
		private String position_name;
		@Schema(description = "선수정보 KOVO 외부 링크")
		@JsonAlias("kovoUrl")
		private String kovo_url;
		@Schema(description = "노출 순번")
		private int ord;
		@Schema(description = "선수 번호")
		@JsonAlias("backNo")
		private int back_no;
		@Schema(description = "선수 국적")
		private String nation;
		@Schema(description = "생년월일")
		@JsonAlias("birthDate")
		private String birth_date;
		@Schema(description = "신장")
		private int height;
		@Schema(description = "무게")
		private int weight;
		@Schema(description = "초등학교")
		@JsonAlias("elSchool")
		private String el_school;
		@Schema(description = "중학교")
		@JsonAlias("midSchool")
		private String mid_school;
		@Schema(description = "고등학교")
		@JsonAlias("highSchool")
		private String high_school;
		@Schema(description = "대학교")
		private String university;
		@Schema(description = "사용여부")
		@JsonAlias("useYn")
		private String use_yn;
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
	}

	@Data
	@ApiModel
	public static class ResUICAAP002Dto {
		@Schema(description = "굿즈 PK Values")
		private String id;
		@Schema(description = "타입코드")
		@JsonAlias("typeCd")
		private String type_cd;
		@Schema(description = "제목")
		private String title;
		@Schema(description = "내용")
		private String content;
		@Schema(description = "0~100%")
		@JsonAlias("discountRate")
		private int discount_rate;
		@Schema(description = "가격")
		private int price;
		@Schema(description = "상품경로")
		@JsonAlias("playerName")
		private String gift_url;
		@Schema(description = "")
		private String image;
		@Schema(description = "상품경로1")
		private String image1;
		@Schema(description = "상품경로2")
		private String image2;
		@Schema(description = "상품경로3")
		private String image3;
		@Schema(description = "상품경로4")
		private String image4;
		@Schema(description = "상품경로5")
		private String image5;
		@Schema(description = "사용연령코드")
		@JsonAlias("availableCd")
		private String available_cd;
		@Schema(description = "삭제여부")
		@JsonAlias("delYn")
		private String del_yn;
		@Schema(description = "활성화여부")
		@JsonAlias("playerName")
		private String use_yn;
		@Schema(description = "노출순서")
		private int ord;
		@Schema(description = "생성일시")
		@JsonAlias("regDt")
		private String reg_dt;
		@Schema(description = "생성자")
		@JsonAlias("regId")
		private int reg_id;
		@Schema(description = "수정일시")
		@JsonAlias("modDt")
		private String mod_dt;
		@Schema(description = "수정자")
		@JsonAlias("modId")
		private int mod_id;
		@Schema(description = "타입명")
		@JsonAlias("typeNm")
		private String type_nm;
		@Schema(description = "사용연령명")
		@JsonAlias("availableNm")
		private String available_nm;
	}

	@Data
	@ApiModel
	public static class ResUICAAP003Dto {
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
	public static class ResUICAAP004Dto {
		@Schema(description = "공지사항 아이디")
		private String id;
		@Schema(description = "공지사항 타이틀")
		private String title;
		@Schema(description = "게시판 구분 코드" )
		@JsonAlias("contentCd")
		private String content_cd;
		@Schema(description = "게시반 구분 내용")
		@JsonAlias("contentNm")
		private String content_nm;
		@Schema(description = "생성 날짜")
		@JsonAlias("regDtAt")
		private String reg_dt;
	}

	@Data
	@ApiModel
	public static class ResUICAAP005Dto {
		@Schema(description = "공지사항 아이디")
		private String id;
		@Schema(description = "공지사항 타이틀")
		private String title;
		@Schema(description = "상세 내용")
		private String content;
		@Schema(description = "게시판 구분 코드" )
		@JsonAlias("contentCd")
		private String content_cd;
		@Schema(description = "조회수" )
		private String hit;
		@Schema(description = "생성 날짜")
		@JsonAlias("regDt")
		private String reg_dt;

	}

	@Data
	@ApiModel
	public static class ResUICAAP006Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "코드 아이디")
		@JsonAlias("cdId")
		private String cd_id;
		@Schema(description = "코드명")
		@JsonAlias("cdNm")
		private String cd_nm;
		@Schema(description = "코드 설명" )
		@JsonAlias("cdDesc")
		private String cd_desc;
		@Schema(description = "그룹코드 명" )
		@JsonAlias("upCdId")
		private String up_cd_id;
		@Schema(description = "코드순서")
		private String ord;

	}

	@Data
	@ApiModel
	public static class ResUICAAP007Dto {
		@Schema(description = "이벤트 아이디")
		private String id;
		@Schema(description = "이벤트 타이틀")
		private String title;
		@Schema(description = "이벤트 내용")
		private String content;
		@Schema(description = "게시판 구분 코드" )
		@JsonAlias("contentCd")
		private String content_cd;
		@Schema(description = "이벤트 링크")
		private String link;
		@Schema(description = "썸네일 이미지" )
		private String thumb;
		@Schema(description = "썸네일 이미지" )
		@JsonAlias("popThumb")
		private String pop_thumb;
		@Schema(description = "순서" )
		private String ord;
		@Schema(description = "생성 날짜")
		@JsonAlias("regDt")
		private String reg_dt;
	}

	@Data
	@ApiModel
	public static class ResUICAAP009Dto {
		@Schema(description = "선수 아이디")
		private String id;
		@Schema(description = "선수 이름")
		@JsonAlias("playerName")
		private String player_name;
		@Schema(description = "선수 영어 이름")
		@JsonAlias("ePlayerName")
		private String e_player_name;
		@Schema(description = "선수 회원 아이디")
		@JsonAlias("userId")
		private String user_id;
		@Schema(description = "선수 이미지 URL")
		@JsonAlias("playerPicUrl")
		private String player_pic_url;
		@Schema(description = "선수 블로그 이미지 URL")
		@JsonAlias("bgUrl")
		private String bg_url;
		@Schema(description = "선수 입단 날짜")
		@JsonAlias("joinDate")
		private String join_date;
		@Schema(description = "선수 포지션")
		private String position;
		@Schema(description = "kovoUrl")
		@JsonAlias("kovoUrl")
		private String kovo_url;
		@Schema(description = "정렬 순서")
		private String ord;
		@Schema(description = "선수 백넘버")
		@JsonAlias("backNo")
		private String back_no;
		@Schema(description = "국적")
		private String nation;
		@Schema(description = "생년 월일")
		@JsonAlias("birthDate")
		private String birth_date;
		@Schema(description = "선수 키")
		private String height;
		@Schema(description = "선수 무게")
		private String weight;
		@Schema(description = "선수- 초등학교")
		@JsonAlias("elSchool")
		private String el_school;
		@Schema(description = "선수 - 중학교")
		@JsonAlias("midSchool")
		private String mid_school;
		@Schema(description = "선수- 고등학교")
		@JsonAlias("highSchool")
		private String high_school;
		@Schema(description = "선수 - 대학교")
		private String university;
	}

	@Data
	@ApiModel
	public static class ResUICAAP010Dto {
		@JsonProperty("MatchSchedule")
		@JsonAlias("matchSchedule")
		private MatchSchedule matchSchedule;
		@JsonProperty("HomeTeam")
		@JsonAlias("homeTeam")
		private HomeTeam homeTeam;
		@JsonProperty("AwayTeam")
		@JsonAlias("awayTeam")
		private AwayTeam awayTeam;


		@Data
		@ApiModel
		public static class MatchSchedule {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "")
			private String gubun;
			@Schema(description = "")
			@JsonAlias("matchDate")
			private String match_date;
			@Schema(description = "")
			@JsonAlias("stadiumNm")
			private String stadium_nm;

		}


		@Data
		@ApiModel
		public static class HomeTeam {
			@Schema(description = "팀 아이디")
			@JsonAlias("teamId")
			private String team_id;
			@Schema(description = "오너")
			private String owner;
			@Schema(description = "팀 이름")
			@JsonAlias("teamName")
			private String team_name;
			@Schema(description = "팀 이름 영문")
			@JsonAlias("eTeamName")
			private String e_team_name;
			@Schema(description = "팀 배너 사진 URL")
			@JsonAlias("teamPicUrl")
			private String team_pic_url;
			@Schema(description = "팀 사진 URL")
			@JsonAlias("teamPicUrlSub")
			private String team_pic_url_sub;
		}

		@Data
		@ApiModel
		public static class AwayTeam {
			@Schema(description = "팀 아이디")
			@JsonAlias("teamId")
			private String team_id;
			@Schema(description = "오너")
			private String owner;
			@Schema(description = "팀 이름")
			@JsonAlias("teamName")
			private String team_name;
			@Schema(description = "팀 이름 영문")
			@JsonAlias("eTeamName")
			private String e_team_name;
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
	public static class ResUICAAP011Dto {
		private HomeTeam homeTeam;
		private OppTeam oppTeam;

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
		@Schema(description = "원정팀 스코어")
		@JsonAlias("awayScore")
		private String away_score;
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
		@Schema(description = "라운드 명")
		@JsonAlias("roundNm")
		private String round_nm;
		@Schema(description = "경기장 명")
		private String stadium;
		@Schema(description = "")
		@JsonAlias("homeWin")
		private String home_win;
		@Schema(description = "")
		@JsonAlias("finalScore")
		private String final_score;

		@Data
		@ApiModel
		public static class HomeTeam {
			@Schema(description = "팀 아이디")
			@JsonAlias("teamId")
			private String team_id;
			@Schema(description = "오너")
			private String owner;
			@Schema(description = "팀 이름")
			@JsonAlias("teamName")
			private String team_name;
			@Schema(description = "지역")
			@JsonAlias("regionName")
			private String region_name;
			@Schema(description = "팀 배너 사진 URL")
			@JsonAlias("teamPicUrl")
			private String team_pic_url;
		}

		@Data
		@ApiModel
		public static class OppTeam {
			@Schema(description = "팀 아이디")
			@JsonAlias("teamId")
			private String team_id;
			@Schema(description = "오너")
			private String owner;
			@Schema(description = "팀 이름")
			@JsonAlias("teamName")
			private String team_name;
			@Schema(description = "지역")
			@JsonAlias("regionName")
			private String region_name;
			@Schema(description = "팀 배너 사진 URL")
			@JsonAlias("teamPicUrl")
			private String team_pic_url;
		}
	}


	@Data
	@ApiModel
	public static class ResUICAAP012Dto {
		@JsonProperty("MatchSchedule")
		@JsonAlias("matchSchedule")
		private MatchSchedule matchSchedule;
		@JsonProperty("Home")
		@JsonAlias("home")
		private Home home;
		@JsonProperty("Away")
		@JsonAlias("away")
		private Away away;

		@Data
		@ApiModel
		public static class MatchSchedule {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "")
			private String gubun;
			@Schema(description = "")
			@JsonAlias("matchDate")
			private String match_date;
			@Schema(description = "홈팀 아이디")
			@JsonAlias("hometeamId")
			private String hometeam_id;
			@Schema(description = "원정팀 아이디")
			@JsonAlias("awayteamId")
			private String awayteam_id;
			@Schema(description = "")
			@JsonAlias("stadiumId")
			private String stadium_id;

		}

		@Schema(description = "")
		private String stadium;


		@Data
		@ApiModel
		public static class Home {
			@JsonProperty("HomeTeam")
			@JsonAlias("homeTeam")
			private HomeTeam homeTeam;

			@Data
			@ApiModel
			public static class HomeTeam {

				@Schema(description = "팀 아이디")
				@JsonAlias("teamId")
				private String team_id;
				@Schema(description = "오너")
				private String owner;
				@Schema(description = "팀 이름")
				@JsonAlias("teamName")
				private String team_name;
				@Schema(description = "지역")
				@JsonAlias("regionName")
				private String region_name;
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
		public static class Away {
			@JsonProperty("OppTeam")
			@JsonAlias("oppTeam")
			private OppTeam oppTeam;

			@Data
			@ApiModel
			public static class OppTeam {

				@Schema(description = "팀 아이디")
				@JsonAlias("teamId")
				private String team_id;
				@Schema(description = "오너")
				private String owner;
				@Schema(description = "팀 이름")
				@JsonAlias("teamName")
				private String team_name;
				@Schema(description = "지역")
				@JsonAlias("regionName")
				private String region_name;
				@Schema(description = "팀 배너 사진 URL")
				@JsonAlias("teamPicUrl")
				private String team_pic_url;
				@Schema(description = "팀 사진 URL")
				@JsonAlias("teamPicUrlSub")
				private String team_pic_url_sub;
			}
		}
	}

}
