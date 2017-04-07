package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wang on 2017/3/27.
 */
@Repository
public interface OnliveRoomRepository extends JpaRepository<OnliveRoom,Long> {
    OnliveRoom findOnliveRoomByRoomNo(String roomNo);

    List<OnliveRoom> findOnliveRoomByOpenId(String openId);

    boolean deleteOnliveRoomByRoomNo(String roomNo);
}
