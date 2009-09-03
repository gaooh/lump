package com.gaooh.wicket.markup.html.list;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;

/**
 * @author gaooh
 * @date 2008/06/26
 */
public abstract class LazyLoadingList<E> extends AbstractList<E> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int cacheStart = -1;
	private int cacheEnd = 0;
	private List<E> cache = null;

	@Override
	public E get(int index) {
		if (cache == null || index < cacheStart || cacheEnd < index) {
			cache = getPage(getCacheSize(), index/getCacheSize());
			cacheStart = index;
			cacheEnd = index + cache.size() - 1;
			System.out.println("RELOADED:" + cacheStart + "-" + cacheEnd);
		}
		return cache.get(index - cacheStart);
	}

	abstract protected List<E> getPage(int limit, int page);

	abstract protected int getCacheSize();

}
