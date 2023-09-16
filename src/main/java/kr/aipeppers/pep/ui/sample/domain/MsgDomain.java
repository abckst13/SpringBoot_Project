package kr.aipeppers.pep.ui.sample.domain;

import kr.aipeppers.pep.core.domain.ReqPageDto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class MsgDomain {

	@Data
	@ApiModel
	public static class ReqMsgSrchDto {
		@Schema(description = "메시지유형코드(S:성공, I:정보, W:경고, E:업무오류, F:시스템에러)")
		private String srchMsgType;
		@Schema(description = "시작일")
		private String srchStrDt;
		@Schema(description = "종료일")
		private String srchEndDt;
		@Schema(description = "시작월")
		private String srchStrMonDt;
		@Schema(description = "종료월")
		private String srchEndMonDt;
		@Schema(description = "검색컬럼명")
		private String srchMethod;
		@Schema(description = "검색어")
		private String srchWord;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	@ApiModel
	public static class ReqMsgPageSrchDto extends ReqPageDto {
		@Schema(description = "메시지유형코드(S:성공, I:정보, W:경고, E:업무오류, F:시스템에러)")
		private String srchMsgType;
		@Schema(description = "시작일")
		private String srchStrDt;
		@Schema(description = "종료일")
		private String srchEndDt;
		@Schema(description = "시작월")
		private String srchStrMonDt;
		@Schema(description = "종료월")
		private String srchEndMonDt;
		@Schema(description = "검색컬럼명")
		private String srchMethod;
		@Schema(description = "검색어")
		private String srchWord;
	}

	@Data
	@ApiModel
	public static class ReqMsgViewDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
	}

	@Data
	@ApiModel
	public static class ReqMsgDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
		@Schema(description = "메세지내용", example="로그인 정보가 존재하지 않습니다.")
		private String msgNm;
		@Schema(description = "메시지유형코드(S:성공, I:정보, W:경고, E:업무오류, F:시스템에러)", example="S")
		private String msgType;
	}

	@Data
	@ApiModel
	public static class ReqMsgDeleteDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
	}

	@Data
	@ApiModel
	public static class ReqMsgDupCntDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
	}

	@Data
	@ApiModel
	public static class ResMsgDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
		@Schema(description = "메세지내용", example="로그인 정보가 존재하지 않습니다.")
		private String msgNm;
		@Schema(description = "메시지유형코드(S:성공, I:정보, W:경고, E:업무오류, F:시스템에러)", example="S")
		private String msgType;
		@Schema(description = "시스템최초등록상세일시", example="20160623")
		private String cretDt;
	}

	@Data
	@ApiModel
	public class ReqMsgInsertDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
		@Schema(description = "메세지내용", example="로그인 정보가 존재하지 않습니다.")
		private String msgNm;
		@Schema(description = "메시지유형코드(S:성공, I:정보, W:경고, E:업무오류, F:시스템에러)", example="S")
		private String msgType;
	}

	@Data
	@ApiModel
	public class ReqMsgUpdateDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
		@Schema(description = "메세지내용", example="로그인 정보가 존재하지 않습니다.")
		private String msgNm;
		@Schema(description = "메시지유형코드(S:성공, I:정보, W:경고, E:업무오류, F:시스템에러)", example="S")
		private String msgType;
	}

	@Data
	@ApiModel
	public class ReqMsgSaveDto {
		@Schema(description = "메세지ID", example="E101")
		private String msgId;
		@Schema(description = "메세지내용", example="로그인 정보가 존재하지 않습니다.")
		private String msgNm;
		@Schema(description = "메시지유형코드(S:성공, I:정보, W:경고, E:업무오류, F:시스템에러)", example="S")
		private String msgType;
//		@Schema(description = "저장구분(C:insert, U:update, D:delete)", example="C")
//		private String saveFlag;
	}

	@Data
	@ApiModel
	public class ReqMsgDifDto {
		private List<ReqMsgDto> msgList;
		private List<ReqMsgSrchDto> msgSrchList;

		@Schema(description = "요청건수", example="3")
		private int reqCnt;
		@Schema(description = "샘플문자열", example="TXCQQZX12")
		private String strSample;
		@Schema(description = "요청ID", example="REQ100000012")
		private String reqId;

	}
}