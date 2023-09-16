package kr.aipeppers.pep.ui.guestBook.controller;

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
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ReqPageDto;
import kr.aipeppers.pep.core.domain.ResIntDto;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResPageDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.guestBook.component.GuestBookComponet;
import kr.aipeppers.pep.ui.main.component.MainComponet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/restapi/guestbook/info")
@Api(tags = { "방명록" }, description = "GuestBookController")
public class GuestBookController {

	@Autowired
	protected GuestBookComponet guestBookComponet;

	/**
	 * @Method Name : guestBookList
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "담벼락 조회")
	@PostMapping(value = "guestBookList") // nextMatchInfoView
	public ResResultDto<ResUIGBIF001Dto> guestBookList(@RequestBody ReqUIGBIF001Dto reqDto) throws Exception {
		return new ResResultDto<ResUIGBIF001Dto>(
				BeanUtil.convert(guestBookComponet.guestBookList(BoxUtil.toBox(reqDto)), ResUIGBIF001Dto.class));
	}

	/**
	 * @Method Name : guestBookInsert
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "담벼락 등록(선수 / 사용자 )")
	@PostMapping(value = "guestBookInsert") // nextMatchInfoView
	public ResIntDto guestBookInsert(@RequestBody ReqUIGBIF002Dto reqDto) throws Exception {
		return new ResIntDto(guestBookComponet.guestBookInsert(BoxUtil.toBox(reqDto)));
	}

	/**
	 * @Method Name : guestBookUpdate
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "담벼락 수정(선수 / 사용자 )")
	@PostMapping(value = "guestBookUpdate") // nextMatchInfoView
	public ResIntDto guestBookUpdate(@RequestBody ReqUIGBIF003Dto reqDto) throws Exception {
		return new ResIntDto(guestBookComponet.guestBookUpdate(BoxUtil.toBox(reqDto)));
	}

	/**
	 * @Method Name : guestBookDel
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "담벼락 삭제(사용자 )")
	@PostMapping(value = "guestBookDel") // nextMatchInfoView
	public ResIntDto guestBookDel(@RequestBody ReqUIGBIF004Dto reqDto) throws Exception {
		return new ResIntDto(guestBookComponet.guestBookDel(BoxUtil.toBox(reqDto)));
	}

	/**
	 * @Method Name : guestBookReadInsert
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "담벼락 읽음 표시")
	@PostMapping(value = "guestBookReadInsert") // nextMatchInfoView
	public ResIntDto guestBookReadInsert(@RequestBody ReqUIGBIF004Dto reqDto) throws Exception {
		return new ResIntDto(guestBookComponet.guestBookReadInsert(BoxUtil.toBox(reqDto)));
	}

	// -------------- reqParameter-------------------

	@Data
	@ApiModel
	public static class ReqUIGBIF001Dto {
		@Schema(description = "선수 아이디 (USER_ID X ID = O )" , example = "\"29\"")
		@JsonAlias("playerId")
		private String player_id;
	}

	@Data
	@ApiModel
	public static class ReqUIGBIF002Dto {
		@Schema(description = "선수용/사용자 담벼락" , example = "테스트 내용")
		private String content;
		@Schema(description = "선수 아이디" , example = "\"29\"")
		@JsonAlias("playerId")
		private String player_id;
	}

	@Data
	@ApiModel
	public static class ReqUIGBIF003Dto {
		@Schema(description = "선수용/사용자 담벼락" , example = "테스트 내용")
		private String content;
		@Schema(description = "선수 아이디" , example = "\"29\"")
		@JsonAlias("playerId")
		private String player_id;
		@Schema(description = "comment id ( 선수 담벼락 일 경우 값 세팅 x )" , example = "\"29\"")
		@JsonAlias("commentId")
		private String comment_id;
	}

	@Data
	@ApiModel
	public static class ReqUIGBIF004Dto {
		@Schema(description = "comment id" , example = "\"7\"")
		@JsonAlias("commentId")
		private String comment_id;
	}

	@Data
	@ApiModel
	public static class ReqUIGBIF005Dto {
		@Schema(description = "player id" , example = "\"29\"")
		@JsonAlias("playerId")
		private String player_id;
	}


	// -------------- resParameter-------------------

	@Data
	@ApiModel
	public static class ResUIGBIF001Dto {
		@JsonProperty("Player")
		@JsonAlias("player")
		private Player player;
		@JsonProperty("UserComment")
		@JsonAlias("userComment")
		private List<UserComment> userComment;

		@Schema(description = "선수 id")
		@JsonAlias("playerId")
		private String player_id;
		@Schema(description = "선수 명")
		@JsonAlias("playerNm")
		private String player_nm;
		@Schema(description = "선수 담벼락 내용")
		private String content;
		@Schema(description = "선수 담벼락 답글 개수")
		@JsonAlias("commentCnt")
		private String comment_cnt;
		@Schema(description = "글 등록 상세 날짜")
		@JsonAlias("diffDate")
		private String diff_date;
		@Schema(description = "전체 담벼락 수")
		private String count;

		@Data
		@ApiModel
		public static class Player {
			@Schema(description = "선수아이디")
			private String	id;
			@Schema(description = "선수이름")
			@JsonAlias("playerName")
			private String	player_name;
			@Schema(description = "영어이름")
			@JsonAlias("ePlayerName")
			private String	e_player_name;
			@Schema(description = "선수 가입 아이디")
			@JsonAlias("userId")
			private String	user_id;
			@Schema(description = "선수 프로필 사진")
			@JsonAlias("playerPiUurl")
			private String	player_pic_url;
			@Schema(description = "블러그 url")
			@JsonAlias("bgUrl")
			private String	bg_url;
			@Schema(description = "입단일")
			@JsonAlias("joinDate")
			private String	join_date;
			@Schema(description = "선수 포지션")
			private String	position;
			@Schema(description = "선수 백넘버")
			@JsonAlias("backNo")
			private String	back_no;
			@Schema(description = "사용 여부")
			@JsonAlias("useYn")
			private String	use_yn;
		}

		@Data
		@ApiModel
		public static class UserComment {
			@JsonProperty("User")
			@JsonAlias("user")
			private User user;

			@Schema(description = "id")
			private String id;
			@Schema(description = "등록자 아이디")
			@JsonAlias("userId")
			private String user_id;
			@Schema(description = "선수 아이디")
			@JsonAlias("playerId")
			private String player_id;
			@Schema(description = "등록자 이름")
			@JsonAlias("userNm")
			private String user_nm;
			@Schema(description = "내용")
			private String comment;
			@Schema(description = "등록일")
			@JsonAlias("regDt")
			private String reg_dt;
			@Schema(description = "글 등록 상세 날짜")
			@JsonAlias("diffDate")
			private String diff_date;

			@Data
			@ApiModel
			public static class User {
				@Schema(description = "id")
				private String id;
				@Schema(description = "활동 유무")
				private String active;
				@Schema(description = "등록자 닉네임")
				private String username;
				@Schema(description = "등록자 권한")
				private String role;
				@Schema(description = "사용자 썸네일 이미지")
				@JsonAlias("profilePicSmall")
				private String profile_pic_small;
				@Schema(description = "사용자 프로필 이미지")
				@JsonAlias("profilePic")
				private String profile_pic;
				@Schema(description = "등록일자")
				@JsonAlias("regDt")
				private String reg_dt;
			}
		}
	}
}
