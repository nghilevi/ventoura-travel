//
//  JChatViewController.h
//  
//
//  Created by Wenchao Chen on 25/07/2014.
//
//

#import <UIKit/UIKit.h>
#import "Person.h"
#import "JSQMessages.h"
#import "DBManager.h"
#import "ventouraClassAppDelegate.h"
#import "BookTourViewController.h"
#import "Emoticon.h"
#import "MessageViewController.h"
#import "OtherUserProfileViewController.h"
#import "ProfileManager.h"


@class JChatViewController;


@protocol JSQDemoViewControllerDelegate <NSObject>

- (void)didDismissJSQDemoViewController:(JChatViewController *)vc;

@end

@interface JChatViewController : JSQMessagesViewController<SMMessageDelegate,UIActionSheetDelegate,UINavigationControllerDelegate>
@property (nonatomic, readwrite, assign) Person *person;

@property (weak, nonatomic) id<JSQDemoViewControllerDelegate> delegateModal;
@property (strong, nonatomic) NSMutableArray *messages;
@property (copy, nonatomic) NSDictionary *avatars;

@property (strong, nonatomic) UIImageView *outgoingBubbleImageView;
@property (strong, nonatomic) UIImageView *incomingBubbleImageView;

- (void)bookingPressed:(UIBarButtonItem *)sender;
- (void)setupChat;
@end
