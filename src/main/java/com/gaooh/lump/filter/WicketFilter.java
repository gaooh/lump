package com.gaooh.lump.filter;

import org.apache.wicket.application.ReloadingClassLoader;
import org.apache.wicket.protocol.http.ReloadingWicketFilter;

/**
 * クラス変更時に自動的にロードするFilterクラス
 * @author gaooh
 * @date 2008/05/04
 */
public class WicketFilter extends ReloadingWicketFilter {

	static {
		/* リロード対象とするクラスパターンを指定 */
		ReloadingClassLoader.includePattern("com.gaooh.lump.*");
	}
}
