//
//  ventouraClassAppDelegate.m
//  Ventoura
//
//  Created by Wenchao Chen on 30/06/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ventouraClassAppDelegate.h"


// Log levels: off, error, warn, info, verbose
#if DEBUG
static const int ddLogLevel = LOG_LEVEL_VERBOSE;
#else
static const int ddLogLevel = LOG_LEVEL_INFO;
#endif


@interface ventouraClassAppDelegate()
@property (nonatomic, strong) DBManager *dbManager;
- (void)setupStream;
- (void)teardownStream;

- (void)goOnline;
- (void)goOffline;

@end

@implementation ventouraClassAppDelegate

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark -
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



@synthesize xmppStream;
@synthesize xmppReconnect;
@synthesize xmppRoster;
@synthesize xmppRosterStorage;
@synthesize xmppvCardTempModule;
@synthesize xmppvCardAvatarModule;
@synthesize xmppCapabilities;
@synthesize xmppCapabilitiesStorage;
@synthesize window;

@synthesize _chatDelegate, _messageDelegate;




- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    NSLog(@"APP START");
    
    
    
    //load DB
    // Extract the notification data
//    NSDictionary *notificationPayload = launchOptions[UIApplicationLaunchOptionsRemoteNotificationKey];
    
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    //view specifies which view controller should be used up on firing. Only works for inactive App
    //Not needed for sleep App for this stage
//    NSString *viewId = [notificationPayload objectForKey:@"view"];
//    NSLog(@"Notification Payload %@", viewId);
//    UIAlertView *message = [[UIAlertView alloc] initWithTitle: [NSString stringWithFormat:@"hello , %@", viewId]
//                                                      message:@"This is your first UIAlertview message."
//                                                     delegate:nil
//                                            cancelButtonTitle:@"OK"
//                                            otherButtonTitles:nil];
//    
//    [message show];
    
    
    
    [FBLoginView class];
    [FBProfilePictureView class];
    
    [[UINavigationBar appearance] setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:[UIColor colorWithRed:167/255.0f green:167/255.0f blue:167/255.0f alpha:1.0f], NSForegroundColorAttributeName, [UIFont fontWithName:@"Hiragino Kaku Gothic ProN W3 0.0" size:20], NSFontAttributeName, nil]];
    [[UINavigationBar appearance] setTintColor:[UIColor colorWithRed:233/255.0f green:113/255.0f blue:112/255.0f alpha:1.0f]];

    // initialize defaults
    NSString *dateKey    = @"dateKey";
    NSDate *lastRead    = (NSDate *)[[NSUserDefaults standardUserDefaults] objectForKey:dateKey];
    if (lastRead == nil)     // App first run: set up user defaults.
    {
        NSDictionary *appDefaults  = [NSDictionary dictionaryWithObjectsAndKeys:[NSDate date], dateKey, nil];
        
        // do any other initialization you want to do here - e.g. the starting default values.
        // [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"should_play_sounds"];
        
        // sync the defaults to disk
        [[NSUserDefaults standardUserDefaults] registerDefaults:appDefaults];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
    [[NSUserDefaults standardUserDefaults] setObject:[NSDate date] forKey:dateKey];
    //initialise values, print defaults out/
    NSLog(@"User Face Book Id %@",  [[NSUserDefaults standardUserDefaults] stringForKey:@"userFacebookId"]);
    NSLog(@"User Ventoura Login Status %d",  [[NSUserDefaults standardUserDefaults] boolForKey:@"userVentouraIsLoggedIn"]);
    NSLog(@"User Guide Flag %d",  [[NSUserDefaults standardUserDefaults] boolForKey:@"userIsGuide"]);
    
    
    
    // Configure logging framework Need to call connect again after account create/login
    // if first loggin  need to create an account.
    //if account is null, do not connect(i think its already handled by xmpp)
    
    [DDLog addLogger:[DDTTYLogger sharedInstance] withLogLevel:XMPP_LOG_FLAG_SEND_RECV];
    
    // Setup the XMPP stream
    
    [self setupStream];
    // Setup the view controllers
    
    NSLog(@"XMPP Stream Setup Complete");
    //add code to test if its logged in or not //test if user exists here

    
    if (![self connect])
    {
        dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, 0.0 * NSEC_PER_SEC);
        dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
            NSLog(@"XMPP did not connect");
        });
    }

    
    
    
//    login = [[NSUserDefaults standardUserDefaults] boolForKey:@"userVentouraIsLoggedIn"]
//    BOOL login=1;
//    if (0) {
    if ([ventouraUtility isUserLoggedin]) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                             bundle:nil];
        SWRevealViewController *viewController =
        [storyboard instantiateViewControllerWithIdentifier:@"SWRevealViewController"];
        
        self.window.rootViewController = viewController;
        
        
    }else{
        //shoe login screen, also delete all fb caches, so user have to log in again from fb.
        //delete all fb cache first.

        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
        UIViewController *viewController = [storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
        self.window.rootViewController = viewController;

        }
    
    // Let the device know we want to receive push notifications, this can be moved to log in section?
	[[UIApplication sharedApplication] registerForRemoteNotificationTypes:
     (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    
    
    if ([application respondsToSelector:@selector(registerUserNotificationSettings:)]) {
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:(UIRemoteNotificationTypeBadge
                                                                                             |UIRemoteNotificationTypeSound
                                                                                             |UIRemoteNotificationTypeAlert) categories:nil];
        [application registerUserNotificationSettings:settings];
    } else {
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound;
        [application registerForRemoteNotificationTypes:myTypes];
    }
    
    
    return YES;
}

#ifdef __IPHONE_8_0
- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{
    //register to receive notifications
    [application registerForRemoteNotifications];
}

- (void)application:(UIApplication *)application handleActionWithIdentifier:(NSString *)identifier forRemoteNotification:(NSDictionary *)userInfo completionHandler:(void(^)())completionHandler
{
    //handle the actions
    if ([identifier isEqualToString:@"declineAction"]){
    }
    else if ([identifier isEqualToString:@"answerAction"]){
    }
}
#endif


- (void)application:(UIApplication*)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData*)deviceToken
{
    //save device token
    NSString *devToken = [[[[deviceToken description]
                            stringByReplacingOccurrencesOfString:@"<"withString:@""]
                           stringByReplacingOccurrencesOfString:@">" withString:@""]
                          stringByReplacingOccurrencesOfString: @" " withString: @""];
    NSLog(@"My token is: %@", devToken);

    _deviceToken = devToken;
}

- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error
{
	NSLog(@"Failed to get token, error: %@", error);
}


- (void)dealloc
{
	[self teardownStream];
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark UIApplicationDelegate
/////

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
//    [self disconnect];
    NSString *query2 =[NSString stringWithFormat:@"select sum(unreadCount) from Match where ownerId like '%@'", [ventouraUtility returnMyUserIdWithType]];
    NSArray *badges = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query2]];
    
    if(badges.count>0){
        int unread = [badges[0][0] intValue];
        if(unread>0){
            [[UIApplication sharedApplication] setApplicationIconBadgeNumber:unread];
        }else{
            [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
        }
    }else{
        [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    }

}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
	// Use this method to release shared resources, save user data, invalidate timers, and store
	// enough application state information to restore your application to its current state in case
	// it is terminated later.
	//
	// If your application supports background execution,
	// called instead of applicationWillTerminate: when the user quits.
	
//	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
//    
//#if TARGET_IPHONE_SIMULATOR
//	DDLogError(@"The iPhone simulator does not process background network traffic. "
//			   @"Inbound traffic is queued until the keepAliveTimeout:handler: fires.");
//#endif
//    
//	if ([application respondsToSelector:@selector(setKeepAliveTimeout:handler:)])
//	{
//		[application setKeepAliveTimeout:600 handler:^{
//			
//			DDLogVerbose(@"KeepAliveHandler");
//			
//			// Do other keep alive stuff here.
//		}];
//	}
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
}


- (void)applicationDidBecomeActive:(UIApplication *)application
{
    NSLog(@"applicationDidBecomeActive");
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
//    [self connect];

   }

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.

}



- (void)handleBackgroundNotification:(NSDictionary *)notification
{
//    NSDictionary *aps = (NSDictionary *)[notification objectForKey:@"aps"];
//    NSMutableString *alert = [NSMutableString stringWithString:@""];
//    if ([aps objectForKey:@"alert"])
//    {
//        [alert appendString:(NSString *)[aps objectForKey:@"alert"]];
//    }
//    if ([notification objectForKey:@"job_id"])
//    {
//        // do something with job id
//        int jobID = [[notification objectForKey:@"job_id"] intValue];
//    }
    NSLog(@"A Push has been Received");
}


//-(void) receivedremoteNotificationHandle{
//    NSLog(@"didReceiveRemoteNotification");
//
//}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Private
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (void)setupStream
{
    NSLog(@"stream setup start");
	NSAssert(xmppStream == nil, @"Method setupStream invoked multiple times");
	
	// Setup xmpp stream
	//
	// The XMPPStream is the base class for all activity.
	// Everything else plugs into the xmppStream, such as modules/extensions and delegates.
    
	xmppStream = [[XMPPStream alloc] init];
	
#if !TARGET_IPHONE_SIMULATOR
	{
		// Want xmpp to run in the background?
		//
		// P.S. - The simulator doesn't support backgrounding yet.
		//        When you try to set the associated property on the simulator, it simply fails.
		//        And when you background an app on the simulator,
		//        it just queues network traffic til the app is foregrounded again.
		//        We are patiently waiting for a fix from Apple.
		//        If you do enableBackgroundingOnSocket on the simulator,
		//        you will simply see an error message from the xmpp stack when it fails to set the property.
		
		xmppStream.enableBackgroundingOnSocket = YES;
	}
#endif
	
	// Setup reconnect
	//
	// The XMPPReconnect module monitors for "accidental disconnections" and
	// automatically reconnects the stream for you.
	// There's a bunch more information in the XMPPReconnect header file.
	
	xmppReconnect = [[XMPPReconnect alloc] init];
	
	// Setup roster
	//
	// The XMPPRoster handles the xmpp protocol stuff related to the roster.
	// The storage for the roster is abstracted.
	// So you can use any storage mechanism you want.
	// You can store it all in memory, or use core data and store it on disk, or use core data with an in-memory store,
	// or setup your own using raw SQLite, or create your own storage mechanism.
	// You can do it however you like! It's your application.
	// But you do need to provide the roster with some storage facility.
	
	xmppRosterStorage = [[XMPPRosterCoreDataStorage alloc] init];
    //	xmppRosterStorage = [[XMPPRosterCoreDataStorage alloc] initWithInMemoryStore];
	
	xmppRoster = [[XMPPRoster alloc] initWithRosterStorage:xmppRosterStorage];
	
	xmppRoster.autoFetchRoster = YES;
	xmppRoster.autoAcceptKnownPresenceSubscriptionRequests = YES;
	
	// Setup vCard support
	//
	// The vCard Avatar module works in conjuction with the standard vCard Temp module to download user avatars.
	// The XMPPRoster will automatically integrate with XMPPvCardAvatarModule to cache roster photos in the roster.
	
	xmppvCardStorage = [XMPPvCardCoreDataStorage sharedInstance];
	xmppvCardTempModule = [[XMPPvCardTempModule alloc] initWithvCardStorage:xmppvCardStorage];
	
	xmppvCardAvatarModule = [[XMPPvCardAvatarModule alloc] initWithvCardTempModule:xmppvCardTempModule];
	
	// Setup capabilities
	//
	// The XMPPCapabilities module handles all the complex hashing of the caps protocol (XEP-0115).
	// Basically, when other clients broadcast their presence on the network
	// they include information about what capabilities their client supports (audio, video, file transfer, etc).
	// But as you can imagine, this list starts to get pretty big.
	// This is where the hashing stuff comes into play.
	// Most people running the same version of the same client are going to have the same list of capabilities.
	// So the protocol defines a standardized way to hash the list of capabilities.
	// Clients then broadcast the tiny hash instead of the big list.
	// The XMPPCapabilities protocol automatically handles figuring out what these hashes mean,
	// and also persistently storing the hashes so lookups aren't needed in the future.
	//
	// Similarly to the roster, the storage of the module is abstracted.
	// You are strongly encouraged to persist caps information across sessions.
	//
	// The XMPPCapabilitiesCoreDataStorage is an ideal solution.
	// It can also be shared amongst multiple streams to further reduce hash lookups.
	
	xmppCapabilitiesStorage = [XMPPCapabilitiesCoreDataStorage sharedInstance];
    xmppCapabilities = [[XMPPCapabilities alloc] initWithCapabilitiesStorage:xmppCapabilitiesStorage];
    
    xmppCapabilities.autoFetchHashedCapabilities = YES;
    xmppCapabilities.autoFetchNonHashedCapabilities = NO;
    
	// Activate xmpp modules
    
	[xmppReconnect         activate:xmppStream];
	[xmppRoster            activate:xmppStream];
	[xmppvCardTempModule   activate:xmppStream];
	[xmppvCardAvatarModule activate:xmppStream];
	[xmppCapabilities      activate:xmppStream];
    
	// Add ourself as a delegate to anything we may be interested in
    
	[xmppStream addDelegate:self delegateQueue:dispatch_get_main_queue()];
	[xmppRoster addDelegate:self delegateQueue:dispatch_get_main_queue()];
    
	// Optional:
	//
	// Replace me with the proper domain and port.
	// The example below is setup for a typical google talk account.
	//
	// If you don't supply a hostName, then it will be automatically resolved using the JID (below).
	// For example, if you supply a JID like 'user@quack.com/rsrc'
	// then the xmpp framework will follow the xmpp specification, and do a SRV lookup for quack.com.
	//
	// If you don't specify a hostPort, then the default (5222) will be used.
	
	[xmppStream setHostName:ventouraXMPPHostname];
    //	[xmppStream setHostPort:5222];
	
    NSLog(@"stream!!!");
    
	// You may need to alter these settings depending on the server you're connecting to
	customCertEvaluation = YES;
}

- (void)teardownStream
{
	[xmppStream removeDelegate:self];
	[xmppRoster removeDelegate:self];
	
	[xmppReconnect         deactivate];
	[xmppRoster            deactivate];
	[xmppvCardTempModule   deactivate];
	[xmppvCardAvatarModule deactivate];
	[xmppCapabilities      deactivate];
	
	[xmppStream disconnect];
	
	xmppStream = nil;
	xmppReconnect = nil;
    xmppRoster = nil;
	xmppRosterStorage = nil;
	xmppvCardStorage = nil;
    xmppvCardTempModule = nil;
	xmppvCardAvatarModule = nil;
	xmppCapabilities = nil;
	xmppCapabilitiesStorage = nil;
}

// It's easy to create XML elments to send and to read received XML elements.
// You have the entire NSXMLElement and NSXMLNode API's.
//
// In addition to this, the NSXMLElement+XMPP category provides some very handy methods for working with XMPP.
//
// On the iPhone, Apple chose not to include the full NSXML suite.
// No problem - we use the KissXML library as a drop in replacement.
//
// For more information on working with XML elements, see the Wiki article:
// https://github.com/robbiehanson/XMPPFramework/wiki/WorkingWithElements

- (void)goOnline
{
	XMPPPresence *presence = [XMPPPresence presence]; // type="available" is implicit
    
    NSString *domain = [xmppStream.myJID domain];
    
    //Google set their presence priority to 24, so we do the same to be compatible.
    
    if([domain isEqualToString:@"gmail.com"]
       || [domain isEqualToString:@"gtalk.com"]
       || [domain isEqualToString:@"talk.google.com"])
    {
        NSXMLElement *priority = [NSXMLElement elementWithName:@"priority" stringValue:@"24"];
        [presence addChild:priority];
    }
	
	[[self xmppStream] sendElement:presence];
}

- (void)goOffline
{
	XMPPPresence *presence = [XMPPPresence presenceWithType:@"unavailable"];
	
	[[self xmppStream] sendElement:presence];
}




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark XMPPStream Delegate
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (void)xmppStream:(XMPPStream *)sender socketDidConnect:(GCDAsyncSocket *)socket
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
}

- (void)xmppStream:(XMPPStream *)sender willSecureWithSettings:(NSMutableDictionary *)settings
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
	
	NSString *expectedCertName = [xmppStream.myJID domain];
	if (expectedCertName)
	{
		[settings setObject:expectedCertName forKey:(NSString *)kCFStreamSSLPeerName];
	}
	
	if (customCertEvaluation)
	{
		[settings setObject:@(YES) forKey:GCDAsyncSocketManuallyEvaluateTrust];
	}
}

/**
 * Allows a delegate to hook into the TLS handshake and manually validate the peer it's connecting to.
 *
 * This is only called if the stream is secured with settings that include:
 * - GCDAsyncSocketManuallyEvaluateTrust == YES
 * That is, if a delegate implements xmppStream:willSecureWithSettings:, and plugs in that key/value pair.
 *
 * Thus this delegate method is forwarding the TLS evaluation callback from the underlying GCDAsyncSocket.
 *
 * Typically the delegate will use SecTrustEvaluate (and related functions) to properly validate the peer.
 *
 * Note from Apple's documentation:
 *   Because [SecTrustEvaluate] might look on the network for certificates in the certificate chain,
 *   [it] might block while attempting network access. You should never call it from your main thread;
 *   call it only from within a function running on a dispatch queue or on a separate thread.
 *
 * This is why this method uses a completionHandler block rather than a normal return value.
 * The idea is that you should be performing SecTrustEvaluate on a background thread.
 * The completionHandler block is thread-safe, and may be invoked from a background queue/thread.
 * It is safe to invoke the completionHandler block even if the socket has been closed.
 *
 * Keep in mind that you can do all kinds of cool stuff here.
 * For example:
 *
 * If your development server is using a self-signed certificate,
 * then you could embed info about the self-signed cert within your app, and use this callback to ensure that
 * you're actually connecting to the expected dev server.
 *
 * Also, you could present certificates that don't pass SecTrustEvaluate to the client.
 * That is, if SecTrustEvaluate comes back with problems, you could invoke the completionHandler with NO,
 * and then ask the client if the cert can be trusted. This is similar to how most browsers act.
 *
 * Generally, only one delegate should implement this method.
 * However, if multiple delegates implement this method, then the first to invoke the completionHandler "wins".
 * And subsequent invocations of the completionHandler are ignored.
 **/
- (void)xmppStream:(XMPPStream *)sender didReceiveTrust:(SecTrustRef)trust
 completionHandler:(void (^)(BOOL shouldTrustPeer))completionHandler
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
	
	// The delegate method should likely have code similar to this,
	// but will presumably perform some extra security code stuff.
	// For example, allowing a specific self-signed certificate that is known to the app.
	
	dispatch_queue_t bgQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
	dispatch_async(bgQueue, ^{
		
		SecTrustResultType result = kSecTrustResultDeny;
		OSStatus status = SecTrustEvaluate(trust, &result);
		
		if (status == noErr && (result == kSecTrustResultProceed || result == kSecTrustResultUnspecified)) {
			completionHandler(YES);
		}
		else {
			completionHandler(NO);
		}
	});
}

- (void)xmppStreamDidSecure:(XMPPStream *)sender
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
}

- (void)xmppStreamDidConnect:(XMPPStream *)sender
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
	
	isXmppConnected = YES;
	
	NSError *error = nil;
	
	if (![[self xmppStream] authenticateWithPassword:password error:&error])
	{
		DDLogError(@"Error authenticating: %@", error);
	}
    
}

- (void)xmppStreamDidAuthenticate:(XMPPStream *)sender
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
	
	[self goOnline];
}

- (void)xmppStream:(XMPPStream *)sender didNotAuthenticate:(NSXMLElement *)error
{
    NSLog(@"didnot auth");
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
}

- (BOOL)xmppStream:(XMPPStream *)sender didReceiveIQ:(XMPPIQ *)iq
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
	
	return NO;
}

- (void)xmppStream:(XMPPStream *)sender didReceiveMessage:(XMPPMessage *)message
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
    
	// A simple example of inbound message handling.
    
	if ([message isChatMessageWithBody])
	{
            //can probs get more stuff
            NSString *msg = [[message elementForName:@"body"] stringValue];
            NSString *from = [[message attributeForName:@"from"] stringValue];
        
        
        
            NSString *msgSender = from;
        NSLog(@"sender %@", msgSender);
            NSArray*  nameArray= [msgSender componentsSeparatedByString: @"@"];
            msgSender = [nameArray objectAtIndex: 0];
        
        if (![msgSender isEqualToString:@"ventouraserver"]) {
            NSMutableDictionary *m = [[NSMutableDictionary alloc] init];
            [m setObject:msg forKey:@"msg"];
            [m setObject:msgSender forKey:@"sender"];
            
            NSString *currentUserVentouraID =[ventouraUtility returnMyUserIdWithType];
            [ventouraDatabaseUtility saveMessageToDatabase:self.dbManager ownerId:currentUserVentouraID userId:msgSender msg:msg];
            [ventouraDatabaseUtility updateUnreadCount:self.dbManager ownerId:currentUserVentouraID userId:msgSender];
            
            
            NSLog(@"DONE!");
            AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
            [_messageDelegate newMessageReceived:m from:msgSender];
        }else{
            NSLog(@"Received a server Msg");
        }
        
	}
}

//- (void)xmppStream:(XMPPStream *)sender didReceiveMessage:(XMPPMessage *)message {
//
//
//}


- (void)xmppStream:(XMPPStream *)sender didReceivePresence:(XMPPPresence *)presence
{
	DDLogVerbose(@"%@: %@ - %@", THIS_FILE, THIS_METHOD, [presence fromStr]);
}

- (void)xmppStream:(XMPPStream *)sender didReceiveError:(id)error
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
}

- (void)xmppStreamDidDisconnect:(XMPPStream *)sender withError:(NSError *)error
{
	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
	
	if (!isXmppConnected)
	{
		DDLogError(@"Unable to connect to server. Check xmppStream.hostName");
	}
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Core Data
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (NSManagedObjectContext *)managedObjectContext_roster
{
	return [xmppRosterStorage mainThreadManagedObjectContext];
}

- (NSManagedObjectContext *)managedObjectContext_capabilities
{
	return [xmppCapabilitiesStorage mainThreadManagedObjectContext];
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark XMPPRosterDelegate
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (void)xmppRoster:(XMPPRoster *)sender didReceiveBuddyRequest:(XMPPPresence *)presence
{
//	DDLogVerbose(@"%@: %@", THIS_FILE, THIS_METHOD);
//	
//	XMPPUserCoreDataStorageObject *user = [xmppRosterStorage userForJID:[presence from]
//	                                                         xmppStream:xmppStream
//	                                               managedObjectContext:[self managedObjectContext_roster]];
//	
//	NSString *displayName = [user displayName];
//	NSString *jidStrBare = [presence fromStr];
//	NSString *body = nil;
//    
//	
//	if (![displayName isEqualToString:jidStrBare])
//	{
//		body = [NSString stringWithFormat:@"Buddy request from %@ <%@>", displayName, jidStrBare];
//	}
//	else
//	{
//		body = [NSString stringWithFormat:@"Buddy request from %@", displayName];
//	}
//	
//	
//	if ([[UIApplication sharedApplication] applicationState] == UIApplicationStateActive)
//	{
//		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:displayName
//		                                                    message:body
//		                                                   delegate:nil
//		                                          cancelButtonTitle:@"Not implemented"
//		                                          otherButtonTitles:nil];
//		[alertView show];
//	}
//	else
//	{
//		// We are not active, so use a local notification instead
//		UILocalNotification *localNotification = [[UILocalNotification alloc] init];
//		localNotification.alertAction = @"Not implemented";
//		localNotification.alertBody = body;
//		
//		[[UIApplication sharedApplication] presentLocalNotificationNow:localNotification];
//	}
	
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Connect/disconnect
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (BOOL)connect
{
	if (![xmppStream isDisconnected]) {
        NSLog(@"is connected xmmp");
		return YES;
	}
    #pragma mark  TODO, what is this and move to constant
    NSString *const kXMPPmyJID = @"kXMPPmyJID";
    NSString *const kXMPPmyPassword = @"kXMPPmyPassword";
	NSString *myJID = [[NSUserDefaults standardUserDefaults] stringForKey:kXMPPmyJID];
	NSString *myPassword = [[NSUserDefaults standardUserDefaults] stringForKey:kXMPPmyPassword];
    
	//
	// If you don't want to use the Settings view to set the JID,
	// uncomment the section below to hard code a JID and password.
	//
    // create test login id here,
    NSString *currentUserVentouraID =[[NSUserDefaults standardUserDefaults] stringForKey:@"userVentouraId"];
    BOOL currentUserIsGuide =[[NSUserDefaults standardUserDefaults] boolForKey:@"userisGuide"];
    if (currentUserIsGuide) {
        //NSLog(@"g_%@",person.ventouraId);
        myJID = [NSString stringWithFormat:@"g_%@@ip-172-31-23-78",currentUserVentouraID];
    }else{
        //NSLog(@"t_%@",person.ventouraId);
        myJID = [NSString stringWithFormat:@"t_%@@ip-172-31-23-78",currentUserVentouraID];
    }
	//myJID = @"t_50@ip-172-31-23-78";
	myPassword = @"199089576961109";
    NSLog(@"LOOGING with %@ and pw %@",myJID, myPassword);

    //	http://54.191.27.22
	if (myJID == nil || myPassword == nil) {
        NSLog(@"no user/password");
		return NO;
	}
    NSLog(@"username :  %@ , password :  %@", myJID, myPassword);
	[xmppStream setMyJID:[XMPPJID jidWithString:myJID]];
	password = myPassword;
    
	NSError *error = nil;
	if (![xmppStream connectWithTimeout:XMPPStreamTimeoutNone error:&error])
	{
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error connecting"
		                                                    message:@"See console for error details."
		                                                   delegate:nil
		                                          cancelButtonTitle:@"Ok"
		                                          otherButtonTitles:nil];
		[alertView show];
        
		DDLogError(@"Error connecting: %@", error);
        
		return NO;
	}
    
	return YES;
}

- (void)disconnect
{
	[self goOffline];
	[xmppStream disconnect];
    NSLog(@" disconnect xmmp");

}

// In order to process the response you get from interacting with the Facebook login process
// and to handle any deep linking calls from Facebook
// you need to override application:openURL:sourceApplication:annotation:
// In order to process the response you get from interacting with the Facebook login process
// and to handle any deep linking calls from Facebook
// you need to override application:openURL:sourceApplication:annotation:
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    
    BOOL wasHandled = [FBAppCall handleOpenURL:url sourceApplication:sourceApplication fallbackHandler:^(FBAppCall *call) {
        if([[call appLinkData] targetURL] != nil) {
            // get the object ID string from the deep link URL
            // we use the substringFromIndex so that we can delete the leading '/' from the targetURL
            NSString *objectId = [[[call appLinkData] targetURL].path substringFromIndex:1];
            
            // now handle the deep link
            // write whatever code you need to show a view controller that displays the object, etc.
            NSLog(@" deep link: %@", objectId);
            [[[UIAlertView alloc] initWithTitle:@"Directed from Facebook"
                                        message:[NSString stringWithFormat:@"Deep link to %@", objectId]
                                       delegate:self
                              cancelButtonTitle:@"OK!"
                              otherButtonTitles:nil] show];
        } else {
            //
            NSLog(@"Unhandled deep link: %@", [[call appLinkData] targetURL]);
        }
    }];
    
    return wasHandled;
}

// A function for parsing URL parameters
- (NSDictionary*)parseURLParams:(NSString *)query {
    NSArray *pairs = [query componentsSeparatedByString:@"&"];
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    for (NSString *pair in pairs) {
        NSArray *kv = [pair componentsSeparatedByString:@"="];
        NSString *val = [[kv objectAtIndex:1]
                         stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        [params setObject:val forKey:[kv objectAtIndex:0]];
    }
    return params;
}
- (void)newMessageReceived:(NSDictionary *)messageContent from:(NSString *)sender{

}



@end
