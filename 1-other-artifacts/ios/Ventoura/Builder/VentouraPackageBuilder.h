//
//  VentouraPackageBuilder.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface VentouraPackageBuilder : NSObject
//turns json into objects.
+ (NSArray *)ventouraPackageFromJSON:(NSData *)objectNotation error:(NSError **)error;
//turns zip into uiimage and saves it to the nssarray of person.
+(NSArray*) ventouraPackageAddImageZip:(NSData *) objectNotation withVentouraPackage:(NSArray*)ventouraPackage error:(NSError **)error;
+(BOOL)ventouraPackagIsMatchFromJSON:(NSData *)objectNotation error:(NSError **)error;


@end
