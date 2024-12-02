package com.mingi.springframe.serviceTest;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mingi.springframe.dao.UserDaoJdbc;
import com.mingi.springframe.service.DummyMailSender;
import com.mingi.springframe.service.TxProxyFactoryBean;
import com.mingi.springframe.service.UserService;
import com.mingi.springframe.service.UserServiceImpl;

@Configuration
public class UserServiceFactory {
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:3306/sbdt_db?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");

		return dataSource;
	}

	@Bean
	public UserDaoJdbc userDao() {
		UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
		userDaoJdbc.setDataSource(dataSource());
		return userDaoJdbc;
	}
	
	@Bean("userServiceImplement")
	@Qualifier("userService1")
	public UserService userServiceImpl() {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		userServiceImpl.setUserDao(userDao());
		userServiceImpl.setMailSender(mailSender());
		return userServiceImpl;
	}
	
	@Bean("userServiceFactoryBean")
	@Qualifier("userService2")
	public TxProxyFactoryBean userService() {
		TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
		txProxyFactoryBean.setTarget(userServiceImpl());
		txProxyFactoryBean.setTransactionManager(transactionManager());
		txProxyFactoryBean.setPattern("upgradeLevels");
		txProxyFactoryBean.setServiceInterface(UserService.class);
		return txProxyFactoryBean;
	}	
	
	@Bean
	public DummyMailSender mailSender() {
		DummyMailSender dummyMailSender = new DummyMailSender();		
		return dummyMailSender;
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
		DataSourceTransactionManager dataSourceTransactionManager = 
				new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}	
}