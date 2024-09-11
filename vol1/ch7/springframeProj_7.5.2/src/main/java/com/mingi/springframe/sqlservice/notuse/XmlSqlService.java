package com.mingi.springframe.sqlservice.notuse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.sqlservice.SqlNotFoundException;
import com.mingi.springframe.sqlservice.SqlReader;
import com.mingi.springframe.sqlservice.SqlRegistry;
import com.mingi.springframe.sqlservice.SqlRetrievalFailureException;
import com.mingi.springframe.sqlservice.SqlService;
import com.mingi.springframe.sqlservice.json.SQLMap;
import com.mingi.springframe.sqlservice.json.SQLQuery;

public class XmlSqlService implements SqlService {

	// --------- SqlProvider ------------
	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;
		
	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return this.sqlRegistry.findSql(key);
		} 
		catch(SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}

	// --------- SqlRegistry ------------	
	private Map<String, String> sqlMap = new HashMap<String, String>();

	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if (sql == null)  
			throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다");
		else
			return sql;

	}

	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}
	
	// --------- SqlReader ------------
	private String sqlmapFile;

	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}

	public void read(SqlRegistry sqlRegistry) {
		String contextPath = SQLMap.class.getPackage().getName(); 
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
			
			SQLMap sqlmap = (SQLMap)unmarshaller.unmarshal(is);
			for(SQLQuery sql : sqlmap.getSqlmap()) {
				sqlRegistry.registerSql(sql.getKey(), sql.getQuery());
			}
		} 
		catch (JAXBException e) {
			throw new RuntimeException(e);
		} 		
	}

}