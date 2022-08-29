//
//  ventouraPackageManagerDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol PromotionManagerDelegate <NSObject>

- (void)didReceivePromotionImage:(UIImage*) image;
- (void)didReceivePromotionEnter;
- (void)didReceivePromotionStatus;
- (void)fetchingPromotioinFailedWithError:(NSError *)error;
@end
