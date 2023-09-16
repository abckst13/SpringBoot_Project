package kr.aipeppers.pep.ui.lib.controller;

import javax.validation.Valid;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.aipeppers.pep.core.annotation.MethodMapping;
import kr.aipeppers.pep.core.cont.BizEnum.MethodType;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.lib.domain.ReqPrflImgUldDto;
import kr.aipeppers.pep.ui.lib.service.CmnRestService;
import lombok.Data;


/**
 * 공통 rest 컨트롤러
 *
 */
@RestController
@RequestMapping("restapi/cmn")
@Api(tags = {"＃공통>공통코드, 메세지조회등"}, description = "CmnRestController")
public class CmnRestController {

	@Autowired
	private CmnRestService cmnRestService;

	/**
	 * 공통 코드 조회
	 * @param reqDto
	 * @return
	 */
//	@ApiOperation(value="공통코드 조회")
//	@PostMapping(value = "cdList")
//	@MethodMapping(MethodType.SELECT)
//	public ResListDto<ResCmnCdDto> cdList(@RequestBody ReqCmnCdDto reqDto) throws Exception {
//		return cmnRestService.cdList(reqDto);
//	}

	/**
	 * 공통 메시지 단건 조회
	 * @param reqDto
	 * @return
	 */
//	@ApiOperation("공통 메시지 단건 조회")
//	@PostMapping("msg")
//	@MethodMapping(MethodType.SELECT)
//	public ResResultDto<ResCmnMsgDto> msg(@RequestBody ReqCmnMsgDto reqDto) {
//		return new ResResultDto<ResCmnMsgDto>(cmnRestService.msg(reqDto));
//	}

	/**
	 * 프로필 이미지(base64) 업로드
	 * @param reqDto
	 * @return
	 */
	@ApiOperation("프로필 이미지(base64) 업로드")
	@PostMapping("prflImgUld")
	@MethodMapping(MethodType.INPUT)
	public ResResultDto<Box> prflImgUld(@RequestBody ReqPrflImgUldDto reqDto) throws Exception {
		return new ResResultDto<Box>(cmnRestService.prflImgUld(reqDto));
	}

	/**
	 * 이메일 조회
	 * @param reqDto
	 * @return
	 */
	@ApiOperation("이메일 암호화 조회")
	@PostMapping("tokenUserInfoView")
	@MethodMapping(MethodType.INPUT)
	public ResResultDto<ResUserTokenDto> tokenUserInfoView(@RequestBody ReqUserTokenDto reqDto) throws Exception {
		return new ResResultDto<ResUserTokenDto>(BeanUtil.convert(cmnRestService.tokenUserInfoView(BoxUtil.toBox(reqDto)), ResUserTokenDto.class));
	}

	@ApiOperation(value="파일 업로드 테스트")
	@PostMapping(value = "fileUploadTest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResResultDto<Box> fileUploadTest(ReqUploadDto reqDto) throws Exception {
//		return new ResResultDto<ResUploadDto>(BeanUtil.convert(cmnRestService.fileUploadService(BoxUtil.toBox(reqDto)), ResUploadDto.class));
		return new ResResultDto<Box>(cmnRestService.fileUploadService(reqDto));
	}

	@Data
	@ApiModel
	public static class ReqUploadDto {
		@JsonProperty("files")
		private MultipartFile files[];
		@Schema(description = "파일 타입 (프로필 이미지 : image, 비디오 : video, 포스트 : post)", example="image")
		private String fileType;
	}

	@Data
	@ApiModel
	public static class ResUploadDto {

	}

	@Data
	@ApiModel
	public static class ReqUserTokenDto {
		@Schema(description = "조회 할 값 ", example="Jl3Pyt1SLGHIFYu002N/q1mPEGn8dKZsnI2xdxxDQak=")
		private String encValue;
		@Schema(description = "조회 타입 (이메일 : email , 폰번호 : phone , 이름 : name)", example="email")
		private String encType;
	}

	@Data
	@ApiModel
	public static class ResUserTokenDto {
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
	}

}