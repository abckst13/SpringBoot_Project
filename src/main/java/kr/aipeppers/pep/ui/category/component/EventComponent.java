package kr.aipeppers.pep.ui.category.component;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.category.service.EventService;
import lombok.extern.slf4j.Slf4j;

/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@Component
public class EventComponent {
	@Autowired
	private EventService eventService;

	/**
	 * @Method Name : eventLuckyDrawView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> eventLuckyDrawView(Box box) throws Exception {
		return eventService.eventLuckyDrawView(box);
	}

	/**
	 * @Method Name : luckydrawDetailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box luckydrawDetailView(Box box) throws Exception {
		return eventService.luckydrawDetailView(box);
	}

	/**
	 * @Method Name : luckydrawInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box luckydrawInsert(Box box) throws Exception {
		return eventService.luckydrawInsert(box);
	}
	/**
	 * @Method Name : isMyVote
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box isMyVote(Box box) throws Exception {
		return eventService.isMyVote(box);
	}

	/**
	 * @Method Name : defaultAttendList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> defaultAttendList(Box box) throws Exception {
		return eventService.defaultAttendList(box);
	}

	/**
	 * @Method Name : attendChkInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box attendChkInsert(Box box) throws Exception {
		return eventService.attendChkInsert(box);
	}
	/**
	 * @Method Name : attendanceRecipientInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box attendanceRecipientInsert(Box box) throws Exception {
		return eventService.attendanceRecipientInsert(box);
	}

	/**
	 * @Method Name : luckydrawWinView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> luckydrawWinView(Box box) throws Exception {
		return eventService.luckydrawWinView(box);
	}

	/**
	 * @Method Name : luckydrawChk
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box luckydrawChk(Box box) throws Exception {
		return eventService.luckydrawChk(box);
	}

	/**
	 * @Method Name : mvpEventInfoInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box mvpEventInfoInsert(Box box) throws Exception {
		return eventService.mvpEventInfoInsert(box);
	}
	/**
	 * @Method Name : postMatchVictoryInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box postMatchVictoryInsert(Box box) throws Exception {
		return eventService.postMatchVictoryInsert(box);
	}
	/**
	 * @Method Name : getMatchVictory
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box getMatchVictory(Box box) throws Exception {
		return eventService.getMatchVictory(box);
	}

	/**
	 * @Method Name : matchVictoryInsertInfo
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box matchVictoryInsertInfo(Box box) throws Exception {
		return eventService.matchVictoryInsertInfo(box);
	}

	/**
	 * @Method Name : checkMatchVictoryApply
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box checkMatchVictoryApply(Box box) throws Exception {
		return eventService.checkMatchVictoryApply(box);
	}
}