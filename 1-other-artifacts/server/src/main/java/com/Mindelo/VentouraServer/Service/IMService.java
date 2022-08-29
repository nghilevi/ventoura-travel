package com.Mindelo.VentouraServer.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.springframework.stereotype.Component;

import com.Mindelo.VentouraServer.Constant.IMConstant;
import com.Mindelo.VentouraServer.Enum.UserRole;
import com.Mindelo.VentouraServer.IService.IIMService;

@Component
public class IMService implements IIMService {

	public XMPPConnection getConnection(String domain) throws Exception {

		ConnectionConfiguration config = new ConnectionConfiguration(domain);
		config.setCompressionEnabled(true);

		XMPPConnection connection = new XMPPTCPConnection(config);
		// Connect to the server
		connection.connect();
		return connection;
	}

	public XMPPConnection getConnection(String domain, int port, String service)
			throws Exception {
		ConnectionConfiguration config = new ConnectionConfiguration(domain,
				port, service);
		config.setSecurityMode(SecurityMode.disabled);
		XMPPConnection connection = new XMPPTCPConnection(config);
		connection.connect();
		return connection;
	}

	public boolean login(XMPPConnection connection, String username,
			String password) {
		try {
			connection.login(username, password);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendMessage(XMPPConnection imConnection,
			String chattingPartnerIMAccountname, String content) {
		Message msg = new Message(chattingPartnerIMAccountname,
				Message.Type.chat);

		msg.setBody(content);
		try {
			if (imConnection != null) {

				// send the message
				imConnection.sendPacket(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(XMPPConnection imConnection, long userId,
			UserRole userRole, String content) {
		String userIMAccount;
		if (userRole == UserRole.GUIDE) {
			userIMAccount = "g_" + userId + "@" + IMConstant.SERVER_DOMAIN_NAME;
		} else {
			userIMAccount = "t_" + userId + "@" + IMConstant.SERVER_DOMAIN_NAME;
		}
		sendMessage(imConnection, userIMAccount, content);
	}

	public boolean createAccount(XMPPConnection connection, String regUserName,
			String regUserPwd) {
		try {
			AccountManager manager = AccountManager.getInstance(connection);
			manager.createAccount(regUserName, regUserPwd);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean deleteAccount(XMPPConnection connection) {
		try {
			AccountManager manager = AccountManager.getInstance(connection);
			manager.deleteAccount();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean changePassword(XMPPConnection connection, String pwd) {
		try {
			AccountManager manager = AccountManager.getInstance(connection);
			manager.changePassword(pwd);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<RosterGroup> getGroups(Roster roster) {
		List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		Iterator<RosterGroup> i = rosterGroup.iterator();
		while (i.hasNext())
			groupsList.add(i.next());
		return groupsList;
	}

	public List<RosterEntry> getEntriesByGroup(Roster roster, String groupName) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		RosterGroup rosterGroup = roster.getGroup(groupName);
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext())
			EntriesList.add(i.next());
		return EntriesList;
	}

	public List<RosterEntry> getAllEntries(Roster roster) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntry = roster.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext())
			EntriesList.add(i.next());
		return EntriesList;
	}

	public boolean addGroup(Roster roster, String groupName) {
		try {
			roster.createGroup(groupName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeGroup(Roster roster, String groupName) {
		return false;
	}

	public boolean addUser(Roster roster, String userName, String name) {
		try {
			roster.createEntry(userName, name, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addUser(Roster roster, String userName, String name,
			String groupName) {
		try {
			roster.createEntry(userName, name, new String[] { groupName });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeUser(Roster roster, String userName) {
		try {

			if (userName.contains("@")) {
				userName = userName.split("@")[0];
			}
			RosterEntry entry = roster.getEntry(userName);
			roster.removeEntry(entry);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/* Presence Functions */
	public void updateStateToAvailable(XMPPConnection connection)
			throws NotConnectedException {
		Presence presence = new Presence(Presence.Type.available);
		connection.sendPacket(presence);
	}

	public void updateStateToUnAvailable(XMPPConnection connection)
			throws NotConnectedException {
		Presence presence = new Presence(Presence.Type.unavailable);
		connection.sendPacket(presence);
	}

	public void updateStateToUnAvailableToSomeone(XMPPConnection connection,
			String userName) throws NotConnectedException {
		Presence presence = new Presence(Presence.Type.unavailable);
		presence.setTo(userName);
		connection.sendPacket(presence);
	}

	public void updateStateToAvailableToSomeone(XMPPConnection connection,
			String userName) throws NotConnectedException {
		Presence presence = new Presence(Presence.Type.available);
		presence.setTo(userName);
		connection.sendPacket(presence);
	}

	public void changeStateMessage(XMPPConnection connection, String status)
			throws NotConnectedException {
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);
		connection.sendPacket(presence);

	}

	/* Presence Functions End */

}
