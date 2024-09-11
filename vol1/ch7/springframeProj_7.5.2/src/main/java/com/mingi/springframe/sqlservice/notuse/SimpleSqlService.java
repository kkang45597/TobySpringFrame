package com.mingi.springframe.sqlservice.notuse;

import java.util.Map;

import com.mingi.springframe.sqlservice.SqlRetrievalFailureException;
import com.mingi.springframe.sqlservice.SqlService;

public class SimpleSqlService implements SqlService {

	private Map<String, String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if (sql == null)  
			throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다");
		else
			return sql;
	}

}