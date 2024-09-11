package com.mingi.springframe.sqlservice.notuse;

import com.mingi.springframe.sqlservice.BaseSqlService;
import com.mingi.springframe.sqlservice.HashMapSqlRegistry;

public class DefaultSqlService extends BaseSqlService {
	public DefaultSqlService() {
		setSqlReader(new JaxbXmlSqlReader());
		setSqlRegistry(new HashMapSqlRegistry());
	}
}