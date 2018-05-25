package org.hasan.bean.model.message;

import org.gatlin.core.bean.model.message.Message;
import org.hasan.bean.enums.MessageType;

public class PushMessage<ATTACH> implements Message {

	private static final long serialVersionUID = -2026076351389300870L;

	private String title;
	private ATTACH attach;
	private String content;
	private MessageType type;
	
	public PushMessage() {
		this.type = MessageType.COMMON;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ATTACH getAttach() {
		return attach;
	}
	
	public void setAttach(ATTACH attach) {
		this.attach = attach;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public void setType(MessageType type) {
		this.type = type;
	}
}
