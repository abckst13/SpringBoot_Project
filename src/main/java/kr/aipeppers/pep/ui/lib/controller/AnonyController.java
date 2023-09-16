package kr.aipeppers.pep.ui.lib.controller;

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
import kr.aipeppers.pep.core.domain.ResIntDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.lib.service.AnonyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("anony")
@Api(tags = {"회원>로그인,세션정보,시스템정보등"}, description = "AnonyController")
public class AnonyController {

	@Autowired
	private AnonyService anonyService;

	/**
	 * @Method Name : login
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="로그인")
	@PostMapping(value="login")
	public ResResultDto<ResUIJOIN001Dto> login(@RequestBody ReqUIJOIN001Dto reqDto) throws Exception {
		try {
			return new ResResultDto<ResUIJOIN001Dto>(BeanUtil.convert(anonyService.loginView(BoxUtil.toBox(reqDto)), ResUIJOIN001Dto.class), "I227"); //로그인 되었습니다.
         } catch (BizException e) {
            if (e.getMessageId().equals("E122")) {
            	anonyService.updateFailCnt(BoxUtil.toBox(reqDto));
            }
            throw new BizException(e.getMessageId());
         }
	}

	/**
	 * @Method Name : getAccessToken
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="accessToken 발급")
	@PostMapping(value="getAccessToken")
	public ResResultDto<ResUIJOIN002Dto> getAccessToken(@RequestBody ReqUIJOIN002Dto reqDto) throws Exception {
		return new ResResultDto<ResUIJOIN002Dto>(BeanUtil.convert(anonyService.accessTokenInsertView(BoxUtil.toBox(reqDto)), ResUIJOIN002Dto.class));
	}

	/**
	 * @Method Name : seedEncrptyInfo
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="SEED 암호화 복구(DB값 복구화 하여 값 체크할 때 사용)")
	@PostMapping(value="seedEncrptyInfo")
	public ResResultDto<ResEncryptDto> seedEncrptyInfo(@RequestBody ReqEncryptDto reqDto) throws Exception {
		return new ResResultDto<ResEncryptDto>(BeanUtil.convert(anonyService.seedEncrptyInfo(BoxUtil.toBox(reqDto)), ResEncryptDto.class));
	}


	/**
	 * @Method Name : verifyEmailPassword
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="enmail&password 인증")
	@PostMapping(value="verifyEmailPassword")
	public ResResultDto<ResUIJOIN004Dto> verifyEmailPassword(@RequestBody ReqUIJOIN004Dto reqDto) throws Exception {
		return new ResResultDto<ResUIJOIN004Dto>(BeanUtil.convert(anonyService.sendEnmailView(BoxUtil.toBox(reqDto)), ResUIJOIN004Dto.class));
	}

	/**
	 * @Method Name : verifyAuthCode
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="phone 중복체크")
	@PostMapping(value="getAuthCode")
	public ResResultDto<ResUIJOIN005Dto> getAuthCode(@RequestBody ReqUIJOIN005Dto reqDto) throws Exception {
		return new ResResultDto<ResUIJOIN005Dto>(BeanUtil.convert(anonyService.phoneChk(BoxUtil.toBox(reqDto)), ResUIJOIN005Dto.class));
	}

	/**
	 * @Method Name : verifyAuthCode
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="phone 중복체크 확인")
	@PostMapping(value="verifyAuthCode")
	public ResResultDto<ResUIURLG006Dto> verifyAuthCode(@RequestBody ReqUIURLG006Dto reqDto) throws Exception {
		return new ResResultDto<ResUIURLG006Dto>(BeanUtil.convert(anonyService.confirmChk(BoxUtil.toBox(reqDto)), ResUIURLG006Dto.class),"I201");
	}

	/**
	 * @Method Name : findPassword
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="비밀번호 재설정 (아이디/비밀번호찾기)")
	@PostMapping(value="findPassword")
	public ResIntDto findPassword(@RequestBody ReqUIURLG007Dto reqDto) throws Exception {
		return new ResIntDto(anonyService.passwordUpdate(BoxUtil.toBox(reqDto)),"I211");
	}


	/**
	 * @Method Name : userRegister
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="회원가입")
	@PostMapping(value="userRegister")
	@ReqInfo(validForm = "anony.joinChk")
	public ResResultDto<ResUIJOIN008Dto> userRegister(@RequestBody ReqUIJOIN008Dto reqDto) throws Exception {
		return new ResResultDto<ResUIJOIN008Dto>(BeanUtil.convert(anonyService.joinInsert(BoxUtil.toBox(reqDto)), ResUIJOIN008Dto.class ),"I204");
	}

	/**
	 * @Method Name : checkUsername
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="닉네임 체크")
	@PostMapping(value="checkUsername")
	public ResIntDto checkUsername(@RequestBody ReqUIJOIN010Dto reqDto) throws Exception {
		return new ResIntDto(anonyService.usernameChk(BoxUtil.toBox(reqDto)),"I203");
	}

	/**
	 * @Method Name : registerEmailVerify
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="아이디(이메일) 체크")
	@PostMapping(value="registerEmailVerify")
	public ResResultDto<ResUIJOIN011Dto> registerEmailVerify(@RequestBody ReqUIJOIN011Dto reqDto) throws Exception {
		return new ResResultDto<ResUIJOIN011Dto>(BeanUtil.convert(anonyService.emailChk(BoxUtil.toBox(reqDto)), ResUIJOIN011Dto.class),"I205");
	}

	/**
	 * @Method Name : findEmail
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="마스킹된 이메일 조회")
	@PostMapping(value="findEmail")
	public ResResultDto<ResUIJOIN012Dto> findEmail(@RequestBody ReqUIJOIN012Dto reqDto) throws Exception {
		return new ResResultDto<ResUIJOIN012Dto>(BeanUtil.convert(anonyService.maskEmailView(BoxUtil.toBox(reqDto)), ResUIJOIN012Dto.class));
	}

//	/**
//	 * @Method Name : userRegister
//	 * @param reqDto
//	 * @return
//	 * @throws Exception
//	 */
//	@ApiOperation(value="회원가입")
//	@PostMapping(value="userRegister")
//	@ReqInfo(validForm = "anony.joinChk")
//	public ResIntDto userRegister1(@RequestBody ReqUIJOIN008Dto reqDto) throws Exception {
//		return new ResIntDto(anonyService.joinInsert(BoxUtil.toBox(reqDto)),"I204");
//	}
//
	@ApiOperation(value="로그아웃")
	@PostMapping(value="logout")
	public ResIntDto tempLogout() throws Exception {
		return new ResIntDto(anonyService.tempLogout(),"I209");
	}

	@ApiOperation(value="session 값 확인 api")
	@PostMapping(value="sessionChk")
	public void sessionChk() throws Exception {
		anonyService.sessionChk();
	}

	/**
	 * @Method Name : aecEncrypt
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="aes암호화 로직(평문을 쓰면 AES,SEED 로 암호화 됨)")
	@PostMapping(value="aecEncrypt")
	public ResResultDto<ResEncryptDto> aecEncrypt(@RequestBody ReqEncryptDto reqDto) throws Exception {
		return new ResResultDto<ResEncryptDto>(BeanUtil.convert(anonyService.aecEncrypt(BoxUtil.toBox(reqDto)), ResEncryptDto.class));
	}


	/**
	 * @Method Name : apiVersionChk
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("버전체크")
	@PostMapping(value="versionCheck")
	public ResResultDto<ResVersionDto> versionCheck(@RequestBody ReqVersionDto reqDto) throws Exception {
		return new ResResultDto<ResVersionDto>(BeanUtil.convert(anonyService.apiVersionChk(BoxUtil.toBox(reqDto)), ResVersionDto.class));
	}

	@ApiOperation("탈퇴")
	@PostMapping("withdrawUser")
	public ResResultDto<ResUIJOIN014Dto> withdrawUser(@RequestBody ReqUIJOIN014Dto reqDto) throws Exception {
		return new ResResultDto<ResUIJOIN014Dto>(BeanUtil.convert(anonyService.withdrawUserUpdate(BoxUtil.toBox(reqDto)), ResUIJOIN014Dto.class),"I216");
	}

//	@ApiOperation(value="시스템 정보 조회")
//	@GetMapping(value="systemInfo")
//	public ResResultDto<ResSystemDto> systemInfo() throws Exception {
//		return new ResResultDto<ResSystemDto>(anonyService.systemInfo());
//	}

	@Data
	@ApiModel
	public static class ReqEncryptDto {
		@Schema(description = "암호화할 String 값")
		private String text;
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN014Dto {
		@Schema(description = "패스워드")
		private String password;
		@Schema(description = "탈퇴 코드")
		@JsonAlias("activeReason")
		private String active_reason;
		@Schema(description = "탈퇴 상세 내용")
		@JsonAlias("activeReasonDetail")
		private String active_reason_detail;
	}


	@Data
	@ApiModel
	public static class ReqUIJOIN008Dto {
		@Schema(description = "사용자명", example = "김성태")
		private String username;
		@Schema(description = "휴대폰번호", example = "4o1ZDp0EZ1PDMKTKoEngmw==")
		private String phone;
		@Schema(description = "회원가입 이메일", example = "ol+sa1jEtSv6ix0CQ7jE6EW2WQNmFpPhp0Du+IY2fT0=")
		private String email;
		@Schema(description = "패스워드", example = "/EIIlzx6NrnKJE9RKrEnxg==")
		private String password;
		@Schema(description = "authCode", example = "")
		private String authCode;
		@Schema(description = "accessToken", example = "")
		private String accessToken;
		@Schema(description = "마케팅 허용 Y/N", example = "Y")
		private String marketingYn;
		@Schema(description = "프로필 이미지 정보 ", example = "/9j/4AAQSkZJRgABAQEBLAEsAAD/4QBSRXhpZgAASUkqAAgAAAABAA4BAgAwAAAAGgAAAAAAAABzYW1wbGUgc2lnbi4gc2FtcGxlIHNxdWFyZSBzcGVlY2ggYnViYmxlLiBzYW1wbGX/4QVNaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIj4KCTxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CgkJPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczpJcHRjNHhtcENvcmU9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBDb3JlLzEuMC94bWxucy8iICAgeG1sbnM6R2V0dHlJbWFnZXNHSUZUPSJodHRwOi8veG1wLmdldHR5aW1hZ2VzLmNvbS9naWZ0LzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGx1cz0iaHR0cDovL25zLnVzZXBsdXMub3JnL2xkZi94bXAvMS4wLyIgIHhtbG5zOmlwdGNFeHQ9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBFeHQvMjAwOC0wMi0yOS8iIHhtbG5zOnhtcFJpZ2h0cz0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3JpZ2h0cy8iIHBob3Rvc2hvcDpDcmVkaXQ9IkdldHR5IEltYWdlcy9pU3RvY2twaG90byIgR2V0dHlJbWFnZXNHSUZUOkFzc2V0SUQ9IjExNjEzNTI0ODAiIHhtcFJpZ2h0czpXZWJTdGF0ZW1lbnQ9Imh0dHBzOi8vd3d3LmlzdG9ja3Bob3RvLmNvbS9sZWdhbC9saWNlbnNlLWFncmVlbWVudD91dG1fbWVkaXVtPW9yZ2FuaWMmYW1wO3V0bV9z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")
		@JsonAlias("profilePic")
		private String profile_pic;
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN004Dto {
		@Schema(description = "휴대폰번호", example = "/C2QEuHi37ZKevsM3LuZ5Q==")
		private String phone;
		@Schema(description = "accessToken", example = "5ff06ad6c45005c6")
		private String accessToken;
		@Schema(description = "accessCode", example = "000000")
		private String accessCode;
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN005Dto {
		@Schema(description = "휴대폰번호", example = "4o1ZDp0EZ1PDMKTKoEngmw==")
		private String phone;
		@Schema(description = "accessToken", example = "2a405895f9f3b4ec")
		private String accessToken;
		@Schema(description = "type(find = 아이디&비밀번호 찾기/ '' = 회원가입->핸드폰인증)", example = "")
		private String type;
	}

	@Data
	@ApiModel
	public static class ReqUIURLG006Dto {
		@Schema(description = "휴대폰번호", example = "4o1ZDp0EZ1PDMKTKoEngmw==")
		private String phone;
		@Schema(description = "accessToken", example = "2a405895f9f3b4ec")
		private String accessToken;
		@Schema(description = "accessCode", example = "555083")
		private String accessCode;
	}

	@Data
	@ApiModel
	public static class ReqUIURLG007Dto {
		@Schema(description = "password", example = "dmq3diAOpPIYfgvG7Rj4xw==")
		private String password;
		@Schema(description = "email", example = "tAYqylZNI+elq+73R9H3nc731GmCUP/94V8lOlpRdaI=")
		private String email;
	}

	@Data
	@ApiModel
	public static class ResUIJOIN008Dto {
		@Schema(description = "아이디값")
		@JsonAlias("userId")
		private String user_id;
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN002Dto {
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN001Dto {
		@Schema(description = "디바이스 토큰", example = "eSKxMiZwJU5Wkk64i_FrxR:APA91bHc-xlYQnwJnQr7xO1kp_ChK9gCE_hljN_LvelQQ6qxHinHLvDZXiX1pqbyUG5G79QZHc-bfBsvZxQws0_9_DVbyLRabydO3LnpYSdC_WjyjIThGtbZwY4y-92dCVhuDfTWl7Dm")
		private String device_token;
		@Schema(description = "이메일", example = "ol+sa1jEtSv6ix0CQ7jE6EW2WQNmFpPhp0Du+IY2fT0=")
		private String email;
		@Schema(description = "패스워드", example = "dmq3diAOpPIYfgvG7Rj4xw==")
		private String password;
		@Schema(description = "자동로그인 yn", example = "Y")
		@JsonAlias("checkYn")
		private String check_yn;
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN010Dto {
		@Schema(description = "닉네임", example = "담콩")
		private String username;
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN011Dto {
		@Schema(description = "이메일", example = "ol+sa1jEtSv6ix0CQ7jE6EW2WQNmFpPhp0Du+IY2fT0=")
		private String email;
	}

	@Data
	@ApiModel
	public static class ReqUIJOIN012Dto {
		@Schema(description = "이메일", example = "ol+sa1jEtSv6ix0CQ7jE6EW2WQNmFpPhp0Du+IY2fT0=")
		private String email;
	}


	@Data
	@ApiModel
	public static class ReqVersionDto {
		@Schema(description =  "버전정보", example = "i = ios , a = android")
		private String app_type;
	}

	@Data
	@ApiModel
	public static class ResVersionDto {
		@Schema(description =  "버전정보")
		private String value;
		@Schema(description =  "force_yn")
		@JsonAlias("forceYn")
		private String force_yn;
		@Schema(description =  "update_link")
		@JsonAlias("updateLink")
		private String update_link;
	}

	@Data
	@ApiModel
	public static class ResUIJOIN004Dto {
		@Schema(description = "이메일")
		private String email;
	}

	@Data
	@ApiModel
	public static class ResUIJOIN005Dto {
		@Schema(description = "성공시 코드")
		@JsonAlias("authCode")
		private String auth_code;
	}

	@Data
	@ApiModel
	public static class ResUIURLG006Dto {

	}


	@Data
	@ApiModel
	public static class ResUIJOIN010Dto {
	}

	@Data
	@ApiModel
	public static class ResUIJOIN011Dto {
		@Schema(description = "이메일")
		@JsonAlias("email")
		private String email;
	}

	@Data
	@ApiModel
	public static class ResUIJOIN012Dto {
		@Schema(description = "이메일")
		private String email;
		@Schema(description = "사용자 이름")
		private String username;
	}

	@Data
	@ApiModel
	public static class ResUIJOIN001Dto {
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;
		@Schema(description = "성공시 메세지")
		private String result;

		@Data
		@ApiModel
		public static class User {
			@Schema(description = "회원 아이디")
			private String id;
			@Schema(description = "device_token")
			@JsonAlias("deviceToken")
			private String device_token;
			@Schema(description = "auth_token")
			@JsonAlias("authToken")
			private String auth_token;
			@Schema(description = "이메일")
			private String email;
//			@Schema(description = "패스워드")
//			private String password;
			@Schema(description = "핸드폰 번호")
			private String phone;
			@Schema(description = "활동 여부")
			private String active;
			@Schema(description = "탈퇴 이유")
			private String active_reason;
			@Schema(description = "탈퇴 이유상세")
			@JsonAlias("activeReasonDetail")
			private String active_reason_detail;
			@Schema(description = "사용자 이름")
			private String username;
			@Schema(description = "사용자 권한")
			private String role;
			@Schema(description = "사용자 포인트")
			private String point;
			@Schema(description = "회원 등급 코드")
			private String grade;
			@Schema(description = "영문 성")
			@JsonAlias("firstName")
			private String first_name;
			@Schema(description = "영문 이름")
			@JsonAlias("lastName")
			private String last_name;
			@Schema(description = "성별")
			private String gender;
			@Schema(description = "bio")
			private String bio;
			@Schema(description = "회원 프로필 이미지")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "회원 썸네일 이미지")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
			@Schema(description = "회원 토큰 값")
			private String token;
			@Schema(description = "회원아이피")
			private String ip;
			@Schema(description = "회원로그인 실패 수")
			@JsonAlias("lockCnt")
			private String lock_cnt;
			@Schema(description = "passChnYn")
			@JsonAlias("passChnYn")
			private String pass_chn_yn;
			@Schema(description = "passModifyDatetime")
			@JsonAlias("passModifyDatetime")
			private String pass_modify_datetime;
			@Schema(description = "등록자")
			@JsonAlias("regId")
			private String reg_id;
			@Schema(description = "등록 일자")
			@JsonAlias("regDt")
			private String reg_dt;
		}

	}


	@Data
	@ApiModel
	public static class ResUIJOIN002Dto {
		@Schema(description = "accessToken")
		private String accessToken;
	}

	@Data
	@ApiModel
	public static class ResEncryptDto {
		@Schema(description = "aes 암화화 값")
		private String aesEncrypt;
		@Schema(description = "seed 암화화 값")
		private String seedEncrypt;
		@Schema(description = "seed Decrypt 값")
		private String seedDecrypt;
	}

	@Data
	@ApiModel
	public static class ResSessionChkDto {
//		@Schema(description = "aes 암화화 값")
//		private String aesEncrypt;
//		@Schema(description = "seed 암화화 값")
//		private String seedEncrypt;
	}

	@Data
	@ApiModel
	public static class ResUIJOIN014Dto {
		@Schema(description = "resultMsg")
		private String resultMsg;
	}

}
