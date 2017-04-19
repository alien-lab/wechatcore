package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wang on 2017/3/27.
 */
@Repository
public interface OnliveStreamRepository extends JpaRepository<OnliveStream,Long>{
    OnliveStream findOnliveStreamByContentNo(Long contentNo);


    @Query("SELECT a.contentNo,a.roomNo,a.contentType,a.time,a.openId,a.content,a.link,a.contentPicLink,b.nick,b.localPic,c.cover,c.brandCover FROM OnliveStream a,OnliveMember b,OnliveRoom c WHERE a.roomNo='?1' AND a.openId=b.openId AND a.roomNo=c.roomNo AND a.roomNo=b.roomNo AND a.time > '?2' ORDER BY a.time desc")
    Page<OnliveStream> findStreamDESC(String roomNo, String date, Pageable page);

    @Query(value = "SELECT a.contentNo,a.roomNo,a.contentType,a.time,a.openId,a.content,a.link,a.contentPicLink,b.nick,b.localPic,c.cover,c.brandCover FROM OnliveStream a,OnliveMember b,OnliveRoom c WHERE a.roomNo='?1' AND a.openId=b.openId AND a.roomNo=c.roomNo AND a.roomNo=b.roomNo AND a.time < '?2' ORDER BY a.time")
    Page<OnliveStream> findStreamASC(String roomNo, String date, Pageable page);
}
