package kr.aipeppers.pep.ui.sns.controller;

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
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.sns.component.SNSComponent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("restapi/sns/info")
@Api(tags = {"SNS정보"}, description = "SNSController")
public class SNSController {

	@Autowired
	protected SNSComponent SNSComp;


	/**
	 * @Method Name : showVideosAgainstHashtag
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Sns 리스트")
	@PostMapping(value = "showSns")
	public ResListDto<ResUISNIF001Dto> showSns(@RequestBody ReqUISNIF001Dto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new  ResListDto<ResUISNIF001Dto>(BeanUtil.convertList(SNSComp.showSnsList(box), ResUISNIF001Dto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}


// =========================================== REQ DTO ==========================================================

	@Data
	@ApiModel
	public static class ReqUISNIF001Dto {
		@Schema(description = "페이지", example = "1")
		private int page;
		@Schema(description = "게시글 타입코드", example = "SN03")
		@JsonAlias("typeCd")
		private String type_cd;
	}




// =========================================== RES DTO ==========================================================

	@Data
	@ApiModel
	public static class ResUISNIF001Dto {
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
}
