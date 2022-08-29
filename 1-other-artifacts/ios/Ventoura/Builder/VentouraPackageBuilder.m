//
//  VentouraPackageBuilder.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "VentouraPackageBuilder.h"
#import "Person.h"
@implementation VentouraPackageBuilder
+ (NSArray *)ventouraPackageFromJSON:(NSData *)objectNotation error:(NSError **)error
{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    NSLog(@"Count Converting");
    NSMutableArray *people = [[NSMutableArray alloc] init];
    NSArray *results = [parsedObject valueForKey:@"ventouraList"];
    NSLog(@"Count %lu", (unsigned long)results.count);
    
    for (NSDictionary *groupDic in results) {
        //need to add attraction and other things fml!
       Person *person = [[Person alloc] initForPackageWithFirstName:groupDic[@"firstname"]
                                                         image:[UIImage imageNamed:@"noimg.jpg"]
                                                          city:groupDic[@"city"]
                                                           age:groupDic[@"age"]
                                                    ventouraId:groupDic[@"id"]
                                                      userRole:groupDic[@"userRole"]
                                                        country:groupDic[@"country"]
                                                    tourType:groupDic[@"tourType"]
                                                useravgReviewScoreRole:nil
                                                      textBiography:groupDic[@"textBiography"]                                                       images:groupDic[@"galleryIds"]];
        
//         NSLog(@"builder %@", person.tourType);
    
        
        [people addObject:person];
    }
    
    return people;
}
+(NSArray*)ventouraPackageAddImageZip:(NSData *)zipData withVentouraPackage:(NSArray *)ventouraPackage error:(NSError *__autoreleasing *)error
{
            NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
            NSString *path = [paths objectAtIndex:0];
            //can probs remove this step.
            //decided still mage the images here
            NSLog(@"Count package %lu", (unsigned long)ventouraPackage.count);
            for (Person *person in ventouraPackage) {
                //NSLog(@"builder vent ID %@",person.name);

                NSString *imgPath;
                if ([person.userRole isEqualToString:@"GUIDE"]) {
                    imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"g_%@_.png",person.ventouraId]];

                    //NSLog(@"g_%@",person.ventouraId);
                    //post = [post stringByAppendingString:[NSString stringWithFormat:@"g_%@=g_%@&",person.ventouraId,person.ventouraId]];
                }else{
                    //NSLog(@"t_%@",person.ventouraId);
                    imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"t_%@_.png",person.ventouraId]];
                    //post = [post stringByAppendingString:[NSString stringWithFormat:@"t_%@=t_%@&",person.ventouraId,person.ventouraId]];
                    //NSLog(@"traveller %@",person.ventouraId);

                }
                BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imgPath];
                if (fileExists) {
                    NSLog(@"file exists");
                    UIImage *newImg = [UIImage imageWithContentsOfFile:imgPath];
                    CGImageRef cgref = [newImg CGImage];
                    CIImage *cim = [newImg CIImage];
                    if (cim == nil && cgref == NULL)
                    {
                        NSLog(@"no underlying data");
                    }
                    person.image = newImg;
                }
            }

    NSLog(@"Finished!");
    return ventouraPackage;
}
+(BOOL)ventouraPackagIsMatchFromJSON:(NSData *)objectNotation error:(NSError *__autoreleasing *)error{
    
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return NO;
    }
    int value = [parsedObject[@"value"] intValue];
    //BOOL result = value;
    return value;
}
@end
