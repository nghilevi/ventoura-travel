package com.Mindelo.Ventoura.Constant;

public class BroadcastConstant {

	public static final String CHOOSE_ROLE_REFRESH_VIEWPAGE_ACTION="com.Mindelo.Ventoura.UI.Activity.LoginChooseRoleActivity.CHOOSE_ROLE_REFRESH_VIEWPAGE_ACTION";
	public static final String PAYMENTMETHOD_REFRESH_VIEWPAGE_ACTION="com.Mindelo.Ventoura.UI.Activity.PaymentSelect.PAYMENTMETHOD_REFRESH_VIEWPAGE_ACTION";
	
	/*
	 * when the user's profile images have been changed, we need to update the slide gallery and the portal image
	 */
	public static final String USER_PROFILE_IMAGES_UPDATED_ACTION = "com.Mindelo.Ventoura.Constants.ProfileEdit.USER_PROFILE_IMAGES_UPDATED_ACTION";
	
	/*
	 * when the head image for a match was changed, we need to update the head image of the match
	 */
	public static final String USER_MATCH_IMAGES_UPDATED_ACTION = "com.Mindelo.Ventoura.Constants.ProfileEdit.USER_MATCH_IMAGES_UPDATED_ACTION";

	/*
	 * when the done editing the message filter, we need to update the message list
	 */
	public static final String USER_MESSAGE_FILTER_UPDATED_ACTION = "com.Mindelo.Ventoura.Constants.MessageFragment.USER_MESSAGE_FILTER_UPDATED_ACTION";
	
	
	/*
	 * when the done editing the booking filter, we need to update the booking list
	 */
	public static final String  USER_BOOKING_FILTER_UPDATED_ACTION = "com.Mindelo.Ventoura.Constants.BookingFragment.USER_BOOKING_FILTER_UPDATED_ACTION";

	/*
	 * when the user's new trip added, notice the booking trip list
	 */
	public static final String  USER_NEW_TRIP_ADDED_ACTION = "com.Mindelo.Ventoura.Constants.AddTripActivity.USER_NEW_TRIP_ADDED_ACTION";
}
