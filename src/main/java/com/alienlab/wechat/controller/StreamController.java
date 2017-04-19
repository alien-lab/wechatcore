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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name="roomNo",value="直播间编号",paramType = "query"),
            @ApiImplicitParam(name="time",value="时间条件",paramType = "query"),
            @ApiImplicitParam(name="index",value="分页位置",paramType = "query"),
            @ApiImplicitParam(name="length",value="分页长度",paramType = "query")
    })
    @GetMapping(value="onlive/loadstream")
    public ResponseEntity loadStream(HttpServletRequest request, @RequestParam String time, @RequestParam int index, @RequestParam int length){
        String sorttype = request.getParameter("sorttype");
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
        String roomNo = request.getParameter("roomNo");
        ExecResult er = new ExecResult();
        if(roomNo != null && !roomNo.equals("")){
            if(sorttype!=null&&sorttype.equals("asc")){
                Page<OnliveStream> streamsASC = onliveStreamService.getStreamsASC(roomNo, time, new PageRequest(index, length));
                er.setResult(true);
                er.setData((JSON)JSON.toJSON(streamsASC));
                return ResponseEntity.ok().body(er);
            }else{
                Page<OnliveStream> streamsDESC = onliveStreamService.getStreamsDESC(roomNo, time, new PageRequest(index, length));
                er.setResult(true);
                er.setData((JSON)JSON.toJSON(streamsDESC));
                return ResponseEntity.ok().body(er);
            }
        }else{
            er.setResult(false);
            er.setMessage("参数传递错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
        }
    }

    @ApiOperation(value = "更新内容")
    @GetMapping(value="onlive/reloaditem")
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
