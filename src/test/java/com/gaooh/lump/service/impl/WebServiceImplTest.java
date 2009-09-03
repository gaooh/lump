package com.gaooh.lump.service.impl;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaooh.lump.service.WebService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class WebServiceImplTest {

	@Inject
    private static WebService webService;
	
	
	/**
	 * テスト前に実行する1度だけ呼ばれるメソッド
	 * @throws Exception
	 */
	@BeforeClass
    public static void beforeClass() throws Exception {
		Injector injector = Guice.createInjector(new WebServiceImpl());
		webService = injector.getInstance(WebService.class);
    }
	
	@Test
	public void 指定URLのサイトストリームを取得する() throws Exception {
		InputStream stream = webService.getHTMLStream("http://www.yahoo.jp/");
		Assert.assertNotNull(stream);
	}
	
	
}
