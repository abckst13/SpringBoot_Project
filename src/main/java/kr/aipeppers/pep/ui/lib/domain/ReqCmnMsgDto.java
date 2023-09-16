
package kr.aipeppers.pep.ui.lib.domain;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 공통메시지 요청 Dto
 * <p>
 * 공통메시지 요청 Dto
 * 
 * @comment 공통메시지 요청 Dto
 * 
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("공통메시지 요청 Dto")
@Generated("OnGoSoft powered jsonschema2pojo")
public class ReqCmnMsgDto {

	/**
	 * 메시지코드
	 * <p>
	 * 각 채널에서 사용하는 메시지내용을 관리하는 코드, 메시지유형코드(1)+메시지종류코드(1)+메시지업무구분코드(3)+메시지일련번호(4)
	 * 
	 * @comment 각 채널에서 사용하는 메시지내용을 관리하는 코드, 메시지유형코드(1)+메시지종류코드(1)+메시지업무구분코드(3)+메시지일련번호(4)
	 * 
	 */
	@JsonProperty("msgC")
	@JsonPropertyDescription("각 채널에서 사용하는 메시지내용을 관리하는 코드, 메시지유형코드(1)+메시지종류코드(1)+메시지업무구분코드(3)+메시지일련번호(4)")
	@Schema(description = "메시지코드", example = "EBHPP0019", required = true)
	private String msgC;

}
