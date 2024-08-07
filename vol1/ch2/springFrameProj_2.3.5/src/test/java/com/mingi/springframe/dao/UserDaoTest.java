package com.mingi.springframe.dao;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer.OrderAnnotation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import com.mingi.springframe.domain.User;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order 순서대로 실행
//@TestMethodOrder(MethodOrderer.Random.class) // 랜덤으로 실행
//@TestMethodOrder(MethodOrderer.DisplayName.class) // 
public class UserDaoTest {
	
	private UserDao dao; // UserDaoTest의 오브젝트 인스턴스 필드
	
	private User user1;
	private User user2;
	private User user3;
	
	@BeforeAll
	public static void beforeAll() {
		System.out.println("BeforeAll 동작");
	}
	
	@BeforeEach
	public void setUp() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		this.dao = context.getBean("userDao", UserDao.class);
		
		this.user1 = new User("gyumee", "유저 1", "springno1");
		this.user2 = new User("leegw700", "유저 2", "springno2");
		this.user3 = new User("bumjin", "유저 3", "springno3");

	}
	
	@Test 
	@Order(1) // 순서대로 실행
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
	@Order(2) // 순서대로 실행
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertEquals(dao.getCount(), 0);
		
//		public interface Executable {
//			void execute() throws Throwable;
//		}
		
		// EmptyResultDataAccessException 예외를 발생시키면 이 단위 테스트는 성공한다.
		// 만약 어떠한 예외도 발생하지 않거나, EmptyResultDataAccessException가 아닌 예외가 발생하면 실패가된다.
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
	}

	
	@Test
	@Order(3) // 순서대로 실행
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
	
	@AfterEach
	public void afterEach() {
		System.out.println("AfterEach 동작");
	}
	
	@AfterAll
	public static void afterAll() {
		System.out.println("AfterAll 동작");
	}
}