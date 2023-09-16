package kr.aipeppers.pep.ui.lib.service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.management.OperatingSystemMXBean;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ResIntDto;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.core.util.DateUtil;
import kr.aipeppers.pep.core.util.JwtUtil;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.SpringUtil;
import kr.aipeppers.pep.core.util.crypto.AutoCrypto;
import kr.aipeppers.pep.core.util.crypto.DigestUtil;
import kr.aipeppers.pep.core.util.mask.FilterUtil;
import kr.aipeppers.pep.core.util.mask.MaskUtil;
import kr.aipeppers.pep.core.util.sms.SMSComponent;
import kr.aipeppers.pep.ui.lib.domain.MainDomain.ResCdDto;
import kr.aipeppers.pep.ui.lib.domain.MainDomain.ResMenuDto;
import kr.aipeppers.pep.ui.lib.domain.MainDomain.ResMetaDto;
import kr.aipeppers.pep.ui.lib.domain.MainDomain.ResMsgDto;
import kr.aipeppers.pep.ui.lib.domain.MainDomain.ResSystemDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnonyService {

	@Value("${user.session.timeout}")
	private int sessionTimeout;

	@Autowired
	protected SqlSessionTemplate dao;

	@Autowired
	protected SMSComponent smsComponent;

	@Autowired
	protected CmnService cmnService;



	@Value("${spring.config.activate.on-profile}")
    private String ACTIVE;

//	String toDate = DateUtil.now(DateUtil.DATE_DASH_PATTERN);
	/**
	 * @Method Name : accessTokenInsert
	 * @return
	 */
	public Box accessTokenInsertView(Box box) {
		Box resBox = new Box();
		String tokenValue = "";
		tokenValue = DigestUtil.md5(DateUtil.now(DateUtil.TIME_HM_COLONE_PATTERN).getBytes()).substring(0,16);
		int result = dao.selectOne("anony.accessTokenView",tokenValue);
		if ( result == 0 ) {
			dao.insert("anony.accessTokenInsert", tokenValue);
		}
		resBox.put("accessToken", tokenValue);
		tokenValue = "";
		return resBox;
	}

	/**
	 * @Method Name : phoneChk
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box phoneChk(Box box) throws Exception {
		Box resBox = new Box();
		Box reqBox = new Box();
		Box accessTokenInfo = dao.selectOne("anony.accessInfoView", box);//accessToken 값 조회
		String decrypt = AutoCrypto.aesDecrypt(box.nvl("phone")); // 실제 로직
		String seedEncrypt = AutoCrypto.seedEncrypt(decrypt);
		log.debug("decryt: {}" ,decrypt);
		log.debug("seedEncrypt: {}" ,seedEncrypt);
		Box phoneNumBox = dao.selectOne("anony.phoneAuthChk", seedEncrypt); // 탈퇴 회원 인지 아닌지 조회

		if ( "".equals(box.nvl("type"))) {
			if (phoneNumBox != null) {
				throw new BizException("E134"); //이미 가입된 번호입니다.
			}
		}

		if ( "find".equals(box.nvl("type"))) {
			if (phoneNumBox == null) {
				throw new BizException("E122"); // 일치하는 회원정보가 없습니다.<br> 회원가입 후 이용해 주세요.
			}

			if(box.eq("type", "find") && phoneNumBox.eq("active", "0" , "2")) {
				throw new BizException("E121", new String[] {""}); // 탈퇴한 회원입니다.
			}
		}
		reqBox.put("phone", seedEncrypt);
		Box sendSmsChk = dao.selectOne("anony.sendSmsChk", reqBox);
		if (sendSmsChk != null ) {
			int sendSmsCnt = dao.selectOne("anony.sendSmsCnt", reqBox);
			if ( sendSmsCnt > 10 ) {
				throw new BizException("E139"); // 금일 전송 가능한 수를 초과하였습니다. <br> 내일 다시 시도해 주세요.
			}
		}

		if("local".equals(ACTIVE)) {
			String[] userPhone = new String[1]; // 핸드폰 번호 세팅
			userPhone[0] = decrypt; // 핸드폰 번호 세팅
			Box userAccessInfo = smsComponent.SendMsg(userPhone, "", "", 1); // SMS 인증 메소드
			if("발송완료".equals(userAccessInfo.nvl("result"))) {	// ACCESSTOKEN 값이 10분이 넘었을 경우 에러 로그 던짐 ( ACCESS_TOKEN 이 만료 되었습니다. )
				userAccessInfo.put("accessToken", accessTokenInfo.nvl("accessToken"));
				userAccessInfo.put("phone", seedEncrypt);
				dao.update("anony.userAccessUpdate",userAccessInfo);
				resBox.put("authCode", userAccessInfo.nvl("accessCd"));
			}else {
				throw new BizException("E118"); // 휴대폰 인증에 실패하였습니다.
			}
		} else if("dev".equals(ACTIVE)) {
			String[] userPhone = new String[1]; // 핸드폰 번호 세팅
			userPhone[0] = decrypt; // 핸드폰 번호 세팅
			Box userAccessInfo = smsComponent.SendMsg(userPhone, "", "", 1); // SMS 인증 메소드
			if("발송완료".equals(userAccessInfo.nvl("result"))) {	// ACCESSTOKEN 값이 10분이 넘었을 경우 에러 로그 던짐 ( ACCESS_TOKEN 이 만료 되었습니다. )
				userAccessInfo.put("accessToken", accessTokenInfo.nvl("accessToken"));
				userAccessInfo.put("phone", seedEncrypt);
				dao.update("anony.userAccessUpdate",userAccessInfo);
				resBox.put("authCode", userAccessInfo.nvl("accessCd"));
			}else {
				throw new BizException("E118"); // 휴대폰 인증에 실패하였습니다.
			}
		}else if("prod".equals(ACTIVE)) {
			dao.insert("anony.smsProdInsert", box); //
		}
		return resBox;
	}

	/**
	 * @Method Name : passwordUpdate
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int passwordUpdate(Box box) throws Exception {
		if(!MaskUtil.passwordChk(AutoCrypto.aesDecrypt(box.nvl("password")))) {
			throw new BizException("E123"); // password는 8 ~ 16자 이내로 영문, 숫자, 특수문자가 포함되어야 합니다.
		} else {
			box.put("password",  DigestUtil.sha256ToStr(CmnConst.PASSWORD_KEY+ AutoCrypto.aesDecrypt(box.nvl("password"))));
			box.put("email", AutoCrypto.seedEncrypt(AutoCrypto.aesDecrypt(box.nvl("email"))));
			dao.update("anony.passwordUpdate", box);
		}
		return 1;
	}

	/**
	 * @Method Name : maskEmailView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box maskEmailView(Box box) throws Exception {
		Box resBox = new Box();
		String aesDecrypt = AutoCrypto.aesDecrypt(box.nvl("email"));
		String seedEncrypt = AutoCrypto.seedEncrypt(aesDecrypt);
		box.put("aesEncrypt", seedEncrypt);
		Box resultBox = dao.selectOne("anony.emailNmView", seedEncrypt);
		resBox.put("email",MaskUtil.maskEmail(AutoCrypto.seedDecrypt(resultBox.nvl("email"))));
		resBox.put("username",resultBox.nvl("username"));
		return resBox;
	}

	/**
	 * @Method Name : phoneAuthChk
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box sendEnmailView(Box box) throws Exception {
		Box resBox = new Box();
		long accessTokenDt = 0;
		Box accessTokenInfo = dao.selectOne("anony.accessEmailInfoView", box);//accessToken 값 조회
		if (accessTokenInfo == null) {
			throw new BizException("E135", new String[] {"인증코드를"});// {0} 확인해주세요.
		}
		String decrypt = AutoCrypto.aesDecrypt(box.nvl("phone")); // 실제 로직
		String seedEncrypt = AutoCrypto.seedEncrypt(decrypt);
		accessTokenDt = DateUtil.parse(accessTokenInfo.nvl("accessCodeSendAt"), "yyyy-MM-dd HH:mm:ss").getTime();
		long nowDt = DateUtil.getDateTime().getMillis();
		long resultDt = nowDt - accessTokenDt;
		if(resultDt > 180000) { // 3분 세팅
			throw new BizException("E119"); //인증 시간이 지났습니다.
		} else {
			Box reqBox = new Box();
			reqBox.put("seedPhone", seedEncrypt);
			reqBox.put("accessToken", accessTokenInfo.nvl("accessToken"));
			reqBox.put("accessCode", accessTokenInfo.get("accessCode"));
			int confirmCnt = dao.selectOne("anony.verifyAuthCode", reqBox);//accessToken 값 조회
			if(confirmCnt> 0) {
				Box userBox = dao.selectOne("anony.userEmailView", reqBox);
				resBox.put("result", "인증이 완료되었습니다."); //회원가입 후 -> 아이디/패스워드 찾기 -> 인증 후  풀 이메일 정보 조회 후 data set 까지 필요.( 그 이후에 findEmail api 사용하여 패스워드 변경 )
				resBox.put("email", AutoCrypto.seedDecrypt(userBox.nvl("email")));
			} else {
				throw new BizException("E135", new String[] {"인증 번호를"}); //{0} 확인해 주세요.
			}
		}
		return resBox;
	}

	/**
	 * @Method Name : verifyAuthCode
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box confirmChk(Box box) throws Exception {
		Box resBox = new Box();
		long accessTokenDt = 0;
		Box accessTokenInfo = dao.selectOne("anony.accessInfoView", box);//accessToken 값 조회
		String decrypt = AutoCrypto.aesDecrypt(box.nvl("phone")); // 실제 로직
		String seedEncrypt = AutoCrypto.seedEncrypt(decrypt);
		accessTokenDt = DateUtil.parse(accessTokenInfo.nvl("accessCodeSendAt"), "yyyy-MM-dd HH:mm:ss").getTime();
		long nowDt = DateUtil.getDateTime().getMillis();
		long resultDt = nowDt - accessTokenDt;
		if(resultDt > 180000) { // 3분 세팅
			throw new BizException("E119"); //인증 시간이 지났습니다.
		} else {
			Box reqBox = new Box();
			reqBox.put("seedPhone", seedEncrypt);
			reqBox.put("accessToken", box.nvl("accessToken"));
			reqBox.put("accessCode", box.get("accessCode"));
			int confirmCnt = dao.selectOne("anony.verifyAuthCode", reqBox);//accessToken 값 조회
			if(confirmCnt> 0) {
			} else {
				throw new BizException("E135", new String[] {"인증 번호를"}); //{0} 확인해 주세요.
			}
		}
		return resBox;
	}

	/**
	 * @Method Name : duplUserNmChk
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public int usernameChk(Box box) throws Exception {
		String filter = cmnService.setFilterView();
		FilterUtil.koreanInitial(box.nvl("username")); // koreanInitial filter
		Boolean filterVal = FilterUtil.filterText(box.nvl("username"),filter); // description filter

		box.put("username", box.nvl("username"));
		int result = 0;
		if (filterVal) {
			int resultCnt = dao.selectOne("anony.duplChk", box);
			if(resultCnt <=  0) {
				result = 1;
			} else {
				throw new BizException("E130", new String[] {"닉네임"} ); // 이미 사용중인 닉네임입니다.
			}
		}
		return result;
	}

	/**
	 * @Method Name : duplIdChk
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box emailChk(Box box) throws Exception {
		Box resBox = new Box();
		Box reqBox = new Box();
		reqBox.put("email", AutoCrypto.aesDecrypt(box.nvl("email")));
		FilterUtil.checkEmailFormat(reqBox.nvl("email")); //  이메일 형식 filter
		FilterUtil.checkUpperCaseAfterAt(reqBox.nvl("email")); // 이메일 대문자 filter
		reqBox.put("email", AutoCrypto.seedEncrypt(AutoCrypto.aesDecrypt(box.nvl("email"))));
		int resultCnt = dao.selectOne("anony.duplChk", reqBox);
		if(resultCnt <= 0) {
			resBox.put("email", AutoCrypto.aesDecrypt(box.nvl("email")));
		} else {
			throw new BizException("E130" , new String[] {"아이디"}); // 이미 사용중인 {0}입니다.
		}
		return resBox;
	}

    /**
	 * @Method Name : tempJoinInsert
	 * @param box
	 * @return
	 */
	public Box joinInsert(Box box) throws Exception {
		Box resBox = new Box();
		if("".equals(box.get("email")) || box.get("email") == null){
			throw new BizException("E107", new String[] {"email"}); //{0} 존재하지 않습니다
		}
		if("".equals(box.get("phone")) || box.get("phone") == null){
			throw new BizException("E107", new String[] {"phone"}); //{0} 존재하지 않습니다
		}
		if("".equals(box.get("password")) || box.get("password") == null){
			throw new BizException("E107", new String[] {"password"}); //{0} 존재하지 않습니다
		}
		if("".equals(box.get("username")) || box.get("username") == null){
			throw new BizException("E107", new String[] {"username"}); //{0} 존재하지 않습니다
		}

		if ("".equals(box.nvl("accessToken")) || "".equals(box.nvl("authCode"))) {
			throw new BizException("E120"); // 인증내역이 없습니다.
		}

		// ACCESS 값이 10분이 안지나야함
		// ACCESS 토큰값 발행 > 핸드폰 인증 >  핸드폰 인증 정보값 ( ACCESS,인증코드, 폰 번호 ) 가 일치 하면 가입 진행
		long accessTokenDt = 0;
		Box accessTokenInfo = dao.selectOne("anony.accessInfoView", box);//accessToken 값 조회
		if (accessTokenInfo == null || accessTokenInfo.isEmpty()) {
			throw new BizException("E120"); // 인증내역이 없습니다.
		}
		String decryptPhone = AutoCrypto.aesDecrypt(box.nvl("phone")); // 실제 로직
		String seedEncrypt = AutoCrypto.seedEncrypt(decryptPhone);
		accessTokenDt = DateUtil.parse(accessTokenInfo.nvl("accessCodeSendAt"), "yyyy-MM-dd HH:mm:ss").getTime();
		long nowDt = DateUtil.getDateTime().getMillis();
		long resultDt = nowDt - accessTokenDt;
		if(resultDt > 180000) { // 3분 세팅
			throw new BizException("E119"); //인증 시간이 지났습니다.
		} else {
			String seedEmail = AutoCrypto.seedEncrypt(AutoCrypto.aesDecrypt(box.nvl("email")));	// 실제 데이터
			String seedPhone = AutoCrypto.seedEncrypt(AutoCrypto.aesDecrypt(box.nvl("phone"))); // 실제 데이터
			String sharpassword = DigestUtil.sha256ToStr(CmnConst.PASSWORD_KEY+ AutoCrypto.aesDecrypt(box.nvl("password")));

			box.put("seedPhone", seedPhone);
			int joinBox = dao.selectOne("anony.joinPhoneChk",box); // backEnd에서 회원가입 유무 한번더 체크
			if(joinBox > 0) {
				throw new BizException("E114", new String[] {"이미"}); // {0} 존재하는 데이터 입니다.
			}
			Box imgInfo = cmnService.userImageUpdate(box.nvl("profile_pic"));
			box.put("email", seedEmail);
			box.put("phone", seedPhone);
			box.put("password", sharpassword);
			box.put("active", 1);
			box.put("role", "user");
			box.put("point", 0);
			box.put("grade", "PM01");
			box.put("profilePic", imgInfo.nvl("profile_pic"));
			box.put("profilePicSmall", imgInfo.nvl("profile_pic_small"));
			int result = dao.insert("anony.userInsert", box);
			if (result > 0) {
				resBox.put("userId", dao.selectOne("anony.joinInfoUsrIdView", box));
			}
		}

		return resBox;
	}

	/**
	 * @Method Name : loginView
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box loginView(Box box) throws Exception {
		Box resBox = new Box();
		String sysFileNm = UUID.randomUUID().toString().replace("-", "");
		box.put("email", AutoCrypto.seedEncrypt(AutoCrypto.aesDecrypt(box.nvl("email"))));
		box.put("password", DigestUtil.sha256ToStr(CmnConst.PASSWORD_KEY+ AutoCrypto.aesDecrypt(box.nvl("password"))));

		if (Integer.parseInt(dao.selectOne("anony.lockCntChk", box)) > 5) { //fail cnt 6회 부터 로그인 lock
			throw new BizException("E127"); // 로그인 시도 가능 횟수를 모두 소진하였습니다.\n패스워드를 변경해 주세요.
		}

		Box userBox = dao.selectOne("anony.userView", box); //  이메일 패스워드로 로그인 진행
		if (!userBox.nvl("email").equals(box.nvl("email"))) {
			throw new BizException("E133"); //로그인 정보가 일치하지 않습니다. 아이디나 비밀번호를 확인 후 다시 입력해주세요.
		}

		if (!userBox.nvl("password").equals(box.nvl("password"))) {
			throw new BizException("E133");// 로그인 정보가 일치하지 않습니다. 아이디나 비밀번호를 확인 후 다시 입력해주세요.
		}

		if (userBox == null || userBox.isEmpty()) {	//사용자 조회 실패시
			throw new BizException("E122"); // 일치하는 회원정보가 없습니다.\n 회원가입 후 이용해 주세요.
		}

		/* 	device token , auth token 이 없을 경우
		 	기존 device token 과 파라미터 device token 이 다를 경우
		 */
		if("".equals(box.nvl("device_token")) && "".equals(userBox.nvl("deviceToken"))) { //divcieToken 값이 없을 경우 파라미터로 넘어온 deviceToken 값 update
			box.put("deviceToken", box.nvl("device_token"));
			dao.update("anony.deviceUpdate", box);
		}

		if (!box.nvl("device_token").equals(userBox.nvl("deviceToken"))) { // DEVICE TOKEN 값이 달라질 경우 UPDATE
			box.put("deviceTokenNew", box.nvl("device_token"));
			box.put("authToken", DigestUtil.sha256ToStr(sysFileNm));
			dao.update("anony.deviceUpdate", box);
			dao.update("anony.updateAuthToken", box);
		}

		if("".equals(userBox.nvl("authToken"))) { // auth token 이 없을 경우 update 하여 값 세팅
			box.put("authToken", DigestUtil.sha256ToStr(sysFileNm));
			dao.update("anony.updateAuthToken", box);
		}

		Box userInfoBox = dao.selectOne("anony.userView", box); // 변경된 값을 최종적으로 가져옴
		userInfoBox.put("email", AutoCrypto.seedDecrypt(userInfoBox.nvl("email")));

//		if ("Y".equals(box.nvl("check_yn"))) {
//			String result = autoLogin(userInfoBox);
//			if(result == "Y") {
//				resBox.put("result", "로그인이 완료되었습니다."); // 정보 조회가 완료 되면 resBox(결과 box) 에 result 값으로 로그인 성공 메세지 추가
//			} else {
//				throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
//			}
//		} else {
//			resBox.put("result", "로그인이 완료되었습니다."); // 정보 조회가 완료 되면 resBox(결과 box) 에 result 값으로 로그인 성공 메세지 추가
//		}

		HttpSession session = SessionUtil.getSession();
		if (!session.isNew()) {
			session.invalidate();
			session = SessionUtil.getSession();
		}
		session.setAttribute(CmnConst.SES_USER_ID, userInfoBox.nvl("id"));
		session.setAttribute(CmnConst.SES_DEVICE_TOKEN, userInfoBox.nvl("deviceToken"));
		session.setAttribute(CmnConst.SES_AUTH_TOKEN, userInfoBox.nvl("authToken"));
		session.setAttribute(CmnConst.SES_USER_EMAIL, AutoCrypto.seedEncrypt(userInfoBox.nvl("email")));
//		session.setAttribute(CmnConst.SES_USER_ID, userBox.nvl("deviceToken"));
		session.setAttribute(CmnConst.SES_USER_DATA, userInfoBox);
		session.setMaxInactiveInterval(sessionTimeout * 60); //기준 : sec

		resBox.put("user",userInfoBox);

		return resBox;
	}

	/**
	 * @Method Name : updateFailCnt
	 * @param box
	 * @throws Exception
	 */
	public void updateFailCnt(Box box) throws Exception {
		box.put("email", AutoCrypto.seedEncrypt(AutoCrypto.aesDecrypt(box.nvl("email"))));
		dao.update("anony.updateFailCnt",box); // fail cnt update
	}

	/**
	 * @Method Name : autoLogin
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public String autoLogin(Box box) throws Exception {
		String result = "";

		int userInfo = dao.selectOne("anony.autoLoginChk", box);
		if (userInfo > 0) {
			result = "Y";
		} else {
			result = "N";
		}
		return result;
	}


	/**
	 * 로그아웃
	 * @return
	 * @throws Exception
	 */
	public int tempLogout() throws Exception {
//		Box box = new Box();
//		box.put("userId", SessionUtil.getUserId());
//		box.put("reqIp", HttpUtil.getRemoteIpAddr());
//		UserAgentUtil userAgentUtil = new UserAgentUtil();
//		box.put("userAgent", userAgentUtil.getUserAgent());
//		box.put("reqType", "LOGOUT");
//		log.debug("box: {}", box);
//		if (!"".equals(box.nvl("userId"))) { //userId가 세션에 존재할때만 로그를 쌓는다
//			dao.insert("anony.userHistInsert", box); //로그아웃 로그를 쌓는다.
//			dao.update("anony.lastAccUpdate", box); //최종접속일시 update
//		}

		HttpSession session = SessionUtil.getSession();
		session.invalidate();
		return 1;
	}

	/**
	 * @Method Name : sessionChk
	 * @return
	 */
	public void sessionChk() {
		Box resBox = new Box();
		HttpSession session = SessionUtil.getSession();
//		log.debug("session value: {}", session);
//		log.debug("session value: {}", session.getAttribute(CmnConst.SES_USER_ID));
	}

	/**
	 * @Method Name : meta
	 * @return
	 */
	public ResMetaDto meta() {
		HttpServletRequest httpRequest = SpringUtil.getHttpServletRequest();
		String authHeader = httpRequest.getHeader(CmnConst.HTTP_HEADER_AUTHORIZATION);

		if (null == authHeader || "".equals(authHeader)) {
//			throw new BizException("E109"); //유효하지 않은 접근입니다.
			throw new BizException("E100"); //사용자 정보가 존재하지 않습니다.
		}
		authHeader = authHeader.substring(authHeader.indexOf("Bearer") + 7, authHeader.length());

		Integer authNo = Integer.parseInt(JwtUtil.decodeTokenBox(authHeader).nvl("authNo"));
		String memNm = JwtUtil.decodeTokenBox(authHeader).nvl("memNm");

		List<ResCdDto> cdList = BoxUtil.toDtoList(dao.selectList("anony.cdAllList"), ResCdDto.class);
		List<ResMsgDto> msgList = BoxUtil.toDtoList(dao.selectList("anony.msgAllList"), ResMsgDto.class);
		List<ResMenuDto> menuList = BoxUtil.toDtoList(dao.selectList("anony.menuAllList", new Box(){{put("authNo", authNo);}}), ResMenuDto.class);

		if(menuList.size() == 0) {
			throw new BizException("E110"); //메뉴 권한이 없습니다.
		}
		return new ResMetaDto(cdList, msgList, menuList, memNm);
	}

	/**
	 * @Method Name : systemInfo
	 * @return
	 * @throws Exception
	 */
	public ResSystemDto systemInfo() throws Exception {
		long cpu = 0;
		long memory = 0;
		long disk = 0;
		OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		cpu = (long) (osBean.getSystemCpuLoad() * 100);
		memory = (long) (((double)(osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize()) / (double)osBean.getTotalPhysicalMemorySize()) * 100);

//		Runtime runtime = Runtime.getRuntime();

		File file = new File("/");
		disk = (long) (((double)(file.getTotalSpace() - file.getFreeSpace()) / (double)file.getTotalSpace()) * 100);
		if (cpu < 0) { cpu = 0;}
		if (memory < 0) { memory = 0;}
		if (disk < 0) { disk = 0;}

		log.debug("osBean.getSystemCpuLoad:" + osBean.getSystemCpuLoad());
		log.debug("osBean.getTotalPhysicalMemorySize:" + osBean.getTotalPhysicalMemorySize());
		log.debug("osBean.getFreePhysicalMemorySize:" + osBean.getFreePhysicalMemorySize());
//		log.debug("runtime.freeMemory:" + (runtime.freeMemory() / 1048576));
//		log.debug("runtime.totalMemory:" + (runtime.totalMemory() / 1048576));
//		log.debug("runtime.maxMemory:" + (runtime.maxMemory() / 1048576));
		log.debug("file.getFreeSpace:" + file.getFreeSpace());
		log.debug("file.getTotalSpace:" + file.getTotalSpace());
		return new ResSystemDto(cpu, memory, disk);
	}

	/**
	 * @Method Name : aecEncrypt
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public Box aecEncrypt(Box box) throws Exception {
		Box resBox = new Box();
		String aesEncrypt = AutoCrypto.aesEncrypt(box.nvl("text"));
		String seedEncrypt = AutoCrypto.seedEncrypt(box.nvl("text"));
		resBox.put("aesEncrypt", aesEncrypt);
		resBox.put("seedEncrypt", seedEncrypt);
		log.debug("ACTIVE: {}", ACTIVE);
		return resBox;
	}

	/**
	 * @Method Name : seedEncrptyInfo
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box seedEncrptyInfo(Box box) throws Exception {
		Box resBox = new Box() ;
		log.debug("seedDecrypt: {}", AutoCrypto.seedDecrypt(box.nvl("text")));
		resBox.put("seedDecrypt", AutoCrypto.seedDecrypt(box.nvl("text")));

		return resBox;
	}

	/**
	 * @Method Name : apiVersionChk
	 * @param box
	 * @return
	 */
	public Box apiVersionChk(Box box) {
		Box resBox = new Box();
		box.put("appType", box.nvl("app_type"));
		resBox = dao.selectOne("anony.apiVersionChk", box);
		return resBox;
	}

	/**
	 * @Method Name : withdrawUserUpdate
	 * @param box
	 * @return
	 */
	public Box withdrawUserUpdate(Box box) {
		Box resBox = new Box();
		box.put("sBox", SessionUtil.getUserData());
		if (box.getBox("sBox") ==  null ) {
			throw new BizException(""); // 사용자 정보가 없습니다.
		}

		if ("".equals(box.nvl("active_reason"))) { //  탈퇴 사유 체크값이 없을 시
			throw new BizException("E135", new String[] {"탈퇴 사유를"}); // {0} 확인해주세요.
		}

		String[] arrStr = box.nvl("active_reason").split(",");
		for (String text : arrStr) { // 탈퇴 사유가 "기타" 일 때 상세 내용 필수 값 체크
			if (text.equals("RES05")) {
				if (box.nvl("active_reason_detail") == "") {
					throw new BizException("E135", new String[] {"기타 사유를"}); // {0} 확인해주세요.
				}
			}
		}

		int resultView = dao.selectOne("anony.userDrawChk", box); // 회원 검증
		box.put("password",  DigestUtil.sha256ToStr(CmnConst.PASSWORD_KEY+ AutoCrypto.aesDecrypt(box.nvl("password")))); // 비밀번호
		int passwordView = dao.selectOne("anony.passwordChk", box); // 패스워드를 확인
		if ( resultView == 0 ) {
			throw new BizException("E136", new String[] {"이미 탈퇴 된"}); // {0} 회원입니다.
		}
		if(passwordView == 0 ) {
			throw new BizException("E135", new String[] {"패스워드를"}); // {0} 확인해 주세요.
		}
		dao.update("anony.drawUpdate", box);
		resBox.put("resultMsg", "회원 탈퇴가 완료되었습니다.");
		return resBox;
	}
}
