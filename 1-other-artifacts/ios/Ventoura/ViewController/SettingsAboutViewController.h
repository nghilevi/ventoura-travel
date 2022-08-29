//
//  SettingsAboutViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 1/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "webContentViewController.h"
@interface SettingsAboutViewController : UIViewController

@property (weak, nonatomic) IBOutlet UILabel *versionLabel;
@property (weak, nonatomic) IBOutlet UILabel *rightsLabel1;
@property (weak, nonatomic) IBOutlet UILabel *rightsLabel2;
@property (weak, nonatomic) IBOutlet UIButton *facebookBtn;
@property (weak, nonatomic) IBOutlet UIButton *twitterBtn;
@property (weak, nonatomic) IBOutlet UIButton *instagramBtn;
@property (weak, nonatomic) IBOutlet UIScrollView *sView;
@property (weak, nonatomic) IBOutlet UITableView* tView;
@end
