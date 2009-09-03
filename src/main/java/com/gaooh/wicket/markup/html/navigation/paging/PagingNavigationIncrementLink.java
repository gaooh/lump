package com.gaooh.wicket.markup.html.navigation.paging;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 次、前ページナビゲート
 * 
 * @author gaooh
 * @date 2008/05/12
 */
public class PagingNavigationIncrementLink extends AjaxLink {

	private static final long serialVersionUID = 1L;

	private IPageable pageable;

	private int increment;

	private Panel panel;

	public PagingNavigationIncrementLink(String id, IPageable pageable,
			Panel panel, int increment) {
		super(id);
		this.pageable = pageable;
		this.panel = panel;
		this.increment = increment;
	}

	public final int getPageNumber() {
		int idx = pageable.getCurrentPage() + increment;
		return Math.max(0, Math.min(pageable.getPageCount() - 1, idx));
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		pageable.setCurrentPage(getPageNumber());
		target.addComponent(panel);
	}

	public boolean isFirst() {
		return pageable.getCurrentPage() <= 0;
	}

	public boolean isLast() {
		return pageable.getCurrentPage() >= (pageable.getPageCount() - 1);
	}

	public boolean linksTo(final Page page) {
		pageable.getCurrentPage();
		return ((increment < 0) && isFirst()) || ((increment > 0) && isLast());
	}
}
