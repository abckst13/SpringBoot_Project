package kr.aipeppers.pep.ui.report.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.core.exception.BizException;
import kr.aipeppers.pep.core.util.SessionUtil;
import kr.aipeppers.pep.core.util.paginate.Paginate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReportService {

	@Autowired
	protected SqlSessionTemplate dao ;

	@Autowired
	private Paginate paginate;

	public Box reportCommentSave(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		//컨트롤러에서 널체크 해서 널 비교 x
		String type = box.nvl("type").toString();
		if(!(type.equals("post_comment") || type.equals("post_comment_reply") || type.equals("video_comment") || type.equals("video_comment_reply") )) {
			throw new BizException("F112", new String[] {"type 올바른 값을 입력해주세요."} );
		}

		int chk = 0;
		//댓글잇는지 확인
		if(type.equals("post_comment") || type.equals("post_comment_reply")) {	//포스트
			chk = dao.selectOne("report.postCommentChk", box);
		}else {	//비디오
			chk = dao.selectOne("report.videoCommentChk", box);
		}

		String contentId = "";
		// 댓글잇는지 탈퇴한지 체크
		if(type.equals("post_comment") || type.equals("post_comment_reply")) {	//포스트
			Box post = new Box();
			if(type.equals("post_comment")){
				post = dao.selectOne("report.postCommentSelect", box);
			}else {
				post = dao.selectOne("report.postCommentReplySelect", box);
			}

			if(post == null ) {
				throw new BizException("E107", new String[] {"댓글이 "}); // {0} 존재하지 않습니다
			}

			if(post.get("active") == null || post.get("active").equals("0")) {
				throw new BizException("E121" );	//탈퇴한 회원입니다.
			}
			box.put("block_id", post.get("userId"));
			int blockCnt = dao.selectOne("report.userBlockChk", box);

			if(blockCnt > 0) {
				throw new BizException("E136", new String[] {"차단 된"}); // {0} 회원입니다.
			}

			contentId = post.get("postId").toString();
			box.put("post_id", contentId);

			//포스트 등록된 사용자 확인
			post  = dao.selectOne("report.postSelect", box);

			if(post == null ) {
				throw new BizException("E107", new String[] {"게시글이 "}); // {0} 존재하지 않습니다
			}

			if(post.get("active") == null || post.get("active").equals("0")) {
				throw new BizException("E121" );	//탈퇴한 회원입니다.
			}

			box.put("block_id", post.get("userId"));
			blockCnt = dao.selectOne("report.userBlockChk", box);

			if(blockCnt > 0) {
				throw new BizException("E136", new String[] {"차단 된"}); // {0} 회원입니다.
			}

		}else if(type.equals("video_comment") || type.equals("video_comment_reply")) {	//비디오
			Box video = new Box();
			if(type.equals("video_comment")){
				video = dao.selectOne("report.videoCommentSelect", box);
			}else {
				video = dao.selectOne("report.videoCommentReplySelect", box);
			}

			if(video == null ) {
				throw new BizException("E107", new String[] {"댓글이 "}); // {0} 존재하지 않습니다
			}

			if(video.get("active") == null || video.get("active").equals("0")) {
				throw new BizException("E121" );	//탈퇴한 회원입니다.
			}

			box.put("block_id", video.get("userId"));
			int blockCnt = dao.selectOne("report.userBlockChk", box);

			if(blockCnt > 0) {
				throw new BizException("E136", new String[] {"차단 된"}); // {0} 회원입니다.
			}
			contentId = video.get("videoId").toString();
			box.put("video_id", contentId);

			//비디오 등록된 사용자 체크
			video  = dao.selectOne("report.videoSelect", box);

			if(video == null ) {
				throw new BizException("E107", new String[] {"게시글이 "}); // {0} 존재하지 않습니다
			}

			if(video.get("active") == null || video.get("active").equals("0")) {
				throw new BizException("E121" );	//탈퇴한 회원입니다.
			}

			box.put("block_id", video.get("userId"));
			blockCnt = dao.selectOne("report.userBlockChk", box);

			if(blockCnt > 0) {
				throw new BizException("E136", new String[] {"차단 된"}); // {0} 회원입니다.
			}
		}

		int cnt = dao.insert("report.reportCommentInsert", box);
		if(cnt > 0) {
			Box retnBox = new Box();
			return retnBox;
		}else {
			throw new BizException("E107", new String[] {"게시글이 "}); // {0} 존재하지 않습니다
		}
	}

	public Box reportVideoSave(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		Box box1 = SessionUtil.getUserData();

		if(box1.get("id").equals( box.get("owner_id"))) {
			throw new BizException("E135", new String[] {"신고자와 소유자를"}); // {0} 확인해주세요
		}
		//비디오 등록된 사용자 체크
		Box video  = dao.selectOne("report.videoSelect", box);

		if(video == null ) {
			throw new BizException("E107", new String[] {"게시글이 "}); // {0} 존재하지 않습니다
		}

		if(video.get("active") == null || video.get("active").equals("0")) {
			throw new BizException("E121" );	//탈퇴한 회원입니다.
		}

		box.put("block_id", video.get("userId"));
		int blockCnt = dao.selectOne("report.userBlockChk", box);

		if(blockCnt > 0) {
			throw new BizException("E136", new String[] {"차단 된"}); // {0} 회원입니다.
		}
		dao.insert("report.reportVideoInsert", box);

		return null;
	}

	public Box reportUserSave(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		Box box1 = SessionUtil.getUserData();

		if(box1.get("id").equals( box.get("report_user_id"))) {
			throw new BizException("E135", new String[] {"신고자와 신고대상자를"}); // {0} 확인해주세요
		}

		//신고된지 확인
		int reportCnt = dao.selectOne("report.reportUserCnt", box);
		if(reportCnt > 0 ) {
			throw new BizException("E117", new String[] {"신고를 "} ); //{0} 이미 신청하였습니다.
		}else {
			dao.insert("report.reportUserInsert", box);
		}
		return null;
	}

	public Box reportPostSave(Box box) throws Exception  {
		box.put("sBox", SessionUtil.getUserData());
		Box post  = dao.selectOne("report.postSelect", box);

		if(post == null ) {
			throw new BizException("E107", new String[] {"게시글이 "}); // {0} 존재하지 않습니다
		}

		if(post.get("active") == null || post.get("active").equals("0")) {
			throw new BizException("E121" );	//탈퇴한 회원입니다.
		}

		box.put("block_id", post.get("userId"));
		int blockCnt = dao.selectOne("report.userBlockChk", box);

		if(blockCnt > 0) {
			throw new BizException("E136", new String[] {"차단 된 "}); // {0} 회원입니다.
		}

		int postCnt = dao.selectOne("report.reportPostCnt", box);
		if(postCnt > 0 ) {
			throw new BizException("E117", new String[] {"신고를 "} ); //{0} 이미 신청하였습니다.
		}else {
			dao.insert("report.reportPostInsert", box);
		}
		return null;
	}
}
