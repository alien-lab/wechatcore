package com.alienlab.wechat.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.common.CatchImage;
import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.common.WeixinUtil;
import com.alienlab.wechat.entity.NamelistItem;
import com.alienlab.wechat.entity.OnliveMember;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.service.NamelistItemService;
import com.alienlab.wechat.service.OnliveMemberService;
import com.alienlab.wechat.service.OnliveRoomService;
import com.alienlab.wechat.utils.PropertyConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by Wang on 2017/4/6.
 */
@Api(value="/wechatcore-api/OnliveRoom",description = "直播间API")
@RestController
@RequestMapping("/wechatcore-api")
public class RoomController {
    private static Logger logger = Logger.getLogger(RoomController.class);
    @Autowired
    private OnliveRoomService onliveRoomService;
    @Autowired
    private NamelistItemService namelistItemService;
    @Autowired
    private OnliveMemberService onliveMemberService;

    @ApiOperation(value = "创建直播间")
    @PostMapping(value="onlive/create")
    public ResponseEntity createOnliveRoom(HttpServletRequest request){
        PropertyConfig pc = new PropertyConfig("sysConfig.properties");
        OnliveRoom room = new OnliveRoom();
        room.setDescription(request.getParameter("bc_abstract"));
        room.setEndTime(request.getParameter("bc_endtime").replace("T","").replace("-","").replace(":",""));
        room.setGuest(request.getParameter("bc_guest"));
        room.setName(request.getParameter("bc_name"));
        room.setProject(request.getParameter("bc_project"));
        room.setSpeakMode(request.getParameter("bc_speakmode"));
        room.setStartTime(request.getParameter("bc_starttime").replace("T","").replace("-","").replace(":",""));
        room.setStatus(request.getParameter("bc_status"));
        NamelistItem name = namelistItemService.findNamelistItemByPhone(request.getParameter("bc_manager_phone"));
        room.setManager(name);
        String covermedia = request.getParameter("bc_cover");
        String iconmedia = request.getParameter("bc_vip_cover");
        ServletConfig config = null;
        if(covermedia != null && !covermedia.equals("")){
            System.out.println("download image mediaid is:"+covermedia);
            String url = WeixinUtil.getMediaUrl(covermedia);
            System.out.println("download image url is:"+url);
            CatchImage ci = new CatchImage();
            String cover = ci.Download(url, config.getServletContext().getRealPath("/"+pc.getValue("cover_path")));
            System.out.println("download image success filename is:"+cover);
            room.setCover(cover);
        }
        if(iconmedia != null && !iconmedia.equals("")){
            System.out.println("download image mediaid is:"+iconmedia);
            String url=WeixinUtil.getMediaUrl(iconmedia);
            System.out.println("download image url is:"+url);
            CatchImage ci=new CatchImage();
            String cover=ci.Download(url, config.getServletContext().getRealPath("/"+pc.getValue("cover_path")));
            System.out.println("download image success filename is:"+cover);
            room.setBrandCover(cover);
        }
        OnliveRoom onliveRoom = onliveRoomService.addOnliveRoom(room);
        if(onliveRoom != null){
            //将管理员自动添加到直播间中。
            onliveMemberService.joinRoom(new OnliveMember(room.getRoomNo(),room.getManager()));
            ExecResult er = new ExecResult(true,"您的直播间《"+room.getName()+"》已成功创建。请在"+room.getStartTime()+"至"+room.getEndTime()+" 之间在此服务号上发送直播内容。");
            return ResponseEntity.ok().body(er);
        }else{
            ExecResult er = new ExecResult(false,"直播创建失败。您选择时间区间已有其他直播。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "加载直播间成员数量")
    @PostMapping(value="onlive/loadRoomMemberCount")
    public ResponseEntity loadRoomMemberCount(HttpServletRequest request){
        String roomNo = request.getParameter("roomno");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        ExecResult er = new ExecResult();
        JSONObject data = new JSONObject();
        if(room!=null){
            er.setResult(true);
            data.put("count", room.getMembers().keySet().size());
            er.setData(data);
            return ResponseEntity.ok().body(er);
        }else{
            data.put("count", 0);
            er.setResult(false);
            er.setData(data);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "删除直播间")
    @PostMapping(value="onlive/deleteRoom")
    public ResponseEntity deleteRoom(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        boolean result = onliveRoomService.deleteOnliveRoom(roomNo);
        if(result){
            ExecResult er=  new ExecResult(true,"删除成功");
            return ResponseEntity.ok().body(er);
        }else{
            ExecResult er = new ExecResult(false,"删除失败，您的直播间正在直播。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "管理直播间")
    @PostMapping(value="onlive/getUserRoom")
    public ResponseEntity getUserRoom(HttpServletRequest request){
        String openId = request.getParameter("openId");
        logger.error("getuserroom.do>>>>openId:"+openId);
        try {
            String input = IOUtils.toString(request.getInputStream());
            logger.error("getuserroom.do>>>>input:"+input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<OnliveRoom> rooms = onliveRoomService.findOnliveRoomByOpenId(openId);
        JSONArray array = new JSONArray();
        for(int i = 0;i < rooms.size();i++){
            JSONObject room=new JSONObject();
            room.put("roomNo",rooms.get(i).getRoomNo());
            room.put("roomName",rooms.get(i).getName());
            room.put("roomDesc",rooms.get(i).getDescription());
            room.put("cover",rooms.get(i).getCover());
            array.add(room);
        }
        ExecResult er=new ExecResult();
        er.setResult(true);
        er.setData(array);
        return ResponseEntity.ok().body(er);
    }

    @ApiOperation(value = "获取直播间最新动态")
    @PostMapping(value="onlive/getRoomLatestInfo")
    public ResponseEntity getRoomLatestInfo(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }else{
            JSONObject info = new JSONObject();
            info.put("latesttext", room.getLatestText());
            info.put("latestpic", room.getLatestPic());
            ExecResult er = new ExecResult(true,"获取成功");
            er.setData(info);
            return ResponseEntity.ok().body(er);
        }
    }

    @ApiOperation(value = "更新直播间")
    @PostMapping(value="onlive/renameRoom")
    public ResponseEntity renameRoom(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String newName = request.getParameter("name");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }else{
            room.setName(newName);
            onliveRoomService.updateOnliveRoom(room);
            ExecResult er = new ExecResult(true,"更新成功");
            return ResponseEntity.ok().body(er);
        }
    }

    @ApiOperation(value = "设置选项")
    @PostMapping(value="onlive/setSwitch")
    public ResponseEntity setSwitch(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String value = request.getParameter("value");
        String config = request.getParameter("config");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room==null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
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
            ExecResult er = new ExecResult(true,"更新成功");
            return ResponseEntity.ok().body(er);
        }
    }
}
