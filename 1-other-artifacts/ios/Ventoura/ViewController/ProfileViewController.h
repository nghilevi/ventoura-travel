//
//  ProfileViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 16/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EditProfileViewController.h"
#import "constants.h"
#import "ProfileManager.h"
#import "ProfileManagerDelegate.h"
#import "ProfileCommunicator.h"
#import "Attraction.h"
#import "JBParallaxCell.h"
#import "FXPageControl.h"
#import "MBProgressHUD.h"
#import "ventouraDatabaseUtility.h"
#import "ProfileBuilder.h"
@interface ProfileViewController : UIViewController <UITableViewDelegate, UITableViewDataSource,UIScrollViewDelegate>{
//    UIScrollView* scrollView;
//    UIPageControl* pageControl;
}


@property (nonatomic, retain) UIScrollView* scrollView;
@property (nonatomic, retain) UIPageControl* pageControl;

@property (nonatomic) IBOutlet UIBarButtonItem* revealButtonItem;
@property (nonatomic) IBOutlet UIBarButtonItem* editProfileButtonItem;


@property (weak, nonatomic) IBOutlet UIImageView *profilePicture;
@property (nonatomic,retain) IBOutlet UITableView *tView;
- (IBAction)pageControlAction:(FXPageControl *)sender;

@end
