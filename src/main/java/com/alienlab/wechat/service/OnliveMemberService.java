package com.alienlab.wechat.service;

import com.alienlab.wechat.entity.OnliveMember;
import com.alienlab.wechat.repository.OnliveMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Wang on 2017/3/30.
 */
@Service
public class OnliveMemberService {
    @Autowired
    private OnliveMemberRepository onliveMemberRepository;

    //新增直播间成员
    public OnliveMember addOnliveMember(OnliveMember onliveMember){
        if(onliveMember != null){
            return  onliveMemberRepository.save(onliveMember);
        }else{
            return null;
        }
    }

    //根据直播间编号和成员微信查找直播间成员
    public OnliveMember findOnliveMemberByRoomNoAndOpenId(Long roomNo,String openId){
        return onliveMemberRepository.findOnliveMemberByRoomNoAndOpenId(roomNo, openId);
    }

    //根据直播间编号查找直播间成员
    public List<OnliveMember> findOnliveMemberByRoomNo(Long roomNo){
        return onliveMemberRepository.findOnliveMemberByRoomNo(roomNo);
    }

    //更新直播间成员
    public OnliveMember updateOnliveMember(OnliveMember onliveMember){
        try{
            return onliveMemberRepository.save(onliveMember);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //删除直播间成员
    public boolean deleteOnliveMember(Long roomNo, String openId){
        try{
            onliveMemberRepository.deleteOnliveMemberByRoomNoAndOpenId(roomNo, openId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
