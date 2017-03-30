package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Wang on 2017/3/27.
 */
@Repository
public interface OnliveRoomRepository extends JpaRepository<OnliveRoom,Long> {
    OnliveRoom findOnliveRoomByRoomNo(Long roomNo);
}