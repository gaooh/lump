package com.gaooh.lump.service;

import com.gaooh.lump.entity.User;
import com.gaooh.lump.service.impl.UserServiceImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {

	/**
	 * 主キーでユーザを検索する。なければnullを返す
	 * @param userId
	 * @return
	 */
	public User findById(Integer userId);
	
	public User findByOfficeId(String officeId);
	
	public void createByOfficeId(String officeId);
}
