package com.mingi.sprinframe.daoTest;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource; // JDBC가 구현했다.

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.dao.UserDaoJdbc;
import com.mingi.springframe.service.UserService;

@Configuration
public class DaoFactoryTest {
	
	@Bean
	public DataSource dataSource() { // Bean 팩토리 메서드
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();

		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");

		return dataSource;
	}

	@Bean
	public UserDao userDao() {
//		UserDao userDao = new UserDao();
//		userDao.setDataSource(dataSource());
		UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
		userDaoJdbc.setDataSource(dataSource());
		return userDaoJdbc;
	}
	
//	@Bean
//	public UserDaoSql userDaoSql() {
//		UserDaoSql userDaoSql = new UserDaoSql();
//		userDaoSql.setDataSource(dataSource());
//		return userDaoSql;	
//	}
	
	@Bean
	public UserService userService() {
		UserService userService = new UserService();
		userService.setUserDao(userDao()); // userDao를 주입합니다.
		return userService;
	}
}