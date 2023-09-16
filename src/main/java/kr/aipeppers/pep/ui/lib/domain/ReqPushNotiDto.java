package kr.aipeppers.pep.ui.lib.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class ReqPushNotiDto {
	private String title;
	private String body;
	private String badge;
	private String sound;
	private String icon;
	private String type;
	private String receiver_id;
	private String sender_id;
}
