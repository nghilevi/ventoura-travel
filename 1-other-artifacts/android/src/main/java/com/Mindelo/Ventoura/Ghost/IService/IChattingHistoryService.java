package com.Mindelo.Ventoura.Ghost.IService;

import java.util.List;

import com.Mindelo.Ventoura.Entity.ChattingHistory;

public interface IChattingHistoryService {

	
	/**
	 * the first time the user enter the conversation page, load all unread messages and maybe more to fill the page
	 */
	public List<ChattingHistory> getChattingHistoryForInitialState(long userId,
			long partnerId, int userRole, int partnerRole);
	
	public List<ChattingHistory> getChattingHistory(long userId,
			long partnerId, int userRole, int partnerRole, long earlierThanId,
			int limit);
	/**
	 * given a particular user, return the unread messages of him with a specific chatter
	 */
	public List<ChattingHistory> getAllUnreadChattingHistory(long userId,
			long partnerId, int userRole, int partnerRole);
	/**
	 * given a particular user, return the unread messages of him with a specific chatter
	 */
	public int getNumberOfUnreadChattingHistoryParticularPartner(long userId,
			long partnerId, int userRole, int partnerRole);
	/**
	 * @return the number of how many chatters send new messages to this user.
	 */
	public int getNumberOfChatterWithUnreadMessage(long userId, int userRole);


	public ChattingHistory getChattingHistoryByid(long id);

	public void saveChattingHistory(ChattingHistory chattingHistory);

	public void setChattingHistoryAsRead(List<ChattingHistory> chattingHistories);

	/**
	 * information about the last message
	 */
	public ChattingHistory getLastMessageWithASpecificChatter(long userId, long partnerId, int userRole,
			int partnerRole);
}
