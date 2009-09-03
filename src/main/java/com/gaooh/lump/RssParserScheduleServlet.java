package com.gaooh.lump;

import java.util.Timer;

import javax.servlet.http.HttpServlet;

/**
 * 外部RSSの定期解析処理をするServlet
 * @author gaooh
 * @date 2008/06/06
 */
public class RssParserScheduleServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static Timer parserTimer = new Timer();
	
	/* (非 Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() {
		ParserTask parserTask = new ParserTask();
		parserTimer.schedule(parserTask, 1 * 60 * 1000);
	}
	
	/* (非 Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		parserTimer.cancel();
	}
}
