package com.gaooh.wicket.markup.html.navigation.paging;

import java.util.List;

/**
 * ページ管理するリスト処理interface
 * @author gaooh
 * @date 2008/05/14
 */
public interface IPageableListAction {

	/**
	 * limit、pageNumberにあわせたfind処理を実装する
	 * @param limit
	 * @param pageNumber
	 * @return
	 */
	public List find(int limit, int pageNumber);
	
	/**
	 * 取得全体数のカウント処理を実装する
	 * @return
	 */
	public Integer count();
}
