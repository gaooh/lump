package com.gaooh.lump.service.impl;

import org.junit.Test;

import com.gaooh.lump.service.HatenaService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class HatenaServiceImplTest {

	@Inject
    private HatenaService hatenaService;
	
	@Test
	public void はてなホットエントリーを取得しパースする() throws Exception {
		Injector injector = Guice.createInjector(new HatenaServiceImpl());
		hatenaService = injector.getInstance(HatenaService.class);
        
		hatenaService.updateHotEntryRss();
		
	}
}
