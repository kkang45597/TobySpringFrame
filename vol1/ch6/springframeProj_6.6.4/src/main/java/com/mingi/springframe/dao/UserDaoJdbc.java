package com.mingi.springframe.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mingi.springframe.domain.User;
import com.mingi.springframe.domain.Level;

public class UserDaoJdbc implements UserDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private RowMapper<User> userRowMapper() {
		return (rs, rowNum) -> {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		};
	}
	

	@Override
	public void add(User user) {
		try {
			this.jdbcTemplate.update(
					"insert into users(id, name, password, email, level, login, recommend) " +
							"values(?,?,?,?,?,?,?)", 
							user.getId(), 
							user.getName(), 
							user.getPassword(), 
							user.getEmail(),
							user.getLevel().intValue(), 
							user.getLogin(), 
							user.getRecommend());
		} catch (DataAccessException de) {
			System.out.println(de);			
		} 

	}

	@Override
	public Optional<User> get(String id) {
		String sql = "select * from users where id = ?";
		try (Stream<User> stream = 
				jdbcTemplate.queryForStream(
						sql, 
						userRowMapper(),
						id)) {
			return stream.findFirst();
		} catch (DataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id",
				userRowMapper());

	}

	@Override
	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	@Override
	public int getCount() {
		List<Integer> result = jdbcTemplate.query(
				"select count(*) from users",
				(rs, rowNum) -> rs.getInt(1));
		
		return DataAccessUtils.singleResult(result);
	}
	
	@Override
	public void update(User user) {
		this.jdbcTemplate.update(
				"update users set "
				+ "name = ?, "
				+ "password = ?, "
				+ "email = ?, "
				+ "level = ?, "
				+ "login = ?, " 
				+ "recommend = ? "
				+ "where id = ? ", 
				user.getName(), 
				user.getPassword(), 
				user.getEmail(),
				user.getLevel().intValue(), ///////
				user.getLogin(), 
				user.getRecommend(),
				user.getId());
		
	}

}