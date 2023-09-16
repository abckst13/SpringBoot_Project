package kr.aipeppers.pep.ui.category.controller;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
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
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResPageDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.category.component.EventComponent;
import kr.aipeppers.pep.ui.category.controller.AipeppersController.ResUICAAP010Dto.AwayTeam;
import kr.aipeppers.pep.ui.category.controller.AipeppersController.ResUICAAP010Dto.HomeTeam;
import kr.aipeppers.pep.ui.category.controller.AipeppersController.ResUICAAP010Dto.MatchSchedule;
import kr.aipeppers.pep.ui.category.service.EventService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@RestController
@RequestMapping("/restapi/category/event")
@Api(tags = {"카테고리>이벤트"}, description = "EventController")
public class EventController {

	@Autowired
	protected EventComponent eventComp;

	@Autowired
	protected EventService eventService;

	@Autowired
	protected SqlSessionTemplate dao;

	/**
	 * @Method Name : luckydrawList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="럭키드로우 조회")
	@PostMapping(value="luckydrawList")
	public ResListDto<ResUICAET001Dto> luckydrawList(@RequestBody ReqUICAET001Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResUICAET001Dto>(BeanUtil.convertList(eventComp.eventLuckyDrawView(box), ResUICAET001Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * @Method Name : luckydrawDetail
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="럭키드로우 상세 조회")
	@PostMapping(value="luckydrawDetail")
	public ResResultDto<ResUICAET002Dto> luckydrawDetail(@RequestBody ReqUICAET002Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET002Dto>(BeanUtil.convert(eventComp.luckydrawDetailView(BoxUtil.toBox(reqDto)), ResUICAET002Dto.class));//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	/**
	 * @Method Name : luckydrawApply
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="럭키드로우 응모하기")
	@PostMapping(value="luckydrawApply")
	@ReqInfo(validForm = "event.luckydrawInsert")
	public ResResultDto<ResUICAET003Dto> luckydrawApply(@RequestBody ReqUICAET003Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET003Dto>(BeanUtil.convert(eventComp.luckydrawInsert(BoxUtil.toBox(reqDto)), ResUICAET003Dto.class));//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	/**
	 * @Method Name : defaultAttendanceCheck
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="출석체크 defult 조회")
	@PostMapping(value="defaultAttendanceCheck")
	public ResListDto<ResUICAET005Dto> defaultAttendanceCheck(@RequestBody ReqUICAET005Dto reqDto) throws Exception {
		return new ResListDto<ResUICAET005Dto>(BeanUtil.convertList(eventComp.defaultAttendList(BoxUtil.toBox(reqDto)), ResUICAET005Dto.class));//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	/**
	 * @Method Name : setAttendanceCheck
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="출석체크 클릭 시 상품 등록")
	@PostMapping(value="setAttendanceCheck")
	@ReqInfo(validForm = "event.attendChkInsert")
	public ResResultDto<ResUICAET006Dto> setAttendanceCheck(@RequestBody ReqUICAET006Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET006Dto>(BeanUtil.convert(eventComp.attendChkInsert(BoxUtil.toBox(reqDto)), ResUICAET006Dto.class));//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	/**
	 * @Method Name : setAttendanceCheck
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="출석체크 수령인 정보 입력")
	@PostMapping(value="setAttendanceRecipient")
	@ReqInfo(validForm = "event.attendanceRecipientInsert")
	public ResResultDto<ResUICAET009Dto> setAttendanceRecipient(@RequestBody ReqUICAET009Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET009Dto>(BeanUtil.convert(eventComp.attendanceRecipientInsert(BoxUtil.toBox(reqDto)), ResUICAET009Dto.class), "I214");//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	/**
	 * @Method Name : setAttendanceCheck
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="")
	@PostMapping(value="isMyVote")
	public ResResultDto<ResUICAET010Dto> isMyVote(@RequestBody ReqUICAET010Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET010Dto>(BeanUtil.convert(eventComp.isMyVote(BoxUtil.toBox(reqDto)), ResUICAET010Dto.class), "I232");
	}


	@ApiOperation(value="럭키드로우 신청 여부")
	@PostMapping(value="checkLuckydrawApply")
	@ReqInfo(validForm = "event.luckydrawChk")
	public ResResultDto<ResUICAET007Dto> checkLuckydrawApply(@RequestBody ReqUICAET007Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET007Dto>(BeanUtil.convert(eventComp.luckydrawChk(BoxUtil.toBox(reqDto)), ResUICAET007Dto.class));//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	@ApiOperation(value="럭키드로우 당첨자")
	@PostMapping(value="showWinner")
	@ReqInfo(validForm = "event.showWinner")
	public ResListDto<ResUICAET004Dto> showWinner(@RequestBody ReqUICAET004Dto reqDto) throws Exception {
		return new ResListDto<ResUICAET004Dto>(BeanUtil.convertList(eventComp.luckydrawWinView(BoxUtil.toBox(reqDto)), ResUICAET004Dto.class));//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	/**
	 * @Method Name : setAttendanceCheck
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="수훈 선수이벤트 정보입력")
	@PostMapping(value="setMvpEventInfo")
	@ReqInfo(validForm = "event.mvpEventInfoInsert")
	public ResResultDto<ResUICAET011Dto> setMvpEventInfo(@RequestBody ReqUICAET011Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET011Dto>(BeanUtil.convert(eventComp.mvpEventInfoInsert(BoxUtil.toBox(reqDto)), ResUICAET011Dto.class), "I225");//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}
	/**
	 * @Method Name : postMatchVictory
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="승부예측 참가하기")
	@PostMapping(value="postMatchVictory")
	@ReqInfo(validForm = "event.postMatchVictoryInsert")
	public ResResultDto<ResUICAET012Dto> postMatchVictory(@RequestBody ReqUICAET012Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET012Dto>(BeanUtil.convert(eventComp.postMatchVictoryInsert(BoxUtil.toBox(reqDto)), ResUICAET012Dto.class), "I233");//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}
	/**
	 * @Method Name : getMatchVictory
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="승부예측 확인하기")
	@PostMapping(value="getMatchVictory")
	public ResResultDto<ResUICAET013Dto> getMatchVictory(@RequestBody ReqUICAET013Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET013Dto>(BeanUtil.convert(eventComp.getMatchVictory(BoxUtil.toBox(reqDto)), ResUICAET013Dto.class));//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}

	/**
	 * @Method Name : matchVictoryInsertInfo
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="")
	@PostMapping(value="matchVictoryInsertInfo")
	@ReqInfo(validForm = "event.matchVictoryInsertInfo")
	public ResResultDto<ResUICAET014Dto> matchVictoryInsertInfo(@RequestBody ReqUICAET014Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET014Dto>(BeanUtil.convert(eventComp.matchVictoryInsertInfo(BoxUtil.toBox(reqDto)), ResUICAET014Dto.class), "I214");//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}
	/**
	 * @Method Name : checkMatchVictoryApply
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="")
	@PostMapping(value="checkMatchVictoryApply")
	@ReqInfo(validForm = "event.checkMatchVictoryApply")
	public ResResultDto<ResUICAET015Dto> checkMatchVictoryApply(@RequestBody ReqUICAET015Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAET015Dto>(BeanUtil.convert(eventComp.checkMatchVictoryApply(BoxUtil.toBox(reqDto)), ResUICAET015Dto.class), "E234");//파일 테이블 바뀌면 list 로 파일 데이터 받기.
	}


	@Data
	@ApiModel
	public static class ReqUICAET001Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "타입(on/close/pre)", example = "close")
		private String type;
	}

	@Data
	@ApiModel
	public static class ReqUICAET002Dto {
		@Schema(description = "이벤트 아이디", example = "\"7\"")
		private String id;
	}

	@Data
	@ApiModel
	public static class ReqUICAET003Dto {
		@Schema(description = "이벤트 아이디", example = "\"7\"")
		private String id;
		@Schema(description = "응모자 이름", example = "김성태")
		private String name;
		@Schema(description = "응모자 핸드폰", example = "01034503017")
		private int phone; // 핸드폰 정보 암호화 필요
		@Schema(description = "개인정보 동의", example = "Y")
		private String isAgree;
	}

	@Data
	@ApiModel
	public static class ReqUICAET004Dto {
		@Schema(description = "럭키드로우 아이디", example = "\"2\"")
		private String event_id;
	}

	@Data
	@ApiModel
	public static class ReqUICAET005Dto {

	}

	@Data
	@ApiModel
	public static class ReqUICAET006Dto {
		@Schema(description = "클릭 날짜", example = "2023-02-22")
		private String date;
	}

	@Data
	@ApiModel
	public static class ReqUICAET007Dto {
		@Schema(description = "럭키드로우 아이디", example = "\"7\"")
		private String id;
	}

	@Data
	@ApiModel
	public static class ReqUICAET009Dto {
		@Schema(description = "이벤트 아이디", example = "\"7\"")
		private String attendId;
		@Schema(description = "응모자 이름", example = "김성태")
		private String name;
		@Schema(description = "응모자 핸드폰", example = "01034503017")
		private int phone; // 핸드폰 정보 암호화 필요
		@Schema(description = "개인정보 동의", example = "Y")
		private String isAgree;
	}

	@Data
	@ApiModel
	public static class ReqUICAET010Dto {
		@Schema(description = "", example = "\"72\"")
		@JsonAlias("matchId")
		private String match_id;
	}

	@Data
	@ApiModel
	public static class ReqUICAET011Dto {
		@Schema(description = "경기 아이디", example = "\"7\"")
		private String matchId;
		@Schema(description = "응모자 이름", example = "김성태")
		private String name;
		@Schema(description = "응모자 핸드폰", example = "01034503017")
		private int phone;
		@Schema(description = "개인정보 동의", example = "Y")
		private String agree;
	}
	@Data
	@ApiModel
	public static class ReqUICAET012Dto {
		@Schema(description = "경기 아이디", example = "\"72\"")
		private String matchId;
		@Schema(description = "홈팀 총 스코어", example = "3")
		private String homeScore;
		@Schema(description = "원정팀 총 스코어", example = "0")
		private String awayScore;
		@Schema(description = "홈팀 예측 스코어", example = "25,25,25,0,0")
		private String our;
		@Schema(description = "원정팀 예측 스코어", example = "1,1,1,0,0")
		private String opp;
	}

	@Data
	@ApiModel
	public static class ReqUICAET013Dto {
		@Schema(description = "", example = "\"66\"")
		@JsonAlias("matchId")
		private String match_id;
	}

	@Data
	@ApiModel
	public static class ReqUICAET014Dto {
		@Schema(description = "경기 아이디", example = "\"7\"")
		private String matchId;
		@Schema(description = "빅토리 아이디", example = "\"7\"")
		private String victoryId;
		@Schema(description = "응모자 이름", example = "김성태")
		private String name;
		@Schema(description = "응모자 핸드폰", example = "01034503017")
		private int phone;
		@Schema(description = "개인정보 동의", example = "Y")
		private String agree;
	}
	@Data
	@ApiModel
	public static class ReqUICAET015Dto {
		@Schema(description = "경기 아이디", example = "\"7\"")
		private String matchId;
		@Schema(description = "빅토리 아이디", example = "\"7\"")
		private String victoryId;
		@Schema(description = "응모자 아이디", example = "김성태")
		private String userId;
	}

	@Data
	@ApiModel
	public static class ResUICAET001Dto {
		@Schema(description = "럭키드로우 키")
		private String id;
		@Schema(description = "이벤트 제목")
		@JsonAlias("eventName")
		private String event_name;
		@Schema(description = "이벤트 시작일자")
		@JsonAlias("startDt")
		private String start_dt;
		@Schema(description = "이벤트 종료일자")
		@JsonAlias("endDt")
		private String end_dt;
		@Schema(description = "당첨상품ID")
		@JsonAlias("winnerGoodsId")
		private String winner_goods_id;
		@Schema(description = "조회수")
		private String hits;
		@Schema(description = "당첨자발표일")
		@JsonAlias("winnerDt")
		private String winner_dt;
		@Schema(description = "당첨인원")
		@JsonAlias("winnerNum")
		private String winner_num;
		@Schema(description = "당첨 결과 완료 여부")
		@JsonAlias("winnerResultYn")
		private String winner_result_yn;
		@Schema(description = "파일경로")
		@JsonAlias("filePath")
		private String file_path;
		@Schema(description = "파일경로2")
		@JsonAlias("filePath2")
		private String file_path2;
		@Schema(description = "파일경로3")
		@JsonAlias("filePath3")
		private String file_path3;
		@Schema(description = "파일경로4")
		@JsonAlias("filePath4")
		private String file_path4;
		@Schema(description = "파일경로5")
		@JsonAlias("filePath5")
		private String file_path5;
		@Schema(description = "파일명")
		@JsonAlias("fileName")
		private String file_name;
		@Schema(description = "사용 여부")
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
		@Schema(description = "순서")
		private String ord;
		@Schema(description = "삭제 여부")
		@JsonAlias("delYn")
		private String del_yn;
		@Schema(description = "순번")
		private String rnum;
	}

	@Data
	@ApiModel
	public static class ResUICAET002Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "이벤트 이름")
		@JsonAlias("eventName")
		private String event_name;
		@Schema(description = "이벤트 시작일")
		@JsonAlias("startDt")
		private String start_dt;
		@Schema(description = "이벤트 종료일")
		@JsonAlias("endDt")
		private String end_dt;
		@Schema(description = "당첨상품ID")
		@JsonAlias("winnerGoodsId")
		private String winner_goods_id;
		@Schema(description = "조회수")
		private String hits;
		@Schema(description = "당첨자 발표일")
		@JsonAlias("winnerDt")
		private String winner_dt;
		@Schema(description = "당첨인원")
		@JsonAlias("winnerNum")
		private String winner_num;
		@Schema(description = "당첨결과 완료여부")
		@JsonAlias("winnerResultYn")
		private String winner_result_yn;
		@Schema(description = "파일경로")
		@JsonAlias("filePath")
		private String file_path;
		@Schema(description = "파일경로 2")
		@JsonAlias("filePath2")
		private String file_path2;
		@Schema(description = "파일경로 3")
		@JsonAlias("filePath3")
		private String file_path3;
		@Schema(description = "파일경로 4")
		@JsonAlias("filePath4")
		private String file_path4;
		@Schema(description = "파일 경로 5")
		@JsonAlias("filePath5")
		private String file_path5;
		@Schema(description = "파일 명")
		@JsonAlias("fileName")
		private String file_name;
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
		@Schema(description = "조회 순서")
		private String ord;
		@Schema(description = "삭제 여부")
		@JsonAlias("delYn")
		private String del_yn;
	}

	@Data
	@ApiModel
	public static class ResUICAET003Dto {
		@Schema(description = "결과 메세지( 성공 - 응모가완료 되었습니다. , 실패 - 이미 응모하신 럭키드로우 입니다.", example = "응모가 완료 되었습니다.")
		private String msg;
	}

	@Data
	@ApiModel
	public static class ResUICAET004Dto {
		@Schema(description = "당첨자 명")
		@JsonAlias("applyUserId")
		private String apply_user_id;
		@Schema(description = "당첨자 이메일")
		@JsonAlias("applyEmail")
		private String apply_email;
		@Schema(description = "당첨자 이름")
		private String name;
	}

	@Data
	@ApiModel
	public static class ResUICAET005Dto {
		@Schema(description = "아이디 값")
		private String id;
		@Schema(description = "출석 날짜")
		private String date;
		@Schema(description = "해당 일의 상품 타입")
		@JsonAlias("attendProductType")
		private String attend_product_type;
		@Schema(description = "해당 일 상품명")
		@JsonAlias("attendProductNm")
		private String attend_product_nm;
		@Schema(description = "해당 일의 포인트")
		@JsonAlias("attendPoint")
		private String attend_point;
		@Schema(description = "해당일 체크 여부")
		@JsonAlias("attendCheck")
		private String attend_check;
		@Schema(description = "삭제 여부")
		@JsonAlias("attendGiftOpen")
		private String attend_gift_open;
	}

	@Data
	@ApiModel
	public static class ResUICAET006Dto {
		@Schema(description = "선물박스 list")
		@JsonAlias("attendId")
		private String attend_id;
		@Schema(description = "포츈쿠키 내용")
		@JsonAlias("fortuneMessage")
		private String fortune_message;
		@Schema(description = "포츈쿠키 명")
		@JsonAlias("fortuneName")
		private String fortune_name;
		@Schema(description = "선물박스 ")
		@JsonProperty("attendGift")
		private Box attend_gift;

	}

	@Data
	@ApiModel
	public static class ResUICAET007Dto {
		@Schema(description = "럭키드로우 신청 여부 메세지")
		private String result;
	}

	@Data
	@ApiModel
	public static class ResUICAET009Dto {
		@Schema(description = "결과 메세지")
		private String msg;
	}

	@Data
	@ApiModel
	public static class ResUICAET010Dto {
		@Schema(description = "")
		@JsonAlias("playerId")
		private String player_id;
	}


	@Data
	@ApiModel
	public static class ResUICAET011Dto {
		@Schema(description = "결과 메세지")
		private String msg;
	}

	@Data
	@ApiModel
	public static class ResUICAET012Dto {
		@Schema(description = "결과 메세지")
		private String msg;
	}


	@Data
	@ApiModel
	public static class ResUICAET013Dto {
		@JsonProperty("MatchVictory")
		@JsonAlias("matchVictory")
		private MatchVictory matchVictory;
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
		public static class MatchVictory {
			@Schema(description = "아이디")
			private String id;
			@Schema(description = "경기아이디")
			@JsonAlias("matchId")
			private String match_id;
			@Schema(description = "")
			@JsonAlias("userId")
			private String user_id;
			@JsonAlias("homeScore")
			private String home_score;
			@Schema(description = "")
			@JsonAlias("awayScore")
			private String away_score;
			@JsonAlias("homeScoreDetail")
			private String home_score_detail;
			@JsonAlias("awayScoreDetail")
			private String away_score_detail;
			@JsonAlias("predictYn")
			private String predict_yn;

		}

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
			@JsonAlias("homeScore")
			private String home_score;
			@Schema(description = "")
			@JsonAlias("awayScore")
			private String away_score;
			@JsonAlias("homeScoreDetail")
			private String home_score_detail;
			@JsonAlias("awayScoreDetail")
			private String away_score_detail;

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
	public static class ResUICAET014Dto {
		@Schema(description = "결과 메세지")
		private String msg;
	}
	@Data
	@ApiModel
	public static class ResUICAET015Dto {
		@Schema(description = "결과 메세지")
		private String msg;
	}
}
