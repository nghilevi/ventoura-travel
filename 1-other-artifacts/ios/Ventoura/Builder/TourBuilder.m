//
//  VentouraPackageBuilder.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "TourBuilder.h"
#import "Person.h"
@implementation TourBuilder
+ (Person *)CreateTourFromJSON:(NSData *)objectNotation error:(NSError **)error
{
    NSError *localError = nil;
//    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    NSLog(@"Create Tour Converting");
    
    
//    Person *person = [[Person alloc] initForGuideProfileWithFirstName:parsedObject[@"guideFirstname"]
//                                                                image:[UIImage imageNamed:@"noimg.jpg"]
//                                                                 city:parsedObject[@"city"]
//                                                            tourPrice:parsedObject[@"tourPrice"]
//                                                           ventouraId:parsedObject[@"id"]
//                                                        paymentMethod:parsedObject[@"paymentMethod"]
//                                                           tourLength:parsedObject[@"tourLength"]];
//    return person;
    return nil;
}
+(NSArray*)CreateTourTripListFromJSON:(NSData *)objectNotation error:(NSError **)error
{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    NSLog(@"Count Converting");
//    NSMutableArray *tourTripList = [[NSMutableArray alloc] init];
    NSMutableArray *tourList = [[NSMutableArray alloc] init];
//    NSMutableArray *tripList = [[NSMutableArray alloc] init];

    NSArray *travellerScheduleList = [parsedObject valueForKey:@"travellerScheduleList"];
    NSLog(@"travellerScheduleList count %lu", (unsigned long)travellerScheduleList.count);
    NSArray *travellerBookingList = [parsedObject valueForKey:@"travellerBookingList"];
    NSLog(@"travellerBookingList count %lu", (unsigned long)travellerBookingList.count);
    
    
    for (NSDictionary *groupDic in travellerScheduleList) {
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"dd/MM/yyyy"];
        [df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
        NSDate *tourDate = [df dateFromString: groupDic[@"startTime"]];
        
        Tour *trip = [[Tour alloc] initTripForListViewWithTourId:groupDic[@"id"]
                                                     travellerId:groupDic[@"travellerId"]
                                                            city:groupDic[@"city"]
                                                         country:groupDic[@"country"]
                                                         endTime:groupDic[@"endTime"]
                                                       startTime:groupDic[@"startTime"]
                                                    numberOfDays:groupDic[@"numberOfDays"]
                                                          isTour:@"0"
                                                  dateForSorting:tourDate];
        
        //NSLog(@"builder %@", person.ventouraId);
        
//        NSLog(@"date for tour %@,....%@", tourDate,groupDic[@"startTime"]);

        [tourList addObject:trip];
    }
   
    for (NSDictionary *groupDic in travellerBookingList) {
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
        [df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
        NSDate *tourDate = [df dateFromString: groupDic[@"tourDate"]];
        Tour *tour = [[Tour alloc] initTourForListViewWithTourId:groupDic[@"id"]
                                              travellerFirstname:groupDic[@"travellerFirstname"]
                                                     travellerId:groupDic[@"travellerId"]
                                                         guideId:groupDic[@"guideId"]
                                                  guideFirstname:groupDic[@"guideFirstname"]
                                                       tourPrice:groupDic[@"tourPrice"]
                                                   bookingStatus:groupDic[@"bookingStatus"]
                                                        tourDate:groupDic[@"tourDate"]
                                                          isTour:@"1"
                                                  dateForSorting:tourDate
                                                            city:groupDic[@"city"]
                                                        tourType:groupDic[@"tourType"]
                                           statusLastUpdatedTime:groupDic[@"statusLastUpdatedTime"]];
        NSLog(@"date for tour %@", tourDate);
        [tourList addObject:tour];
    }
    
    
//    NSArray *sortedArray;
//    sortedArray = [tourList sortedArrayUsingComparator:^NSComparisonResult(id a, id b) {
//        NSDate *first = [(Tour*)a dateForSorting];
//        NSDate *second = [(Tour*)b dateForSorting];
//        return [first compare:second];
//    }];
    NSSortDescriptor *sortDescriptor;
    sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"dateForSorting"
                                                 ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    NSArray *sortedArray;
    sortedArray = [tourList sortedArrayUsingDescriptors:sortDescriptors];
    return sortedArray;
}

+(NSArray*)CreateTourListFromJSON:(NSData *)objectNotation error:(NSError **)error
{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    NSLog(@"Count Converting");
    NSMutableArray *tourList = [[NSMutableArray alloc] init];
    
    NSArray *bookingList = [parsedObject valueForKey:@"bookings"];
    NSLog(@"travellerBookingList count %lu", (unsigned long)bookingList.count);
    
    for (NSDictionary *groupDic in bookingList) {
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"yyyy-MM-dd HH:mm"];
        [df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
        NSDate *tourDate = [df dateFromString: groupDic[@"tourDate"]];
        Tour *tour = [[Tour alloc] initTourForListViewWithTourId:groupDic[@"id"]
                                              travellerFirstname:groupDic[@"travellerFirstname"]
                                                     travellerId:groupDic[@"travellerId"]
                                                         guideId:groupDic[@"guideId"]
                                                  guideFirstname:groupDic[@"guideFirstname"]
                                                       tourPrice:groupDic[@"tourPrice"]
                                                   bookingStatus:groupDic[@"bookingStatus"]
                                                        tourDate:groupDic[@"tourDate"]
                                                          isTour:@"1"
                                                  dateForSorting:tourDate
                                                            city:groupDic[@"city"]
                                                        tourType:groupDic[@"tourType"]
                                           statusLastUpdatedTime:groupDic[@"statusLastUpdatedTime"]];
        NSLog(@"date for tour %@", tourDate);
        [tourList addObject:tour];
    }
    NSSortDescriptor *sortDescriptor;
    sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"dateForSorting"
                                                 ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    NSArray *sortedArray;
    sortedArray = [tourList sortedArrayUsingDescriptors:sortDescriptors];
    return sortedArray;
}

@end
