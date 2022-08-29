//
//  Tour.h
//  Ventoura
//
//  Created by Wenchao Chen on 30/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Tour : NSObject
@property (nonatomic, copy) NSString *tourId;
@property (nonatomic, copy) NSString *travellerFirstname;
@property (nonatomic, copy) NSString *travellerId;
@property (nonatomic, copy) NSString *guideId;
@property (nonatomic, copy) NSString *guideFirstname;
@property (nonatomic, copy) NSString *tourPrice;
@property (nonatomic, copy) NSString *bookingStatus;
@property (nonatomic, copy) NSString *tourDate;
//list of property for the tours page
@property (nonatomic, copy) NSString *isTour; //0
@property (nonatomic, copy) NSString *country;
@property (nonatomic, copy) NSString *city;
@property (nonatomic, copy) NSString *numberOfDays;
@property (nonatomic, copy) NSString *endTime;
@property (nonatomic, copy) NSString *startTime;
@property (nonatomic, copy) NSDate *dateForSorting;
@property (nonatomic, copy) NSString *statusLastUpdatedTime;
@property (nonatomic, copy) NSString *tourType;



- (instancetype)initWithTourId:(NSString *)tourId
            travellerFirstname:(NSString *)travellerFirstname
                   travellerId:(NSString*)travellerId
                       guideId:(NSString*)guideId
                guideFirstname:(NSString*)guideFirstname
                     tourPrice:(NSString*)tourPrice
                 bookingStatus:(NSString*)bookingStatus
                      tourDate:(NSString*)tourDate;

- (instancetype)initTourForListViewWithTourId:(NSString *)tourId
                           travellerFirstname:(NSString *)travellerFirstname
                                  travellerId:(NSString*)travellerId
                                      guideId:(NSString*)guideId
                               guideFirstname:(NSString*)guideFirstname
                                    tourPrice:(NSString*)tourPrice
                                bookingStatus:(NSString*)bookingStatus
                                     tourDate:(NSString*)tourDate
                                       isTour:(NSString*)isTour
                               dateForSorting:(NSDate*) dateForSorting
                                         city:(NSString*)city
                                     tourType:(NSString*)tourType
                        statusLastUpdatedTime:(NSString*)statusLastUpdatedTime;

-(instancetype) initTripForListViewWithTourId:(NSString *)tourId
                                  travellerId:(NSString *)travellerId
                                         city:(NSString *)city
                                      country:(NSString *)country
                                      endTime:(NSString *)endTime
                                    startTime:(NSString *)startTime
                                 numberOfDays:(NSString *)numberOfDays
                                       isTour:(NSString *)isTour
                               dateForSorting:(NSDate *)dateForSorting;

@end
