package com.mingi.springframe.learningtest.factorybean;

public class MyCustomObject {
	
	private String name = "hello";
	
	public MyCustomObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
