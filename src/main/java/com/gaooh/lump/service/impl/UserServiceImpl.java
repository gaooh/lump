package com.gaooh.lump.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.java.ao.EntityManager;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.User;
import com.gaooh.lump.service.UserService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

@Singleton
public class UserServiceImpl extends AbstractModule implements UserService {

	private EntityManager manager;
	
	public UserServiceImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
	}

	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.UserService#findById(java.lang.Integer)
	 */
	public User findById(Integer userId) {
		User[] users = null;
		try {
			users = manager.find(User.class, "id = ?", userId);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		if(users.length == 0) {
			return null;
		} else {
			return users[0];
		}
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.UserService#findByOfficeId(java.lang.String)
	 */
	public User findByOfficeId(String officeId) {
		User[] users = null;
		try {
			users = manager.find(User.class, "officeId = ?", officeId);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		if(users.length == 0) {
			return null;
		} else {
			return users[0];
		}
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.UserService#createByOfficeId(java.lang.String)
	 */
	public void createByOfficeId(String officeId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("officeId", officeId);
		try {
			manager.create(User.class, param);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
	}
	
	@Override
	protected void configure() {
		bind( UserService.class ).to( UserServiceImpl.class ).in( Scopes.SINGLETON );
	}
	
}
