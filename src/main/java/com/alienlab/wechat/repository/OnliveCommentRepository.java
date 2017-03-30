package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Wang on 2017/3/28.
 */
public interface OnliveCommentRepository extends JpaRepository<OnliveComment,Long>{
    List<OnliveComment> findOnliveCommentByRoomNoAndOpenIdOrderAndStreamNoOrderByCommentTimeDesc(Long roomNo, String openId, Long streamNo);

    List<OnliveComment> findOnliveCommentByRoomNoAndOpenIdOrderByStreamNoAndCommentTimeDesc(Long roomNo, String openId);
}