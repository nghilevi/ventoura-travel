//
//  VentouraPackageManager.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "TourCommunicatorDelegate.h"
#import "TourManagerDelegate.h"
#import "Person.h"
@class TourCommunicator;

@interface TourManager : NSObject<TourCommunicatorDelegate>
@property (strong, nonatomic) TourCommunicator *communicator;
@property (weak, nonatomic) id<TourManagerDelegate> delegate;

- (void)createBookingWithGuideId:(NSString*)guideId travellerId:(NSString*)travellerId guideName:(NSString*)guideName travellerName:(NSString*)travellerName tourPrice:(NSString*)tourPrice tourLength:(NSString*)tourLength tourDate:(NSDate*)tourDate city:(NSString*)city tourType:(NSString*)tourType;

- (void)fetchTripAndTour;
- (void)fetchTours;
- (void)fetchTourById:(NSString*)tourId;
- (void)createTripForCity:(NSString*)cityId withStartDate:(NSDate*)startDate andEndDate:(NSDate*)endDate countryId:(NSString*) countryId;
@end
