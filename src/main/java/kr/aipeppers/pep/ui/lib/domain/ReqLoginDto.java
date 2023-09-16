package kr.aipeppers.pep.ui.lib.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel
public class ReqLoginDto {

	@Schema(description = "로그인ID", example = "10000000001")
	private String loginId;
}