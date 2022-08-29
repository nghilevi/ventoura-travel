//
//  CitySelectionViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 15/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "City.h"
#import "DBManager.h"
@class TourTypeSelectionViewController;
@protocol TourTypeSelectionViewControllerDelegate <NSObject>
- (void)addItemViewController:(TourTypeSelectionViewController *)controller didFinishTourTypeSelection:(NSString *)tourType;
@end
@interface TourTypeSelectionViewController : UIViewController
@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic, weak) id <TourTypeSelectionViewControllerDelegate> delegate;
@property (nonatomic, strong) NSString *tourType;

@end
