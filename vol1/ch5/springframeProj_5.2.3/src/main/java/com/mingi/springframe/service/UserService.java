package com.mingi.springframe.service;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;

@Service // 추가
public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	private DataSource dataSource;  			

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void upgradeLevels() throws Exception {
		TransactionSynchronizationManager.initSynchronization();  
		Connection c = DataSourceUtils.getConnection(dataSource); 
		c.setAutoCommit(false); // 자동 커밋
		
		try {									   
			List<User> users = userDao.getAll();
			for (User user : users) {
				if (canUpgradeLevel(user)) {
					upgradeLevel(user); // UserServiceTets의 upgradeLevel을 호출한다.
				}
			}
			c.commit();  
		} catch (Exception e) {    
			c.rollback();
			throw e;
		} finally {
			DataSourceUtils.releaseConnection(c, dataSource);	
			TransactionSynchronizationManager.unbindResource(this.dataSource);  
			TransactionSynchronizationManager.clearSynchronization(); // 동기화
		}
	}
	
	private boolean canUpgradeLevel(User user) { // 테스트를 위해 변경
//	protected boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel(); 
		switch(currentLevel) {                                   
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER); 
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel); 
		}
	}

	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}
	
	public void add(User user) {
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}

}