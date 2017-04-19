package com.alienlab.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.entity.OnliveMember;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.service.NamelistItemService;
import com.alienlab.wechat.service.OnliveMemberService;
import com.alienlab.wechat.service.OnliveRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by Wang on 2017/4/7.
 */
@Api(value="/wechatcore-api/OnliveMember",description = "直播间成员API")
@RestController
@RequestMapping("/wechatcore-api")
public class MemberController {
    @Autowired
    private NamelistItemService namelistItemService;
    @Autowired
    private OnliveMemberService onliveMemberService;
    @Autowired
    private OnliveRoomService onliveRoomService;

    @ApiOperation(value = "加入直播间")
    @PostMapping(value="onlive/joinRoom")
    public ResponseEntity joinRoom(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String openId = request.getParameter("openId");
        String nick = request.getParameter("nick");
        String headerimg = request.getParameter("headerimg");
        String joinType = request.getParameter("joinType");
        String isVip = request.getParameter("isVip");
        JSONObject member = new JSONObject();
        member.put("member_join_type", joinType);
        member.put("member_pic", headerimg);
        member.put("member_nick", nick);
        member.put("member_openid", openId);
        member.put("member_phone", "");
        member.put("bc_no", roomNo);
        member.put("member_unionid", "");
        member.put("member_vip", isVip);
        OnliveMember onliveMember = new OnliveMember(member);
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        ExecResult er = new ExecResult();
        if(room!=null){
            onliveMemberService.joinRoom(onliveMember);
            er.setResult(true);
            er.setMessage("加入成功");
            return ResponseEntity.ok().body(er);
        }else{
            er.setResult(false);
            er.setMessage("加入失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "查看直播间成员")
    @GetMapping(value="onlive/getMembers")
    public ResponseEntity getMembers(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String openId = request.getParameter("openId");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }else{
            if(!namelistItemService.findNamelistItemByPhone(room.getManagerPhone()).getOpenId().equals(openId)){
                ExecResult er = new ExecResult(false,"对不起，您不是直播间管理员。");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
            List<OnliveMember> members = new ArrayList<>();
            List<OnliveMember> onliveMemberList = onliveMemberService.findOnliveMemberByRoomNo(roomNo);
            for(OnliveMember e : onliveMemberList){
                members.add(e);
            }
            ExecResult er = new ExecResult();
            er.setResult(true);
            er.setData((JSON)JSON.toJSON(members));
            return ResponseEntity.ok().body(er);
        }
    }

    @ApiOperation(value = "设置为直播间嘉宾")
    @PutMapping(value="onlive/setVip")
    public ResponseEntity setVip(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String openId = request.getParameter("openId");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        OnliveMember member = onliveMemberService.getOnliveMember(roomNo, openId);
        if(member!=null){
            if(onliveMemberService.addSpeaker(member)){
                ExecResult er = new ExecResult(true,"设置成功");
                return ResponseEntity.ok().body(er);
            }else{
                ExecResult er = new ExecResult(false,"保存失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }else{
            ExecResult er = new ExecResult(false,"直播间不存在该成员");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "移除直播间嘉宾")
    @PutMapping(value="onlive/removeVip")
    public ResponseEntity removeVip(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String openId = request.getParameter("openId");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if(onliveMemberService.removeSpeaker(openId, roomNo)){
            ExecResult er = new ExecResult(true,"设置成功");
            return ResponseEntity.ok().body(er);
        }else{
            ExecResult er = new ExecResult(false,"保存失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
}
