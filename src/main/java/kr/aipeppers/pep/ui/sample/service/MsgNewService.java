package kr.aipeppers.pep.ui.sample.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResIntDto;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResPageDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ReqMsgDeleteDto;
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
import lombok.extern.slf4j.Slf4j;

/**
 * ＃샘플>시스템 메시지 API (MYBATIS 신규 샘플) 서비스
 *
 * @author 이성주(Y5003979)
 */
@Slf4j
@Service
public class MsgNewService {

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	private Paginate paginate;

	/**
	 * 메시지 다건 조회(페이징 없음)
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResListDto<ResMsgDto> msgNoPageList(ReqMsgSrchDto reqDto) throws Exception {
		return new ResListDto<ResMsgDto>(BeanUtil.convertList(dao.selectList("msg.msgList", BoxUtil.toBox(reqDto)), ResMsgDto.class));
	}

	/**
	 * 메시지 다건 조회(페이징)
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResListDto<ResMsgDto> msgList(ReqMsgPageSrchDto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);
		int totCnt = (Integer)dao.selectOne("msg.msgCnt", box);
		paginate.init(box, totCnt);
		return new ResListDto<ResMsgDto>(BeanUtil.convertList(dao.selectList("msg.msgList", box), ResMsgDto.class), BeanUtil.convert(box.get("paginate"), ResPageDto.class));
	}

	/**
	 * 메시지 단건 조회
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResResultDto<ResMsgDto> msgView(ReqMsgViewDto reqDto) throws Exception {
		return new ResResultDto<ResMsgDto>(BeanUtil.convert((Box)dao.selectOne("msg.msgView", BoxUtil.toBox(reqDto)), ResMsgDto.class));
	}

	/**
	 * 메시지 중복체크 (존재: 1이상, 미존재: 0)
	 * @param box
	 * @return
	 */
	public ResIntDto msgDupCnt(ReqMsgDupCntDto reqDto) throws Exception {
		return new ResIntDto(dao.selectOne("msg.msgCnt", BoxUtil.toBox(reqDto)));
	}

	/**
	 * 메시지 insert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResIntDto msgInsert(ReqMsgInsertDto reqDto) throws Exception {
		return new ResIntDto(dao.insert("msg.msgInsert", BoxUtil.toBox(reqDto)));
	}

	/**
	 * 메시지 update
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResIntDto msgUpdate(ReqMsgUpdateDto reqDto) throws Exception {
		return new ResIntDto(dao.update("msg.msgUpdate", BoxUtil.toBox(reqDto)));
	}

	/**
	 * 메시지 delete
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResIntDto msgDelete(ReqMsgDeleteDto reqDto) throws Exception {
		return new ResIntDto(dao.delete("msg.msgDelete", BoxUtil.toBox(reqDto)));
	}

	/**
	 * 메시지 merge 형식으로 저장
	 * @param box
	 * @return
	 * @throws Exception
	 */
//	public ResIntDto msgSave(ReqMsgSaveDto reqDto) throws Exception {
//		return new ResIntDto(dao.update("msg.msgMerge", BoxUtil.toBox(reqDto)));
//	}

	/**
	 * 메시지 flag 형식으로 저장
	 * @param box
	 * @return
	 * @throws Exception
	 */
//	public ResIntDto msgSave(ReqMsgSaveDto reqDto) throws Exception {
//		Box box = BoxUtil.toBox(reqDto);
//
//		int result = 0;
//		if (box.eq("saveFlag", "C")) {
//			result = dao.insert("msg.msgInsert", box);
//		} else if (box.eq("saveFlag", "U")) {
//			result = dao.update("msg.msgUpdate", box);
//		} else if (box.eq("saveFlag", "D")) {
//			result = dao.delete("msg.msgUpdate", box);
//		}
//		return new ResIntDto(result);
//	}

	/**
	 * 메시지가 존재하는지 체크후 저장
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResIntDto msgSave(ReqMsgSaveDto reqDto) throws Exception {
		Box box = BoxUtil.toBox(reqDto);

		if ((Integer)dao.selectOne("msg.msgCnt", box) == 0) {
			return new ResIntDto(dao.insert("msg.msgInsert", box));
		} else {
			return new ResIntDto(dao.update("msg.msgUpdate", box));
		}
	}

	/**
	 * 업무예제
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public ResRoanDto bizExam(ReqRoanDto reqDto) throws Exception {

		//사용하는 방법(예제1: dto)
		log.debug("reqDto: {}", reqDto);
		log.debug("dataBody: {}", reqDto.getDataBody());
		log.debug("dataBody/anincmAm: {}", reqDto.getDataBody().getAnincmAm());
		log.debug("dataProducts: {}", reqDto.getDataProducts());
		log.debug("dataProducts[1]: {}", reqDto.getDataProducts().get(0));
		log.debug("dataProducts[1]/lnPdC: {}", reqDto.getDataProducts().get(0).getLnPdC());

		//사용하는 방법(예제2: box)
		Box box = BoxUtil.toBox(reqDto);
		log.debug("box: {}", box);
		log.debug("dataBody: {}", box.getBox("dataBody"));
		log.debug("예제1)dataBody/anincmAm: {}", box.getBox("dataBody").nvl("anincmAm"));
		log.debug("예제2)dataBody/anincmAm: {}", box.getPathString("dataBody/anincmAm"));
		log.debug("dataProducts: {}", box.getList("dataProducts"));
		log.debug("dataProducts[1]: {}", box.getList("dataProducts").get(0));
		log.debug("dataProducts[1]/lnPdC: {}", box.getPathString("dataProducts[1]/lnPdC"));

		//응답값 셋팅(예제1: dto)
//		ResRoanDto resRoanDto = new ResRoanDto();
//		ReturnBody returnBody = new ReturnBody();
//		returnBody.setRspC("0000");
//
//		List<ReturnOption> returnOptions = new ArrayList<ReturnOption>();
//		ReturnOption returnOption = new ReturnOption();
//		returnOption.setKey("111");
//		returnOption.setValue("value11");
//		returnOptions.add(returnOption);
//
//		returnOption = new ReturnOption();
//		returnOption.setKey("222");
//		returnOption.setValue("value22");
//		returnOptions.add(returnOption);
//
//		resRoanDto.setReturnBody(returnBody);
//		resRoanDto.setReturnOption(returnOptions);
//		return resRoanDto;

		//응답값 세팅(예제2: box + query)
		Box resBox = new Box();
		resBox.put("returnBody", new Box() {{put("rspC", "0000");}});
		//resBox.putPath("returnBody/rspC", "9999"); //putPath는 업데이트만 가능, 존재하지 않는 요소에 생성 불가
		resBox.put("returnOption", dao.selectList("msg.bizExamList", box));
		return BeanUtil.convert(resBox, ResRoanDto.class);
	}

}
