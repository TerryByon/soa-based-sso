package com.apex.sso.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVER")
public class Server implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1959521986007132577L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	@Column(name = "IP")
	private String ip;

	@Column(name = "SECURITY_KEY")
	private byte[] securityKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public byte[] getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(byte[] securityKey) {
		this.securityKey = securityKey;
	}

}
