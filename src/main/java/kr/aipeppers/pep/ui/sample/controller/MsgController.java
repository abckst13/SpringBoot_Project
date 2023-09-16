package kr.aipeppers.pep.ui.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.aipeppers.pep.core.annotation.ReqInfo;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResPageDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgDeleteDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgDupCntDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgPageSrchDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgSrchDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgViewDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ResMsgDto;
import kr.aipeppers.pep.ui.sample.service.MsgService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("sample/msg")
@Api(tags = {"＃샘플>시스템 메시지 API "}, description = "MsgController")
public class MsgController {

	@Autowired
	private MsgService msgService;

	@ApiOperation(value="메시지 다건 조회 (페이징 없음)")
	@PostMapping(value = "msgNoPageList")
	public ResListDto<ResMsgDto> msgNoPageList(@RequestBody ReqMsgSrchDto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResMsgDto>(BeanUtil.convertList(msgService.msgNoPageList(box), ResMsgDto.class));
	}

	@ApiOperation(value="메시지 다건 조회 (페이징) - 축약소스")
	@PostMapping(value = "msgList")
	public ResListDto<ResMsgDto> msgList(@RequestBody ReqMsgPageSrchDto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		return new ResListDto<ResMsgDto>(BeanUtil.convertList(msgService.msgList(box), ResMsgDto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

//	@ApiOperation(value="메시지 다건 조회 (페이징) - 원소스")
//	@PostMapping(value = "msgList")
//	public ResListDto<ResMsgDto> msgList(@RequestBody ReqMsgSrchDto reqDto) throws Exception {
//		Box box = BoxUtil.toBox(reqDto);
//		List<Box> list = msgService.msgList(box);
//		List<ResMsgDto> resList = BeanUtil.convertList(list, ResMsgDto.class);
//		return new ResListDto<ResMsgDto>(resList, BeanUtil.convert(box.get("paginate"), PaginateDto.class));
//	}

	@ApiOperation(value="메시지 단건 조회 - 축약소스")
	@PostMapping(value = "msgView")
	public ResResultDto<ResMsgDto> msgView(@RequestBody ReqMsgViewDto reqDto) throws Exception {
		return new ResResultDto<ResMsgDto>(BeanUtil.convert(msgService.msgView(BoxUtil.toBox(reqDto)), ResMsgDto.class));
	}

//	@ApiOperation(value="메시지 단건 조회 - 원소스")
//	@PostMapping(value = "msgView")
//	public ResResultDto<ResMsgDto> msgView(@RequestBody ReqMsgViewDto reqDto) throws Exception {
//		Box box = BoxUtil.toBox(reqDto);
//		Box resBox = msgService.msgView(box);
//		ResMsgDto resVo = BeanUtil.convert(resBox, ResMsgDto.class);
//		return new ResResultDto<ResMsgDto>(resVo);
//	}

	@ApiOperation(value="메세지 등록")
	@PostMapping(value = "msgInsert")
	@ReqInfo(validForm="msg.msgInsert")
	public ResResultDto<Integer> msgInsert(@RequestBody ReqMsgDto reqDto) throws Exception {
		return new ResResultDto<Integer>(msgService.msgInsert(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="메세지 수정")
	@PostMapping(value = "msgUpdate")
	public ResResultDto<Integer> msgUpdate(@RequestBody ReqMsgDto reqDto) throws Exception {
		return new ResResultDto<Integer>(msgService.msgUpdate(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="메세지 삭제")
	@PostMapping(value = "msgDelete")
	public ResResultDto<Integer> msgDelete(@RequestBody ReqMsgDeleteDto reqDto) throws Exception {
		return new ResResultDto<Integer>(msgService.msgDelete(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="메세지 저장")
	@PostMapping(value = "msgSave")
	public ResResultDto<Integer> msgSave(@RequestBody ReqMsgDto reqDto) throws Exception {
		return new ResResultDto<Integer>(msgService.msgSave(BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="메세지 중복확인")
	@PostMapping(value = "msgDupCnt")
	public ResResultDto<Integer> msgDupCnt(@RequestBody ReqMsgDupCntDto reqDto) throws Exception {
		return new ResResultDto<Integer>(msgService.msgDupCnt(BoxUtil.toBox(reqDto)));
	}

}
