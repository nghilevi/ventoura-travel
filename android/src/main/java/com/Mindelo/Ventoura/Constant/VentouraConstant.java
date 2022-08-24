package com.Mindelo.Ventoura.Constant;

import android.graphics.Color;

public class VentouraConstant {

	/*
	 * used by external links
	 */
	public static final String URL_VENTOURA_FACEBOOK = "https://www.facebook.com/pages/Ventoura/346550045486906";
	public static final String URL_VENTOURA_TWITTEE = "https://www.facebook.com/pages/Ventoura/346550045486906";

	/* Shared Preferences */
	public static final String SHARED_PREFERENCE_VENTOURA = "ventouraSharedReference";

	public static final String PRE_USER_ROLE = "UserRole";
	public static final String PRE_USER_FACEBOOK_ID = "UserFacebookId";
	public static final String PRE_USER_FIRST_NAME = "userFirstName";
	public static final String PRE_USER_LAST_NAME = "userLastName";
	public static final String PRE_USER_CITY = "userCity";
	public static final String PRE_USER_BIRTHDAY = "userBirthday";
	public static final String PRE_USER_GENDER = "userGender";
	public static final String PRE_USER_COUNTRY = "userCountry";
	public static final String PRE_USER_ID_IN_SERVER = "UserIdInServer";

	public static final String PRE_CHATTING_PARTNER_ROLE = "chattingPartneRole";
	public static final String PRE_CHATTING_PARTNER_ID_IN_SERVER = "chattingPartnerId";
	public static final String PRE_CHATTING_PARTNER_IM_ACCOUNT_NAME = "currentChattingPartnerIMAccountName";

	public static final String MALE = "male";
	public static final String FEMALE = "female";

	public static final String EURO_SYMBOL = "\u20ac";

	/**
	 * shared preferences added for message filter constants
	 */
	public static final String PRE_MESSAGE_FILTER_USER_ROLE_TRAVELLER = "messageFilterUserRoleTraveller";
	public static final String PRE_MESSAGE_FILTER_USER_ROLE_LOCAL = "messageFilterUserRoleLocal";
	public static final String PRE_MESSAGE_FILTER_GENDER_MALE = "messageFilterGenderMale";
	public static final String PRE_MESSAGE_FILTER_GENDER_FEMALE = "messageFilterGenderFemale";
	public static final String PRE_MESSAGE_FILTER_MAX_AGE = "messageFilterMaxAge";
	public static final String PRE_MESSAGE_FILTER_MIN_AGE = "messageFilterMinAge";
	public static final String PRE_MESSAGE_FILTER_BOOK_REQUEST = "messageFilterBookRequest";
	public static final String PRE_MESSAGE_FILTER_BOOK_PAID = "messageFilterBookPaid";
	public static final String PRE_MESSAGE_FILTER_BOOK_OTHERS = "messageFilterBookOthers";

	/**
	 * shared preferences added for booking filter constants
	 */
	public static final String PRE_BOOKING_FILTER_GENDER_MALE = "bookingFilterGenderMale";
	public static final String PRE_BOOKING_FILTER_GENDER_FEMALE = "bookingFilterGenderFemale";
	public static final String PRE_BOOKING_FILTER_BOOK_REQUEST = "bookingFilterBookRequest";
	public static final String PRE_BOOKING_FILTER_BOOK_PAID = "bookingFilterBookPaid";
	public static final String PRE_BOOKING_FILTER_BOOK_NOT_PAID = "bookingFilterBookNotPaid";
	public static final String PRE_BOOKING_FILTER_BOOK_CANCELLED = "bookingFilterBookCancelled";
	public static final String PRE_BOOKING_FILTER_BOOK_COMPLETE = "bookingFilterBookComplete";
	public static final String PRE_BOOKING_FILTER_BOOK_ERROR = "bookingFilterBookError";

	/**
	 * Default Ventoura color.
	 */
	public static final int DEFAULT_COLOR = Color.rgb(0xF3, 0x6b, 0x67);
	
	/**
	 *  shared preferences used by promotion 
	 */
	public static final String PRE_USER_ATTENDED_PROMOTION = "userAttendedPromotion";
	
	
	/**
	 * The following contants are used by value passing between activities 
	 */
	public static final String INTENT_GUIDE_PROFILE = "guideProfile";
	public static final String INTENT_TRAVELLER_PROFILE = "travellerProfile";
	public static final String INTENT_LOCAL_SET_TOUR_PRICE = "priceSet";
	public static final String INTENT_LOCAL_SET_TOUR_PAYMENT_METHOD = "paymentMethod";
	

}
