package com.gaooh.lump.html.guideboard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.wicketstuff.scriptaculous.effect.Effect;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.guideboard.parts.SetupSitesListView;
import com.gaooh.lump.service.SiteService;
import com.gaooh.wicket.markup.html.ExternalImage;
import com.google.inject.Inject;

/**
 * サイト情報のパネル
 * 
 * @author gaooh
 * @date 2008/05/04
 */
public class SitePanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private SiteService siteService;
	
	/** サイト情報　*/
	private final Site site;
	
	/** ログインユーザ　*/
	private Integer userId = null;
	
	/** サイト情報が選択されているか　*/
	private boolean isSelect = false;
	
	/** 追加ボタンが見えているか　*/
	private boolean isAddGuideBoardVisabled = true;
	
	/** 削除ボタンが見えているか　*/
	private boolean isDeleteGuideBoardVisabled = true;
	
	public SitePanel(String id, Site site, Integer userId, boolean isAddGuideBoardVisabled) {
		super(id);
		this.site = site;
		this.userId = userId;
		
		setAddGuideBoardVisabled(isAddGuideBoardVisabled);
		//setDeleteGuideBoardVisabled(!isAddGuideBoardVisabled);//削除ボタンと追加ボタンは表裏一体
		
		Label idLabel = new Label("id", String.valueOf(site.getID()));
		
		String imageUrl = "";
		String[] urlSplits = site.getUrl().split("/");
		if(site.getType() == Site.Type.office.ordinal()) { // オフィスの場合の画像URL設定
			imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/" + urlSplits[3] + "/file/profile";
		} else {
			if(urlSplits.length >= 2) {
				imageUrl = urlSplits[0] + "//" + urlSplits[2] + "/favicon.ico";
			}
		}
		
		add(idLabel);
		add(new ExternalImage("image", new Model(imageUrl)));
		add(new ExternalLink("title", site.getUrl(), site.getTitle()));
		add(new Label("description", site.getDescription()));
		
		AddGuideBoardAjaxLink addGuideBoardAjaxLink = new AddGuideBoardAjaxLink("addGuideBoard");
		addGuideBoardAjaxLink.setVisible(isAddGuideBoardVisabled);
		add(addGuideBoardAjaxLink);
		
		//DeleteGuideBoardAjaxLink deleteGuideBoardAjaxLink = new DeleteGuideBoardAjaxLink("delteGuideBoard");
		//deleteGuideBoardAjaxLink.setVisible(!isAddGuideBoardVisabled);
		//add(deleteGuideBoardAjaxLink);
		
		super.setModel(idLabel.getModel());
		this.setOutputMarkupId(true);
	}
	
	/* (非 Javadoc)
	 * @see org.apache.wicket.markup.html.panel.Panel#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	public void onComponentTag(ComponentTag tag) {
		if (isSelect) {
			tag.put("class", "siteSelected");
		}
		super.onComponentTag(tag);
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public boolean isAddGuideBoardVisabled() {
		return isAddGuideBoardVisabled;
	}

	public void setAddGuideBoardVisabled(boolean isAddGuideBoardVisabled) {
		this.isAddGuideBoardVisabled = isAddGuideBoardVisabled;
	}

	public boolean isDeleteGuideBoardVisabled() {
		return isDeleteGuideBoardVisabled;
	}

	public void setDeleteGuideBoardVisabled(boolean isDeleteGuideBoardVisabled) {
		this.isDeleteGuideBoardVisabled = isDeleteGuideBoardVisabled;
	}
	
	/**
	 * ガイドボードへ追加するAjaxLink
	 * @author gaooh
	 * @date 2008/05/09
	 */
	private class AddGuideBoardAjaxLink extends AjaxLink {
		
		private static final long serialVersionUID = 1L;

		public AddGuideBoardAjaxLink(String id) {
			super(id);
		}
		
		/* (非 Javadoc)
		 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		public void onClick(AjaxRequestTarget target) {
			Edit editPage = (Edit) this.getPage();
			Integer guidebordId = editPage.getGuideboard().getID();
			Integer siteId = Integer.valueOf(site.getID());

			siteService.connetcGuideboard(guidebordId, siteId);
			setSelect(true);
			this.setVisible(false); // 追加ボタンは消す
			
			target.addComponent(this.getParent());
			
			/* -- 既存サイト情報を再描画　-- */
			final List<Site> setUpSites = siteService.findSitesByGuideboardId(guidebordId);
			SetupSitesListView setupSitesListView = new SetupSitesListView("sites", setUpSites, userId);
			
			SetupSitePanel setupSitePanel = editPage.getSetupSitePanel();
			setupSitePanel.addOrReplace(setupSitesListView);
			
	        target.addComponent(setupSitePanel);
	        target.appendJavascript(new Effect.Highlight(this.getParent()).toJavascript());
		}
	};
	
}
