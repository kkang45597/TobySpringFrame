package com.mingi.sprinframe.daoTest;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DaoFactoryTest.class })
@DirtiesContext
public class UserDaoTest {
	@Autowired
	ApplicationContext context;
	
	@Autowired 
	DataSource dataSource;
	
	private UserDao dao; 
	
	private User user1;
	private User user2;
	private User user3;
	
	@BeforeEach
	public void setUp() {
		
		// AlpabectNumberic, NumbericAlpabet
		this.dao = this.context.getBean("userDao", UserDao.class);
		
		this.user1 = new User("user1", "유저1", "springno1");
		this.user2 = new User("user2", "유저2", "springno2");
		this.user3 = new User("user3", "유저3", "springno3");

	}
	
	@Test 
	public void andAndGet() throws SQLException, ClassNotFoundException {		
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		dao.add(user1);
		dao.add(user2);
		assertEquals(dao.getCount(), 2);
		
//		User userget1 = dao.get(user1.getId());
//		assertEquals(userget1.getName(), user1.getName());
//		assertEquals(userget1.getPassword(), user1.getPassword());
//		
//		User userget2 = dao.get(user2.getId());
//		assertEquals(userget2.getName(), user2.getName());
//		assertEquals(userget2.getPassword(), user2.getPassword());
		
		Optional<User> Optuserget1  = dao.get(user1.getId());
		if (!Optuserget1.isEmpty()) {
			User userget = Optuserget1.get();
			assertEquals(user1.getName(), userget.getName());
			assertEquals(user1.getPassword(), userget.getPassword());
		}
		
		Optional<User> Optuserget2  = dao.get(user2.getId());
		if (!Optuserget2.isEmpty()) {
			User userget = Optuserget2.get();
			assertEquals(user2.getName(), userget.getName());
			assertEquals(user2.getPassword(), userget.getPassword());
		}
	}

	@Test
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);

		// EmptyResultDataAccessException 예외를 발생시키면 이 단위 테스트는 성공한다.
		// 만약 어떠한 예외도 발생하지 않거나, EmptyResultDataAccessException가 아닌 예외가 발생하면 실패가된다.
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
		// 오류 발생 ()
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
	
	@Test
	public void getAll()  {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertEquals(users0.size(), 0);
		
		dao.add(user1);
		List<User> users1 = dao.getAll();
		assertEquals(users1.size(), 1);
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2);
		List<User> users2 = dao.getAll();
		assertEquals(users2.size(), 2);
		checkSameUser(user1, users2.get(0));  
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3);
		List<User> users3 = dao.getAll();
		assertEquals(users3.size(), 3);
		checkSameUser(user1, users3.get(0));  
		checkSameUser(user2, users3.get(1));  
		checkSameUser(user3, users3.get(2));  
	}
	
	private void checkSameUser(User user1, User user2) {
		assertEquals(user1.getId(), user2.getId());
		assertEquals(user1.getName(), user2.getName());
		assertEquals(user1.getPassword(), user2.getPassword());
	}
	
	@Test 
	public void duplciateKey() throws SQLException {		
		dao.deleteAll();
		
		dao.add(user1);
		assertThrows(DuplicateKeyException.class, () -> dao.add(user1));
	}
	
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		}
//		catch (DuplicateKeyException e) { // 에러코드가 나열됨
//			e.printStackTrace();
//		}
		catch(RuntimeException ex) { // FM 이지만 사용하지 않는다. (단위 테스트이기 때문에 안씀)
			SQLException sqlEx = (SQLException)ex.getCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);			
			DataAccessException transEx = set.translate(null, null, sqlEx);
			
			assertEquals(DuplicateKeyException.class, transEx.getClass());
		}
		finally {
			
		}
	}
}