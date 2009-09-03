package com.gaooh.lump.html.guideboard.parts;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.list.ListItem;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.guideboard.Edit;
import com.gaooh.lump.html.guideboard.GroupSitePanel;
import com.gaooh.lump.html.guideboard.HatenaSitePanel;
import com.gaooh.lump.html.guideboard.OfficeSitePanel;
import com.gaooh.lump.html.guideboard.SitePanel;
import com.gaooh.wicket.markup.html.list.PageableListView;
import com.gaooh.wicket.markup.html.navigation.paging.IPageableListAction;
import com.gaooh.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 * サイトリストへのリンク
 * 
 * @author gaooh
 * @date 2008/05/05
 */
public class SiteTabListAjaxLink extends TabListAjaxLink {
	
	private static final long serialVersionUID = 1L;

	private IPageableListAction pageableAction;
	
	/** 既に追加されているサイトリスト */
	private List<Site> setUpSites = null;
	
	/** 表示するサイトを表示するパネル */
	private GroupSitePanel groupSitePanel = null;
	
	private Integer rowsPerPage = 0;
	
	private Integer userId = null;
	
	/**
	 * @param id　コンポーネントID
	 * @param sites
	 * @param setUpSites
	 * @param groupSitePanel
	 */
	public SiteTabListAjaxLink(String id, Integer userId, IPageableListAction pageableAction, Integer rowsPerPage) {
		super(id);
		this.userId = userId;
		this.pageableAction = pageableAction;
		this.rowsPerPage = rowsPerPage;
	}
	
	public void setSetupSites(List<Site> setUpSites) {
		this.setUpSites = setUpSites;
	}
	
	public void setGroupSitePanel(GroupSitePanel groupSitePanel) {
		this.groupSitePanel = groupSitePanel;
	}
	
	/* (非 Javadoc)
	 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	public void onClick(AjaxRequestTarget target) {
		PageableListView sitseListView = new PageableListView("sites", pageableAction, rowsPerPage) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item) {
				Site site = (Site) item.getModelObject();
				boolean isSelect = false;
				boolean isAddButtonVisable = true;
				for(Site setUpSite : setUpSites) {
					if(site.getID() == setUpSite.getID()) {
						isSelect = true;
						isAddButtonVisable = false;
						break;
					}
				}
				SitePanel panel = null;
				if(site.getType().intValue() == Site.Type.office.ordinal()) {
					panel = new OfficeSitePanel("site", site, userId, isAddButtonVisable);
				} else {
					panel = new HatenaSitePanel("site", site, userId, isAddButtonVisable);
				}
				panel.setSelect(isSelect);
				panel.setOutputMarkupId(true);
				item.add(panel);

			}
		};
		sitseListView.setCurrentPage(0);
		sitseListView.setRowsPerPage(5);
		
		groupSitePanel.addOrReplace(sitseListView);
		target.addComponent(groupSitePanel);
		
		PagingNavigator pagingNavigator = new PagingNavigator("navigator", sitseListView, groupSitePanel);
		pagingNavigator.setOutputMarkupId(true);
		groupSitePanel.addOrReplace(pagingNavigator);
		target.addComponent(pagingNavigator);
        
		TabListAjaxLink.setSelectedLinkName(this.getId());
        Edit showPage = (Edit) this.getPage();
        List<TabListAjaxLink> tabListAjaxLinks = showPage.getTabListAjaxLinks();
        for(TabListAjaxLink tabAjaxLink : tabListAjaxLinks) {
        	target.addComponent(tabAjaxLink);
        }
        
	}
	
}
