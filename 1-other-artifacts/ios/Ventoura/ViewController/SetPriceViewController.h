//
//  SetPriceViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 7/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ventouraSlider.h"
@class SetPriceViewController;
@protocol SetPriceViewControllerDelegate <NSObject>
- (void)editPriceViewController:(SetPriceViewController *)controller didFinishEditTourDetail:(NSInteger ) value;
@end

@interface SetPriceViewController : UIViewController
@property (nonatomic,assign) NSInteger priceValue;
@property (nonatomic,assign) NSInteger paymentType;
@property (nonatomic, weak) id <SetPriceViewControllerDelegate> delegate;

@end
