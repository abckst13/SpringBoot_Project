package kr.aipeppers.pep.ui.guestBook.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.guestBook.service.GuestBookService;
import kr.aipeppers.pep.ui.main.service.MainService;
import lombok.extern.slf4j.Slf4j;
/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@Component
public class GuestBookComponet {

	@Autowired
	protected GuestBookService guestBookService;

	//SNS API START
	/**
	 * @Method Name : aipepperPotoList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box guestBookList(Box box) throws Exception {
		return guestBookService.guestBookList(box);
	}

	/**
	 * @Method Name : guestBookInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int guestBookInsert(Box box) throws Exception {
		return guestBookService.guestBookInsert(box);
	}

	/**
	 * @Method Name : guestBookUpdate
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int guestBookUpdate(Box box) throws Exception {
		return guestBookService.guestBookUpdate(box);
	}

	/**
	 * @Method Name : guestBookDel
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int guestBookDel(Box box) throws Exception {
		return guestBookService.guestBookDel(box);
	}

	/**
	 * @Method Name : guestBookReadInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int guestBookReadInsert(Box box) throws Exception {
		return guestBookService.guestBookReadInsert(box);
	}

}
