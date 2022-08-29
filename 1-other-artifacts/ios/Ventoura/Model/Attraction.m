//
//  Attraction.m
//  Ventoura
//
//  Created by Wenchao Chen on 26/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "Attraction.h"

@implementation Attraction
-(instancetype) initWithAttractionId:(NSString *)attractionId ownerId:(NSString *)ownerId attractionName:(NSString *)attractionName isInMemory:(BOOL)isInMemory{
    self = [super init];
    if (self) {
        _attractionId = attractionId;
        _ownerId = ownerId;
        _isInMemory = isInMemory;
        _attractionName = attractionName;
    }
    return self;
}
@end
