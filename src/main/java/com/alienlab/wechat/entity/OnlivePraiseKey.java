package com.alienlab.wechat.entity;

import java.io.Serializable;

/**
 * Created by Wang on 2017/4/14.
 */
public class OnlivePraiseKey implements Serializable {
    private static final long serialVersionUID = 1717638966560959020L;

    private Long streamNo;
    private String openId;

    public OnlivePraiseKey() {
    }

    public OnlivePraiseKey(Long streamNo, String openId) {
        this.streamNo = streamNo;
        this.openId = openId;
    }

    public Long getStreamNo() {
        return streamNo;
    }

    public void setStreamNo(Long streamNo) {
        this.streamNo = streamNo;
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

        OnlivePraiseKey that = (OnlivePraiseKey) o;

        if (!streamNo.equals(that.streamNo)) return false;
        return openId.equals(that.openId);

    }

    @Override
    public int hashCode() {
        int result = streamNo.hashCode();
        result = 31 * result + openId.hashCode();
        return result;
    }
}
