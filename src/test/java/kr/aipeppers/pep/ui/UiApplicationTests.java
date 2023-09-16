package kr.aipeppers.pep.ui;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(properties = {
	"key=value"
})
@ActiveProfiles("local")
@Slf4j
@EnableEncryptableProperties
class UiApplicationTests {

	/**
	 * 프라퍼티 암호화를 위한 기본 stringEncryptor 1
	 */
	@Autowired
	private StringEncryptor lazyJasyptStringEncryptor;

	/**
	 * 프라퍼티 암호화를 위한 기본 stringEncryptor 2
	 */
	@Autowired
	private StringEncryptor jasyptStringEncryptor;


	/**
	 * Jasypt를 이요한 property 암호화 테스트
	 */
	@Test
	void propertyEncryptTest() {

		// ENC(암호화값) 에서 암호화 값을 생성하고자 하는 평문값을 입력
//		String plainString = "password1!";
		String plainString = "passw0rd1";

		//jasyptStringEncryptor는 커스텀 정의한 Bean이다.
		//lazyJasyptStringEncryptor는 커스텀 정의를 하든 말든(안하면 디폴트 bean 사용됨),
		// 그 encryptor를 wrapping한 encryptor이다.
		// 암호화는 매번 다르게 생성되는게 특징이므로, 암호화 결과를 비교하지 말고, 각각 암호화한걸, 서로 크로스해서 반대로 복호화 하면 정상 복호화도되고 결과도 같아야 한다.

		//암호화
		String enc1 = lazyJasyptStringEncryptor.encrypt(plainString);
		String enc2 = jasyptStringEncryptor.encrypt(plainString);

		//복호화 (암호화한 encryptor가 아닌 크로스해서 상대걸로 decrypt 해봄)
		String dec1 = jasyptStringEncryptor.decrypt(enc1);
		String dec2 = lazyJasyptStringEncryptor.decrypt(enc2);

		log.debug("enc1: {}", enc1);
		log.debug("enc2: {}", enc2);

		log.debug("dec1: {}", dec1);
		log.debug("dec2: {}", dec2);

		Assert.assertEquals(dec1, dec2);
	}

}
