//
//  CityFilterViewController.h
//  Ventoura
//
//  Created by Jai Carlton on 6/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ventouraUtility.h"
#import "VentouringFilterViewController.h"
#import "DBManager.h"
#import "ventouraUtility.h"
#import "ventouraDatabaseUtility.h"
#import "TourManager.h"
#import "Tour.h"
#import "TourManagerDelegate.h"
#import "TourCommunicator.h"
@class CityFilterViewController;
@protocol CityFilterViewControllerDelegate <NSObject>
-(void) passCitySelection:(NSArray*) citySel;
@end

@interface CityFilterViewController : UIViewController
@property (nonatomic,retain) IBOutlet UITableView* tView;
@property (nonatomic, weak) id <CityFilterViewControllerDelegate> delegate;
@property (nonatomic) BOOL cityFilterOn;
@property (nonatomic) NSMutableArray *tripList;
@end
