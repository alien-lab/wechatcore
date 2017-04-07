package com.alienlab.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.common.TypeUtils;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.entity.OnliveStream;
import com.alienlab.wechat.service.OnliveRoomService;
import com.alienlab.wechat.service.OnliveStreamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Wang on 2017/4/6.
 */
@Api(value="/wechatcore-api/OnliveStream",description = "内容流API")
@RestController
@RequestMapping("/wechatcore-api")
public class StreamController {
    @Autowired
    private OnliveStreamService onliveStreamService;
    @Autowired
    private OnliveRoomService onliveRoomService;

    @ApiOperation(value = "加载内容")
    @PostMapping(value="onlive/loadstream")
    public ResponseEntity loadStream(HttpServletRequest request){
        String time = request.getParameter("time");
        String direction = request.getParameter("direction");
        String sorttype = request.getParameter("sorttype");
        //如果正序排列
        if(sorttype!=null&&sorttype.equals("asc")){
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
        }
        String roomNo = request.getParameter("roomNo");
        ExecResult er = new ExecResult();
        if(roomNo != null && !roomNo.equals("")){
            List<OnliveStream> streams = onliveStreamService.getStreams(roomNo, time, direction, sorttype);
            er.setResult(true);
            er.setData((JSON)JSON.toJSON(streams));
            return ResponseEntity.ok().body(er);
        }else{
            er.setResult(false);
            er.setMessage("参数传递错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "更新内容")
    @PostMapping(value="onlive/reloaditem")
    public ResponseEntity reloaditem(HttpServletRequest request){
        String streamNo = request.getParameter("streamNo");
        long contentNo  = Long.parseLong(streamNo);
        OnliveStream stream = onliveStreamService.loadStreams(contentNo);
        JSON.toJSONString(stream);
        ExecResult er=  new ExecResult(true,"加载成功");
        return ResponseEntity.ok().body(er);
    }

    @ApiOperation(value = "发布内容")
    @PostMapping(value="onlive/publishStream")
    public ResponseEntity publishStream(HttpServletRequest request){
        String roomNo = request.getParameter("roomNo");
        OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
        ExecResult er = new ExecResult();
        if(room != null){
            JSONObject s = new JSONObject();
            s.put("openid", request.getParameter("openId"));
            s.put("content_type", request.getParameter("type"));
            s.put("member_nick", request.getParameter("nick"));
            s.put("content_time", TypeUtils.getTime(new Date(), "yyyyMMddHHmmss"));
            s.put("bc_no", roomNo);
            s.put("content_body", request.getParameter("content"));
            s.put("content_welink", request.getParameter("mediaId"));
            if(onliveStreamService.publishStream(s)){
                er.setResult(true);
                er.setMessage("发布成功");
                return ResponseEntity.ok().body(er);
            }else{
                er.setResult(false);
                er.setErrormsg("内容发布失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
            }
        }else{
            er.setResult(false);
            er.setErrormsg("未找到直播间。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }
}
