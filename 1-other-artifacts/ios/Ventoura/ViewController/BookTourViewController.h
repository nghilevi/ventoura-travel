//
//  BookTourViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 27/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ProfileManager.h"
#import "ProfileManagerDelegate.h"
#import "ProfileCommunicator.h"

#import "TourManager.h"
#import "TourManagerDelegate.h"
#import "TourCommunicator.h"
#import "DBManager.h"
#import "RMDateSelectionViewController.h"
#import "MBProgressHUD.h"
#import "ventouraDatabaseUtility.h"
@interface BookTourViewController : UIViewController
@property (nonatomic, readwrite, assign) Person *person;
@property (weak, nonatomic) IBOutlet UILabel *guideNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *cityLabel;
@property (weak, nonatomic) IBOutlet UILabel *amountLabel;
@property (weak, nonatomic) IBOutlet UILabel *lengthLabel;
@property (weak, nonatomic) IBOutlet UILabel *paymentMethodLabel;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UIImageView *portalPicture;
@property (weak, nonatomic) IBOutlet UITableView *tView;

@property (nonatomic, retain) IBOutlet UIButton *bookButton;
- (IBAction)bookPressed;



@end
