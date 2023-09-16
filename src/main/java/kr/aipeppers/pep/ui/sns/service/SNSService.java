package kr.aipeppers.pep.ui.sns.service;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SNSService {

	@Autowired
	protected SqlSessionTemplate dao;


	@Autowired
	private Paginate paginate;


	/**
	 * @Method Name : showSnsList
	 * @param box
	 * @return
	 */
	public List<Box> showSnsList(Box box) throws Exception {

		box.put("sBox", SessionUtil.getUserData());
		box.put("typeCd", box.get("type_cd"));

		List<Box> resBox = new ArrayList<>();
		int totCnt = dao.selectOne("sns.info.showSnsCnt", box);
		paginate.init(box, totCnt);
		resBox = dao.selectList("sns.info.showSnsList",box);
		return resBox;
	}
}
