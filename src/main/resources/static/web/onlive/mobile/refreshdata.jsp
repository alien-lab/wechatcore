<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.alienlab.wechat.service.OnliveRoomService"%>
<%@page import="com.alienlab.wechat.entity.OnliveRoom"%>
<%@page import="com.alienlab.wechat.entity.OnliveMember"%>
<%@ page import="com.alienlab.wechat.service.OnliveMemberService" %>

<%
 	String roomNo=request.getParameter("roomNo");
 	int number=Integer.parseInt(request.getParameter("number"));
 	
 	if(roomNo==null||roomNo.equals("")){
 		System.out.println("请指定直播间编号");
 		return;
 	}
	OnliveRoomService onliveRoomService = null;
	OnliveMemberService onliveMemberService = null;
 	OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
 	if(room==null){
 		System.out.println("直播间不存在");
 		return;
 	}
 	for(int i=0;i<number;i++){
 		OnliveMember member = new OnliveMember();
 		member.setOpenId("alienzoo_test"+Calendar.getInstance().getTimeInMillis());
 		member.setNick("机器人用户");
 		member.setLocalPic("http://www.bigercat.com/wechatsolution/img/azlogo.jpg");
// 		member.setPic("http://www.bigercat.com/wechatsolution/img/azlogo.jpg");
 		member.setVip(false);
 		member.setJoinType("test");
 		member.setRoomNo(roomNo);
 		onliveMemberService.joinRoom(member);
 	}
 	System.out.println("用户数据刷新完成，当前直播间已有用户："+room.getMembers().keySet().size());
 %>