package kr.aipeppers.pep.ui.category.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.DateUtil;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.crypto.AutoCrypto;
import kr.aipeppers.pep.core.util.mask.MaskUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;

/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@Service
public class EventService {

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	private Paginate paginate;

	@Autowired
	HttpSession session = SessionUtil.getSession();
	/**
	 * @Method Name : eventLuckyDrawView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> eventLuckyDrawView(Box box) throws Exception {
		int totCnt = (Integer)dao.selectOne("category.event.eventLuckyDrawCnt", box);
		paginate.init(box, totCnt);
		return dao.selectList("category.event.eventLuckyDrawList", box);
	}

	/**
	 * @Method Name : luckydrawDetailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box luckydrawDetailView(Box box) throws Exception {
		return dao.selectOne("category.event.luckydrawDetailView",box);
	}

	/**
	 * @Method Name : luckydrawInsert
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box luckydrawInsert(Box box) throws Exception {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		box.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
		Box luckyDView = dao.selectOne("category.event.luckydrawDetailView", box);// 럭키 드로우 테이블에서 해당 럭키 드로우 정보 조회 (이벤트 네임)
		if (!luckyDView.isEmpty()) {
			int luckyDAView = dao.selectOne("category.event.luckydrawApplyCnt", box);	// 럭키드로우 응모 테이블에서 응모 내역 조회 (이벤트 네임 , userId)
			if (luckyDAView < 1) {
					int resultLd = dao.insert("category.event.luckydrawInsert", box); // 럭키드로우 apply테이블 Insert
					if(resultLd > 0) { // 럭키드로우 응모가 제출 된 경우
						box.put("eventType", "LD"); // 럭키드로우 코드 값 insert
						box.put("eventDesc", luckyDView.get("eventName")); // 럭키드로우 이벤트 이름 추가 커밋
						dao.insert("category.event.eventApplyInsert", box); // 이벤트 apply 히스토리 테이블 Insert
						resBox.put("msg", "제출 완료되었습니다.");
					}
			} else {
				throw new BizException("E117", new String[] {"럭키드로우에"} ); //{0} 이미 신청하였습니다.
			}
		} else {
			throw new BizException("E107", new String[] {"해당 럭키드로우가"}); // {0}존재하지 않습니다.
		}
		return resBox;
	}

	/**
	 * @Method Name : defaultAttendList
	 * @param box
	 * @return
	 */
	public List<Box> defaultAttendList(Box box) {
		box.put("year", DateUtil.now(DateUtil.YEAR_PATTERN));
		box.put("month", DateUtil.now(DateUtil.MONTH_PATTERN));

		if(session.getAttribute(CmnConst.SES_USER_ID) == null || "".equals(session.getAttribute(CmnConst.SES_USER_ID))) { // 로그인 상태가 아닐 경우 userId 를 0 으로 세팅 하여 전체 조회 해오기
			box.put("userId", 0);
		} else {
			box.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
		}
		List<Box> list = dao.selectList("category.event.defaultAttendList", box); //  전체 출책 List 목록 조회
		if(list != null && !list.isEmpty() && list.size() > 0) {
			for (Box rowBox : list) { // 선물박스 (attendProductType) 확인 에 따른 Y,N 값 셋팅
				if(rowBox.eq("attendProductType", "PT03")) { // PT03 == 선물박스 ( 코드값 )
					rowBox.put("attendGiftOpen", 'Y'); //선물박스 확인
				} else {
					rowBox.put("attendGiftOpen", 'N'); // 선물박스 미확인
				}
			}
		} else {
			if(list.size() <= 0) {
				throw new BizException("E107", new String[] {"이벤트가"});  //{0} 존재하지 않습니다
			}
		}
		return list;
	}

	/**
	 * @Method Name : attendChkInsert
	 * @param box
	 * @return
	 */
	public Box attendChkInsert(Box box) {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		String toDate = DateUtil.now(DateUtil.DATE_DASH_PATTERN);
		if (toDate.equals(box.get("date"))) { // 다른 일자 출석체크 시 throw 진행
			Box setBox = dao.selectOne("category.event.attendSettingView",box); // 날짜 (파라미터 값) 로 attendSetting 값 조회
			if (setBox == null) { // attendSetting 값 null 체크
				throw new BizException("E107", new String[] {"attendSetting이"}); //{0} 존재하지 않습니다
			}
			resBox = this.attendGiftChk(setBox,box);	// 포인트 insert
			if (resBox.eq("attendProductType", "PT03")) { // 출책 체크
				Box pointBox = new Box();
				pointBox = resBox.getBox("attendGift");
				if(pointBox.getInt("point") > 0) { // 포인트가 있으면 포인트 추가
					setBox.put("attendPoint", pointBox.get("point"));
					setBox.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
					dao.update("category.event.usrPointUpdate", setBox); //사용자 포인트 수정
					setBox.put("userId", ""); // userId 초기화
					this.pointInsert(setBox, box);
				}
			} else if (resBox.eq("attendProductType", "!PT03")) {
				this.pointInsert(setBox, box);
			}
		} else {
			throw new BizException("E116"); // 다른 일자의 출석체크를 진행 할 수 없습니다.
		}
		return resBox;
	}
	/**
	 * @Method Name : attendanceRecipientInsert
	 * @param box
	 * @return
	 */
	public Box attendanceRecipientInsert(Box box) {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		Boolean result = false;
		int doubleChk = dao.selectOne("category.event.attendDoubleChk", box); // 등록된 수령 정보가 있는지 - attendId, user_id
		Box attendGiftId = dao.selectOne("category.event.attendGiftIdChk", box); // 등록된 선물 데이터가 있는지 체크 - attendId
		if(doubleChk > 0 ) { // 등록된 수령 정보 중복 체크
			throw new BizException("E140");
		}
		if(attendGiftId == null ) { // 등록된 선물데이터 존재 여부 체크
			throw new BizException("E141");
		} else {
			box.put("attendGiftId", attendGiftId);
		}
		box.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
		dao.insert("category.event.attendanceRecipientInsert", box);
		return resBox;
	}

	/**
	 * @Method Name : pointInsert
	 * @param setData
	 * @param box
	 * @return
	 */
	public Box pointInsert(Box setBox, Box box) {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		if(!setBox.isEmpty()) { // 포인트 테이블에 해당 내역 insert
			int pointCnt = dao.selectOne("category.event.pointChkCnt", box); // 날짜 (파라미터 값) 로 attendSetting 값 조회
			if(pointCnt > 0) {
				resBox.put("msg", "포인트가 이미 지급되었습니다.");
				resBox.put("code", "201");
				resBox.put("result", "fail");
			} else {
				setBox.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
				setBox.put("usePoint", 0);
				//			inserBox.put("pointType", setBox.get("pointType"));
				//			inserBox.put("descreption", setBox.get("descreption")); //추후 공통 코드화 사용 시 변경 로직
				dao.insert("category.event.pointInsert", setBox);
			}
		}
		return resBox;
	}
	/**
	 * @Method Name : attendGiftChk
	 * @param setData
	 * @param box
	 * @return
	 */
	public Box attendGiftChk(Box setBox, Box box) {
		/* 선물 type 별로 attendence_check insert
		 * 1. 포인트 , user 테이블에 user point update
		 * 2. 포츈쿠키 (랜덤 ) , user 테이블에 user point update
		 * 3. 선물박스( 랜덤 ) , user 테이블에 user point update
		 */
		Box resBox = new  Box();
		if (setBox.eq("attendProductType", "PT01")) {// 포인트 지급
			setBox.put("userId", session.getAttribute(CmnConst.SES_USER_ID)); // (추후) 섹션으로 넘어온 회원 아이디
			setBox.put("checkDate", box.get("date")); // 파라미터로 넘어온 출책 날짜
			setBox.put("pointType", "PS");
			setBox.put("description", "출석체크 포인트 적립");
			if((int)dao.selectOne("category.event.attendChk", setBox) > 0) { // 중복 출석체크 방어 로직
				throw new BizException("E117", new String[] {"출석체크 이벤트에"} ); //{0} 이미 신청하였습니다.
			}
			dao.insert("category.event.attendChkInsert", setBox); // attend_check  해당 이벤트 포인트 insert
			dao.update("category.event.usrPointUpdate", setBox); //사용자 포인트 수정
			resBox.put("attendId", dao.selectOne("category.event.attendIdView",box));

		} else if (setBox.eq("attendProductType", "PT02")) { //포츈 쿠키 정보 조회 , 포인트 지급
			// 포츈쿠키 데이터 랜덤으로 조회  및 resBox 에 값 세팅 (fortune_message, fortune_name)
			Box fortuneData = dao.selectOne("category.event.fortuneData");
			setBox.put("userId", session.getAttribute(CmnConst.SES_USER_ID)); // (추후) 섹션으로 넘어온 회원 아이디
			setBox.put("checkDate", box.get("date")); // 파라미터로 넘어온 출책 날짜
			setBox.put("fortuneMessageId", fortuneData.get("id")); // 포츈쿠키 메세지 아이디
			setBox.put("pointType", "PS");
			setBox.put("description", "출석체크 포인트 적립");
			if((int)dao.selectOne("category.event.attendChk", setBox) > 0) { // 중복 출석체크 방어 로직
				throw new BizException("E117", new String[] {"출석체크 이벤트에"} ); //{0} 이미 신청하였습니다.
			}
			dao.insert("category.event.attendChkInsert", setBox); // attend_check  해당 이벤트 포인트 insert
			dao.update("category.event.usrPointUpdate", setBox); //사용자 포인트 수정
			if (dao.selectList("category.event.attendIdView",box).size() > 0) { // 동일 check date 가 있을 경우 에러
				throw new BizException("E114"); // 이미 존재하는 데이터 입니다.
			}
			resBox.put("attendId", dao.selectOne("category.event.attendIdView",box));
			resBox.put("fortune_name", fortuneData.get("fortuneName"));
			resBox.put("fortune_message", fortuneData.get("fortuneMessage"));
		} else if (setBox.eq("attendProductType", "PT03")) { // 선물 박스 상품 조회 , 포인트 지급
			Box randomGiftData = this.randomGiftView(box);// 선물 정보 조회
			setBox.put("userId", session.getAttribute(CmnConst.SES_USER_ID)); // (추후) 섹션으로 넘어온 회원 아이디
			setBox.put("checkDate", box.get("date")); // 파라미터로 넘어온 출책 날짜
			setBox.put("attendGiftId", randomGiftData.get("id")); // 파라미터로 넘어온 출책 날짜
			setBox.put("pointType", "PS");
			setBox.put("description", "출석체크 포인트 적립");
			if((int)dao.selectOne("category.event.attendChk", setBox) > 0) { // 중복 출석체크 방어 로직
				throw new BizException("E117", new String[] {"출석체크 이벤트에"} ); //{0} 이미 신청하였습니다.
			}
			dao.insert("category.event.attendChkInsert", setBox); // attend_check  해당 이벤트 포인트 insert
			if(randomGiftData.getInt("attendPoint") != 0) { // 세팅 값에 포인트 추가 할 포인트가 있으면 사용자 point 에 추가
				box.put("attendPoint", randomGiftData.getInt("attendPoint"));
			}
			resBox.put("attendProductType", setBox.get("attendProductType"));
			resBox.put("attendId", dao.selectOne("category.event.attendIdView",box));
			resBox.put("attendGift", dao.selectOne("category.event.attendGiftView" ,randomGiftData));
		}
		return resBox;
	}

	/**
	 * @Method Name : randomGiftView
	 * @param box
	 * @return
	 */
	public Box randomGiftView(Box box) {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		String[] dateArry = box.nvl("date").split("-"); // 출책 클릭 날짜
		box.put("year", dateArry[0]); // 출책 클릭 year
		box.put("month", dateArry[1]); // 출책 클릭 month
		if(dao.selectList("category.event.giftBoxList", box).isEmpty()) { // 선물 제고 없을 시 에러
			throw new BizException("E107", new String[] {"상품 수량이"}); //{0} 존재하지 않습니다.
		}
		List<Box> resultList = dao.selectList("category.event.giftBoxList", box);  // db에서 조회 해온 물건명/수량/확률 list 데이터
		Float lvl = (float) (Math.random()* 100); // random 수의 100 곱해서 퍼센트 구하기

		for (Box rowBox : resultList) {
			if(lvl <= (Float) rowBox.get("rate")) { // lvl 값이 상품 확률 비교하여 찾기( 마지막 상품 전까지만 확인 )
				rowBox.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
				dao.update("category.event.giftOrderUpdate", rowBox);	// 당첨된 물품 수량 -1
				resBox.putAll(rowBox); // 당첨된 상품 정보
				return resBox;
			}
		}
		if(resBox.isEmpty()) { // 등수에 들지 못했을 경우 마지막 선물 등록
			resBox = resultList.get(resultList.size() -1);
		}
		resBox.put("userId", box.get("userId"));
		dao.update("category.event.giftOrderUpdate", resBox);	// 마지막 선물 등수 의 상품 목록 으로 수량 -1
		return resBox;
	}

	/**
	 * @Method Name : luckydrawWinView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> luckydrawWinView(Box box) throws Exception {
		box.put("eventId", box.get("event_id"));
		List<Box> winList = dao.selectList("category.event.luckydrawWinView", box);
		for(Box innerBox : winList) {
			if (innerBox.nvl("applyEmail") != null && !"".equals(innerBox.nvl("applyEmail"))) {
				innerBox.put("applyEmail", MaskUtil.maskEmail(AutoCrypto.seedDecrypt(innerBox.nvl("applyEmail")))); //seed암호화 된 데이터를 복호화 해준후 마스킹 해줘야 함.
			}
		}
		if (winList == null || winList.isEmpty()) {
			throw new BizException("E107", new String[] {"당첨자가 "}); //{0} 존재하지 않습니다
		}
		return winList;
	}

	/**
	 * @Method Name : luckydrawChk
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box luckydrawChk(Box box) throws Exception {
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null || "".equals(session.getAttribute(CmnConst.SES_USER_ID))) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		Box resBox = new Box();
		box.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
		int cnt = (dao.selectOne("category.event.luckydrawApplyCnt", box));
		if (cnt > 0) {
			throw new BizException("E117", new String[] {"럭키드로우 이벤트에"} ); //{0} 이미 신청하였습니다.
		}
		resBox.put("result", "응모 가능");
		return resBox;
	}
	/**
	 * @Method Name : isMyVote
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box isMyVote(Box box) throws Exception {
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null || "".equals(session.getAttribute(CmnConst.SES_USER_ID))) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		Box resBox = new Box();
		box.put("userId", session.getAttribute(CmnConst.SES_USER_ID));
		box.put("matchId", box.nvl("match_id"));
		Box result = dao.selectOne("category.event.isMyVote", box);
		if (result != null) {
			throw new BizException("E142");
		} else {
			resBox.put("player_id", null);
		}
		return resBox;
	}

	/**
	 * @Method Name : mvpEventInfoInsert
	 * @param box
	 * @return
	 */
	public Box mvpEventInfoInsert(Box box) throws Exception {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		Box matchExist = dao.selectOne("category.event.matchScheduleDetail", box); // 경기 있는지 - match_id
		if (!matchExist.isEmpty()) {
			Box mvpExist = dao.selectOne("category.event.mvpPlayerIsExist", box); // mvp 있는지 - match_id, user_id
			if(mvpExist != null) {
				// 업데이트
				Box setBox = new Box();
				setBox.put("phone", AutoCrypto.seedEncrypt(box.nvl("phone")));
				setBox.put("name", box.nvl("name"));
				setBox.put("isAgree", "Y");
				setBox.put("userId",  session.getAttribute(CmnConst.SES_USER_ID));
				setBox.put("matchId", box.nvl("matchId"));

				dao.update("category.event.mvpPlayerUpdate", setBox);
				//수훈선수 응모 히스토리 저장
				box.put("eventRelId", box.nvl("matchId"));
				box.put("userId",  session.getAttribute(CmnConst.SES_USER_ID));
				box.put("eventType", "VP");
				String matchDate = matchExist.nvl("matchDate");
				box.put("eventDesc", matchDate + " 경기");

				dao.insert("category.event.eventApplyHistoryInsert", box);


			} else {
				throw new BizException("E143");
			}

		} else {

		}
		return resBox;
	}
	/**
	 * @Method Name : postMatchVictoryInsert
	 * @param box
	 * @return
	 */
	public Box postMatchVictoryInsert(Box box) throws Exception {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}

		box.put("userId",  session.getAttribute(CmnConst.SES_USER_ID));
		// match_victory 테이블에서 이미 지원했는지확인
		int isExistMatchVictory = dao.selectOne("category.event.isExistMatchVictory", box); // user_is, match_id
		if (isExistMatchVictory > 0) {
			throw new BizException("E150");
		}

		// 경기가 end 생타래면 실패
		Box matchSchedule = dao.selectOne("category.event.matchScheduleDetail", box); // match_id
		if(matchSchedule.nvl("status").equals("MS03")) {
			throw new BizException("E151");
		}

		if(box.nvl("our").split(",").length != box.nvl("opp").split(",").length) {
			throw new BizException("E147");
		}

		if (Integer.parseInt(box.nvl("homeScore")) != 3 && Integer.parseInt(box.nvl("awayScore")) != 3) {
			throw new BizException("E149");
		}
		int result = dao.insert("category.event.matchVictoryInsert", box); // matchVictory에 insert


		if(result == 0) {
			throw new BizException("E146");
		}


		// 승부예측 응모 히스토리 저장
		box.put("eventRelId", box.nvl("matchId"));
		box.put("eventType", "MP");
		String matchDate = matchSchedule.nvl("matchDate");
		box.put("eventDesc", matchDate + " 경기결과");

		dao.insert("category.event.eventApplyHistoryInsert", box);



		return resBox;
	}
	/**
	 * @Method Name : getMatchVictory
	 * @param box
	 * @return
	 */
	public Box getMatchVictory(Box box) throws Exception {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		box.put("userId",  session.getAttribute(CmnConst.SES_USER_ID));
		box.put("matchId", box.nvl("match_id"));
		// 내예측
		resBox.put("matchVictory", dao.selectOne("category.event.getMatchVictory", box));
		// 경기스케줄
		resBox.put("matchSchedule", dao.selectOne("category.event.matchScheduleDetail", box));

		// predict_yn 업데이트
		String predictYn = "";
		if(resBox.getBox("matchVictory").nvl("homeScoreDetail").equals(resBox.getBox("matchSchedule").nvl("homeScoreDetail")) && resBox.getBox("matchVictory").nvl("awayScoreDetail").equals(resBox.getBox("matchSchedule").nvl("awayScoreDetail"))) {
			predictYn = "Y";
		} else {
			predictYn = "N";
		}
		box.put("predictYn", predictYn);
		box.put("id", resBox.getBox("matchVictory").nvl("id"));

		dao.update("category.event.updateMatchVictory", box);

		resBox.getBox("matchVictory").put("predictYn", predictYn);


		// 홈팀, 원정팀
		resBox.put("teamId", resBox.getBox("matchSchedule").nvl("hometeamId"));
		resBox.put("homeTeam", dao.selectOne("category.event.teamView", resBox)); //홈팀 정보 조회 및 세팅
		resBox.put("teamId", resBox.getBox("matchSchedule").nvl("awayteamId"));
		resBox.put("awayTeam", dao.selectOne("category.event.teamView", resBox)); //away 팀 정보 조회 및 세팅

		return resBox;
	}


/**
	 * @Method Name : matchVictoryInsertInfo
	 * @param box
	 * @return
	 */
	public Box matchVictoryInsertInfo(Box box) throws Exception {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		box.put("userId",  session.getAttribute(CmnConst.SES_USER_ID));
		int exist = dao.selectOne("category.event.matchGiftUserInfoIsExist", box); // user_id, match_id, victory_id
		int isVictory = dao.selectOne("category.event.matchVictoryIsExist", box); // victory_id, user_id
		int isMatch = dao.selectOne("category.event.matchScheduleIsExist", box); // match_id

		if(isMatch != 1) {
			throw new BizException("E137", new String[] {"경기를"});
		} else if(isVictory != 1) {
			throw new BizException("E137", new String[] {"응모 정보를"});
		} else if(exist >= 1) {
			throw new BizException("E154");
		}
		box.put("phone", AutoCrypto.seedEncrypt(box.nvl("phone")));
		dao.insert("category.event.matchVictoryInsertInfo", box);



		return resBox;
	}
	/**
	 * @Method Name : checkMatchVictoryApply
	 * @param box
	 * @return
	 */
	public Box checkMatchVictoryApply(Box box) throws Exception {
		Box resBox = new Box();
		//로그인 체크
		if(session.getAttribute(CmnConst.SES_USER_ID) == null) {
			throw new BizException("E101"); //로그인 정보가 존재하지 않습니다.
		}
		box.put("userId",  session.getAttribute(CmnConst.SES_USER_ID));
		int exist = dao.selectOne("category.event.matchGiftUserInfoIsExist", box); // user_id, match_id, victory_id

		if(exist > 0) {
			throw new BizException("E153", new String[] {"응모 내역이"});
		}

		return resBox;
	}


}
