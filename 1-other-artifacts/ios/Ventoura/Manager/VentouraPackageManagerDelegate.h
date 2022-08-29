//
//  ventouraPackageManagerDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol VentouraPackageManagerDelegate <NSObject>
- (void)didReceiveVentouraPakcage:(NSArray *)ventouraPackage;
- (void)didReceiveVentouraPakcageWithImage:(NSArray *)ventouraPackage;
- (void)didReceiveVentouraMatch:(BOOL)isMatch;
- (void)didReceiveVentouraMatch:(BOOL)isMatch withName:(NSString*)name;

- (void)fetchingVentouraPackageFailedWithError:(NSError *)error;
@end
