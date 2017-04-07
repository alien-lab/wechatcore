package com.alienlab.wechat.controller;

import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.common.TextInfo;
import com.alienlab.wechat.entity.OnliveMember;
import com.alienlab.wechat.entity.OnlivePraise;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.message.PushMessage;
import com.alienlab.wechat.service.OnlivePraiseService;
import com.alienlab.wechat.service.OnliveRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wang on 2017/4/7.
 */
@Api(value="/wechatcore-api/OnlivePraise",description = "内容点赞API")
@RestController
@RequestMapping("/wechatcore-api")
public class PraiseController {
    @Autowired
    private OnlivePraiseService onlivePraiseService;
    @Autowired
    private OnliveRoomService onliveRoomService;

    @ApiOperation(value = "发布点赞")
    @PostMapping(value="onlive/publishPraise")
    public ResponseEntity publishPraise(HttpServletRequest request){
        String openId = request.getParameter("openid");
        String roomNo = request.getParameter("roomno");
        String streamno = request.getParameter("streamno");
        Long streamNo = Long.parseLong(streamno);
        OnlivePraise praise = new OnlivePraise(streamNo, roomNo, openId);
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }else{
            onlivePraiseService.addOnlivePraise(praise);
            ExecResult er = new ExecResult(true,"点赞成功");
            if(er.getResult()>0){
                if(room.getCommentMsg().equals("1")){
                    OnliveMember member = room.getMembers().get(openId);
                    if(member!=null){
                        String text = member.getNick()+" 赞了您的直播内容。";
                        TextInfo ti = new TextInfo(text);
                        ti.setToUserName(room.getManager().getOpenId());
                        PushMessage.sendMessage(ti);
                    }
                }
            }
            return ResponseEntity.ok().body(er);
        }
    }


}
