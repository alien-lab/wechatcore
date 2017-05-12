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

    //发布评论
    public void publishComment(String openId, Long streamNo, String content){
        OnliveComment comment = new OnliveComment();
        comment.setOpenId(openId);
        comment.setStreamNo(streamNo);
        comment.setContent(content);
        onliveCommentRepository.save(comment);
        updateOnliveComment(comment);
    }

    //根据评论时间顺序获得当前内容下的所有评论
    public List<OnliveComment> loadComments(Long streamNo){
        return onliveCommentRepository.findOnliveCommentByStreamNoOrderByCommentTimeDesc(streamNo);
    }

    //根据内容流序号和时间顺序获得当前直播间的所有评论
    public List<OnliveComment> getComments(String roomNo){
        return onliveCommentRepository.findOnliveCommentByRoomNoOrderByStreamNoAndCommentTimeDesc(roomNo);
    }

    //更新评论
    public OnliveComment updateOnliveComment(OnliveComment onliveComment){
        try{
            onliveComment = onliveCommentRepository.save(onliveComment);
            return onliveComment;
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
