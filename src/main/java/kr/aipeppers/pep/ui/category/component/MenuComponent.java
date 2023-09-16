package kr.aipeppers.pep.ui.category.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.category.service.MenuService;

@Component
public class MenuComponent {

	@Autowired
	protected MenuService menuService;

	/**
	 * @Method Name : menuList
	 * @param box
	 * @return
	 */
	public Box menuList(Box box) {
		return menuService.menuList(box);
	}


}
