package com.gaooh.lump.html.site;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.wicketstuff.scriptaculous.ScriptaculousAjaxBehavior;
import org.wicketstuff.scriptaculous.effect.Effect;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.SiteUserService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.gaooh.wicket.markup.html.HatenaBookmarkPanel;
import com.google.inject.Inject;

/**
 * サイト情報を閲覧する
 * 
 * @author gaooh
 * @date 2008/05/11
 */
public class Show extends BasicWebPage {

	@Inject
    private SiteService siteService;
	
	@Inject
    private SiteUserService siteUserService;
	
	private Site site;
	
	private UserSitePanel userSitePanel;
	
	public Show(final PageParameters parameters) {
		super();
		Integer siteId = Integer.valueOf(parameters.getString("0"));
		site = siteService.findById(siteId);
		
		add(new Label("title", site.getTitle()));
		add(new Label("description", site.getDescription()));
		add(new ExternalLink("url", site.getUrl(), site.getUrl()));
		String archiveUrl = "/site/Archive/" + site.getID();
		add(new ExternalLink("archive", archiveUrl, archiveUrl));
		add(new HatenaBookmarkPanel("hatenaPanel", site.getUrl(), 5, null, null));
		
		String thumbnailUrl = "http://img.simpleapi.net/small/" + site.getUrl();
		if(site.getUrl().startsWith("https://intra.office.drecom.jp/")) {
			thumbnailUrl = "/image";
		}
		
		add(new ExternalImage("thumbnail",  new Model(thumbnailUrl)));
		userSitePanel = new UserSitePanel("userSitePanel", siteId);
		userSitePanel.setOutputMarkupId(true);
		add(userSitePanel);
		
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
					Show show = (Show) this.getPage();
					String comment = commentTextArea.getModelObjectAsString();
					
					siteUserService.addComment(site.getID(), getUserId(), comment);
					userSitePanel.reflashSiteComment();
					target.addComponent(userSitePanel);
					target.appendJavascript(new Effect.Highlight(userSitePanel).toJavascript());
	            }
	        });
		}
	}
}
