/**
 * @author  Eric
 * @Date:2016年4月26日上午9:36:11
 * @version 1.0
 */
package com.alienlab.wechat.entity;

import com.alienlab.wechat.db.ExecResult;
import com.alienlab.wechat.db.ISyncDb;
import com.alienlab.wechat.response.JSONResponse;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "", schema = "", catalog = "")
public class WechatUserInfo implements ISyncDb{
	String userinfo_id;  				//信息编号
	private String openId;  			//openid
	private int subscribe;  			//是否关注标识
	private String subscribe_time;	//关注事件
	private String nickname;			//昵称
	private int sex;  					//年龄
	private String country;  			//国家
	private String province;  		//省份
	private String city;  				//城市
	private String language;  		//语言
	private String headImgUrl;		//头像地址
	private String lastactive;		//最后活跃时间

	public String getUserinfo_id() {
		return userinfo_id;
	}
	public void setUserinfo_id(String userinfo_id) {
		this.userinfo_id = userinfo_id;
	}

	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public String getSubscribe_time() {
		return subscribe_time;
	}
	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	public String getLastactive() {
		return lastactive;
	}
	public void setLastactive(String lastactive) {
		this.lastactive = lastactive;
	}
	/* (non-Javadoc)
	 * @see com.alienlab.db.ISyncDb#initfdb()
	 */
	@Override
	public void initfdb() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.alienlab.db.ISyncDb#insert2db()
	 */
	@Override
	public boolean insert2db() {
		// TODO Auto-generated method stub
		JSONResponse jr=new JSONResponse();
		String sql = "INSERT INTO `wx_user_info`( `openId`, `subscribe`, `subscribe_time`, `nickname`, `sex`, `country`, `province`, `city`, `language`,`lastactive`,`headImgUrl`) "
				+ "VALUES ('" + this.getOpenId() + "','" + this.getSubscribe() + "','" + this.getSubscribe_time()
				+ "','" + this.getNickname() + "','" + this.getSex() + "','" + this.getCountry() + "','"
				+ this.getProvince() + "','" + this.getCity() + "','" + this.getLanguage()
				+ "',DATE_ADD(subscribe_time,INTERVAL 72 HOUR),'" + this.getHeadImgUrl() + "')";

		//写入数据库获得数据编号
		ExecResult er=jr.getExecInsertId(sql, null, null, null);
		if(er.getResult()>0){
			this.setUserinfo_id(er.getMessage());
			return true;
			
		}else{
			return false;
		}
		
		
	}
	/* (non-Javadoc)
	 * @see com.alienlab.db.ISyncDb#update2db()
	 */
	@Override
	public boolean update2db() {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see com.alienlab.db.ISyncDb#deletefdb()
	 */
	@Override
	public boolean deletefdb() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	

}
