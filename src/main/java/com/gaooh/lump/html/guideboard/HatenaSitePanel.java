package com.gaooh.lump.html.guideboard;

import com.gaooh.lump.entity.Site;

/**
 * はてなサイトパネル
 * 
 * @author gaooh
 * @date 2008/05/09
 */
public class HatenaSitePanel extends SitePanel {

	private static final long serialVersionUID = 1L;

	public HatenaSitePanel(String id, Site site, Integer userId, boolean isAddGuideBoardVisabled) {
		super(id, site, userId, isAddGuideBoardVisabled);
	}

}
