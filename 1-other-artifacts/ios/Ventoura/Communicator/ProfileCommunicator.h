//
//  VentouraPakcageCommunicator.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"
#import "ProfileCommunicatorDelegate.h"
#import "UserImage.h"
#import "Attraction.h"
@protocol ProfileCommunicatorDelegate;

@interface ProfileCommunicator : NSObject
@property (weak, nonatomic) id<ProfileCommunicatorDelegate> delegate;
- (void)getUserProfile: (BOOL)isGuide ventouraId:(NSString*)ventouraId;
- (void)getUserCreatedProfile: (BOOL)isGuide ventouraId:(NSString*)ventouraId;


- (void)getUserProfileImages:(BOOL)isGuide ventouraId:(NSString*)ventouraId;
- (void)postImage:(BOOL)isGuide ventouraId:(NSString *)ventouraId image:(UIImage *)image isPortal:(BOOL)isPortal;
- (void)postProfileUpdate:(NSString*) ventouraId textBiography:(NSString*)textBiography country:(NSString*)country;
- (void)postGuideProfileUpdate:(NSString*)ventouraId textBiography:(NSString*)textBiography tourLength:(NSString*)tourLength tourPrice:(NSString*)tourPrice city:(NSString*)city country:(NSString*)country tourType:(NSString*)tourType;
- (void)postDeleteUserImages:(NSArray*)deletedImages isUserGuide:(BOOL)isUserGuide;


- (void)postGuideAttractions:(NSArray*)attractionsArrDisplay;
- (void)postDeleteGuideAttractions:(NSArray*)attractionsArrDeleted;

- (void)postPortalImageId:(NSString*)imageId withUserId:(NSString *)ventouraId isUserGuide:(BOOL)isUserGuide;

-(void)getUserProfileImageWithId:(NSString*)imageId isGuide:(BOOL)isGuide ventouraId:(NSString*)ventouraId imagePosition:(NSUInteger)imagePosition;
@end
