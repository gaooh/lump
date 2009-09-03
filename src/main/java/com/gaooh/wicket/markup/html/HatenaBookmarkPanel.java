package com.gaooh.wicket.markup.html;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;

/**
 * はてなブックマークコメントを表示するパネル
 * 
 * @author gaooh
 * @date 2008/06/19
 */
public class HatenaBookmarkPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static ResourceReference JAVASCRIPT = new JavascriptResourceReference(
			HatenaBookmarkPanel.class, "hatena-bookmark-anywhere-0-1.js");

	/** ブックマークのURL */
	private String targetUrl = null;

	/** 表示最大数　*/
	private Integer limit = null;
	
	/** スタイルをCSSで指定するか　*/
	private Boolean useStyle = null;
	
	/** コメントの書いてないブクマを表示するか　*/ 
	private Boolean isCollapse = null;
	
	public HatenaBookmarkPanel(String id, String url, final Integer limit, Boolean useStyle, Boolean isCollapse) {
		super(id);
		this.targetUrl = url;
		this.limit = limit;
		this.useStyle = useStyle;
		this.isCollapse = isCollapse;
		
		add(new WebMarkupContainer((getContentId())));
		add(new HatenaBookmarkBehavior());
		add(HeaderContributor.forJavaScript(JAVASCRIPT));

	}

	public String getContentId() {
		return "content";
	}
	
	class HatenaBookmarkBehavior extends AbstractBehavior {
		
		private static final long serialVersionUID = 1L;
		
		public void renderHead(IHeaderResponse response) {
			super.renderHead(response);
			StringBuffer buffer = new StringBuffer();
			if(targetUrl != null) {
				buffer.append("var hatena_bookmark_anywhere_url = '" + targetUrl + "';");
			}
			if(limit != null) {
				buffer.append("var hatena_bookmark_anywhere_limit = " + limit + ";");
			}
			if(useStyle != null) {
				buffer.append("var hatena_bookmark_anywhere_style = " + useStyle + ";");
			}
			if(isCollapse != null) {
				buffer.append("var hatena_bookmark_anywhere_collapse = " + isCollapse + ";");
			}
			
			response.renderJavascript(buffer.toString(), null);
		}
	}
}
