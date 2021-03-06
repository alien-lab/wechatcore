package com.alienlab.wechat.service;

import com.alienlab.wechat.common.ResponseConfig;
import com.alienlab.wechat.message.PushMessage;
import com.alienlab.wechat.common.TextInfo;
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
public class OnliveMemberService {
    @Autowired
    private NamelistItemRepository namelistItemRepository;
    @Autowired
    private OnliveMemberRepository onliveMemberRepository;
    @Autowired
    private OnliveRoomRepository onliveRoomRepository;

    //新增直播间成员
    public OnliveMember addOnliveMember(OnliveMember onliveMember){
        if(onliveMember != null){
            return  onliveMemberRepository.save(onliveMember);
        }else{
            return null;
        }
    }

    //根据直播间编号和成员微信查找直播间成员
    public OnliveMember findOnliveMemberByRoomNoAndOpenId(String roomNo,String openId){
        return onliveMemberRepository.findOnliveMemberByRoomNoAndOpenId(roomNo, openId);
    }

    //根据直播间编号查找直播间成员
    public List<OnliveMember> findOnliveMemberByRoomNo(String roomNo){
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
    public boolean deleteOnliveMember(String roomNo, String openId){
        try{
            onliveMemberRepository.deleteOnliveMemberByRoomNoAndOpenId(roomNo, openId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public OnliveMember getOnliveMember(String roomNo, String openId){
        List<OnliveMember> members = onliveMemberRepository.findOnliveMemberByRoomNo(roomNo);
        for(OnliveMember member : members){
            if(member.getOpenId().equals(openId)){
                return member;
            }
        }
        return null;
    }

    public OnliveMember getMember(String roomNo, String nickName){
        List<OnliveMember> members = onliveMemberRepository.findOnliveMemberByRoomNo(roomNo);
        for(OnliveMember member : members){
            if(member.getNick().equals(nickName)){
                return member;
            }
        }
        return null;
    }

    //成员加入直播间
    public boolean joinRoom(OnliveMember member){
        //如果在列表中不存在，表示新用户
        OnliveRoom room = new OnliveRoom();
        if(getOnliveMember(room.getRoomNo(),member.getOpenId()) != null){
            if(onliveMemberRepository.save(member) != null){
                if(room.getJoinMsg().equals("1")){
                    String text = member.getNick()+" 进入您的直播间《"+room.getName()+"》。";
                    TextInfo ti = new TextInfo(text);
                    ti.setToUserName(member.getOpenId());
                    PushMessage.sendMessage(ti);
                }
            }
        }else{
            //如果成员已存在，获得成员是否vip属性，更新member
            OnliveMember existsmem = getOnliveMember(room.getRoomNo(), member.getOpenId());
            member.setVip(existsmem.isVip());
            onliveMemberRepository.save(member);
        }
       room.getMembers().concat(member.getOpenId());
        /*if(member.isVip()){
        	addSpeaker(member);
        }*/
        return true;
    }

    //添加嘉宾
    public boolean addSpeaker(String nickName, OnliveRoom room){
        OnliveMember member = getMember(room.getRoomNo(),nickName);
        member.setVip(true);
        return addSpeaker(member);
    }

    public boolean addSpeaker(OnliveMember member){
        OnliveRoom room = onliveRoomRepository.findOnliveRoomByRoomNo(member.getRoomNo());
        if(!member.isVip()){
            member.setVip(true);
            onliveMemberRepository.save(member);
            TextInfo t = new TextInfo();
            t.setToUserName(member.getOpenId());
            t.setContent(namelistItemRepository.findNamelistItemByPhone(room.getManagerPhone()).getNickName()+"邀请您做他的直播嘉宾，快进来看看吧：<a href=\""+room.getShareLink()+"\">"+room.getName()+"</a>");
            PushMessage.sendMessage(t);
        }
        room.getSpeakers().concat(member.getOpenId());

        //此处需要增加响应监听
        ResponseConfig rc = ResponseConfig.getConfigById(room.getRoomNo());
        String rcOpenId = rc.getUseropenid();
        if("".equals(rcOpenId)){
            rcOpenId = member.getOpenId();
        }else{
            if(rcOpenId.indexOf(member.getOpenId())>=0){
                //已存在该用户监听
            }else{
                rcOpenId+=","+member.getOpenId();
            }
        }
        rc.setUseropenid(rcOpenId);
        return true;
    }

    //移除嘉宾
    public boolean removeSpeaker(String openId, String roomNo){
        OnliveRoom room = onliveRoomRepository.findOnliveRoomByRoomNo(roomNo);
        if(getOnliveMember(roomNo,openId)!=null){
            OnliveMember member = getOnliveMember(roomNo, openId);
            if(member.isVip()){
                return removeSpeaker(member);
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public boolean removeSpeaker(OnliveMember member){
        OnliveRoom room = onliveRoomRepository.findOnliveRoomByRoomNo(member.getRoomNo());
        if(member.isVip()){
            member.setVip(false);
            onliveMemberRepository.save(member);
        }
        room.getSpeakers().replace(member.getOpenId(),"");

        //此处删除响应监听
        ResponseConfig rc=ResponseConfig.getConfigById(room.getRoomNo());
        String rcopenid=rc.getUseropenid();
        if(rcopenid.indexOf(member.getOpenId())>=0){
            rcopenid=rcopenid.replace(member.getOpenId(), "");
        }
        rc.setUseropenid(rcopenid);
        return true;
    }
}
