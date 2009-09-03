package com.gaooh.lump.service.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.java.ao.EntityManager;
import net.java.ao.Query;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.Site;
import com.gaooh.lump.entity.UserSite;
import com.gaooh.lump.service.SiteUserService;
import com.google.inject.Singleton;

@Singleton
public class SiteUserImpl implements SiteUserService {

	private EntityManager manager;
	
	public SiteUserImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
		Logger.getLogger("net.java.ao").setLevel(Level.FINE);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteUserService#countUser(java.lang.Integer)
	 */
	public Integer countUser(Integer siteId) {
		UserSite[] count = null;
		try {
			count = manager.find(UserSite.class, Query.select("userId").distinct().where("siteId = ? ", siteId));
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return count.length;
	}

	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteUserService#countComment(java.lang.Integer)
	 */
	public Integer countComment(Integer siteId) {
		Integer count = null;
		try {
			count = manager.count(UserSite.class, "siteId = ? ", siteId);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return count;
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteUserService#findSitesByUserId(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public List<Site> findSitesByUserId(Integer userId, Integer limit, Integer page) {
		Site[] sites = null;
		try {
			sites = manager.findWithSQL(Site.class, "siteID",
										"select distinct(siteID) from user_site " +
										"where userID = ? limit ?, ? ", 
										userId.intValue(), page.intValue() * limit.intValue(), limit.intValue());
			
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		
		return Arrays.asList(sites);
	}

	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteUserService#countSitesByUserId(java.lang.Integer)
	 */
	public Integer countSitesByUserId(Integer userId) {
		Site[] sites = null;
		try {
			sites = manager.findWithSQL(Site.class, "siteID",
										"select distinct(siteID) from user_site " +
										"where userID = ? ", 
										userId.intValue());
			
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		
		return sites.length;
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteUserService#findSiteUsersBySiteId(java.lang.Integer)
	 */
	public List<UserSite> findSiteUsersBySiteId(Integer siteId) {
		UserSite[] userSites = null;
		try {
			userSites = manager.find(UserSite.class, 
								 "siteId = ? order by updatedAt desc ", 
								 siteId);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return Arrays.asList(userSites);
	}

	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.SiteUserService#addComment(com.gaooh.lump.entity.Site, java.lang.String)
	 */
	public void addComment(Integer siteId, Integer userId, String comment) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userID", userId);
		param.put("siteID", siteId);
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
	
}
