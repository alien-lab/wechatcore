package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wang on 2017/3/27.
 */
@Repository
public interface OnliveStreamRepository extends JpaRepository<OnliveStream,Long>{
    OnliveStream findOnliveStreamByContentNo(Long contentNo);

    @Query("SELECT a.content_no,a.bc_no,a.content_type,a.content_time,a.`openid`,a.content_body,a.content_link," +
            "a.`content_piclink`,b.`member_nick`,b.`member_pic`,c.`bc_cover`,c.`bc_vip_cover` FROM `wx_onlive_content` a,`wx_onlive_members` b,`wx_onlive_broadcasting` c WHERE a.`bc_no` = ?1 AND a.`openid`=b.`member_openid` AND a.`bc_no`=c.`bc_no` AND a.`bc_no`=b.`bc_no` AND content_time ?3 ' ?2 ' ORDER BY content_time ?4 LIMIT 0,10 ")
    List<OnliveStream> findOnliveStreamByRoomNoOrderByContentTimeDesc(String roomNo, String date, String compare, String sorttype);
}
