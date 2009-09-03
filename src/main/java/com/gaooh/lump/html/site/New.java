package com.gaooh.lump.html.site;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.IOUtils;
import org.wicketstuff.scriptaculous.ScriptaculousAjaxBehavior;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.html.BookmarkletWebPage;
import com.gaooh.lump.service.JabberService;
import com.gaooh.lump.service.SiteService;
import com.gaooh.lump.service.WebService;
import com.google.inject.Inject;

/**
 * サイト情報の新規登録
 * @author gaooh
 * @date 2008/05/06
 */
public class New extends BookmarkletWebPage {

	@Inject
    private SiteService siteService;
	
	@Inject
    private JabberService jabberService;
	
	@Inject
    private WebService webService;
	
	public New(final PageParameters parameters) {
		super();
		add( new FeedbackPanel("feedback"));
		add( new SiteCreateForm("createForm", parameters));
		add(ScriptaculousAjaxBehavior.newJavascriptBindingBehavior());
	}
	
	/**
	 * サイト情報を新規作成するフォーム
	 * @author gaooh
	 * @date 2008/05/07
	 */
	private class SiteCreateForm extends Form {
		
		private static final long serialVersionUID = 1L;

		private TextField urlFld = null;
		
		private TextField textFld = null;
		
		private TextArea textArea = null;
		
		private CheckBox jabberCheck = new CheckBox("jabberCheckBox");
		
		private TextField jabberFld = new TextField("jabberTextField", new Model(""));
		
		private TextArea commentArea = new TextArea("commentTextField", new Model(""));
		
		public SiteCreateForm(String id, final PageParameters parameters) {
			super(id);
			String url = parameters.getString("url");
			String title = parameters.getString("title");
			String description = parameters.getString("description");
			
			// cookieの値を設定する
			String jabberSendValue = getCookieValue("jabber-send");
			jabberFld.setModel(new Model(jabberSendValue));
			
			String jabberCheckValue = getCookieValue("jabber-checkbox");
			jabberCheck.setModel(new Model(Boolean.valueOf(jabberCheckValue)));
			
			try {
				if(StringUtils.isNotBlank(url)) {
					url = URLDecoder.decode(parameters.getString("url"), "UTF-8");
				}
				if(StringUtils.isNotBlank(title)) {
					title = URLDecoder.decode(parameters.getString("title"), "UTF-8");
				}
				if(StringUtils.isNotBlank(description)) {
					description = URLDecoder.decode(description, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				throw new ApplicationException("e001");
			}
			
			urlFld = new TextField("urlTextField", new Model(url));
			add(urlFld);
			
			textFld = new TextField("titleTextField", new Model(title));
			add(textFld);
			
			textArea = new TextArea("descriptionTextField", new Model(description));
			add(textArea);
			
			add(jabberFld); 
			add(jabberCheck);
			add(commentArea);
			
			add(new SiteCreateSubmitButton("submitBtn"));
		}
	}
	
	/**
	 * サイト情報更新ボタンコンポーネント
	 * @author gaooh
	 * @date 2008/05/07
	 */
	private class SiteCreateSubmitButton extends Button {
		
		private static final long serialVersionUID = 1L;
		
		public SiteCreateSubmitButton(String id) {
			super(id);
		}
		
		@Override
		public void onSubmit() {
			Component urlField = this.getParent().get( "urlTextField");
	        Component titleField = this.getParent().get( "titleTextField");
	        Component descriptionField = this.getParent().get("descriptionTextField");
	        Component commentField = this.getParent().get("commentTextField");
	        Component jabberCheckBox = this.getParent().get("jabberCheckBox");
	        Component jabberField = this.getParent().get("jabberTextField");
	        
	        Site site = siteService.findByUrl(urlField.getModelObjectAsString().trim());
	        if(site == null) {
	        	site = siteService.create();
	        	site.setUserId(getUserId());
		        site.setUrl(urlField.getModelObjectAsString().trim());
		        site.setTitle(titleField.getModelObjectAsString());
		        site.setDescription(descriptionField.getModelObjectAsString());
		        
		        InputStream stream = webService.getHTMLStream(site.getUrl());
		        if(stream != null) {
		        	InputStreamReader reader = new InputStreamReader(stream);
		        	StringBuffer stringBuffer = new StringBuffer();
		        	int contents = 0;
		        	try {
						while ((contents = reader.read()) != -1) {
							stringBuffer.append((char)contents);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						IOUtils.closeQuietly(reader);
						IOUtils.closeQuietly(stream);
					}
		        	site.setHtml(stringBuffer.toString());
		        }
		    }
	        siteService.save(site, commentField.getModelObjectAsString());
	        
	        Boolean jabberCheckBoxValue = Boolean.valueOf(jabberCheckBox.getModelObjectAsString());
	        if(jabberCheckBoxValue) {
	        	StringBuilder builer = new StringBuilder();
	        	builer.append(urlField.getModelObjectAsString());
	        	builer.append("\n");
	        	builer.append("「" + commentField.getModelObjectAsString() + "」");
	        	builer.append("　by " + site.getUser().getOfficeId());
	        
	        	String conference = jabberField.getModelObjectAsString() + "@conference.im.drecom.co.jp";
	        	jabberService.sendMessageForRoom(conference, builer.toString());
	        }
	        
	        addCookie("jabber-checkbox", jabberCheckBox.getModelObjectAsString());
	        if(StringUtils.isNotBlank(jabberField.getModelObjectAsString())) {
	        	addCookie("jabber-send", jabberField.getModelObjectAsString());
	        }
	        
	        final RequestCycle cycle = getRequestCycle();
            PageParameters parameters = new PageParameters();
            cycle.setResponsePage(getPageFactory().newPage(Close.class, parameters));
            cycle.setRedirect(true);
	    }
	}
}
