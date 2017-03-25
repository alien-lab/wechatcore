package com.alienlab.wechat.response;

public class ResponseFactory {
	public static IResponse CreateResponse(){
		return new JSONResponse();
	}
}
