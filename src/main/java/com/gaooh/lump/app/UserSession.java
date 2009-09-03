package com.gaooh.lump.app;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

/**
 * セッション情報クラス
 * @author gaooh
 * @date 2008/05/04
 */
public class UserSession extends WebSession {

	private static final long serialVersionUID = 1L;

	/** ユーザID */
	private Integer userId;
	
	public UserSession(Request request) {
		super(request);
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * ログインしているかどうか
	 * @return　ログインしていればtrueがかえる
	 */
	public boolean isSignIn() {
		return userId != null;
	}
}
