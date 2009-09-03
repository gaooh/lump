package com.gaooh.lump.entity;

import java.io.Serializable;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("guideboard_star")
public interface GuideboardStar extends Entity, Serializable {

	public Integer getGuideboardID();
    
    public void setGuideboardID(Integer guideboardId);
    
    public Integer getUserID();
    
    public void setUserID(Integer userId);
    
    public Integer getStarCount();
    
    public void setStarCount(Integer starCount);
}
