package com.alienlab.wechat.utils;

import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.common.TypeUtils;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.service.OnliveRoomService;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.asyn4j.core.callback.AsynCallBack;

/**
 * 信息流缩略图下载回调函数
 * @author 橘
 *
 */

@SuppressWarnings("serial")
public class StreamThumbCallback extends AsynCallBack {
	private static Logger logger = Logger.getLogger(StreamThumbCallback.class);
	private OnliveRoomService onliveRoomService;
	@Override
	public void doNotify() {
		ExecResult er = (ExecResult)this.methodResult;
		logger.info("StreamThumbCallback>>>>"+er.toString());
		JSONObject data = (JSONObject)er.getData();
		String sql = "update wx_onlive_content set content_piclink='"+data.getString("filename")+"',download_pic_time='"+ TypeUtils.getTime()+"' where content_no="+data.getString("streamno");
//		JSONResponse jr = new JSONResponse();
//		jr.getExecResult(sql, null);
		sql = "SELECT * FROM `wx_onlive_content` a WHERE content_no="+data.getString("streamno");
		ExecResult result = new ExecResult(true, "wx_onlive_content");
		if(result.getResult()>0){
			JSONArray ja = (JSONArray)result.getData();
			if(ja.size()>0){
				JSONObject stream = ja.getJSONObject(0);
				String type = stream.getString("content_type");
				String roomNo = stream.getString("bc_no");
				OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomNo);
				if(type.equals("image")){
					room.setLatestPic(stream.getString("content_link"));
				}else if(type.equals("shortvideo")||type.equals("video")){
					room.setLatestPic(stream.getString("content_piclink"));
				}
				onliveRoomService.updateOnliveRoom(room);
			}
			
		}
	}

}
