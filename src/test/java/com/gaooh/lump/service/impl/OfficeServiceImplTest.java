package com.gaooh.lump.service.impl;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gaooh.lump.service.OfficeService;
import com.gaooh.lump.service.impl.OfficeServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * OfficeServiceImplのテスト
 * @author gaooh
 * @date 2008/05/05
 */
public class OfficeServiceImplTest {

	@Inject
    private static OfficeService officeService;
	
	/**
	 * テスト前に実行する1度だけ呼ばれるメソッド
	 * @throws Exception
	 */
	@BeforeClass
    public static void beforeClass() throws Exception {
		Injector injector = Guice.createInjector(new OfficeServiceImpl());
		officeService = injector.getInstance(OfficeService.class);
    }
	
	@Test
	public void オフィスRSSを取得しパースする() throws Exception {
		officeService.updateBlogRss();
	}
	
	@Test
	public void オフィス認証で成功する() {
		Assert.assertTrue(officeService.isAuth("asami", "sabosabo1022"));
	}
	
	@Test
	public void オフィス認証で失敗する() {
		Assert.assertFalse(officeService.isAuth("asami", "errorPasswor"));
	}
}
