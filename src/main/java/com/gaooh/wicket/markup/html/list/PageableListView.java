package com.gaooh.wicket.markup.html.list;

import java.util.List;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

import com.gaooh.wicket.markup.html.navigation.paging.IPageableListAction;

/**
 * Ajaxを使って画面遷移せずにページリスト処理をするためのListView
 * @author gaooh
 * @date 2008/05/14
 */
public abstract class PageableListView extends ListView implements IPageable {

	private static final long serialVersionUID = 1L;

	private final IPageableListAction iPageableListAction;
	
	/** The page to show. */
	private int currentPage;

	/** Number of rows per page of the list view. */
	private int rowsPerPage;
	
	/**
	 * @param id
	 * @param action 検索処理をするIPageableListActionを実装したクラス
	 * @param rowsPerPage 取得上限数
	 */
	public PageableListView(String id, IPageableListAction action, Integer rowsPerPage) {
		super(id, action.find(rowsPerPage, 0));
		iPageableListAction = action;
		this.rowsPerPage = rowsPerPage;
	}

	/* (非 Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getCurrentPage()
	 */
	public final int getCurrentPage() {
		while ((currentPage * rowsPerPage) > iPageableListAction.count()){
			currentPage--;
		}

		return currentPage;
	}

	/* (非 Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getPageCount()
	 */
	public final int getPageCount() {
		return ((iPageableListAction.count() + rowsPerPage) - 1) / rowsPerPage;
	}

	/**
	 * 1ページで表示する件数
	 * @return
	 */
	public final int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	
	/* (非 Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#setCurrentPage(int)
	 */
	public final void setCurrentPage(int currentPage) {
		if (currentPage < 0) {
			currentPage = 0;
		}

		int pageCount = getPageCount();
		if ((currentPage > 0) && (currentPage >= pageCount)) {
			currentPage = pageCount - 1;
		}
		
		this.currentPage = currentPage;
		List select = iPageableListAction.find(rowsPerPage, this.currentPage);
		super.setList(select);
	}
	
}
