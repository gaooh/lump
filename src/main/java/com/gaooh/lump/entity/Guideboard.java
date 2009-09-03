package com.gaooh.lump.entity;

import java.io.Serializable;
import java.util.Date;

import net.java.ao.Entity;

/**
 * Entity:guideboard
 * 
 * @author gaooh
 * @date 2008/05/06
 */
public interface Guideboard extends Entity, Serializable {
	
	/**
	 * 1:1
	 * @return
	 */
	public User getUser();
	
	public String getTitle();
	
    public void setTitle(String name);
    
    public String getDescription();
    
    public void setDescription(String description);
    
    public Integer getUserId();
    
    public void setUserId(Integer userId);
    
    public Date getCreatedAt();
    
    public void setCreatedAt(Date date);
    
    public Date getUpdatedAt();
    
    public void setUpdatedAt(Date date);
    
    public Date getDeletedAt();
    
    public void setDeletedAt(Date date);
}
