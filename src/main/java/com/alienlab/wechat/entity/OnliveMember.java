package com.alienlab.wechat.entity;

import com.alibaba.fastjson.JSONObject;
import com.alienlab.wechat.common.TypeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Wang on 2017/3/27.
 */
@ApiModel(value="直播间成员实体类")
@Entity
@Table(name = "wx_onlive_members")
@IdClass(OnliveMemberKey.class)
public class OnliveMember implements Serializable{
//    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="直播间编号")
    private String roomNo;
    @ApiModelProperty(value="成员微信")
    private String openId;
    @ApiModelProperty(value="管理员微信")
    private String unionId;
    @ApiModelProperty(value="成员昵称")
    private String nick;
    @ApiModelProperty(value="成员手机号码")
    private String phone;
//    @ApiModelProperty(value="成员远程头像地址")
//    private String pic;
    @ApiModelProperty(value="成员本地头像地址")
    private String localPic;
    @ApiModelProperty(value="成员加入方式")
    private String joinType;
    @ApiModelProperty(value="成员加入时间")
    private String joinTime;
    @ApiModelProperty(value="是否嘉宾")
    private boolean isVip;

    public OnliveMember(JSONObject member) {
    }

    @Id
    @Column(name = "bc_no")
    public String getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Id
    @Column(name = "member_openid")
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Basic
    @Column(name = "member_unionid")
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
    @Column(name = "member_phone")
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

//    @Basic
//    @Column(name = "member_pic")
//    public String getPic() {
//        return pic;
//    }
//    public void setPic(String pic) {
//        this.pic = pic;
//    }

    @Basic
    @Column(name = "member_pic")
    public String getLocalPic() {
        return localPic;
    }
    public void setLocalPic(String localPic) {
        this.localPic = localPic;
    }

    @Basic
    @Column(name = "member_join_type")
    public String getJoinType() {
        return joinType;
    }
    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    @Basic
    @Column(name = "member_join_time")
    public String getJoinTime() {
        return joinTime;
    }
    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    @Basic
    @Column(name = "member_vip")
    public boolean isVip() {
        return isVip;
    }
    public void setVip(boolean vip) {
        isVip = vip;
    }

    public OnliveMember(){
    }

    public OnliveMember(String roomNo, NamelistItem name){
        this.setJoinTime(TypeUtils.getTime());
        this.setJoinType("system");
        this.setLocalPic(name.getHeaderimg());
        this.setNick(name.getNickName());
        this.setOpenId(name.getOpenId());
        this.setPhone(name.getPhone());
//        this.setPic(name.getHeaderimg());
        this.setRoomNo(roomNo);
        this.setUnionId(name.getUnionId());
        this.setVip(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnliveMember that = (OnliveMember) o;

        if (isVip != that.isVip) return false;
        if (!roomNo.equals(that.roomNo)) return false;
        if (!openId.equals(that.openId)) return false;
        if (!unionId.equals(that.unionId)) return false;
        if (!nick.equals(that.nick)) return false;
        if (!phone.equals(that.phone)) return false;
        if (!localPic.equals(that.localPic)) return false;
        if (!joinType.equals(that.joinType)) return false;
        return joinTime.equals(that.joinTime);

    }

    @Override
    public int hashCode() {
        int result = roomNo.hashCode();
        result = 31 * result + openId.hashCode();
        result = 31 * result + unionId.hashCode();
        result = 31 * result + nick.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + localPic.hashCode();
        result = 31 * result + joinType.hashCode();
        result = 31 * result + joinTime.hashCode();
        result = 31 * result + (isVip ? 1 : 0);
        return result;
    }
}
