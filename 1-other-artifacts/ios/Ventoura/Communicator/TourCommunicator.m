//
//  VentouraPakcageCommunicator.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import "TourCommunicator.h"

@implementation TourCommunicator

- (void)getTripAndTourJson{
    NSString *urlAsString;
        urlAsString = [NSString stringWithFormat:@"%@/traveller/getTravellerTrip/%@", ventouraBaseURL,[ventouraUtility returnMyUserId]];


    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        if (error) {
            [self.delegate fetchingTourJSONFailedWithError:error];
        } else {
            [self.delegate receivedTripAndTourJson:data];
        }
    }];

    
}

-(void)getTourJsonById:(NSString *)tourId{

    NSString *urlAsString;
    urlAsString = [NSString stringWithFormat:@"%@/traveller/getTravellerTrip/%@", ventouraBaseURL,[ventouraUtility returnMyUserId]];
    
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        if (error) {
            [self.delegate fetchingTourJSONFailedWithError:error];
        } else {
            [self.delegate receivedTripAndTourJson:data];
        }
    }];
}

-(void)getTourJson{
    NSString *urlAsString;
    urlAsString = [NSString stringWithFormat:@"%@/guide/getAllBookings/%@", ventouraBaseURL,[ventouraUtility returnMyUserId]];
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];

    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        if (error) {
            [self.delegate fetchingTourJSONFailedWithError:error];
        } else {
            [self.delegate receivedTourJson:data];
        }
    }];
    

}

- (void)postBookingWithGuideId:(NSString*)guideId travellerId:(NSString*)travellerId guideName:(NSString*)guideName travellerName:(NSString*)travellerName tourPrice:(NSString*)tourPrice tourLength:(NSString*)tourLength tourDate:(NSDate*)tourDate city:(NSString*)city tourType:(NSString *)tourType{
    //build parameters. and then send request., post back the data
    // Dictionary that holds post parameters. You can set your post parameters that your server accepts or programmed to accept.
    
    NSString *postDate = [ventouraUtility ventouraDateToStringForTourPost:tourDate];
//    NSLog(@"after Date: %@", postDate);
    
    
    NSString *post = [NSString stringWithFormat:@"guideId=%@&travellerId=%@&guideFirstname=%@&travellerFirstname=%@&tourDate=%@&tourPrice=%@&tourLength=%@&city=%@&tourType=%@", guideId, travellerId, guideName, travellerName, [NSString stringWithFormat:@"%@:00",postDate], tourPrice,tourLength,city,tourType];
    NSLog(@"Create Booking String %@", post);
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/traveller/createBooking", ventouraBaseURL]
]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        NSLog(@"response for post ");
        NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
        NSDictionary *dic = [httpResponse allHeaderFields];
        NSLog(@"response content: %@",dic);
        if (error) {
            [self.delegate fetchingTourJSONFailedWithError:error];
        } else {
            [self.delegate receivedTourJSON:data];
        }
    }];

    
}

-(void)postTripForCity:(NSString *)cityId withStartDate:(NSDate *)startDate andEndDate:(NSDate *)endDate countryId:(NSString *)countryId{
    
    
    
    NSString *postStartDate = [ventouraUtility ventouraDateToStringForPost:startDate];

    NSString *postEndDate = [ventouraUtility ventouraDateToStringForPost:endDate];

    NSString *post = [NSString stringWithFormat:@"travellerId=%@&travellerScheduleStartDate=%@&travellerScheduleEndDate=%@&travellerScheduleCity=%@&travellerScheduleCountry=%@",[ventouraUtility returnMyUserId],[NSString stringWithFormat:@"%@ 00:00:00",postStartDate], [NSString stringWithFormat:@"%@ 00:00:00",postEndDate],cityId,countryId];
    NSLog(@" end date convert %@", [ventouraUtility ventouraDateToStringForPost:endDate]);
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/traveller/createTravellerSchedule", ventouraBaseURL]
                     ]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        NSLog(@"response for post ");
        NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
        NSDictionary *dic = [httpResponse allHeaderFields];
        NSLog(@"response content: %@",dic);
        if (error) {
            [self.delegate fetchingTourJSONFailedWithError:error];
        } else {
            [self.delegate receivedTripCreateJSON:data];
        }
    }];
}


@end
