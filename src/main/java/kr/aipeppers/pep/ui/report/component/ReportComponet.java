package kr.aipeppers.pep.ui.report.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.report.service.ReportService;
import lombok.extern.slf4j.Slf4j;
/**
 * 카테고리 > 이벤트
 * @author 김성태
 */
@Slf4j
@Component
public class ReportComponet {

	@Autowired
	protected ReportService reportService;

	public Box reportCommentSave(Box box) throws Exception  {

		return reportService.reportCommentSave(box);
	}

	public Box reportVideoSave(Box box)  throws Exception  {
		return reportService.reportVideoSave(box);
	}

	public Box reportUserSave(Box box) throws Exception  {
		return reportService.reportUserSave(box);
	}

	public Box reportPostSave(Box box)throws Exception  {
		return reportService.reportPostSave(box);
	}

}
