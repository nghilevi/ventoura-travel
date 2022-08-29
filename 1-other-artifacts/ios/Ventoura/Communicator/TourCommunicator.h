//
//  VentouraPakcageCommunicator.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"
#import "TourCommunicatorDelegate.h"
#include "Person.h"
@protocol TourCommunicatorDelegate;

@interface TourCommunicator : NSObject
@property (weak, nonatomic) id<TourCommunicatorDelegate> delegate;
- (void)postBookingWithGuideId:(NSString*)guideId travellerId:(NSString*)travellerId guideName:(NSString*)guideName travellerName:(NSString*)travellerName tourPrice:(NSString*)tourPrice  tourLength:(NSString*)tourLength tourDate:(NSDate*)tourDate city:(NSString*)city tourType:(NSString*)tourType;
- (void)getTripAndTourJson;
- (void)getTourJson;
- (void)getTourJsonById:(NSString*)tourId;

-(void)postTripForCity:(NSString*)cityId withStartDate:(NSDate*)startDate andEndDate:(NSDate*)endDate countryId:(NSString*) countryId;
@end
