package com.gaooh.lump.html.portal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.entity.User;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.html.GuideboardPanel;
import com.gaooh.lump.html.comon.UserSitePanel;
import com.gaooh.lump.html.guideboard.Edit;
import com.gaooh.lump.service.GuideboardService;
import com.gaooh.lump.service.GuideboardStarService;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.UserService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.gaooh.wicket.markup.html.StarImageListPanel;
import com.gaooh.wicket.markup.html.list.PageableListView;
import com.gaooh.wicket.markup.html.navigation.paging.IPageableListAction;
import com.gaooh.wicket.markup.html.navigation.paging.PagingNavigator;
import com.google.inject.Inject;

/**
 * 自分の登録情報を表示する
 * 
 * @author gaooh
 * @date 2008/06/29
 */
public class Show extends BasicWebPage {

	@Inject
	private UserService userService;
	
	public Show() {
		final User user = userService.findById(super.getUserId());
		add(new Label("userName", user.getOfficeId()));
		
		setModel(new Model("tabpanel"));

		List<AbstractTab> tabs = new ArrayList<AbstractTab>();
        tabs.add(new AbstractTab(new Model("コメントしたサイト一覧")) {

			private static final long serialVersionUID = 1L;

			public Panel getPanel(String panelId) {
                return new CommentTabPanel(panelId, user);
            }

        });

        tabs.add(new AbstractTab(new Model("作成したガイドボード一覧")) {

        	private static final long serialVersionUID = 1L;
        	
            public Panel getPanel(String panelId) {
                return new GuideboardTabPanel(panelId, getUserId());
            }

        });

        add(new TabbedPanel("tabs", tabs).add(new AttributeModifier("class", true, Show.this.getModel())));
        
	}
	
	private static class CommentTabPanel extends Panel {

		private static final long serialVersionUID = 1L;

		/** コメントしたサイト一覧　*/
		private UserSitePanel userSitePanel;
		
        public CommentTabPanel(String id, User user) {
            super(id);
            userSitePanel = new UserSitePanel("userSitePanel", user.getID(), 10);
    		userSitePanel.setOutputMarkupId(true);
    		add(new PagingNavigator("userSiteNavigator", userSitePanel.getSitesListView(), userSitePanel));
    		add(userSitePanel);
        }

    };
    
    private static class GuideboardTabPanel extends Panel {
    	
		private static final long serialVersionUID = 1L;

		/** ガイドボード一覧パネル */
    	private GuideboardPanel guideboardPanel = null;
    	
    	@Inject
    	private GuideboardService guideboardService;
    	
    	@Inject
    	private GuideboardStarService guideboardStarService;
    	
    	@Inject
    	private SiteService siteService;
    	
        public GuideboardTabPanel(String id, Integer userId) {
            super(id);
            guideboardPanel = new GuideboardPanel("guideboardPanel");
    		guideboardPanel.setOutputMarkupId(true);
    		GuideBoardListView guideBoardListView = new GuideBoardListView("guideboards", userId, new GuideboardListAction(), 10);
    		guideboardPanel.add(guideBoardListView);
    		add(guideboardPanel);
    		
    		add(new PagingNavigator("guideboardNavigator", guideBoardListView, guideboardPanel));
        }
        
        /**
    	 * ガイドボード一覧リスト
    	 * @author gaooh
    	 * @date 2008/05/06
    	 */
    	private class GuideBoardListView extends PageableListView {
    		
    		private static final long serialVersionUID = 1L;

    		private Integer userId = null;
    		
    		public GuideBoardListView(String id, Integer userId, IPageableListAction action, Integer rowsPerPage) {
    			super(id, action, rowsPerPage);
    			this.userId = userId;
    		}
    		
    		protected void populateItem(ListItem item) {
    			Guideboard board = (Guideboard) item.getModelObject();
    			String imageUrl = "https://intra.office.drecom.jp/" + board.getUser().getOfficeId() + "/file/profile"; 
    			item.add(new ExternalImage("image", new Model(imageUrl)));
    			//item.add(com.gaooh.lump.html.guideboard.Show.link("showUrl", board));
    			item.add(new Label("description", board.getDescription()));
    			
    			BookmarkablePageLink editLink = Edit.link("editUrl", board);
    			//BookmarkablePageLink deleteLink = Delete.link("deleteUrl", board);
    			if(board.getUserId().intValue() != userId) { // 作成者が自分でないものの編集は不可
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

    };
	
}
