package com.mingi.springframe.serviceTest;

import javax.sql.DataSource;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.mingi.springframe.dao.UserDaoJdbc;
import com.mingi.springframe.service.DummyMailSender;
import com.mingi.springframe.service.UserServiceImpl;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan(basePackages = "com.mingi.springframe.service")
public class TestServiceFactory {
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
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = 
				new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}
	
	@Bean
    public TransactionInterceptor transactionAdvice() {
        NameMatchTransactionAttributeSource txAttributeSource = 
        		new NameMatchTransactionAttributeSource();
        
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setTimeout(30);
        
        RuleBasedTransactionAttribute readWriteTx = new RuleBasedTransactionAttribute();
        readWriteTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        readWriteTx.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        
        txAttributeSource.addTransactionalMethod("get*", readOnlyTx);
        txAttributeSource.addTransactionalMethod("*", readWriteTx);
        
        return new TransactionInterceptor(transactionManager(), txAttributeSource);
        
    }
	
	@Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*Service)");
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
    }

	// application components
	@Bean
	public UserDaoJdbc userDao() {
		UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
		userDaoJdbc.setDataSource(dataSource());
		return userDaoJdbc;
	}
	
	@Bean
	public UserServiceImpl userService() {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		userServiceImpl.setUserDao(userDao());
		userServiceImpl.setMailSender(mailSender());		
		return userServiceImpl;
	}	
	
	
	@Bean
	@Qualifier("testUserService")
	public UserServiceImpl testUserService() {
	    UserServiceImpl testUserServiceImpl = new UserServiceTest.TestUserServiceImpl();
	    testUserServiceImpl.setUserDao(userDao());
	    testUserServiceImpl.setMailSender(mailSender());
	    return testUserServiceImpl;
	}
	
	@Bean
	public DummyMailSender mailSender() {
		DummyMailSender dummyMailSender = new DummyMailSender();
		return dummyMailSender;
	}
	
	
}