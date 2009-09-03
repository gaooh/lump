package com.gaooh.lump.html.guideboard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.guideboard.parts.SetupSitesListView;
import com.gaooh.lump.service.SiteService;
import com.google.inject.Inject;

/**
 * 登録済みのシンプルなサイト情報パネル
 * @author gaooh
 * @date 2008/08/22
 */
public class SiteSimplePanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SiteService siteService;
	
	/** サイト情報　*/
	private final Site site;
	
	private Integer userId = null;
	
	public SiteSimplePanel(String id, Site site, Integer userId, Integer index) {
		super(id);
		this.site = site;
		this.userId = userId;
		
		Label idLabel = new Label("id", String.valueOf(site.getID()));
		
		add(idLabel);
		String url = "/site/Show/" + site.getID();
		add(new ExternalLink("title", url, site.getTitle()));
		add(new Label("indexNumber", String.valueOf(index)));
		add(new ExternalLink("siteUrl", site.getUrl(), site.getUrl()));
		
		DeleteGuideBoardAjaxLink deleteGuideBoardAjaxLink = new DeleteGuideBoardAjaxLink("delete");
		if(userId.intValue() != site.getUserId().intValue()) { // 追加者のみ削除できる
			deleteGuideBoardAjaxLink.setVisible(false);
		}
		add(deleteGuideBoardAjaxLink);
		
		super.setModel(idLabel.getModel());
		this.setOutputMarkupId(true);
	}
	
	/**
	 * ガイドボードから削除するAjaxLink
	 * @author gaooh
	 * @date 2008/05/09
	 */
	private class DeleteGuideBoardAjaxLink extends AjaxLink {
		
		private static final long serialVersionUID = 1L;

		public DeleteGuideBoardAjaxLink(String id) {
			super(id);
		}
		
		/* (非 Javadoc)
		 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		public void onClick(AjaxRequestTarget target) {
			Edit editPage = (Edit) this.getPage();
			Integer guidebordId = editPage.getGuideboard().getID();
			Integer siteId = Integer.valueOf(site.getID());

			siteService.unConnectGuideboard(guidebordId, siteId);;
			
			/* -- 既存サイト情報を再描画　-- */
			final List<Site> setUpSites = siteService.findSitesByGuideboardId(guidebordId);
			SetupSitesListView setupSitesListView = new SetupSitesListView("sites", setUpSites, userId);
			
			SetupSitePanel setupSitePanel = editPage.getSetupSitePanel();
			setupSitePanel.addOrReplace(setupSitesListView);
			
	        target.addComponent(setupSitePanel);
		}
	};
}
