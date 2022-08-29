//
//  ventouraClassTravellerCommunicatorDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

/*
 The TravellerCommunicator class is responsible for the communication with the Meetup APIs and fetching the JSON data.
 It relies on the delegate of TravellerCommunicatorDelegate to handle the parsing of JSON data. 
 The communicator has no idea how the JSON data is handled. 
 Its focus is only on creating connection to the Meetup APIs and fetching the raw JSON result.
 */

#import <Foundation/Foundation.h>

@protocol LoginCommunicatorDelegate
- (void)receivedTravellerJSON:(NSData *)objectNotation;
- (void)receivedPersonLoginJSON:(NSData *)objectNotation;
- (void)receivedTokenPostJSON:(NSData *)objectNotation;

- (void)receivedTravellerCreateJSON:(NSData *)objectNotation;
- (void)fetchingPersonJSONFailedWithError:(NSError *)error;

@end

