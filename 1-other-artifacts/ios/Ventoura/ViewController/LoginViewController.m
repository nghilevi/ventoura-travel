//
//  ventouraClassLoginViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 1/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
//  Tests if the user has an account
//  Jump to role selection if does not,
//  logs in if account exists.


#import "LoginViewController.h"

@interface LoginViewController (){
//    LoginManager *_manager;

}
@end

@implementation LoginViewController

- (ventouraClassAppDelegate *)appDelegate {
	return (ventouraClassAppDelegate *)[[UIApplication sharedApplication] delegate];
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [[self navigationController] setNavigationBarHidden:YES animated:NO];
       
    }
    return self;
}


-(UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [[self appDelegate] disconnect];

    NSLog(@"LoginView Start!");
    

    self.loginButton.readPermissions = @[@"public_profile", @"email",@"user_birthday"];
    self.loginButton.delegate = self;
    if ([ventouraUtility isUserLoggedin]==0) {
        NSLog(@"Vent not logged incheck for fb");
        //when facebook is logged-in but ventoura is not connected, hide fb login.
        //show a loading screen/ make a connection instead/ hide all login format(isGuide setup will need some fixing)
        if (FBSession.activeSession.isOpen) {
            NSLog(@"FB is logged in, but is not logged into ventoura/ should start a vent server connection");
            
        }else{
        }
    }else{
        NSLog(@"Vent is logged in");
    }
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    
    UIImage *backgroundImg = [UIImage imageNamed:@"ParisMovingPicture.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
  
    UIImage *diamondImg = [UIImage imageNamed:@"TransperentOverlay.png"];
    diamondImg = [ventouraUtility imageWithImage:diamondImg scaledToHeight:569] ;
    UIImageView * diamondImgView= [[UIImageView alloc] initWithImage:diamondImg];
    diamondImgView.frame = CGRectMake(0,0, diamondImg.size.width, diamondImg.size.height);

    [self.view addSubview:diamondImgView];
    [self.view sendSubviewToBack: diamondImgView];
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    
    [UIView animateWithDuration:13.0f
                          delay:1.0f
                        options:UIViewAnimationOptionTransitionNone
                     animations:^(void){
                            backgroundView.center = CGPointMake(backgroundView.center.x -225, backgroundView.center.y);
                     }
                     completion:nil];
    
    self.actionView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6];
    if(screenHeight>567){
        self.actionView.frame = CGRectMake(0, 470, 320, 100);
    }else{
        self.actionView.frame = CGRectMake(0, 390, 320, 100);
    }
    

}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    NSLog(@"viewWillAppear!");

}

#pragma mark - Local Functions


- (void)showRoleSelection{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        [self saveSystemSettings];
        dispatch_sync(dispatch_get_main_queue(), ^{
            
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                 bundle:nil];
            SWRevealViewController *viewController =
            [storyboard instantiateViewControllerWithIdentifier:@"RoleSelectionViewController"];
            [self presentViewController:viewController animated:YES completion:NULL];
            
            
        });
    });
}
- (void) saveSystemSettings{
    //save information to local db as well
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [[NSUserDefaults standardUserDefaults] setObject:_facebookId forKey:@"userFacebookId"];
    [[NSUserDefaults standardUserDefaults] setObject:_userVentouraID forKey:@"userVentouraId"];
    [[NSUserDefaults standardUserDefaults] setObject:UIImagePNGRepresentation(_fbUIImage) forKey:@"userFacebookProfile"];
    //    [[NSUserDefaults standardUserDefaults] setBool:isGuide forKey:@"userisGuide"];
    [[NSUserDefaults standardUserDefaults] setObject: [_fbUserObject objectForKey:@"first_name"] forKey:@"userFirstName"];
    [[NSUserDefaults standardUserDefaults] setObject: [_fbUserObject objectForKey:@"last_name"] forKey:@"userLastName"];
    [defaults synchronize];
}

#pragma mark - Facebook Delegates

-(void)loginViewFetchedUserInfo:(FBLoginView *)loginView user:(id<FBGraphUser>)user{

//    id<FBGraphPlace> fbPlace = [user location];
//    id<FBGraphLocation> fbLocation = [fbPlace location];
//    NSString * userCity = [fbLocation city];
//    NSString * userCountry = [fbLocation country];
//    NSString * userZip = [fbLocation zip];
    
//    NSLog(@"info fetched user %@", user.location.objectID);
    _fbUserObject = user;
    _facebookId = user.objectID;
    [self showRoleSelection];

    //save all the information using facebook. user create should go in here.

}

-(void)loginViewShowingLoggedOutUser:(FBLoginView *)loginView{
    NSString *domainName = [[NSBundle mainBundle] bundleIdentifier];
    [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:domainName];
}


-(void)loginView:(FBLoginView *)loginView handleError:(NSError *)error{
    NSLog(@"%@", [error localizedDescription]);
}

-(void)loginViewShowingLoggedInUser:(FBLoginView *)loginView{
    NSLog(@"loggedin-to fb book, now open connections to ventoura server");
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark - XMPP DELEGATEs

//- (void)fetchFBMyProfileImage{
//
//}
- (void)xmppStreamDidRegister:(XMPPStream *)sender{
    
}


- (void)xmppStream:(XMPPStream *)sender didNotRegister:(NSXMLElement *)error{
}

- (void)fetchingLoginFailedWithError:(NSError *)error
{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
}

//
//-(void)didReceiveTravellerCreate:(Person *)person{
//
//}
//
//-(void)didReceivePersonForLogin:(Person *)person{
//}
//
//-(void)didReceiveTokenPost{
//}


@end
