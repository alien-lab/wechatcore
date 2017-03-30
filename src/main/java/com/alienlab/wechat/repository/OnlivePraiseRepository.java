package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnlivePraise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Wang on 2017/3/28.
 */
public interface OnlivePraiseRepository extends JpaRepository<OnlivePraise,Long>{
    OnlivePraise findOnlivePariseByStreamNoAndOpenId(Long streamNo, String openId);

    List<OnlivePraise> findOnlivePraiseByStreamNoOrderByPraiseTime(Long streamNo);

    List<OnlivePraise> findOnlivePariseByRoomNoOrderByStreamNoAndPraiseTime(Long roomNo);
}