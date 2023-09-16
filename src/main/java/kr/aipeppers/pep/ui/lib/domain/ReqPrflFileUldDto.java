
package kr.aipeppers.pep.ui.lib.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 프로필파일업로드 요청 Dto
 * <p>
 * 프로필파일업로드 요청 Dto
 * @comment 프로필파일업로드 요청 Dto
 *
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("프로필파일업로드 요청 Dto")
public class ReqPrflFileUldDto {

	/**
	 * 프로필별명암호화
	 * <p>
	 * 프로필별명암호화
	 * @comment 프로필별명암호화
	 * (Required)
	 *
	 */
	@NotNull
	@JsonProperty("mnmPrflNcknme")
	@JsonPropertyDescription("프로필별명암호화")
	@Schema(description = "프로필별명암호화", required = true, example = "내 첫 프로필 사진!")
	private String mnmPrflNcknme;
	/**
	 * 프로필파일
	 * <p>
	 * 프로필파일
	 * @comment 프로필파일
	 * (Required)
	 *
	 */
	@NotNull
	@JsonProperty("mnmPrflFile")
	@JsonPropertyDescription("프로필파일")
	@Schema(description = "프로필파일", required = true, example = "sample.jpeg")
	@Valid
	private MultipartFile mnmPrflFile;

}
