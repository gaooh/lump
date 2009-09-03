package com.gaooh.lump.service.impl;

import java.util.logging.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.gaooh.lump.app.ApplicationException;
import com.gaooh.lump.service.JabberService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * JabberServiceの実装クラス
 * @author gaooh
 * @date 2008/06/23
 */
public class JabberServiceImpl extends AbstractModule implements JabberService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private static XMPPConnection connection = null;
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.JabberService#connection(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void connection(String serverName, String userName, String password) {
		connection = new XMPPConnection(serverName);
		try {
			connection.connect();
			connection.login(userName, password);
		} catch (XMPPException e) {
			logger.fine("IMサーバとの連携処理に問題が発生しました。" + e.getMessage());
			throw new ApplicationException("j-001");
		}
	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.JabberService#sendMessageForRoom(java.lang.String, java.lang.String)
	 */
	public void sendMessageForRoom(String roomName, String message) {
		try {
			MultiUserChat multiUserChat = new MultiUserChat(connection, roomName);
			multiUserChat.addMessageListener(new PacketListener() {
				public void processPacket(Packet arg0) {
					System.out.println("Received message: " + ((Message)arg0).getBody());
				}
			});
			
			try {
				multiUserChat.join("lump bot");
				
			} catch (XMPPException e) {
				logger.warning(e.getMessage());
			}
			multiUserChat.sendMessage(message);
			
		} catch (XMPPException e) {
			logger.fine("IMサーバとの連携処理に問題が発生しました。" + e.getMessage());
			throw new ApplicationException("j-002");
		} 

	}
	
	/* (非 Javadoc)
	 * @see com.gaooh.lump.service.JabberService#disConnection()
	 */
	public void disConnection() {
		if(connection != null) {
			connection.disconnect();
		}
	}	
	
	@Override
	protected void configure() {
		bind(JabberService.class).to(JabberServiceImpl.class).in(Scopes.SINGLETON);
	}
	
}
