//
//  VentouraPackageCommunicatorDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol BuddyListCommunicatorDelegate <NSObject>
- (void)receivedBuddyJSON:(NSData *)objectNotation;
//- (void)receivedLikeOrNotJSON:(NSData *)objectNotation;
- (void)receivedDeleteBuddyJSON:(NSData *)objectNotation userId:(NSString*)userId isUserGuide:(BOOL)isUserGuide;

- (void)fetchingBuddyListJSONWithError:(NSError *)error;

//- (void)receivedPackageImageZip:(NSData *)objectNotation packageWithNoImage:(NSArray*)ventouraPackage;

@end
