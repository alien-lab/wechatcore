package com.alienlab.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.common.*;
import com.alienlab.wechat.entity.*;
import com.alienlab.wechat.message.PushMessage;
import com.alienlab.wechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Wang on 2017/3/31.
 */
@Api(value="/wechatcore-api/NamelistItem",description = "白名单API")
@Controller
@RequestMapping("/onlive")
public class ItemController {
    private static Logger logger = Logger.getLogger(ItemController.class);
    @Autowired
    private NamelistItemService namelistItemService;
    @Autowired
    private OnliveRoomService onliveRoomService;
    @Autowired
    private OnliveMemberService onliveMemberService;
    @Autowired
    private OnliveStreamService onliveStreamService;
    @Autowired
    private OnliveCommentService onliveCommentService;
    @Autowired
    private OnlivePraiseService onlivePraiseService;

    @ApiOperation(value = "验证手机号")
    @RequestMapping (value = "/newUser")
    public String validatePhone(HttpServletRequest request, HttpServletResponse response, String phone) throws IOException {
        boolean isValidate = namelistItemService.validatePhone(phone);
        //账户不可用
        if (!isValidate) {
            System.out.println("phone number wrong...");
            return new ExecResult(false, "该手机号码不能创建直播间").toString();
        } else {
            NamelistItem namelistItem = namelistItemService.findNamelistItemByPhone(phone);
            namelistItem.setCity(request.getParameter("city"));
            namelistItem.setCountry(request.getParameter("country"));
            namelistItem.setOpenId(request.getParameter("openid"));
            namelistItem.setPhone(phone);
            namelistItem.setProvince(request.getParameter("province"));
            namelistItem.setSex(request.getParameter("sex"));
            namelistItem.setUnionId(request.getParameter("unionid"));
            namelistItem.setHeaderimg(request.getParameter("headerimg"));
            namelistItem.setNickName(request.getParameter("nickname"));
            namelistItemService.addNamelistItem(namelistItem);
            request.getSession().setAttribute("namelistItem", namelistItem);
            ExecResult er = new ExecResult();
            er.setResult(true);
            er.setData((JSON) JSON.toJSON(namelistItem));
            return er.toString();
        }
    }

    @RequestMapping(value="/goTo")
    public void goTo(HttpServletRequest request, HttpServletResponse response)throws IOException{
        String target=request.getParameter("target");
        String state=request.getParameter("phone");
        if(target == null||target.equals("")){
            return;
        }
        String gotolink = WeixinUtil.getAuthUrl(target, state, "info");
        System.out.println("go to link:"+gotolink);
        response.sendRedirect(gotolink);
    }

    @ApiOperation(value = "创建直播间")
    @RequestMapping (value = "/create")
    public String createOnliveRoom(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException{
        String PATH = request.getContextPath();
        String BASE_URL  = request.getScheme()+"://"+request.getServerName();
        if(request.getServerPort()!=80){
            BASE_URL+=":"+request.getServerPort();
        }
        String BASE_PATH = BASE_URL + PATH +"/";
        modelMap.addAttribute("BASE_PATH", BASE_PATH);
        NamelistItem namelistItem = (NamelistItem) request.getSession().getAttribute("namelistItem");
        String phone = namelistItem.getPhone();
        String openId = namelistItem.getOpenId();
        String headerimg = namelistItem.getHeaderimg();
        String nickName = namelistItem.getNickName();
        if(openId != null && !openId.equals("")){
            NamelistItem nameItem = namelistItemService.findNamelistItemByOpenId(openId);
            if(nameItem!=null){
                nameItem.setHeaderimg(headerimg);
                nameItem.setNickName(nickName);
                namelistItemService.updateNamelistItem(nameItem);
                if(phone!=null&&!phone.equals("")&&!phone.equals("null")){
                    if(nameItem.getPhone().equals(phone)){
                        if(!namelistItemService.validatePhone(phone)){
                            response.sendRedirect("noRoom.html");
                        }
                    }else{
                        //如果没有合法参数，跳转到电话号码验证
                        response.sendRedirect("noRoom.html");
                    }
                }else{
                    phone = nameItem.getPhone();
                }
            }else{
                //如果没有绑定手机号码，跳转到手机号码绑定
                response.sendRedirect("goTo?target="+BASE_PATH+"/onlive/mobile/reg.html");
            }
        }else{
            //如果没有绑定手机号码，跳转到手机号码绑定
            response.sendRedirect("goTo?target="+BASE_PATH+"/onlive/mobile/reg.html");
        }
        OnliveRoom room = new OnliveRoom();
        room.setDescription(request.getParameter("bc_abstract"));
        room.setEndTime(request.getParameter("bc_endtime").replace("T","").replace("-","").replace(":",""));
        room.setGuest(request.getParameter("bc_guest"));
        room.setName(request.getParameter("bc_name"));
        room.setProject(request.getParameter("bc_project"));
        room.setSpeakMode(request.getParameter("bc_speakmode"));
        room.setStartTime(request.getParameter("bc_starttime").replace("T","").replace("-","").replace(":",""));
        room.setStatus(request.getParameter("bc_status"));
        room.setManagerPhone(phone);
        String covermedia = request.getParameter("bc_cover");
        String iconmedia = request.getParameter("bc_vip_cover");
        if(covermedia != null && !covermedia.equals("")){
            System.out.println("download image mediaid is:"+covermedia);
            String url = WeixinUtil.getMediaUrl(covermedia);
            System.out.println("download image url is:"+url);
            CatchImage ci = new CatchImage();
            String cover = ci.Download(url, OnliveStart.coverPath);
            System.out.println("download image success filename is:"+cover);
            room.setCover(cover);
        }
        if(iconmedia != null && !iconmedia.equals("")){
            System.out.println("download image mediaid is:"+iconmedia);
            String url=WeixinUtil.getMediaUrl(iconmedia);
            System.out.println("download image url is:"+url);
            CatchImage ci=new CatchImage();
            String cover=ci.Download(url, OnliveStart.coverPath);
            System.out.println("download image success filename is:"+cover);
            room.setBrandCover(cover);
        }
        OnliveRoom onliveRoom = onliveRoomService.addOnliveRoom(room);
        if(onliveRoom != null){
            //将管理员自动添加到直播间中。
            onliveMemberService.joinRoom(new OnliveMember(room.getRoomNo(),namelistItemService.findNamelistItemByPhone(room.getManagerPhone())));
            return new ExecResult(true,"您的直播间《"+room.getName()+"》已成功创建。请在"+room.getStartTime()+"至"+room.getEndTime()+" 之间在此服务号上发送直播内容。").toString();
        }else{
            return new ExecResult(false,"直播创建失败。您选择时间区间已有其他直播。").toString();
        }
    }

    @ApiOperation(value="我的直播间")
    @RequestMapping(value = "/myRoom")
    public void myRoom(HttpServletRequest request, ModelMap modelMap)throws IOException{
        modelMap = new ModelMap("myRoom");
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        List<OnliveRoom> roomList = onliveRoomService.findOnliveRoomByOpenId(openId);
        modelMap.addAttribute("roomList", roomList);
        String PATH = request.getContextPath();
        String BASE_URL  = request.getScheme()+"://"+request.getServerName();
        if(request.getServerPort()!=80){
            BASE_URL+=":"+request.getServerPort();
        }
        String BASE_PATH = BASE_URL + PATH +"/";
        modelMap.addAttribute("BASE_PATH", BASE_PATH);
    }

    @ApiOperation(value = "加载直播间")
    @RequestMapping(value="/getUserRoom")
    public String loadRooms(HttpServletRequest request){
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        logger.error("getuserroom.do>>>>openId:"+openId);
        try {
            String input = IOUtils.toString(request.getInputStream());
            logger.error("getuserroom.do>>>>input:"+input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<OnliveRoom> roomList = onliveRoomService.findOnliveRoomByOpenId(openId);
        JSONArray array = new JSONArray();
        for(int i = 0;i < roomList.size();i++){
            JSONObject room=new JSONObject();
            room.put("roomNo",roomList.get(i).getRoomNo());
            room.put("roomName",roomList.get(i).getName());
            room.put("roomDesc",roomList.get(i).getDescription());
            room.put("cover",roomList.get(i).getCover());
            array.add(room);
        }
        ExecResult er=new ExecResult();
        er.setResult(true);
        er.setData(array);
        return er.toString();
    }

    @ApiOperation(value="直播间设置")
    @RequestMapping(value = "/roomConfig")
    public void roomConfig(HttpServletRequest request, ModelMap modelMap, String roomNo)throws IOException {
        modelMap = new ModelMap("roomConfig");
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        OnliveRoom room = null;
        if(openId != null && !openId.equals("")){
            room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
            NamelistItem name = namelistItemService.findNamelistItemByPhone(room.getManagerPhone());
            if(room != null){   //验证直播间是否存在
                if(!name.getOpenId().equals(openId)){   //验证直播间是否由当前用户管理
                    System.out.println("您不是该直播间管理员");
                    return;
                }else{
                    //roomjo=JSONObject.toJSONString(room);
                    modelMap.addAttribute("room", room);
                }
            }else{
                System.out.println("直播间不存在");
                return;
            }
        }else{
            System.out.println("未获得微信登录授权");
            return;
        }
        String layoutTime = onliveRoomService.getLayoutTime(roomNo);
        String currenttime = TypeUtils.getTime();
        String isover="0";
        if(currenttime.compareTo(room.getEndTime())>0){
            isover="1";
        }
        modelMap.addAttribute("room", room);
        modelMap.addAttribute("layoutTime", layoutTime);
        modelMap.addAttribute("isover", isover);
    }

    @ApiOperation(value = "设置选项")
    @RequestMapping(value="/setSwitch")
    public String setSwitch(HttpServletRequest request, String roomNo, String value, String config){
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }else{
            switch(config){
                case "speakMod":{
                    room.setSpeakMode(value);
                    break;
                }
                case "joinMsg":{
                    room.setJoinMsg(value);
                    break;
                }
                case "commentMsg":{
                    room.setCommentMsg(value);
                    break;
                }
            }
            onliveRoomService.updateOnliveRoom(room);
            return new ExecResult(true,"更新成功").toString();
        }
    }

    @ApiOperation(value = "修改直播间名称")
    @RequestMapping(value="/renameRoom")
    public String renameRoom(HttpServletRequest request, String roomNo, String newName){
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }else{
            room.setName(newName);
            onliveRoomService.updateOnliveRoom(room);
            return new ExecResult(true,"更新成功").toString();
        }
    }

    @ApiOperation(value = "查看直播间成员")
    @RequestMapping(value="/getMembers")
    public String getMembers(HttpServletRequest request, String roomNo){
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }else{
            if(!namelistItemService.findNamelistItemByPhone(room.getManagerPhone()).getOpenId().equals(openId)){
                return new ExecResult(false,"对不起，您不是直播间管理员。").toString();
            }
            List<OnliveMember> members = new ArrayList<>();
            List<OnliveMember> onliveMemberList = onliveMemberService.findOnliveMemberByRoomNo(roomNo);
            for(OnliveMember e : onliveMemberList){
                members.add(e);
            }
            ExecResult er = new ExecResult();
            er.setResult(true);
            er.setData((JSON)JSON.toJSON(members));
            return er.toString();
        }
    }

    @ApiOperation(value = "设置为直播间嘉宾")
    @RequestMapping(value="/setVip")
    public String setVip(HttpServletRequest request, String roomNo){
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }
        OnliveMember member = onliveMemberService.getOnliveMember(roomNo, openId);
        if(member!=null){
            if(onliveMemberService.addSpeaker(member)){
                return new ExecResult(true,"设置成功").toString();
            }else{
                return new ExecResult(false,"保存失败").toString();
            }
        }else{
            return new ExecResult(false,"直播间不存在该成员").toString();
        }
    }

    @ApiOperation(value = "移除直播间嘉宾")
    @RequestMapping(value="/removeVip")
    public String removeVip(HttpServletRequest request, String roomNo){
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }
        if(onliveMemberService.removeSpeaker(openId, roomNo)){
            return new ExecResult(true,"设置成功").toString();
        }else{
            return new ExecResult(false,"保存失败").toString();
        }
    }

    @ApiOperation(value = "删除直播间")
    @RequestMapping(value="/deleteRoom")
    public String deleteRoom(HttpServletRequest request, String roomNo){
        boolean result = onliveRoomService.deleteOnliveRoom(roomNo);
        if(result){
            return new ExecResult(true,"删除成功").toString();
        }else{
            return new ExecResult(false,"删除失败，您的直播间正在直播。").toString();
        }
    }

    @ApiOperation(value="直播间")
    @RequestMapping(value = "/onliveRoom")
    public void onliveRoom(HttpServletRequest request, HttpServletResponse response, ModelMap map, String roomNo)throws IOException{
        map = new ModelMap("onliveRoom");
        NamelistItem namelistItem = (NamelistItem) request.getSession().getAttribute("namelistItem");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        String layoutTime = onliveRoomService.getLayoutTime(roomNo);
        NamelistItem name = namelistItemService.findNamelistItemByPhone(room.getManagerPhone());
        map.addAttribute("layoutTime", layoutTime);
        String currentTime = TypeUtils.getTime();
        if(room == null){
            response.sendRedirect("noRoom.html");
            return;
        }
        if(room.getManagerPhone() == null){
            response.sendRedirect("noRoom.html");
            return;
        }
        if(name.getStatus().equals("0")){
            response.sendRedirect("noRoom.html");
            return;
        }
        if(room.getStatus().equals("9")){
            response.sendRedirect("noRoom.html");
            return;
        }
        String ismanager="0";   //是否直播间管理员
        //是否是演讲者
        String isspeaker="0";   //是否演讲嘉宾
        //如果是直播间管理员，更新管理员信息
        if(namelistItem.getOpenId().equals(name.getOpenId())){
            ismanager="1";
            isspeaker="1";
            name.setNickName(namelistItem.getNickName());
            name.setHeaderimg(namelistItem.getHeaderimg());
            onliveRoomService.updateOnliveRoom(room);
        }
        String autoload="1";    //是否自动加载
        String isover="0";      //直播是否结束
        String status = "即将开始";
        if(currentTime.compareTo(room.getEndTime())>0){ //直播间到期
            autoload="0";   //不自动加载
            //如果直播间已到期，取消页面发布按钮
            isover="1";     //结束
            status="已结束";
        }
        String endTime = room.getEndTime();
        String startTime = room.getStartTime();
        String isonlive="0"; //正在直播
        if(currentTime.compareTo(startTime)>=0 && currentTime.compareTo(endTime)<0){
            isonlive="1";
            status="直播中";
        }
        room.setStatus(status);
        if(room.getSpeakers().indexOf(namelistItem.getOpenId())>=0){
            isspeaker="1";
        }
        map.addAttribute("room", room);
        map.addAttribute("isismanager",ismanager);
        map.addAttribute("isspeaker", isspeaker);
        map.addAttribute("autoload", autoload);
        map.addAttribute("isover", isover);
        map.addAttribute("isonlive", isonlive);
    }

    @ApiOperation(value="直播间")
    @RequestMapping(value = "/onliveRoom2")
    public void onliveRoom2(HttpServletRequest request, HttpServletResponse response, ModelMap map, String roomNo)throws IOException{
        map = new ModelMap("onliveRoom2");
        NamelistItem namelistItem = (NamelistItem) request.getSession().getAttribute("namelistItem");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        String layoutTime = onliveRoomService.getLayoutTime(roomNo);
        NamelistItem name = namelistItemService.findNamelistItemByPhone(room.getManagerPhone());
        map.addAttribute("layoutTime", layoutTime);
        String currentTime = TypeUtils.getTime();
        if(room == null){
            response.sendRedirect("noRoom.html");
            return;
        }
        if(room.getManagerPhone() == null){
            response.sendRedirect("noRoom.html");
            return;
        }
        if(name.getStatus().equals("0")){
            response.sendRedirect("noRoom.html");
            return;
        }
        if(room.getStatus().equals("9")){
            response.sendRedirect("noRoom.html");
            return;
        }
        String ismanager="0";   //是否直播间管理员
        //是否是演讲者
        String isspeaker="0";   //是否演讲嘉宾
        //如果是直播间管理员，更新管理员信息
        if(namelistItem.getOpenId().equals(name.getOpenId())){
            ismanager="1";
            isspeaker="1";
            name.setNickName(namelistItem.getNickName());
            name.setHeaderimg(namelistItem.getHeaderimg());
            onliveRoomService.updateOnliveRoom(room);
        }
        String autoload="1";    //是否自动加载
        String isover="0";      //直播是否结束
        String status = "即将开始";
        if(currentTime.compareTo(room.getEndTime())>0){ //直播间到期
            autoload="0";   //不自动加载
            //如果直播间已到期，取消页面发布按钮
            isover="1";     //结束
            status="已结束";
        }
        String endTime = room.getEndTime();
        String startTime = room.getStartTime();
        String isonlive="0"; //正在直播
        if(currentTime.compareTo(startTime)>=0 && currentTime.compareTo(endTime)<0){
            isonlive="1";
            status="直播中";
        }
        room.setStatus(status);
        if(room.getSpeakers().indexOf(namelistItem.getOpenId())>=0){
            isspeaker="1";
        }
        map.addAttribute("room", room);
        map.addAttribute("isismanager",ismanager);
        map.addAttribute("isspeaker", isspeaker);
        map.addAttribute("autoload", autoload);
        map.addAttribute("isover", isover);
        map.addAttribute("isonlive", isonlive);
    }

    @ApiOperation(value = "加入直播间")
    @RequestMapping(value="/joinRoom")
    public String joinRoom(HttpServletRequest request, String roomNo, String joinType, String isVip){
        NamelistItem namelistItem = (NamelistItem) request.getSession().getAttribute("namelistItem");
        String openId = namelistItem.getOpenId();
        String nick = namelistItem.getNickName();
        String headerimg = namelistItem.getHeaderimg();
        JSONObject member = new JSONObject();
        member.put("bc_no", roomNo);
        member.put("member_openid", openId);
        member.put("member_nick", nick);
        member.put("member_phone", "");
        member.put("member_pic", headerimg);
        member.put("member_join_type", joinType);
        member.put("member_unionid", "");
        member.put("member_vip", isVip);
        OnliveMember onliveMember = new OnliveMember(member);
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        ExecResult er = new ExecResult();
        if(room != null){
            onliveMemberService.joinRoom(onliveMember);
            er.setResult(true);
            er.setMessage("加入成功");
        }else{
            er.setResult(false);
            er.setMessage("加入失败");
        }
        return er.toString();
    }

    @ApiOperation(value = "加载直播间人数")
    @RequestMapping(value = "/loadRoomMemberCount")
    public String loadRoomMemberCount(HttpServletRequest request, String roomNo){
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        ExecResult er = new ExecResult();
        JSONObject data = new JSONObject();
        if(room!=null){
            er.setResult(true);
            data.put("count", onliveMemberService.findOnliveMemberByRoomNo(roomNo).size());
            er.setData(data);
        }else{
            data.put("count", 0);
            er.setResult(false);
            er.setData(data);
        }
        return er.toString();
    }

    @ApiOperation(value = "获取直播间最新动态")
    @RequestMapping(value="/getRoomLatestInfo")
    public String getRoomLatestInfo(HttpServletRequest request, String roomNo){
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }else{
            JSONObject info = new JSONObject();
            info.put("latesttext", room.getLatestText());
            info.put("latestpic", room.getLatestPic());
            ExecResult er = new ExecResult(true,"获取成功");
            er.setData(info);
            return er.toString();
        }
    }

    @ApiOperation(value = "发布内容")
    @RequestMapping(value="/publishStream")
    public String publishStream(HttpServletRequest request, String roomNo, String type, String nick, String content, String mediaId){
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        ExecResult er = new ExecResult();
        if(room != null){
            JSONObject s = new JSONObject();
            s.put("openid", ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId());
            s.put("content_type", type);
            s.put("member_nick", nick);
            s.put("content_time", TypeUtils.getTime(new Date(), "yyyyMMddHHmmss"));
            s.put("bc_no", roomNo);
            s.put("content_body", content);
            s.put("content_welink", mediaId);
            if(onliveStreamService.publishStream(s)){
                er.setResult(true);
                er.setMessage("发布成功");
            }else{
                er.setResult(false);
                er.setErrormsg("内容发布失败");
            }
        }else{
            er.setResult(false);
            er.setErrormsg("未找到直播间。");
        }
        return er.toString();
    }

    @ApiOperation(value = "加载内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name="roomNo",value="直播间编号",paramType = "query"),
            @ApiImplicitParam(name="time",value="时间条件",paramType = "query"),
            @ApiImplicitParam(name="index",value="分页位置",paramType = "query"),
            @ApiImplicitParam(name="length",value="分页长度",paramType = "query")
    })
    @RequestMapping(value="/loadStream")
    public String loadStream(HttpServletRequest request, String roomNo, String time, int index, int length){
        //如果正序排列
        /*if(sorttype!=null&&sorttype.equals("asc")){
            if(direction.equals("head")){
                direction="<";
            }else{
                direction=">";
            }
        }else{//倒序排列
            if(direction.equals("head")){
                direction=">";
            }else{
                direction="<";
            }
        }*/
        ExecResult er = new ExecResult();
        if(roomNo != null && !roomNo.equals("")){
                Page<OnliveStream> streamsDESC = onliveStreamService.getStreamsDESC(roomNo, time, new PageRequest(index, length));
                er.setResult(true);
                er.setData((JSON)JSON.toJSON(streamsDESC));
        }else{
            er.setResult(false);
            er.setMessage("参数传递错误");
        }
        return er.toString();
    }

    @ApiOperation(value = "更新内容")
    @RequestMapping(value="/reloadItem")
    public String reloaditem(HttpServletRequest request, String streamNo){
        long contentNo  = Long.parseLong(streamNo);
        OnliveStream stream = onliveStreamService.loadStreams(contentNo);
        return JSON.toJSONString(stream);
    }

    @ApiOperation(value = "发布评论")
    @RequestMapping(value="/publishComment")
    public String publishComment(HttpServletRequest request, String roomNo, String streamno, String content){
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        Long streamNo = Long.parseLong(streamno);
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room.getSpeakMode().equals("speaker")){
            return new ExecResult(false,"直播间未开放评论").toString();
        }
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }else{
            onliveCommentService.publishComment(roomNo, streamNo, content);
            ExecResult er = new ExecResult(true,"评论成功");
            if(er.getResult()>0){
                if(room.getCommentMsg().equals("1")){
                    OnliveMember member = onliveMemberService.getOnliveMember(roomNo, openId);
                    if(member!=null){
                        String text = member.getNick()+" 评论了您："+content;
                        TextInfo ti = new TextInfo(text);
                        ti.setToUserName(namelistItemService.findNamelistItemByPhone(room.getManagerPhone()).getOpenId());
                        PushMessage.sendMessage(ti);
                    }
                }
            }
            return er.toString();
        }
    }

    @ApiOperation(value = "删除评论")
    @RequestMapping(value="/deleteComment")
    public String deleteComment(HttpServletRequest request, String roomNo, String commentno){
        Long commentNo = Long.parseLong(commentno);
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }else{
            onliveCommentService.deleteOnliveComment(commentNo);
            return new ExecResult(true,"删除成功").toString();
        }
    }

    @ApiOperation(value = "查看点赞和评论")
    @RequestMapping(value="/getPraiseComment")
    public String getPraiseComment(HttpServletRequest request, String roomNo, String streamno){
        Long streamNo = Long.parseLong(streamno);
        ExecResult er = new ExecResult();
        JSONObject data = new JSONObject();
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            return new ExecResult(false,"直播间不存在").toString();
        }else{
            if(streamNo == null){
                onlivePraiseService.getPraises(roomNo);
                ExecResult per = new ExecResult(true,"");
                if(per.getResult()>0){
                    data.put("praise", per.getData());
                }
                onliveCommentService.getComments(roomNo);
                ExecResult cper = new ExecResult(true,"");
                if(cper.getResult()>0){
                    data.put("comment", cper.getData());
                }
            }else{
                data.put("praise",onlivePraiseService.loadPraises(streamNo));
                data.put("comment",onliveCommentService.loadComments(streamNo));
            }
            er.setResult(true);
            er.setData(data);
            return er.toString();
        }
    }

    @ApiOperation(value = "发布点赞")
    @RequestMapping(value="/publishPraise")
    public String publishPraise(HttpServletRequest request, String roomNo, String streamno) {
        String openId = ((NamelistItem) request.getSession().getAttribute("namelistItem")).getOpenId();
        Long streamNo = Long.parseLong(streamno);
        OnlivePraise praise = new OnlivePraise(streamNo, roomNo, openId);
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if (room == null) {
            return new ExecResult(false, "直播间不存在").toString();
        } else {
            onlivePraiseService.publishPraise(openId, streamNo);
            ExecResult er = new ExecResult(true, "点赞成功");
            if (er.getResult() > 0) {
                if (room.getCommentMsg().equals("1")) {
                    OnliveMember member = onliveMemberService.getOnliveMember(roomNo, openId);
                    if (member != null) {
                        String text = member.getNick() + " 赞了您的直播内容。";
                        TextInfo ti = new TextInfo(text);
                        ti.setToUserName(namelistItemService.findNamelistItemByPhone(room.getManagerPhone()).getOpenId());
                        PushMessage.sendMessage(ti);
                    }
                }
            }
            return er.toString();
        }
    }

    @RequestMapping(value="/refreshdata")
    public void refreshData(HttpServletRequest request, String roomNo){
        int number=Integer.parseInt(request.getParameter("number"));
        if(roomNo==null||roomNo.equals("")){
            System.out.println("请指定直播间编号");
            return;
        }
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room==null){
            System.out.println("直播间不存在");
            return;
        }
        for(int i=0;i<number;i++){
            OnliveMember member = new OnliveMember();
            member.setOpenId("alienzoo_test"+ Calendar.getInstance().getTimeInMillis());
            member.setNick("机器人用户");
            member.setLocalPic("http://www.bigercat.com/wechatsolution/img/azlogo.jpg");
// 		member.setPic("http://www.bigercat.com/wechatsolution/img/azlogo.jpg");
            member.setVip(false);
            member.setJoinType("test");
            member.setRoomNo(roomNo);
            onliveMemberService.joinRoom(member);
        }
        System.out.println("用户数据刷新完成，当前直播间已有用户："+room.getMembers().length());
    }
}
