package kr.aipeppers.pep.ui.lib.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel
public class CmnCdDto {

	/**
	 * 통합코드의 한글명
	 *
	 * 통합코드테이블에서 코드컬럼 한글명으로 사용
	 */

	@JsonProperty("itcKnm")
	@JsonPropertyDescription("통합코드테이블에서 코드컬럼 한글명으로 사용")
	@Schema(description = "통합코드한글명")
	private String itcKnm;
	/**
	 * 통합 코드 관리시 통합그룹코드의 유효(코드)값을 의미
	 */
	@JsonProperty("itcIns")
	@JsonPropertyDescription("통합 코드 관리시 통합그룹코드의 유효(코드)값을 의미")
	@Schema(description = "통합코드인스턴스")
	private String itcIns;
	/**
	 * 통합코드 관리시 등록된 분류 유형의 ID
	 */
	@JsonProperty("itcClsfTpId")
	@JsonPropertyDescription("통합코드 관리시 등록된 분류 유형의 ID")
	@Schema(description = "통합코드분류유형")
	private String itcClsfTpId;
	/**
	 * 메타에서 관리하는 Object ID
	 */
	@JsonProperty("itcDrmId")
	@JsonPropertyDescription("메타에서 관리하는 Object ID")
	@Schema(description = "통합코드식별ID")
	private String itcDrmId;
	/**
	 * 통합코드 관리시 통합그룹코드의 통합코드(유효값)의 명칭을 의미함
	 */
	@JsonProperty("itcInsNm")
	@JsonPropertyDescription("통합코드 관리시 통합그룹코드의 통합코드(유효값)의 명칭을 의미함")
	@Schema(description = "통합코드인스턴스명")
	private String itcInsNm;
	/**
	 * 코드의 영문명,코드테이블에서의 컬럼ID
	 */
	@JsonProperty("itcEnm")
	@JsonPropertyDescription("코드의 영문명,코드테이블에서의 컬럼ID")
	@Schema(description = "통합코드영문명")
	private String itcEnm;
	/**
	 * 통합코드의 유효내용(값)의 순서를 관리하기 위한 번호
	 */
	@JsonProperty("itcInsSeqn")
	@JsonPropertyDescription("통합코드의 유효내용(값)의 순서를 관리하기 위한 번호")
	@Schema(description = "통합코드인스턴스순번")
	private int itcInsSeqn;
	/**
	 * 통합코드 유효내용(값)에 대한 정식 명칭을 나타냄
	 */
	@JsonProperty("itcInsFrmlNm")
	@JsonPropertyDescription("통합코드 유효내용(값)에 대한 정식 명칭을 나타냄")
	@Schema(description = "통합코드인스턴스정식명")
	private String itcInsFrmlNm;
	/**
	 * 메타 시스템 코드 유효값에 대한 영문약어명을 나타냄
	 */
	@JsonProperty("itcInsEnglAbbrNm")
	@JsonPropertyDescription("메타 시스템 코드 유효값에 대한 영문약어명을 나타냄")
	@Schema(description = "통합코드인스턴스영문약어명")
	private String itcInsEnglAbbrNm;
	/**
	 * 메타 시스템 코드 유효값에 대한 영문정식명을 나타냄
	 */
	@JsonProperty("itcInsEnglFrmlNm")
	@JsonPropertyDescription("메타 시스템 코드 유효값에 대한 영문정식명을 나타냄")
	@Schema(description = "통합코드인스턴스영문정식명")
	private String itcInsEnglFrmlNm;
	/**
	 * 통합코드인스턴스(인스턴스)의 상위 통합코드인스턴스(유효값)를 의미한다.
	 */
	@JsonProperty("hgrItcIns")
	@JsonPropertyDescription("통합코드인스턴스(인스턴스)의 상위 통합코드인스턴스(유효값)를 의미한다.")
	@Schema(description = "상위통합코드인스턴스")
	private String hgrItcIns;
	/**
	 * 통합코드인스턴스(인스턴스)의 상위 통합코드인스턴스(유효값)의 명을 의미한다.
	 */
	@JsonProperty("hgrItcInsNm")
	@JsonPropertyDescription("통합코드인스턴스(인스턴스)의 상위 통합코드인스턴스(유효값)의 명을 의미한다.")
	@Schema(description = "상위통합코드인스턴스명")
	private String hgrItcInsNm;
	/**
	 * 상품서비스관계 정의시 상하위 관계의 유효시작일
	 */
	@JsonProperty("vlStrtdt")
	@JsonPropertyDescription("상품서비스관계 정의시 상하위 관계의 유효시작일")
	@Schema(description = "유효시작일자")
	private String vlStrtdt;
	/**
	 * 상품서비스관계 정의시 상하위 관계의 유효종료일
	 */
	@JsonProperty("vlEnddt")
	@JsonPropertyDescription("상품서비스관계 정의시 상하위 관계의 유효종료일")
	@Schema(description = "유효종료일자")
	private String vlEnddt;
	/**
	 * [정의] 코드인스턴스(유효값)에 해당하는 실제 상수로서 프로그램 로직 구현시 해당 유효값을 조회하여 처리 (하드코딩 방지)
	 * [특이사항] VARCHAR타입이기 때문에 숫자를 정의해서 사용하는 경우 NUMBER타입으로 변환해서 사용해야 함
	 *
	 * 예) 인스턴스 : 01, 인스턴스명 : 할인율 10%, 인스턴스상수내용 : 0.1
	 */
	@JsonProperty("itcInsCnstntCn")
	@JsonPropertyDescription("통합코드인스턴스상수내용")
	@Schema(description = "통합코드인스턴스상수내용")
	private String itcInsCnstntCn;
	/**
	 * 메타 데이터가 생성된 일자(메타에서 생성되어 복제되어 사용)
	 */
	@JsonProperty("metaCrtTs")
	@JsonPropertyDescription("메타 데이터가 생성된 일자(메타에서 생성되어 복제되어 사용)")
	@Schema(description = "메타생성상세일시")
	private String metaCrtTs;
	/**
	 * EAI에서 CRUD 처리시 기준 코드
	 */
	@JsonProperty("eaiProcsStc")
	@JsonPropertyDescription("EAI에서 CRUD 처리시 기준 코드")
	@Schema(description = "EAI처리상태코드")
	private String eaiProcsStc;
	/**
	 * 메타 데이터 정보가 전송된 일자
	 */
	@JsonProperty("metaTrsDt")
	@JsonPropertyDescription("메타 데이터 정보가 전송된 일자")
	@Schema(description = "메타전송일자")
	private String metaTrsDt;
	/**
	 * 시스템을 외부기관이 사용이 가능하도록 하기 위해 데이터를 구분하기 위한 코드 예) '001' : '일반'
	 */
	@JsonProperty("sysBurC")
	@JsonPropertyDescription("시스템을 외부기관이 사용이 가능하도록 하기 위해 데이터를 구분하기 위한 코드 예) '001' : '일반'")
	@Schema(description = "시스템기관코드")
	private String sysBurC;
	/**
	 * 시스템속성으로 해당 정보가 최초 등록된 상세일시
	 */
	@JsonProperty("sysFstRgTs")
	@JsonPropertyDescription("시스템속성으로 해당 정보가 최초 등록된 상세일시")
	@Schema(description = "시스템최초등록상세일시")
	private String sysFstRgTs;
	/**
	 * "시스템속성으로 해당 정보를 최초 등록한 주체의 정보
	 *
	 * - 로그인ID(사원번호 포함), Batch 프로그램ID, 인터넷 접속시 시스템구분"
	 */
	@JsonProperty("sysFstRgUsid")
	@JsonPropertyDescription("시스템속성으로 해당 정보를 최초 등록한 주체의 정보")
	@Schema(description = "시스템최초등록사용자ID")
	private String sysFstRgUsid;
	/**
	 * 시스템속성으로 해당 정보가 등록/변경된 최종 상세일시
	 */
	@JsonProperty("sysLtChTs")
	@JsonPropertyDescription("시스템속성으로 해당 정보가 등록/변경된 최종 상세일시")
	@Schema(description = "시스템최종변경상세일시")
	private String sysLtChTs;
	/**
	 * "시스템속성으로 해당 정보를 등록/변경한 주체의 최종 정보
	 *
	 * - 로그인ID(사원번호 포함), Batch 프로그램ID, 인터넷 접속시 시스템구분"
	 */
	@JsonProperty("sysLtChUsid")
	@JsonPropertyDescription("시스템속성으로 해당 정보를 등록/변경한 주체의 최종 정보")
	@Schema(description = "시스템최종변경사용자ID")
	private String sysLtChUsid;
	/**
	 * 시,군,구의 한글명칭
	 */
	@JsonProperty("sggNm")
	@JsonPropertyDescription("시,군,구의 한글명칭")
	@Schema(description = "시군구명")
	private String sggNm;

}