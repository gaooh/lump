package com.gaooh.lump;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.service.GuideboardService;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.impl.GuideboardServiceImpl;
import com.gaooh.lump.service.impl.SiteServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import de.nava.informa.core.ChannelExporterIF;
import de.nava.informa.core.ChannelFormat;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.exporters.RSS_2_0_Exporter;
import de.nava.informa.impl.basic.ChannelBuilder;

/**
 * ガイドボードのRSSを出力する
 * 
 * @author gaooh
 * @date 2008/08/23
 */
public class GuideBoardRssServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private SiteService siteService;

	@Inject
	private GuideboardService guideboardService;

	/* (非 Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() {
		Injector injector = Guice.createInjector(new SiteServiceImpl());
		siteService = injector.getInstance(SiteService.class);
		
		injector = Guice.createInjector(new GuideboardServiceImpl());
		guideboardService = injector.getInstance(GuideboardService.class);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.exec(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.exec(request, response);
	}

	public void exec(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/xml; charset=UTF-8");
		
		Integer guideboardId = Integer.valueOf(request.getParameter("id"));
		Guideboard guideboard = guideboardService.findById(guideboardId);
		List<Site> sites = siteService.findSitesByGuideboardId(guideboardId);

		ChannelBuilder builder = new ChannelBuilder();
		ChannelIF newChannel = builder.createChannel("LAMP - " + guideboard.getTitle());

		newChannel.setFormat(ChannelFormat.RSS_2_0);
		newChannel.setLanguage("ja");
		newChannel.setCopyright("Project-G");
		newChannel.setLastUpdated(guideboard.getUpdatedAt());
		
		try {

			for (Site site : sites) {
				ItemIF item = builder.createItem(newChannel, site.getTitle(),
						site.getDescription(), new URL(site.getUrl()));
				item.setDate(site.getCreatedAt());
				newChannel.addItem(item);
			}
			
			ChannelExporterIF writer = new RSS_2_0_Exporter(response.getWriter(), "UTF-8");
			writer.write(newChannel);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
