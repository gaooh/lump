package com.gaooh.lump.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import net.java.ao.Entity;
import net.java.ao.schema.SQLType;

public interface Site extends Entity, Serializable {

	public static enum Type {normal, office, hatena};
	
	/**
	 * 1:1 To User Table
	 * @return
	 */
	public User getUser();
	
	public Integer getType();
	
	public void setType(Integer type);
	
	public String getUrl();
	
	public void setUrl(String url);
	
	public String getTitle();
	
    public void setTitle(String name);
    
    public String getDescription();
    
    public void setDescription(String description);
    
	public String getHtml();
    
    public void setHtml(String html);
    
    public Integer getUserId();
    
    public void setUserId(Integer userId);
    
    public Date getCreatedAt();
    
    public void setCreatedAt(Date date);
    
    public Date getUpdatedAt();
    
    public void setUpdatedAt(Date date);
    
    public Date getDeletedAt();
    
    public void setDeletedAt(Date date);
}
