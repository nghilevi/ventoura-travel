//
//  Trip.h
//  Ventoura
//
//  Created by Wenchao Chen on 30/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Trip : NSObject
@property (nonatomic, copy) NSString *tripId;
@property (nonatomic, copy) NSString *country;
@property (nonatomic, copy) NSString *city;
@property (nonatomic, copy) NSString *travellerId;
@property (nonatomic, copy) NSString *endTime;
@property (nonatomic, copy) NSString *startTime;
@property (nonatomic, copy) NSString *numberOfDays;

- (instancetype)initWithTripId:(NSString *)tripId
                       country:(NSString *)country
                          city:(NSString *)city
                   travellerId:(NSString *)travellerId
                       endTime:(NSString *)endTime
                     startTime:(NSString *)startTime
                  numberOfDays:(NSString *)numberOfDays;

@end
