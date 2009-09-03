package com.gaooh.lump;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.impl.SiteServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * アーカイブ情報を出力するServlet
 * 
 * @author gaooh
 * @date 2008/08/24
 */
public class ArchiveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private SiteService siteService;
	
	/* (非 Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() {
		Injector injector = Guice.createInjector(new SiteServiceImpl());
		siteService = injector.getInstance(SiteService.class);
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
		response.setContentType("text/html; charset=UTF-8");

		Integer siteId = Integer.valueOf(request.getParameter("id"));
		Site site = siteService.findById(siteId);
		
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(site.getHtml());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
		}
		
	}
}
