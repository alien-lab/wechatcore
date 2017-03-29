package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnlivePraise;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Wang on 2017/3/28.
 */
public interface OnlivePraiseRepository extends JpaRepository<OnlivePraise,Long>{
    OnlivePraise findOnlivePariseByStreamNoAndOpenId(String streamNo, String openId);
}
