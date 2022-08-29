//
//  CitySelectionViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 15/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DBManager.h"
#import "City.h"
@class CitySelectionViewController;
@protocol CitySelectionViewControllerDelegate <NSObject>
- (void)addItemViewController:(CitySelectionViewController *)controller didFinishCitySelection:(City *)city;
@end
@interface CitySelectionViewController : UIViewController
@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic, weak) id <CitySelectionViewControllerDelegate> delegate;
@property (nonatomic, strong) City *city;

@end
