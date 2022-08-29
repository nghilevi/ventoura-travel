//
//  SettingsViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 16/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import "ventouraClassAppDelegate.h"
#import "SettingsAboutViewController.h"

@interface SettingsViewController : UIViewController <FBLoginViewDelegate,UITableViewDelegate, UITableViewDataSource,UIActionSheetDelegate,UINavigationControllerDelegate>
@property (nonatomic) IBOutlet UIBarButtonItem* revealButtonItem;
@property (nonatomic,retain) IBOutlet UITableView *tView;
@end
