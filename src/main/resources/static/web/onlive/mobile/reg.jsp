<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="com.alienlab.wechat.service.NamelistItemService"%>
    <%@page import="com.alienlab.wechat.entity.NamelistItem"%>
<!DOCTYPE html">
<html>
<%@ include file="wxutil.jsp"%>
<% 
String user_sex="";
String user_province="";
String user_city="";
String user_country="";
String user_unionid="";
if(user!=null){
	user_sex=user.get("sex");
	user_province=user.get("province");
	user_city=user.get("city");
	user_country=user.get("country");
	user_unionid=user.get("unionid");
}
if(openId!=null){
	NamelistItemService namelistItemService = null;
	NamelistItem nameitem = namelistItemService.findNamelistItemByOpenId(openId);
	//如果存在此用户，跳转到创建界面
	if(nameitem!=null){
		String url=BASE_PATH+"onlive/mobile/CreateRoom.jsp";
		response.sendRedirect("onlive/mobile/goto.jsp?target="+url+"&state="+nameitem.getPhone());
	}
}
	
	
%>
<style>
	.headerimg,.nickName{
		width:100%;
		text-align:center;
		margin:15px auto;
	}
	.headerimg img{
		width:50%;
		margin:0 auto;
	}
</style>
<body>
	<div class="weui_cells_title weui_cell_bd weui_cell_primary">
		<h2>欢迎使用直播间服务</h2>
	</div>
	<div class="">
		<div class="headerimg">
			<img src="<%=headerimg %>">
		</div>
		<div class="nickName"><%=nickName %></div>		
	</div>
	
	<div class="weui_cells">
	    <div class="weui_cell weui_cell_bd">
	    	<input id="phonenumber" class="weui_input" type="number" pattern="[0-9]*" placeholder="请输入您的手机号码">
	    </div>
	 </div>

	<div class="weui_btn_area">
       	<a class="weui_btn weui_btn_primary" href="javascript:" id="btnbegin">开始创建直播间</a>
   	</div>
</body>
<script>
	var openId="<%=openId%>";
	$(function(){
		$(".headerimg img").height($(".headerimg img").width());
		
		$("#btnbegin").click(function(){
			if($("#phonenumber").val()==""){
				$.toast("请填写手机号码", "forbidden");
				return;
			}
			$.showLoading("正在验证")
			$.ajax({
				url:"onlive/newuser",
				type:"POST",
				dataType:"json",
				data:{
					phone:$("#phonenumber").val(),
					city:"<%=user_city%>",
					country:"<%=user_country%>",
					openId:"<%=openId%>",
					province:"<%=user_province%>",
					sex:"<%=user_sex%>",
					unionid:"<%=user_unionid%>",
					headerimg:"<%=headerimg%>",
					nickName:"<%=nickName%>"
				},
				success:function(rep){
					console.log(rep);
					$.hideLoading();
					if(rep.result>0){
						var name=rep.data;
						var url="<%=BASE_PATH%>onlive/mobile/myroom.jsp";
						window.location.href="onlive/mobile/goto.jsp?target="+url+"&state="+name.phone;
					}else{
						$.toast("验证失败", "forbidden");
						window.location.href="<%=BASE_PATH%>onlive/mobile/noroom.jsp";
					}
				}
			});
		});
	});
</script>
</html>