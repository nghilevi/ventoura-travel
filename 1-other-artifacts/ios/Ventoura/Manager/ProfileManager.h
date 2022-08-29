//
//  VentouraPackageManager.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ProfileCommunicatorDelegate.h"
#import "ProfileManagerDelegate.h"
#import "Person.h"
@class ProfileCommunicator;

@interface ProfileManager : NSObject<ProfileCommunicatorDelegate>
@property (strong, nonatomic) ProfileCommunicator *communicator;
@property (weak, nonatomic) id<ProfileManagerDelegate> delegate;

-(void)fetchUserProfile: (BOOL)isGuide ventouraId:(NSString*)ventouraId;
-(void)fetchCreatedUserProfile: (BOOL)isGuide ventouraId:(NSString*)ventouraId;

-(void)fetchUserProfileImages: (BOOL)isGuide ventouraId:(NSString*)ventouraId;
-(void)fetchUserProfileImageWithId:(NSString*)imageId isGuide:(BOOL)isGuide ventouraId:(NSString*)ventouraId imagePosition:(NSUInteger)imagePosition;

-(void)createNewImage:(BOOL)isGuide ventouraId:(NSString*)ventouraId image:(UIImage*)image isPortal:(BOOL)isPortal;
-(void)updateTravellerProfile:(NSString*)ventouraId textBiography:(NSString*)textBiography country:(NSString*)country;
-(void)updateGuideProfile:(NSString*)ventouraId textBiography:(NSString*)textBiography tourLength:(NSString*)tourLength tourPrice:(NSString*)tourPrice city:(NSString*)city country:(NSString*)country tourType:(NSString*)tourType;
-(void)deleteUserImages:(NSArray*)deleteImages isUserGuide:(BOOL)isUserGuide;

-(void)updateGuideAttractions:(NSArray*)attractionsArrDisplay;
-(void)deleteGuideAttractions:(NSArray*)attractionsArrDeleted;

- (void)setPortalImageId:(NSString*)imageId withUserId:(NSString *)ventouraId isUserGuide:(BOOL)isUserGuide;


@end
