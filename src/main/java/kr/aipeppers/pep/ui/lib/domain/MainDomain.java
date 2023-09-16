package kr.aipeppers.pep.ui.lib.domain;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.aipeppers.pep.core.data.Box;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MainDomain {

	@Data
	public static class ResCdDto {
		private String grpCd;
		private String grpCdNm;
		private String cd;
		private String cdNm;
		private String useYn;

		private String CdVal1;
		private String CdVal2;
		private String CdVal3;
		private String CdVal4;
		private String CdVal5;

	}

	@Data
	public static class ResMsgDto {
		private String msgId;
		private String msgNm;
		private String msgType;
		private String msgTypeNm;
		private String msgDtl;
	}

	@Data
	public static class ResMenuDto {
		private String menuId;
		private String menuNm;
		private String subId;
		private String subNm;
		private String path;
		private String pageUrl;
		private String subDtl;
		private Integer lv;
	}

	@Data
	public static class ResMetaDto {
		private List<ResCdDto> cdAllList;
		private List<ResMsgDto> msgAllList;
		private List<ResMenuDto> menuAllList;
		private String memNm;
		public ResMetaDto(List<ResCdDto> cdAllList, List<ResMsgDto> msgAllList, List<ResMenuDto> menuAllList, String memNm) {
			this.cdAllList = cdAllList;
			this.msgAllList = msgAllList;
			this.menuAllList = menuAllList;
			this.memNm = memNm;
		}
	}

	@Data
	public static class ReqMetaDto {
		private String jwtToken;
	}


	@Data
	@ApiModel
	public static class ReqTokenDto {
		@NotNull
		@NotEmpty
		@Schema(description = "계정 ID", example="test1", required = true)
		private String memId;
		@NotNull
		@NotEmpty
		@Schema(description = "계정 패스워드", example="1234", required = true)
		private String passwd;
	}

	@Data
	@ApiModel
	public static class ResTokenDto {
		@Schema(description = "token", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjcmV0RHQiOjE1OTg4NTc5MjMxNDksImNyZXRyIjpudWxsLCJjaGdEdCI6MTU5ODg1NzkyMzE0OSwiY2hnciI6bnVsbCwibWVtTm8iOjAsIm1lbUlkIjoidGVzdDEiLCJwYXNzd2QiOiI4MWRjOWJkYjUyZDA0ZGMyMDAzNmRiZDgzMTNlZDA1NSIsIm1lbU5tIjoi7J207ISx7KO8IiwidXNlWW4iOiJZIiwiZW1haWwiOiJ4eHhAbWFpbC5jb20iLCJub3ciOjE1OTg5NDY2MjI0MTEsImV4cCI6MTY5OTk5MDIyMn0.rv6gs9sBVN_HRB42mabiaLcmI6jiUYxbCINqGHuSSfA")
		private String jwtToken;
		@Schema(description = "token type", example="Bearer")
		private String tokenType;
		@Schema(description = "token 유효시간(sec)", example="3600")
		private int validTimeSec;
		public ResTokenDto(Box memBox, String jwtToken, String tokenType, int validTimeSec) {
			this.jwtToken = jwtToken;
			this.tokenType = tokenType;
			this.validTimeSec = validTimeSec;
		}
	}

	@Data
	@ApiModel
	@NoArgsConstructor
	public static class ResSystemDto {
		@Schema(description = "cpu 사용량", example="12")
		private long cpu;
		@Schema(description = "memory 사용량", example="26")
		private long memory;
//		@Schema(description = "총 memory", example="75")
//		private long totalMemory;
		@Schema(description = "disk 사용량", example="33")
		private long disk;
//		@Schema(description = "총 disk", example="200")
//		private long totalDisk;

		public ResSystemDto(long cpu, long memory, long disk) {
			this.cpu = cpu;
			this.memory = memory;
//			this.totalMemory = totalMemory;
			this.disk = disk;
//			this.totalDisk = totalDisk;
		}
	}

}