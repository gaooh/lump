package com.gaooh.lump.service;

import com.gaooh.lump.service.impl.HatenaServiceImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(HatenaServiceImpl.class)
public interface HatenaService {

	public void updateHotEntryRss();
}
