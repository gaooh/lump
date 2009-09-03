package com.gaooh.wicket.markup.html;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * スター一覧画像パネル
 * @author gaooh
 * @date 2008/08/24
 */
public class StarImageListPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private ListView listview = null;
	
	private Integer starCount = 0;
	
	public StarImageListPanel(String id, Integer count) {
		super(id);
		this.starCount = count;
		
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < count.intValue(); i++) {
			list.add(i);
		}
		
		listview = new ListView("stars", list) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item) {
                item.add(new ExternalImage("star", new Model("/images/star.gif")));
            }
        };
        listview.setOutputMarkupId(true);
        add(listview);
	}
	
	public void resetStarCount(Integer count) {
		this.starCount = count;
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < count.intValue(); i++) {
			list.add(i);
		}
		
		listview = new ListView("stars", list) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item) {
                item.add(new ExternalImage("star", new Model("/images/star.gif")));
            }
        };
        listview.setOutputMarkupId(true);
        addOrReplace(listview);
	}
	
}
