package com.atlas.fun.sso.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserPk implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1688293164277075606L;

	@Column(name = "SERVER_ID")
	private int serverid;


	@Column(name = "USER_ID")
	private String userid;



	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}



	public int getServerid() {
		return serverid;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
	}
}
