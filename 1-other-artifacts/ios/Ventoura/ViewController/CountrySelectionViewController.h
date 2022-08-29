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
@class CountrySelectionViewController;
@protocol CountrySelectionViewControllerDelegate <NSObject>
- (void)addItemViewController:(CountrySelectionViewController *)controller didFinishCountrySelection:(City *)country;
@end
@interface CountrySelectionViewController : UIViewController
@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic, weak) id <CountrySelectionViewControllerDelegate> delegate;
@property (nonatomic, strong) City *country;

@end
