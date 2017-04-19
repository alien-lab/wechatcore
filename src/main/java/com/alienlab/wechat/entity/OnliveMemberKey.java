package com.alienlab.wechat.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by Wang on 2017/4/14.
 */
public class OnliveMemberKey implements Serializable {
    private static final long serialVersionUID = 1717638966560959020L;

    private String roomNo;
    private String openId;

    public OnliveMemberKey() {
    }

    public OnliveMemberKey(String roomNo, String openId) {
        this.roomNo = roomNo;
        this.openId = openId;
    }


    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnliveMemberKey that = (OnliveMemberKey) o;

        if (!roomNo.equals(that.roomNo)) return false;
        return openId.equals(that.openId);

    }

    @Override
    public int hashCode() {
        int result = roomNo.hashCode();
        result = 31 * result + openId.hashCode();
        return result;
    }
}
