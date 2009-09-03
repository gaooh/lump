package com.gaooh.lump.html.guideboard.parts;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;

public abstract class TabListAjaxLink extends AjaxLink {

	private static final long serialVersionUID = 1L;

	/** 選択済みのリンク名　*/
	private static String selectedLinkName = "regist";
	
	public TabListAjaxLink(String id) {
		super(id);
	}

	/* (非 Javadoc)
	 * @see org.apache.wicket.ajax.markup.html.AjaxLink#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	public void onComponentTag(ComponentTag tag) {
		if(this.getId().equals(selectedLinkName)) {
			tag.put("class", "active");
		}
		super.onComponentTag(tag);
	}
	
	protected static String getSelectedLinkName() {
		return selectedLinkName;
	}

	protected static void setSelectedLinkName(String selectedLinkName) {
		TabListAjaxLink.selectedLinkName = selectedLinkName;
	}
}
