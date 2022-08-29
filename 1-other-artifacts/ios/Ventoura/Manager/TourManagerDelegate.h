//
//  ventouraPackageManagerDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"
@protocol TourManagerDelegate <NSObject>
- (void)didReceiveCreateTourResult:(Person *)tourResult;
- (void)didReceiveCreateTripResult:(BOOL)didCreate;
- (void)didReceiveTourTripObject:(NSArray*) tourTripObjects;
- (void)fetchingTourFailedWithError:(NSError *)error;
@end
