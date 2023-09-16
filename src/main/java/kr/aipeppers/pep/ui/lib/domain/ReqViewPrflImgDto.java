
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
 * 프로필 이미지 조회 요청 Dto
 * <p>
 * 프로필 이미지 조회 요청 Dto
 * @comment 프로필 이미지 조회 요청 Dto
 *
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("프로필 이미지 조회 요청 Dto")
public class ReqViewPrflImgDto {

	/**
	 * 고객관리번호
	 * <p>
	 *  고객에게 유일하게 부여하는 식별번호[특이사항] '1' + 10자리일련번호 (총 11자리)
	 * @comment  고객에게 유일하게 부여하는 식별번호[특이사항] '1' + 10자리일련번호 (총 11자리)
	 *
	 */
	@JsonProperty("mnmCstMngtNo")
	@JsonPropertyDescription(" 고객에게 유일하게 부여하는 식별번호[특이사항] '1' + 10자리일련번호 (총 11자리)")
	@Schema(description = "고객관리번호", example = "10000000001", defaultValue = "10000000001")
	private String mnmCstMngtNo;

}
