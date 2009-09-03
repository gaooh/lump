package com.gaooh.lump.entity;

import java.io.Serializable;
import java.util.Date;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("user_site")
public interface UserSite extends Entity, Serializable {

	/**
	 * 1:1 To user Table
	 * 
	 * @return
	 */
	public User getUser();

	/**
	 * 1:1 To site Table
	 * 
	 * @return
	 */
	public Site getSite();
	
	public Integer getSiteID();

	public void setSiteID(Integer siteId);

	public Integer getUserID();

	public void setUseID(Integer userId);
	
	public String getComment();
    
    public void setComment(String comment);
    
	public Date getCreatedAt();

	public void setCreatedAt(Date date);
}
