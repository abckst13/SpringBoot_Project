package kr.aipeppers.pep.ui.sample.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.MsgUtil;
import kr.aipeppers.pep.core.util.ValidatorUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MsgService {

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	private Paginate paginate;

//	@Autowired
//	private ValidatorUtil validatorUtil;

	/**
	 * 메시지 다건 조회(페이징 없음)
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> msgNoPageList(Box box) throws Exception {
		return dao.selectList("msg.msgList", box);
	}

	/**
	 * 메시지 다건 조회(페이징)
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> msgList(Box box) throws Exception {
		int totCnt = (Integer)dao.selectOne("msg.msgCnt", box);
		paginate.init(box, totCnt);
		return dao.selectList("msg.msgList", box);
	}

	/**
	 * 메시지 단건 조회
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box msgView(Box box) throws Exception {
		return dao.selectOne("msg.msgView", box);
	}

	/**
	 * 메시지 중복체크 (존재: 1이상, 미존재: 0)
	 * @param box
	 * @return
	 */
	public int msgDupCnt(Box box) throws Exception {
		return dao.selectOne("msg.msgCnt", box);
	}

	/**
	 * 메시지 insert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int msgInsert(Box box) throws Exception {
//		validatorUtil.validator("msg.msgInsert", box);
//		if (!box.nvl("msgId").substring(0, 1).equals(box.nvl("msgType"))) {
//			throw new BizException("F124", new String[] {"메시지유형"}); //{0} - 유효성조건 오류
//		}
		return dao.insert("msg.msgInsert", box);
	}

	/**
	 * 메시지 update
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int msgUpdate(Box box) throws Exception {
		return dao.update("msg.msgUpdate", box);
	}

	/**
	 * 메시지 merge 형식으로 저장
	 * @param box
	 * @return
	 * @throws Exception
	 */
//	public int msgSave(Box box) throws Exception {
//		return dao.update("msg.msgMerge", box);
//	}

	/**
	 * 메시지 if 형식으로 저장
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int msgSave(Box box) throws Exception {
		if ((Integer)dao.selectOne("msg.msgCnt", box) == 0) {
			return dao.insert("msg.msgInsert", box);
		} else {
			return dao.update("msg.msgUpdate", box);
		}
	}

	/**
	 * 메시지 delete
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int msgDelete(Box box) throws Exception {
		return dao.delete("msg.msgDelete", box);
	}

}
