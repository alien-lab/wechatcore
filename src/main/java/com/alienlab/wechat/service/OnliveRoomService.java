package com.alienlab.wechat.service;

import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.repository.OnliveRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Wang on 2017/3/30.
 */
@Service
public class OnliveRoomService {
    @Autowired
    private OnliveRoomRepository onliveRoomRepository;

    //新增直播间
    public OnliveRoom addOnliveRoom(OnliveRoom onliveRoom){
        if(onliveRoom != null){
            return  onliveRoomRepository.save(onliveRoom);
        }else{
            return null;
        }
    }

    //根据直播间号查找直播间
    public OnliveRoom findOnliveRoomByRoomNo(Long roomNo){
        return onliveRoomRepository.findOnliveRoomByRoomNo(roomNo);
    }

    //根据成员微信查找直播间
    public List<OnliveRoom> findOnliveRoomByOpenId(String openId){
        return onliveRoomRepository.findOnliveRoomByOpenId(openId);
    }

    //更新直播间
    public OnliveRoom updateOnliveRoom(OnliveRoom onliveRoom){
        try{
            return onliveRoomRepository.save(onliveRoom);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //删除直播间
    public boolean deleteOnliveRoom(Long roomNo){
        try{
            onliveRoomRepository.delete(roomNo);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
