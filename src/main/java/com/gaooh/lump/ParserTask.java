package com.gaooh.lump;

import java.util.TimerTask;

import com.gaooh.lump.service.HatenaService;
import com.gaooh.lump.service.OfficeService;
import com.gaooh.lump.service.impl.HatenaServiceImpl;
import com.gaooh.lump.service.impl.OfficeServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * 解析処理実行
 * 
 * @author gaooh
 * @date 2008/06/06
 */
public class ParserTask extends TimerTask {

	@Inject
    private HatenaService hatenaService;
	
	@Inject
    private OfficeService officeService;
    
	@Override
	public void run() {
		Injector injector = Guice.createInjector(new HatenaServiceImpl());
		hatenaService = injector.getInstance(HatenaService.class);
		hatenaService.updateHotEntryRss();
		
		injector = Guice.createInjector(new OfficeServiceImpl());
		officeService = injector.getInstance(OfficeService.class);
		officeService.updateBlogRss();
	}

}
