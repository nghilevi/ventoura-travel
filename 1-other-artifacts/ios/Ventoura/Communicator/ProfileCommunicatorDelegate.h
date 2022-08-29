//
//  VentouraPackageCommunicatorDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ProfileCommunicatorDelegate <NSObject>
- (void)receivedGuideProfileJSON:(NSData *)objectNotation;
- (void)receivedGuideCreatedProfileJSON:(NSData *)objectNotation;

- (void)receivedGuideProfileImagesZip:(NSData*)objectNotation ventouraId:ventouraId;
- (void)receivedTravellerProfileImagesZip:(NSData*)objectNotation ventouraId:ventouraId;

- (void)receivedTravellerProfileJSON:(NSData *)objectNotation;
- (void)receivedTravellerCreatedProfileJSON:(NSData *)objectNotation;

//- (void)receivedProfileJSON:(NSData *)objectNotation;
- (void)receivedImageUplpoadJSON:(NSData *) objectNotation imageData:(UIImage*)imageData isPortal:(BOOL)isPoral;


- (void)fetchingProfileJSONFailedWithError:(NSError *)error;


-(void)receivedPostGuideUpdateAttractions;
-(void)receivedPostGuideDeleteAttractions;

-(void)receivedPostUploadUserImages;
-(void)receivedPostDeleteUserImages;

-(void)receivedPostUpdateGuideProfile;
-(void)receivedPostUpdateTravellerProfile;
-(void)receivedSetPortalImage;

- (void)receivedTravellerProfileImage:(NSData*)objectNotation ventouraId:(NSString*)ventouraId isGuide:(BOOL)isGuide imageId:(NSString*)imageId;
- (void)receivedGuideProfileImage:(NSData*)objectNotation ventouraId:(NSString*)ventouraId isGuide:(BOOL)isGuide imageId:(NSString*)imageId;


//- (void)receivedPackageImageZip:(NSData *)objectNotation packageWithNoImage:(NSArray*)ventouraPackage;

@end
