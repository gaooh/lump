package com.gaooh.lump.html.site;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.ArchiveWebPage;
import com.gaooh.lump.service.SiteService;
import com.google.inject.Inject;

public class Archive extends ArchiveWebPage {

	@Inject
    private SiteService siteService;
	
	private Site site;
	
	public Archive(final PageParameters parameters) {
		Integer siteId = Integer.valueOf(parameters.getString("0"));
		site = siteService.findById(siteId);
		
		String siteUrl = "/site/Show/" + site.getID();
		add(new ExternalLink("siteUrl", siteUrl));
		
		//Label label = new Label("iframe", site.getHtml());
		//label.setEscapeModelStrings(false);
		//add(label);
		
		add(new WebMarkupContainer("iframe") {
			private static final long serialVersionUID = 1L;

			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				String url = "/site/archive_main?id=" + site.getID();
				tag.put("src", url);
			}
		});

	}
}
