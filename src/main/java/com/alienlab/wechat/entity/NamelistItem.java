package com.alienlab.wechat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Created by Wang on 2017/3/25.
 */
@ApiModel(value="白名单成员实体类")
@Entity
@Table(name = "wx_olive_namelist")
public class NamelistItem {
    @ApiModelProperty(value="白名单序号")
    private Long no;
    @ApiModelProperty(value="成员手机号")
    private String phone="";
    @ApiModelProperty(value="成员直播间数量上限")
    private int count=5;
    @ApiModelProperty(value="成员有效期")
    private String endTime="";
    @ApiModelProperty(value="成员状态")
    private String status="1";
    @ApiModelProperty(value="开放平台身份编号")
    private String openId="";
    @ApiModelProperty(value="微信昵称")
    private String nickName="";
    @ApiModelProperty(value="微信头像")
    private String headerimg="";
    @ApiModelProperty(value="性别")
    private String sex="";
    @ApiModelProperty(value="省份")
    private String province="";
    @ApiModelProperty(value="城市")
    private String city="";
    @ApiModelProperty(value="国家")
    private String country="";
    @ApiModelProperty(value="联合编号")
    private String unionId="";
    @ApiModelProperty(value="特权")
    private String privilege="";

    @Id
    @Column(name = "namelist_no")
    public Long getNo() {
        return no;
    }
    public void setNo(Long no) {
        this.no = no;
    }

    @Basic
    @Column(name = "namelist_phone")
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "namelist_count")
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @Basic
    @Column(name = "namelist_endtime")
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "namelist_status")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    @Column(name = "nickname")
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Basic
    @Column(name = "headerimg")
    public String getHeaderimg() {
        return headerimg;
    }
    public void setHeaderimg(String headerimg) {
        this.headerimg = headerimg;
    }

    @Basic
    @Column(name = "sex")
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "province")
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
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
    @Column(name = "privilege")
    public String getPrivilege() {
        return privilege;
    }
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public NamelistItem(){

    }

    public NamelistItem(Long no, String phone, int count, String endTime, String status, String openId, String nickName,
                        String headerimg, String sex, String province, String city, String country, String unionId, String privilege) {
        this.no = no;
        this.phone = phone;
        this.count = count;
        this.endTime = endTime;
        this.status = status;
        this.openId = openId;
        this.nickName = nickName;
        this.headerimg = headerimg;
        this.sex = sex;
        this.province = province;
        this.city = city;
        this.country = country;
        this.unionId = unionId;
        this.privilege = privilege;
    }
}
