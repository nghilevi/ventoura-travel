//
//  VentouringViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 14/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChoosePersonView.h"
#import "Person.h"
#import "MDCSwipeToChoose.h"
#import "SWRevealViewController.h"

#import "VentouraPackageManager.h"
#import "VentouraPackageManagerDelegate.h"
#import "imageTapDelegate.h"
#import "VentouraPackageCommunicator.h"
#import "ProfileViewController.h"
#import "MBProgressHUD.h"
#import "ventouraClassAppDelegate.h"
#import "MessageViewController.h"
#import "VentouringFilterViewController.h"
#import "OtherUserProfileViewController.h"
#import "ADTransitionController.h"


@interface VentouringViewController : ADTransitioningViewController <MDCSwipeToChooseDelegate, ChoosePersonClassDelegate, OtherUserProfileViewControllerDelegate>{
    CGFloat _duration;
    ADTransitionOrientation _orientation;

}
@property (nonatomic) IBOutlet UIBarButtonItem* revealButtonItem;
@property (nonatomic) IBOutlet UIBarButtonItem* filterButtonItem;
@property (weak, nonatomic) IBOutlet UIView *actionView;
@property (weak, nonatomic) IBOutlet UIView *nameLabelView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *likeButton;
@property (weak, nonatomic) IBOutlet UIButton *nopeButton;
@property (nonatomic, strong) Person *currentPerson;
@property (nonatomic, strong) ChoosePersonView *frontCardView;
@property (nonatomic, strong) ChoosePersonView *backCardView;


@end
