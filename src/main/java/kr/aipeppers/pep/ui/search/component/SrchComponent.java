package kr.aipeppers.pep.ui.search.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.search.service.SrchService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class SrchComponent {

	@Autowired
	protected SrchService srchService;

	/**
	 * @Method Name : searchList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> searchList(Box box) throws Exception {
		return srchService.searchList(box);
	}

	/**
	 * @Method Name : bennerAndHashList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> hashtagAllList(Box box) throws Exception {
		return srchService.hashtagAllList(box);
	}

	/**
	 * @Method Name : hashtagDetailList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> hashtagDetailList(Box box) throws Exception {
		return srchService.hashtagDetailList(box);
	}

	/**
	 * @Method Name : hashfavourSave
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box hashfavourSave(Box box) throws Exception {
		return srchService.hashfavourSave(box);
	}
//
//	/**
//	 * @Method Name : userHashtagList
//	 * @param box
//	 * @return
//	 */
//	public List<Box> userHashtagList(Box box) {
//		return srchService.userHashtagList(box);
//	}
//
//	/**
//	 * @Method Name : showPeperVideoList
//	 * @param box
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Box> showPeperVideoList(Box box) throws Exception {
//		return srchService.showPeperVideoList(box);
//	}
}
