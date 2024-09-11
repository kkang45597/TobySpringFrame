package com.mingi.springframe.sqlservice;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.sqlservice.json.SQLMapReader;
import com.mingi.springframe.sqlservice.json.SQLMap;

@Service
public class JsonSqlService implements SqlService {

	private final BaseSqlService baseSqlService = new BaseSqlService();

	private final SQLMapReader jsonSQLMapReader;
	private SqlRegistry sqlRegistry = new HashMapSqlRegistry();


    @Autowired
    public JsonSqlService(SQLMapReader jsonSQLMapReader) {
        this.jsonSQLMapReader = jsonSQLMapReader;
    }
    
    @Autowired
    public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
    
    @PostConstruct
	public void loadSql() {
		this.baseSqlService.setSqlReader(this.jsonSQLMapReader);
		this.baseSqlService.setSqlRegistry(this.sqlRegistry);
		
		this.baseSqlService.loadSql();
	}

    // 특정 SQL 쿼리를 key로 찾아서 반환하는 메서드
    public String getQueryByKey(String key) {
        return jsonSQLMapReader.getSqlMap().getSqlmap().stream()
                .filter(sqlQuery -> sqlQuery.getKey().equals(key))
                .map(sqlQuery -> sqlQuery.getQuery())
                .findFirst()
                .orElse(null);
    }

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		// TODO Auto-generated method stub
		//return getQueryByKey(key);
		return this.baseSqlService.getSql(key);
	}
	
	
}