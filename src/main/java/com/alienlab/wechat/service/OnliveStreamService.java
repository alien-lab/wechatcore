package com.alienlab.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.common.TypeUtils;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.entity.OnliveStream;
import com.alienlab.wechat.repository.OnliveStreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Wang on 2017/3/30.
 */
@Service
public class OnliveStreamService {
    @Autowired
    private OnliveStreamRepository onliveStreamRepository;

    //新增内容流
    public OnliveStream addOnliveStream(OnliveStream onliveStream){
        if(onliveStream != null){
            return  onliveStreamRepository.save(onliveStream);
        }else{
            return null;
        }
    }

    //根据内容流序号查找内容流
    public OnliveStream loadStreams(Long contentNo){
        return onliveStreamRepository.findOnliveStreamByContentNo(contentNo);
    }

    //根据发布时间顺序获得内容流
    public List<OnliveStream> getStreams(String roomNo, String date, String compare, String sorttype){
        return onliveStreamRepository.findOnliveStreamByRoomNoOrderByContentTimeDesc(roomNo, date, compare, sorttype);
    }

    //更新内容流
    public OnliveStream updateOnliveStream(OnliveStream onliveStream){
        try{
            return onliveStreamRepository.save(onliveStream);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //删除内容流
    public boolean deleteOnliveStream(Long streamNo){
        try{
            onliveStreamRepository.delete(streamNo);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean publishStream(JSONObject s){
        String now = TypeUtils.getTime();
        OnliveRoom room = new OnliveRoom();
        if(now.compareTo(room.getEndTime())>0){
            return false;
        }
        OnliveStream stream = new OnliveStream(s);
        //记录入库
        stream.setRoomNo(room.getRoomNo());
        onliveStreamRepository.save(stream);
        stream.downLoadMedia();
        if(stream.getContentType().equals("text")){
            room.setLatestText(stream.getContent());
        }
        return true;
    }
}
