package com.mingi.springframe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;

//UserService 인터페이스 도입을 통해 약한 결합을 갖는 유연한 구조
//트랜잭션과 관련된 코드는 모두 제거
public class UserServiceImpl implements UserService {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;

	private UserDao userDao;
	private MailSender mailSender;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}
	
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel(); 
		switch(currentLevel) {                                   
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER); 
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel); 
		}
	}

	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("kkang45597@gmail.com");
		mailMessage.setSubject("Upgrade 반가워요");
		mailMessage.setText("등급을 축하 드려요. " + user.getLevel().name());
		
		this.mailSender.send(mailMessage);
	}
	
	public void add(User user) {
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}

	public void deleteAll() { 	userDao.deleteAll(); }
	
	public Optional<User> get(String id) { 
		return userDao.get(id); 
	}
	
	public List<User> getAll() { 
		return userDao.getAll(); 
	}
	
	public void update(User user) { 
		userDao.update(user); 
	}
}