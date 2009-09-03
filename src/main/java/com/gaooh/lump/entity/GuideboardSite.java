package com.gaooh.lump.entity;

import java.io.Serializable;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("guideboard_site")
public interface GuideboardSite extends Entity, Serializable {

	public Site getSite();
	
	public Guideboard getGuideboard();
	
	public Integer getGuideboardID();
    
    public void setGuideboardID(Integer guideboardId);
    
    public Integer getSiteID();
    
    public void setSiteID(Integer siteId);
}
