package com.alienlab.wechat.service;

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
    public OnliveStream findOnliveStreamByContentNo(Long contentNo){
        return onliveStreamRepository.findOnliveStreamByContentNo(contentNo);
    }

    //根据发布时间顺序获得内容流
    public List<OnliveStream> loadStreams(Long roomNo, String openId, String contentTime){
        return onliveStreamRepository.findOliveStreamByRoomNoAndOpenIdAndContentTimeOrderByContentTimeDesc(roomNo, openId, contentTime);
    }

    //
    public OnliveStream getStreams(Long contentNo, Long roomNo, String openId){
        return onliveStreamRepository.findOnliveStreamByContentNoAndRoomNoAndOpenId(contentNo, roomNo, openId);
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
}
