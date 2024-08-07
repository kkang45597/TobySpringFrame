package com.mingi.springframe.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

//	public abstract Connection makeConnection() throws ClassNotFoundException, SQLException;
	Connection makeConnection() throws ClassNotFoundException, SQLException; // 인터페이스라 생략 가능

}