package com.gaooh.lump.entity;

import java.io.Serializable;

import net.java.ao.Entity;

public interface User extends Entity, Serializable {

	public String getOfficeId();
	
	public void setOfficeId(String officeId);
}
