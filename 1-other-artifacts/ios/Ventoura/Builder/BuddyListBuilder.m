//
//  VentouraPackageBuilder.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "BuddyListBuilder.h"
#import "Person.h"
@implementation BuddyListBuilder
+ (NSArray *)buddyListFromJSON:(NSData *)objectNotation error:(NSError **)error{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    NSLog(@"Buddy Converting");
    NSMutableArray *people = [[NSMutableArray alloc] init];
    NSArray *results = [parsedObject valueForKey:@"matches"];
    NSLog(@"Count %lu", (unsigned long)results.count);
    
    
    
    
    
    for (NSDictionary *groupDic in results) {
        UIImage *img;

        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
        NSString *path = [paths objectAtIndex:0];
        
        
        NSString *imgPath;
        if ([ventouraUtility isUserGuide:groupDic[@"userRole"]]) {
            imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"g_%@_.png",groupDic[@"userId"]]];
        }else{
            imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"t_%@_.png",groupDic[@"userId"]]];
        }
        BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imgPath];
        if (fileExists) {
            NSLog(@"file exists");
            UIImage *newImg = [UIImage imageWithContentsOfFile:imgPath];
            CGImageRef cgref = [newImg CGImage];
            CIImage *cim = [newImg CIImage];
            if (cim == nil && cgref == NULL){
                NSLog(@"no underlying data");
            }
            img = newImg;
        }else{
            img =[UIImage imageNamed:@"noimg.jpg"];
        }

        
       img = [self imageCrop:img];
        
        
       Person *person = [[Person alloc] initForBuddyListWithFirstName:groupDic[@"userFirstname"]
                                                              image:img
                                                               city:groupDic[@"city"]
                                                                age:groupDic[@"age"]
                                                         ventouraId:groupDic[@"userId"]
                                                           userRole:groupDic[@"userRole"]
                                                            country:groupDic[@"country"]
                                                           tourType:nil
                                             useravgReviewScoreRole:nil
                                                      textBiography:groupDic[@"textBiography"]
                                                             images:groupDic[@"galleryIds"]
                                                      lastMessageTime:nil
                                                            matchTime:groupDic[@"timeMatched"]
                                                         unreadCount:0
                                                        lastMessage:nil
                                                           isNewMatch:0];

        
         //NSLog(@"builder %@", person.ventouraId);
        
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

+(UIImage*)imageCrop:(UIImage*)original
{
    UIImage *ret = nil;
    
    // This calculates the crop area.
    
    float originalWidth  = original.size.width;
    float originalHeight = original.size.height;
    
    float edge = fminf(originalWidth, originalHeight);
    
    float posX = (originalWidth   - edge) / 2.0f;
    float posY = (originalHeight  - edge) / 2.0f;
    
    
    CGRect cropSquare;
    // If orientation indicates a change to portrait.
    if(original.imageOrientation == UIImageOrientationLeft ||
       original.imageOrientation == UIImageOrientationRight)
    {
        cropSquare = CGRectMake(posY, posX,
                                edge, edge);
        
    }
    else
    {
        cropSquare = CGRectMake(posX, posY,
                                edge, edge);
    }
    
    
    // This performs the image cropping.
    
    CGImageRef imageRef = CGImageCreateWithImageInRect([original CGImage], cropSquare);
    
    ret = [UIImage imageWithCGImage:imageRef
                              scale:original.scale
                        orientation:original.imageOrientation];
    
    CGImageRelease(imageRef);
    
    return ret;
}



@end
