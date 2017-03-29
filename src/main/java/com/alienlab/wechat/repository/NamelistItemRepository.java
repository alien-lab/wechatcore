package com.alienlab.wechat.repository;

import com.alienlab.wechat.entity.NamelistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Wang on 2017/3/27.
 */
@Repository
public interface NamelistItemRepository extends JpaRepository<NamelistItem,Long> {
    NamelistItem findNamelistItemByPhone(String phone);
}
