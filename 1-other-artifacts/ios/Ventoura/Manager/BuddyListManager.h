//
//  VentouraPackageManager.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BuddyListCommunicatorDelegate.h"
#import "BuddyListManagerDelegate.h"
#import "Person.h"
@class BuddyListCommunicator;

@interface BuddyListManager : NSObject<BuddyListCommunicatorDelegate>
@property (strong, nonatomic) BuddyListCommunicator *communicator;
@property (weak, nonatomic) id<BuddyListManagerDelegate> delegate;

- (void)fetchBuddyList;
- (void)postDeleteBuddyById:(NSString*)userId isUserGuide:(BOOL)isUserGuide;


@end
