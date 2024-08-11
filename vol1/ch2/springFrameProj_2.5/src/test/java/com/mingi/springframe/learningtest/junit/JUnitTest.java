package com.mingi.springframe.learningtest.junit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import com.mingi.springframe.learningtest.factorybean.MyCustomObject;
import com.mingi.springframe.learningtest.factorybean.MyCustomObjectFactoryBean;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJunit.class})
public class JUnitTest {
	
	@Autowired 
	ApplicationContext context; // Spring IoC 컨테이너가 자동 주입됨
	
	// Set 은 동일한 엘리먼트 중복을 허용하지 않느다.
	// 엘리먼트 : 특정 JUnitTest 클래스 객체(오브젝트/인스턴스) 참조 값
	// 참조는 C언에서 포인터로 값을 메모리 주소로 가진다.
	static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
	static ApplicationContext contextObject = null;
	
	@Test 
	public void test1() {
		assertFalse(testObjects.contains(this));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Test 
	public void test2() {
		assertFalse(testObjects.contains(this));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Test 
	public void test3() {
		assertFalse(testObjects.contains(this));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
///////////////////////////////////////////
	@DirtiesContext
	@Test 
	@Order(1)
	public void test4() throws IllegalStateException, Exception {
		
		MyCustomObjectFactoryBean factoryBean = 
				(MyCustomObjectFactoryBean) context.getBean("&myCustomObjectFactroy");
		
		ConfigurableApplicationContext configAppContext = 
				(ConfigurableApplicationContext) context;
		
		configAppContext.getBeanFactory().registerSingleton("myCustomObjectReal", factoryBean.getObject());
		MyCustomObject myObject = (MyCustomObject) context.getBean("myCustomObject");
		
		System.out.println(myObject.getName());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}