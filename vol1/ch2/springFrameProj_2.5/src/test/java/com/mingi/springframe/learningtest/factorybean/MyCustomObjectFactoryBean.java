package com.mingi.springframe.learningtest.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class MyCustomObjectFactoryBean implements FactoryBean<MyCustomObject> {

	private String objectName = "hello factorybean";
	
	@Override
	public MyCustomObject getObject() throws Exception {
		return new MyCustomObject(objectName);
	}

	@Override
	public Class<?> getObjectType() {
		return MyCustomObject.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
//		return false;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	
}
