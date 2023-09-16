package kr.aipeppers.pep.ui.category.service;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PeppersNowService {

	@Autowired
	private SqlSessionTemplate dao;

	@Autowired
	private Paginate paginate;


	/**
	 * @Method Name : pepperNowList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperNowList(Box box) throws Exception {
		List<Box> resBox = new ArrayList<>();
		int totCnt = dao.selectOne("category.peppernow.pepperNowCnt" , box);
		paginate.init(box, totCnt);
		resBox = dao.selectList("category.peppernow.pepperNowList", box);
		return resBox;
	}
}
