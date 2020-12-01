package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	private String createdTime;
	private String host, content;
	
	public Message() {
		super();
		this.createdTime = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss").format(new Date());
	}

	public Message(String host, String content) {
		super();
		this.createdTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		this.host = host;
		this.content = content;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "["+this.createdTime+"]>"+this.host+": "+content;
	}
	
	
}
