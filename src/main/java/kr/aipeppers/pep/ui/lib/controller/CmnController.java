package kr.aipeppers.pep.ui.lib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.HttpUtil;
import kr.aipeppers.pep.ui.lib.domain.ReqPrflFileUldDto;
import kr.aipeppers.pep.ui.lib.domain.ReqViewPrflImgDto;
import kr.aipeppers.pep.ui.lib.service.CmnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Api(tags = {"＃공통>파일처리, 앱버전체크 등"}, description = "CmnController")
@RequestMapping("cmn")
@Slf4j
public class CmnController {

	@Autowired
	private CmnService cmnService;

	@ApiOperation("프로필 파일 업로드")
	@PostMapping(value="prflFileUld", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResResultDto<Box> prflFileUld(ReqPrflFileUldDto reqDto) throws Exception {
		return new ResResultDto<Box>(cmnService.prflFileUld(reqDto));
	}

	@ApiOperation("프로필 이미지 조회 (POST방식)")
	@PostMapping(value="viewPrflImg")
	@ResponseBody
	public ResponseEntity<byte[]> viewPrflImg(@RequestBody ReqViewPrflImgDto reqDto) throws Exception {
		log.debug("HttpUtil.getRequestDomain(): {}", HttpUtil.getRequestDomain());
		return cmnService.viewPrflImg(reqDto);
	}

	@ApiOperation("프로필 이미지 조회 (GET방식-IMG처리 컴포넌트 등의 src 속성 용)")
	@GetMapping(value="getPrflImg", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getPrflImg(@ModelAttribute ReqViewPrflImgDto reqDto) throws Exception {
		return cmnService.viewPrflImg(reqDto);
	}
}

