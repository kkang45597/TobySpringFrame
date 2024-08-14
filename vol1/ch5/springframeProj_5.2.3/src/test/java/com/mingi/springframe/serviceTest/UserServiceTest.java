package com.mingi.springframe.serviceTest;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;
import com.mingi.springframe.service.UserService;
import static com.mingi.springframe.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.mingi.springframe.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DaoFactoryTest.class })
public class UserServiceTest {
	
	@Autowired
	UserService userService;	
	
	@Autowired 
	UserDao userDao;
	
	@Autowired 
	DataSource dataSource;
	
	List<User> users; // 테스트를 위해 변경
//	static List<User> users;
	
	@BeforeEach
	public void setUp() {
		
		
		users = Arrays.asList(
//				new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD), // 테스트를 의해 위치 변경
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
				new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
				);
	}

	@Test
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
//		checkLevelUpgraded(users.get(0), true); // 테스트를 위해 변경
//		checkLevelUpgraded(users.get(1), false);
//		checkLevelUpgraded(users.get(2), true);
//		checkLevelUpgraded(users.get(3), false);
//		checkLevelUpgraded(users.get(4), false);
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		
		Optional<User> userUpdateOpt = userDao.get(user.getId()); // Optional<User>에 맞춰 변경
		User userUpdate = userUpdateOpt.get();
		
		if (upgraded) {
			assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
		}
		else {
			assertEquals(userUpdate.getLevel(), user.getLevel());
		}
	}

	@Test 
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);	  // GOLD 레벨  
		User userWithoutLevel = users.get(0);  
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);	  
		userService.add(userWithoutLevel);
		
		Optional<User> userWithLevelRead = userDao.get(userWithLevel.getId());
		Optional<User> userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		User userWithLevelReadOpt = userWithLevelRead.get();
		User userWithoutLevelReadOpt = userWithoutLevelRead.get();
		
		assertEquals(userWithLevelReadOpt.getLevel(),  userWithLevel.getLevel());
		assertEquals(userWithoutLevelReadOpt.getLevel(),  Level.BASIC);
		
//		assertEquals(userWithLevelRead.getLevel(),  userWithLevel.getLevel()); 
//		assertEquals(userWithoutLevelRead.getLevel(),  Level.BASIC);
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId());  
//		UserService testUserService = new TestUserService(users.get(3).getId()); // 테스트를 위해 변경
		testUserService.setUserDao(this.userDao); 
		testUserService.setDataSource(this.dataSource);
		 
		userDao.deleteAll();			  
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();   
			fail("TestUserServiceException expected"); // 테스트를 위해 변경
		}
		catch(TestUserServiceException e) { 
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	static class TestUserService extends UserService { // UserService를 상속해서  오버라이딩 된다
		private String id;
		
		private TestUserService(String id) {  
			this.id = id;
		}

		protected void upgradeLevel(User user) { // 테스트를 위해 변경
			if (user.getId().equals(this.id)) throw new TestUserServiceException();  
			super.upgradeLevel(user); // UserService의 upgradeLevel를 호출한다.
			
//		public void upgradeLevels() throws Exception { // UserService에 있었음, 테스트를 위해 추가함
//			TransactionSynchronizationManager.initSynchronization();  
//			Connection c = DataSourceUtils.getConnection(this.getdataSource()); 
//			c.setAutoCommit(false); // 자동 커밋
//			
//			try {									   
//				for (User user : users) {
//					if (canUpgradeLevel(user)) {
//						upgradeLevel(user); // UserServiceTets의 upgradeLevel을 호출한다.
//					}
//				}
//				c.commit();  
//			} catch (Exception e) {    
//				c.rollback();
//				throw e;
//			} finally {
//				DataSourceUtils.releaseConnection(c, this.getdataSource());	
//				TransactionSynchronizationManager.unbindResource(this.getdataSource());  
//				TransactionSynchronizationManager.clearSynchronization();  
//			}
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
	}
}