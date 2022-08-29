//
//  ImageBuilder.h
//  Ventoura
//
//  Created by Wenchao Chen on 30/10/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ImageBuilder : NSObject
+ (NSString *)ImageUploadResultFromJSON:(NSData *)objectNotation error:(NSError **)error;

@end
