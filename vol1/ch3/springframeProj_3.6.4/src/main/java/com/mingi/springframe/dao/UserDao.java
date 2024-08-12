package com.mingi.springframe.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.mingi.springframe.domain.User;

public class UserDao {
	private DataSource dataSource;
		
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
			
		this.dataSource = dataSource;
	}
	
	private JdbcTemplate jdbcTemplate;
	
	public void add(final User user) {
		this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
						user.getId(), user.getName(), user.getPassword());
	}


//	@SuppressWarnings("deprecation")
//	public User get(String id) {
		
//		return this.jdbcTemplate.queryForObject("select * from users where id = ?", // 더 이상 지원되지 않음
//				new Object[] {id}, 
//				new RowMapper<User>() {
//					public User mapRow(ResultSet rs, int rowNum)
//							throws SQLException {
//						User user = new User();
//						user.setId(rs.getString("id"));
//						user.setName(rs.getString("name"));
//						user.setPassword(rs.getString("password"));
//						return user;
//					}
//				});
		
//		return this.jdbcTemplate.queryForObject("select * from users where id = ?", // Chat GPT 수정
//				new Object[] {id}, (ResultSet rs, int rowNum) -> {
//					User user = new User();
//					user.setId(rs.getString("id"));
//					user.setName(rs.getString("name"));
//					user.setPassword(rs.getString("password"));
//					return user;
//				});
//	} 
//	
	public Optional<User> get(String id) throws ClassNotFoundException, SQLException { // 강사님꺼
		String sql = "select * from users where id = ?";
		try (Stream<User> stream = 
				jdbcTemplate.queryForStream(
						sql, 
						userRowMapper(),
						id)){
			return stream.findFirst();
			
		} catch (DataAccessException e){
			return Optional.empty();
		}
	}

	private RowMapper<User> userRowMapper() {
		return (ResultSet rs, int rowNum) -> {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		};
	}


	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() {
//		return this.jdbcTemplate.queryForInt("select count(*) from users");
//		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class); // Chat GPT
		
		List<Integer> result = jdbcTemplate.query("select count(*) from users",
				(rs, rowNum) -> rs.getInt(1)); // 강사님 방식
		
		return (int)DataAccessUtils.singleResult(result); // (int) 제너릭으로 유추하기 때문에 없어도 된다. (언박싱)
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id",
				new RowMapper<User>() {
					public User mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						User user = new User();
						user.setId(rs.getString("id"));
						user.setName(rs.getString("name"));
						user.setPassword(rs.getString("password"));
						return user;
					}
				});
		
//		return this.jdbcTemplate.query("select * from users order by id",
//				(ResultSet rs, int rowNum) -> {
//					User user = new User();
//	                user.setId(rs.getString("id"));
//	                user.setName(rs.getString("name"));
//	                user.setPassword(rs.getString("password"));
//	                return user;
//				});
	}

}
