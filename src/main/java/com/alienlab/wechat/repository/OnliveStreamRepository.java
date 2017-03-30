package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wang on 2017/3/27.
 */
@Repository
public interface OnliveStreamRepository extends JpaRepository<OnliveStream,Long>{
    OnliveStream findOnliveStreamByContentNo(Long contentNo);

    List<OnliveStream> findOliveStreamByRoomNoAndOpenIdAndContentTimeOrderByContentTimeDesc(Long roomNo, String openId, String contentTime);

    List<OnliveStream> findOnliveStreamByContentNoAndRoomNoAndOpenId(Long contentNo, Long roomNo, String openId);
}
