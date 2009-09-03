package com.gaooh.lump.service;

import com.gaooh.lump.service.impl.JabberServiceImpl;
import com.google.inject.ImplementedBy;

/**
 * Jabberへの処理
 * @author gaooh
 * @date 2008/06/22
 */
@ImplementedBy(JabberServiceImpl.class)
public interface JabberService {

	/**
	 * 接続処理をする
	 */
	public void connection(String serverName, String userName, String password);
	
	/**
	 * カンファレンスルームにメッセージを送る
	 * @param roomName
	 * @param message
	 */
	public void sendMessageForRoom(String roomName, String message);
	
	/**
	 * 接続処理を解除する
	 */
	public void disConnection();
	
}
