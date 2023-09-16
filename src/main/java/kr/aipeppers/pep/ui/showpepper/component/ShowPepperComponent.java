package kr.aipeppers.pep.ui.showpepper.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.aipeppers.pep.core.data.Box;
import kr.aipeppers.pep.ui.showpepper.service.ShowPepperService;

@Component
public class ShowPepperComponent {

	@Autowired
	protected ShowPepperService showPepperService;

	/**
	 * @Method Name : showPeperVideoList
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public List<Box> showPeperVideoList(Box box) throws Exception {
		return showPepperService.showPeperVideoList(box);
	}

	/**
	 * @Method Name : showPepperVideoView
	 * @param box
	 * @return
	 */
	public Box showPepperVideoSave(Box box) {
		return showPepperService.showPepperVideoSave(box);
	}

	/**
	 * @Method Name : showSoundsList
	 * @param box
	 * @return
	 */
	public List<Box> showSoundsList(Box box) {
		return showPepperService.showSoundsList(box);
	}

	/**
	 * @Method Name : videoCommentInsert
	 * @param box
	 * @return
	 */
	public Box videoCommentInsert(Box box) {
		return showPepperService.videoCommentInsert(box);
	}

	/**
	 * @Method Name : videoCommentDel
	 * @param box
	 * @return
	 */
	public Box videoCommentDel(Box box) {
		return showPepperService.videoCommentDel(box);
	}

	/**
	 * @Method Name : showVideoCommentsList
	 * @param box
	 * @return
	 */
	public List<Box> showVideoCommentsList(Box box) {
		return showPepperService.showVideoCommentsList(box);
	}

	/**
	 * @Method Name : likeCommenInsert
	 * @param box
	 * @return
	 */
	public int likeCommenSave(Box box) {
		// TODO Auto-generated method stub
		return showPepperService.likeCommenSave(box);
	}

	/**
	 * @Method Name : likeCommentReplyInsert
	 * @param box
	 * @return
	 */
	public Box likeCommentReplySave(Box box) {
		// TODO Auto-generated method stub
		return showPepperService.likeCommentReplySave(box);
	}

	/**
	 * @Method Name : likeVideoSave
	 * @param box
	 * @return
	 */
	public Box likeVideoSave(Box box) {
		// TODO Auto-generated method stub
		return showPepperService.likeVideo(box);
	}

	/**
	 * @Method Name : myShowPepperInsert
	 * @param box
	 * @return
	 */
	public int myShowPepperInsert(Box box) {
		// TODO Auto-generated method stub
		return showPepperService.myShowPepperInsert(box);
	}

	/**
	 * @Method Name : addVideoFavouriteSave
	 * @param box
	 * @return
	 */
	public Box addVideoFavouriteSave(Box box) {
		// TODO Auto-generated method stub
		return showPepperService.addVideoFavouriteSave(box);
	}
}
