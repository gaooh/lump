package com.gaooh.wicket.markup.html.navigation.paging;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.wicketstuff.scriptaculous.effect.Effect;

/**
 * @author gaooh
 * @date 2008/05/12
 */
public class PagingNavigationLink extends AjaxLink {

	private static final long serialVersionUID = 1L;

	private IPageable pageable;
	
	private int pageNumber;
	
	private Panel panel;
	
	public PagingNavigationLink(String id, IPageable pageable, Panel panel, int pageNumber) {
		super(id);
		this.pageable = pageable;
		this.panel = panel;
		this.pageNumber = pageNumber;
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		pageable.setCurrentPage(pageNumber);
		target.addComponent(panel);
		target.appendJavascript(new Effect.Highlight(panel).toJavascript());
	}
}
