package com.mingi.sprinframe.daoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DaoFactoryTest.class })
@DirtiesContext
public class UserDaoTest {
	@Autowired
	ApplicationContext context;
	
	private UserDao dao; 
	
	private User user1;
	private User user2;
	private User user3;
	
	@BeforeEach
	public void setUp() {
		this.dao = this.context.getBean("userDao", UserDao.class);
		
		this.user1 = new User("user1", "유저1", "springno1");
		this.user2 = new User("user2", "유저2", "springno2");
		this.user3 = new User("user3", "유저3", "springno3");

	}
	
	@Test 
	public void andAndGet() throws SQLException {		
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		dao.add(user1);
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
		User userget1 = dao.get(user1.getId());
		assertEquals(userget1.getName(), user1.getName());
		assertEquals(userget1.getPassword(), user1.getPassword());
		
		User userget2 = dao.get(user2.getId());
		assertEquals(userget2.getName(), user2.getName());
		assertEquals(userget2.getPassword(), user2.getPassword());
	}

	@Test
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		// EmptyResultDataAccessException 예외를 발생시키면 이 단위 테스트는 성공한다.
		// 만약 어떠한 예외도 발생하지 않거나, EmptyResultDataAccessException가 아닌 예외가 발생하면 실패가된다.
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
	}
	
	@Test
	public void count() throws SQLException {
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);
				
		dao.add(user1);
		assertEquals(dao.getCount(), 1);
		
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
		dao.add(user3);
		assertEquals(dao.getCount(), 3);
	}
}