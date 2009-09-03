package com.gaooh.lump.html.guideboard;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.html.BasicWebPage;
import com.gaooh.lump.service.GuideboardService;
import com.google.inject.Inject;

/**
 * ガイドボード新規作成ページ
 * 
 * @author gaooh
 * @date 2008/05/06
 */
public class New extends BasicWebPage {

	@Inject
    private GuideboardService guideboardService;
	
	public New() {
		super();
		add( new FeedbackPanel("feedback"));
		add( new GuideBoardCreateForm("createForm"));
	}

	/**
	 * 新規作成フォーム
	 * @author gaooh
	 * @date 2008/05/06
	 */
	private class GuideBoardCreateForm extends Form {
		
		private static final long serialVersionUID = 1L;

		private TextField textFld = new TextField("titleTextField", new Model());
		
		private TextArea textArea = new TextArea("descriptionTextField", new Model());

		private GuideBoardSubmitButton submitButton = new GuideBoardSubmitButton("submitBtn");
		
		public GuideBoardCreateForm(String id) {
			super(id);
			add(textFld);
			add(textArea);
			add(submitButton);
		}
	}
	
	/**
	 * 新規作成サブミットボタンコンポーネント
	 * @author gaooh
	 * @date 2008/05/06
	 */
	private class GuideBoardSubmitButton extends Button {
	
		private static final long serialVersionUID = 1L;

		public GuideBoardSubmitButton(String id) {
			super(id);
		}
		
		@Override
		public void onSubmit() {
	        Component titleField = this.getParent().get( "titleTextField");
	        Component descriptionField = this.getParent().get("descriptionTextField");
	        
	        Guideboard guideboard = guideboardService.create();
	        guideboard.setUserId(1);
	        guideboard.setTitle(titleField.getModelObjectAsString());
	        guideboard.setDescription(descriptionField.getModelObjectAsString());
	        guideboard.setDeletedAt(null);
	        guideboardService.save(guideboard);
	        
	        // サイト設定画面へリダイレクト
	        setResponsePage(Edit.class, new PageParameters("id=" + guideboard.getID()));
	    }
	}
}
