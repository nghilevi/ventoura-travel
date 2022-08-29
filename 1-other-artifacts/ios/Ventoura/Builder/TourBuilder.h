//
//  VentouraPackageBuilder.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"
#import "Tour.h"

@interface TourBuilder : NSObject
//turns json into objects.
+ (Person *)CreateTourFromJSON:(NSData *)objectNotation error:(NSError **)error;
//turns zip into uiimage and saves it to the nssarray of person.
+(NSArray*) CreateTourTripListFromJSON:(NSData *) objectNotation error:(NSError **)error;
+(NSArray*)CreateTourListFromJSON:(NSData *)objectNotation error:(NSError **)error;

@end
