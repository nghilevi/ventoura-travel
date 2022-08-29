//
//  AboutMeEditViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 20/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>

@class AboutMeEditViewController;
@protocol AboutMeEditViewControllerDelegate <NSObject>
- (void)editAboutMeViewController:(AboutMeEditViewController *)controller didFinishEditAboutMe:(NSString *) aboutMe;
@end
@interface AboutMeEditViewController : UIViewController<UITextViewDelegate>
@property (nonatomic,retain) IBOutlet UITextView *textField;
@property (nonatomic,retain) IBOutlet UILabel *limitLabel;
@property (nonatomic,retain) IBOutlet UIScrollView *sView;

@property (nonatomic,strong) NSString *aboutMeText;
@property (nonatomic, weak) id <AboutMeEditViewControllerDelegate> delegate;

@end
