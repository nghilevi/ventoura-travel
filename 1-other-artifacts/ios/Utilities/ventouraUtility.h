//
//  ventouraUtility.h
//  Ventoura
//
//  Created by Wenchao Chen on 9/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ventouraUtility : NSObject

+(UIImageView*)returnLoadingAnimation;
+(UIImageView*)returnImageLoadingAnimation:(UIView*)superView;
+(UIColor*) ventouraPink;
+(UIColor*) ventouraMenuPink;
+(UIColor*) ventouraBlue;
+(UIColor*) ventouraTitleColour;
+(UIColor*) ventourasettingsPaddingColour;
+(UIColor*)ventouraSettingLegalTextColour;
+(UIColor*) ventouraNavBackgroundColour;
+(UIColor*)ventouraTextBodyGrey;
+(UIColor*)ventouraTextBodyGrey2;
+(UIColor*)ventouraTextBodyGreyProfileLabel;
+(UIColor*)ventouraStarGreyColour;
+(UIColor*)ventouraTextHeadingGrey;
+(UIColor*)ventouraTextBodyGreyWithA;
+(UIColor*)ventouraPromotionGrey;
+(UIColor*)ventouraPinkAlpha;
+(UIColor*)ventouraDarkGreyAlpha;
+(UIColor*)ventouraSettingTextColour;
+(NSString*) ventouraDateToStringForTours: (NSDate*) date;
+(NSString *) ventouraDateToStringForTripsStartAt:(NSString*)startTime endAt:(NSString*)endTime;
+(NSString*) ventouraDateToStringForCreateTrip: (NSDate*) date;
+(NSString*) ventouraDateToStringForPost: (NSDate*) date;
+(NSString*) ventouraDateToStringForProfilePost: (NSDate*) date;
+(NSString*) ventouraDateToStringForTourPost: (NSDate*) date;
+(UIImage*)getLocalProfileImageWithID:(NSString*) localId;
+(UIImage*)getTravellerProfileImageWithID:(NSString*) localId;
+(UIImage*)imageWithImage: (UIImage*) sourceImage scaledToWidth: (float) i_width;
+(UIImage*)imageWithImage: (UIImage*) sourceImage scaledToHeight: (float) i_height;
+(UIImage*)imageCropSquareCentre:(UIImage*)original;
+(CGFloat)getScreenHeight;
+(BOOL) isUserLoggedin;
+(BOOL) isUserGuide;
+(BOOL) isUserGuide:(NSString*) userRole;
+(NSString*) returnMyUserIdWithType;
+(NSString*) returnUserIdWithType:(BOOL)isUserGuide ventouraId:(NSString*)ventouraId;
+(NSString*) returnMyUserId;
+(NSString*) returnMyFacbeookId;
+(NSString*) returnMyUserFirstName;
+(NSString*) returnImagePathPrefixIsUserGuide:(BOOL)isUserGuide ventouraId:(NSString*)ventouraId;

+(NSString*) returnImagePathIsUserGuide:(BOOL)isUserGuide ventouraId:(NSString*)ventouraId imageId:(NSString*)imageId;

+(void) checkImagePathUserGuide:(BOOL)isUserGuide ventouraId:(NSString*)ventouraId;
+(void) deleteLocalImageBy:(NSString*) imageId ventouraId:(NSString*)ventouraId isUserGuide:(BOOL)isUserGuide;

+(NSString*) returnImageIdFromImagePath:(NSString*) imagePath;
+(void) userLogOut;
+(void) saveUserInfo;
+(UIImage *)imageByApplyingAlpha:(CGFloat)alpha withImage:(UIImage*) image;
+ (NSString *) escapeStringSQL:(NSString *)string;
+(void)saveFacebookImage:(NSString *) imageId;
+(CGSize)sizeWithImage: (UIImage*) sourceImage scaledToHeight: (float) i_height;
+ (UIImage *)resizeImage:(UIImage*)image newSize:(CGSize)newSize;
@end
