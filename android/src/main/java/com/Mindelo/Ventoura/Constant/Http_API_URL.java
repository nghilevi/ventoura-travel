package com.Mindelo.Ventoura.Constant;

public class Http_API_URL {
	/* URLS connect to server */
	public static final String URL_SERVER_ROOT = "http://54.191.27.22";
	//public static final String URL_SERVER_ROOT = "http://10.0.0.18";
	
	
	/*
	 * system
	 */
	public static final String URL_SERVER_TRAVELLER_LOGIN_PROBE = URL_SERVER_ROOT + "/system/login/traveller"; //GET + {facebookAccountName}
	public static final String URL_SERVER_GUIDE_LOGIN_PROBE = URL_SERVER_ROOT + "/system/login/guide"; //GET + {facebookAccountName}
	
	
	/*
	 * Traveller Profile
	 */
	public static final String URL_SERVER_UPLOAD_TRAVERLLER = URL_SERVER_ROOT + "/traveller/createNewTraveller";
	public static final String URL_SERVER_UPDATE_TRAVERLLER = URL_SERVER_ROOT + "/traveller/updateTraveller";
	public static final String URL_SERVER_GET_TRAVERLLER_PROFILE = URL_SERVER_ROOT + "/traveller/getTravellerProfile"; // GET METHOD + {travellerId}
	public static final String URL_SERVER_DEACTIVATE_TRAVERLLER = URL_SERVER_ROOT + "/traveller/deactivateTravller";  // GET METHOD + {travellerId}
	public static final String URL_SERVER_GET_TRAVELLER_MY_TRIP = URL_SERVER_ROOT + "/traveller/getTravellerTrip";
	public static final String URL_SERVER_GET_TRAVELLER_BOOKINGS = URL_SERVER_ROOT + "/traveller/getTravellerBookings";
	public static final String URL_SERVER_GET_TRAVELLER_SCHEDULE = URL_SERVER_ROOT + "/traveller/getTravellerSchedules";
	public static final String URL_SERVER_GET_TRAVELLER_MATCHES_LIST = URL_SERVER_ROOT + "/traveller/getAllMatches"; // GET METHOD + {travellerId}
	
	/*
	 * Traveller gallery
	 */
	public static final String URL_SERVER_GET_TRAVELLER_PORTALIMG = URL_SERVER_ROOT + "/traveller/getTravellerPortalGallery"; // GET METHOD + {travellerId}
	public static final String URL_SERVER_GET_TRAVELLER_GALLERY_IMAGES = URL_SERVER_ROOT + "/traveller/getAllGalleryImages"; // GET METHOD + {travellerId}
	public static final String URL_SERVER_UPLOAD_SINGLE_TRAVELLER_GALLERY = URL_SERVER_ROOT + "/traveller/uploadSingleTravellerGallery"; 
	public static final String URL_SERVER_DELETE_TRAVELLER_GALLERY = URL_SERVER_ROOT + "/traveller/deleteTravellerGallery"; //  GET METHOD + {galleryId}
	public static final String URL_SERVER_SET_TRAVELLER_PORTAL_GALLERY = URL_SERVER_ROOT + "/traveller/setTravellerPortalGallery"; //  GET METHOD + {travellerId} {galleryId}
	public static final String URL_SERVER_LOAD_TRAVELLER_MATCHES_IMAGES = URL_SERVER_ROOT + "/traveller/loadMatchImages"; // POST
	public static final String URL_SERVER_TRAVELLER_DELETE_BATCH_GALLERY = URL_SERVER_ROOT + "/traveller/deleteBatchGallery"; //  POST
	
	/*
	 * Guide Profile
	 */
	public static final String URL_SERVER_UPLOAD_GUIDE = URL_SERVER_ROOT + "/guide/createNewGuide";
	public static final String URL_SERVER_UPDATE_GUIDE = URL_SERVER_ROOT + "/guide/updateGuide";
	public static final String URL_SERVER_DEACTIVATE_GUIDE = URL_SERVER_ROOT + "/guide/deactivateGuide";  // GET METHOD + {guideId}
	public static final String URL_SERVER_GET_GUIDE_PROFILE = URL_SERVER_ROOT + "/guide/getGuideProfile"; // GET METHOD + {guideId}
	public static final String URL_SERVER_GET_GUIDE_BOOKINGS = URL_SERVER_ROOT + "/guide/getAllBookings"; // GET METHOD + {guideId}
	public static final String URL_SERVER_GUIDE_UPDATE_BOOKING_STATUS = URL_SERVER_ROOT + "/guide/updateBookingStatus"; // GET MEthod + {bookingId} + {statusCode}
	public static final String URL_SERVER_GET_GUIDE_MATCHES_LIST = URL_SERVER_ROOT + "/guide/getAllMatches"; // GET METHOD + {tuideId}
	public static final String URL_SERVER_LOAD_GUIDE_MATCHES_IMAGES = URL_SERVER_ROOT + "/guide/loadMatchImages"; // POST
	
	
	public static final String URL_SERVER_BATCH_UPLOAD_GUIDE_ATTRACTION =   URL_SERVER_ROOT + "/guide/batchCreateGuideAttraction";  // POST + {guideId}
	public static final String URL_SERVER_BATCH_DELETE_GUIDE_ATTRACTION =   URL_SERVER_ROOT + "/guide/deleteBatchGuideAttraction";  // POST
	
	public static final String URL_SERVER_UPLOAD_GUIDE_ATTRACTION = URL_SERVER_ROOT + "/guide/createGuideAttraction";  // POST
	public static final String URL_SERVER_UPLOAD_GUIDE_HIDDEN_TREASURE = URL_SERVER_ROOT + "/guide/createGuideHiddenTreasure";  // POST
	public static final String URL_SERVER_DELETE_GUIDE_ATTRACTION = URL_SERVER_ROOT + "/guide/deleteGuideAttraction";  // GET METHOD + {deleteGuideAttraction}
	public static final String URL_SERVER_DELETE_GUIDE_HIDDEN_TREASURE = URL_SERVER_ROOT + "/guide/deleteGuideHiddenTreasure";  // GET METHOD + {guideHiddenTreasureId}
	
	/*
	 * Guide Review
	 */
	public static final String URL_SERVER_GET_GUIDE_REVIEWS = URL_SERVER_ROOT + "/guide/guideReview/getGuideReviews"; // GET METHOD + {guideId}
	public static final String URL_SERVER_CREATE_GUIDE_REVIEWS = URL_SERVER_ROOT + "/guide/createGuideReview"; // POST method
	
	/*
	 * Guide gallery
	 */
	public static final String URL_SERVER_GET_GUIDE_PORTALIMG = URL_SERVER_ROOT + "/guide/getGuidePortalGallery"; // GET METHOD + {guideId}
	public static final String URL_SERVER_GET_GUIDE_GALLERY_IMAGES = URL_SERVER_ROOT + "/guide/getAllGalleryImages"; // GET METHOD + {tuideId}
	public static final String URL_SERVER_UPLOAD_SINGLE_GUIDE_GALLERY = URL_SERVER_ROOT + "/guide/uploadSingleGuideGallery"; 
	public static final String URL_SERVER_DELETE_GUIDE_GALLERY = URL_SERVER_ROOT + "/guide/deleteGuideGallery"; //  GET METHOD + {galleryId}
	public static final String URL_SERVER_SET_GUIDE_PORTAL_GALLERY = URL_SERVER_ROOT + "/guide/setGuidePortalGallery"; //  GET METHOD + {travellerId} {galleryId}
	public static final String URL_SERVER_GUIDE_DELETE_BATCH_GALLERY = URL_SERVER_ROOT + "/guide/deleteBatchGallery"; //  POST
	
	/*
	 * Traveller schedule and bookings
	 */
	public static final String URL_SERVER_TRAVELLER_UPDATE_BOOKING_STATUS = URL_SERVER_ROOT + "/traveller/updateBookingStatus"; // GET MEthod + {bookingId} + {statusCode}
	public static final String URL_SERVER_DELETE_TRAVELLER_SCHEDULE = URL_SERVER_ROOT + "/traveller/deleteTravellerSingleSchedule";  // GET METHOD + {scheduleId}
	public static final String URL_SERVER_LOAD_TRAVELLER_BOOKIN_CREATE_BOOKING = URL_SERVER_ROOT + "/traveller/createBooking"; // POST
	public static final String URL_SERVER_LOAD_TRAVELLER_CREATE_SCHEDULE = URL_SERVER_ROOT + "/traveller/createTravellerSchedule"; // POST
	
	/*
	 * V Function settings
	 */
	public static final String URL_SERVER_UPDATE_GUIDE_VFUNCTION_SETTINGS = URL_SERVER_ROOT + "/VFunctionSettings/updateGuideVFunctionSettings"; // POST
	public static final String URL_SERVER_UPDATE_TRAVELLER_VFUNCTION_SETTINGS = URL_SERVER_ROOT + "/VFunctionSettings/updateTravellerVFunctionSettings"; // POST
	public static final String URL_SERVER_GET_GUIDE_VFUNCTION_SETTINGS = URL_SERVER_ROOT + "/VFunctionSettings/getGuideVFunctionSettings"; // {guideId}
	public static final String URL_SERVER_GET_TRAVELLER_VFUNCTION_SETTINGS = URL_SERVER_ROOT + "/VFunctionSettings/getTravellerVFunctionSettings"; // +{travellerId}
	 
	
	
	/*
	 * Traveller ventoura actions
	 */
	public static final String URL_SERVER_GET_TRAVELLER_VENTOURA = URL_SERVER_ROOT + "/ventoura/traveller/getVentouraPackage"; //  GET METHOD + {travellerId}
	public static final String URL_SERVER_GET_GUIDE_VENTOURA = URL_SERVER_ROOT + "/ventoura/guide/getVentouraPackage"; //  GET METHOD + {guideId}
	public static final String URL_SERVER_GET_LOAD_VENTOURA_IMAGES = URL_SERVER_ROOT + "/ventoura/loadVentouraImages"; // POST
	
	public static final String URL_SERVER_TRAVELLER_JUDGE_TRAVELLER = URL_SERVER_ROOT + "/ventoura/traveller/judgeTraveller"; //  GET METHOD + {travellerId} +{likeornot} + {travellerId}
	public static final String URL_SERVER_TRAVELLER_JUDGE_GUIDE = URL_SERVER_ROOT + "/ventoura/traveller/judgeGuide"; //  GET METHOD + {travellerId} +{likeornot} + {guideId}
	public static final String URL_SERVER_GUIDE_JUDGE_TRAVELLER = URL_SERVER_ROOT + "/ventoura/guide/judgeTraveller"; //  GET METHOD + {guideId} +{likeornot} + {travellerId}

	
	
	
	/*
	 * Promotion URLs
	 */
	public static final String URL_SERVER_GET_PROMOTION_CITY_IMAGES =  URL_SERVER_ROOT + "/promotion/getPromotionImage/1"; //POST Method 4 cityIds  
	public static final String URL_SERVER_TRAVELLER_PROMOTION_PROBE =  URL_SERVER_ROOT + "/promotion/promotionProbe"; //GET + {travellerId}
	public static final String URL_SERVER_ADD_PROMOTION_TRAVELLER_CANDIDATE =  URL_SERVER_ROOT + "/promotion/addNewCandidate"; //POST + {travellerId}
	
	/*
	 * payment URLs
	 */
	public static final String URL_SERVER_PAYMENT_GET_CLIENT_TOKEN = URL_SERVER_ROOT + "/payment/braintree/client_token";
	public static final String URL_SERVER_PAYMENT_POST_NONCE_PURCHASE = URL_SERVER_ROOT + "/payment/braintree/purchases";
	
}
