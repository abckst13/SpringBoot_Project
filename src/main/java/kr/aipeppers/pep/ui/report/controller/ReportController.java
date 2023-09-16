package kr.aipeppers.pep.ui.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.aipeppers.pep.core.annotation.ReqInfo;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.report.component.ReportComponet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/restapi/report")
@Api(tags = { "신고하기" }, description = "ReportController")
public class ReportController {

	@Autowired
	protected ReportComponet reportComponet;

	@ApiOperation(value = "댓글 신고하기")
	@PostMapping(value = "reportComment")
	@ReqInfo(validForm = "repo.reportComment")
	public ResResultDto<ResREPO001Dto> reportComment(@RequestBody ReqREPO001Dto reqDto) throws Exception {
		return new ResResultDto<ResREPO001Dto>(BeanUtil.convert(reportComponet.reportCommentSave(BoxUtil.toBox(reqDto)), ResREPO001Dto.class));
	}

	@ApiOperation(value = "비디오 신고하기")
	@PostMapping(value = "reportVideo")
	@ReqInfo(validForm = "repo.reportVideo")
	public ResResultDto<ResREPO002Dto> reportVideo(@RequestBody ReqREPO002Dto reqDto) throws Exception {
		return new ResResultDto<ResREPO002Dto>(BeanUtil.convert(reportComponet.reportVideoSave(BoxUtil.toBox(reqDto)), ResREPO002Dto.class));
	}

	@ApiOperation(value = "유저 신고하기")
	@PostMapping(value = "reportUser")
	@ReqInfo(validForm = "repo.reportUser")
	public ResResultDto<ResREPO003Dto> reportUser(@RequestBody ReqREPO003Dto reqDto) throws Exception {
		return new ResResultDto<ResREPO003Dto>(BeanUtil.convert(reportComponet.reportUserSave(BoxUtil.toBox(reqDto)), ResREPO003Dto.class));
	}

	@ApiOperation(value = "포스트 신고하기")
	@PostMapping(value = "reportPost")
	@ReqInfo(validForm = "repo.reportPost")
	public ResResultDto<ResREPO004Dto> reportPost(@RequestBody ReqREPO004Dto reqDto) throws Exception {
		return new ResResultDto<ResREPO004Dto>(BeanUtil.convert(reportComponet.reportPostSave(BoxUtil.toBox(reqDto)), ResREPO004Dto.class));
	}

	@Data
	@ApiModel
	public static class ReqREPO001Dto{
		@Schema(description = "코멘트id" , example = "")
		private String comment_id;
		@Schema(description = "타입 (post_comment ,post_comment_reply ,video_comment  ,video_comment_reply) " , example = "")
		private String type;
		@Schema(description = "상위 id" , example = "")
		private String parent_id;
		@Schema(description = "사유" , example = "")
		private String description;
		@Schema(description = "신고제목" , example = "")
		private String report_reason_title;
	}

	@Data
	@ApiModel
	public static class ReqREPO002Dto{
		@Schema(description = "오너id" , example = "")
		private String owner_id;
		@Schema(description = "비디오 id" , example = "")
		private String video_id;
		@Schema(description = "사유" , example = "")
		private String description;
		@Schema(description = "신고제목" , example = "")
		private String report_reason_title;
	}

	@Data
	@ApiModel
	public static class ReqREPO003Dto{
		@Schema(description = "차단id" , example = "")
		private String report_user_id;
		@Schema(description = "사유" , example = "")
		private String description;
	}

	@Data
	@ApiModel
	public static class ReqREPO004Dto{
		@Schema(description = "postid" , example = "")
		private String post_id;
		@Schema(description = "신고제목" , example = "")
		private String report_reason_title;
		@Schema(description = "사유" , example = "")
		private String description;
	}

	@Data
	@ApiModel
	public static class ResREPO001Dto {

	}
	@Data
	@ApiModel
	public static class ResREPO002Dto {

	}

	@Data
	@ApiModel
	public static class ResREPO003Dto {

	}
	@Data
	@ApiModel
	public static class ResREPO004Dto {

	}
}
