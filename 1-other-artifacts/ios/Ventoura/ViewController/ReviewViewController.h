//
//  ReviewViewController.h
//  Ventoura
//
//  Created by Jai Carlton on 3/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ventouraUtility.h"
#import "FXPageControl.h"

@class ReviewViewController;

@protocol ReviewViewControllerDelegate <NSObject>
//- (void)likeSelectedInOtherUserView;
//- (void)nopeSelectedInOtherUserView;
@end

@interface ReviewViewController : UIViewController

@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic) IBOutlet UIBarButtonItem *moreButton;
@property (nonatomic, weak) id <ReviewViewControllerDelegate> delegate;

@end
