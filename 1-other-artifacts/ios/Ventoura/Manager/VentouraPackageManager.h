//
//  VentouraPackageManager.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VentouraPackageCommunicatorDelegate.h"
#import "VentouraPackageManagerDelegate.h"
#import "Person.h"
@class VentouraPackageCommunicator;

@interface VentouraPackageManager : NSObject<VentouraPackageCommunicatorDelegate>
@property (strong, nonatomic) VentouraPackageCommunicator *communicator;
@property (weak, nonatomic) id<VentouraPackageManagerDelegate> delegate;

- (void)fetchVentouraPackage;
- (void)fetchVentouraPackageImages: (NSArray *) ventouraPackage;
- (void)likeOrNot: (Person *) currentPerson likeOrNotValue:(BOOL)likeOrNot;

@end
