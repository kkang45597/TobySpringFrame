package com.mingi.springframe.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
	
	@Bean
	public UserDao userDaoM() {
		UserDao dao = new UserDao(connectionMMaker()); // 의존성 주입, 생성자 아규먼트
		return dao;
	}
	@Bean
	public UserDao userDaoH() {
		UserDao dao = new UserDao(connectionMMaker()); // 의존성 주입, 생성자 아규먼트
		return dao;
	}

	@Bean
	public ConnectionMaker connectionMMaker() {
		ConnectionMaker connectionMaker = new MConnectionMaker();
		return connectionMaker;
	}
	@Bean
	public ConnectionMaker connectionHMaker() {
		ConnectionMaker connectionMaker = new HConnectionMaker();
		return connectionMaker;
	}
}
