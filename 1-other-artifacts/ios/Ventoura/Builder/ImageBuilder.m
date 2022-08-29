//
//  ImageBuilder.m
//  Ventoura
//
//  Created by Wenchao Chen on 30/10/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ImageBuilder.h"
#import "UserImage.h"
@implementation ImageBuilder
+ (NSString *)ImageUploadResultFromJSON:(NSData *)objectNotation error:(NSError **)error{
    
    NSLog(@"Start Building Image Id");
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    for (NSString *key in parsedObject) {
        NSLog(@"Key %@", key);
        NSLog(@"Value: %@", parsedObject[key]);
    }
//    NSLog()
//    Person *person = [[Person alloc] init];
    NSString* imgId = parsedObject[@"value"];
    return imgId;

}
@end
