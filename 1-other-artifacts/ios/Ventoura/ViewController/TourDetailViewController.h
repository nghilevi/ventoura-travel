//
//  TourDetailViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 28/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TourManager.h"
#import "TourManagerDelegate.h"
#import "TourCommunicator.h"
#import "Tour.h"
#import "ventouraDatabaseUtility.h"
#import "DBManager.h"
@interface TourDetailViewController : UIViewController
//@property (nonatomic) IBOutlet UIImageView* imageProfileView;
//@property (nonatomic) IBOutlet UIScrollView* scrollView;
//@property (nonatomic) IBOutlet UILabel* nameLabel;
//@property (nonatomic) IBOutlet UILabel* tourTypeLabel;
//@property (nonatomic) IBOutlet UILabel* tourDateLabel;
//@property (nonatomic) IBOutlet UILabel* tourPriceLabel;
@property (nonatomic , strong) Tour* tourBooking;
@property (nonatomic) IBOutlet UITableView* tView;
@property (nonatomic) IBOutlet  UIButton* actionBtn;
@property (nonatomic) IBOutlet  UIButton* cancelBtn;

@end
