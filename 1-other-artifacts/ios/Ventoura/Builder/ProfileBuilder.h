//
//  VentouraPackageBuilder.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"
#import "Attraction.h"

@interface ProfileBuilder : NSObject
//turns json into objects.
+ (Person *)ProfileFromJSON:(NSData *)objectNotation error:(NSError **)error;
+ (Person *)travellerProfileFromJSON:(NSData *)objectNotation error:(NSError **)error;
+ (Person *)guideProfileFromJSON:(NSData *)objectNotation error:(NSError **)error;
+ (Person *)travellerProfileFromDatabase:(NSArray*)results;
+ (Person *)guideProfileFromDatabase:(NSArray*)profileResults attracitonResults:(NSArray*)attractionResults;
+(Person*) matchProfileFromDatabase:(NSArray *)results;
@end
