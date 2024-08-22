package com.mingi.springframe.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mingi.springframe.domain.User;

//트랜잭션 경계설정을 위한 UserServiceTx 도입 : 비즈니스 트랜잭션 처리를 담당
//그림 6-3 참조
public class UserServiceTx implements UserService{
	
	UserService userService;
	private PlatformTransactionManager transactionManager;

	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public UserService getUserSerive() {
		return this.userService;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public PlatformTransactionManager getTransactionManager() {
		return this.transactionManager;
	}
	
	public void upgradeLevels() {
		TransactionStatus status = 
				this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			
			this.userService.upgradeLevels();
			
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}	
	
	public void add(User user) {
		this.userService.add(user);
	}
	
}