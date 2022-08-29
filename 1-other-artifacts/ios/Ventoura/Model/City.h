//
//  City.h
//  Ventoura
//
//  Created by Wenchao Chen on 15/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface City : NSObject

@property (nonatomic, copy) NSString *cityId;
@property (nonatomic, copy) NSString *cityName;
@property (nonatomic, copy) NSString *countryId;
@property (nonatomic, copy) NSString *countryName;
@property (nonatomic, assign) NSUInteger tableIndexPath;


- (instancetype)initWithId:(NSString *)cityId
                  cityName:(NSString *)cityName
                 countryId:(NSString*)countryId
               countryName:(NSString*)countryName
            tableIndexPath:(NSUInteger)tableIndexPath;
@end
