//
//  City.m
//  Ventoura
//
//  Created by Wenchao Chen on 15/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "City.h"

@implementation City

- (instancetype)initWithId:(NSString *)cityId cityName:(NSString *)cityName countryId:(NSString *)countryId countryName:(NSString *)countryName tableIndexPath:(NSUInteger)tableIndexPath{
    self = [super init];
    if (self) {
        _cityId = cityId;
        _countryId= countryId;
        _countryName = countryName;
        _tableIndexPath = tableIndexPath;
        _cityName = cityName;
    }
    return self;
}
@end
