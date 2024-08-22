package com.mingi.springframe.serviceTest;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.dao.UserDaoJdbc;
import com.mingi.springframe.service.UserService;

@Configuration
public class UserServiceFactory {
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();

		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");

		return dataSource;
	}

	@Bean
	public UserDao userDao() {
		UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
		userDaoJdbc.setDataSource(dataSource());
		
		return userDaoJdbc;
	}
	
	@Bean
	public UserService userService() {
		UserService userService = new UserService();
		userService.setUserDao(userDao()); // userDao를 의존성으로 주입합니다.
		userService.setTransactionManager(transactionManager());		
		userService.setMailSender(mailSenderImpl());
		
		return userService;
	}
	
/////////////////////////////////////////////////////////////////////////////
	@Bean
	public JavaMailSenderImpl mailSenderImpl() {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		
		mailSenderImpl.setJavaMailProperties(properites());
		
		mailSenderImpl.setHost("smtp.gmai.com");
		mailSenderImpl.setPort(587);
		
		mailSenderImpl.setUsername("kkang45597@gmail.com"); 
		mailSenderImpl.setPassword("iodf mybw qhjg mcux");
		
		return mailSenderImpl;
	}
	
	@Bean
	public Properties properites() {
		Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        return props;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}
}