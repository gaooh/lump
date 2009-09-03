package com.gaooh.lump.html.user;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import com.gaooh.lump.entity.User;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.html.comon.UserSitePanel;
import com.gaooh.lump.service.UserService;
import com.gaooh.wicket.markup.html.navigation.paging.PagingNavigator;
import com.google.inject.Inject;

/**
 * ユーザの登録したサイト一覧とかを表示するページ
 * 
 * @author gaooh
 * @date 2008/05/11
 */
public class Show extends BasicWebPage {

	@Inject
	private UserService userService;
	
	private UserSitePanel userSitePanel;
	
	public Show(final PageParameters parameters) {
		String officeId = parameters.getString("0");
		User user = userService.findByOfficeId(officeId);
		
		if(user != null) {
			add(new Label("userName", user.getOfficeId()));
			
			userSitePanel = new UserSitePanel("userSitePanel", user.getID(), 10);
			userSitePanel.setOutputMarkupId(true);
			add(new PagingNavigator("navigator", userSitePanel.getSitesListView(), userSitePanel));
			add(userSitePanel);
		}
	}
	
}
