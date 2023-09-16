package kr.aipeppers.pep.ui.lib.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 해당 서비스는 특정업무시 공통작업 영역임
 * 예) url기반 API 특정 명세 저장
 * @author Y5003979
 *
 */
@Slf4j
@Service
public class BizService {

	@Autowired
	protected SqlSessionTemplate dao;

	/**
	 * url기반 API 특정 명세 저장(가칭)
	 * @param reqBox
	 * @param resBox
	 * @return
	 * @throws Exception
	 */
	public void apiInsert(Box reqBox, Box resBox) throws Exception {
		//TODO : apiInfoBox(api기준정보)와 POOM_서버API요청응답명세를 비교해 설정된 변수값으로 성공 여부를 판별한다
		Box apiInfoBox = reqBox.getBox("apiInfoBox");
		log.debug("apiInfoBox: {}", apiInfoBox);
//		if (resBox.nvl("succYn").equals("Y")) {
			//성공값 세팅
//		} else {
			//실패값 세팅
//		}
		//TODO : 성공/실패값을 기준으로 POOM_서버API요청응답명세 로그 테이블에 필요한 데이터 세팅
		reqBox.put("memId", DateUtil.nowYYYYMMDDHH24MIssSSS());
		int result = dao.insert("biz.apiLogInsert", reqBox);
		if (result > 0) {
			resBox.put("sucYn", "Y");
		} else {
			resBox.put("sucYn", "N");
		}
	}

}
