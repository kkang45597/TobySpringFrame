package com.mingi.springframe.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {
	
	@Bean
	public UserDao userDao() {
		return new UserDao(connectionMaker()); // 의존성 주입 : 생성자 아규먼트 방식
	}

	@Bean
	public ConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker()); // 의존성 주입 : 생성자 아규먼트 방식
	}

	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new MConnectionMaker();
	}
}