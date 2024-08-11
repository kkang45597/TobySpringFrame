package com.mingi.springframe.learningtest.junit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mingi.springframe.learningtest.factorybean.MyCustomObject;
import com.mingi.springframe.learningtest.factorybean.MyCustomObjectFactoryBean;

@Configuration
public class TestJunit {

	@Bean
	public MyCustomObjectFactoryBean myCustomObjectFactroy() {
		MyCustomObjectFactoryBean factoryBean = new MyCustomObjectFactoryBean();
		return factoryBean;
	}
	
	@Bean
	public MyCustomObject myCustomObject() throws Exception {
		return myCustomObjectFactroy().getObject();
	}
}
