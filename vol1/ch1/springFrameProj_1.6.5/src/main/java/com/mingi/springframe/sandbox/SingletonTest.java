package com.mingi.springframe.sandbox;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mingi.springframe.dao.DaoFactory;
import com.mingi.springframe.dao.UserDao;
 
public class SingletonTest {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		System.out.println(context.getBean(UserDao.class));
		System.out.println(context.getBean(UserDao.class));
	}
} 