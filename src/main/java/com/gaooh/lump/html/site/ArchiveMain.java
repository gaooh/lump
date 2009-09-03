package com.gaooh.lump.html.site;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.AuthenticatedWebPage;
import com.gaooh.lump.service.SiteService;
import com.google.inject.Inject;

public class ArchiveMain extends AuthenticatedWebPage {
	
	@Inject
    private SiteService siteService;
	
	private Site site;
	
	
	public ArchiveMain(final PageParameters parameters) {
		Integer siteId = Integer.valueOf(parameters.getString("0"));
		site = siteService.findById(siteId);
		
		add(new WebMarkupContainer("html") {

			private static final long serialVersionUID = 1L;
			
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("src", "http://wicketframework.org/");
			}
		});
		
	}
	

}
