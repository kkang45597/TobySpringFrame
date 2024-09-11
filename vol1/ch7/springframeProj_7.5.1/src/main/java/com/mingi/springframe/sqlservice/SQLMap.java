package com.mingi.springframe.sqlservice;

import java.util.List;

public class SQLMap {
    private List<SQLQuery> sqlmap;

    // Getters and Setters
    public List<SQLQuery> getSqlmap() {
        return sqlmap;
    }

    public void setSqlmap(List<SQLQuery> sqlmap) {
        this.sqlmap = sqlmap;
    }
}