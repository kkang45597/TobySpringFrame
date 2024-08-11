package com.mingi.springframe.domain;

public class User {
	String id;
	String name;
	String password;
	
	public User() {
	}
	
	public User(String id, String name, String password) { // springFrameProj_2.3.3Set에 추가되었습니다.
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}