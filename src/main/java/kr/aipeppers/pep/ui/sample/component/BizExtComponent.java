package kr.aipeppers.pep.ui.sample.component;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 예외시 롤백되지 않고 수행되는 업무(이력)쿼리(async로 동작해야만 함 : 리턴값 void)
 *
 * @author 이성주(Y5003979)
 */
@Slf4j
@Component
@Async
public class BizExtComponent {

	@Autowired
	protected SqlSessionTemplate dao;

	/**
	 * 예제
	 * @param box
	 * @throws Exception
	 */
	public void histInsert(Box box) throws Exception {
		box.put("memNo", DateUtil.now(DateUtil.TIME_PATTERN));
		box.put("memId", "히스토리이력");
		dao.insert("test.histInsert", box);
	}

}
