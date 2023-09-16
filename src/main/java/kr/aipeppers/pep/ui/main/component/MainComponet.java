package kr.aipeppers.pep.ui.main.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.main.service.MainService;
import lombok.extern.slf4j.Slf4j;
/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@Component
public class MainComponet {

	@Autowired
	protected MainService mainService;

	//SNS API START
	/**
	 * @Method Name : aipepperPotoList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box aipepperMainSNSList(Box box) throws Exception {
		return mainService.aipepperMainSNSList(box);
	}

	/**
	 * @Method Name : aipepperMainInfoList
	 * @return
	 * @throws Exception
	 */
	public Box aipepperMainInfoList(Box box) throws Exception {
		return mainService.aipepperMainInfoList(box);
	}

	//SNS API END
	/**
	 * @Method Name : nextMatchInfoView
	 * @param box
	 * @return
	 */
	public Box nextMatchInfoView() throws Exception{

		return mainService.nextMatchInfoView();
	}


	/**
	 * @Method Name : notificationCnt
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box notificationCnt(Box box) throws Exception{

		return mainService.notificationCnt(box);
	}

	/**
	 * @Method Name : notificationList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public  List<Box> notificationList(Box box) throws Exception{

		return mainService.notificationList(box);
	}

	/**
	 * @Method Name : showAppSliderList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public  List<Box> showAppSliderList(Box box) throws Exception{

		return mainService.showAppSliderList(box);
	}

	/**
	 * @Method Name : popupEventView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public  Box popupEventView(Box box) throws Exception{
		return mainService.popupEventView(box);
	}


}
