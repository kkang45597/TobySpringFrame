package com.mingi.sprinframe.domain;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;

public class UserTest {
	User user;
	
	@BeforeEach
	public void setUp() {
		user = new User();
	}
	
	@Test()
	public void upgradeLevel() {
		Level[] levels = Level.values();
		for(Level level : levels) {
			if (level.nextLevel() == null) continue;
			user.setLevel(level);
			user.upgradeLevel();
			assertEquals(user.getLevel(),  level.nextLevel());
		}
	}
	
	@Test
	public void cannotUpgradeLevel() throws IllegalStateException {
		Level[] levels = Level.values();
		for(Level level : levels) {
			if (level.nextLevel() != null) continue;
			user.setLevel(level);
			user.upgradeLevel();
		}
	}

}