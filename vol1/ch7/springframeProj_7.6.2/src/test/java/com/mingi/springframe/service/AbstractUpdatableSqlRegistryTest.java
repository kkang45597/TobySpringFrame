package com.mingi.springframe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mingi.springframe.sqlservice.SqlNotFoundException;
import com.mingi.springframe.sqlservice.SqlUpdateFailureException;
import com.mingi.springframe.sqlservice.UpdatableSqlRegistry;

public abstract class AbstractUpdatableSqlRegistryTest {
	UpdatableSqlRegistry sqlRegistry;
	
	@BeforeEach
	public void setUp() {
		sqlRegistry = createUpdatableSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}
	
	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

	@Test
	public void find() {
		checkFind("SQL1", "SQL2", "SQL3");
	}
	
	@Test
	public void unknownKey() {
		//sqlRegistry.findSql("SQL9999!@#$");
		assertThrows(SqlNotFoundException.class, () -> sqlRegistry.findSql("SQL9999!@#$"));
		
	}
	
	protected void checkFind(String expected1, String expected2, String expected3) {
		assertEquals(sqlRegistry.findSql("KEY1"), expected1);		
		assertEquals(sqlRegistry.findSql("KEY2"), expected2);		
		assertEquals(sqlRegistry.findSql("KEY3"), expected3);		
	}
	
	@Test
	public void updateSingle() {
		sqlRegistry.updateSql("KEY2", "Modified2");
		
		checkFind("SQL1", "Modified2", "SQL3");
	}
	
	@Test
	public void updateMulti() {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");
		
		sqlRegistry.updateSql(sqlmap);
		
		checkFind("Modified1", "SQL2", "Modified3");
	}
	
	@Test
	public void updateWithNotExistingKey() {
		//sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
		assertThrows(SqlUpdateFailureException.class, () -> sqlRegistry.updateSql("SQL9999!@#$", "Modified2"));
	}
}