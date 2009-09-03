package com.gaooh.lump.service;

import java.io.InputStream;

import com.gaooh.lump.service.impl.WebServiceImpl;
import com.google.inject.ImplementedBy;

/**
 * @author gaooh
 * @date 2008/08/23
 */
@ImplementedBy(WebServiceImpl.class)
public interface WebService {
	
	/**
	 * 指定したURLのHTMLを取得するStreamを取得する
	 * @param url
	 */
	public InputStream getHTMLStream(String url);

}
