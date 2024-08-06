package com.mingi.springframe.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HConnectionMaker implements ConnectionMaker {
	
//	public HConnectionMaker() { } // XML 기반 테스트
	
	public Connection makeConnection() throws ClassNotFoundException,
			SQLException {
//		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
		return c;
	}
}
