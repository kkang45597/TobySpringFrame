package com.mingi.springframe.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MConnectionMaker implements ConnectionMaker {
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		
//		Class.forName("com.mysql.jdbc.Driver");
		
		Connection c = DriverManager.getConnection(
				"jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8",
				"root", "1234");
		return c;
	}
}