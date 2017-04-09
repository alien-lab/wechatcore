<%@page import="com.alienlab.wechat.common.WeixinUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<% 
String cur_url= request.getScheme()+"://"+ request.getServerName()+request.getRequestURI();
String queryStr=request.getQueryString();
if(queryStr!=null&&!queryStr.equals("")){
	cur_url=cur_url+"?"+queryStr;
}
Map<String ,String> m=WeixinUtil.getJsapiSignature(cur_url);
if(m==null){
	WeixinUtil.getMenu();
	System.out.println("没有正确获取微信js-sdk");
	return;
}
String timestamp=m.get("timestamp");
String nonceStr=m.get("nonceStr");
String signature=m.get("signature");
String appid=m.get("appid");

//从session获得用户信息
String openId=(session.getAttribute("openId")==null)?"":session.getAttribute("openId").toString();
if(openId==null)openId="";
String nickName=(session.getAttribute("nickName")==null)?"":session.getAttribute("nickName").toString();
if(nickName==null)nickName="";
String headerimg=(session.getAttribute("headerimg")==null)?"":session.getAttribute("headerimg").toString();
if(headerimg==null)headerimg=BASE_PATH+"img/azlogo.jpg";

java.util.Map<String,String> user=null;

String code=request.getParameter("code");
//没有接到微信返回的授权码
if(code!=null){
	//通过code获得openId
	if(openId==null||openId.equals("")){//session中有值将直接使用
		com.alibaba.fastjson.JSONObject jo=WeixinUtil.getWxUserBase(code);
		//获取当前用户openId出错，有可能是用户点击浏览器返回按钮
		if(jo.containsKey("errcode")){
			openId=(session.getAttribute("openId")==null)?"":session.getAttribute("openId").toString();
			if(openId==null)openId="";
			nickName=(session.getAttribute("nickName")==null)?"":session.getAttribute("nickName").toString();
			if(nickName==null)nickName="";
			headerimg=(session.getAttribute("headerimg")==null)?"":session.getAttribute("headerimg").toString();
			if(headerimg==null)headerimg=BASE_PATH+"img/azlogo.jpg";
			System.out.println("code has timeout..");
			if(openId==null||openId.equals("")){
				System.out.println("获取微信授权失败，请关闭界面重新进入系统。");
				return;
			}
	  	}else{
			openId=jo.getString("openId");//使用接口获得到的openId；
	  		String token=jo.getString("access_token");
	  		//调取用户基本信息
	  		user=WeixinUtil.getWxinfo(openId, token);
	  		//获得用户昵称
	  		nickName=user.get("nickName");
	  		//获得用户头像地址
	  		headerimg=user.get("headimgurl");
	  		//用户信息获取正确，将信息存入session
	  		session.setAttribute("openId",openId);
	  		session.setAttribute("nickName",nickName);
	  		session.setAttribute("headerimg",headerimg);
	  	}
	}
}else{
	System.out.println("request is not from wechat..");
}



%>
<script type="text/javascript" charset="utf-8" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
var wxdebug=false;
wx.config({
    debug: wxdebug,
    appId: '<%=appid%>',
    timestamp: <%=timestamp%>,
    nonceStr: '<%=nonceStr%>',
    signature: '<%=signature%>',
    jsApiList: [
      'checkJsApi',
      'onMenuShareTimeline',
      'onMenuShareAppMessage',
      'onMenuShareQQ',
      'onMenuShareWeibo',
      'hideMenuItems',
      'showMenuItems',
      'hideAllNonBaseMenuItem',
      'showAllNonBaseMenuItem',
      'translateVoice',
      'startRecord',
      'stopRecord',
      'onRecordEnd',
      'playVoice',
      'pauseVoice',
      'stopVoice',
      'uploadVoice',
      'downloadVoice',
      'chooseImage',
      'previewImage',
      'uploadImage',
      'downloadImage',
      'getNetworkType',
      'openLocation',
      'getLocation',
      'hideOptionMenu',
      'showOptionMenu',
      'closeWindow',
      'scanQRCode',
      'chooseWXPay',
      'openProductSpecificView',
      'addCard',
      'chooseCard',
      'openCard'
    ]
});

wx.ready(function (){ 	
	if(wxdebug){
		alert("微信JS onready");
	}
	wx.hideMenuItems({
	    menuList: ["menuItem:share:appMessage",
	    	"menuItem:share:timeline",
	    		"menuItem:share:qq",
	    		"menuItem:share:weiboApp",
	    		"menuItem:favorite",
	    		"menuItem:share:facebook",
	    		"menuItem:share:QZone"] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
	});
});
</script>
