package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnliveMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wang on 2017/3/27.
 */
@Repository
public interface OnliveMemberRepository extends JpaRepository<OnliveMember,Long>{
    OnliveMember findOnliveMemberByRoomNoAndOpenId(Long roomNo,String openId);

    List<OnliveMember> findOnliveMemberByRoomNo(Long roomNo);
}
