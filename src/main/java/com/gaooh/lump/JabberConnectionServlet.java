package com.gaooh.lump;

import javax.servlet.http.HttpServlet;

import com.gaooh.lump.service.JabberService;
import com.gaooh.lump.service.impl.JabberServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Jabberへ接続しログイン状態を保持するServlet
 * @author gaooh
 * @date 2008/06/24
 */
public class JabberConnectionServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
    private JabberService jabberService;
	
	/* (非 Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() {
		Injector injector = Guice.createInjector(new JabberServiceImpl());
		jabberService = injector.getInstance(JabberService.class);
		
		jabberService.connection("im.drecom.co.jp", "nivocy", "6Zx4UjtI");
	}
	
	/* (非 Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		jabberService.disConnection();
	}
}
