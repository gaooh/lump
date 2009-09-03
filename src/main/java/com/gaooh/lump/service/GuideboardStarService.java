package com.gaooh.lump.service;

import com.gaooh.lump.service.impl.GuideboardStarServiceImpl;
import com.google.inject.ImplementedBy;

/**
 * @author gaooh
 * @date 2008/06/08
 */
@ImplementedBy(GuideboardStarServiceImpl.class)
public interface GuideboardStarService {

	/**
	 * そのガイドボードにつけられた星の数を取得する
	 * @param guideboardId
	 * @return
	 */
	public Integer getStarCount(Integer guideboardId);
	
	/**
	 * 星を追加する
	 * @param guideboardId
	 * @param userId
	 * @return
	 */
	public Integer addStar(Integer guideboardId, Integer userId);
}
