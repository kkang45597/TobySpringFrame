package com.mingi.springframe.serviceTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;
import com.mingi.springframe.service.UserService;
import static com.mingi.springframe.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.mingi.springframe.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserServiceFactory.class })
public class UserServiceTest {
	
	@Autowired UserService userService;	
	@Autowired UserDao userDao;
	@Autowired MailSender mailSender; 
	@Autowired PlatformTransactionManager transactionManager;
	
	static List<User> users;
	
	@BeforeEach
	public void setUp() {
		users = Arrays.asList(
			new User("madnite1", "이상호", "p4", "kkang45597@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
			new User("bumjin", "박범진", "p1", "kkang45597@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("joytouch", "강명성", "p2", "kkang45597@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("erwins", "신승한", "p3", "kkang45597@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
			//new User("madnite1", "이상호", "p4", "kkang45597@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
			new User("green", "오민규", "p5", "kkang45597@gmail.com", Level.GOLD, 100, Integer.MAX_VALUE)
			);
	}

	@Test
	public void upgradeLevels() throws Exception {
		
		MockMailSender mockMailSender = new MockMailSender();  
		userService.setMailSender(mockMailSender);  
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), true);
		checkLevelUpgraded(users.get(1), false);
		checkLevelUpgraded(users.get(2), true);
		checkLevelUpgraded(users.get(3), false);
		checkLevelUpgraded(users.get(4), false);
		
		List<String> request = mockMailSender.getRequests();
		assertEquals(request.size(), 2); 
		assertEquals(request.get(0),users.get(1).getEmail());  
		assertEquals(request.get(1), users.get(3).getEmail());  
	}
	
	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<String>();	
		
		public List<String> getRequests() {
			return requests;
		}

		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);  
		}

		public void send(SimpleMailMessage[] mailMessage) throws MailException {
		}
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
		
		User userWithLevel = users.get(4);
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
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
//		UserService testUserService = new TestUserService(users.get(3).getId()); 
		UserService testUserService = new TestUserService(users.get(2).getId()); 
		testUserService.setUserDao(this.userDao); 
		testUserService.setTransactionManager(this.transactionManager);
		testUserService.setMailSender(this.mailSender);
		 
		userDao.deleteAll();			  
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();   
			fail("TestUserServiceException expected");
		}
		catch(TestUserServiceException e) { 
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	static class TestUserService extends UserService {
		private String id;
		
		private TestUserService(String id) {  
			this.id = id;
		}

		@Override
		public void upgradeLevels() throws Exception {
			TransactionStatus status = this.getTransactionManager().
					getTransaction(new DefaultTransactionDefinition());
			
			try {									   
				for (User user : users) {
					if (canUpgradeLevel(user)) {
						upgradeLevel(user);
					}
				}
				this.getTransactionManager().commit(status);
				
			} catch (Exception e) {  
				this.getTransactionManager().rollback(status);
	            throw e;
			}
		}
		
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) 
				throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
	}
}