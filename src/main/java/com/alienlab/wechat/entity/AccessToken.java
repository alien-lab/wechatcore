package com.alienlab.wechat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "", schema = "", catalog = "")
public class AccessToken {
	private String token;  // 获取到的凭证
	private int expiresIn;  // 凭证有效时间，单位：秒
	private long tokenTime;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Basic
	@Column(name = "")
	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Basic
	@Column(name = "")
	public long getTokenTime() {
		return tokenTime;
	}

	public void setTokenTime(long tokenTime) {
		this.tokenTime = tokenTime;
	}
}
