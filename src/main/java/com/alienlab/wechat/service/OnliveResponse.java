package com.alienlab.wechat.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alienlab.wechat.common.*;
import com.alienlab.wechat.entity.NamelistItem;
import com.alienlab.wechat.entity.OnliveMember;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.message.*;
import com.alienlab.wechat.utils.IResponse;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 直播消息响应
 * 
 * @author 橘
 *
 */
public class OnliveResponse implements IResponse {
	private static Logger logger = Logger.getLogger(OnliveResponse.class);
	private OnliveRoomService onliveRoomService;
	private OnliveStreamService onliveStreamService;
	private NamelistItemService namelistItemService;
	private OnliveMemberService onliveMemberService;

	@Override
	public void preResponse(BaseMessage msg, JSONObject param) {
		// TODO Auto-generated method stub

	}

	@Override
	public String doResponse(BaseMessage msg, JSONObject param) {
		logger.info("begin OnliveResponse>>>>>>>>>>>" + param.toJSONString());
		String res = "";
		if (param.containsKey("key")) {
			if (param.getString("key").equals("onliveroom")) {
				String content = ((TextMessage) msg).getContent();
				if (content.equals("我的直播间")) {
					res= BaseResponse.getTextResponse("<a href=\""+ WeixinUtil.getAuthUrl(OnliveStart.onlivebasepath + "/onlive/mobile/myroom.jsp", null, "info")
					+ "\">进入我的直播间</a>",msg);
				}else if(content.equals("查看直播")){//返回直播间列表,前9篇
					List<OnliveRoom> rooms = onliveRoomService.findOnliveRoomByOpenId(msg.getFromUserName());
					List<ArticleObject> articles = new ArrayList<ArticleObject>();
					int count=rooms.size();
					if(count>9)count=9;
					for(int i=0;i<count;i++){
						OnliveRoom room=rooms.get(i);
						ArticleObject onliveroom = new ArticleObject();
						articles.add(onliveroom.getArticle(room));
					}
					res=BaseResponse.getNewsResponse(articles, msg);
				}else if(content.equals("我要直播")){
					res=quickOnlive(msg);
				}
			} else if (param.getString("key").equals("onlivestream")) {
				String roomNo = param.getString("roomno");
				OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
				if (room == null) {
					ResponseConfig.removeConfig(roomNo);
					res = BaseResponse.getTextResponse("没有找到您的直播间。", msg);
				} else {
					//直播间状态9表示封号
					if(room.getStatus().equals("9")){
						ResponseConfig.removeConfig(roomNo);
						return  BaseResponse.getTextResponse("您的直播间已被关闭。", msg);
					}
					logger.info("Publish [" + msg.getMsgType() + "] stream>>>>>>>>>>>");
					JSONObject s = new JSONObject();
					s.put("openid", msg.getFromUserName());
					s.put("content_type", msg.getMsgType());
					String nick = "";
					OnliveMember member = onliveMemberService.getOnliveMember(roomNo, msg.getFromUserName());
					if (member != null) {
						nick = member.getNick();
					}
					Date dt = new Date();
					dt.setTime((Long.parseLong(msg.getCreateTime())*1000));
					s.put("member_nick", nick);
					s.put("content_time", TypeUtils.getTime(dt, "yyyyMMddHHmmss"));
					s.put("bc_no", room.getRoomNo());
					
					switch (msg.getMsgType()) {
						case "text": {
							String content = ((TextMessage) msg).getContent();
							s.put("content_body", content);
							break;
						}
						case "image":{
							s.put("content_welink", ((PicMessage)msg).getMediaId());
							break;
						}
						case "voice":{
							s.put("content_welink", ((AudioMessage)msg).getMediaId());
							break;
						}
						case "video":{
							s.put("content_welink", ((VideoMessage)msg).getMediaId());
							s.put("content_pic", ((VideoMessage)msg).getThumbMediaId());
							break;
						}
						case "shortvideo": {
							s.put("content_welink", ((ShortVideoMessage)msg).getMediaId());
							s.put("content_pic", ((ShortVideoMessage)msg).getThumbMediaId());
							break;
						}
						case "event": {
							
							break;
						}
						default: {
							logger.info("OnliveResponse no catch message>>>>>>>>>>>");
							break;
						}
					}
					
					logger.info("OnliveResponse publish stream:"+s.toJSONString());
					logger.info("begin publist stream。。。。。"+System.currentTimeMillis());
					onliveStreamService.publishStream(s);
					logger.info("publist stream finish。。。。。"+System.currentTimeMillis());
					List<ArticleObject> articles=new ArrayList<ArticleObject>();
					ArticleObject onliveroom = new ArticleObject();
					articles.add(onliveroom.getArticle(room));
					res=BaseResponse.getNewsResponse(articles, msg);
					//res=BaseResponse.getTextResponse("消息已成功直播，点击链接进入直播间>>><a href=\""+room.getShareLink()+"\">"+room.getName()+"</a>", msg);
				}
			}else if(param.getString("key").equals("qrscan")){
				String type = msg.getClass().toString();
				logger.info("get a qr scan message>>>>");
				if(type.indexOf("EqrMessage")>=0){
					EqrMessage qrmsg = (EqrMessage)msg;
					String eventkey = qrmsg.getEventKey();
					if(eventkey != null && eventkey.startsWith("qrscene_")){
						eventkey = eventkey.split("_")[1];
					}
					String roomNo = eventkey;
					if(roomNo == null || roomNo.equals("")){
						if(qrmsg.getEvent().equals("subscribe")){
							res=BaseResponse.getTextResponse("AlienZoo欢迎您，我们的直播组件还在封测中。如果您对组件感兴趣，请回复申请测试+手机号码，例如：申请测试13013013000，系统将自动为您开通测试权限。",msg);
						}
					}else{
						OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
						if(room==null){
							res=BaseResponse.getTextResponse("对不起，系统没有找到对应的直播间。",msg);
						}else{
							//返回直播间图文
							ArticleObject onliveroom = new ArticleObject();
							List<ArticleObject> articles=new ArrayList<ArticleObject>();
							articles.add(onliveroom.getArticle(room));
							res=BaseResponse.getNewsResponse(articles, msg);
						}
					}
				}else{
					res="";
				}
				
			}else if(param.getString("key").equals("all")){//没有任何匹配时，进入此响应
				if(msg.getMsgType().equals("text")){
					String text=((TextMessage) msg).getContent();
					if(text.startsWith("申请测试")){
						text.replace(" ","").replace(",", "").replace(".","").replace("，","").replace("。","").replace("+","").replace("-","");
						String phone=text.substring(4).trim();
						NamelistItem item = namelistItemService.findNamelistItemByPhone(phone);
						if(item!=null){
							res=BaseResponse.getTextResponse("您已有测试权限，无需重复申请。",msg);
						}else{
							NamelistItem name = new NamelistItem();
							name.setPhone(phone);
							if(namelistItemService.addNamelistItem(name) != null){
								res=BaseResponse.getTextResponse("测试权限已开通，点击下方链接，输入您刚才申请的手机号码："+phone+"进行绑定，欢迎使用。",msg);
							}else{
								res=BaseResponse.getTextResponse("未能开通测试权限。",msg);
							}
						}
					}else{
						res=BaseResponse.getTextResponse("系统没有匹配到您的直播间，您可以点击链接 <a href=\""+WeixinUtil.getAuthUrl(OnliveStart.onlivebasepath + "/onlive/mobile/myroom.jsp", null, "info")
						+ "\">管理您的直播间</a>",msg);
					}
				}else{
					res=BaseResponse.getTextResponse("系统没有匹配到您的直播间，您可以点击链接 <a href=\""+WeixinUtil.getAuthUrl(OnliveStart.onlivebasepath + "/onlive/mobile/myroom.jsp", null, "info")
					+ "\">管理您的直播间</a>",msg);
				}
				
			}
		} else {
			res = BaseResponse.getTextResponse("没有正确解析参数。", msg);
		}
		return res;
	}

	@Override
	public void afterResponse(BaseMessage msg, JSONObject param) {
		// TODO Auto-generated method stub

	}

	private void processMediaMessage(BaseMessage msg) {

	}
	
	/**
	 * 快捷创建直播间方法
	 * @param msg
	 * @return
	 */
	public String quickOnlive(BaseMessage msg){
		String openId = msg.getFromUserName();
		NamelistItem name = namelistItemService.findNamelistItemByOpenId(openId);
		if(name==null){
			return BaseResponse.getTextResponse("您还没有绑定手机号，<a href=\""+WeixinUtil.getAuthUrl(OnliveStart.onlivebasepath + "/onlive/mobile/myroom.jsp", null, "info")+"\">马上绑定。</a>", msg);
		}
		if(!namelistItemService.validatePhone(name.getPhone())){
			return BaseResponse.getTextResponse("很抱歉，您的号已无法再创建直播间。",msg);
		}
		OnliveRoom room=new OnliveRoom();
		room.setDescription(name.getNickName()+"刚创建的直播间，快来看看精彩内容");
		Calendar c = Calendar.getInstance();
		String stime=TypeUtils.getTime(c.getTime(), "yyyyMMddHHmmss");
		c.add(Calendar.HOUR,2);
		String etime=TypeUtils.getTime(c.getTime(), "yyyyMMddHHmmss");

		room.setEndTime(etime);
		room.setName(name.getNickName()+"的直播间");
		room.setSpeakMode("speaker");
		room.setStartTime(stime);
		room.setStatus("1");
		room.setManagerPhone(name.getPhone());
		room.setCover(OnliveStart.onlivebasepath+"onlive/mobile/image/defaultcover.png");
		room.setBrandCover(name.getHeaderimg());
		
		if(onliveRoomService.addOnliveRoom(room) != null){
			onliveMemberService.joinRoom(new OnliveMember(room.getRoomNo(),namelistItemService.findNamelistItemByPhone(room.getManagerPhone())));
			return BaseResponse.getTextResponse("直播间已创建，现在直接向服务号发送内容，即可直播。",msg);
		}else{
			return BaseResponse.getTextResponse("直播间创建失败，您在此时段可能已有直播间。",msg);
		}
	}

	

}
