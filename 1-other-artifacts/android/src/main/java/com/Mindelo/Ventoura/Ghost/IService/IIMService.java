package com.Mindelo.Ventoura.Ghost.IService;

import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.VCard;

public interface IIMService {

	public XMPPConnection getConnection(String domain) throws XMPPException;

	public XMPPConnection getConnection(String domain, int port, String service)
			throws XMPPException;
	
	public boolean login(XMPPConnection connection, String username, String password);
	
	

	public void sendMessage(XMPPConnection connection, String chattingPartnerIMAccountname, String content);

	/**
	 * 注册用户
	 * 
	 * @param connection
	 * @param regUserName
	 * @param regUserPwd
	 * @return
	 */
	public boolean createAccount(XMPPConnection connection, String regUserName,
			String regUserPwd);

	/**
	 * 删除当前用户
	 * 
	 * @param connection
	 * @return
	 */
	public boolean deleteAccount(XMPPConnection connection);

	/**
	 * 删除修改密码
	 * 
	 * @param connection
	 * @return
	 */
	public boolean changePassword(XMPPConnection connection, String pwd);

	/**
	 * 返回所有组信息 <RosterGroup>
	 * 
	 * @return List(RosterGroup)
	 */
	public List<RosterGroup> getGroups(Roster roster);

	/**
	 * 返回相应(groupName)组里的所有用户<RosterEntry>
	 * 
	 * @return List(RosterEntry)
	 */
	public List<RosterEntry> getEntriesByGroup(Roster roster, String groupName);

	/**
	 * 返回所有用户信息 <RosterEntry>
	 * 
	 * @return List(RosterEntry)
	 */
	public List<RosterEntry> getAllEntries(Roster roster);

	/**
	 * 获取用户的vcard信息
	 * 
	 * @param connection
	 * @param user
	 * @return
	 * @throws XMPPException
	 */
	public VCard getUserVCard(XMPPConnection connection, String user)
			throws XMPPException;

	/**
	 * 添加一个组
	 */
	public boolean addGroup(Roster roster, String groupName);

	/**
	 * 删除一个组
	 */
	public boolean removeGroup(Roster roster, String groupName);

	/**
	 * 添加一个好友 无分组
	 */
	public boolean addUser(Roster roster, String userName, String name);

	/**
	 * 添加一个好友到分组
	 * 
	 * @param roster
	 * @param userName
	 * @param name
	 * @return
	 */
	public boolean addUser(Roster roster, String userName, String name,
			String groupName);

	/**
	 * 删除一个好友
	 * 
	 * @param roster
	 * @param userName
	 * @return
	 */
	public boolean removeUser(Roster roster, String userName);

	/* Presence Functions */
	public void updateStateToAvailable(XMPPConnection connection);

	public void updateStateToUnAvailable(XMPPConnection connection);

	public void updateStateToUnAvailableToSomeone(XMPPConnection connection,
			String userName);

	public void updateStateToAvailableToSomeone(XMPPConnection connection,
			String userName);

	/**
	 * 修改心情
	 * 
	 * @param connection
	 * @param status
	 */
	public void changeStateMessage(XMPPConnection connection, String status);
}
