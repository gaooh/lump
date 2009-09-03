package com.gaooh.lump.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.java.ao.EntityManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.service.OfficeService;
import com.gaooh.lump.service.SiteService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;
import de.nava.informa.parsers.FeedParser;

@Singleton
public class OfficeServiceImpl extends AbstractModule implements OfficeService {

	private static final String RSS_URL = "https://intra.office.drecom.jp/rss/latest";
	
	@Inject
	private SiteService siteService;
	
	private EntityManager manager;
	
	public OfficeServiceImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
	}
	
	public void updateBlogRss() {
		HttpClient client = new HttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("asami", "sabosabo1022");
		client.getState().setCredentials(AuthScope.ANY, credentials);
		
		GetMethod method = new GetMethod(RSS_URL);
		method.setDoAuthentication(true);
		
        ChannelIF channel = null;
        try {
        	client.executeMethod(method);
        	InputStream inputStream = new ByteArrayInputStream(method.getResponseBody());
            channel = FeedParser.parse(new ChannelBuilder(), inputStream);
            
            Collection<Item> items = channel.getItems();
            for(Item item: items) {
            	Site[] sites = manager.find(Site.class, "url = ? ", item.getLink());
            	if(sites.length == 0) {
            		Map<String, Object> param = new HashMap<String, Object>();
            		param.put("userId", 1);
            		param.put("url", item.getLink());
            		param.put("title", item.getTitle());
            		param.put("description", item.getDescription());
            		siteService.createByOffice(param);
            	}
            }
            
        } catch (Exception e) {
            System.out.println("Couldn't read " + RSS_URL);
            System.out.println(e);
        } finally {
            method.releaseConnection();
        }
	}

	public boolean isAuth(String officeId, String password) {
		HttpClient client = new HttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(officeId, password);
		client.getState().setCredentials(AuthScope.ANY, credentials);
		
		GetMethod method = new GetMethod(RSS_URL);
		method.setDoAuthentication(true);
		try {
			client.executeMethod(method);
			if(method.getStatusCode() == HttpServletResponse.SC_OK) {
				return true;
			}
		} catch (HttpException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	protected void configure() {
		bind( OfficeService.class ).to( OfficeServiceImpl.class ).in( Scopes.SINGLETON );
	}

}
