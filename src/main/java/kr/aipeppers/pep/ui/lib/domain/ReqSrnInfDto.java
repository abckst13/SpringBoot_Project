
package kr.aipeppers.pep.ui.lib.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 화면정보조회 요청 Dto
 * <p>
 * 화면정보조회 요청 Dto
 * @comment 화면정보조회 요청 Dto
 *
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("화면정보조회 요청 Dto")
public class ReqSrnInfDto {

	/**
	 * 화면ID
	 * <p>
	 * 화면ID(UI화면식별하는 중복되지 않은 명칭 )
	 * @comment 화면ID(UI화면식별하는 중복되지 않은 명칭 )
	 *
	 */
	@JsonProperty("srnId")
	@JsonPropertyDescription("화면ID(UI화면식별하는 중복되지 않은 명칭 )")
	@Schema(description = "화면ID")
	private String srnId;

}
