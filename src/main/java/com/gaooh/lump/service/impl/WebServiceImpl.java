package com.gaooh.lump.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.service.WebService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

/**
 * @author gaooh
 * @date 2008/08/23
 */
@Singleton
public class WebServiceImpl extends AbstractModule implements WebService {

	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.WebService#getHTMLStream(java.lang.String)
	 */
	public InputStream getHTMLStream(String url) {
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
	    client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
	    GetMethod get = new GetMethod(url.toString());
	    get.setFollowRedirects(true);
	    try {
			int iGetResultCode = client.executeMethod(get);
			if(iGetResultCode == 200) {
				return get.getResponseBodyAsStream();
			}
		    
		} catch (HttpException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		} catch (IOException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return null;
	}

	@Override
	protected void configure() {
		bind( WebService.class ).to( WebServiceImpl.class ).in( Scopes.SINGLETON );
	}

}
