package kr.aipeppers.pep.ui.sample.domain;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel
public class ReqRoanDto {
	private List<DataProducts> dataProducts;
	private DataBody dataBody;
	
	//금리한도요청 상품목록
	@Data
	public static class DataProducts {
		@Schema(description = "대출조회번호", example="B01202001040001")
		private String lnInqno;
		@Schema(description = "상품코드", example="product01")
		private String lnPdC;	
	}
	
	@Data
	public static class DataBody {
		@Schema(description = "신청자명", example="홍길동")
		private String name;
		@Schema(description = "신청자주민번호", example="8502051234567")
		private String rrno;
		@Schema(description = "CI", example="sadfewq23safvfsadgf4tddrsdbvddasdS")
		private String cino;
		@Schema(description = "직업분류(01: 직장인, 02: 개인사업자, 03: 공무원, 04: 주부, 99: 기타)", example="01")
		private String jobClsf;
		@Schema(description = "직장(사업장)명", example="홍길사")
		private String offiNm;
		@Schema(description = "사업자번호", example="1234567890")
		private String bzrno;
		@Schema(description = "고용형태(직장인인 경우 01: 정규직, 02: 계약직, 03: 파견직, 99: 기타)", example="01")
		private String hirFom;
		@Schema(description = "업종형태(개인사업자인 경우만 해당	01: 제조업, 02: 도소매업, 03: 운수업, 04: 음식숙박)", example="01")
		private String tobz;
		@Schema(description = "입사일(개업일)", example="20050111")
		private String entcoDt;
		@Schema(description = "연소득", example="60000000")
		private String anincmAm;
		@Schema(description = "의료보험가입구분(01: 직장의료보험, 02: 지역의료보험)", example="01")
		private String hlInsrSscbDvC;
		@Schema(description = "주거소유형태(01: 자가, 02: 전세, 03: 월세, 99: 기타)", example="01")
		private String huseOwnDvC;
		@Schema(description = "약관동의시간", example="20210625132234")
		private String prvAgTs;
		@Schema(description = "휴대전화번호", example="01012341234")
		private String mpno;
		@Schema(description = "휴대폰인증시간", example="20201021141555")
		private String hsCtfTs;
	}
			
}