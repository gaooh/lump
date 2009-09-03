package com.gaooh.lump.service;

import java.util.List;
import java.util.Map;

import com.gaooh.lump.entity.Site;
import com.gaooh.lump.service.impl.SiteServiceImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(SiteServiceImpl.class)
public interface SiteService {

	public Site create();

	/**
	 * オフィスサイトを作成する
	 * @return
	 */
	public Site createByOffice(Map<String, Object> param);
	
	/**
	 * サイト情報を作成する
	 * @param site
	 * @param comment
	 */
	public void save(Site site, String comment);

	/**
	 * サイトをガイドボードに紐づける
	 * @param guideboardId
	 * @param siteId
	 */
	public void connetcGuideboard(Integer guideboardId, Integer siteId);

	/**
	 * サイトとガイドボードの紐付けを解除する
	 * @param guideboardId
	 * @param siteId
	 */
	public void unConnectGuideboard(Integer guideboardId, Integer siteId);
	
	/**
	 * 全てのサイトを取得する
	 * @return
	 */
	public List<Site> findAll();
	
	/**
	 * @param siteId
	 * @return
	 */
	public Site findById(Integer siteId);
	
	/**
	 * 指定ガイドボードのサイト数を取得する
	 * @param id
	 * @return
	 */
	public Integer countSiteByGuideboardId(Integer id);
	
	/**
	 * 指定URLをもつサイト情報を取得する。なければnullを返す
	 * @param url
	 * @return
	 */
	public Site findByUrl(String url);
	
	/**
	 * ユーザ登録サイト一覧を取得する
	 * @param limit
	 * @param page
	 * @return
	 */
	public List<Site> findNormalSites(Integer limit, Integer page);
	
	/**
	 * 通常サイト数を返す
	 * @return
	 */
	public Integer countNormalSites();
	
	/**
	 * オフィスのサイト一覧を取得する
	 * @param limit
	 * @param page
	 * @return
	 */
	public List<Site> findOfficeSites(Integer limit, Integer page);
	
	/**
	 * オフィスサイト数を返す
	 * @return
	 */
	public Integer countOfficeSites();
	
	/**
	 * はてなブックマークのサイト一覧を取得する
	 * @return　List<Site>
	 */
	public List<Site> findHatenaSites(Integer limit, Integer page);
	
	/**
	 * はてぶサイト数を返す
	 * @return
	 */
	public Integer countHatenaSites();
	
	public List<Site> findSitesByGuideboardId(Integer guideboardId);
	
}
