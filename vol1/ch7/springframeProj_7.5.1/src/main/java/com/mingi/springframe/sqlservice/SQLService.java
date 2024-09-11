package com.mingi.springframe.sqlservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SQLService {

    private final SQLMapReader sqlMapReader;

    @Autowired
    public SQLService(SQLMapReader sqlMapReader) {
        this.sqlMapReader = sqlMapReader;
    }

    // 특정 SQL 쿼리를 key로 찾아서 반환하는 메서드
    public String getQueryByKey(String key) {
        return sqlMapReader.getSqlMap().getSqlmap().stream()
                .filter(sqlQuery -> sqlQuery.getKey().equals(key))
                .map(sqlQuery -> sqlQuery.getQuery())
                .findFirst()
                .orElse(null);
    }
}