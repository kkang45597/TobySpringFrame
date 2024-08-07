package com.mingi.springframe.domain;

public class User { // 엔티티 클래스 = 테이블
	
	String id;
	String name;
	String password;
	// 필드는 로우와 매핑된다.
	
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