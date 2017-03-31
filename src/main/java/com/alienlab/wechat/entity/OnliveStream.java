package com.alienlab.wechat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by Wang on 2017/3/27.
 */
@ApiModel(value="直播间内容流实体类")
@Entity
@Table(name = "wx_onlive_content")
public class OnliveStream {
    @ApiModelProperty(value="内容流序号")
    private String contentNo;
    @ApiModelProperty(value="直播间编号")
    private String roomNo;
    @ApiModelProperty(value="内容流类型")
    private String contentType;
    @ApiModelProperty(value="内容流时间")
    private String time;
    @ApiModelProperty(value="内容流发送者身份码")
    private String openId;
    @ApiModelProperty(value="内容流发送者统一码")
    private String unionId;
    @ApiModelProperty(value="内容流发送者昵称")
    private String nick;
    @ApiModelProperty(value="内容流内容")
    private String content;
    @ApiModelProperty(value="媒体内容本地链接")
    private String link;
    @ApiModelProperty(value="视频小视频缩略图mediaid")
    private String contentPic;
    @ApiModelProperty(value="视频小视频缩略图地址")
    private String contentPicLink;
    @ApiModelProperty(value="多媒体文件")
    private String media;

    @Id
    @Column(name = "content_no")
    public String getContentNo() {
        return contentNo;
    }
    public void setContentNo(String contentNo) {
        this.contentNo = contentNo;
    }

    @Basic
    @Column(name = "bc_no")
    public String getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Basic
    @Column(name = "content_type")
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Basic
    @Column(name = "content_time")
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
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
    @Column(name = "unionid")
    public String getUnionId() {
        return unionId;
    }
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Basic
    @Column(name = "member_nick")
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }

    @Basic
    @Column(name = "content_body")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "content_link")
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    @Basic
    @Column(name = "content_pic")
    public String getContentPic() {
        return contentPic;
    }
    public void setContentPic(String contentPic) {
        this.contentPic = contentPic;
    }

    @Basic
    @Column(name = "content_piclink")
    public String getContentPicLink() {
        return contentPicLink;
    }
    public void setContentPicLink(String contentPicLink) {
        this.contentPicLink = contentPicLink;
    }

    @Basic
    @Column(name = "content_welink")
    public String getMedia() {
        return media;
    }
    public void setMedia(String media) {
        this.media = media;
    }

    public OnliveStream() {
    }

    public OnliveStream(String roomNo, String contentType, String time, String openId, String unionId, String nick,
                        String content, String link, String contentPic, String contentPicLink, String media) {
        this.roomNo = roomNo;
        this.contentType = contentType;
        this.time = time;
        this.openId = openId;
        this.unionId = unionId;
        this.nick = nick;
        this.content = content;
        this.link = link;
        this.contentPic = contentPic;
        this.contentPicLink = contentPicLink;
        this.media = media;
        //本地链接未生成
        if((!this.getContentType().equals("text"))&&(this.getLink()==null||this.getLink().equals(""))){
            //调用多媒体接口进行下载后，更新本地连接字段
            switch(this.getContentType()){
                case "image":
                case "voice":{
                    this.setMedia(media);
                    break;
                }
                case "video":
                case "shortvideo":{
                    this.setMedia(media);
                    this.setContentPic(contentPic);
                    break;
                }
            }
        }
    }
}
