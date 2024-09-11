package com.mingi.springframe.service;

import com.mingi.springframe.sqlservice.UpdatableSqlRegistry;
import com.mingi.springframe.sqlservice.updatable.ConcurrentHashMapSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
}