package com.gaooh.lump.service.impl;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gaooh.lump.service.JabberService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;


/**
 * @author gaooh
 * @date 2008/06/22
 */
public class JabberServiceImplTest {

	@Inject
    private static JabberService jabberService;
	
	/**
	 * テスト前に実行する1度だけ呼ばれるメソッド
	 * @throws Exception
	 */
	@BeforeClass
    public static void beforeClass() throws Exception {
		Injector injector = Guice.createInjector(new JabberServiceImpl());
		jabberService = injector.getInstance(JabberService.class);
    }
	
	
	@Test
	public void カンファレンスルームにメッセージを送る() throws Exception {
		jabberService.connection("im.drecom.co.jp", "nivocy", "6Zx4UjtI");
		jabberService.sendMessageForRoom("gaooh-test@conference.im.drecom.co.jp", "てすと");
		jabberService.sendMessageForRoom("gaooh-test2@conference.im.drecom.co.jp", "てすと");
		jabberService.disConnection();
	}
}
