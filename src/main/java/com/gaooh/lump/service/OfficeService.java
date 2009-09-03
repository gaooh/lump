package com.gaooh.lump.service;

import com.gaooh.lump.service.impl.OfficeServiceImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(OfficeServiceImpl.class)
public interface OfficeService {

	public void updateBlogRss();
	
	public boolean isAuth(String officeId, String password);
}
