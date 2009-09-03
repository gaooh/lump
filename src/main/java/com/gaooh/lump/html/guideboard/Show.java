package com.gaooh.lump.html.guideboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.wicketstuff.scriptaculous.ScriptaculousAjaxBehavior;
import org.wicketstuff.scriptaculous.effect.Effect;

import com.gaooh.lump.app.UserSession;
import com.gaooh.lump.entity.Comment;
import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.html.guideboard.parts.SetupSitesListView;
import com.gaooh.lump.service.CommentService;
import com.gaooh.lump.service.GuideboardService;
import com.gaooh.lump.service.GuideboardStarService;
import com.gaooh.lump.service.SiteService;
import com.google.inject.Inject;

/**
 * 個別ガイドボード表示ページ
 * 
 * @author gaooh
 * @date 2008/05/06
 */
public class Show extends BasicWebPage {

	@Inject
	private GuideboardService guideboardService;
	
	@Inject
	private GuideboardStarService guideboardStarService;
	
	@Inject
	private SiteService siteService;
	
	@Inject
	private CommentService commentService;
	
	/**　コメント一覧を表示するパネル　*/
	private GroupCommentPanel groupCommentPanel = null;
	
	/** 表示するガイドボード情報　*/
	private Guideboard guideboard = null;
	
	/** ☆数を表示するラベル */
	private Label starLable = null;
	
	public Show(PageParameters parameters) {
		super();
		guideboard = guideboardService.findById(parameters.getInt("id"));
		
		String guideboardId = String.valueOf(guideboard.getID());
		add(new Label("id", guideboardId));
		add(new Label("title", guideboard.getTitle()));
		add(new Label("description", guideboard.getDescription()));
		
		Integer starCount = guideboardStarService.getStarCount(guideboard.getID());
		starLable = new Label("star", String.valueOf(starCount));
		starLable.setOutputMarkupId(true);
		add(starLable);
		
		add(new AjaxFallbackLink("addStar") {
			
			private static final long serialVersionUID = 1L;

			public void onClick(AjaxRequestTarget target) {
            	Integer addStar = guideboardStarService.addStar(guideboard.getID(), getUserId());
            	starLable = new Label("star", String.valueOf(addStar));
            	starLable.setOutputMarkupId(true);
            	this.getPage().addOrReplace(starLable);
				target.addComponent(starLable);
            }
        });
		
		add(createSetupSitePanel());
		
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
	 * サイト情報一覧パネルを生成する追加する
	 */
	protected SetupSitePanel createSetupSitePanel() {
		SetupSitePanel setupSitePanel = new SetupSitePanel("setupSites");
		setupSitePanel.setOutputMarkupId(true);
		
		final List<Site> setUpSites = siteService.findSitesByGuideboardId(guideboard.getID());
		SetupSitesListView setupSitesListView = new SetupSitesListView("sites", setUpSites, getUserId());
		
		setupSitePanel.add(setupSitesListView);
		return setupSitePanel;
	}
	
	/**
	 * Showページへのリンク
	 * @param string
	 * @param board
	 * @return
	 */
	public static Component link(String title, Guideboard guideboard) {
		final BookmarkablePageLink link = new BookmarkablePageLink(title, Show.class);

		link.setParameter("id", guideboard.getID());
		link.add(new Label("title", new Model(guideboard.getTitle())));

		return link;
	}

	public Guideboard getGuideboard() {
		return guideboard;
	}

	public void setGuideboard(Guideboard guideboard) {
		this.guideboard = guideboard;
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
					Show show = (Show) this.getPage();
					String comment = commentTextArea.getModelObjectAsString();
					
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("comment", comment);
					param.put("guideboardId", show.getGuideboard().getID());
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
	
}


