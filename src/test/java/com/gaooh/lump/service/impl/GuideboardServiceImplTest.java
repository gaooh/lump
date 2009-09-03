package com.gaooh.lump.service.impl;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gaooh.lump.service.GuideboardService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * GuideboardServiceImplのテスト
 * 
 * @author gaooh
 * @date 2008/05/06
 */
public class GuideboardServiceImplTest {

	@Inject
    private static GuideboardService guideboardService;
	
	/**
	 * テスト前に実行する1度だけ呼ばれるメソッド
	 * @throws Exception
	 */
	@BeforeClass
    public static void beforeClass() throws Exception {
		Injector injector = Guice.createInjector(new GuideboardServiceImpl());
		guideboardService = injector.getInstance(GuideboardService.class);
    }
	
	@Test
	public void 主キーでガイドボードを取得する() throws Exception {
		Assert.assertNotNull(guideboardService.findById(1));
	}
}
