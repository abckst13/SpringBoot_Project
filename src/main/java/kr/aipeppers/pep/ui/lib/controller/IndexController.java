package kr.aipeppers.pep.ui.lib.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.aipeppers.pep.core.cont.CmnConst;
import kr.aipeppers.pep.core.util.ConfigUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = {"＃기타>접근페이지"}, description = "IndexController")
@ApiIgnore
public class IndexController {

	@Value("${spring.application.name}")
	private String appName;

	/**
	 * index page
	 *
	 * @param box the box
	 * @param model the model box
	 */
	@ApiOperation(value="index page")
	@RequestMapping(value="/")
	@ResponseBody
	public String index() throws Exception {
		String idxStr = "BACKOFFICE";
		if (appName.equals(CmnConst.APP_NAME_UI)) { //UI일때
			idxStr = "UI";
		}

//		String subUrl = "";
//		if (!ConfigUtil.getProfile().equals("local")) {
//			subUrl = "/ui";
//		}
		return "<link rel=\"shortcut icon\" href=\"#\">PEPPER-" + idxStr + "-BACKEND<br /> <a href=\"/swagger-ui/index.html\">Go Swagger</a>";
	}
}

