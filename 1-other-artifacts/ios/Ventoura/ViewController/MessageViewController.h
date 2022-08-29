//
//  MessageViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 1/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ventouraClassAppDelegate.h"
#import "SMChatDelegate.h"
#import "DBManager.h"
#import "LoginManager.h"
#import "LoginCommunicator.h"
#import "XMPPFramework.h"
#import "DDLog.h"
#import "JChatViewController.h"
#import "SWRevealViewController.h"
#import "BuddyListManager.h"
#import "BuddyListManagerDelegate.h"
#import "BuddyListCommunicator.h"
#import "JSQMessagesTimestampFormatter.h"
#import "M13BadgeView.h"
#import "SWTableViewCell.h"
#import "MessageTableViewCell.h"
#import "ventouraDatabaseUtility.h"
#import "ProfileBuilder.h"
#import "ProfileManager.h"

@interface MessageViewController : UIViewController <UITableViewDelegate, NSFetchedResultsControllerDelegate, UITableViewDataSource,SMChatDelegate,SMMessageDelegate,SWTableViewCellDelegate,UIActionSheetDelegate> { 
	UITableView *tView;
    NSMutableArray *onlineBuddies;
//    UIView		*addBuddyView;
	UITextField *buddyField;
    NSFetchedResultsController *fetchedResultsController;

}
//@property (nonatomic, retain) M13BadgeView *badgeView;

@property (nonatomic) IBOutlet UIBarButtonItem* revealButtonItem;
@property (nonatomic, strong) UIColor* color;
@property (nonatomic, strong) NSString* text;
@property (nonatomic, assign) id  delegate;
@property (nonatomic,retain) IBOutlet UITableView *tView;
@end
