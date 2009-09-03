package com.gaooh.lump.html.site;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.SiteUserService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.gaooh.wicket.markup.html.list.LazyLoadingList;
import com.google.inject.Inject;

/**
 * サイト情報一覧を表示する
 * 
 * @author gaooh
 * @date 2008/06/26
 */
public class List extends BasicWebPage {

	@Inject
	private SiteUserService siteUserService;

	@Inject
	private SiteService siteService;
	
	public List() {
		SitesLazyLoadingList siteList = new SitesLazyLoadingList(10);
		SitesListView listView = new SitesListView("sites", siteList, 10);
		add(listView);
		add(new PagingNavigator("navigator", listView));
	}

	private class SitesLazyLoadingList extends LazyLoadingList<Site> {

		private static final long serialVersionUID = 1L;
		
		private Integer rowPerPage = null;
		
		public SitesLazyLoadingList(Integer rowPerPage) {
			this.rowPerPage = rowPerPage;
		}
		@Override
		public int size() {
			return siteService.countNormalSites();
		}

		@Override
		protected int getCacheSize() {
			return rowPerPage;
		}

		@Override
		protected java.util.List<Site> getPage(int limit, int page) {
			return siteService.findNormalSites(limit, page);
		}

	}

	private class SitesListView extends PageableListView {

		private static final long serialVersionUID = 1L;

		public SitesListView(String id, java.util.List<Site> list, Integer rowsPerPage) {
			super(id, list, rowsPerPage);
		}

		protected void populateItem(ListItem item) {
			Site site = (Site) item.getModelObject();

			String imageUrl = "";
			String[] urlSplits = site.getUrl().split("/");
			if (site.getType() == Site.Type.office.ordinal()) { // オフィスの場合の画像URL設定
				imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/" + urlSplits[3] + "/file/profile";
			} else {
				if(urlSplits.length > 2) {
					imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/favicon.ico";
				}
			}

			item.add(new ExternalImage("image", new Model(imageUrl)));
			item.add(new ExternalLink("title", site.getUrl(), site.getTitle()));
			item.add(new Label("description", site.getDescription()));

			Integer count = siteUserService.countUser(site.getID());
			Integer commentCount = siteUserService.countComment(site.getID());

			String url = "/site/Show/" + site.getID();
			String linkValue = String.valueOf(count) + "users/"
					+ String.valueOf(commentCount) + "comment";
			item.add(new ExternalLink("count", url, linkValue));
		}
	}
}
