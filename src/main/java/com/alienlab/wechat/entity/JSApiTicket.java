package com.alienlab.wechat.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "", schema = "", catalog = "")
public class JSApiTicket {
	private String errcode;
	private String errmsg;
	private String ticket;
	private String expires_in;
	private long ticketTime;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	@Basic
	@Column(name = "")
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Basic
	@Column(name = "")
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	@Basic
	@Column(name = "")
	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	@Basic
	@Column(name = "")
	public long getTicketTime() {
		return ticketTime;
	}

	public void setTicketTime(long ticketTime) {
		this.ticketTime = ticketTime;
	}
}
