package kr.aipeppers.pep.ui.sample.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.domain.ClientApiDto;
import kr.aipeppers.pep.core.domain.ResCmnMsgDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.core.util.ConfigUtil;
import kr.aipeppers.pep.core.util.HttpClient;
import kr.aipeppers.pep.core.util.JsonUtil;
import kr.aipeppers.pep.core.util.crypto.AesBasisUtil;
import kr.aipeppers.pep.core.util.crypto.AesUtil;
import kr.aipeppers.pep.ui.sample.domain.MsgDomain.ResMsgDto;
import kr.aipeppers.pep.ui.sample.service.TestService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("test")
@RestController
@RequiredArgsConstructor
//@ApiIgnore
@Api(tags = {"＃샘플>개발 참고용 테스트 샘플"}, description = "TestController")
public class TestController {

	@Autowired
	private TestService testService;

	@GetMapping("test1/{key}")
	@ApiOperation(value="테스트페이지", notes="객체를 테스트합니다")
	@ApiImplicitParams({
		@ApiImplicitParam(name="key", value="url을 두번째 path입니다. key의 의미입니다", paramType="path"),
		@ApiImplicitParam(name="id", required=true, value="고유아이디", paramType="query"),
		@ApiImplicitParam(name="email", value="이메일", paramType="query"),
	})
	public ResponseEntity<ResUserDto> test1(HttpServletRequest request, @PathVariable("key") String key, @ModelAttribute ReqUserDto reqDto) throws Exception {
//		Box box = new Box(request);
//		log.info("box1:"+box);

		Box rqtHeaderBox = new Box();
		rqtHeaderBox.put("Authorization", "Basic NWMxYTc5OTItOWQ3Ni00NDllLWJmNGYtY2IxNDJmYWQyNzJlOmI3NWZjYmRmODBlYjRkMGJiYmJm");
		rqtHeaderBox.put("Content-Type", "application/x-www-form-urlencoded");

		Box box = new Box();
		box.put("page", "2");
		box.put("pageUnit", "15");
		box.put("srchMsgKndC", "E");

		ClientApiDto clientApiDto = new ClientApiDto();
		clientApiDto.setRqtParamBox(box);
		//clientApiDto.setRqtContent(box.toString());
		clientApiDto.setRqtHeaderBox(rqtHeaderBox);
		clientApiDto.setScheme("http");
//		clientApiDto.setHost("ailab.sorizava.co.kr");
//		clientApiDto.setHost("40.1.16.43");
		clientApiDto.setHost("localhost");
		clientApiDto.setPort(8052);
		clientApiDto.setTimeout(30000);
		clientApiDto.setPath("msg/msgList");
//		clientApiDto.setPath("wcms/footer/personal/UHPPCO0802D0.html");
		HttpClient.sendPost(clientApiDto);
		if (null == clientApiDto.getException() && "1".equals(clientApiDto.getRpyContent().split("\\|")[0])) { //성공시
			Box resBox = BoxUtil.toBox(clientApiDto.getRpyContent());
			String resJsonStr = JsonUtil.toJson(resBox.get("list"));
			log.debug(">>>>>" + resJsonStr);
//			ResListDto<ResMsgDto> resDto = JsonUtil.toObject(ClientApiDto.getRpyContent(), new TypeReference<ResListDto<ResMsgDto>>(){});
			List<ResMsgDto> resDto = BeanUtil.convert(resJsonStr, new TypeReference<List<ResMsgDto>>(){});
			log.info("res: " + resBox.nvl("resultCnt"));
			log.info("list: " + resBox.getPathString("list[3]/msgKrnCn"));
			log.info("list.resMsg: " + resBox.getPath("list[3]"));
//			log.info("resDto: " +  resDto);
			log.info("resDto.list[1]: " +  resDto.get(1));
			log.info("resDto.list[2].getMsgType: " +  resDto.get(2).getMsgType());
		} else {
			log.debug("error: {}", clientApiDto.getException().getMessage());
			throw new BizException("F600", new String[]{"dummy-backend"}); //{0} 연동 오류
		}


		UUID jobId = UUID.randomUUID();
		ResUserDto resUserDto = new ResUserDto();
//		resUserDto.setEmail("ourage@daum.net");
//		resUserDto.setId(jobId);
//		resUserDto.setKeycloakId(key);
		resUserDto.setEmail(reqDto.getEmail()+"!!!");
		resUserDto.setId(reqDto.getId());
		resUserDto.setKeycloakId(key);

//		Map map = new HashMap();
//
//		List list = new ArrayList<Map>();
//		Map rowMap = new HashMap();
//		rowMap.put("inRow", "111");
//		list.add(rowMap);
//
//		rowMap = new HashMap();
//		rowMap.put("inRow", "222");
//		list.add(rowMap);
//
//		rowMap = new HashMap();
//		rowMap.put("inRow", "333");
//		list.add(rowMap);
//
//		map.put("rowMap", rowMap);
//		map.put("list", list);
//		map.put("aaa", 111);
//		map.put("jobId", jobId);

		log.debug("jobId: {}", jobId);
//		return ResponseEntity.accepted().build();
//		return ResponseEntity.status(HttpStatus.OK)
//				.body("Some files were rejected because of a wrong sample rate: ");
		return new ResponseEntity<>(resUserDto, HttpStatus.OK);
	}

	@PostMapping(value = "test2")
	@ApiOperation(value="연동 테스트")
	public ResponseEntity<ResUserDto> test2(@RequestBody ReqPhpDto reqDto) throws Exception {
		Box rqtHeaderBox = new Box();
		rqtHeaderBox.put("api-key", "156c4675-9608-4591-2022-08161");
		rqtHeaderBox.put("Content-Type", "application/json");

		Box box =  BoxUtil.toBox(reqDto);
		ClientApiDto clientApiDto = new ClientApiDto();
		clientApiDto.setRqtHeaderBox(rqtHeaderBox);
//		clientApiDto.setRqtParamBox(box);
		clientApiDto.setRqtContent(JsonUtil.toJson(box));
		clientApiDto.setScheme("https");
//		clientApiDto.setHost("aipeppers-app.com");
		clientApiDto.setHost("aipeppers-api.pepperbank.kr");
		clientApiDto.setPort(443);
		clientApiDto.setTimeout(30000);
//		clientApiDto.setPath("mobileapp_api/admin/login");
//		clientApiDto.setPath("admin/login");
		clientApiDto.setPath("index.php");
		HttpClient.sendPost(clientApiDto);
		if (null == clientApiDto.getException()) { //성공시
//			log.debug(">>>>>" + clientApiDto.getRpyContent());
		} else {
			log.debug("error: {}", clientApiDto.getException().getMessage());
			throw new BizException("F600", new String[]{"dummy-backend"}); //{0} 연동 오류
		}

		ResUserDto resUserDto = new ResUserDto();
		return new ResponseEntity<>(resUserDto, HttpStatus.OK);
	}

	@ApiOperation(value="jwt 토큰 발급")
	@PostMapping("createToken")
	public void createToken() throws Exception  {
//		log.debug(">>>>>>>>" + MsgUtil.getMsg("F503"));
//		log.debug(">>>>>>>>" + MsgUtil.getMsgBox("F503"));
		Box headerBox = new Box();
		headerBox.put("typ", "JWT");
		headerBox.put("alg", "HS256");
		Box claimBox = new Box();
		LocalDateTime ldt = LocalDateTime.now();
		long now = ldt.toDate().getTime();
		long exp = ldt.plusSeconds(60).toDate().getTime();
		log.debug("now:"+now);
		log.debug("exp:"+exp);
		claimBox.put("now", now);
		claimBox.put("exp", exp);
		claimBox.put("userId", "abc");

		String jwt = Jwts.builder()
				.setHeader(headerBox)
				.setClaims(claimBox)
				.setExpiration(new Date(exp))
				.signWith(SignatureAlgorithm.HS256, "A".getBytes())
				.compact();
		log.debug("jwt 토큰: {}", jwt);
	}

	@ApiOperation(value="jwt 토큰 디코딩")
	@PostMapping("jwtDecode")
	public void jwtDecode(@ModelAttribute ReqJwtDecodeDto reqDto) throws Exception {
		Claims claims = Jwts.parser()
				.setSigningKey("A".getBytes())
				.parseClaimsJws(reqDto.getJwtToken())
				.getBody();
		log.debug("now:" + claims.get("now", Date.class));
		log.debug("exp:" + claims.get("exp", Date.class));
		log.debug("memId:" + claims.get("memId"));
		log.debug("memNm:" + claims.get("memNm"));
	}

	@ApiOperation(value="암호화 테스트")
	@PostMapping("encode")
	public void encode(@ModelAttribute ReqEncodeDto reqDto) throws Exception {
		log.debug("server.system.name: {}", ConfigUtil.getString("server.system.name"));
		log.debug("spring.config.activate.on-profile: {}", ConfigUtil.getString("spring.config.activate.on-profile"));

		log.debug("encodeKey:" + reqDto.getEncodeKey());
//		String encodeData = AesOldUtil.encryptString(reqDto.getData(), reqDto.getEncodeKey());
//		String decodeData = AesOldUtil.decryptString(encodeData, reqDto.getEncodeKey());
//		log.debug("encodeData:" + encodeData);
//		log.debug("decodeData:" + decodeData);

		String encodeData =  AesUtil.encrypt(reqDto.getData(), reqDto.getEncodeKey());
		String decodeData = AesUtil.decrypt(encodeData, reqDto.getEncodeKey());
		log.debug("encodeData:" + encodeData);
		log.debug("decodeData:" + decodeData);

		encodeData = AesUtil.encrypt(reqDto.getData());
		decodeData = AesUtil.decrypt(encodeData);
		log.debug("encodeData:" + encodeData);
		log.debug("decodeData:" + decodeData);

	}

	@Data
	@ApiModel
	public static class ReqJwtDecodeDto {
		@Schema(description = "jwt 토큰", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJub3ciOjE1OTg4NTI2MTI2MjYsImV4cCI6MTU5ODg1MjY3MiwidXNlcklkIjoiYWJjIn0.mpLlWyHa1FtRTg904hofJLNGKeS6gPtZRRf_OyzGHhY")
		private String jwtToken;
	}

	@Data
	@ApiModel
	public static class ReqEncodeDto {

		@Schema(description = "암호데이터", example="aa")
		private String data;
		@Schema(description = "암호키", example="1234123412341234")
		private String encodeKey;
	}

	@ApiOperation(value="디아모 암복호화 테스트")
	@PostMapping(value = "damoCryptTest")
	public  ResDecodeDto2 damoCryptTest(@ModelAttribute ReqEncodeDto2 reqDto) throws Exception {

		Box box = BoxUtil.toBox(reqDto);
		log.debug("encrypte::::{}", box);

		log.debug("decrypted res ↓↓↓");
		return BeanUtil.convert(box, ResDecodeDto2.class);
	}

	@ApiOperation(value="트렌젝션 테스트")
	@PostMapping(value = "txTest")
	public ResResultDto<ResCmnMsgDto> test() throws Exception {
		testService.testInsert();
		return new ResResultDto<ResCmnMsgDto>();
	}


	@Data
	@ApiModel
	public static class ReqEncodeDto2 {

		@Schema(description = "암호데이터", example="aa")
		private String data;
		@Schema(description = "암호키", example="1234123412341234")
		private String encodeKey;
	}

	@Data
	@ApiModel
	public static class ResDecodeDto2 {

		@Schema(description = "복호데이터", example="aa")
		private String data;
		@Schema(description = "복호키", example="1234123412341234")
		private String encodeKey;
	}

	@Data
	@ApiModel
	public static class ReqUserDto {
		@Schema(description = "아이디", example="11", required=true)
		private int id;
		@Schema(description = "float 테스트")
		private float xx;
		@Schema(description = "boolean 테스트")
		private boolean flag;
		@Schema(description = "date 테스트")
		private Date today;
		@Schema(description = "키클락 아이디")
		private String keycloakId;
		@Schema(description = "이메일", example="test@aa.com")
		private String email;
	}

	@Data
	@ApiModel
	public static class ReqPhpDto {
		@Schema(description = "email형식 아이디", example="admin@admin.com", required=true)
		private String email;
		@Schema(description = "암호", example="qwer09!@", required=true)
		private String password;
		@Schema(description = "역할", example="admin", required=true)
		private String role;
	}

	@Data
	@ApiModel
	public static class ResUserDto {
		@Schema(description = "아이디")
		private int id;
		@Schema(description = "float 테스트")
		private float xx;
		@Schema(description = "boolean 테스트")
		private boolean flag;
		@Schema(description = "date 테스트")
		private Date today;
		@Schema(description = "키클락 아이디")
		private String keycloakId;
		@Schema(description = "이메일")
		private String email;
	}
}


