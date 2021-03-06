package com.alienlab.wechat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Wang on 2017/3/27.
 */
@ApiModel(value="直播间点赞实体类")
@Entity
@Table(name = "wx_onlive_stream_praise")
@IdClass(OnlivePraiseKey.class)
public class OnlivePraise implements Serializable{
    @ApiModelProperty(value="内容流序号")
    private Long streamNo;
    @ApiModelProperty(value="直播间编号")
    private String roomNo;
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
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
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

    public OnlivePraise() {
    }

    public OnlivePraise(Long streamNo, String roomNo, String openId) {
        this.streamNo = streamNo;
        this.roomNo = roomNo;
        this.openId = openId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnlivePraise praise = (OnlivePraise) o;

        if (!streamNo.equals(praise.streamNo)) return false;
        if (!roomNo.equals(praise.roomNo)) return false;
        if (!openId.equals(praise.openId)) return false;
        return praiseTime.equals(praise.praiseTime);

    }

    @Override
    public int hashCode() {
        int result = streamNo.hashCode();
        result = 31 * result + roomNo.hashCode();
        result = 31 * result + openId.hashCode();
        result = 31 * result + praiseTime.hashCode();
        return result;
    }
}
