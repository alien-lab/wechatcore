package com.alienlab.wechat.entity;

import com.alienlab.wechat.common.TypeUtils;
import com.alienlab.wechat.common.WeixinUtil;
import com.alienlab.wechat.utils.PropertyConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Wang on 2017/3/27.
 */
@ApiModel(value="直播间实体类")
@Entity
@Table(name = "wx_onlive_broadcasting")
public class OnliveRoom {
    PropertyConfig pc = new PropertyConfig("sysConfig.properties");
    @ApiModelProperty(value="直播间编号")
    private String roomNo;
    @ApiModelProperty(value="直播间名称")
    private String name;
    @ApiModelProperty(value="直播间描述")
    private String description;
    @ApiModelProperty(value="直播间主题")
    private String project;
    @ApiModelProperty(value="嘉宾介绍")
    private String guest;
    @ApiModelProperty(value="主讲人")
    private SortedMap<String, OnliveMember> speakers = new TreeMap<String ,OnliveMember>();
    @ApiModelProperty(value="直播间成员")
    private SortedMap<String, OnliveMember> members = new TreeMap<String ,OnliveMember>();
    @ApiModelProperty(value="直播间管理员手机号")
    private NamelistItem manager;
    @ApiModelProperty(value="直播间创建时间")
    private String creatTime;
    @ApiModelProperty(value="直播间状态")
    private String status;
    @ApiModelProperty(value="最近更新文字")
    private String latestText;
    @ApiModelProperty(value="最近更新图片")
    private String latestPic;
    @ApiModelProperty(value="直播间二维码")
    private String qrcode;
    @ApiModelProperty(value="直播间分享链接")
    private String shareLink;
    @ApiModelProperty(value="直播开始时间")
    private String startTime;
    @ApiModelProperty(value="直播结束时间")
    private String endTime;
    @ApiModelProperty(value="演讲模式")
    private String speakMode;
    @ApiModelProperty(value="直播间封面")
    private String cover;
    @ApiModelProperty(value="直播头像")
    private String brandCover;
    @ApiModelProperty(value="进入直播间信息")
    private String joinMsg = "1";
    @ApiModelProperty(value="评论信息")
    private String commentMsg = "1";

    @Id
    @Column(name = "bc_no")
    public String getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Basic
    @Column(name = "bc_name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "bc_abstract")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "bc_project")
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @Basic
    @Column(name = "bc_guest")
    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    @OneToMany
    @JoinColumn(name = "bc_manager", referencedColumnName = "member_openid")
    public SortedMap<String, OnliveMember> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(SortedMap<String, OnliveMember> speakers) {
        this.speakers = speakers;
    }

    @OneToMany
    @JoinColumn(name = "bc_manager_union", referencedColumnName = "member_openid")
//    public SortedMap<String, OnliveMember> getMembers() {
//        return members;
//    }
    public Map<String, OnliveMember> getMembers() {
        return members;
    }
    public void setMembers(SortedMap<String, OnliveMember> members) {
        this.members = members;
    }

    @OneToOne
    @JoinColumn(name = "bc_manager_phone", referencedColumnName = "namelist_phone")
    public NamelistItem getManager() {
        return manager;
    }

    public void setManager(NamelistItem name) {
        this.manager = name;
    }

    @Basic
    @Column(name = "bc_creattime")
    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    @Basic
    @Column(name = "bc_status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "bc_latest_text")
    public String getLatestText() {
        if(this.latestText == null||this.latestText.equals(""))return "";
        return latestText;
    }

    public void setLatestText(String latestText) {
        this.latestText = latestText;
    }

    @Basic
    @Column(name = "bc_latest_pic")
    public String getLatestPic() {
        if(this.latestPic==null||this.latestPic.equals(""))return "";
        String base_path = pc.getValue("base_path");
        String stream_path = pc.getValue("stream_path");
        if(this.latestPic.indexOf("http") >= 0){
            return latestPic;
        }else{
            return base_path + stream_path + latestPic;
        }
    }

    public void setLatestPic(String latestPic) {
        this.latestPic = latestPic;
    }

    @Basic
    @Column(name = "bc_qrcode")
    public String getQrcode() {
        String qrticket = this.qrcode;
        try {
            String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ URLEncoder.encode(qrticket,"UTF-8");
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    @Basic
    @Column(name = "bc_link")
    public String getShareLink() {
        String url = pc.getValue("base_path");
        url += "onlive/mobile/onliveroom.jsp";
        String link = WeixinUtil.getAuthUrl(url, this.getRoomNo(), "info");
        return link;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    @Basic
    @Column(name = "bc_starttime")
    public String getStartTime() {
        Date stime = null;
        if(this.startTime.length()>12){
            stime = TypeUtils.str2date(this.startTime, "yyyyMMddHHmmss");
        }else{
            stime = TypeUtils.str2date(this.startTime, "yyyyMMddHHmm");
        }
        return TypeUtils.getTime(stime, "yyyy-MM-dd HH:mm:ss");
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "bc_endtime")
    public String getEndTime() {
        Date stime = null;
        if(this.endTime.length()>12){
            stime = TypeUtils.str2date(this.endTime, "yyyyMMddHHmmss");
        }else{
            stime = TypeUtils.str2date(this.endTime, "yyyyMMddHHmm");
        }
        return TypeUtils.getTime(stime, "yyyy-MM-dd HH:mm:ss");
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "bc_speakmod")
    public String getSpeakMode() {
        return speakMode;
    }

    public void setSpeakMode(String speakMode) {
        this.speakMode = speakMode;
    }

    @Basic
    @Column(name = "bc_cover")
    public String getCover() {
        String base_path = pc.getValue("base_path");
        String cover_path = pc.getValue("cover_path");
        if(this.cover.indexOf("http") >= 0){
            return cover;
        }else{
            return base_path + cover_path + cover;
        }
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Basic
    @Column(name = "bc_vip_cover")
    public String getBrandCover() {
        String base_path = pc.getValue("base_path");
        String cover_path = pc.getValue("cover_path");

        if(this.brandCover.indexOf("http") >= 0){
            return this.brandCover;
        }else{
            return base_path + cover_path + brandCover;
        }
    }

    public void setBrandCover(String brandCover) {
        this.brandCover = brandCover;
    }

    @Basic
    @Column(name = "bc_join_message")
    public String getJoinMsg() {
        if(joinMsg == null)joinMsg="";
        return joinMsg;
    }

    public void setJoinMsg(String joinMsg) {
        this.joinMsg = joinMsg;
    }

    @Basic
    @Column(name = "bc_comment_message")
    public String getCommentMsg() {
        if(commentMsg == null)commentMsg="";
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }

    public OnliveRoom(){

    }

    public OnliveRoom(String roomNo, String name, String description, String project, String guest,
                      SortedMap<String, OnliveMember> speakers, SortedMap<String, OnliveMember> members, NamelistItem manager,
                      String creatTime, String status, String latestText, String latestPic, String qrcode, String startTime,
                      String endTime, String speakMode, String cover, String brandCover, String joinMsg, String commentMsg) {
        this.roomNo = roomNo;
        this.name = name;
        this.description = description;
        this.project = project;
        this.guest = guest;
        this.speakers = speakers;
        this.members = members;
        this.manager = manager;
        this.creatTime = creatTime;
        this.status = status;
        this.latestText = latestText;
        this.latestPic = latestPic;
        this.qrcode = qrcode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speakMode = speakMode;
        this.cover = cover;
        this.brandCover = brandCover;
        this.joinMsg = joinMsg;
        this.commentMsg = commentMsg;
    }

    public String getSpeakerOpenId(){
        List<OnliveMember> s = getSpeaker();
        String openids="";
        for(int i=0;i<s.size();i++){
            if(i==0){
                openids+=s.get(i).getOpenId();
            }else{
                openids+=","+s.get(i).getOpenId();
            }
        }
        return openids;
    }

    public OnliveMember getMember(String nickname){
        for(String openid : members.keySet()){
            OnliveMember member = members.get(openid);
            if(member.getNick().equals(nickname)){
                return member;
            }
        }
        return null;
    }

    public String getLayoutTime(){
        String s=this.getStartTime();
        String e=this.getEndTime();
        String date="";
        String time="";
        if(s.substring(0,8).equals(e.substring(0,8))){
            date=s.substring(0,8);
            date=date.substring(0,4)+"/"+date.substring(4,6)+"/"+date.substring(6,8);
            time=s.substring(8,12)+e.substring(8,12);
            time=time.substring(0,2)+":"+time.substring(2,4)+"~"+time.substring(4,6)+":"+time.substring(6,8);

            return date+" "+time;
        }else{
            return this.getStartTime()+"~"+this.getEndTime();
        }
    }

    public List<OnliveMember> getSpeaker() {
        List<OnliveMember> s=new ArrayList<OnliveMember>();
        for(Map.Entry<String,OnliveMember> e:speakers.entrySet()){
            s.add(e.getValue());
        }
        return s;
    }
}
