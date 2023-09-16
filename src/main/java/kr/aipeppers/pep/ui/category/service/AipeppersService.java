package kr.aipeppers.pep.ui.category.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.StringUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AipeppersService {

	@Autowired
	private SqlSessionTemplate dao;

	@Autowired
	private Paginate paginate;

	HttpSession session = SessionUtil.getSession();
	/**
	 * @Method Name : playerList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> playerList (Box box) throws Exception {
		return dao.selectList("category.aipeppers.playerList", box);
	}

	/**
	 * @Method Name : playerDetailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box playerDetailView (Box box) throws Exception {
		box.put("playerId", box.nvl("player_id"));
		return dao.selectOne("category.aipeppers.playerDetailView", box);
	}

	/**
	 * @Method Name : merchandiseList
	 * @param box
	 * @return
	 */
	public List<Box> merchandiseList (Box box) {
		return dao.selectList("category.aipeppers.merchandiseList", box);
	}

	/**
	 * @Method Name : nextMatchList
	 * @param box
	 * @return
	 */
	public List<Box> leagueScheduleList (Box box) {
		List<Box> list = dao.selectList("category.aipeppers.leagueScheduleList", box);
		if(list.isEmpty() || list == null) {
			throw new BizException("E107", new String[] {"경기일정이"});// {0} 존재하지 않습니다.
		}
		for(Box rowBox : list) {
			rowBox.put("teamId", rowBox.nvl("hometeamId"));
			rowBox.put("homeTeam", dao.selectOne("category.aipeppers.teamView", rowBox)); //홈팀 정보 조회 및 세팅
			rowBox.put("teamId", rowBox.nvl("awayteamId"));
			rowBox.put("oppTeam", dao.selectOne("category.aipeppers.teamView", rowBox)); //away 팀 정보 조회 및 세팅
		}
		return list;
	}

	/**
	 * @Method Name : getScheduleDetail
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box getScheduleDetail (Box box) throws Exception {
		Box resBox = new Box();
		Box matBox = dao.selectOne("category.aipeppers.getScheduleDetail", box);

		if(matBox == null ) {
			throw new BizException("E107", new String[] {"데이터가"});// {0} 존재하지 않습니다.
		}
		resBox.put("MatchSchedule", matBox);
		resBox.put("teamId", resBox.getBox("MatchSchedule").nvl("hometeamId"));
		resBox.put("homeTeam", dao.selectOne("category.aipeppers.teamViewDetail", resBox)); //홈팀 정보 조회 및 세팅
		resBox.put("teamId", resBox.getBox("MatchSchedule").nvl("awayteamId"));
		resBox.put("awayTeam", dao.selectOne("category.aipeppers.teamViewDetail", resBox)); //away 팀 정보 조회 및 세팅
		return resBox;
	}
	/**
	 * @Method Name : getVoteMatch
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box getVoteMatch (Box box) throws Exception {
		box.put("matchId", box.nvl("match_id"));
		Box resBox = new Box();
		Box homeBox = new Box();
		Box awayBox = new Box();
		Box matBox = dao.selectOne("category.aipeppers.getVoteMatch", box);
		if(matBox == null) {
			throw new BizException("E107", new String[] {"데이터가"});// {0} 존재하지 않습니다.
		}
		resBox.put("MatchSchedule", matBox);
		resBox.put("stadium", matBox.nvl("stadium"));

		matBox.put("teamId", matBox.nvl("hometeamId"));

		homeBox.put("homeTeam", dao.selectOne("category.aipeppers.teamViewMatch",matBox));
		resBox.put("home", homeBox);

		matBox.put("teamId", matBox.nvl("awayteamId"));
		awayBox.put("oppTeam", dao.selectOne("category.aipeppers.teamViewMatch", matBox));
		resBox.put("away", awayBox);

		return resBox;
	}
	/**
	 * @Method Name : nextMatchBanner
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box nextMatchBanner (Box box) throws Exception {
		box.put("id", box.nvl("id"));
		Box resBox = new Box();
		resBox = dao.selectOne("category.aipeppers.nextMatchBanner");
		resBox.put("teamId", resBox.nvl("hometeamId"));
		resBox.put("homeTeam", dao.selectOne("category.aipeppers.teamViewBanner", resBox)); //홈팀 정보 조회 및 세팅
		resBox.put("teamId", resBox.nvl("awayteamId"));
		resBox.put("oppTeam", dao.selectOne("category.aipeppers.teamViewBanner", resBox)); //away 팀 정보 조회 및 세팅
		return resBox;
	}



	/**
	 * @Method Name : noticeList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> noticeList (Box box) throws Exception {
		box.put("typeCd", "BD03");
		int totCnt = (Integer)dao.selectOne("category.aipeppers.noticeCnt", box);
		paginate.init(box, totCnt);
		List<Box> resBox = dao.selectList("category.aipeppers.noticeList", box);
		if(resBox == null || resBox.isEmpty()) {
			throw new BizException("E107", new String[] {"공지사항이"}); //{0} 존재하지 않습니다.
		}
		return  resBox;
	}

	/**
	 * @Method Name : noticeDetailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box noticeDetailView (Box box) throws Exception {
		Box resBox = dao.selectOne("category.aipeppers.noticeDetailView", box);
		resBox.put("content", HtmlUtils.htmlUnescape(resBox.nvl("content")));
		return dao.selectOne("category.aipeppers.noticeDetailView", box);
	}

	/**
	 * @Method Name : leaguesList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> leaguesList (Box box) {
		return dao.selectList("category.aipeppers.leaguesList", box);
	}

	/**
	 * @Method Name : eventList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> eventList (Box box) throws Exception {
		box.put("typeCd", "BD04");
		int totCnt = (Integer)dao.selectOne("category.aipeppers.eventCnt", box);
		paginate.init(box, totCnt);
		List<Box> resBox = dao.selectList("category.aipeppers.eventList", box);
		if(resBox == null || resBox.isEmpty()) {
			throw new BizException("E107", new String[] {"이벤트가"}); // {0} 존재하지 않습니다.
		}
		return resBox;
	}
}
