package com.alienlab.wechat.entity;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.service.OnliveStart;
import com.alienlab.wechat.utils.MediaDownloader;
import com.alienlab.wechat.utils.MediaUtil;
import com.alienlab.wechat.utils.StreamFileCallback;
import com.alienlab.wechat.utils.StreamThumbCallback;
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
    private Long contentNo;
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

    public OnliveStream(JSONObject s) {
    }

    @Id
    @Column(name = "content_no")
    public Long getContentNo() {
        return null;
    }
    public void setContentNo(Long contentNo) {
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

    public void downLoadMedia(){
        String path = OnliveStart.streamPath;
        //如果不存在媒体文件，直接返回
        if(this.getMedia()==null||this.getMedia().equals(""))return;
        String mediaurl = MediaUtil.getWechatLink(this.getMedia());
        String ext="";
        switch(this.getContentType()){
            case "image":{
                ext="jpg";
                break;
            }
            case "voice":{
                ext="amr";
                break;
            }
            case "video":
            case "shortvideo":{
                ext="mp4";
                break;
            }
        }
        //下载文件自动回调
        MediaDownloader.addDownload(mediaurl, path, ext, this.getRoomNo(),new StreamFileCallback());
        //如果缩略图不为空，下载缩略图
        if(this.getContentPic()!=null&&(!this.getContentPic().equals(""))){
            MediaDownloader.addDownload(MediaUtil.getWechatLink(this.getContentPic()), path, "jpg", this.getRoomNo(),new StreamThumbCallback());
        }
    }
}
