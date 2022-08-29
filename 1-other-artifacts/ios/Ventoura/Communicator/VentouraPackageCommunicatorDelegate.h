//
//  VentouraPackageCommunicatorDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol VentouraPackageCommunicatorDelegate <NSObject>
- (void)receivedPackageJSON:(NSData *)objectNotation;
- (void)receivedLikeOrNotJSON:(NSData *)objectNotation;
- (void)receivedLikeOrNotJSON:(NSData *)objectNotation withName:(NSString*)name;

- (void)fetchingPackageJSONFailedWithError:(NSError *)error;
- (void)receivedPackageImageZip:(NSData *)objectNotation packageWithNoImage:(NSArray*)ventouraPackage;

@end
