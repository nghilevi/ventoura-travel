//
//  VentouraPackageCommunicatorDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol PromotionCommunicatorDelegate <NSObject>
- (void)receivedPromotionImageData:(NSData *)objectNotation;

- (void)fetchingPromotionJSONFailedWithError:(NSError *)error;
- (void)receivedEnterPromotionData:(NSData *)objectNotation;
- (void)receivedPromotionStatus:(NSData*) objectNotation response:(NSURLResponse*) response;
@end
