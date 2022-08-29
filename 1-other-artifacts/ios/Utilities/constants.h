//
//  constants.h
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import <FacebookSDK/FacebookSDK.h>

#ifdef DEBUG
#   define NSLog(...) NSLog(__VA_ARGS__)
#else
#   define NSLog(...)
#endif

#ifndef Ventoura_constants_h
#define Ventoura_constants_h
//pre-defined Values
//#define ventouraBaseURL @"http://10.0.0.8/"

#define ventouraBaseURL @"http://54.191.27.22/"
#define ventouraXMPPHostname @"54.191.27.22"

#define emojiTextPadding @"*** "
#define emojiTextPaddingEmpty @"     "

#define ventouraScreenWidth 320

#define  _profileViewProfileCellHeight 290
#define  _profileViewNameLabelHeight 60
//padding General


//padding - Role selection Page
#define ventouraRoleSelectionTopPadding 50

#define  IPHONE4SCREENHEIGHT 480
#define  IPHONE5SCREENHEIGHT 568
#define  IPHONENAVBARHEIGHT 64
#define  ABOUT_ME_TEXT_LIMIT 200
#define  TAG_TEXT_LIMIT 20

//tour status

#define	REQUEST_BY_TRAVELLER 0
#define	REQUEST_CANCELLED_BY_TRAVELLER 1
#define	REQUEST_DECLINED_BY_LOCAL 2
#define	REQUEST_ACCEPTED_BY_LOCAL 3

#define	TOUR_CANCELLED_BY_TRAVELLER_BFRORE_BOOKING 4
#define	TOUR_CANCELLED_BY_GUIDE_BFRORE_BOOKING 5
#define	TOUR_TIME_OUT_BEFORE_BOOKING 6
#define	TOUR_BOOKED_BY_TRAVELLER 7
#define	TOUR_CHARGED_SUCCESSFULLY 8

#define	BOOKING_CANCELLED_BY_TRAVELLER_BEFORE_CHARGED 9
#define	BOOKING_CANCELLED_BY_TRAVELLER_AFTER_CHARGED 10
#define	BOOKING_AUTHORIZATION_FAILED 11

#define	BOOKING_CAPTURED_FAILED 12
#define	BOOKING_LAPSED_DUE_TO_PAYMENT_FAILURE 13
#define	BOOKING_CANCELLED_BY_LOCAL_BEFORE_CHARGED 14
#define	BOOKING_CANCELLED_BY_LOCAL_AFTER_CHARGED 15

#define	TOUR_REGISTERED 16
#define	TOUR_REVIEWED 17
#define	TOUR_DISPUTED 18
#define	TOUR_DELETED_BY_TRAVELLER 19
#define	TOUR_DELETED_BY_GUIDE 20


#define AD_SYSTEM_VERSION_GREATER_THAN_7 ([[[UIDevice currentDevice] systemVersion] compare:@"7" options:NSNumericSearch] == NSOrderedDescending)

#define AL_SPEED_KEY @"speed"
#define AL_ORIENTATION_KEY @"orientation"
#define AL_NAVIGATION_BAR_HIDDEN_KEY @"navigationBarHidden"
#define AL_TOOLBAR_HIDDEN_KEY @"toolbarHidden"

//global objects. they should be initialised.
NSString  *_facebookId;
NSString  *_userVentouraID;
NSString  *_deviceToken;



BOOL _userIsGuide;
BOOL _payByCard;

id<FBGraphUser> _fbUserObject;
UIImage *_fbUIImage;
#endif
