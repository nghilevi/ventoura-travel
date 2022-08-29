//
//  VentouraPackageCommunicatorDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol TourCommunicatorDelegate <NSObject>
- (void)receivedTourJSON:(NSData *)objectNotation;
- (void)receivedTripAndTourJson:(NSData *)objectNotation;
- (void)receivedTourJson:(NSData *)objectNotation;
- (void)receivedTourByIdJson:(NSData *)objectNotation;

- (void)receivedTripCreateJSON:(NSData *)objectNotation;
- (void)fetchingTourJSONFailedWithError:(NSError *)error;

@end
