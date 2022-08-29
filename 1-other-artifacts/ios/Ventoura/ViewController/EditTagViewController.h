//
//  EditTagViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 25/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AMTagListView.h"
#import "Attraction.h"
#import "Person.h"
@interface EditTagViewController : UIViewController <UITextFieldDelegate>
@property (nonatomic, readwrite, assign) Person *person;
@property (nonatomic, strong) NSMutableArray *tags;
@property (nonatomic, strong) NSMutableArray *deletedArrayTmp;

@property (nonatomic, strong) NSMutableArray *deletedArray;

@property (nonatomic,assign) NSInteger valueType;
@property (nonatomic,retain) IBOutlet UILabel *limitLabel;
@end
