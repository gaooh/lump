package com.gaooh.lump.html;

import org.apache.wicket.behavior.HeaderContributor;


/**
 * ブックマークレットページの基底クラス
 * 
 * @author gaooh
 * @date 2008/06/24
 */
public class BookmarkletWebPage extends AuthenticatedWebPage {

	public BookmarkletWebPage() {
		add(HeaderContributor.forCss("/css/bookmarklet.css"));
	}
	
}
