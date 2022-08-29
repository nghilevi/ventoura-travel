//
//  OtherUserProfileViewController.h
//  Ventoura
//
//  Created by Jai Carlton on 29/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Person.h"
#import "ventouraDatabaseUtility.h"
#import "DBManager.h"
#import "FXPageControl.h"
#import "ventouraUtility.h"
#import "ProfileBuilder.h"
#import "ProfileManager.h"
#import "ProfileManagerDelegate.h"
#import "ProfileCommunicator.h"
#import "MBProgressHUD.h"
#import "ADTransitionController.h"
#import "ReviewViewController.h"

@class OtherUserProfileViewController;
@protocol OtherUserProfileViewControllerDelegate <NSObject>
- (void)likeSelectedInOtherUserView;
- (void)nopeSelectedInOtherUserView;
- (void)timerMethod;
-(void)passBackNavDelegate:(ADNavigationControllerDelegate*)_navDelegate;
-(void)viewPop;
@end

@interface OtherUserProfileViewController : ADTransitioningViewController <UIScrollViewDelegate,UIActionSheetDelegate,UINavigationControllerDelegate,ReviewViewControllerDelegate>{
    CGFloat _duration;

}

@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic, retain) UIScrollView* scrollView;
@property (nonatomic,strong) Person *person;
@property (nonatomic, weak) id <OtherUserProfileViewControllerDelegate> delegate;
@property (nonatomic) IBOutlet UIBarButtonItem *moreButton;
@property (nonatomic) BOOL fromVentouring;
@property (nonatomic) BOOL fromMessages;

- (IBAction)pageControlAction:(FXPageControl *)sender;

@end
