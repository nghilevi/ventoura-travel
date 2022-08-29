//
//  EditTourDetailViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 21/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
@class EditTourDetailViewController;
@protocol EditTourDetailViewControllerDelegate <NSObject>
- (void)editTourDetailViewController:(EditTourDetailViewController *)controller didFinishEditTourDetail:(NSString *) value type:(NSInteger)type;
@end
@interface EditTourDetailViewController : UIViewController<UITextFieldDelegate>
@property (nonatomic,retain) IBOutlet UITextField *textField;
@property (nonatomic,retain) IBOutlet UILabel *textLabel;

@property (nonatomic,strong) NSString *textValue;
@property (nonatomic,assign) NSInteger valueType;
@property (nonatomic, weak) id <EditTourDetailViewControllerDelegate> delegate;

@end
