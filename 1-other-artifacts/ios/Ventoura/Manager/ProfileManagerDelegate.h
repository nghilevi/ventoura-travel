//
//  ventouraPackageManagerDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"
@protocol ProfileManagerDelegate <NSObject>
- (void)didReceivePersonProfile:(Person *)personProfile;
- (void)didReceiveTravellerProfile:(Person *)personProfile;
- (void)didReceiveCreatedTravellerProfile:(Person *)personProfile;


- (void)didReceiveGuideProfile:(Person *)personProfile;
- (void)didReceiveCreatedGuideProfile:(Person *)personProfile;

- (void)didReceiveTravellerProfileImages:(NSArray *) imageArray;
- (void)didReceiveTravllerImage;
- (void)didReceiveGuideProfileImages:(NSArray *) imageArray;

- (void)fetchingProfileFailedWithError:(NSError *)error;
-(void) didReceiveImageId:(NSString*)imageId;
-(void) didReceiveImageId:(NSString*)imageId isPortal:(BOOL)isPortal;

-(void)receivedGuideUpdateAttractions;
-(void)receivedGuideDeleteAttractions;
-(void)receivedGuideUpdateSecrets;
-(void)receivedGuideDeleteSecrets;

//-(void)receivedPostUploadUserImages;
-(void)receivedDeleteUserImages;
-(void)receivedUpdateGuideProfile;
-(void)receivedUpdateTravellerProfile;
-(void)receivedSetPortalImage;

@end
