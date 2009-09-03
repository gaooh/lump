package com.gaooh.lump.html.access;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import com.gaooh.lump.Application;
import com.gaooh.lump.app.UserSession;
import com.gaooh.lump.entity.User;
import com.gaooh.lump.html.UnAuthenticatedWebPage;
import com.gaooh.lump.service.OfficeService;
import com.gaooh.lump.service.UserService;
import com.google.inject.Inject;

/**
 * サインインページ
 * 
 * @author gaooh
 * @date 2008/05/04
 */
public class SignInPage extends UnAuthenticatedWebPage {

	@Inject
	private OfficeService officeService;
	
	@Inject
	private UserService userService;
	
	public SignInPage() {
		add(new FeedbackPanel("feedback"));
		add(new InputForm());
	}
	
	/**
	 * ログイン情報入力From
	 * @author gaooh
	 * @date 2008/05/04
	 */
	private class InputForm extends Form {
		
		private static final long serialVersionUID = 1L;

		private TextField officeIdTextField = new TextField("officeId", new Model(""));	
		
		private PasswordTextField passwordTextField = new PasswordTextField("password", new Model(""));	
		
		public InputForm()  {
			super("form");
			add(officeIdTextField);
			add(passwordTextField);
		}
		
		/* (非 Javadoc)
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		protected void onSubmit() {	
			String officeId = officeIdTextField.getModelObjectAsString();
			String password = passwordTextField.getModelObjectAsString();
			
			if(officeService.isAuth(officeId, password)) { // 認証クリア
				User user = userService.findByOfficeId(officeId);
				if(user == null) {
					userService.createByOfficeId(officeId);
					user = userService.findByOfficeId(officeId);
				}
				((UserSession)Session.get()).setUserId(user.getID());
				
				boolean isReidrect = continueToOriginalDestination();
				if (!isReidrect) {
					setResponsePage(Application.get().getHomePage());
				}
			} else {
				error("ログインに失敗しました");
			}
			
		}
	}
}
