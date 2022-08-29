package com.Mindelo.Ventoura.Ghost.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;

import com.Mindelo.Ventoura.Ghost.IService.IIMService;

public class IMService implements IIMService {

	public XMPPConnection getConnection(String domain) throws XMPPException {

		XMPPConnection connection = new XMPPConnection(domain);
		connection.connect();
		return connection;
	}

	public XMPPConnection getConnection(String domain, int port, String service)
			throws XMPPException {
		ConnectionConfiguration config = new ConnectionConfiguration(domain,
				port, service);
		XMPPConnection connection = new XMPPConnection(config);
		connection.connect();
		return connection;
	}
	
	public boolean login(XMPPConnection connection, String username,
			String password){
		try {
			connection.login(username, password);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void sendMessage(XMPPConnection imConnection, String chattingPartnerIMAccountname, String content){
		Message msg = new Message(chattingPartnerIMAccountname,
				Message.Type.chat);

//		msg.setProperty("size", size);
//		msg.setProperty("color", color);

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

	public boolean createAccount(XMPPConnection connection, String regUserName,
			String regUserPwd) {
		try {
			connection.getAccountManager().createAccount(regUserName,
					regUserPwd);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean deleteAccount(XMPPConnection connection) {
		try {
			connection.getAccountManager().deleteAccount();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean changePassword(XMPPConnection connection, String pwd) {
		try {
			connection.getAccountManager().changePassword(pwd);

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

	public VCard getUserVCard(XMPPConnection connection, String user)
			throws XMPPException {
		VCard vcard = new VCard();
		vcard.load(connection, user);

		return vcard;
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
	public void updateStateToAvailable(XMPPConnection connection) {
		Presence presence = new Presence(Presence.Type.available);
		connection.sendPacket(presence);
	}

	public void updateStateToUnAvailable(XMPPConnection connection) {
		Presence presence = new Presence(Presence.Type.unavailable);
		connection.sendPacket(presence);
	}

	public void updateStateToUnAvailableToSomeone(XMPPConnection connection,
			String userName) {
		Presence presence = new Presence(Presence.Type.unavailable);
		presence.setTo(userName);
		connection.sendPacket(presence);
	}

	public void updateStateToAvailableToSomeone(XMPPConnection connection,
			String userName) {
		Presence presence = new Presence(Presence.Type.available);
		presence.setTo(userName);
		connection.sendPacket(presence);
	}

	public void changeStateMessage(XMPPConnection connection, String status) {
		Presence presence = new Presence(Presence.Type.available);
		presence.setStatus(status);
		connection.sendPacket(presence);

	}

	/* Presence Functions End */

}
