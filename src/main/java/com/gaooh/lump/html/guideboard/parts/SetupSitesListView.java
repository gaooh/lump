package com.gaooh.lump.html.guideboard.parts;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.guideboard.SiteSimplePanel;

/**
 * 
 * @author gaooh
 * @date 2008/05/05
 */
public class SetupSitesListView extends PageableListView {

	private static final long serialVersionUID = 1L;

	private Integer index;
	
	private Integer userId;
	
	public SetupSitesListView(String id, List setUpSites, Integer userId) {
		super(id, setUpSites, 10);
		index = 1;
		this.userId = userId;
	}

	@Override
	protected void populateItem(ListItem item) {
		Site site = (Site) item.getModelObject();
		SiteSimplePanel panel = new SiteSimplePanel("site", site, userId, index++);
		panel.setOutputMarkupId(true);
		item.add(panel);
	}
}
