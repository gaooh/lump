package com.gaooh.lump.html.guideboard.parts;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.gaooh.lump.html.guideboard.Edit;
import com.gaooh.lump.html.guideboard.GroupSitePanel;

public class RegistTabListAjaxLink extends TabListAjaxLink {

	private static final long serialVersionUID = 1L;
	
	private GroupSitePanel groupSitePanel = null;
	
	public RegistTabListAjaxLink(String id, GroupSitePanel groupSitePanel) {
		super(id);
		this.groupSitePanel = groupSitePanel;
	}
	
	@Override
	public void onClick(AjaxRequestTarget target) {
		TabListAjaxLink.setSelectedLinkName(this.getId());
		
		//TextField urlFld = new TextField("urlTextField", new Model(""));
		//urlFld.setOutputMarkupId(true);
		//groupSitePanel.addOrReplace(new RegistPanel("sites"));
		//target.addComponent(groupSitePanel);
		
		Edit editPage = (Edit) this.getPage();
		List<TabListAjaxLink> tabListAjaxLinks = editPage.getTabListAjaxLinks();
        for(TabListAjaxLink tabAjaxLink : tabListAjaxLinks) {
        	target.addComponent(tabAjaxLink);
        }
	}
	
	public class RegistPanel extends Panel {

		public RegistPanel(String id) {
			super(id);
			TextField urlFld = new TextField("urlTextField", new Model(""));
			urlFld.setOutputMarkupId(true);
			add(urlFld);
		}
		
	}
}
