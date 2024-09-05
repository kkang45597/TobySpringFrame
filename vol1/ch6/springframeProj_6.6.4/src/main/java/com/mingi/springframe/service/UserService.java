package com.mingi.springframe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mingi.springframe.domain.User;

@Service
@Transactional
public interface UserService {
	void add(User user);
	
	void deleteAll();
	
	void update(User user);		
	
	@Transactional(readOnly = true)
	Optional<User> get(String id);	
	
	@Transactional(readOnly = true) // 없으면 readOnlyTransactionAttribute() 테스트가 실패
	List<User> getAll();
	
	void upgradeLevels();
}