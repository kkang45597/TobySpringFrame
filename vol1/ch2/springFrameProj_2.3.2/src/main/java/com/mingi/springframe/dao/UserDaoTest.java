package com.mingi.springframe.dao;

import java.sql.SQLException;

//import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mingi.springframe.domain.User;

public class UserDaoTest {	
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {				
		ApplicationContext context =
				new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();	
		// Assertion 메소드들 중에 하나인,
		assertEquals(dao.getCount(), 0);
		
		User user = new User();
		user.setId("gyumee");
		user.setName("박성철");
		user.setPassword("springno1");
		dao.add(user);
		assertEquals(dao.getCount(), 1);
		
		User user2 = dao.get(user.getId());		
		
		assertEquals(user2.getName(), user.getName());
		assertEquals(user2.getPassword(), user.getPassword());		
	}
}