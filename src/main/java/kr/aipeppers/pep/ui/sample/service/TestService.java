package kr.aipeppers.pep.ui.sample.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResIntDto;
import kr.aipeppers.pep.core.util.DateUtil;
import kr.aipeppers.pep.ui.sample.component.BizExtComponent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestService {

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	private BizExtComponent bizExtComp;

	public ResIntDto testInsert() throws Exception {
		int result = 0;
		Box box = new Box();
		box.put("grpCd", DateUtil.nowYYYYMMDDHH24MIssSSS());
		box.put("grpCdNm", "주데이터 삽입");
		if (1 == 1) { //롤백되도 수행되야 하는 업무 조건
			bizExtComp.histInsert(box); //트랜잭션이 롤백되도 수행
		}

		result = dao.insert("test.testInsert", box);
		int i = 1 / 0;

//		try {
//			result = dao.insert("test.testInsert", box);
//			int i = 1 / 0;
//		} finally {
//			bizExtComp.histInsert(box); //예외이력 삽입
//		}

		return new ResIntDto(result);
	}

}
