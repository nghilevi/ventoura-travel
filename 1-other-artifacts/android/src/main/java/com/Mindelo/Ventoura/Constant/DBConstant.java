package com.Mindelo.Ventoura.Constant;

public class DBConstant {

	// nightfury db name, save all the local caches
	public static final String VENTOURA_DB_NAME = "ventoura.db";

	// table names
	public static final String TABLE_GUIDE_PROFILE = "GUIDE_PROFILE";
	public static final String TABLE_GUIDE_ATTRACTION = "GUIDE_ATTACTION";
	public static final String TABLE_TRAVELLER_PROFILE = "TRAVELLER_PROFILE";
	public static final String TABLE_CHATTING_HISTORY = "CHATTING_HISTORY";
	public static final String TABLE_MATCH = "MATCHES";
	public static final String TABLE_BOOKINGS = "BOOKINGS";
	public static final String TABLE_TRAVELLER_SCHEDULE = "TRAVELLER_SCHEDULE";
	public static final String TABLE_GUIDE_UPDATED_LOG = "GUIDE_UPDATED_LOG";
	public static final String TABLE_TRAVELLER_UPDATED_LOG = "TRAVELLER_UPDATED_LOG";
	
	public static final String TABLE_TRAVELLER_PROFILE_GALLERY = "TABLE_TRAVELLER_PROFILE_GALLERY";
	public static final String TABLE_GUIDE_PROFILE_GALLERY = "TABLE_GUIDE_PROFILE_GALLERY";
	public static final String TABLE_MATCH_HEAD_IMAGE = "TABLE_MATCH_HEAD_IMAGE";
	
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS = "TABLE_TRAVELLER_VFUNCTION_SETTINGS";
	public static final String TABLE_GUIDE_VFUNCTION_SETTINGS = "TABLE_GUIDE_VFUNCTION_SETTINGS";

	public static final String TABLE_CITY = "City";
	public static final String TABLE_COUNTRY = "Country";

	// chatting history‘s fields
	public static final String TABLE_CHATTING_HISTORY_FIELD_ID = "id";
	public static final String TABLE_CHATTING_HISTORY_FIELD_USER_ID = "userId";
	public static final String TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID = "partnerId";
	public static final String TABLE_CHATTING_HISTORY_FIELD_USER_ROLE = "userRole";
	public static final String TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE = "partnerRole";
	public static final String TABLE_CHATTING_HISTORY_FIELD_DATETIME = "dateTime";
	public static final String TABLE_CHATTING_HISTORY_FIELD_ISREAD = "isRead";
	public static final String TABLE_CHATTING_HISTORY_FIELD_MINE = "mine";
	public static final String TABLE_CHATTING_HISTORY_FIELD_STATUS_MESSAGE = "statusMessage";
	public static final String TABLE_CHATTING_HISTORY_FIELD_MESSAGE_CONTENT = "messageContent";

	// matches fields
	public static final String TABLE_MATCH_FIELD_ID = "id";
	public static final String TABLE_MATCH_FIELD_OWNER_ID = "ownerId";
	public static final String TABLE_MATCH_FIELD_OWNER_ROLE = "ownerRole";
	public static final String TABLE_MATCH_FIELD_PARTNER_ID = "userId";
	public static final String TABLE_MATCH_FIELD_PARTNER_ROLE = "userRole";
	public static final String TABLE_MATCH_FIELD_PARTNER_FIRSTNAME = "userFirstname";
	public static final String TABLE_MATCH_FIELD_TIME_MATCHED = "timeMatched";
	public static final String TABLE_MATCH_FIELD_CITY = "city";
	public static final String TABLE_MATCH_FIELD_COUNTRY = "country";
	public static final String TABLE_MATCH_FIELD_GENDER = "gender";
	public static final String TABLE_MATCH_FIELD_AGE = "age";
	
	
	// match images
	public static final String TABLE_MATCH_HEAD_IMAGE_FIELD_ID = "id";
	public static final String TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID = "userId";
	public static final String TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE = "userRole";
	public static final String TABLE_MATCH_HEAD_IMAGE_FIELD_CONTENT = "content";

	// city fields
	public static final String TABLE_CITY_FIELD_ID = "id";
	public static final String TABLE_CITY_FIELD_CITY_NAME = "cityName";
	public static final String TABLE_CITY_FIELD_COUNTRY_ID = "countryId";
	public static final String TABLE_CITY_FIELD_COUNTRY_NAME = "countryName";

	// country fields
	public static final String TABLE_COUNTRY_FIELD_ID = "id";
	public static final String TABLE_COUNTRY_FIELD_COUNTRY_NAME = "countryName";

	// bookings fields
	public static final String TABLE_BOOKINGS_FIELD_ID = "id";
	public static final String TABLE_BOOKINGS_FIELD_TRAVELLER_ID = "travellerId";
	public static final String TABLE_BOOKINGS_FIELD_TRAVELLER_FIRSTNAME = "travellerFirstname";
	public static final String TABLE_BOOKINGS_FIELD_TRAVELLER_AGE = "travellerAge";
	public static final String TABLE_BOOKINGS_FIELD_TRAVELLER_GENDER = "travellerGender";
	
	public static final String TABLE_BOOKINGS_FIELD_GUIDE_ID = "guideId";
	public static final String TABLE_BOOKINGS_FIELD_GUIDE_FIRSTNAME = "guideFirstname";
	public static final String TABLE_BOOKINGS_FIELD_GUIDE_AGE = "guideAge";
	public static final String TABLE_BOOKINGS_FIELD_GUIDE_GENDER = "guideGender";
	
	public static final String TABLE_BOOKINGS_FIELD_BOOKING_STATUS = "bookingStatus";
	public static final String TABLE_BOOKINGS_FIELD_STATUS_LAST_UPDATED_DATE = "statusLastUpdatedDate";
	public static final String TABLE_BOOKINGS_FIELD_TOUR_PRICE = "tourPrice";
	public static final String TABLE_BOOKINGS_FIELD_TOUR_DATE = "tourDate";

	// traveller schedule fields
	public static final String TABLE_TRAVELLER_SCHEDULE_FIELD_ID = "id";
	public static final String TABLE_TRAVELLER_SCHEDULE_FIELD_TRAVELLER_ID = "travellerId";
	public static final String TABLE_TRAVELLER_SCHEDULE_FIELD_START_TIME = "startTime";
	public static final String TABLE_TRAVELLER_SCHEDULE_FIELD_END_TIME = "endTime";
	public static final String TABLE_TRAVELLER_SCHEDULE_FIELD_CITY = "city";
	public static final String TABLE_TRAVELLER_SCHEDULE_FIELD_COUNTRY = "country";

	// guide update log
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_ID = "id";
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID = "guideId";
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_PROFILE_UPDATED = "profileUpdated";
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_GALLERY_UPDATED = "galleryUpdated";
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_BOOKINGS_UPDATED = "bookingsUpdated";
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_REVIEWS_UPDATED = "reviewsUpdated";
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_MATCHES_UPDATED = "matchesUpdated";
	public static final String TABLE_GUIDE_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED = "vFunctionSettings";

	// traveller update log
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_ID = "id";
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID = "travellerId";
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_PROFILE_UPDATED = "profileUpdated";
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_GALLERY_UPDATED = "galleryUpdated";
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_TOURS_UPDATED = "toursUpdated";
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_BOOKINGS_UPDATED = "bookingsUpdated";
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_MATCHES_UPDATED = "matchesUpdated";
	public static final String TABLE_TRAVELLER_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED = "vFunctionSettings";

	// guide profile
	public static final String TABLE_GUIDE_PROFILE_FIELD_ID = "id";
	public static final String TABLE_GUIDE_PROFILE_FIELD_FIRSTNAME = "firstname";
	public static final String TABLE_GUIDE_PROFILE_FIELD_LASTNAME = "lastname";
	public static final String TABLE_GUIDE_PROFILE_FIELD_GENDER = "gender";
	public static final String TABLE_GUIDE_PROFILE_FIELD_AGE = "age";
	public static final String TABLE_GUIDE_PROFILE_FIELD_FACEBOOK = "facebookAccountName";
	public static final String TABLE_GUIDE_PROFILE_FIELD_COUNTRY = "country";
	public static final String TABLE_GUIDE_PROFILE_FIELD_CITY = "city";
	public static final String TABLE_GUIDE_PROFILE_FIELD_BIOGRAPHY = "biography";

	public static final String TABLE_GUIDE_PROFILE_FIELD_NUMBER_GALLERY = "numberOfGalleries";
	public static final String TABLE_GUIDE_PROFILE_FIELD_NUMBER_REVIEWS = "numberOfReviews";
	public static final String TABLE_GUIDE_PROFILE_FIELD_AVG_REVIEW_SCORE = "avgReviewScore";
	public static final String TABLE_GUIDE_PROFILE_FIELD_IS_PREMIUM = "isPremium";

	public static final String TABLE_GUIDE_PROFILE_FIELD_PAYMENT_METHOD = "paymentMethod";
	public static final String TABLE_GUIDE_PROFILE_FIELD_MONEY_TYPE = "moneyType";
	public static final String TABLE_GUIDE_PROFILE_FIELD_TOUR_LENGTH = "tourLength";
	public static final String TABLE_GUIDE_PROFILE_FIELD_TOUR_PRICE = "tourPrice";
	public static final String TABLE_GUIDE_PROFILE_FIELD_TOUR_TYPE = "tourType";

	// guide attraction
	public static final String TABLE_GUIDE_ATTRACTION_FIELD_ID = "id";
	public static final String TABLE_GUIDE_ATTRACTION_FIELD_GUIDE_ID = "guideId";
	public static final String TABLE_GUIDE_ATTRACTION_FIELD_NAME = "attractionName";

	// traveller profile
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_ID = "id";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_FIRSTNAME = "firstname";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_LASTNAME = "lastname";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_GENDER = "gender";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_AGE = "age";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_FACEBOOK = "facebookAccountName";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_COUNTRY = "country";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_CITY = "city";
	public static final String TABLE_TRAVELLER_PROFILE_FIELD_BIOGRAPHY = "biography";

	public static final String TABLE_TRAVELLER_PROFILE_FIELD_NUMBER_GALLERY = "numberOfGalleries";

	// traveller profile gallery images
	public static final String TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_ID = "id";
	public static final String TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_TRAVELLER_ID = "travellerId";
	public static final String TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_IS_PORTAL = "isPortal";
	public static final String TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_CONTENT = "imageContent";

	
	// guide profile gallery images
	public static final String TABLE_GUIDE_PROFILE_GALLERY_FIELD_ID = "id";
	public static final String TABLE_GUIDE_PROFILE_GALLERY_FIELD_GUIDE_ID = "guideId";
	public static final String TABLE_GUIDE_PROFILE_GALLERY_FIELD_IS_PORTAL = "isPortal";
	public static final String TABLE_GUIDE_PROFILE_GALLERY_FIELD_CONTENT = "imageContent";
	
	// traveller vfunction settings
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_TRAVELLER_ID = "travellerId";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER = "preferedGender";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_USER_ROLE = "preferedUserRole";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_AGE = "maxAge";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_AGE = "minAge";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_PRICE = "maxPrice";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_PRICE = "minPrice";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_TOGGLE = "specifyCityToggle";
	public static final String TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_IDS = "cityIds";
	
	// guide vfunction settings
	public static final String TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_GUIDE_ID = "guideId";
	public static final String TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER = "preferedGender";
	public static final String TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MAX_AGE = "maxAge";
	public static final String TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MIN_AGE = "minAge";
	public static final String TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_LAST_ACTIVE_DAYS = "lastActivateDays";
	

}
