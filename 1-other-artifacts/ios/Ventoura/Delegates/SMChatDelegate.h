//
//  SMChatDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 30/06/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import <UIKit/UIKit.h>


@protocol SMChatDelegate


- (void)newBuddyOnline:(NSString *)buddyName;
- (void)buddyWentOffline:(NSString *)buddyName;
//- (void)didDisconnect;


@end
