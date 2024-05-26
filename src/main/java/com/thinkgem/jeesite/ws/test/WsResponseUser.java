package com.thinkgem.jeesite.ws.test;

public class WsResponseUser {
	private String name;

	private String username;

	private String workid;

	public WsResponseUser(String name, String workid, String username) {
		this.name = name;
		this.workid = workid;
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWorkid() {
		return workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}
}
