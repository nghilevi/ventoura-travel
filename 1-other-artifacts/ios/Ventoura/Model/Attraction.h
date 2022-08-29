//
//  Attraction.h
//  Ventoura
//
//  Created by Wenchao Chen on 26/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Attraction : NSObject
@property (nonatomic, copy) NSString *attractionId;
@property (nonatomic, copy) NSString *ownerId;
@property (nonatomic, copy) NSString *attractionName;
@property (nonatomic, assign) BOOL isInMemory;


- (instancetype)initWithAttractionId:(NSString *)attractionId
                             ownerId:(NSString *)ownerId
                      attractionName:(NSString *)attractionName
                          isInMemory:(BOOL)isInMemory;
@end
