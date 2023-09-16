package kr.aipeppers.pep.ui.lib.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel
public class ReqDamoDto {

	@Schema(description = "요청문자열", example = "가나다")
	private String reqTxt;

}