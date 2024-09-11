package com.mingi.springframe.sqlservice.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingi.springframe.sqlservice.SqlReader;
import com.mingi.springframe.sqlservice.SqlRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
public class SQLMapReader implements SqlReader{
    private SQLMap sqlMap;
//    private SqlRegistry sqlRegistry;

//    @Autowired
//    public void setSqlRegistry(SqlRegistry sqlRegistry) {
//        this.sqlRegistry = sqlRegistry;
//    }

    public SQLMapReader() {
        // JSON 파일을 resources 디렉토리에서 읽어와서 SQLMap 객체로 변환
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            // ClassPathResource로 resources/sqlmap.json 파일을 읽어옴
//            ClassPathResource resource = new ClassPathResource("sqlmap.json");
//            InputStream inputStream = resource.getInputStream();
//            this.sqlMap = objectMapper.readValue(inputStream, SQLMap.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @PostConstruct
    public void init() {
//        // 의존성 주입 후에 sqlRegistry에 SQL을 등록
//        for (SQLQuery sql : this.sqlMap.getSqlmap()) {
//            sqlRegistry.registerSql(sql.getKey(), sql.getQuery());
//        }
    }

    public SQLMap getSqlMap() {
        return sqlMap;
    }

	@Override
	public void read(SqlRegistry sqlRegistry) {
		ObjectMapper objectMapper = new ObjectMapper();
        try {
            // ClassPathResource로 resources/sqlmap.json 파일을 읽어옴
            ClassPathResource resource = new ClassPathResource("sqlmap.json");
            InputStream inputStream = resource.getInputStream();
            this.sqlMap = objectMapper.readValue(inputStream, SQLMap.class);
            
            // 의존성 주입 후에 sqlRegistry에 SQL을 등록
            for (SQLQuery sql : this.sqlMap.getSqlmap()) {
                sqlRegistry.registerSql(sql.getKey(), sql.getQuery());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
}