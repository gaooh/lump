package com.gaooh.lump.service;

import java.util.List;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.entity.UserSite;
import com.gaooh.lump.service.impl.SiteUserImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(SiteUserImpl.class)
public interface SiteUserService {
	
	/**
	 * 指定サイトへコメントを追加する
	 * @param siteId
	 * @param userId
	 * @param comment
	 */
	public void addComment(Integer siteId, Integer userId, String comment);
	
	/**
	 * 指定サイトを登録しているユーザ数をかえす
	 * @param siteId
	 * @return
	 */
	public Integer countUser(Integer siteId);
	
	/**
	 * 指定サイトをコメントしているユーザ数をかえす
	 * @param siteId
	 * @return
	 */
	public Integer countComment(Integer siteId);
	
	/**
	 * 指定ユーザが登録しているサイト一覧を返す
	 * @param siteId
	 * @return
	 */
	public List<Site> findSitesByUserId(Integer userId, Integer limit, Integer page);
	
	/**
	 * 指定サイトを登録しているユーザ情報一覧を返す
	 * @param siteId
	 * @return
	 */
	public List<UserSite> findSiteUsersBySiteId(Integer siteId);
	
	/**
	 * 指定ユーザが登録しているサイト数を返す
	 * @param uesrId
	 * @return
	 */
	public Integer countSitesByUserId(Integer uesrId);
}
