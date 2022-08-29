//
// ChoosePersonView.h
//
//
//  Created by Wenchao Chen on 14/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "MDCSwipeToChoose.h"
#import "ventouraUtility.h"
#import "DBManager.h"
#import "ImageLabelView.h"
#import "Person.h"
@class Person;
@protocol ChoosePersonClassDelegate
-(void)profileImageTapped;

@end

@interface ChoosePersonView : MDCSwipeToChooseView

@property (nonatomic, strong, readonly) Person *person;

// define delegate property
@property (nonatomic, assign) id  delegate;

// define public functions
- (instancetype)initWithFrame:(CGRect)frame
                       person:(Person *)person
                      options:(MDCSwipeToChooseViewOptions *)options;

@end
