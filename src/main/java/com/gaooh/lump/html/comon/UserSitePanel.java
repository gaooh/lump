package com.gaooh.lump.html.comon;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.service.SiteUserService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.gaooh.wicket.markup.html.list.PageableListView;
import com.gaooh.wicket.markup.html.navigation.paging.IPageableListAction;
import com.google.inject.Inject;

/**
 * ユーザが登録したサイト情報を載せるパネル
 * @author gaooh
 * @date 2008/06/26
 */
public class UserSitePanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private SiteUserService siteUserService;
	
	private SitesListView sitesListView;
	
	public UserSitePanel(String id, Integer userId, Integer count) {
		super(id);
		sitesListView = new SitesListView("sites", new SiteListAction(userId), count);
		add(sitesListView);
	}

	public SitesListView getSitesListView() {
		return sitesListView;
	}

	public void setSitesListView(SitesListView setupSitesListView) {
		this.sitesListView = setupSitesListView;
	}
	
	/**
	 * サイト一覧リスト
	 * @author gaooh
	 * @date 2008/05/06
	 */
	private class SitesListView extends PageableListView {
		
		private static final long serialVersionUID = 1L;

		public SitesListView(String id, IPageableListAction action, Integer rowsPerPage) {
			super(id, action, rowsPerPage);
		}
		
		protected void populateItem(ListItem item) {
			Site site = (Site) item.getModelObject();
			
			String imageUrl = "";
			String[] urlSplits = site.getUrl().split("/");
			if(site.getType() == Site.Type.office.ordinal()) { // オフィスの場合の画像URL設定
				imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/" + urlSplits[3] + "/file/profile";
			} else {
				imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/favicon.ico";
			}
			
			item.add(new ExternalImage("image", new Model(imageUrl)));
			item.add(new ExternalLink("title", site.getUrl(), site.getTitle()));
			item.add(new Label("description", site.getDescription()));
			
			Integer count = siteUserService.countUser(site.getID());
			Integer commentCount = siteUserService.countComment(site.getID());
			
			String url = "/site/Show/" + site.getID();
			String linkValue = String.valueOf(count) + "users/" + String.valueOf(commentCount) + "comment";
			item.add(new ExternalLink("count", url, linkValue));
		}
	}
	
	/**
	 * 自分が登録したサイト一覧を取得処理するAction
	 * @author gaooh
	 * @date 2008/05/14
	 */
	private class SiteListAction implements IPageableListAction {

		private Integer userId = null;
		
		public SiteListAction(Integer userId) {
			this.userId = userId;
		}
		
		public Integer count() {
			return siteUserService.countSitesByUserId(this.userId);
		}

		public List find(int limit, int pageNumber) {
			return siteUserService.findSitesByUserId(this.userId, limit, pageNumber);
		}
		
	}
}
