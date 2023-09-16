package kr.aipeppers.pep.ui.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.aipeppers.pep.core.annotation.ReqInfo;
import kr.aipeppers.pep.core.domain.ResListDto;
import kr.aipeppers.pep.core.domain.ResResultDto;
import kr.aipeppers.pep.core.util.BeanUtil;
import kr.aipeppers.pep.core.util.BoxUtil;
import kr.aipeppers.pep.ui.category.component.MenuComponent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/restapi/category/info")
@Api(tags = {"카테고리>menu"}, description = "MenuController")
public class MenuController {

	@Autowired
	protected MenuComponent menuCompo;


	/**
	 * @Method Name : searchMenu
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="카테고리 메뉴 조회")
	@PostMapping(value = "searchMenu")
	public ResResultDto<ResUICAIF004Dto> searchMenu(@RequestBody ReqUICAIF004Dto reqDto) throws Exception {
		return new ResResultDto<ResUICAIF004Dto>(BeanUtil.convert(menuCompo.menuList(BoxUtil.toBox(reqDto)), ResUICAIF004Dto.class));
	}


	//============================================ reqDto ==============================================================

	@Data
	@ApiModel
	public static class ReqUICAIF004Dto {

	}

	//============================================ resDto ==============================================================

	@Data
	@ApiModel
	public static class ResUICAIF004Dto {
		@JsonProperty("Menu")
		@JsonAlias("menu")
		private List<Menu> menu;
		@JsonProperty("User")
		@JsonAlias("user")
		private User user;

		@Data
		@ApiModel
		public static class Menu {
			@JsonProperty("Child")
			@JsonAlias("child")
			private List<Child> child;

			@Schema(description = "그룹코드")
			@JsonAlias("gnbCode")
			private String gnb_code;
			@Schema(description = "그룹코드 이름")
			@JsonAlias("gnbName")
			private String gnb_name;
			@Schema(description = "그룹코드 URL")
			@JsonAlias("gnbUrl")
			private String gnb_url;

			@Data
			@ApiModel
			public static class Child {
				@Schema(description = "그룹코드")
				@JsonAlias("gnbCode")
				private String gnb_code;
				@Schema(description = "그룹코드 이름")
				@JsonAlias("gnbName")
				private String gnb_name;
				@Schema(description = "그룹코드 URL")
				@JsonAlias("gnbUrl")
				private String gnb_url;
				@Schema(description = "그룹코드 설명")
				private String description;
			}
		}

		@Data
		@ApiModel
		public static class User {
			@Schema(description = "사용자 아이디")
			private String id;
			@Schema(description = "사용자 이름")
			private String username;
			@Schema(description = "사용자 프로필 이미지")
			@JsonAlias("profilePic")
			private String profile_pic;
			@Schema(description = "사용자 작은 프로필 이미지")
			@JsonAlias("profilePicSmall")
			private String profile_pic_small;
			@Schema(description = "포인트")
			private String point;
			@Schema(description = "등급코드")
			private String grade;
			@Schema(description = "등급코드 명")
			@JsonAlias("gradeNm")
			private String grade_nm;
			@Schema(description = "이벤트 응모 CNT")
			@JsonAlias("eventApplyCnt")
			private String event_apply_cnt;
		}
	}
}
