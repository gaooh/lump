package com.gaooh.lump.html;

import javax.servlet.http.Cookie;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebRequest;

import com.gaooh.lump.app.UserSession;

/**
 * 認証済みページ基底クラス
 * ログイン後にのみ閲覧できるページはすべてこのクラスを継承する。
 * 
 * @author gaooh
 */
public class AuthenticatedWebPage extends WebPage {

	public AuthenticatedWebPage() {
	}
	
	/**
	 * セッションに格納されているユーザIDを取得する。
	 * @return
	 */
	protected Integer getUserId() {
		return ((UserSession)Session.get()).getUserId();
	}
	
	/**
	 * Cookieとして値を保存する
	 * @param name
	 * @param value
	 */
	protected void addCookie(String name, String value) {
		getWebRequestCycle().getWebResponse().addCookie(new Cookie(name, value));
	}
	
	/**
	 * 指定した名前で保存したCookieを取得する。
	 * もしなければ長さ0の文字列がかえる
	 * @param name
	 * @return
	 */
	protected String getCookieValue(String name) {
		Cookie[] cookies = ((WebRequest)getRequestCycle().getRequest()).getCookies();
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return "";
	}
}
