//
//  TravellerManager.h
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LoginManagerDelegate.h"
#import "LoginCommunicatorDelegate.h"
#import <FacebookSDK/FacebookSDK.h>
#import "TravellerBuilder.h"
#import "LoginCommunicator.h"

@class LoginCommunicator;

@interface LoginManager : NSObject<LoginCommunicatorDelegate>
@property (strong, nonatomic) LoginCommunicator *communicator;
@property (weak, nonatomic) id<LoginManagerDelegate> delegate;

- (void)fetchTravellerById:(NSString*)TravellerId;
- (void)fetchLoginByFacebookId:(NSString*)facebookId isGuide:(BOOL)isGuide;
- (void)fetchLoginByFacebookIdWithDeviceToken:(NSString*)facebookId isGuide:(BOOL)isGuide;
- (void)createVentouraAccountByFacebookId:(id<FBGraphUser>) fbUser isGuide:(BOOL)isGuide country:(NSString*)country;
- (void)createVentouraAccountByFacebookId:(id<FBGraphUser>) fbUser payByCard:(BOOL)payByCard country:(NSString*)country;

@end
