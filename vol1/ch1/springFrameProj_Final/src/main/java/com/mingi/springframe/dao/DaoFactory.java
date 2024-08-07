package com.mingi.springframe.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource; // JDBC가 구현했다.

@Configuration
public class DaoFactory {
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource (); // 심플해서 사용한다.

		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");

		return dataSource;
	}

	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());
		return userDao;
	}
}