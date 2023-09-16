package kr.aipeppers.pep.ui.category.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.category.service.AipeppersService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AipeppersComponent {

	@Autowired
	private AipeppersService aipeppersService;


	/**
	 * @Method Name : playerList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> playerList(Box box) throws Exception {
		return aipeppersService.playerList(box);
	}

	/**
	 * @Method Name : playerDetailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box playerDetailView(Box box) throws Exception {
		return aipeppersService.playerDetailView(box);
	}

	/**
	 * @Method Name : getScheduleDetail
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box getScheduleDetail(Box box) throws Exception {
		return aipeppersService.getScheduleDetail(box);
	}

	/**
	 * @Method Name : merchandiseList
	 * @param box
	 * @return
	 */
	public List<Box> merchandiseList(Box box) {
		return aipeppersService.merchandiseList(box);
	}

	/**
	 * @Method Name : nextMatchList
	 * @param box
	 * @return
	 */
	public List<Box> leagueScheduleList(Box box) {
		return aipeppersService.leagueScheduleList(box);
	}

	/**
	 * @Method Name : leaguesList
	 * @param box
	 * @return
	 */
	public List<Box> leaguesList(Box box) {
		return aipeppersService.leaguesList(box);
	}

	/**
	 * @Method Name : noticeList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> noticeList(Box box) throws Exception {
		return aipeppersService.noticeList(box);
	}

	/**
	 * @Method Name : noticeDetailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box noticeDetailView(Box box) throws Exception {
		return aipeppersService.noticeDetailView(box);
	}
	/**
	 * @Method Name : nextMatchBanner
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box nextMatchBanner(Box box) throws Exception {
		return aipeppersService.nextMatchBanner(box);
	}
	/**
	 * @Method Name : getVoteMatch
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box getVoteMatch(Box box) throws Exception {
		return aipeppersService.getVoteMatch(box);
	}

	/**
	 * @Method Name : eventList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> eventList(Box box) throws Exception {
		return aipeppersService.eventList(box);
	}

}
