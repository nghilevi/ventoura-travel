package com.Mindelo.Ventoura.Constant;

public class IMConstant {

	/* URLS connect to server */
	public static final String URL_IM_SERVER = "54.191.27.22";
	//domain name of the IM server is needed, because the user's name will use it as suffix
	public static final String SERVER_DOMAIN_NAME = "ip-172-31-23-78";
	public static final int IM_SERVER_PORT = 5222;
	public static final String SERVICE = "Smack";
	
	public static final String USER_NAME = "userName";
	public static final String USER_PASSWORD = "userPassword";
	
	public static final String VENTOURA_SERVER_IM_ACCOUNT = "ventouraserver";
	
	/*
	 * used by incoming message broadcast notification
	 */
	public static final String INCOMING_MESSAGE_NOTICE_CONVERSATION = "incomingMessageNoticeConversation";
	public static final String INCOMING_MESSAGE_NOTICE_MESSAGE_LIST = "incomingMessageNoticeMessageList";
	public static final String INCOMING_MESSAGE_NOTICE_MESSAGE_PAYLOAD = "incomingMessageNoticeMessagePayload";
	

	public static final String IM_CURRENT_CHATTING_PARTNER_NAME = "currentChattingPartnerName";
	public static final String IM_CURRENT_CHATTING_PARTNER_ROLE = "currentChattingPartnerRole";
	public static final String IM_CURRENT_CHATTING_PARTNER_ID = "currentChattingPartnerID";
	public static final String IM_CURRENT_CHATTING_PARTNER_IM_ACCOUNT_NAME = "currentChattingPartnerIMAccountName";
	
}
