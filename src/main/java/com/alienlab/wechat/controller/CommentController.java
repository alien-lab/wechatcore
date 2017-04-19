package com.alienlab.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.common.TextInfo;
import com.alienlab.wechat.entity.OnliveComment;
import com.alienlab.wechat.entity.OnliveMember;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.message.PushMessage;
import com.alienlab.wechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Wang on 2017/4/7.
 */
@Api(value="/wechatcore-api/OnliveComment",description = "内容评论API")
@RestController
@RequestMapping("/wechatcore-api")
public class CommentController {
    @Autowired
    private NamelistItemService namelistItemService;
    @Autowired
    private OnliveCommentService onliveCommentService;
    @Autowired
    private OnliveMemberService onliveMemberService;
    @Autowired
    private OnliveRoomService onliveRoomService;
    @Autowired
    private OnlivePraiseService onlivePraiseService;

    @ApiOperation(value = "发布评论")
    @PostMapping(value="onlive/publishComment")
    public ResponseEntity publishComment(HttpServletRequest request){
        String openId = request.getParameter("openId");
        String roomNo = request.getParameter("roomNo");
        String streamno = request.getParameter("streamNo");
        Long streamNo = Long.parseLong(streamno);
        String content = request.getParameter("content");
        OnliveComment comment = new OnliveComment(streamNo, roomNo, openId, content);
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room.getSpeakMode().equals("speaker")){
            ExecResult er = new ExecResult(false,"直播间未开放评论");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }else{
            onliveCommentService.addOnliveComment(comment);
            ExecResult er = new ExecResult(true,"评论成功");
            if(er.getResult()>0){
                if(room.getCommentMsg().equals("1")){
                    OnliveMember member = onliveMemberService.getOnliveMember(roomNo, openId);
                    if(member!=null){
                        String text = member.getNick()+" 评论了您："+comment;
                        TextInfo ti = new TextInfo(text);
                        ti.setToUserName(namelistItemService.findNamelistItemByPhone(room.getManagerPhone()).getOpenId());
                        PushMessage.sendMessage(ti);
                    }
                }
            }
            return ResponseEntity.ok().body(er);
        }
    }

    @ApiOperation(value = "删除评论")
    @DeleteMapping(value="onlive/deleteComment")
    public ResponseEntity deleteComment(HttpServletRequest request){
        String commentno = request.getParameter("commentNo");
        Long commentNo = Long.parseLong(commentno);
        String roomNo = request.getParameter("roomNo");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult er = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }else{
            onliveCommentService.deleteOnliveComment(commentNo);
            ExecResult er = new ExecResult(true,"删除成功");
            return ResponseEntity.ok().body(er);
        }
    }

    @ApiOperation(value = "查看点赞和评论")
    @GetMapping(value="onlive/getPraiseComment")
    public ResponseEntity getPraiseComment(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        String streamno = request.getParameter("streamNo");
        Long streamNo = Long.parseLong(streamno);
        ExecResult er = new ExecResult();
        JSONObject data = new JSONObject();
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        if(room == null){
            ExecResult execResult = new ExecResult(false,"直播间不存在");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(execResult);
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
            return ResponseEntity.ok().body(er);
        }
    }
}
