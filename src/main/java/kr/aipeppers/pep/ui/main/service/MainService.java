package kr.aipeppers.pep.ui.main.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MainService {

	@Autowired
	protected SqlSessionTemplate dao ;

	@Autowired
	private Paginate paginate;

	//PEPPER INFO API START
	/**
	 * @Method Name : aipepperMainInfoList
	 * @return
	 * @throws Exception
	 */
	public Box aipepperMainInfoList(Box box) throws Exception {

		Box resBox = new Box();
		Box nextMatchInfoView = nextMatchInfoView(); // 페퍼 경기 일정
		List<Box> popupEventList = popupEventList(); // 페퍼 이벤트 배너
		List<Box> mainYoutubeList = bannerMediaList(); // 페퍼 YOUTUBE List
		List<Box> pepperPlayer = pepperPlayerList(); // 페퍼 선수 리스트
		List<Box> peppersNews = bannerNewsList(); // 페퍼 선수 리스트
		List<Box> pepperAkaList = pepperAkaList(); // 페퍼 선수 리스트
		List<Box> pepperOnList = pepperOnList(); // 페퍼 선수 리스트
		resBox.put("nextMatchInfoView", nextMatchInfoView);
		resBox.put("popupEventList", popupEventList);
		resBox.put("mainYoutubeList", mainYoutubeList);
		resBox.put("pepperPlayer", pepperPlayer);
		resBox.put("peppersNews", peppersNews);
		resBox.put("pepperOnList", pepperOnList);
		resBox.put("pepperAkaList", pepperAkaList);
		return resBox;
	}

	/**
	 * @Method Name : bannerNewsList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> bannerNewsList() throws Exception {
		Box box = new Box();
		box.put("pageUnit", 4);
		box.put("typeCd", "BD02");
		int totCnt = dao.selectOne("main.info.boardCnt" , box);
		paginate.init(box, totCnt);
		List<Box> resBox = dao.selectList("main.info.boardList" , box);
//		if(resBox == null || resBox.isEmpty()) {
//			throw new BizException("E107", new String[] {"NEWS 정보가"} ); //{0} 존재하지 않습니다.
//		}
		return resBox;
	}

	/**
	 * @Method Name : pepperOnList
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperOnList() throws Exception {
		Box box = new Box();
//		box.put("pageUnit", 10);
		box.put("typeCd", "PL02");
		int totCnt = dao.selectOne("main.info.boardCnt" , box);
		paginate.init(box, totCnt);
		List<Box> resBox = dao.selectList("main.info.boardList" , box);
//		if(resBox == null || resBox.isEmpty()) {
//			throw new BizException("E107", new String[] {"NEWS 정보가"} ); //{0} 존재하지 않습니다.
//		}
		return resBox;
	}


	/**
	 * @Method Name : pepperAkaList
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperAkaList() throws Exception {
		Box box = new Box();
//		box.put("pageUnit", 10);
		box.put("typeCd", "PL01");
		int totCnt = dao.selectOne("main.info.boardCnt" , box);
		paginate.init(box, totCnt);
		List<Box> resBox = dao.selectList("main.info.boardList" , box);
//		if(resBox == null || resBox.isEmpty()) {
//			throw new BizException("E107", new String[] {"NEWS 정보가"} ); //{0} 존재하지 않습니다.
//		}
		return resBox;
	}

	/**
	 * @Method Name : nextMatchInfoView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box nextMatchInfoView() throws Exception {
		Box resBox = dao.selectOne("main.info.nextMatchInfoView");
		String teamId = "";
		if(resBox != null) {
			teamId = resBox.nvl("hometeamId");//홈팀 정보 조회 및 세팅
			Box homeTeam = dao.selectOne("main.info.teamView", teamId);
			if (homeTeam == null) {
				throw new BizException("E107", new String[] {"homeTeam 정보가"} ); //{0} 존재하지 않습니다.
			}
			resBox.put("homeTeam", homeTeam);

			teamId = resBox.nvl("awayteamId");//홈팀 정보 조회 및 세팅
			Box oppTeam = dao.selectOne("main.info.teamView", teamId);
			if (oppTeam == null) {
				throw new BizException("E107", new String[] {"oppTeam 정보가"} ); //{0} 존재하지 않습니다.
			}
			resBox.put("oppTeam", oppTeam);
		}
		return resBox;
	}

	/**
	 * @Method Name : bannerMediaList
	 * @param box
	 * @return
	 */
	public List<Box> bannerMediaList() {
		Box box = new Box();
		box.put("bdType", "BD01");
		List<Box> resBox = dao.selectList("main.info.bannerMediaList",box);
		if(resBox == null || resBox.isEmpty()) {
			throw new BizException("E107", new String[] {"베너&미디어 정보가"} ); //{0} 존재하지 않습니다.
		}
		return resBox;
	}

	/**
	 * @Method Name : mainYoutubeList
	 * @return
	 * @throws Exception
	 */
//	public List<Box> mainYoutubeList() throws Exception {
//		Box box = new Box();
//		int totalCnt = dao.selectOne("main.info.mainYoutubeListCnt", box);
//		paginate.init(box, totalCnt);
//		return dao.selectList("main.info.mainYoutubeList", box);
//	}

	/**
	 * @Method Name : popupEventList
	 * @param box
	 * @return
	 */
	public List<Box> popupEventList() {
		Box box = new Box();
		box.put("typeCd", "BD04");
		List<Box> resBox = dao.selectList("main.info.popupEventList",box);
		if(resBox == null || resBox.isEmpty()) {
			throw new BizException("E107", new String[] {"이벤트 팝업 정보가"} ); //{0} 존재하지 않습니다.
		}
		return resBox;
	}
	//PEPPER INFO API END

	//SNS API START

	/**
	 * @Method Name : aipepperPotoList
	 * @param box
	 * @return
	 * @throws Exception // 담벼락 select  문 구현 필요
	 */
	public Box aipepperMainSNSList(Box box) throws Exception {
		Box resBox = new Box();
		List<Box> pepperPotoList = pepperPotoList(); // 페퍼포토
		List<Box> pepperNaverPost = pepperNaverPostList(); // 네이버 포스트
		List<Box> pepperNaverTv = pepperNaverTvList(); // 네이버 티비
		List<Box> pepperStagram = pepperStagramList(); // 페퍼스타그램
		List<Box> pepperPlayer = pepperPlayerList(); // 페퍼 선수 리스트
		resBox.put("pepperNaverPost", pepperNaverPost);
		resBox.put("pepperNaverTv", pepperNaverTv);
		resBox.put("pepperStagram", pepperStagram);
		resBox.put("pepperPotoList", pepperPotoList);
		resBox.put("pepperPlayer", pepperPlayer);
		return resBox;
	}

	/**
	 * @Method Name : pepperNaverPostList
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperNaverPostList() throws Exception {
		Box box = new Box();
		box.put("typeCd", "SN01");
		box.put("limit", 10);
		return dao.selectList("main.info.pepperPotoList", box);
	}

	/**
	 * @Method Name : showAppSliderList
	 * @param box
	 * @return
	 */
	public List<Box> showAppSliderList(Box box) {
		return dao.selectList("main.info.showAppSliderList");
	}

	/**
	 * @Method Name : pepperNaverTvList
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperNaverTvList() throws Exception {
		Box box = new Box();
		box.put("typeCd", "SN02");
		box.put("limit", 10);
		return dao.selectList("main.info.pepperPotoList", box);
	}

	/**
	 * @Method Name : pepperStagramList
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperStagramList() throws Exception {
		Box box = new Box();
		box.put("typeCd", "SN03");
		box.put("limit", 6);
		return dao.selectList("main.info.pepperPotoList", box);
	}

	/**
	 * @Method Name : pepperPotoList
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperPotoList() throws Exception {
		Box box = new Box();
		box.put("typeCd", "SN04");
		box.put("limit", 10);
		return dao.selectList("main.info.pepperPotoList", box);
	}

	/**
	 * @Method Name : pepperPlayerList
	 * @return
	 * @throws Exception
	 */
	public List<Box> pepperPlayerList() throws Exception {
		List<Box> box = new ArrayList<Box>();
		Box reqBox = new Box();
		reqBox.put("sBox", SessionUtil.getUserData());
		if(SessionUtil.getUserData() == null) {
			reqBox.put("chkUser", "N");
		}
		box = dao.selectList("main.info.playerList", reqBox);
		return box;
	}
//
//	/**
//	 * @Method Name : guestBookReadInsert
//	 * @param box
//	 * @return
//	 */
//	public int guestBookReadInsert (Box box) { // 방명록을 읽었을 경우 읽음 표시 api
//		dao.selectList("main.info.guestBookReadInsert", box);
//		return 1;
//	}
	//SNS API END


	/**
	 * @Method Name : notificationCnt
	 * @param box
	 * @return
	 */
	public Box notificationCnt(Box box) {
		Box resBox = new Box();

		box.put("sBox", SessionUtil.getUserData());
		int totCnt = dao.selectOne("main.info.notificationCnt" , box);
		resBox.put("notificationCnt", totCnt);
		return resBox;
	}

	/**
	 * @Method Name : notificationList
	 * @param box
	 * @return
	 */
	public  List<Box> notificationList(Box box) {
		box.put("sBox", SessionUtil.getUserData());
		List<Box> resBox = dao.selectList("main.info.notificationIdList" , box);
		if(resBox == null || resBox.isEmpty()) {
			throw new BizException("E107", new String[] {"알림 정보가"} ); //{0} 존재하지 않습니다.
		}
		for ( Box innerBox : resBox ) {
			innerBox.put("notification", dao.selectOne("main.info.notificationList",innerBox));
			innerBox.put("sender", dao.selectOne("main.info.senderView",innerBox.getBox("notification")));

		}
		return resBox;
	}

	/**
	 * @Method Name : popupEventView
	 * @param box
	 * @return
	 */
	public Box popupEventView(Box box) {
		return dao.selectOne("main.info.popupEventView", box);
	}
}
