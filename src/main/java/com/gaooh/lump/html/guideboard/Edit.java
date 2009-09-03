package com.gaooh.lump.html.guideboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.io.IOUtils;
import org.wicketstuff.scriptaculous.ScriptaculousAjaxBehavior;
import org.wicketstuff.scriptaculous.effect.Effect;

import com.gaooh.lump.app.UserSession;
import com.gaooh.lump.entity.Comment;
import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.html.guideboard.parts.RegistTabListAjaxLink;
import com.gaooh.lump.html.guideboard.parts.SetupSitesListView;
import com.gaooh.lump.html.guideboard.parts.SiteTabListAjaxLink;
import com.gaooh.lump.html.guideboard.parts.TabListAjaxLink;
import com.gaooh.lump.service.CommentService;
import com.gaooh.lump.service.GuideboardService;
import com.gaooh.lump.service.GuideboardStarService;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.WebService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.gaooh.wicket.markup.html.StarImageListPanel;
import com.gaooh.wicket.markup.html.list.PageableListView;
import com.gaooh.wicket.markup.html.navigation.paging.IPageableListAction;
import com.gaooh.wicket.markup.html.navigation.paging.PagingNavigator;
import com.google.inject.Inject;

/**
 * 個別ガイドボード編集ページ
 * 
 * @author gaooh
 * @date 2008/05/04
 */
public class Edit extends BasicWebPage {

	@Inject
	private GuideboardService guideboardService;
	
	@Inject
	private GuideboardStarService guideboardStarService;
	
	@Inject
	private SiteService siteService;
	
	/**　登録済みのサイト一覧を表示するパネル　*/
	private SetupSitePanel setupSitePanel = null;
	
	/** リストを種別ごとに表示するタブのリンクリスト　*/
	private List<TabListAjaxLink> tabListAjaxLinks = new ArrayList<TabListAjaxLink>();
	
	private StarImageListPanel starImageListPanel = null;
	
	/** 表示するガイドボード情報　*/
	private Guideboard guideboard = null;
	
	public Edit(final PageParameters parameters) {
		super();
		
		guideboard = guideboardService.findById(parameters.getInt("id"));
		add(new Label("id", String.valueOf(guideboard.getID())));
		
		setModel(new Model("tabpanel"));
		
		List<AbstractTab> tabs = new ArrayList<AbstractTab>();
		tabs.add(new AbstractTab(new Model("一覧")) {

			private static final long serialVersionUID = 1L;

			public Panel getPanel(String panelId) {
                return new GuideboardTabPanel(panelId);
            }

        });
		
		tabs.add(new AbstractTab(new Model("サイトを登録する")) {

			private static final long serialVersionUID = 1L;

			public Panel getPanel(String panelId) {
				return new RegistTabPanel(panelId);
            }

        });

        tabs.add(new AbstractTab(new Model("コメント")) {

        	private static final long serialVersionUID = 1L;
        	
            public Panel getPanel(String panelId) {
                return new CommentTabPanel(panelId);
            }

        });

        add(new TabbedPanel("tabs", tabs).add(new AttributeModifier("class", true, Edit.this.getModel())));
        
		
		add(ScriptaculousAjaxBehavior.newJavascriptBindingBehavior());
	}

	public static BookmarkablePageLink link(final String title,final Guideboard guideboard) {

		final BookmarkablePageLink link = new BookmarkablePageLink(title, Edit.class);

		link.setParameter("id", guideboard.getID());
		link.add(new Label("title", guideboard.getTitle()));

		return link;
	}

	public SetupSitePanel getSetupSitePanel() {
		return setupSitePanel;
	}

	public void setSetupSitePanel(SetupSitePanel setupSitePanel) {
		this.setupSitePanel = setupSitePanel;
	}

	public List<TabListAjaxLink> getTabListAjaxLinks() {
		return tabListAjaxLinks;
	}

	public void setTabListAjaxLinks(List<TabListAjaxLink> tabListAjaxLinks) {
		this.tabListAjaxLinks = tabListAjaxLinks;
	}

	public Guideboard getGuideboard() {
		return guideboard;
	}

	public void setGuideboard(Guideboard guideboard) {
		this.guideboard = guideboard;
	}
	
	public StarImageListPanel getStarImageListPanel() {
		return starImageListPanel;
	}

	public void setStarImageListPanel(StarImageListPanel starImageListPanel) {
		this.starImageListPanel = starImageListPanel;
	}

	/**
	 * サイト一覧リスト
	 * @author gaooh
	 * @date 2008/05/06
	 */
	private class SitesListView extends PageableListView {
		
		private static final long serialVersionUID = 1L;

		private List<Site> setUpSites;
		
		public SitesListView(String id, IPageableListAction action, Integer rowsPerPage, List<Site> setUpSites) {
			super(id, action, rowsPerPage);
			this.setUpSites = setUpSites;
		}
		
		protected void populateItem(ListItem item) {
			Site site = (Site) item.getModelObject();
			boolean isSelect = false;
			for(Site setUpSite : setUpSites) {
				if(site.getID() == setUpSite.getID()) {
					isSelect = true;
					break;
				}
			}
			SitePanel panel = new SitePanel("site", site, getUserId(), true);
			panel.setSelect(isSelect);
			item.add(panel);
		}
	}
	
	/**
	 * 通常サイトリスト取得Action
	 * @author gaooh
	 * @date 2008/05/14
	 */
	private class NormalSitesListAction implements IPageableListAction {

		public Integer count() {
			return siteService.countNormalSites();
		}

		public List find(int limit, int pageNumber) {
			return siteService.findNormalSites(limit, pageNumber);
		}
	}
	
	/**
	 * オフィスサイトリスト取得Action
	 * @author gaooh
	 * @date 2008/05/14
	 */
	private class OfficeSitesListAction implements IPageableListAction {

		public Integer count() {
			return siteService.countOfficeSites();
		}

		public List find(int limit, int pageNumber) {
			return siteService.findOfficeSites(limit, pageNumber);
		}
	}
	
	/**
	 * はてぶサイトリスト取得Action
	 * @author gaooh
	 * @date 2008/05/14
	 */
	private class HatenaSitesListAction implements IPageableListAction {

		public Integer count() {
			return siteService.countHatenaSites();
		}

		public List find(int limit, int pageNumber) {
			return siteService.findHatenaSites(limit, pageNumber);
		}
	}

	private class GuideboardTabPanel extends Panel {
		
		public GuideboardTabPanel(String id) {
			super(id);
			
			String guideboardId = String.valueOf(guideboard.getID());
			//add(new Label("id", guideboardId));
			add(new Label("title", guideboard.getTitle()));
			add(new Label("description", guideboard.getDescription()));

			String rssUrl = "/guideboard/rss.xml?id=" + guideboardId;
			add(new ExternalLink("rssUrl", rssUrl));
			
			String imageUrl = "https://intra.office.drecom.jp/" + guideboard.getUser().getOfficeId() + "/file/profile"; 
			add(new ExternalImage("image", new Model(imageUrl)));
		
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			add(new Label("createdAt", format.format(guideboard.getCreatedAt())));
			add(new Label("updatedAt", format.format(guideboard.getUpdatedAt())));
			
			Integer starCount = guideboardStarService.getStarCount(guideboard.getID());
			starImageListPanel = new StarImageListPanel("stars", starCount);
			starImageListPanel.setOutputMarkupId(true);
			add(starImageListPanel);
	        
	        add(new AjaxFallbackLink("addStar") {
				
				private static final long serialVersionUID = 1L;

				public void onClick(AjaxRequestTarget target) {
					Integer star = guideboardStarService.addStar(guideboard.getID(), getUserId());
					starImageListPanel.resetStarCount(star);
					target.addComponent(starImageListPanel);
	            }
	        });

			Integer siteCount = siteService.countSiteByGuideboardId(guideboard.getID());
			add(new Label("siteCount", String.valueOf(siteCount)));
			
			setupSitePanel = new SetupSitePanel("setupSites");
			setupSitePanel.setOutputMarkupId(true);
			
			final List<Site> setUpSites = siteService.findSitesByGuideboardId(guideboard.getID());
			SetupSitesListView setupSitesListView = new SetupSitesListView("sites", setUpSites, getUserId());
			
			setupSitePanel.add(setupSitesListView);
			add(setupSitePanel);
			
			final GroupSitePanel groupSitePanel = new GroupSitePanel("groupSites");
			groupSitePanel.setOutputMarkupId(true);
			
			SitesListView sitseListView = new SitesListView("sites", new NormalSitesListAction(), 5, setUpSites);
			PagingNavigator pagingNavigator = new PagingNavigator("navigator", sitseListView, groupSitePanel);
			pagingNavigator.setOutputMarkupId(true);
			groupSitePanel.add(pagingNavigator);
			groupSitePanel.add(sitseListView);
			add(groupSitePanel);
			
			SiteTabListAjaxLink lumpListAjaxLink = new SiteTabListAjaxLink("lumpLink", getUserId(), new NormalSitesListAction(), 5);
			lumpListAjaxLink.setSetupSites(setUpSites);
			lumpListAjaxLink.setGroupSitePanel(groupSitePanel);
			tabListAjaxLinks.add(lumpListAjaxLink);
			add(lumpListAjaxLink);
			
			/* -- オフィス一覧コンポーネント　-- */
			Integer limit = Integer.valueOf((String) new ResourceModel("site.office.limit").getObject());
			SiteTabListAjaxLink officeListAjaxLink = new SiteTabListAjaxLink("officeLink", getUserId(), new OfficeSitesListAction(), 5);
			officeListAjaxLink.setSetupSites(setUpSites);
			officeListAjaxLink.setGroupSitePanel(groupSitePanel);
			tabListAjaxLinks.add(officeListAjaxLink);
			add(officeListAjaxLink);
			
			/* -- はてぶ一覧コンポーネント　-- */
			SiteTabListAjaxLink hatenaListAjaxLink = new SiteTabListAjaxLink("hatenaLink", getUserId(), new HatenaSitesListAction(), 5);
			hatenaListAjaxLink.setSetupSites(setUpSites);
			hatenaListAjaxLink.setGroupSitePanel(groupSitePanel);
			tabListAjaxLinks.add(hatenaListAjaxLink);
			add(hatenaListAjaxLink);
		}
	}
	
	private class RegistTabPanel extends Panel {
		
		public RegistTabPanel(String id) {
			super(id);
			add( new FeedbackPanel("feedback"));
			add( new SiteCreateForm("createForm"));
			add(ScriptaculousAjaxBehavior.newJavascriptBindingBehavior());
			
		}
		
		private class SiteCreateForm extends Form {
			
			public SiteCreateForm(String id) {
				super(id);
				
				TextField urlFld = new TextField("urlTextField", new Model(""));
				add(urlFld);
				
				TextField textFld = new TextField("titleTextField", new Model(""));
				add(textFld);
				
				TextArea textArea = new TextArea("descriptionTextField", new Model(""));
				add(textArea);
				
				TextArea commentArea = new TextArea("commentTextField", new Model(""));
				add(commentArea);
				
				add(new SiteCreateSubmitButton("submitBtn"));
			}
			
		}
		/**
		 * サイト情報更新ボタンコンポーネント
		 * @author gaooh
		 * @date 2008/05/07
		 */
		private class SiteCreateSubmitButton extends Button {
			
			private static final long serialVersionUID = 1L;
			
			@Inject
		    private WebService webService;
			
			public SiteCreateSubmitButton(String id) {
				super(id);
			}
			
			@Override
			public void onSubmit() {
				Component urlField = this.getParent().get( "urlTextField");
		        Component titleField = this.getParent().get( "titleTextField");
		        Component descriptionField = this.getParent().get("descriptionTextField");
		        Component commentField = this.getParent().get("commentTextField");
		        
		        Site site = siteService.findByUrl(urlField.getModelObjectAsString().trim());
		        if(site == null) {
		        	site = siteService.create();
		        	site.setUserId(getUserId());
			        site.setUrl(urlField.getModelObjectAsString().trim());
			        site.setTitle(titleField.getModelObjectAsString());
			        site.setDescription(descriptionField.getModelObjectAsString());
			        
			        InputStream stream = webService.getHTMLStream(site.getUrl());
			        if(stream != null) {
			        	InputStreamReader reader = new InputStreamReader(stream);
			        	StringBuffer stringBuffer = new StringBuffer();
			        	int contents = 0;
			        	try {
							while ((contents = reader.read()) != -1) {
								stringBuffer.append((char)contents);
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							IOUtils.closeQuietly(reader);
							IOUtils.closeQuietly(stream);
						}
			        	site.setHtml(stringBuffer.toString());
			        }
			    }
		        siteService.save(site, commentField.getModelObjectAsString());
		        
		        siteService.connetcGuideboard(guideboard.getID(), site.getID());
				
		        final RequestCycle cycle = getRequestCycle();
	            PageParameters parameters = new PageParameters();
	            parameters.put("id", guideboard.getID());
	            cycle.setResponsePage(getPageFactory().newPage(Edit.class, parameters));
	            cycle.setRedirect(true);
		    }
		}
	}
	
	private class CommentTabPanel extends Panel {
		
		@Inject
		private CommentService commentService;
		
		/**　コメント一覧を表示するパネル　*/
		private GroupCommentPanel groupCommentPanel = null;
		
		public CommentTabPanel(String id) {
			super(id);
			groupCommentPanel = new GroupCommentPanel("groupCommentPanel");
			groupCommentPanel.setOutputMarkupId(true);
			
			List<Comment> comments = commentService.findCommentsByGuideboardId(guideboard.getID());
			CommentsListView commentsListView = new CommentsListView("comments", comments);
			commentsListView.setOutputMarkupId(true);
			
			groupCommentPanel.add(commentsListView);
			add(groupCommentPanel);
			
			add(new FeedbackPanel("feedback"));
			add(new CommentInputForm("form"));
			add(ScriptaculousAjaxBehavior.newJavascriptBindingBehavior());
		}
		
		/**
		 * コメント入力From
		 * @author gaooh
		 * @date 2008/05/04
		 */
		private class CommentInputForm extends Form {

			private static final long serialVersionUID = 1L;
			
			/** コメント入力フォーム　*/
			private TextArea commentTextArea = new TextArea("comment", new Model(""));	
			
			public CommentInputForm(String id) {
				super(id);
				add(commentTextArea);
				
				add(new IndicatingAjaxButton("submit", this) {
					
					private static final long serialVersionUID = 1L;

					/* (非 Javadoc)
					 * @see org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton#onSubmit(org.apache.wicket.ajax.AjaxRequestTarget, org.apache.wicket.markup.html.form.Form)
					 */
					protected void onSubmit(AjaxRequestTarget target, Form form) {
						Edit edit = (Edit) this.getPage();
						String comment = commentTextArea.getModelObjectAsString();
						
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("comment", comment);
						param.put("guideboardId", edit.getGuideboard().getID());
						param.put("userId", ((UserSession)Session.get()).getUserId());
						
						commentService.addComment(param);
						
						/* -- コメント一覧を再描画　-- */
						List<Comment> comments = commentService.findCommentsByGuideboardId(guideboard.getID());
						CommentsListView commentsListView = new CommentsListView("comments", comments);
						
						groupCommentPanel.addOrReplace(commentsListView);
						target.addComponent(groupCommentPanel);
						target.appendJavascript(new Effect.Highlight(groupCommentPanel).toJavascript());
		            }
		        });
			}
		}
		
		/**
		 * コメントリスト
		 * @author gaooh
		 * @date 2008/05/06
		 */
		private class CommentsListView extends ListView {
		
			private static final long serialVersionUID = 1L;
			
			/** コメントのインデックス　*/
			private Integer index = 1;
			
			public CommentsListView(String id, List list) {
				super(id, list);
			}
			
			/* (非 Javadoc)
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			protected void populateItem(ListItem item) {
				Comment comment = (Comment) item.getModelObject();
				
				CommentPanel panel = new CommentPanel("comment", comment, index++);
				panel.setOutputMarkupId(true);
				item.add(panel);

			}
		}
	}
}
