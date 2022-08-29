//
//  TravellerBuilder.h
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"

@interface TravellerBuilder : NSObject
//+ (Traveller *)travellerFromJSON:(NSData *)objectNotation error:(NSError **)error;
//+ (Person *)travellerPersonFromJSON:(NSData *)objectNotation error:(NSError **)error;

//+ (Traveller *)travellerLoginFromJSON:(NSData *)objectNotation error:(NSError **)error;
+ (Person *)personLoginFromJSON:(NSData *)objectNotation error:(NSError **)error;


@end
