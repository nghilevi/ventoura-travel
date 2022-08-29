//
//  Tour.m
//  Ventoura
//
//  Created by Wenchao Chen on 30/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "Tour.h"

@implementation Tour
-(instancetype) initWithTourId:(NSString *)tourId
            travellerFirstname:(NSString *)travellerFirstname
                   travellerId:(NSString *)travellerId
                       guideId:(NSString *)guideId
                guideFirstname:(NSString *)guideFirstname
                     tourPrice:(NSString *)tourPrice
                 bookingStatus:(NSString *)bookingStatus
                      tourDate:(NSString *)tourDate{

    self = [super init];
    if (self) {
        _tourId = tourId;
        _travellerFirstname = travellerFirstname;
        _travellerId = travellerId;
        _guideId = guideId;
        _guideFirstname = guideFirstname;
        _tourPrice = tourPrice;
        _bookingStatus = bookingStatus;
        _tourDate = tourDate;
    }
    return self;

}

-(instancetype) initTourForListViewWithTourId:(NSString *)tourId
                           travellerFirstname:(NSString *)travellerFirstname
                                  travellerId:(NSString *)travellerId
                                      guideId:(NSString *)guideId
                               guideFirstname:(NSString *)guideFirstname
                                    tourPrice:(NSString *)tourPrice
                                bookingStatus:(NSString *)bookingStatus
                                     tourDate:(NSString *)tourDate
                                       isTour:(NSString *)isTour
                               dateForSorting:(NSDate *)dateForSorting
                                         city:(NSString *)city
                                     tourType:(NSString *)tourType
                        statusLastUpdatedTime:(NSString *)statusLastUpdatedTime
{
    
    self = [super init];
    if (self) {
        _tourId = tourId;
        _travellerFirstname = travellerFirstname;
        _travellerId = travellerId;
        _guideId = guideId;
        _guideFirstname = guideFirstname;
        _tourPrice = tourPrice;
        _bookingStatus = bookingStatus;
        _tourDate = tourDate;
        _isTour = isTour;
        _dateForSorting = dateForSorting;
        _city = city;
        _tourType = tourType;
        _statusLastUpdatedTime = statusLastUpdatedTime;

    }
    return self;
    
}
-(instancetype) initTripForListViewWithTourId:(NSString *)tourId
                                  travellerId:(NSString *)travellerId
                                         city:(NSString *)city
                                      country:(NSString *)country
                                      endTime:(NSString *)endTime
                                    startTime:(NSString *)startTime
                                 numberOfDays:(NSString *)numberOfDays
                                       isTour:(NSString *)isTour
                               dateForSorting:(NSDate *)dateForSorting{
    self = [super init];
    if (self) {
        _tourId = tourId;
        _travellerId  = travellerId;
        _city = city;
        _country = country;
        _endTime = endTime;
        _startTime = startTime;
        _numberOfDays = numberOfDays;
        _isTour = isTour;
        _dateForSorting = dateForSorting;
    }


    return self;
}

@end
