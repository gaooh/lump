package com.gaooh.lump.service;

import java.util.List;
import java.util.Map;

import com.gaooh.lump.entity.Comment;
import com.gaooh.lump.service.impl.CommentServiceImpl;
import com.google.inject.ImplementedBy;

/**
 * コメント処理
 * @author gaooh
 * @date 2008/05/06
 */
@ImplementedBy(CommentServiceImpl.class)
public interface CommentService {

	/**
	 * コメントを追加し、作成したコメントを返す
	 * @param param
	 * @return
	 */
	public Comment addComment(Map<String, Object> param);
	
	/**
	 * ガイドボードに紐づいているコメント一覧を返す
	 * @param guideboardId
	 * @return
	 */
	public List<Comment> findCommentsByGuideboardId(Integer guideboardId);
}
