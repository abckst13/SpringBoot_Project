package kr.aipeppers.pep.ui.guestBook.service;

import java.util.List;

import javax.servlet.http.HttpSession;

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
public class GuestBookService {

	@Autowired
	protected SqlSessionTemplate dao ;

	@Autowired
	private Paginate paginate;

	@Autowired
	HttpSession session = SessionUtil.getSession();

	/**
	 * @Method Name : aipepperPotoList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public Box guestBookList(Box box) throws Exception {
		box.put("playerId", box.nvl("player_id"));
		Box resBox = dao.selectOne("guestbook.info.guestBookView", box); // 선수 담벼락 조회
		resBox.put("player", dao.selectOne("guestbook.info.playerView", box)); // 선수 정보 조회
		List<Box> commentList = dao.selectList("guestbook.info.commentList", box);
		resBox.put("userComment", commentList); // 선수 담벼락에 쓴 댓글 조회
		resBox.put("count", commentList.size());
		List<Box> comentBox = resBox.getList("userComment");
		if(resBox.get("userComment") != null ) {
			for(Box innrBox : comentBox) {
				innrBox.put("userId", innrBox.nvl("userId"));
				innrBox.put("user", dao.selectOne("guestbook.info.userView", innrBox));
			}
		}

		return resBox;
	}


	/**
	 * @Method Name : guestBookInser
	 * @param box
	 * @return
	 */
	public int guestBookInsert(Box box) {

		String role = SessionUtil.getUserData().nvl("role");
		box.put("sBox", SessionUtil.getUserData());
		Box contentWritUsr = new Box();
		if(!"".equals(box.nvl("player_id"))) {
			contentWritUsr = dao.selectOne("guestbook.info.guestBookContentChk" , box); // 해당 담벼락을 등록한 user Id
		}
		if ("user".equals(role)) { // 사용자 일 때
			if ("".equals(box.nvl("content"))) {
				throw new BizException("E107", new String[] {"content가"}); // {0} 존재하지 않습니다.
			}
			box.put("playerId", box.nvl("player_id"));
			box.put("comment", box.nvl("content"));
			dao.insert("guestbook.info.guestBookCmmInsert", box);
		} else if ("player".equals(role) && "".equals(box.nvl("player_id"))) { // 선수 일 때
			if ("".equals(box.nvl("content"))) {
				throw new BizException("E107", new String[] {"content가"}); // {0} 존재하지 않습니다.
			}
			dao.update("guestbook.info.guestBookUpdate", box);
		} else if ("player".equals(role) && !SessionUtil.getUserData().nvl("id").equals(contentWritUsr.nvl("userId"))) { // 선수지만 내가 쓴 담벼락이 아닐 경우
			if ("".equals(box.nvl("content"))) {
				throw new BizException("E107", new String[] {"content가"}); // {0} 존재하지 않습니다.
			}
			box.put("playerId", box.nvl("player_id"));
			box.put("comment", box.nvl("content"));
			dao.insert("guestbook.info.guestBookCmmInsert", box);
		} else if ("".equals(role) || role == null) { // role 정보가 없을 때
			throw new BizException("E101"); // 로그인 정보가 존재하지 않습니다.
		}
		return 1;
	}

	/**
	 * @Method Name : guestBookUpdate
	 * @param box
	 * @return
	 */
	public int guestBookUpdate(Box box) {
		String role = SessionUtil.getUserData().nvl("role");
		box.put("sBox", SessionUtil.getUserData());
		Box contentWritUsr = new Box();
		if(!"".equals(box.nvl("player_id"))) {
			contentWritUsr = dao.selectOne("guestbook.info.guestBookContentChk" , box); // 해당 담벼락을 등록한 user Id
		}

		if ("user".equals(role)) { // 사용자 일 때
			if ("".equals(box.nvl("content"))) {
				throw new BizException("E107", new String[] {"content가"}); // {0} 존재하지 않습니다.
			}
			if ("".equals(box.nvl("comment_id"))) {
				throw new BizException("E107", new String[] {"comment id 가"}); // {0} 존재하지 않습니다.
			}
			box.put("commentId", box.nvl("comment_id"));
			box.put("comment", box.nvl("content"));
			dao.update("guestbook.info.guestBookCmmUpdate", box);
		} else if ("player".equals(role) && "".equals(box.nvl("player_id"))) { // 선수 일 때
			if ("".equals(box.nvl("content"))) {
				throw new BizException("E107", new String[] {"content가"}); // {0} 존재하지 않습니다.
			}
			dao.update("guestbook.info.guestBookUpdate", box);
			dao.delete("guestbook.info.guestBookLogDel", box); // 선수가 담벼락을 update  하면 이력 테이블에서 해당 선수의 이력을 다 지움\

		} else if ("player".equals(role) && !SessionUtil.getUserData().nvl("id").equals(contentWritUsr.nvl("userId"))) { // 선수지만 내가 쓴 담벼락이 아닐 경우
			if ("".equals(box.nvl("content"))) {
				throw new BizException("E107", new String[] {"content가"}); // {0} 존재하지 않습니다.
			}
			if ("".equals(box.nvl("comment_id"))) {
				throw new BizException("E107", new String[] {"comment id 가"}); // {0} 존재하지 않습니다.
			}
			box.put("commentId", box.nvl("comment_id"));
			box.put("comment", box.nvl("content"));
			dao.update("guestbook.info.guestBookCmmUpdate", box);
		}
		return 1;
	}

	/**
	 * @Method Name : guestBookDel
	 * @param box
	 * @return
	 */
	public int guestBookDel(Box box) {
		String role = SessionUtil.getUserData().nvl("role");
		box.put("sBox", SessionUtil.getUserData());
		if ("".equals(box.nvl("comment_id"))) {
			throw new BizException("E137", new String[] {"comment id 를"}); // {0} 찾을 수 없습니다.
		}
		box.put("commentId", box.nvl("comment_id"));
		int delViewChk = dao.selectOne("guestbook.info.guestBookChk",box);
		if (delViewChk == 0 ) {
			throw new BizException("E137", new String[] {"담벼락을"}); // {0} 찾을 수 없습니다.
		}
		int delChk = dao.selectOne("guestbook.info.guestBookDelChk",box);
		if (delChk == 1 ) {
			throw new BizException("E125", new String[] {"이미 담벼락이"}); // {0} 삭제 되었습니다.
		}
		box.put("commentId", box.nvl("comment_id"));
		dao.delete("guestbook.info.guestBookDel", box);
		return 1;
	}

	/**
	 * @Method Name : guestBookReadInsert
	 * @param box
	 * @return
	 */
	public int guestBookReadInsert(Box box) {
		box.put("sBox", SessionUtil.getUserData());
		int result = dao.selectOne("guestbook.info.readChk", box);
		if(result > 0) {
			return 1;
		} else {
			box.put("playerId", box.nvl("player_id"));
			if("".equals(box.nvl("player_id"))) {
				throw new BizException("E107", new String[] {"선수아이디가"}); //{0} 존제하지 않습니다.
			}
			dao.insert("guestbook.info.readGuestbookIsrt",box);
		}
		return 1;
	}
}
