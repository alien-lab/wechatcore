package com.alienlab.wechat.utils;

import com.alienlab.wechat.common.ExecResult;
import com.alienlab.wechat.common.JSONResponse;
import com.alienlab.wechat.common.TypeUtils;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.service.OnliveRoomService;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.asyn4j.core.callback.AsynCallBack;

/**
 * 信息流文件下载回调函数
 * @author 橘
 *
 */
@SuppressWarnings("serial")
public class StreamFileCallback extends AsynCallBack {
	private static Logger logger = Logger.getLogger(StreamFileCallback.class);
	private OnliveRoomService onliveRoomService;
	@Override
	public void doNotify() {
		ExecResult er=(ExecResult)this.methodResult;
		JSONObject data=(JSONObject)er.getData();
		logger.info("StreamFileCallback>>>>"+er.toString());
		
		String sql="update wx_onlive_content set content_link='"+data.getString("filename")+"',download_time='"+ TypeUtils.getTime()+"' where content_no="+data.getString("streamno");
		JSONResponse jr=new JSONResponse();
		jr.getExecResult(sql, null);
		
		sql="SELECT * FROM `wx_onlive_content` a WHERE content_no="+data.getString("streamno");
		ExecResult result=jr.getSelectResult(sql, null, "wx_onlive_content");
		if(result.getResult()>0){
			JSONArray ja=(JSONArray)result.getData();
			if(ja.size()>0){
				JSONObject stream=ja.getJSONObject(0);
				String type=stream.getString("content_type");
				String roomno=stream.getString("bc_no");
				OnliveRoom room = onliveRoomService.findOnliveRoomByRoomNo(roomno);
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
