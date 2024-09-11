package com.mingi.springframe.sqlservice;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}