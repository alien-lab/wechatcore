package com.alienlab.wechat.service;

import com.alienlab.wechat.entity.NamelistItem;
import com.alienlab.wechat.repository.NamelistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Wang on 2017/3/28.
 */
@Service
public class NamelistItemService {
    @Autowired
    private NamelistItemRepository namelistItemRepository;

    //新增白名单成员
    public NamelistItem addNamelistItem(NamelistItem namelistItem){
        if(namelistItem != null){
            return namelistItemRepository.save(namelistItem);
        }else{
            return null;
        }
    }

    //通过手机号查找成员
    public NamelistItem findNamelistItemByPhone(String phone){
        return namelistItemRepository.findNamelistItemByPhone(phone);
    }

    //更新成员
    public NamelistItem updateNamelistItem(NamelistItem namelistItem){
        try{
            return namelistItemRepository.save(namelistItem);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //删除成员
    public boolean deleteNamelistItem(Long no){
        try{
            namelistItemRepository.delete(no);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
