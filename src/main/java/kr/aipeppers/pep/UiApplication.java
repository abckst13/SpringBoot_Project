package kr.aipeppers.pep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.extern.slf4j.Slf4j;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
@EnableCaching
//@EnableRedisHttpSession
@EnableWebMvc
@Slf4j
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class})
public class UiApplication extends SpringBootServletInitializer {

	/**
	 * WAR 형태로 배포 시 SpringBootServletInitializer 에서 호출되는 콜백 메소드
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		log.info("{} WAR Application Starting...", this.getClass().getName());
		return UiApplication.configureApplication(builder, this.getClass());
	}

	/**
	 * JAR 형태로 배포 시 SpringBoot 애플리케이션이 시작되는 main 메소드
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("{} JAR Application Starting...", UiApplication.class.getName());
		SpringApplication application = UiApplication.configureApplication(new SpringApplicationBuilder(), UiApplication.class).build();
		application.addListeners(new ApplicationPidFileWriter()); // pid 파일 writer: ps 를 통해 pid를 찾을 수도 있지만, pid를 좀더 쉽게 찾도록 파일로 떨궈준다. (사실 kubectl 사용 환경에선 의미없다.)
		application.run(args);
	}

	/**
	 * WAR / JAR 배포 형태와 상관없이 공통된 초기화 로직을 타도록 초기화 로직을 아래와 같이 분리한다.
	 *
	 * <p>
	 * 사실상 현재는 지금 UiApplication 클래스 내에 별도의 설정이나 초기화 로직등이 들어가 있지 않아서 큰 의미 없지만, 혹여나
	 * 나중에 어떤 설정이 현재 클래스 내에 적용되어야 하는 상황이 올 경우에, 로컬 개발시(SpringBoot Dashboard를 통해 run을
	 * 하면 gradle 빌드스크립트 상관없이(WAR플러그인이여도) JAR모드로 동작하여 위의 main함수를 탄다)와 개발 배포시에 동일한 여기
	 * configure 메소드 로직을 타도록 하는 방법이다. 또한 나중에 위에서 뭔일이 벌어질지 모르기 때문에, 혹여나 embeded로
	 * 회귀되더라도 build.gradle에서 plugin 설정 변경만으로 스위칭이 가능함.
	 * </p>
	 *
	 * <p>
	 * 참고: 공식 가이드 문서 :
	 * https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html
	 * </p>
	 * If you intend to start your application as a war or as an executable
	 * application, you need to share the customizations of the builder in a method
	 * that is both available to the SpringBootServletInitializer callback and in
	 * the main method in a class similar to the following:
	 *
	 * @param builder
	 * @param applicationClazz
	 * @return
	 */
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder, Class<?> applicationClazz) {
		log.info("{} Application Configure!", applicationClazz.getName());

		// builder에 정의된 메소드를 체이닝 호출하여 다양한 configure 설정 적용이 가능
		// VM Arguments를 읽어와서 서버 환경에 따라 다른 설정을 먹인다던가 하는 것도 이 위치에서 가능.
		// 어떤 상황이 닥칠지 모르기 때문에, 솔루션 적용등.. 일단 이 위치 구멍을 만들어 놓음.
		return builder.sources(applicationClazz)
		// .bannerMode(Banner.Mode.OFF) // 애플리케이션 시작 시 로그 배너 OFF
		;
	}

}
