//
//  PromotionPreviewViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 10/10/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PromotionManager.h"
#import "PromotionManagerDelegate.h"
#import "PromotionCommunicator.h"
#import "MBProgressHUD.h"
//#import <FacebookSDK/FacebookSDK.h>
#import "DBManager.h"
#import "ventouraClassAppDelegate.h"

@interface PromotionPreviewViewController : UIViewController <FBLoginViewDelegate>

@property (nonatomic, strong) NSString *city1;
@property (nonatomic, strong) NSString *city2;
@property (nonatomic, strong) NSString *city3;
@property (nonatomic, strong) NSString *city4;

@property (nonatomic) IBOutlet UIImageView* promotionImage;
@property (nonatomic) IBOutlet UIScrollView* scrollView;
@property (nonatomic) IBOutlet UIButton* tickBox;
@property (nonatomic) IBOutlet UIButton* tnc;
@property (nonatomic) IBOutlet UILabel* agreeLabel;

@end
