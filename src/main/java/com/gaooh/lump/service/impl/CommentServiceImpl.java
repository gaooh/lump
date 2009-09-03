package com.gaooh.lump.service.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.java.ao.EntityManager;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.Comment;
import com.gaooh.lump.service.CommentService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

/**
 * コメントの実処理
 * 
 * @author gaooh
 * @date 2008/05/06
 */
@Singleton
public class CommentServiceImpl extends AbstractModule implements CommentService {

	private EntityManager manager;
	
	public CommentServiceImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.CommentService#addComment(java.lang.Integer, java.util.Map)
	 */
	public Comment addComment(Map<String, Object> param) {
		try {
			param.put("createdAt", new Date());
			param.put("updatedAt", new Date());
			return manager.create(Comment.class, param);
		} catch (SQLException e) {
			throw new ApplicationException("e0001");
		}
	}

	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.CommentService#findCommentsByGuideboardId(java.lang.Integer)
	 */
	public List<Comment> findCommentsByGuideboardId(Integer guideboardId) {
		Comment[] comments = null;
		try {
			comments = manager.find(Comment.class, "guideboardId = ? order by updatedAt", guideboardId);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return Arrays.asList(comments);
	}
	
	@Override
	protected void configure() {
		bind( CommentService.class ).to( CommentServiceImpl.class ).in( Scopes.SINGLETON );	
	}
	
}
