package com.mingi.springframe.daotest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mingi.springframe.dao.DaoFactory;
import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.User;

public class UserDaoTest {	
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		// boolean condition = true ... condition false ...
		// assert condition -> 프로그램이 중단되고 AssertError 발생
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);
		
		User user = new User();
		user.setId("mingi");
		user.setName("민기");
		user.setPassword("springno2");
		dao.add(user);
		assertEquals(dao.getCount(), 1);
		
		User user2 = dao.get(user.getId());		
		
		assertEquals(user2.getName(), user.getName());
		assertEquals(user2.getPassword(), user.getPassword());		
	}
}