//
//  ventouraClassAppDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 30/06/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SMChatDelegate.h"
#import "SMMessageDelegate.h"
#import <CoreData/CoreData.h>
#import "ventouraClassAppDelegate.h"
#import "XMPPFramework.h"
#import "DDLog.h"
#import <FacebookSDK/FacebookSDK.h>
#import "GCDAsyncSocket.h"
#import "XMPP.h"
#import "XMPPLogging.h"
#import "XMPPReconnect.h"
#import "XMPPCapabilitiesCoreDataStorage.h"
#import "XMPPRosterCoreDataStorage.h"
#import "XMPPvCardAvatarModule.h"
#import "XMPPvCardCoreDataStorage.h"
#import "SWRevealViewController.h"
#import "DDLog.h"
#import "DDTTYLogger.h"
#import <CFNetwork/CFNetwork.h>
#import "DBManager.h"
#import "ventouraUtility.h"
#import <AudioToolbox/AudioServices.h>
#import "ventouraDatabaseUtility.h"
@interface ventouraClassAppDelegate : UIResponder <UIApplicationDelegate, XMPPRosterDelegate>{
    UIWindow *window;
//    ColorViewController *viewController;
	
	XMPPStream *xmppStream;
	XMPPReconnect *xmppReconnect;
    XMPPRoster *xmppRoster;
	XMPPRosterCoreDataStorage *xmppRosterStorage;
    XMPPvCardCoreDataStorage *xmppvCardStorage;
	XMPPvCardTempModule *xmppvCardTempModule;
	XMPPvCardAvatarModule *xmppvCardAvatarModule;
	XMPPCapabilities *xmppCapabilities;
	XMPPCapabilitiesCoreDataStorage *xmppCapabilitiesStorage;
	
	NSString *password;
	
	BOOL customCertEvaluation;
	
	BOOL isXmppConnected;
	
	__unsafe_unretained NSObject <SMChatDelegate> *_chatDelegate;
	__unsafe_unretained NSObject <SMMessageDelegate> *_messageDelegate;


}

@property (strong, nonatomic) IBOutlet UIWindow *window;
//@property (nonatomic, retain) IBOutlet ColorViewController *viewController;

@property (nonatomic, strong, readonly) XMPPStream *xmppStream;
@property (nonatomic, strong, readonly) XMPPReconnect *xmppReconnect;
@property (nonatomic, strong, readonly) XMPPRoster *xmppRoster;
@property (nonatomic, strong, readonly) XMPPRosterCoreDataStorage *xmppRosterStorage;
@property (nonatomic, strong, readonly) XMPPvCardTempModule *xmppvCardTempModule;
@property (nonatomic, strong, readonly) XMPPvCardAvatarModule *xmppvCardAvatarModule;
@property (nonatomic, strong, readonly) XMPPCapabilities *xmppCapabilities;
@property (nonatomic, strong, readonly) XMPPCapabilitiesCoreDataStorage *xmppCapabilitiesStorage;


@property (nonatomic, assign) id  _chatDelegate;
@property (nonatomic, assign) id  _messageDelegate;

- (NSManagedObjectContext *)managedObjectContext_roster;
- (NSManagedObjectContext *)managedObjectContext_capabilities;

- (BOOL)connect;
- (void)disconnect;

@end


