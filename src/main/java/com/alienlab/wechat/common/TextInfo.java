package com.alienlab.wechat.common;

import com.alibaba.fastjson.JSONObject;

public class TextInfo extends BaseInfo {
	private String Content;
	
	public TextInfo(){
		this.setMsgType("text");
	}
	
	public TextInfo(String text){
		this.setMsgType("text");
		this.setContent(text);
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}
	
	@Override
	public String getPushInfo(){
		JSONObject textinfo=new JSONObject();
		textinfo.put("touser", this.getToUserName());
		textinfo.put("msgtype","text");
		JSONObject content=new JSONObject();
		content.put("content",this.getContent());
		textinfo.put("text",content);
		return textinfo.toJSONString();
		
	}
	
	
}
