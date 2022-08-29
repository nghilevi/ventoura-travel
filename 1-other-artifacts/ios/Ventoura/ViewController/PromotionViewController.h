//
//  PromotionViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 3/09/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#include "CitySelectionViewController.h"
#include "PromotionPreviewViewController.h"
#import "DBManager.h"

@interface PromotionViewController : UIViewController<CitySelectionViewControllerDelegate>
@property (nonatomic) IBOutlet UIBarButtonItem* revealButtonItem;
@property (nonatomic) IBOutlet UIView* city1;
@property (nonatomic) IBOutlet UIImageView* cityBackground1;
@property (nonatomic) IBOutlet UILabel* cityLabel1;

@property (nonatomic) IBOutlet UIView* city2;
@property (nonatomic) IBOutlet UIImageView* cityBackground2;
@property (nonatomic) IBOutlet UILabel* cityLabel2;

@property (nonatomic) IBOutlet UIView* city3;
@property (nonatomic) IBOutlet UIImageView* cityBackground3;
@property (nonatomic) IBOutlet UILabel* cityLabel3;

@property (nonatomic) IBOutlet UIView* city4;
@property (nonatomic) IBOutlet UIImageView* cityBackground4;
@property (nonatomic) IBOutlet UILabel* cityLabel4;

@property (nonatomic) IBOutlet UIScrollView* scrollView;
@property (nonatomic) IBOutlet UIButton* previewButton;
@property (nonatomic) IBOutlet UIButton* likeButton;

- (IBAction)previewPressed;



@property (nonatomic) NSInteger lastSelectedIndex;
@property (nonatomic)  UIImage* circleOutline;
@property (nonatomic)  UIImage* salmonCircle;

@property (nonatomic)  NSString* promotionCity1;
@property (nonatomic)  NSString* promotionCity2;
@property (nonatomic)  NSString* promotionCity3;
@property (nonatomic)  NSString* promotionCity4;


@end
