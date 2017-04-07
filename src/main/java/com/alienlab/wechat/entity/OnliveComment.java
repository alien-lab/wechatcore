package com.alienlab.wechat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by Wang on 2017/3/27.
 */
@ApiModel(value="直播间评论实体类")
@Entity
@Table(name = "wx_onlive_stream_comment")
public class OnliveComment {
    @ApiModelProperty(value="评论序号")
    private Long commentNo;
    @ApiModelProperty(value="内容流序号")
    private Long streamNo;
    @ApiModelProperty(value="直播间编号")
    private String roomNo;
    @ApiModelProperty(value="成员微信")
    private String openId;
    @ApiModelProperty(value="评论内容")
    private String content;
    @ApiModelProperty(value="评论时间")
    private String commentTime;
    @ApiModelProperty(value="昵称")
    private String toNick;

    @Id
    @Column(name = "comment_no")
    public Long getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(Long commentNo) {
        this.commentNo = commentNo;
    }

    @Basic
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

    @Basic
    @Column(name = "openid")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "comment_time")
    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @Basic
    @Column(name = "to_nick")
    public String getToNick() {
        return toNick;
    }

    public void setToNick(String toNick) {
        this.toNick = toNick;
    }

    public OnliveComment() {
    }

    public OnliveComment(Long streamNo, String roomNo, String openId, String content) {
        this.streamNo = streamNo;
        this.roomNo = roomNo;
        this.openId = openId;
        this.content = content;
    }
}
