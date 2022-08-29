//
//  RoleSelectionViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 8/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import <CoreLocation/CoreLocation.h>
#import <UIKit/UIKit.h>
#import "ventouraUtility.h"
#import "constants.h"
#import "LoginManager.h"
#import "ProfileManagerDelegate.h"
#import "ProfileManager.h"
#import "ProfileCommunicator.h"
#import "PromotionManagerDelegate.h"
#import "PromotionManager.h"
#import "PromotionCommunicator.h"
#import "LoginCommunicator.h"
#import "ventouraClassAppDelegate.h"
#import "PaymentSelectionViewController.h"
#import "MBProgressHUD.h"
#import "ventouraDatabaseUtility.h"

@interface RoleSelectionViewController : UIViewController<CLLocationManagerDelegate>
@property (nonatomic, assign) CGPoint lastContentOffset;
@property (nonatomic, strong) CLLocationManager *locationManager;
@property (nonatomic, strong) CLGeocoder *geocoder;
@property (nonatomic, strong) CLPlacemark *placemark;

@end
