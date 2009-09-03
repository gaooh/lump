package com.gaooh.lump.html.guideboard;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.gaooh.lump.entity.Comment;
import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.html.Home;
import com.gaooh.lump.html.guideboard.parts.SetupSitesListView;
import com.gaooh.lump.service.CommentService;
import com.gaooh.lump.service.GuideboardService;
import com.gaooh.lump.service.SiteService;
import com.google.inject.Inject;

/**
 * 削除確認ページ
 * 
 * @author gaooh
 * @date 2008/06/05
 */
public class Delete extends BasicWebPage {

	@Inject
	private GuideboardService guideboardService;
	
	@Inject
	private SiteService siteService;
	
	@Inject
	private CommentService commentService;
	
	/**　コメント一覧を表示するパネル　*/
	private GroupCommentPanel groupCommentPanel = null;
	
	/** 表示するガイドボード情報　*/
	private Guideboard guideboard = null;
	
	public Delete(PageParameters parameters) {
		super();
		guideboard = guideboardService.findById(parameters.getInt("id"));
		
		String guideboardId = String.valueOf(guideboard.getID());
		add(new Label("id", guideboardId));
		add(new Label("title", guideboard.getTitle()));
		add(new Label("description", guideboard.getDescription()));
		
		add(createSetupSitePanel());
		
		groupCommentPanel = new GroupCommentPanel("groupCommentPanel");
		groupCommentPanel.setOutputMarkupId(true);
		
		List<Comment> comments = commentService.findCommentsByGuideboardId(guideboard.getID());
		CommentsListView commentsListView = new CommentsListView("comments", comments);
		commentsListView.setOutputMarkupId(true);
		
		groupCommentPanel.add(commentsListView);
		add(groupCommentPanel);
		
		add(new DeleteInputForm("deleteForm", guideboard.getID()));
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
	 * Deleteページへのリンク
	 * @param string
	 * @param board
	 * @return
	 */
	public static BookmarkablePageLink link(final String title,final Guideboard guideboard) {
		final BookmarkablePageLink link = new BookmarkablePageLink(title, Delete.class);

		link.setParameter("id", guideboard.getID());
		link.add(new Label("title", new Model("削除")));

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
	 * 削除Form
	 * @author gaooh
	 * @date 2008/06/05
	 */
	private class DeleteInputForm extends Form {
		
		private static final long serialVersionUID = 1L;

		private HiddenField guideboardIdField = null;
		
		private DeleteGuideBoardSubmitButton submitButton = new DeleteGuideBoardSubmitButton("submitBtn");
		
		public DeleteInputForm(String id, Integer guideboardId) {
			super(id);
			guideboardIdField = new HiddenField("guideboardId", new Model(guideboardId));
			add(guideboardIdField);
			add(submitButton);
		}
	}
	
	/**
	 * 削除実行ボタン
	 * @author gaooh
	 * @date 2008/06/05
	 */
	private class DeleteGuideBoardSubmitButton extends Button {
	
		private static final long serialVersionUID = 1L;

		public DeleteGuideBoardSubmitButton(String id) {
			super(id);
		}
		
		@Override
		public void onSubmit() {
	        Component idField = this.getParent().get("guideboardId");
	        
	        guideboardService.delete(new Integer(idField.getModelObjectAsString()));
	        // HOMEリダイレクト
	        setResponsePage(Home.class);
	    }
	}
}
