package kr.aipeppers.pep.ui.lib.domain;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class ReqCmnCdDto {
	private List<CmnCdDto> itgC;
}