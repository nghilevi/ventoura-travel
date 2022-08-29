//
//  VentouraPakcageCommunicator.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"
@protocol BuddyListCommunicatorDelegate;

@interface BuddyListCommunicator : NSObject
@property (weak, nonatomic) id<BuddyListCommunicatorDelegate> delegate;
- (void)getBuddyList;
- (void)deleteBuddyByUserId:(NSString*)userId isUserGuide:(bool)isUserGuide;
//- (void)getVentouraPackageImages:(NSArray *)ventouraPackage;
//- (void)getLikeOrNotResult:(Person *) currentPerson likeOrNot:(BOOL)likeOrNot;

@end
