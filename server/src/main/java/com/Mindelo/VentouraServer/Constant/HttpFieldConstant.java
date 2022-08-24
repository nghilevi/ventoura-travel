package com.Mindelo.VentouraServer.Constant;

public class HttpFieldConstant {
	
	public static final String SERVER_TRAVELLER_GALLERY_ID = "travellerGalleryId";
	public static final String SERVER_GUIDE_GALLERY_ID = "guideGalleryId";
	public static final String GUIDE_NEW_PHOTO = "guideNewPhoto";
	public static final String TRAVELLER_NEW_PHOTO = "travellerNewPhoto";
	
	/* Parameters for uploading traveller's profile */
	public static final String TRAVELLER_TRAVERLLER_FIRST_NAME = "travellerFirstname";
	public static final String TRAVELLER_TRAVERLLER_LAST_NAME = "travellerLastname";
	public static final String TRAVELLER_FACEBOOK_ACCOUNT_NAME = "facebookAccountName";
	public static final String TRAVELLER_GENDER = "gender";
	public static final String TRAVELLER_CITY = "city";
	public static final String TRAVELLER_COUNTRY = "country";
	public static final String TRAVELLER_DATE_OF_BIRTH = "dateOfBirth";
	public static final String TRAVELLER_TEXT_BIOGRAPHY = "textBiography";
	public static final String TRAVELLER_PORTAL_PHOTO = "portalPhoto";
	
	public static final String SERVER_TRAVELLER_ID = "travellerId";
	
	/* Parameters for uploading guide's profile */
	public static final String GUIDE_GUIDE_FIRST_NAME = "guideFirstname";
	public static final String GUIDE_GUIDE_LAST_NAME = "guideLastname";
	public static final String GUIDE_FACEBOOK_ACCOUNT_NAME = "facebookAccountName";
	public static final String GUIDE_GENDER = "gender";
	public static final String GUIDE_DATE_OF_BIRTH = "dateOfBirth";
	public static final String GUIDE_TEXT_BIOGRAPHY = "textBiography";
	public static final String GUIDE_PORTAL_PHOTO = "portalPhoto";
	
	public static final String GUIDE_CITY = "city";
	public static final String GUIDE_COUNTRY = "country";
	public static final String GUIDE_IS_PREMIUM = "isPremium";
	
	public static final String GUIDE_PAYMENT_METHOD = "paymentMethod";
	public static final String GUIDE_PAYMENT_MONEY_TYPE = "momeyType";
	public static final String GUIDE_TOUR_PRICE = "tourPrice";
	public static final String GUIDE_TOUR_LENGTH = "tourLength";
	public static final String GUIDE_TOUR_TYPE = "tourType";
	public static final String GUIDE_ATTRACTION_NAME = "guideAttractionName";
	public static final String GUIDE_HIDDEN_TREASURE_NAME = "guideHiddenTreasureName";
	
	public static final String SERVER_GUIDE_ID = "guideId";
	
	
	/* Parameters for uploading traveler vFunction traveller settings */
	public static final String TVT_MAX_AGE = "maxAge";
	public static final String TVT_MIN_AGE = "minAge";
	public static final String TVT_FEMALE_PERCENT = "femalePercent";
	public static final String TVT_TRAVELLER_ID = "travellerId";
	
	public static final String SERVER_TVT_ID = "tvtId";
	/* Parameters for uploading traveler vFunction guide settings */
	public static final String TVG_MAX_AGE = "maxAge";
	public static final String TVG_MIN_AGE = "minAge";
	public static final String TVG_FEMALE_PERCENT = "femalePercent";
	public static final String TVG_TRAVELLER_ID = "travellerId";
	
	public static final String SERVER_TVG_ID = "tvId";
	/* Parameters for uploading guide settings */
	public static final String GV_MAX_AGE = "maxAge";
	public static final String GV_MIN_AGE = "minAge";
	public static final String GV_FEMALE_PERCENT = "femalePercent";
	public static final String GV_GUIDE_ID = "guideId";
	
	public static final String SERVER_GV_ID = "gvId";
	
	/* used by for  ventoura */
	public static final String IS_A_MATCH = "isAMatch";
	
	/* used by create bookings */
	public static final String SERVER_BOOKING_ID = "bookingId";
	
	public static final String BOOKING_GUIDE_ID = "guideId";
	public static final String BOOKING_TRAVELLER_ID = "travellerId";
	public static final String BOOKING_GUIDE_FIRST_NAME = "guideFirstname";
	public static final String BOOKING_TRAVELLER_FIRST_NAME = "travellerFirstname";
	public static final String BOOKING_TOUR_DATE = "tourDate";
	public static final String BOOKING_TOUR_PRICE = "tourPrice";
	
	/* used by create traveller schedule */
	public static final String SERVER_TRAVELLER_SCHEDULE_ID = "travellerScheduleId";
	
	public static final String TRAVELLER_SCHEDULE_TRAVELLER_ID = "travellerId";
	public static final String TRAVELLER_SCHEDULE_START_DATE = "travellerScheduleStartDate";
	public static final String TRAVELLER_SCHEDULE_END_DATE = "travellerScheduleEndDate";
	public static final String TRAVELLER_SCHEDULE_CITY = "travellerScheduleCity";
	public static final String TRAVELLER_SCHEDULE_COUNTRY = "travellerScheduleCountry";
	
	/* used by create traveller schedule */
	public static final String SERVER_GUIDE_REVIEW_ID = "guideReviewId";
	
	public static final String GUIDE_REVIEW_TRAVELLER_ID = "travellerId";
	public static final String GUIDE_REVIEW_GUIDE_ID = "guideId";
	public static final String GUIDE_REVIEW_TRAVELLER_FIRSTNAME = "travellerFirstname";
	public static final String GUIDE_REVIEW_SCORE = "reviewScore";
	public static final String GUIDE_REVIEW_MESSAGE = "reviewMessage";
	
	
	/* used by create last updated */
	public static final String SERVER_LAST_UPDATED = "lastUpdated";
	
}
