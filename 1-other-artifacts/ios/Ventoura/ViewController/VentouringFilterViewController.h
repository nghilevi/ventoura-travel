//
//  VentouringFilterViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 15/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NMRangeSlider.h"
#import "ADTransitionController.h"
#import "DBManager.h"
#import "ventouraUtility.h"
#import "ventouraDatabaseUtility.h"
#import "CityFilterViewController.h"
@interface VentouringFilterViewController : ADTransitioningViewController
@property (nonatomic,retain) IBOutlet UITableView* tView;
@property (nonatomic,strong) NSMutableArray* citySelection;
@end
