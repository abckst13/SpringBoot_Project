package kr.aipeppers.pep.ui.sample.domain;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel
public class ResRoanDto {
	private List<ReturnOption> returnOption;
	private ReturnBody returnBody;
	
	//옵션배열(TODO : 협의후 재정의)
	@Data
	@ApiModel
	private static class ReturnOption {
		@Schema(description = "옵션키", example="111")
		private String key;
		@Schema(description = "옵션값", example="value111")
		private String value;	
	}
	
	@Data
	@ApiModel
	private static class ReturnBody {
		@Schema(description = "리턴코드", example="0000")
		private String rspC;	
	}
			
}