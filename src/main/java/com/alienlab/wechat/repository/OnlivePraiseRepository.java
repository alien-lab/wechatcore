package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.OnlivePraise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wang on 2017/3/28.
 */
@Repository
public interface OnlivePraiseRepository extends JpaRepository<OnlivePraise,Long>{
    List<OnlivePraise> findOnlivePraiseByStreamNoOrderByPraiseTime(Long streamNo);

    @Query("from OnlivePraise a where a.roomNo=?1 order by a.streamNo,a.praiseTime")
    List<OnlivePraise> findOnlivePariseByRoomNoOrderByStreamNoAndPraiseTime(String roomNo);
}
