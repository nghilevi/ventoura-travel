//
//  Trip.m
//  Ventoura
//
//  Created by Wenchao Chen on 30/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "Trip.h"

@implementation Trip

-(instancetype) initWithTripId:(NSString *)tripId country:(NSString *)country city:(NSString *)city travellerId:(NSString *)travellerId endTime:(NSString *)endTime startTime:(NSString *)startTime numberOfDays:(NSString *)numberOfDays{
    self = [super init];
    if (self) {
        _tripId = tripId;
        _country = country;
        _city = city;
        _travellerId = travellerId;
        _endTime = endTime;
        _startTime = startTime;
        _numberOfDays = numberOfDays;
    }

    return self;
}
@end
