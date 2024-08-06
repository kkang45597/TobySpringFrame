package com.mingi.springframe.dao;

import java.sql.SQLException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mingi.springframe.domain.User;

public class UserDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(DaoFactory.class);
		
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("configuration.xml");
		// XML 기반 테스트 (지금은 사용되지 않음)
		
		// 스프링 IoC 컨테이너는 BeanFactory를 구현한 구체이다. 일반적으론 ApplicationContext 이다.
		// main 에는 DaoFactroy을 userDao 메서드를 호출하는 코드가 없다.
		// @Configuration 이 붙은 클래스는 Bean 설정 클래스로 인식이 된다. 해당 클래스에 정의된 @Bean 이 붙은 메서드는 
		// 스프링 컨테이너가 관리하는 Bean 을 생성하는 메서드로 간주된다.
		
		UserDao dao = context.getBean("userDaoM", UserDao.class);

		User user = new User();
		user.setId("whiteship2");
		user.setName("백기선2");
		user.setPassword("married");

		dao.add(user);
			
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
			
		System.out.println(user2.getId() + " 조회 성공");
	}
}