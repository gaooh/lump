package com.gaooh.lump.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.java.ao.EntityManager;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.service.HatenaService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;
import de.nava.informa.parsers.FeedParser;

public class HatenaServiceImpl extends AbstractModule implements HatenaService {

	private static final String RSS_URL = "http://b.hatena.ne.jp/hotentry?mode=rss";
	
	private EntityManager manager;
	
	public HatenaServiceImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
	}
	
	public void updateHotEntryRss() {
		ChannelIF channel = null;
        try {
            channel = FeedParser.parse(new ChannelBuilder(), RSS_URL);
            
            Collection<Item> items = channel.getItems();
            for(Item item: items) {
            	Site[] sites = manager.find(Site.class, "url = ? ", item.getLink());
            	if(sites.length == 0) {
            		System.out.println(item.getDescription());
            		Map<String, Object> param = new HashMap<String, Object>();
            		param.put("userId", 1);
            		param.put("url", item.getLink());
            		param.put("title", item.getTitle());
            		param.put("description", item.getDescription());
            		param.put("type", Site.Type.hatena);
            		manager.create(Site.class, param);
            	}
            }
            
        } catch (Exception e) {
            System.out.println("Couldn't read " + RSS_URL);
            System.out.println(e);
        } 
	}
	
	@Override
	protected void configure() {
		bind( HatenaService.class ).to( HatenaServiceImpl.class ).in( Scopes.SINGLETON );
	}

}
