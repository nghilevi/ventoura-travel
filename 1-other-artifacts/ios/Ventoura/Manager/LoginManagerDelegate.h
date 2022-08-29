//
//  TravellerManagerDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"

@protocol LoginManagerDelegate 
- (void)didReceivePersonForLogin:(Person *)person;
- (void)didReceiveTravellerCreate:(Person *)person;
- (void)fetchingLoginFailedWithError:(NSError *)error;
- (void)didReceiveTokenPost;

@end
