package com.gaooh.lump.html;

import org.apache.wicket.behavior.HeaderContributor;

/**
 * サイトアーカイブ情報を見るときのレイアウト基底クラス
 * 
 * @author gaooh
 * @date 2008/08/23
 */
public class ArchiveWebPage extends AuthenticatedWebPage {

	public ArchiveWebPage() {
		add(HeaderContributor.forCss("/css/archive.css"));
	}
	
}
