package com.gaooh.lump.html;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.wicketstuff.scriptaculous.ScriptaculousAjaxBehavior;

import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.guideboard.Edit;
import com.gaooh.lump.service.GuideboardService;
import com.gaooh.lump.service.GuideboardStarService;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.SiteUserService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.gaooh.wicket.markup.html.StarImageListPanel;
import com.gaooh.wicket.markup.html.list.PageableListView;
import com.gaooh.wicket.markup.html.navigation.paging.IPageableListAction;
import com.gaooh.wicket.markup.html.navigation.paging.PagingNavigator;
import com.google.inject.Inject;

/**
 * サイトトップページ
 * 
 * @author gaooh
 * @date 2008/05/04
 */
public class Home extends BasicWebPage {

	private static final long serialVersionUID = 1L;

	private static final Map<Integer, Guideboard> idToGuidebord = new HashMap<Integer, Guideboard>();
	
	@Inject
	private GuideboardService guideboardService;

	@Inject
	private GuideboardStarService guideboardStarService;
	
	@Inject
	private SiteService siteService;
	
	/** トップガイドボードパネル */
	private GuideboardPanel topGuideboardPanel = null;
	
	/** ガイドボード一覧パネル */
	private GuideboardPanel guideboardPanel = null;
	
	public Home() {
		super();
		topGuideboardPanel = new  GuideboardPanel("topGuideboardPanel");
		List<Guideboard> guideboardOrderStar = guideboardService.findGuideboardOrderStar(5, 0);
		TopGuideBoardListView topGuideBoardListView = new TopGuideBoardListView("guideboards", guideboardOrderStar);
		topGuideboardPanel.add(topGuideBoardListView);
		add(topGuideboardPanel);
		
		guideboardPanel = new GuideboardPanel("guideboardPanel");
		guideboardPanel.setOutputMarkupId(true);
		GuideBoardListView guideBoardListView = new GuideBoardListView("guideboards", new GuideboardListAction(), 5);
		guideboardPanel.add(guideBoardListView);
		add(guideboardPanel);
		
		add(new PagingNavigator("navigator", guideBoardListView, guideboardPanel));
		
		List<Site> normalSites = siteService.findNormalSites(5, 0);
		SitesListView setupSitesListView = new SitesListView("sites", normalSites);
		add(setupSitesListView);
		
		// script.aculo.us を使うページには必要
		add(ScriptaculousAjaxBehavior.newJavascriptBindingBehavior());
	}

	public static Guideboard get(final int id) {
		return idToGuidebord.get(id);
	}
	
	/**
	 * ガイドボード一覧リスト
	 * @author gaooh
	 * @date 2008/05/06
	 */
	private class GuideBoardListView extends PageableListView {
		
		private static final long serialVersionUID = 1L;

		public GuideBoardListView(String id, IPageableListAction action, Integer rowsPerPage) {
			super(id, action, rowsPerPage);
		}
		
		protected void populateItem(ListItem item) {
			final Guideboard board = (Guideboard) item.getModelObject();
			String imageUrl = "https://intra.office.drecom.jp/" + board.getUser().getOfficeId() + "/file/profile"; 
			item.add(new ExternalImage("image", new Model(imageUrl)));
			//item.add(Show.link("showUrl", board));
			item.add(new Label("description", board.getDescription()));
			
			BookmarkablePageLink editLink = Edit.link("editUrl", board);
			//BookmarkablePageLink deleteLink = Delete.link("deleteUrl", board);
			if(board.getUserId().intValue() != getUserId()) { // 作成者が自分でないものの編集は不可
				editLink.setVisible(false);
				//deleteLink.setVisible(false);
			}
			item.add(editLink);
			//item.add(deleteLink);
			
			Integer starCount = guideboardStarService.getStarCount(board.getID());
			StarImageListPanel starImageListPanel = new StarImageListPanel("stars", starCount);
			item.add(starImageListPanel);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			item.add(new Label("createdAt", format.format(board.getCreatedAt())));
			item.add(new Label("updatedAt", format.format(board.getUpdatedAt())));
			
			Integer siteCount = siteService.countSiteByGuideboardId(board.getID());
			item.add(new Label("siteCount", String.valueOf(siteCount)));
			
			idToGuidebord.put(board.getID(), board);
		}
	}
	
	/**
	 * トップガイドボード一覧
	 * @author gaooh
	 * @date 2008/06/10
	 */
	private class TopGuideBoardListView extends ListView {

		private static final long serialVersionUID = 1L;

		public TopGuideBoardListView(String id, List list) {
			super(id, list);
		}
		
		@Override
		protected void populateItem(ListItem item) {
			final Guideboard board = (Guideboard) item.getModelObject();
			String imageUrl = "https://intra.office.drecom.jp/" + board.getUser().getOfficeId() + "/file/profile"; 
			item.add(new ExternalImage("image", new Model(imageUrl)));
			//item.add(Show.link("showUrl", board));
			item.add(new Label("description", board.getDescription()));
			
			BookmarkablePageLink editLink = Edit.link("editUrl", board);
			//BookmarkablePageLink deleteLink = Delete.link("deleteUrl", board);
			if(board.getUserId().intValue() != getUserId()) { // 作成者が自分でないものの編集は不可
				editLink.setVisible(false);
				//deleteLink.setVisible(false);
			}
			item.add(editLink);
			//item.add(deleteLink);
			
			Integer starCount = guideboardStarService.getStarCount(board.getID());
			StarImageListPanel starImageListPanel = new StarImageListPanel("stars", starCount);
			item.add(starImageListPanel);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			item.add(new Label("createdAt", format.format(board.getCreatedAt())));
			item.add(new Label("updatedAt", format.format(board.getUpdatedAt())));
			
			Integer siteCount = siteService.countSiteByGuideboardId(board.getID());
			item.add(new Label("siteCount", String.valueOf(siteCount)));
		}
		
	}
	/**
	 * サイトリスト一覧
	 * 
	 * @author gaooh
	 * @date 2008/05/11
	 */
	private class SitesListView extends ListView {

		private static final long serialVersionUID = 1L;

		@Inject
		private SiteUserService siteUserService;
		
		public SitesListView(String id, List list) {
			super(id, list);
		}

		@Override
		protected void populateItem(ListItem item) {
			Site site = (Site) item.getModelObject();
			
			String imageUrl = "";
			String[] urlSplits = site.getUrl().split("/");
			if(site.getType() == Site.Type.office.ordinal()) { // オフィスの場合の画像URL設定
				if(urlSplits.length >= 3) {
					imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/" + urlSplits[3] + "/file/profile";
				}
			} else {
				if(urlSplits.length >= 2) {
					imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/favicon.ico";
				}
			}
			String officeId = site.getUser().getOfficeId();
			item.add(new ExternalImage("image", new Model(imageUrl)));
			item.add(new ExternalLink("title", site.getUrl(), site.getTitle()));
			item.add(new ExternalLink("userName", "/user/" + officeId, "id:" + officeId));
			
			Integer count = siteUserService.countUser(site.getID());
			Integer commentCount = siteUserService.countComment(site.getID());
			
			String url = "/site/Show/" + site.getID();
			String linkValue = String.valueOf(count) + "users/" + String.valueOf(commentCount) + "comment";
			item.add(new ExternalLink("count", url, linkValue));
			
			item.setOutputMarkupId(true);
		}

	}

	/**
	 * ガイドボード一覧を取得処理するAction
	 * @author gaooh
	 * @date 2008/05/14
	 */
	private class GuideboardListAction implements IPageableListAction {

		public Integer count() {
			return guideboardService.findAll().size();
		}

		public List find(int limit, int pageNumber) {
			return guideboardService.findGuideboards(limit, pageNumber);
		}
		
	}
}
