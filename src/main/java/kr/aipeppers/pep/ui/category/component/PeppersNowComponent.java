package kr.aipeppers.pep.ui.category.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.category.service.PeppersNowService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PeppersNowComponent {

	@Autowired
	PeppersNowService peppersNowService;

	/**
	 * @Method Name : pepperLiveList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperLiveList(Box box) throws Exception {
		return peppersNowService.pepperNowList(box);
	}
}
