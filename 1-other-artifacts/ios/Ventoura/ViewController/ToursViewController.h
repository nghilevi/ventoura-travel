//
//  ToursViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 16/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TourManager.h"
#import "TourManagerDelegate.h"
#import "TourCommunicator.h"
#import "Tour.h"
#import "ventouraUtility.h"
#import "DBManager.h"
#import "CreateTripViewController.h"
#import "TourDetailViewController.h"
#import "MessageTableViewCell.h"
@interface ToursViewController : UIViewController<SWTableViewCellDelegate,UIActionSheetDelegate>
@property (nonatomic) IBOutlet UIBarButtonItem* revealButtonItem;
@property (nonatomic) IBOutlet UIBarButtonItem* addCityItem;
@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic) CGFloat heightOfRow;
@end
