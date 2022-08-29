//
//  VentouraPackageManager.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PromotionCommunicatorDelegate.h"
#import "PromotionManagerDelegate.h"
#import "ventouraDatabaseUtility.h"
#import "DBManager.h"

@class PromotionCommunicator;

@interface PromotionManager : NSObject<PromotionCommunicatorDelegate>
@property (strong, nonatomic) PromotionCommunicator *communicator;
@property (weak, nonatomic) id<PromotionManagerDelegate> delegate;


-(void)postCityId:(NSString*)city1 withCity2:(NSString*)city2 withCity3:(NSString*)city3 withCity4:(NSString*)city4;
-(void)postEnterPromotionWithCityId:(NSString*)city1 withCity2:(NSString*)city2 withCity3:(NSString*)city3 withCity4:(NSString*)city4;
-(void)fetchPromotionStatus:(NSString*)userId;

@end
