package com.gaooh.lump.service.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.ao.EntityManager;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.GuideboardSite;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.entity.UserSite;
import com.gaooh.lump.service.SiteService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

/**
 * @author gaooh
 * @date 2008/06/26
 */
@Singleton
public class SiteServiceImpl extends AbstractModule implements SiteService {

	private EntityManager manager;
	
	public SiteServiceImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
	}
	
	public Site create() {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", 1);
			param.put("url", "");
			param.put("title", "");
			param.put("type", Site.Type.normal);
			param.put("createdAt", new Date());
			param.put("updatedAt", new Date());
			return manager.create(Site.class, param);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
	}

	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#createByOffice(java.util.Map)
	 */
	public Site createByOffice(Map<String, Object> param) {
		try {
			param.put("type", Site.Type.office);
			param.put("createdAt", new Date());
			param.put("updatedAt", new Date());
			return manager.create(Site.class, param);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
	}


	public void save(Site site, String comment) {
		site.save();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userID", site.getUserId());
		param.put("siteID", site.getID());
		param.put("comment", comment);
		param.put("createdAt", new Date());
		param.put("updatedAt", new Date());
		
		try {
			manager.create(UserSite.class, param);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		
	}
	
	public void connetcGuideboard(Integer guideboardId, Integer siteId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("guideboardId", guideboardId);
		param.put("siteId", siteId);
		try {
			manager.create(GuideboardSite.class, param);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
	}
	
	public void unConnectGuideboard(Integer guideboardId, Integer siteId) {
		try {
			GuideboardSite[] guideboardSites = manager.find(GuideboardSite.class, 
															"guideboardId = ? and siteId = ? ", 
															guideboardId, siteId);
			if(guideboardSites.length > 0) {
				manager.delete(guideboardSites[0]);
			}
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}	
	}
	
	public List<Site> findAll() {
		Site[] sites = null;
		try {
			sites = manager.find(Site.class);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return Arrays.asList(sites);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#findById(java.lang.Integer)
	 */
	public Site findById(Integer siteId) {
		Site[] sites = null;
		try {
			sites = manager.find(Site.class, " id = ? ", siteId);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return sites[0];
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#findByUrl(java.lang.String)
	 */
	public Site findByUrl(String url) {
		Site[] sites = null;
		try {
			sites = manager.find(Site.class, " url = ? ", url);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("SiteServie-0001");
		}
		if(sites == null || sites.length == 0) {
			return null;
		}
		return sites[0];
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#findNormalSites(java.lang.Integer, java.lang.Integer)
	 */
	public List<Site> findNormalSites(Integer limit, Integer page) {
		return helperFindSites(limit, page, Site.Type.normal);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#countNormalSites()
	 */
	public Integer countNormalSites() {
		return helperCountSites(Site.Type.normal);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#findOfficeSites(java.lang.Integer, java.lang.Integer)
	 */
	public List<Site> findOfficeSites(Integer limit, Integer page) {
		return helperFindSites(limit, page, Site.Type.office);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#countOfficeSites()
	 */
	public Integer countOfficeSites() {
		return helperCountSites(Site.Type.office);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#findHatenaSites(java.lang.Integer, java.lang.Integer)
	 */
	public List<Site> findHatenaSites(Integer limit, Integer page) {
		return helperFindSites(limit, page, Site.Type.hatena);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#countHatenaSites()
	 */
	public Integer countHatenaSites() {
		return helperCountSites(Site.Type.hatena);
	}
	
	public List<Site> findSitesByGuideboardId(Integer guideboardId) {
		Site[] sites = null;
		try {
			sites = manager.findWithSQL(Site.class, "siteID",
										"select DISTINCT siteID from guideboard_site " +
										"where guideboardID = ? ", 
										guideboardId.intValue());
			
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		
		return Arrays.asList(sites);
	}	
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteService#countSiteByGuideboardId(java.lang.Integer)
	 */
	public Integer countSiteByGuideboardId(Integer guideboardId) {
		Integer count = 0;
		try {
			count = manager.count(GuideboardSite.class, " guideboardID = ? ", guideboardId);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return count;
	}
	
	/**
	 * サイト数をカウントする
	 * @param type サイトタイプ
	 * @return
	 */
	private Integer helperCountSites(Site.Type type) {
		Integer count = 0;
		try {
			count = manager.count(Site.class, " type = ? ", type);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return count;
	}
	
	/**
	 * サイトを取得する
	 * @param limit
	 * @param page
	 * @param type　サイトタイプ
	 * @return
	 */
	private List<Site> helperFindSites(Integer limit, Integer page, Site.Type type) {
		Site[] sites = null;
		try {
			sites = manager.find(Site.class, 
								 "type = ? order by updatedAt desc limit ?, ? ", 
								 type, page.intValue() * limit.intValue(), limit.intValue());
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return Arrays.asList(sites);
	}

	@Override
	protected void configure() {
		bind( SiteService.class ).to( SiteServiceImpl.class ).in( Scopes.SINGLETON );
	}
		
}
