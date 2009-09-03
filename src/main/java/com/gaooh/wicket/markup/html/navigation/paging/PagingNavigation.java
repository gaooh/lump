package com.gaooh.wicket.markup.html.navigation.paging;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * ページ番号リストを表示する
 * 
 * @author gaooh
 * @date 2008/05/13
 */
public class PagingNavigation extends org.apache.wicket.markup.html.navigation.paging.PagingNavigation {
	
	private static final long serialVersionUID = 1L;
	
	private Panel panel;
	
	public PagingNavigation(final String id, final IPageable pageable, Panel panel){
		super(id, pageable);
		this.panel = panel;
	}
	
	@Override
	protected void populateItem(LoopItem loopItem) {
		// Get the index of page this link shall point to
		final int pageIndex = getStartIndex() + loopItem.getIteration();

		// Add a page link pointing to the page
		final AjaxLink link = new PagingNavigationLink("pageLink", pageable, panel, pageIndex);
		loopItem.add(link);

		// Add a page number label to the list which is enclosed by the link
		String label =  String.valueOf(pageIndex + 1);
		
		link.add(new Label("pageNumber", label));
	}

}
