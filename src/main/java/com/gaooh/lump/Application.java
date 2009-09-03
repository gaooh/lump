package com.gaooh.lump;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedParamUrlCodingStrategy;
import org.apache.wicket.util.lang.PackageName;

import com.gaooh.lump.app.AuthorizationStrategy;
import com.gaooh.lump.app.UserSession;
import com.gaooh.lump.html.Home;


/**
 * アプリケーション基底クラス
 * 
 * @author gaooh
 * @date 2008/05/04
 */
public class Application extends WebApplication {
	
	/* (非 Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	protected void init() {
		super.init();
		addComponentInstantiationListener(new GuiceComponentInjector(this));
		mount("/guideboard", PackageName.forPackage(com.gaooh.lump.html.guideboard.New.class.getPackage()));
		mount("/site", PackageName.forPackage(com.gaooh.lump.html.site.New.class.getPackage()));
		mount("/portal", PackageName.forPackage(com.gaooh.lump.html.portal.Show.class.getPackage()));
		mount(new IndexedParamUrlCodingStrategy("/site/Show", com.gaooh.lump.html.site.Show.class));
		mount(new IndexedParamUrlCodingStrategy("/site/Archive", com.gaooh.lump.html.site.Archive.class));
		mount(new IndexedParamUrlCodingStrategy("/user", com.gaooh.lump.html.user.Show.class));
		
		//getApplicationSettings().setInternalErrorPage(ErrorPage.class);
		//getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		getSecuritySettings().setAuthorizationStrategy(new AuthorizationStrategy());
		
	}
	
	/* (非 Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.Request, org.apache.wicket.Response)
	 */
	public UserSession newSession(Request request, Response response) {
		return new UserSession(request);
	}
	
	@Override
	public Class<? extends WebPage> getHomePage() {
		return Home.class;
	}

	
}
