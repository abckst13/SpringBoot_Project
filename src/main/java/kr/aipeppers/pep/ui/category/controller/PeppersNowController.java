package kr.aipeppers.pep.ui.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAlias;

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
import kr.aipeppers.pep.ui.category.component.PeppersNowComponent;
import kr.aipeppers.pep.ui.category.service.PeppersNowService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/restapi/category/peppernow")
@Api(tags = {"카테고리>AI PEPPERS NOW"}, description = "PeppersNowController")
public class PeppersNowController {

	@Autowired
	PeppersNowService peppersNowService;

	@Autowired
	PeppersNowComponent peppersNowComponent;


	/**
	 * @Method Name : getPepperLive
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation("PEPPER NOW")
	@PostMapping("getPepperLive")
	public ResListDto<ResUICAAL001Dto> getPepperLive(@RequestBody ReqUICAAL001Dto reqDto) throws Exception{
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResUICAAL001Dto>(BeanUtil.convertList(peppersNowComponent.pepperLiveList(box), ResUICAAL001Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	@Data
	@ApiModel
	public static class ReqUICAAL001Dto {
		@Schema(description = "타입-PL01 = 세로영상 ,PL02 = LIVE ,PL03 = 오리지날, ALL = 전체", example = "ALL" )
		private String type;
		@Schema(description = "페이지", example = "1")
		private int page;
	}

	@Data
	@ApiModel
	public static class ResUICAAL001Dto {
		@Schema(description = "아이디")
		private String id;
		@Schema(description = "보드타입")
		@JsonAlias("typeCd")
		private String type_cd;
		@Schema(description = "타이틀")
		private String title;
		@Schema(description = "컨텐츠 타입")
		@JsonAlias("contentCd")
		private String content_cd;
		@Schema(description = "컨텐츠")
		private String content;
		@Schema(description = "외부링크")
		private String link;
		@Schema(description = "조회수")
		private int hits;
		@Schema(description = "삭제 유무")
		@JsonAlias("delYn")
		private String del_yn;
		@Schema(description = "사용유무")
		@JsonAlias("useYn")
		private String use_yn;
		@Schema(description = "메인 노출 유무")
		@JsonAlias("mainYn")
		private String main_yn;
		@Schema(description = "정렬순서")
		private int ord;
		@Schema(description = "썸네일")
		private String thumb;
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
}
