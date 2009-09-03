package com.gaooh.wicket.markup.html.navigation.paging;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * ナビゲーションを表示するパネル
 * 
 * @author gaooh
 * @date 2008/05/11
 */
public class PagingNavigator extends Panel {

	private static final long serialVersionUID = 1L;
	
	/** The pageable component that needs to be updated. */
	private IPageable pageable;

	private final PagingNavigation pagingNavigation;
	
	private Panel panel = null;
	
	public PagingNavigator(final String id, final IPageable pageable, Panel panel) {
		super(id);
		this.pageable = pageable;
		this.panel = panel;
		
		this.pagingNavigation = newNavigation(pageable);
		add(pagingNavigation);
		
		//add(newPagingNavigationLink("first", pageable, 0));
		add(newPagingNavigationIncrementLink("prev", pageable, -1));
		add(newPagingNavigationIncrementLink("next", pageable, 1));
		//add(newPagingNavigationLink("last", pageable, 1));
	}

	protected AjaxLink newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
		return new PagingNavigationLink(id, pageable, panel, pageNumber);
	}
	
	protected AjaxLink newPagingNavigationIncrementLink(String id, IPageable pageable, int increment) {
		return new PagingNavigationIncrementLink(id, pageable, panel, increment);
	}
	
	protected PagingNavigation newNavigation(final IPageable pageable) {
		return new PagingNavigation("navigation", pageable, panel);
	}

}
