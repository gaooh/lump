package com.gaooh.lump.service.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.ao.EntityManager;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.service.GuideboardService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

/**
 * Guideboard系の処理を行うクラス
 * 
 * @author gaooh
 * @date 2008/05/06
 */
@Singleton
public class GuideboardServiceImpl extends AbstractModule implements GuideboardService {

	private EntityManager manager;
	
	public GuideboardServiceImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
	}
	
	public Guideboard findById(Integer id) throws ApplicationException {
		Guideboard[] guideboards = null;
		try {
			guideboards = manager.find(Guideboard.class, "id = ? ", id);
		} catch (SQLException e) {
			throw new ApplicationException("e0001");
		}
		if(guideboards.length == 0) {
			return null;
		}
		return guideboards[0];
	}
	
	public List<Guideboard> findAll() throws ApplicationException {
		Guideboard[] guideboards = null;
		try {
			guideboards = manager.find(Guideboard.class, "deletedAt is null");
		} catch (SQLException e) {
			throw new ApplicationException("e0001");
		}
		return Arrays.asList(guideboards);
	}
	
	public List<Guideboard> findGuideboards(Integer limit, Integer page) throws ApplicationException {
		Guideboard[] guideboard;
		try {
			guideboard = manager.find(Guideboard.class,
									 "deletedAt is null order by updatedAt desc limit ?, ?",
									 page.intValue() * limit.intValue(), limit.intValue());
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return Arrays.asList(guideboard);
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.GuideboardService#findGuideboardOrderStar(java.lang.Integer, java.lang.Integer)
	 */
	public List<Guideboard> findGuideboardOrderStar(Integer limit, Integer page) {
		Guideboard[] guideboard;
		try {
			guideboard = manager.findWithSQL(Guideboard.class, "guideboard.id",
									 "select guideboard.*, sum(guideboard_star.starCount) " +
									 " from guideboard, guideboard_star " +
									 " where guideboard.id = guideboard_star.guideboardID " +
									 " group by guideboard_star.guideboardID " +
									 " order by sum(guideboard_star.starCount) desc " +
									 " limit ?, ?",
									 page * limit, limit);
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		return Arrays.asList(guideboard);
	}
	
	public Guideboard create() throws ApplicationException {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", -1);
			param.put("createdAt", new Date());
			param.put("updatedAt", new Date());
			return manager.create(Guideboard.class, param);
		} catch (SQLException e) {
			throw new ApplicationException("e0001");
		}
	}

	public void save(Guideboard guideboard) {
		guideboard.save();
	}

	public void delete(Integer id) {
		Guideboard[] guideboards = null;
		try {
			guideboards = manager.find(Guideboard.class, "id = ? ", id);
			if(guideboards.length != 0) {
				guideboards[0].setDeletedAt(new Date());
				guideboards[0].save();
			}
		} catch (SQLException e) {
			throw new ApplicationException("e0001");
		}
	}
	
	@Override
	protected void configure() {
		bind( GuideboardService.class ).to( GuideboardServiceImpl.class ).in( Scopes.SINGLETON );
	}

}
