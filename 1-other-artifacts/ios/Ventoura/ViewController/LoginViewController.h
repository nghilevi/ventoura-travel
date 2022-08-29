//
//  ventouraClassLoginViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 1/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import "MessageViewController.h"
#import "LoginManager.h"
#import "LoginCommunicator.h"
#import "SettingsViewController.h"
BOOL isGuide;
@interface LoginViewController : UIViewController <FBLoginViewDelegate,CLLocationManagerDelegate>

@property (weak, nonatomic) IBOutlet FBLoginView *loginButton;
@property (weak, nonatomic) IBOutlet UIView *actionView;

- (void) saveSystemSettings;
@end
