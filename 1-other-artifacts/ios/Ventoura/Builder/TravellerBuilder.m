//
//  TravellerBuilder.m
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "TravellerBuilder.h"
@implementation TravellerBuilder

//+ (Traveller *)travellerFromJSON:(NSData *)objectNotation error:(NSError *__autoreleasing *)error
//{
//    NSLog(@"start building");
//    NSError *localError = nil;
//    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
//    
//    if (localError != nil) {
//        *error = localError;
//        return nil;
//    }
//    for (NSString *key in parsedObject) {
//        NSLog(@"Key %@", key);
//        NSLog(@"Value %@", parsedObject[key]);
//    }
//    Traveller *traveller = [[Traveller alloc] init];
//    return traveller;
//}

//
//+(Person *) travellerPersonFromJSON:(NSData *)objectNotation error:(NSError *__autoreleasing *)error{
//    NSLog(@"start building");
//    NSError *localError = nil;
//    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
//    
//    if (localError != nil) {
//        *error = localError;
//        return nil;
//    }
//    for (NSString *key in parsedObject) {
//        NSLog(@"Key %@", key);
//        NSLog(@"Value %@", parsedObject[key]);
//    }
//    Person *traveller = [[Person alloc] init];
//    return traveller;
//
//}
+(Person *) personLoginFromJSON:(NSData *)objectNotation error:(NSError *__autoreleasing *)error{

    NSLog(@"start building");
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    for (NSString *key in parsedObject) {
        NSLog(@"Key %@", key);
        NSLog(@"Value1111 %@", parsedObject[key]);
    }
    
    
//    NSLog()
    Person *person = [[Person alloc] init];
    person.ventouraId=parsedObject[@"value"];
    return person;


}


@end
