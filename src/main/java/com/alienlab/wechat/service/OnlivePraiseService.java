package com.alienlab.wechat.service;

import com.alienlab.wechat.entity.OnlivePraise;
import com.alienlab.wechat.repository.OnlivePraiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Wang on 2017/3/29.
 */
@Service
public class OnlivePraiseService {
    @Autowired
    private OnlivePraiseRepository onlivePraiseRepository;

    //新增点赞
    public OnlivePraise addOnlivePraise(OnlivePraise onlivePraise){
        if(onlivePraise != null){
            return  onlivePraiseRepository.save(onlivePraise);
        }else{
            return null;
        }
    }

    //根据内容流序号和成员微信号查找点赞
    public OnlivePraise findOnlivePraiseByStreamNoAndOpenId(Long streamNo, String openId){
        return onlivePraiseRepository.findOnlivePariseByStreamNoAndOpenId(streamNo, openId);
    }

    //根据点赞时间顺序获得当前内容的所有赞
    public List<OnlivePraise> loadPraises(Long streamNo){
        return onlivePraiseRepository.findOnlivePraiseByStreamNoOrderByPraiseTime(streamNo);
    }

    //根据内容流序号和时间顺序获得当前直播间的所有赞
    public List<OnlivePraise> getPraises(Long roomNo){
        return onlivePraiseRepository.findOnlivePariseByRoomNoOrderByStreamNoAndPraiseTime(roomNo);
    }

    //更新点赞
    public OnlivePraise updateOnlivePraise(OnlivePraise onlivePraise){
        try{
            return onlivePraiseRepository.save(onlivePraise);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //删除点赞
    public boolean deleteOnlivePraise(Long streamNo){
        try{
            onlivePraiseRepository.delete(streamNo);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
