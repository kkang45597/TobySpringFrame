package com.mingi.sprinframe.daoTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mingi.springframe.domain.User;

public class UserDaoSql {
	
	public UserDaoSql() {
		
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.driver");
		
		Connection c = DriverManager.getConnection(
				"jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8", "root", "1234");
		return c;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = getConnection();
		
		PreparedStatement ps = c.prepareStatement( "insert into users(id, name, passowrd) values(?, ?, ?)");
		
		ps.setString(1,  user.getId());
		ps.setString(2,  user.getName());
		ps.setString(3,  user.getPassword());
		
		ps.close();
		c.close();
	}

}
