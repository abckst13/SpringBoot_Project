package kr.aipeppers.pep.ui.lib.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.util.ConfigUtil;
import kr.aipeppers.pep.core.util.HttpUtil;
import kr.aipeppers.pep.ui.lib.service.BatchService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchComponent {

	@Autowired
	private BatchService batchService;

	/**
	 * 스케쥴 수행여부 체크
	 *
	 * @return
	 */
	public boolean isProc() {
		//Scheduled 수행여부가 'Y'로 설정되지 않을 경우 동작하지 않음
		if (!"Y".equals(ConfigUtil.getString("scheduled.yn"))) {
//			log.debug("스케쥴 수행여부가 Y로 설정되어 있지 않음");
			return false;
		}

		//Scheduled 수행서버ip와 현재 수행하는 머신의 ip가 일치하지 않을 경우 동작하지 않게함
		if (CmnConst.PROFILE_DEV.equals(ConfigUtil.getProfile())) {
			if (!HttpUtil.getLocalIpAddr().equals(ConfigUtil.getString("scheduled.server.ip"))) {
				log.debug("스케쥴이 수행될 머신의 ip 불일치 : " + HttpUtil.getLocalIpAddr() + ", " + ConfigUtil.getString("scheduled.server.ip"));
				return false;
			}
//		} else if ("prd".equals(profileMode)) {
//			String scheduledWasInstanceId = ConfigUtil.getString("scheduled.was.instanceId");;
//			if (scheduledWasInstanceId == null || !SystemUtil.getWasName().equals(scheduledWasInstanceId)) {
//				log.debug("인스턴스명 불일치 : " + SystemUtil.getWasName() + "," + scheduledWasInstanceId);
//				return false;
//			}
		}

		return true;
	}


	/**
	 *  첨부파일정리 스케쥴러
	 *  ex) 공지사항, 1:1문의등의 글이 삭제되었거나 임시저장상태에서 저장된 파일들을 일괄삭제한다.(하루전 데이터)
	 *
	 * @Scheduled(fixedDelay=5000, initialDelay = 1000)
	 * 이전 호출이 완료된 시점부터 고정적으로 5초마다 수행됨.
	 * 최초 수행시 에는 1초 딜레타임이 발생함.
	 *
	 * @Scheduled(fixedRate=5000)
	 * 연속적인 시작 시각의 간격으로 계산된 5초마다 수행됨.
	 * 이전 수행의 종료를 기다리지 않고 수행됨
	 *
	 * @Scheduled(cron="5 * * * * MON-FRI")
	 * cron 표현식을 이용한 주기적 수행.
	 *
		초 0-59 , - * /
		분 0-59 , - * /
		시 0-23 , - * /
		일 1-31 , - * ? / L W
		월 1-12 or JAN-DEC , - * /
		요일 1-7 or SUN-SAT , - * ? / L #
		년(옵션) 1970-2099 , - * /
		* : 모든 값
		? : 특정 값 없음
		- : 범위 지정에 사용
		, : 여러 값 지정 구분에 사용
		/ : 초기값과 증가치 설정에 사용
		L : 지정할 수 있는 범위의 마지막 값
		W : 월~금요일 또는 가장 가까운 월/금요일
		# : 몇 번째 무슨 요일 2#1 => 첫 번째 월요일

		예제) Expression Meaning
		초 분 시 일 월 주(년)
		 "0 0 12 * * ?" : 아무 요일, 매월, 매일 12:00:00
		 "0 15 10 ? * *" : 모든 요일, 매월, 아무 날이나 10:15:00
		 "0 15 10 * * ?" : 아무 요일, 매월, 매일 10:15:00
		 "0 15 10 * * ? *" : 모든 연도, 아무 요일, 매월, 매일 10:15
		 "0 15 10 * * ? : 2005" 2005년 아무 요일이나 매월, 매일 10:15
		 "0 * 14 * * ?" : 아무 요일, 매월, 매일, 14시 매분 0초
		 "0 0/5 14 * * ?" : 아무 요일, 매월, 매일, 14시 매 5분마다 0초
		 "0 0/5 14,18 * * ?" : 아무 요일, 매월, 매일, 14시, 18시 매 5분마다 0초
		 "0 0-5 14 * * ?" : 아무 요일, 매월, 매일, 14:00 부터 매 14:05까지 매 분 0초
		 "0 10,44 14 ? 3 WED" : 3월의 매 주 수요일, 아무 날짜나 14:10:00, 14:44:00
		 "0 15 10 ? * MON-FRI" : 월~금, 매월, 아무 날이나 10:15:00
		 "0 15 10 15 * ?" : 아무 요일, 매월 15일 10:15:00
		 "0 15 10 L * ?" : 아무 요일, 매월 마지막 날 10:15:00
		 "0 15 10 ? * 6L" : 매월 마지막 금요일 아무 날이나 10:15:00
		 "0 15 10 ? * 6L 2002-2005" : 2002년부터 2005년까지 매월 마지막 금요일 아무 날이나 10:15:00
		 "0 15 10 ? * 6#3" : 매월 3번째 금요일 아무 날이나 10:15:0
	 *
	 * @throws Exception
	 */
	public void fileDelBatch() throws Exception {
		if (!this.isProc()) { return; }
		Box box = new Box();
		Box model = new Box();
//		batchService.fileDel(box, model);
	}

	/**
	 * server alive 체크
	 * @throws Exception
	 */
//	@Scheduled(fixedDelay=60000)
//	public void serverAlive() throws Exception {
//
//		//개발계에서 수행되지 말것(TODO : 추후제거)
//		if (CmnConst.PROFILE_DEV.equals(ConfigUtil.getProfile())) {
//			return;
//		}
//
//		Box box = new Box();
//		Box model = new Box();
//		batchService.serverAlive(box, model);
//	}

}