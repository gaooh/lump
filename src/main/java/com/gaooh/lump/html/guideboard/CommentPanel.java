package com.gaooh.lump.html.guideboard;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;

import com.gaooh.lump.entity.Comment;

/**
 * コメント情報を表示するパネル
 * 
 * @author gaooh
 * @date 2008/05/06
 */
public class CommentPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public CommentPanel(String id, Comment comment, Integer index) {
		super(id);
		add(new Label("index", String.valueOf(index)));
		add(new MultiLineLabel("comment", comment.getComment()));
		add(new Label("createdAt", comment.getCreatedAt().toString()));
		add(new Label("userName", comment.getUser().getOfficeId()));
	}

}
