//
//  VentouraPackageManager.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "TourManager.h"
#import "TourBuilder.h"
#import "TourCommunicator.h"
#import "SSZipArchive.h"
#import "Person.h"

@implementation TourManager

-(void)createBookingWithGuideId:(NSString *)guideId travellerId:(NSString *)travellerId guideName:(NSString *)guideName travellerName:(NSString *)travellerName tourPrice:(NSString *)tourPrice tourLength:(NSString*)tourLength  tourDate:(NSDate *)tourDate  city:(NSString*)city tourType:(NSString *)tourType{
    [self.communicator postBookingWithGuideId:guideId travellerId:travellerId guideName:guideName travellerName:travellerName tourPrice:tourPrice tourLength:(NSString*)tourLength  tourDate:tourDate city:city tourType:tourType];
}

-(void)createTripForCity:(NSString*)cityId withStartDate:(NSDate*)startDate andEndDate:(NSDate*)endDate countryId:(NSString *)countryId{
    [self.communicator postTripForCity:cityId withStartDate:startDate andEndDate:endDate countryId:countryId];
}

- (void)fetchTourById:(NSString*)tourId{
    [self.communicator getTourJsonById:tourId];

}
-(void)fetchTripAndTour{
    [self.communicator getTripAndTourJson];
}
-(void)fetchTours{
    [self.communicator getTourJson];
}

#pragma mark - Ventoura Package Communicator Deleagete
-(void)receivedTourByIdJson:(NSData *)objectNotation{



}

- (void)receivedTripAndTourJson:(NSData * )objectNotation{
    NSError *error = nil;

    NSArray *TourList = [TourBuilder CreateTourTripListFromJSON:objectNotation error:&error];
    if (error != nil) {
        [self.delegate fetchingTourFailedWithError:error];
        
    } else {
        [self.delegate didReceiveTourTripObject:TourList];
    }
}


- (void)receivedTourJson:(NSData * )objectNotation{
    NSError *error = nil;
    
    NSArray *TourList = [TourBuilder CreateTourListFromJSON:objectNotation error:&error];
    if (error != nil) {
        [self.delegate fetchingTourFailedWithError:error];
        
    } else {
        [self.delegate didReceiveTourTripObject:TourList];
    }
}


-(void)receivedTripCreateJSON:(NSData *)objectNotation{

    NSError *error = nil;
    if (error != nil) {
        [self.delegate fetchingTourFailedWithError:error];
        
    } else {
        [self.delegate didReceiveCreateTripResult:YES];
    }

}

// a json delegate, you can also have other formats.
- (void)receivedTourJSON:(NSData *)objectNotation{
    //once json is received, convert it and call manager
    NSError *error = nil;
    Person *profile = [TourBuilder CreateTourFromJSON:objectNotation error:&error];
        
        if (error != nil) {
            [self.delegate fetchingTourFailedWithError:error];
   
        } else {
            [self.delegate didReceiveCreateTourResult:profile];
        }
    
}
- (void)fetchingTourJSONFailedWithError:(NSError *)error
{
    [self.delegate fetchingTourFailedWithError:error];
}

@end
