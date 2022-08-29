//
//  ventouraDatabaseUtility.h
//  Ventoura
//
//  Created by Wenchao Chen on 16/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DBManager.h"
@interface ventouraDatabaseUtility : NSObject
//userProfiles
+(NSArray*) userDataFromDatabase:(DBManager*)dbManager userId:(NSString*) userId;


+(void) saveTravellerProfileDataFromDatabase:(DBManager*)dbManager userId:(NSString*) userId name:(NSString*)name city:(NSString*)city country:(NSString*)country age:(NSString*)age textBio:(NSString*)textBio;


+(void) saveGuideProfileDataFromDatabase:(DBManager*)dbManager userId:(NSString*) userId name:(NSString*)name city:(NSString*)city country:(NSString*)country age:(NSString*)age textBio:(NSString*)textBio useravgReviewScore:(NSString*)useravgReviewScore tourLength:(NSString*)tourLength tourPrice:(NSString*)tourPrice paymentMethod:(NSString*)paymentMethod tourType:(NSString*)tourType;

//Attractions

+(NSArray*) userAttractionsFromDatabase:(DBManager*)dbManager userId:(NSString*) userId;

+(void) saveAttractions:(DBManager*)dbManager attractionId:(NSString*)attractionId userId:(NSString*)userId attractionName:(NSString*)attractionName;

+(void) deleteAttractions:(DBManager*)dbManager attractionId:(NSString*)attractionId;
//maybe add a flush all?
+(void) flushAttractionData:(DBManager*)dbManager userId:(NSString*)userId;


//Images

//TODO change this name to just UserImageData
+(NSArray*) userImageDataFromDatabase:(DBManager*)dbManager ownerId:(NSString*) ownerId userId:(NSString*) userId isUserGuide:(BOOL)isUserGuide;

+(void) saveTravellerProfileImageDataToDatabase:(DBManager*)dbManager ownerId:(NSString*)ownerId userId:(NSString*) userId imgId:(NSString*)imgId isUserGuide:(BOOL)isUserGuide isPortal:(BOOL)isPortal;

+(void) deleteImageData:(DBManager*)dbManager ownerId:(NSString*)ownerId imageId:(NSString*)imageId userId:(NSString*)userId isUserGuide:(BOOL)isUserGuide;

+(void) flushImageData:(DBManager*)dbManager ownerId:(NSString*)ownerId userId:(NSString*)userId isUserGuide:(BOOL)isUserGuide;


//Message List //logic, fetch list, compare with local database.




+(void) saveMatchListUser:(DBManager*)dbManager ownerId:(NSString*)ownerId userId:(NSString*)userId userName:(NSString *)userName matchTime:(NSString*)matchTime;

+(NSArray*) lastMessageFromDatabase:(DBManager*)dbManager ownerId:(NSString*)ownerId userId:(NSString*)userId;
+(NSArray*) buddyListFromDatabase:(DBManager*)dbManager ownerId:(NSString*)ownerId;

//Promotion
+(void) savePromotion:(DBManager*) dbManager ownerId:(NSString*)ownerId;

+(NSArray*) unreadCountFromDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId userId:(NSString*)userId;
+(void) updateUnreadCount:(DBManager*) dbManager ownerId:(NSString*)ownerId userId:(NSString*)userId;

+(void) saveMessageToDatabase:(DBManager*)  dbManager ownerId:(NSString*)ownerId userId:(NSString*)userId msg:(NSString*)msg;

//filter settings save

//gender can be females, both, males
//userType can be travellers, both, locals
//cities will be stored in a one way linking table, one user to many cities.
+(void) saveTravellerFilterSettingsToDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId  isUserGuide:(BOOL)isUserGuide gender:(NSString*)gender userType:(NSString*)userType ageMin:(NSString*)ageMin ageMax:(NSString*)ageMax priceMin:(NSString*)priceMin priceMax:(NSString*)priceMax cities:(NSArray*)cities;

+(void) saveGuideFilterSettingsToDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId isUserGuide:(BOOL)isUserGuide gender:(NSString*)gender ageMin:(NSString*)ageMin ageMax:(NSString*)ageMax autoMatch:(BOOL)autoMatch;


+(NSArray*) getFilterSettingsFromDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId isUserGuide:(BOOL)isUserGuide;
+(NSArray*) getCityFromDatabase:(DBManager*) dbManager cityId:(NSString*)cityId;
+(NSArray*) getTourTypeFromDatabase:(DBManager*) dbManager tourTypeId:(NSString*)tourType;
+(NSArray*) getFilterSettingsCitySelectionFromDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId isUserGuide:(BOOL)isUserGuide;
/*
+(NSArray*) getTravellerScheduleFromDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId;

+(void) saveTravellerScheduleToDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId city:(NSString*)city county:(NSString*)country startTime:(NSString*)startTime endTime:(NSString*)endTime ownerId:(NSString*)ownerId;
*/
@end
