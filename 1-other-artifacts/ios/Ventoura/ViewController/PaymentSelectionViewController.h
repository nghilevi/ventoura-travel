//
//  PaymentSelectionViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 18/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import <CoreLocation/CoreLocation.h>

#import <UIKit/UIKit.h>
#import "ventouraUtility.h"
#import "constants.h"
#import "LoginManager.h"
#import "LoginCommunicator.h"
#import "ventouraClassAppDelegate.h"
#import "PaymentSelectionViewController.h"
#import "ProfileManagerDelegate.h"
#import "ProfileManager.h"
#import "ProfileCommunicator.h"
#import "ventouraDatabaseUtility.h"
#import "MBProgressHUD.h"

@interface PaymentSelectionViewController : UIViewController<CLLocationManagerDelegate,UIScrollViewDelegate>

@property (nonatomic, assign) CGPoint lastContentOffset;
@property (nonatomic, strong) CLLocationManager *locationManager;
@property (nonatomic, strong) CLGeocoder *geocoder;
@property (nonatomic, strong) CLPlacemark *placemark;
@end
