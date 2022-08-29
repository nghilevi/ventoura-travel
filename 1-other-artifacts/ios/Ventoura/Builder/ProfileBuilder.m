//
//  VentouraPackageBuilder.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ProfileBuilder.h"
#import "Person.h"
@implementation ProfileBuilder
+ (Person *)ProfileFromJSON:(NSData *)objectNotation error:(NSError **)error
{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }

    Person *person = [[Person alloc] initForGuideProfileWithFirstName:parsedObject[@"guideFirstname"]
                                                                image:[UIImage imageNamed:@"noimg.jpg"]
                                                                 city:parsedObject[@"city"]
                                                            tourPrice:parsedObject[@"tourPrice"]
                                                           ventouraId:parsedObject[@"id"]
                                                        paymentMethod:parsedObject[@"paymentMethod"]
                                                           tourLength:parsedObject[@"tourLength"]
                                                          attractions:nil
                                                         localSecrets:nil
                                                        textBiography:nil
                                                          dateOfBirth:nil
                                                                  age:nil
                                                              country:nil
                                                             tourType:nil
                                               useravgReviewScoreRole:nil
                                                                images:nil];
    return person;
}

+ (Person *)travellerProfileFromJSON:(NSData *)objectNotation error:(NSError *__autoreleasing *)error{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
    NSLog(@"Profile Converting");
    
    NSArray *results = [parsedObject valueForKey:@"galleryIds"];
    NSMutableArray *images = [[NSMutableArray alloc] init];
    //    NSMutableArray *localSecrets = [[NSMutableArray alloc] init];
    
    for (NSString *imgId in results) {
                NSLog(@"hai %@",imgId);
        NSString* image = imgId;
        [images addObject:image];
        
    }
    
     Person *person = [[Person alloc] initForTravellerProfileWithFirstName:parsedObject[@"travellerFirstname"]
                                                                      city:parsedObject[@"city"]
                                                                ventouraId:parsedObject[@"id"]
                                                                    gender:parsedObject[@"gender"]
                                                               dateOfBirth:parsedObject[@"dateOfBirth"]
                                                                   country:parsedObject[@"country"]
                                                             textBiography:parsedObject[@"textBiography"]
                                                                       age:parsedObject[@"age"]
                                                                    images:images];
    return person;

}


+ (Person *)guideProfileFromJSON:(NSData *)objectNotation error:(NSError *__autoreleasing *)error{
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:objectNotation options:0 error:&localError];
    
    if (localError != nil) {
        *error = localError;
        return nil;
    }
//    NSLog(@"Profile Converting attractions %@",parsedObject[@"attractions"]);
    NSArray *results = [parsedObject valueForKey:@"attractions"];
    NSMutableArray *attractions = [[NSMutableArray alloc] init];
//    NSMutableArray *localSecrets = [[NSMutableArray alloc] init];

    for (NSDictionary *groupDic in results) {
//        NSLog(@"hai %@",groupDic[@"attractionName"]);
        Attraction * attraction = [[Attraction alloc]initWithAttractionId:groupDic[@"id"]
                                                                 ownerId:[ventouraUtility returnMyUserIdWithType]
                                                           attractionName:groupDic[@"attractionName"]
                                                              isInMemory:NO];
        [attractions addObject:attraction];

    }
//    results = [parsedObject valueForKey:@"hiddenTreasures"];
//    for (NSDictionary *groupDic in results) {
//        Attraction * hiddenTreasures = [[Attraction alloc]initWithAttractionId:groupDic[@"id"]
//                                                                  ownerId:[ventouraUtility returnMyUserIdWithType]
//                                                           attractionName:groupDic[@"hiddenTreasureName"]
//                                                               isInMemory:NO];
//        [localSecrets addObject:hiddenTreasures];
//        
//    }
    results = [parsedObject valueForKey:@"galleryIds"];
    NSMutableArray *images = [[NSMutableArray alloc] init];
    //    NSMutableArray *localSecrets = [[NSMutableArray alloc] init];
    
    for (NSString *imgId in results) {
        NSLog(@"hai %@",imgId);
        NSString* image = imgId;
        [images addObject:image];
        
    }
    
    Person *person = [[Person alloc] initForGuideProfileWithFirstName:parsedObject[@"guideFirstname"]
                                                                image:nil
                                                                 city:parsedObject[@"city"]
                                                            tourPrice:parsedObject[@"tourPrice"]
                                                           ventouraId:parsedObject[@"id"]
                                                        paymentMethod:parsedObject[@"paymentMethod"]
                                                           tourLength:parsedObject[@"tourLength"]
                                                          attractions:attractions
                                                         localSecrets:nil
                                                        textBiography:parsedObject[@"textBiography"]
                                                          dateOfBirth:parsedObject[@"dateOfBirth"]
                                                                  age:parsedObject[@"age"]
                                                              country:parsedObject[@"country"]
                                                             tourType:parsedObject[@"tourType"]
                                               useravgReviewScoreRole:parsedObject[@"avgReviewScore"]
                                                               images:images];
    

    NSLog(@"Age: %@",parsedObject[@"age"]);
    return person;
    
}

+(Person*) travellerProfileFromDatabase:(NSArray *)results{
    //@"select ProfileId, name, age, textBiography, country, city, isGuide, useravgReviewScoreRole, tourLength, tourPrice, paymentMethod, lastUpdated from UserProfile WHERE ProfileId like '%@'",userId];
    NSLog(@"ID, %@",results[0]);
    NSLog(@"name, %@",results[1]);
    NSLog(@"age, %@",results[2]);
//    NSLog(@"gender, %@",results[3]);
    NSLog(@"textBiography, %@",results[4]);
    NSLog(@"country, %@",results[5]);
    NSLog(@"city, %@",results[6]);


    Person *person =  [[Person alloc] initForTravellerProfileWithFirstName:results[1]
                                                                      city:results[6]
                                                                ventouraId:results[0]
                                                                    gender:nil
                                                               dateOfBirth:nil
                                                                   country:results[4]
                                                             textBiography:results[3]
                                                                       age:results[2]
                                                                    images:nil];
    

    return person;
}

+(Person*) guideProfileFromDatabase:(NSArray *)profileResults attracitonResults:(NSArray *)attractionResults{

    //select ProfileId, name, age, textBiography, country, city, isGuide, useravgReviewScoreRole, tourLength, tourPrice, paymentMethod, tourType, lastUpdated
//    Person *person =  [[Person alloc] initForTravellerProfileWithFirstName:profileResults[1]
//                                                                      city:profileResults[6]
//                                                                ventouraId:profileResults[0]
//                                                                    gender:nil
//                                                               dateOfBirth:nil
//                                                                   country:profileResults[4]
//                                                             textBiography:profileResults[3]
//                                                                       age:profileResults[2]
//                                                                    images:nil];
    
    NSMutableArray *attractions = [[NSMutableArray alloc] init];
    for (int i = 0; i<attractionResults.count; i++) {
        
//        Attraction * tmp = attractionResults[];
//         attractionId,attractionName,ownerId
        Attraction * attraction = [[Attraction alloc]initWithAttractionId:attractionResults[i][0]
                                                                  ownerId:attractionResults[i][2]
                                                           attractionName:attractionResults[i][1]
                                                               isInMemory:NO];
        [attractions addObject:attraction];
    }
//    Person *person;
    
     Person *person = [[Person alloc] initForGuideProfileWithFirstName:profileResults[1]
                                                         image:nil
                                                          city:profileResults[6]
                                                     tourPrice:profileResults[9]
                                                    ventouraId:profileResults[0]
                                                 paymentMethod:profileResults[10]
                                                    tourLength:profileResults[8]
                                                   attractions:attractions
                                                  localSecrets:nil
                                                 textBiography:profileResults[3]
                                                   dateOfBirth:nil
                                                           age:profileResults[2]
                                                       country:profileResults[4]
                                                      tourType:profileResults[11]
                                        useravgReviewScoreRole:profileResults[7]
                                                        images:nil];
    
    return person;

}
+(Person*) matchProfileFromDatabase:(NSArray *)results{
    
    //matchUserId, matchUserName, unreadCount, isNewMatch, matchDate from Match

    NSString * matchUserIdWithType = results[0];
    NSString *userRole;
    NSArray*  parsedArray = [matchUserIdWithType componentsSeparatedByString: @"_"];
    if ([parsedArray[0] isEqualToString:@"g"]) {
        userRole = @"GUIDE";
    }else{
        userRole = @"TRAVELLER";
    }
//    Person *person =  [[Person alloc] initForTravellerProfileWithFirstName:results[1]
//                                                                      city:nil
//                                                                ventouraId:parsedArray[1]
//                                                                    gender:nil
//                                                               dateOfBirth:nil
//                                                                   country:nil
//                                                             textBiography:nil
//                                                                       age:nil
//                                                                    images:nil];
    
   
    
    Person *person = [[Person alloc] initForBuddyListWithFirstName:results[1]
                                                     image:nil
                                                      city:nil
                                                       age:nil
                                                ventouraId:parsedArray[1]
                                                  userRole:userRole
                                                   country:nil
                                                  tourType:nil
                                    useravgReviewScoreRole:nil
                                             textBiography:nil
                                                    images:nil
                                           lastMessageTime:nil
                                                 matchTime:results[4]
                                               unreadCount:[results[2] integerValue]
                                               lastMessage:nil
                                                isNewMatch:0];
    
    person.userRole = userRole;
    
    person.image = [UIImage imageNamed:@"noimg.jpg"];
    
    return person;
    
}
@end
