package kr.aipeppers.pep.ui.sns.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.sns.service.SNSService;

@Component
public class SNSComponent {
	@Autowired
	private SNSService snsService;

	/**
	 * @Method Name : showSnsList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> showSnsList(Box box) throws Exception{

		return snsService.showSnsList(box);
	}
}
