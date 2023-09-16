package kr.aipeppers.pep.ui.sample.controller;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgDeleteDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgDifDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgDupCntDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgInsertDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgPageSrchDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgSaveDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgSrchDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgUpdateDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgViewDto;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ResMsgDto;
import kr.aipeppers.pep.ui.sample.domain.ReqRoanDto;
import kr.aipeppers.pep.ui.sample.domain.ResRoanDto;
import kr.aipeppers.pep.ui.sample.service.MsgNewService;
import kr.aipeppers.pep.core.annotation.ReqInfo;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResIntDto;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BoxUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ＃샘플>시스템 메시지 API (MYBATIS 신규 샘플)
 *
 * @author 이성주(Y5003979)
 */
@Slf4j
//@RestController
//@RequestMapping("sample/msgNew")
//@Api(tags = {"＃샘플>시스템 메시지 API (MYBATIS 신규 샘플)"}, description = "MsgNewController")
@Deprecated
public class MsgNewController {

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	private MsgNewService msgNewService;

	@ApiOperation(value="메시지 다건 조회 (페이징 없음)")
	@PostMapping(value = "msgNoPageList")
	public ResListDto<ResMsgDto> msgNoPageList(@RequestBody ReqMsgSrchDto reqDto) throws Exception {
		return new ResListDto<ResMsgDto>(dao.selectList("msg.msgList", BoxUtil.toBox(reqDto)));
	}

	@ApiOperation(value="메시지 다건 조회 (페이징)")
	@PostMapping(value = "msgList")
	public ResListDto<ResMsgDto> msgList(@RequestBody ReqMsgPageSrchDto reqDto) throws Exception {
		return msgNewService.msgList(reqDto);
	}

	@ApiOperation(value="메시지 단건 조회")
	@PostMapping(value = "msgView")
	public ResResultDto<ResMsgDto> msgView(@RequestBody ReqMsgViewDto reqDto) throws Exception {
		return msgNewService.msgView(reqDto);
	}

	@ApiOperation(value="메세지 등록")
	@PostMapping(value = "msgInsert")
	@ReqInfo(validForm="msg.msgInsert")
	public ResIntDto msgInsert(@RequestBody ReqMsgInsertDto reqDto) throws Exception {
		return msgNewService.msgInsert(reqDto);
	}

	@ApiOperation(value="메세지 수정")
	@PostMapping(value = "msgUpdate")
	public ResIntDto msgUpdate(@RequestBody ReqMsgUpdateDto reqDto) throws Exception {
		return msgNewService.msgUpdate(reqDto);
	}

	@ApiOperation(value="메세지 삭제")
	@PostMapping(value = "msgDelete")
	public ResIntDto msgDelete(@RequestBody ReqMsgDeleteDto reqDto) throws Exception {
		return msgNewService.msgDelete(reqDto);
	}

	@ApiOperation(value="메세지 저장")
	@PostMapping(value = "msgSave")
	public ResIntDto msgSave(@RequestBody ReqMsgSaveDto reqDto) throws Exception {
		return msgNewService.msgSave(reqDto);
	}

	@ApiOperation(value="메세지 중복확인")
	@PostMapping(value = "msgDupCnt")
	public ResIntDto msgDupCnt(@RequestBody ReqMsgDupCntDto reqDto) throws Exception {
		return msgNewService.msgDupCnt(reqDto);
	}

	@ApiOperation(value="복잡한 input json")
	@PostMapping(value = "msgDif")
	public ResIntDto msgDif(@RequestBody ReqMsgDifDto reqDto) throws Exception {

		log.debug("reqDto: {}", reqDto);
		log.debug("msgList: {}", reqDto.getMsgList());
		log.debug("msgList[1]: {}", reqDto.getMsgList().get(0));
		log.debug("msgSrchList: {}", reqDto.getMsgSrchList());
		log.debug("msgSrchList[1]/srchMsgTpc: {}", reqDto.getMsgSrchList().get(0).getSrchMsgType());
		log.debug("strSample: {}", reqDto.getStrSample());

		Box box = BoxUtil.toBox(reqDto);
		log.debug("box: {}", box);
		log.debug("msgList: {}", box.getList("msgList"));
		log.debug("msgList[1]: {}", box.getList("msgList").get(0));
		log.debug("msgSrchList: {}", box.getList("msgSrchList"));
		log.debug("msgSrchList[1]/srchMsgType: {}", box.getPathString("msgSrchList[1]/srchMsgType"));
		log.debug("strSample: {}", box.nvl("strSample"));
		return new ResIntDto(1);
	}

	/**
	 *  [dataBody]
	 *  1	신청자명			name			String	50	√	(전용선 암호화)
		2	신청자주민번호		rrno			String	13	√	(전용선 암호화)
		3	CI				cino			String	88	√	(전용선 암호화)
		4	직업분류			jobClsf			String	2	√	"01 : 직장인	02 : 개인사업자	03 : 공무원	04 : 주부		99 : 기타"
		5	직장(사업장)명		offiNm			String	150		직업분류-직장인,개인사업자,공무원 필수
		6	사업자번호			bzrno			String	10		직업분류-직장인,개인사업자,공무원 필수
		7	고용형태			hirFom			String	2		"직업분류가 ‘직장인' 인 경우	01 : 정규직	02 : 계약직	03 : 파견직	99 : 기타"
		8	업종형태			tobz			String	2		"직업분류가 '개인사업자'인 경우만 해당	01 : 제조업	02 : 도소매업	03 : 운수업	04 : 음식숙박"
		9	입사일(개업일)		entcoDt			String	8		YYYYMMDD
		10	연소득			anincmAm		String	13	√
		11	의료보험가입구분		hlInsrSscbDvC	String	2	√	"01 : 직장의료보험 02 : 지역의료보험"
		12	주거소유형태		huseOwnDvC		String	2	√	"01: 자가	 02: 전세 03 월세 99 기타"
		13	약관동의시간		prvAgTs			String	14	√	YYYYMMDD HHMMSS
		14	휴대전화번호		mpno			String	11	√	마스킹 또는 전용선 암호화
		15	휴대폰인증시간		hsCtfTs			String	14	√	YYYYMMDD HHMMSS

		[dataProducts]	금리한도요청 상품목록	Array
		16		대출조회번호	lnInqno			String	15	√
		17		상품코드		lnPdC			String	20	√	협의된상품코드
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="업무예제 - 대출금리한도요청")
	@PostMapping(value = "bizExam")
	public ResRoanDto bizExam(@RequestBody ReqRoanDto reqDto) throws Exception {
		return msgNewService.bizExam(reqDto);
	}

}
