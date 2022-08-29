package com.Mindelo.VentouraServer.Constant;

public class PushConstant {

	public static final String PUSH_IS_MATCH_WITH_TRAVELLER = "IS_MATCH_WITH_TRAVELLER#"; // + travellerId
	public static final String PUSH_IS_MATCH_WITH_GUIDE = "IS_MATCH_WITH_GUIDE#"; // + guideId
	
	public static final String PUSH_TRAVELLER_CREATE_BOOKING = "TRAVELLER_CREATE_BOOKING#"; // +"bookingId"
	public static final String PUSH_TRAVELLER_PAID_BOOKING = "TRAVELLER_PAID_BOOKING#";  // +"bookingId"
	public static final String PUSH_GUIDE_ACCEPT_BOOKING = "GUIDE_ACCEPT_BOOKING#";  // +"bookingId"
	public static final String PUSH_GUIDE_REFUSE_BOOKING = "GUIDE_REFUSE_BOOKING#";  // +"bookingId"
	
}
