package com.gaooh.lump.html;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebPage;

/**
 * 非ログインページの基底ページクラス
 * 
 * @author gaooh
 * @date 2008/05/10
 */
public class UnAuthenticatedWebPage extends WebPage {

	public UnAuthenticatedWebPage() {
		add(HeaderContributor.forCss("/css/style.css"));
		add(HeaderContributor.forJavaScript("/js/hatena-bookmark-anywhere-0-1.js"));
	}
}
