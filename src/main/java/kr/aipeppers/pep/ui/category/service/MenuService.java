package kr.aipeppers.pep.ui.category.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MenuService {

	@Autowired
	protected SqlSessionTemplate dao;
	/**
	 * @Method Name : menuList
	 * @param box
	 * @return
	 */
	public Box menuList(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData()); // userId  세팅
		log.debug("userId: {}", box.nvl("userId"));
		List<Box> upMenuList = dao.selectList("category.info.upMenuList",box);//상위 메뉴 조회
		if(upMenuList.isEmpty()) {
			throw new BizException("E107", new String[] {"메뉴가"}); // {0} 존재하지 않습니다.
		}
		for (Box rowBox : upMenuList) {
			rowBox.put("child", dao.selectList("category.info.lowMenuList",rowBox.get("gnbCode")));
		}
		resBox.put("Menu", upMenuList);
		resBox.put("user", dao.selectOne("category.info.userInfo", box));
		return resBox;
	}



}
