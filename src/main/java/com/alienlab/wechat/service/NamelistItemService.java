package com.alienlab.wechat.service;

import com.alienlab.wechat.common.TypeUtils;
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

    //验证手机号是否可以创建直播间
    public boolean validatePhone(String phone){
        NamelistItem name = namelistItemRepository.findNamelistItemByPhone(phone);
        if(name != null){
            System.out.println("validatePhone get:" + phone);
            if(Integer.parseInt(name.getStatus()) > 0){
                String endtime = name.getEndTime();
                if(endtime.equals("")){  //如果无结束时间表示无限期
                    return true;
                }else{  //有结束期限，验证结束期限。
                    String nowtime = TypeUtils.getTime();
                    if(nowtime.compareTo(endtime) > 0){
                        return false;
                    }else{
                        return true;
                    }
                }
            }else{  //状态被置为0，直接返回不可用
                return false;
            }
        }else{
            System.out.println("validatePhone wrong:"+phone);
        }
        return false;
    }

    //新增白名单成员
    public NamelistItem addNamelistItem(NamelistItem namelistItem){
        if(namelistItem != null){
            return namelistItemRepository.save(namelistItem);
        }else{
            return null;
        }
    }

    //根据手机号查找成员
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
