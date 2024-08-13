package com.mingi.sprinframe.serviceTest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mingi.sprinframe.daoTest.DaoFactoryTest;
import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;
import com.mingi.springframe.service.UserService;
import static com.mingi.springframe.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.mingi.springframe.service.UserService.MIN_RECCOMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DaoFactoryTest.class })
public class UserServiceTest {
	
	@Autowired
	UserService userService;	
	
	@Autowired 
	UserDao userDao;
	
	List<User> users;
	
	@BeforeEach
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1),
				new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
				);
	}

	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		
//		Optional<User> userUpdate = userDao.get(user.getId());
		
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
		
//		assertEquals(userWithLevelRead.getLevel(),  userWithLevel.getLevel()); 
//		assertEquals(userWithoutLevelRead.getLevel(),  Level.BASIC);
	}
}