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
import org.springframework.jdbc.core.RowMapper;

import com.mingi.springframe.domain.Level;
import com.mingi.springframe.domain.User;

public class UserDaoJdbc implements UserDao {
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					user.setEmail(rs.getString("email"));
					user.setLevel(Level.valueOf(rs.getInt("level")));
					user.setLogin(rs.getInt("login"));
					user.setRecommend(rs.getInt("recommend"));
					return user;
			}
		};

	public void add(final User user) {
		this.jdbcTemplate.update(
				"insert into users(id, name, password, email, level, login, recommend) " +
					"values(?,?,?,?,?,?,?)", 
					user.getId(), user.getName(), user.getPassword(), user.getEmail(),
					user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}

	public Optional<User> get(String id) { // 강사님꺼
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
			user.setEmail("email");
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		};
	}

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() {	
		List<Integer> result = jdbcTemplate.query("select count(*) from users",
				(rs, rowNum) -> rs.getInt(1)); // 강사님 방식
		
		return (int)DataAccessUtils.singleResult(result); // (int) 제너릭으로 유추하기 때문에 없어도 된다. (언박싱)
	}

	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users", this.userMapper);
	}
	
	public void update(User user) {
		this.jdbcTemplate.update(
				"update users set name = ?, password = ?, email = ?, level = ?, login = ?, " +
				"recommend = ? where id = ? ", user.getName(), user.getPassword(), user.getEmail(),
		user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
		
	}
}