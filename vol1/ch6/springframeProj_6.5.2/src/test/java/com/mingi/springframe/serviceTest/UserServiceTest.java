package com.mingi.springframe.serviceTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static com.mingi.springframe.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.mingi.springframe.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;
import com.mingi.springframe.service.Hello;
import com.mingi.springframe.service.HelloTarget;
import com.mingi.springframe.service.TxProxyFactoryBean;
import com.mingi.springframe.service.UpperCaseAdvice;
import com.mingi.springframe.service.UserService;
import com.mingi.springframe.service.UserServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserServiceFactory.class })
public class UserServiceTest {
	@Autowired 
	UserService userService;
	
	@Autowired 
	UserServiceImpl userServiceImpl;
	
	@Autowired UserDao userDao;	
	
	@Autowired MailSender mailSender; 
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired ApplicationContext context;
	
	static List<User> users;	// test fixture
	
	@BeforeEach
	public void setUp() {	
		
		users = Arrays.asList(
			new User("madnite1", "이상호", "p4", "kkang45597@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
			new User("bumjin", "박범진", "p1", "kkang45597@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
			new User("joytouch", "강명성", "p2", "kkang45597@gmail.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
			new User("erwins", "신승한", "p3", "kkang45597@gmail.com", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),				
			new User("green", "오민규", "p5", "kkang45597@gmail.com", Level.GOLD, 100, Integer.MAX_VALUE)
			);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl(); 
		
		MockUserDao mockUserDao = new MockUserDao(this.users);  
		userServiceImpl.setUserDao(mockUserDao);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);  
				
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();  
		assertEquals(updated.size(), 2);
//		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER); // 오류용
//		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD); // 오류용
		checkUserAndLevel(updated.get(0), "madnite1", Level.GOLD);  // 성공용
		checkUserAndLevel(updated.get(1), "joytouch", Level.SILVER); // 성공용
		
		List<String> request = mockMailSender.getRequests();  
		assertEquals(request.size(), 2);
		assertEquals(request.get(0), users.get(1).getEmail());
		assertEquals(request.get(1), users.get(3).getEmail());
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) { 
		assertEquals(updated.getId(), expectedId);
		assertEquals(updated.getLevel(), expectedLevel);
	}
	
	static class MockUserDao implements UserDao { 
		private List<User> users;  
		private List<User> updated = new ArrayList(); 
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}

		public List<User> getUpdated() {
			return this.updated;
		}

		public List<User> getAll() {  
			return this.users;
		}

		public void update(User user) {  
			updated.add(user);
		}
		
		public void add(User user) { throw new UnsupportedOperationException(); }
		public void deleteAll() { throw new UnsupportedOperationException(); }
		public Optional<User> get(String id) { throw new UnsupportedOperationException(); }
		public int getCount() { throw new UnsupportedOperationException(); }

		@Override
		public void setDataSource(DataSource dataSource) {
			
		}
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
	
	@Test
	@DirtiesContext
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		UserDao mockUserDao = mock(UserDao.class);	    
		when(mockUserDao.getAll()).thenReturn(this.users); // getAll 호출시
		userServiceImpl.setUserDao(mockUserDao);

		MailSender mockMailSender = mock(MailSender.class); // 가짜를 전달
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		verify(mockUserDao, times(2)).update(any(User.class));				  
//		verify(mockUserDao).update(users.get(1)); // 오류용
//		verify(mockUserDao).update(users.get(3)); // 오류용
		verify(mockUserDao).update(users.get(0)); // 성공용
		verify(mockUserDao).update(users.get(2)); // 성공용
		
//		assertEquals(users.get(1).getLevel(), Level.SILVER); // 오류용
//		assertEquals(users.get(3).getLevel(), Level.GOLD); // 오류용
		assertEquals(users.get(0).getLevel(), Level.GOLD); // 성공용
		assertEquals(users.get(2).getLevel(), Level.SILVER); // 성공용

		
		// 가짜 메일을 보낼때 아규먼트를 캡쳐
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);  
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
//		assertEquals(mailMessages.get(0).getTo()[0], users.get(1).getEmail()); // 오류용
//		assertEquals(mailMessages.get(1).getTo()[0], users.get(3).getEmail()); // 오류용
		assertEquals(mailMessages.get(0).getTo()[0], users.get(0).getEmail()); // 성공용
		assertEquals(mailMessages.get(1).getTo()[0], users.get(2).getEmail()); // 성공용
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		Optional<User> optionalUser = userDao.get(user.getId());
		if (!optionalUser.isEmpty()) {
			User userUpdate = optionalUser.get();
			if (upgraded) {
				assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
			}
			else {
				assertEquals(userUpdate.getLevel(), (user.getLevel()));
			}			
		}		
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);	  // GOLD Level
		User userWithoutLevel = users.get(0);  
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);	  
		userService.add(userWithoutLevel);
		
		Optional<User> optionalUserWithLevelRead = userDao.get(userWithLevel.getId());
		if (!optionalUserWithLevelRead.isEmpty()) {
			User userWithLevelRead = optionalUserWithLevelRead.get();
			assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel()); 
		}		
		
		Optional<User> optionalUserWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		if (!optionalUserWithoutLevelRead.isEmpty()) {
			User userWithoutLevelRead = optionalUserWithoutLevelRead.get();
			assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
		}		
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
//		TestUserService testUserService = 
//				new TestUserService(users.get(2).getId()); 	// 실패용
		TestUserService testUserService = 
				new TestUserService(users.get(3).getId()); // 성공용
		
		testUserService.setUserDao(userDao); // 추가
		testUserService.setMailSender(mailSender); // 추가

		/*	// 빈이 없어서 실행이 안됨
		TxProxyFactoryBean txProxyFactoryBean = 
				context.getBean("&userServiceFactoryBean", TxProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService) txProxyFactoryBean.getObject();
		 */
		
		userDao.deleteAll(); 
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();
			
		}
		catch(TestUserServiceException e) { 
			e.printStackTrace();
			
			fail("TestUserServiceException expected"); 
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	@Test
	public void upgradeLevelsProxy() throws Exception {		
		userDao.deleteAll();			  
		for(User user : users) 
			userDao.add(user);
				
		userService.upgradeLevels();	

	}
	
	static class TestUserService extends UserServiceImpl {
		private String id;
		UserServiceImpl usImpl;
		
		private TestUserService(String id) {  
			this.id = id;
		}
		
		public void setUsImpl(UserServiceImpl usImpl) {
			this.usImpl = usImpl;
		}
		
		@Override
		public void upgradeLevels() {
			TransactionStatus status = this.getTransactionManager().
					getTransaction(new DefaultTransactionDefinition());			
			
			try {
				for (User user : users) {
					if (usImpl.canUpgradeLevel(user)) {
						upgradeLevel(user);
					}
				}
				this.getTransactionManager().commit(status);
			} catch (RuntimeException e) {
				this.getTransactionManager().rollback(status);
				throw e;
			}
		}

		private PlatformTransactionManager getTransactionManager() {
			return null;
		}

		public void upgradeLevel(User user) {
			if (user.getId().equals(this.id)) 
				throw new TestUserServiceException();  
			this.usImpl.upgradeLevel(user);			
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
	}
	
	@Test
	public void ClassNamePointcutAdvisor() {
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			public ClassFilter getClassFilter() {
				return new ClassFilter() { // boolean matches(Class<?> clazz);
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		
		classMethodPointcut.setMappedName("sayH*");
		
		class HelloWorld extends HelloTarget {}; // local class 	
		checkAdviced(new HelloWorld(), classMethodPointcut, false);
		
//		System.out.println("***********************************************");
		class HelloToby extends HelloTarget {};  // local class 
		checkAdviced(new HelloToby(), classMethodPointcut, true);		
		
	}
	
	private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		if (adviced) {
			assertEquals(proxiedHello.sayHello("Toby"), "HELLO TOBY");
			assertEquals(proxiedHello.sayHi("Toby"), "HI TOBY");
			assertEquals(proxiedHello.sayThanks("Toby"), "Thanks Toby");
		}
		else {
			assertEquals(proxiedHello.sayHello("Toby"), "Hello Toby");
			assertEquals(proxiedHello.sayHi("Toby"), "Hi Toby");
			assertEquals(proxiedHello.sayThanks("Toby"), "Thanks Toby");
		}
	}
	
	private void checkAdviced2(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();		
		
		System.out.println(proxiedHello.sayHello("Toby"));
		System.out.println(proxiedHello.sayHi("Toby"));
		System.out.println(proxiedHello.sayThanks("Toby"));
		
	}

}