package com.gaooh.lump.entity;

import java.io.Serializable;
import java.util.Date;

import net.java.ao.Entity;
import net.java.ao.OneToOne;

/**
 * Entity:Comment
 * ガイドボートに対するコメント情報
 * @author gaooh
 * @date 2008/05/06
 */
public interface Comment extends Entity, Serializable {

	/**
	 * @return
	 */
	public User getUser();
	
	/**
	 * @return
	 */
	public Integer getGuideboardID();
    
    /**
     * @param guideboardId
     */
    public void setGuideboardID(Integer guideboardId);
    
    /**
     * @return
     */
    public Integer getUserId();
    
    /**
     * @param userId
     */
    public void setUserId(Integer userId);
    
    /**
     * @return
     */
    public String getComment();
    
    /**
     * @param comment
     */
    public void setComment(String comment);
    
    /**
     * @return
     */
    public Date getCreatedAt();
    
    /**
     * @param date
     */
    public void setCreatedAt(Date date);
}
