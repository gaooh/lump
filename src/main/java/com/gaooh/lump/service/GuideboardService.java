package com.gaooh.lump.service;

import java.util.List;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.entity.Guideboard;
import com.gaooh.lump.service.impl.GuideboardServiceImpl;
import com.google.inject.ImplementedBy;


/**
 * @author gaooh
 * @date 2008/05/06
 */
@ImplementedBy(GuideboardServiceImpl.class)
public interface GuideboardService {

	/**
	 * Guideboard一覧を返す。
	 * @return
	 * @throws ApplicationException
	 */
	public List<Guideboard> findAll() throws ApplicationException ;
	
	/**
	 * Guideboard一覧をlimit数返す。
	 * @param limit
	 * @param page
	 * @return
	 */
	public List<Guideboard> findGuideboards(Integer limit, Integer page);
	
	/**
	 * 主キーであるIDでGuideboardを返す。見つからなかった場合ApplicationExceptionを返す
	 * @param id
	 * @return　Guideboard
	 * @throws ApplicationException
	 */
	public Guideboard findById(Integer id) throws ApplicationException ;
	
	/**
	 * Guideboard一覧をStar数順に取得し返す
	 * @param limit
	 * @return
	 */
	public List<Guideboard> findGuideboardOrderStar(Integer limit, Integer page);
	
	public Guideboard create() throws ApplicationException ;
	
	public void save(Guideboard guideboard);
	
	/**
	 * 主キーであるIDでGuideboardを削除する
	 * @param id
	 */
	public void delete(Integer id);
	
}
