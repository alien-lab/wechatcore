package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wang on 2017/3/28.
 */
@Repository
public interface OnliveCommentRepository extends JpaRepository<OnliveComment,Long>{
    @Query("FROM OnliveComment a,OnliveMember b WHERE a.roomNo=b.roomNo AND a.openId=b.openId AND a.streamNo=?1 ORDER BY a.commentTime")
    List<OnliveComment> findOnliveCommentByStreamNoOrderByCommentTimeDesc(Long streamNo);

    @Query("FROM OnliveComment a,OnliveMember b WHERE a.roomNo=b.roomNo AND a.openId=b.openId AND a.roomNo=?1 ORDER BY a.streamNo,a.commentTime")
    List<OnliveComment> findOnliveCommentByRoomNoOrderByStreamNoAndCommentTimeDesc(String roomNo);
}
