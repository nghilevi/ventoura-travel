//
//  VentouraPakcageCommunicator.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PromotionCommunicatorDelegate.h"
@protocol PromotionCommunicatorDelegate;

@interface PromotionCommunicator : NSObject
@property (weak, nonatomic) id<PromotionCommunicatorDelegate> delegate;

-(void)postCity1:(NSString*)city1 withCity2:(NSString*)city2 withCity3:(NSString*)city3 withCity4:(NSString*)city4;
-(void)postEnterPromotionCity1:(NSString*)city1 withCity2:(NSString*)city2 withCity3:(NSString*)city3 withCity4:(NSString*)city4;
-(void)getPromotionStatus:(NSString*)userId;
@end
