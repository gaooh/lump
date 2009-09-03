package com.gaooh.lump.html.site;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.gaooh.lump.entity.UserSite;
import com.gaooh.lump.service.SiteUserService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.google.inject.Inject;

/**
 * ユーザのコメント一覧を載せるパネル
 * @author gaooh
 * @date 2008/05/20
 */
public class UserSitePanel extends Panel {

	private static final long serialVersionUID = 1L;

	@Inject
    private SiteUserService siteUserService;
	
	private UserSiteListView commentsListView;
	
	private Integer siteId;
	
	public UserSitePanel(String id, Integer siteId) {
		super(id);
		this.siteId = siteId;
		reflashSiteComment();
	}

	/**
	 * コメントリスト
	 * @author gaooh
	 * @date 2008/05/06
	 */
	private class UserSiteListView extends ListView {
	
		private static final long serialVersionUID = 1L;
		
		public UserSiteListView(String id, List list) {
			super(id, list);
		}
		
		/* (非 Javadoc)
		 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
		 */
		protected void populateItem(ListItem item) {
			UserSite userSite = (UserSite) item.getModelObject();
			
			String officeId = userSite.getUser().getOfficeId();
			String imageUrl = "https://intra.office.drecom.jp/" + officeId + "/file/profile"; 
			item.add(new ExternalImage("image", new Model(imageUrl)));
			item.add(new ExternalLink("userName", "/lump/user/" + officeId, "id:" + officeId));
			item.add(new Label("comment", userSite.getComment()));
		}
	}

	/**
	 * サイト情報を設定する
	 */
	public void reflashSiteComment() {
		List<UserSite> userSites = siteUserService.findSiteUsersBySiteId(siteId);
		commentsListView = new UserSiteListView("comments", userSites);
		addOrReplace(commentsListView);
	}
}
