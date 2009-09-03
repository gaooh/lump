package com.gaooh.lump.html;

import org.apache.wicket.behavior.HeaderContributor;

/**
 * ログイン済み通常のページが継承するクラス
 * @author gaooh
 * @date 2008/06/24
 */
public class BasicWebPage extends AuthenticatedWebPage {

	public BasicWebPage() {
		add(HeaderContributor.forCss("/css/style.css"));
		add(HeaderContributor.forJavaScript("/js/hatena-bookmark-anywhere-0-1.js"));
	}
}
