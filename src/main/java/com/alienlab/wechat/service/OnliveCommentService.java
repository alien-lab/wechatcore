package com.alienlab.wechat.service;

import com.alienlab.wechat.entity.OnliveComment;
import com.alienlab.wechat.repository.OnliveCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Wang on 2017/3/29.
 */
@Service
public class OnliveCommentService {
    @Autowired
    private OnliveCommentRepository onliveCommentRepository;

    //新增评论
    public OnliveComment addOnliveComment(OnliveComment onliveComment){
        if(onliveComment != null){
            return  onliveCommentRepository.save(onliveComment);
        }else{
            return null;
        }
    }

    //获得当前内容下的所有评论
    public List<OnliveComment> loadComments(Long roomNo, String openId, Long streamNo){
        return onliveCommentRepository.findOnliveCommentByRoomNoAndOpenIdOrderAndStreamNoOrderByCommentTimeDesc(roomNo, openId, streamNo);
    }

    //获得当前直播间的所有评论
    public List<OnliveComment> getComments(Long roomNo, String openId){
        return onliveCommentRepository.findOnliveCommentByRoomNoAndOpenIdOrderByStreamNoAndCommentTimeDesc(roomNo, openId);
    }

    //更新评论
    public OnliveComment updateOnliveComment(OnliveComment onliveComment){
        try{
            return onliveCommentRepository.save(onliveComment);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //删除评论
    public boolean deleteOnliveComment(Long commentNo){
        try{
            onliveCommentRepository.delete(commentNo);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
