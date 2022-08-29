//
//  MeetupCommunicator.h
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <FacebookSDK/FacebookSDK.h>
@protocol LoginCommunicatorDelegate;

@interface LoginCommunicator : NSObject
@property (weak, nonatomic) id<LoginCommunicatorDelegate> delegate;

-(void)searchTravellerById:(NSString*)travellerId;
-(void)searchUserByFacebookId:(NSString*)facebookId isGuide:(BOOL)isGuide;
-(void)searchUserByFacebookIdPost:(NSString*)facebookId isGuide:(BOOL)isGuide;

-(void)postPersonFacebookID:(id<FBGraphUser>)fbUser isGuide:(BOOL)isGuide country:(NSString*)country;
-(void)postPersonFacebookID:(id<FBGraphUser>)fbUser payByCard:(BOOL)payByCard country:(NSString*)country;


@end
