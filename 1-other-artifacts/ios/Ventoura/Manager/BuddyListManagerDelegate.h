//
//  ventouraPackageManagerDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol BuddyListManagerDelegate <NSObject>
- (void)didReceiveBuddyList:(NSArray *)ventouraPackage;
- (void)didReceiveDeleteBuddyUserId:(NSString*)userId isUserGuide:(BOOL)isUserGuide;

//- (void)didReceiveVentouraPakcageWithImage:(NSArray *)ventouraPackage;
- (void)fetchingBuddyListFailedWithError:(NSError *)error;
@end
