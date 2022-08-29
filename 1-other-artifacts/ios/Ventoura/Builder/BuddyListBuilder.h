//
//  VentouraPackageBuilder.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BuddyListBuilder : NSObject
//turns json into objects.
+ (NSArray *)buddyListFromJSON:(NSData *)objectNotation error:(NSError **)error;
//turns zip into uiimage and saves it to the nssarray of person.
+(NSArray*) ventouraPackageAddImageZip:(NSData *) objectNotation withVentouraPackage:(NSArray*)ventouraPackage error:(NSError **)error;
+(BOOL)ventouraPackagIsMatchFromJSON:(NSData *)objectNotation error:(NSError **)error;

+(UIImage*)imageCrop:(UIImage*)original;
@end
