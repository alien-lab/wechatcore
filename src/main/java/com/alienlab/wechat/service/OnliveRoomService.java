package com.alienlab.wechat.service;

import com.alienlab.wechat.entity.NamelistItem;
import com.alienlab.wechat.entity.OnliveMember;
import com.alienlab.wechat.entity.OnliveRoom;
import com.alienlab.wechat.repository.NamelistItemRepository;
import com.alienlab.wechat.repository.OnliveMemberRepository;
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
    private NamelistItemRepository namelistItemRepository;
    @Autowired
    private OnliveRoomRepository onliveRoomRepository;
    @Autowired
    private OnliveMemberRepository onliveMemberRepository;

    //新增直播间
    public OnliveRoom addOnliveRoom(OnliveRoom onliveRoom){
        if(onliveRoom != null){
            return  onliveRoomRepository.save(onliveRoom);
        }else{
            return null;
        }
    }

    //根据直播间号查找直播间
    public OnliveRoom findOnliveRoomByRoomNo(String roomNo){
        return onliveRoomRepository.findOnliveRoomByRoomNo(roomNo);
    }

    //根据成员微信号查找直播间
    public List<OnliveRoom> findOnliveRoomByOpenId(String openId){
        NamelistItem listItem = namelistItemRepository.findNamelistItemByOpenId(openId);
        List<OnliveRoom> roomList = onliveRoomRepository.findOnliveRoomByManagerPhone(listItem.getPhone());
        return roomList;
    }

    //查找所有直播间
    public List<OnliveRoom> findAllOnliveRoom(){
        return onliveRoomRepository.findAll();
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
    public boolean deleteOnliveRoom(String roomNo){
        try{
            onliveRoomRepository.deleteOnliveRoomByRoomNo(roomNo);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getSpeakersOpenId(String roomNo){
        List<OnliveMember> speakers = onliveMemberRepository.findOnliveMemberByRoomNo(roomNo);
        String openids = "";
        for(int i = 0;i < speakers.size();i++){
            if(i == 0){
                openids += speakers.get(i).getOpenId();
            }else{
                openids += ","+speakers.get(i).getOpenId();
            }
        }
        return openids;
    }

    public String getLayoutTime(String roomNo){
        OnliveRoom room = onliveRoomRepository.findOnliveRoomByRoomNo(roomNo);
        String s = room.getStartTime();
        String e = room.getEndTime();
        String date = "";
        String time = "";
        if(s.substring(0,8).equals(e.substring(0,8))){
            date = s.substring(0,8);
            date = date.substring(0,4)+"/"+date.substring(4,6)+"/"+date.substring(6,8);
            time = s.substring(8,12)+e.substring(8,12);
            time = time.substring(0,2)+":"+time.substring(2,4)+"~"+time.substring(4,6)+":"+time.substring(6,8);

            return date+" "+time;
        }else{
            return room.getStartTime()+"~"+room.getEndTime();
        }
    }
}
