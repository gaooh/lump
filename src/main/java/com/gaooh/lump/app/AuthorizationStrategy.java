package com.gaooh.lump.app;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;

import com.gaooh.lump.html.AuthenticatedWebPage;
import com.gaooh.lump.html.access.SignInPage;

public class AuthorizationStrategy implements IAuthorizationStrategy {

	public boolean isActionAuthorized(Component component, Action action) {
		return true;
	}

	public boolean isInstantiationAuthorized(Class componentClass) {
		if(AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
			if(((UserSession) Session.get()).isSignIn()) {
				return true;
			}
			throw new RestartResponseAtInterceptPageException(SignInPage.class);
		}
		return true;
	}

}
