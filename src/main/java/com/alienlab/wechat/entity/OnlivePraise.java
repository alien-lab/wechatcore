package com.alienlab.wechat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by Wang on 2017/3/27.
 */
@ApiModel(value="直播间点赞实体类")
@Entity
@Table(name = "wx_onlive_stream_praise")
public class OnlivePraise {
    @ApiModelProperty(value="内容流序号")
    private Long streamNo;
    @ApiModelProperty(value="直播间编号")
    private Long roomNo;
    @ApiModelProperty(value="成员微信")
    private String openId;
    @ApiModelProperty(value="点赞时间")
    private String praiseTime;

    @Id
    @Column(name = "stream_no")
    public Long getStreamNo() {
        return streamNo;
    }

    public void setStreamNo(Long streamNo) {
        this.streamNo = streamNo;
    }

    @Basic
    @Column(name = "room_no")
    public Long getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Long roomNo) {
        this.roomNo = roomNo;
    }

    @Id
    @Column(name = "openid")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Basic
    @Column(name = "praise_time")
    public String getPraiseTime() {
        return praiseTime;
    }

    public void setPraiseTime(String praiseTime) {
        this.praiseTime = praiseTime;
    }
}
