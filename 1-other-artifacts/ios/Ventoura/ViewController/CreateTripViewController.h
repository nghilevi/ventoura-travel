//
//  CreateTripViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 14/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RMDateSelectionViewController.h"
#import "CitySelectionViewController.h"
#import "TourManager.h"
#import "TourManagerDelegate.h"
#import "TourCommunicator.h"
#import "MBProgressHUD.h"

@interface CreateTripViewController : UIViewController<CitySelectionViewControllerDelegate>
@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic,retain) IBOutlet UIScrollView *sView;
@property (nonatomic,retain) IBOutlet UIButton *createButton;
@property (nonatomic,retain) IBOutlet UIImageView *cityImageView;
@end
