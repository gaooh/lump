package com.gaooh.lump.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.java.ao.EntityManager;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.GuideboardStar;
import com.gaooh.lump.service.GuideboardStarService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * GuideboardStar系の処理を行う実装クラス
 * 
 * @author gaooh
 * @date 2008/06/08
 */
public class GuideboardStarServiceImpl extends AbstractModule implements GuideboardStarService {

	private EntityManager manager;
	
	public GuideboardStarServiceImpl() {
		manager = new EntityManager("jdbc:mysql://localhost/board_development", "board", "board");
	}
	
	public Integer getStarCount(Integer guideboardId) {
		GuideboardStar[] count = null;
		try {
			count = manager.findWithSQL(GuideboardStar.class, "sum(starCount)",
								"select sum(starCount) from guideboard_star " +
								"where guideboardID = ? ", 
								guideboardId.intValue());
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
		
		return count[0].getID(); // カラムが一つしかないのでIDに格納されている
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.GuideboardStarService#addStar(java.lang.Integer, java.lang.Integer)
	 */
	public Integer addStar(Integer guideboardId, Integer userId) {
		try {
			GuideboardStar[] guideboardStar = manager.find(GuideboardStar.class, 
					"guideboardId = ? and userId = ? ", 
					guideboardId, userId);
			
			if(guideboardStar.length == 0) { // 新規作成する
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("guideboardId", guideboardId);
				param.put("userId", userId);
				param.put("starCount", 1);
				param.put("createdAt", new Date());
				param.put("updatedAt", new Date());
				GuideboardStar createStar = manager.create(GuideboardStar.class, param);
				return createStar.getStarCount();
				
			} else {
				Integer starCount = guideboardStar[0].getStarCount();
				guideboardStar[0].setStarCount(++starCount);
				guideboardStar[0].save();
				return guideboardStar[0].getStarCount();
			}
			
		} catch (SQLException e) {
			System.out.println(e);
			throw new ApplicationException("e0001");
		}
	}
	
	@Override
	protected void configure() {
		bind( GuideboardStarService.class ).to( GuideboardStarServiceImpl.class ).in( Scopes.SINGLETON );
	}

	

}
