package com.mingi.springframe.learningtest.factorybean;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Program {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(Appconfig.class);
		
//		ConfigurableListableBeanFactory beanFactory = 
//				context.getBeanFactory();
//		beanFactory.destroyBean(context.getBean("myCustomObject"));
//		configAppContext.getBeanFactory().destroyBean("myCustomObject", MyCustomObject.class);
		
		// Bean Scope : 싱글톤, 프로토타임, 리퀘스트[HTTP 리쿼스트]
		ConfigurableApplicationContext configAppContext = 
				(ConfigurableApplicationContext) context;

		
		// Bean 으로 등록된 FactoryBean은 getBean으로 가져올때 '&'를 붙여야한다.
		// 그렇지 않으면, FactoryBean이 생성하는 Bean 객체를 리턴한다.
		MyCustomObjectFactoryBean factoryBean = 
				(MyCustomObjectFactoryBean) context.getBean("&myCustomObjectFactroy");
		
		configAppContext.getBeanFactory().registerSingleton("myCustomObject2", factoryBean.getObject());
		MyCustomObject myObject = (MyCustomObject) context.getBean("myCustomObject");
		
		System.out.println(myObject.getName());
	}

}
