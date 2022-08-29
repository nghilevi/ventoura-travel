//
//  VentouringViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 14/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "VentouringViewController.h"
#import "AGPushNoteView.h"
#import "OtherUserProfileViewController.h"

//#import "ChatViewController.h"

//static NSInteger pushCounter = 0;
static const CGFloat ChoosePersonButtonHorizontalPadding = 80.f;
static const CGFloat ChoosePersonButtonVerticalPadding = 20.f;

CGFloat horizontalPadding = 17.f;
CGFloat ventouraPinHeight = 517.f;
CGFloat ventouraPinWidth = 286.f;
CGFloat topPadding = 45.f + 258.5;

UIImageView *imageView;

@interface VentouringViewController ()<VentouraPackageManagerDelegate,MBProgressHUDDelegate>{
    NSArray *_ventouraPackage;
    VentouraPackageManager *_vmanager;
    UIImage *_backgroundImage;
    MBProgressHUD *HUD;
    ADNavigationControllerDelegate * _navigationDelegate;

}
@property (nonatomic, strong) NSMutableArray *people;


@end

@implementation VentouringViewController

- (ventouraClassAppDelegate *)appDelegate {
	return (ventouraClassAppDelegate *)[[UIApplication sharedApplication] delegate];
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        NSLog(@"custom initialisation");
        

    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    if ([[self appDelegate] connect]) {
        NSLog(@"show buddy list");
    }
    
    self.nameLabel.text = @"";
    NSLog(@"start loading json");
    _vmanager = [[VentouraPackageManager alloc] init];
    _vmanager.communicator =[[VentouraPackageCommunicator alloc] init];
    _vmanager.communicator.delegate = _vmanager;
    _vmanager.delegate = self;
    [_vmanager fetchVentouraPackage];
//    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];
    
    [self.revealButtonItem setTarget: self.revealViewController];
    [self.revealButtonItem setAction: @selector( revealToggle: )];
    [self.filterButtonItem setTarget: self];
    [self.filterButtonItem setAction: @selector( filterButtonClicked )];
    
    
    [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    

    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    
    UIGraphicsBeginImageContext(self.view.frame.size);
    [[UIImage imageNamed:@"Paris_background.png"] drawInRect:self.view.bounds];
    _backgroundImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    self.view.backgroundColor = [UIColor colorWithPatternImage:_backgroundImage];
    self.actionView.backgroundColor = [UIColor clearColor];
    self.nameLabelView.backgroundColor = [ventouraUtility ventouraNavBackgroundColour];
    //set view positions here, default to 4s, iphone 5 will have to be changed using code
    if([ventouraUtility getScreenHeight]>567){
        topPadding = 5.f + 258.5;
        self.actionView.frame = CGRectMake(0, 370, 320, 190);
    }
    
    
    [self.likeButton addTarget:self
               action:@selector(likeFrontCardView)
     forControlEvents:UIControlEventTouchUpInside];
    
    [self.nopeButton addTarget:self
               action:@selector(nopeFrontCardView)
     forControlEvents:UIControlEventTouchUpInside];

    //label setup
    [self.nameLabel setTextAlignment:NSTextAlignmentCenter];
    [self.nameLabel setTextColor:[ventouraUtility ventouraTitleColour] /*#838381*/];
    self.nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:19];
    if (![[NSUserDefaults standardUserDefaults] objectForKey:AL_SPEED_KEY]) {
        [self _defaultsSettings];
    }
    [self _retrieveSettings];
    if (AD_SYSTEM_VERSION_GREATER_THAN_7) {
        _navigationDelegate = [[ADNavigationControllerDelegate alloc] init];
        self.navigationController.delegate = _navigationDelegate;

    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/



- (NSUInteger)supportedInterfaceOrientations {
    return UIInterfaceOrientationMaskPortrait;
}

#pragma mark - MDCSwipeToChooseDelegate Protocol Methods

// This is called when a user didn't fully swipe left or right.
- (void)viewDidCancelSwipe:(UIView *)view {
    NSLog(@"You couldn't decide on %@.", self.currentPerson.name);
}

// This is called then a user swipes the view fully left or right.
- (void)view:(UIView *)view wasChosenWithDirection:(MDCSwipeDirection)direction {
    // MDCSwipeToChooseView shows "NOPE" on swipes to the left,
    // and "LIKED" on swipes to the right.
    if (self.currentPerson != NULL) {
        if (direction == MDCSwipeDirectionLeft) {
            NSLog(@"You noped %@.", self.currentPerson.ventouraId);
            [_vmanager likeOrNot:self.currentPerson likeOrNotValue:false];
        } else {
            NSLog(@"You liked %@.", self.currentPerson.ventouraId);
            [_vmanager likeOrNot:self.currentPerson likeOrNotValue:true];
        }
    }
    
    [self moveBackCardToFront];
}

#pragma mark - Internal Methods

- (void)setFrontCardView:(ChoosePersonView *)frontCardView {
    // Keep track of the person currently being chosen.
    // Quick and dirty, just for the purposes of this sample app.
    _frontCardView = frontCardView;
    self.currentPerson = frontCardView.person;
    if (self.currentPerson.firstName) {
        self.nameLabel.text = [NSString stringWithFormat:@"%@, %@", self.currentPerson.firstName,self.currentPerson.age];
    }else{
        self.nameLabel.text = [NSString stringWithFormat:@" "];

    }
}



- (ChoosePersonView *)popPersonViewWithFrame:(CGRect)frame {
    if ([self.people count] == 0) {
        return nil;
    }
    // UIView+MDCSwipeToChoose and MDCSwipeToChooseView are heavily customizable.
    // Each take an "options" argument. Here, we specify the view controller as
    // a delegate, and provide a custom callback that moves the back card view
    // based on how far the user has panned the front card view.
    // change the picture in here for the background one, then pass the profile stuff into the other
    MDCSwipeToChooseViewOptions *options = [MDCSwipeToChooseViewOptions new];
    options.delegate = self;
    options.threshold = 20.f;
    options.onPan = ^(MDCPanState *state){
        CGRect frame = [self backCardViewFrame];

        self.backCardView.frame = CGRectMake(frame.origin.x,
                                             frame.origin.y-ventouraPinHeight/2 - (state.thresholdRatio * 15.f),
                                             CGRectGetWidth(frame),
                                             CGRectGetHeight(frame));
        NSLog(@"%f",(state.thresholdRatio * 10.f));
    };
    
    // Create a personView with the top person in the people array, then pop
    // that person off the stack.
    ChoosePersonView *personView = [[ChoosePersonView alloc] initWithFrame:frame
                                                                    person:self.people[0]
                                                                   options:options];
    
    personView.delegate =self;
    personView.layer.anchorPoint = CGPointMake(0.5, 1);

    [self.people removeObjectAtIndex:0];
    return personView;
}

#pragma mark View Contruction

- (CGRect)frontCardViewFrame {

    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    return CGRectMake(horizontalPadding,screenHeight-ventouraPinHeight+topPadding,ventouraPinWidth,ventouraPinHeight);
}

- (CGRect)backCardViewFrame {
 
    CGRect frame = [self frontCardViewFrame];
    
    return  CGRectMake(frame.origin.x,
                 frame.origin.y+15,
                 CGRectGetWidth(frame),
                 CGRectGetHeight(frame));

}

// Create and add the "nope" button.
- (void)constructNopeButton {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    UIImage *image = [UIImage imageNamed:@"CrossOff.png"];
    button.frame = CGRectMake(ChoosePersonButtonHorizontalPadding,
                              CGRectGetMaxY(self.backCardView.frame) + ChoosePersonButtonVerticalPadding,
                              image.size.width,
                              image.size.height);
    [button setImage:image forState:UIControlStateNormal];
    [button setTintColor:[UIColor colorWithRed:247.f/255.f
                                         green:91.f/255.f
                                          blue:37.f/255.f
                                         alpha:1.f]];
    [button addTarget:self
               action:@selector(nopeFrontCardView)
     forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button];
}

// Create and add the "like" button.
- (void)constructLikedButton {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    UIImage *image = [UIImage imageNamed:@"liked"];
    button.frame = CGRectMake(CGRectGetMaxX(self.view.frame) - image.size.width - ChoosePersonButtonHorizontalPadding,
                              CGRectGetMaxY(self.backCardView.frame) + ChoosePersonButtonVerticalPadding,
                              image.size.width,
                              image.size.height);
    [button setImage:image forState:UIControlStateNormal];
    [button setTintColor:[UIColor colorWithRed:29.f/255.f
                                         green:245.f/255.f
                                          blue:106.f/255.f
                                         alpha:1.f]];
    [button addTarget:self
               action:@selector(likeFrontCardView)
     forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:button];
}

-(void) profileImageTapped{
//    NSLog(@"image tapped with person id %@", self.currentPerson.ventouraId);
    ADTransition * animation = [[ADScaleTransition alloc] initWithDuration:0.3 orientation:_orientation sourceRect:self.view.frame];

    [self _pushViewControllerWithTransition:animation];

    NSLog(@"after other user profile view controller loads");

}
#pragma mark Control Events

// Programmatically "nopes" the front card view.
- (void)nopeFrontCardView {
    [self.frontCardView mdc_swipeRotate:MDCSwipeDirectionLeft];

}

// Programmatically "likes" the front card view.
- (void)likeFrontCardView {
    [self.frontCardView mdc_swipeRotate:MDCSwipeDirectionRight];
}

-(void)showVentouraPin{

    self.frontCardView = [self popPersonViewWithFrame:[self frontCardViewFrame]];

    [self.view insertSubview:self.frontCardView belowSubview:self.actionView];

    
    
    
    // Display the second ChoosePersonView in back. This view controller uses
    // the MDCSwipeToChooseDelegate protocol methods to update the front and
    // back views after each user swipe.
    
    self.backCardView = [self popPersonViewWithFrame:[self backCardViewFrame]];

    [self.view insertSubview:self.backCardView belowSubview:self.frontCardView];
    NSLog(@"end showing");

}

#pragma mark Delegate For PackageFetch

-(void) didReceiveVentouraPakcage:(NSArray *)ventouraPackage{
    NSLog(@"done, should load the array into people now ;) or it should be passed into there");
    
    [_vmanager fetchVentouraPackageImages:ventouraPackage];

    
    NSLog(@"Package Delegate");

}

-(void) fetchingVentouraPackageFailedWithError:(NSError *)error{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
}


-(void) didReceiveVentouraPakcageWithImage: (NSArray *)ventouraPackage{


    _people = [ventouraPackage mutableCopy];

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];

            [self showVentouraPin];
        });
    });
    NSLog(@"the image delegate");

}

-(void)didReceiveVentouraMatch:(BOOL)isMatch{}

#pragma mark Delegate For Swipe to chose

-(void)moveBackCardToFront{
    // MDCSwipeToChooseView removes the view from the view hierarchy
    // after it is swiped (this behavior can be customized via the
    // MDCSwipeOptions class). Since the front card view is gone, we
    // move the back card to the front, and create a new back card.
    self.frontCardView = self.backCardView;
    if ((self.backCardView = [self popPersonViewWithFrame:[self backCardViewFrame]])) {
        // Fade the back card into view.
        self.backCardView.alpha = 0.f;
        [self.view insertSubview:self.backCardView belowSubview:self.frontCardView];
        [UIView animateWithDuration:0.5
                              delay:0.0
                            options:UIViewAnimationOptionCurveEaseInOut
                         animations:^{
                             self.backCardView.alpha = 1.f;
                         } completion:nil];
    }
}
-(void)didReceiveVentouraMatch:(BOOL)isMatch withName:(NSString *)name{
    NSLog(@"is match %d", isMatch);
    if(isMatch){
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            dispatch_sync(dispatch_get_main_queue(), ^{
                [AGPushNoteView showWithNotificationMessage:[NSString stringWithFormat:@"You have matched with %@! Click here to see now!",name]];

                [AGPushNoteView setMessageAction:^(NSString *message) {
                    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
                        dispatch_sync(dispatch_get_main_queue(), ^{
                            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                                 bundle:nil];
                            MessageViewController *viewController =
                            [storyboard instantiateViewControllerWithIdentifier:@"MessageViewController"];
                            [[self navigationController] pushViewController:viewController animated:YES ];
                            
                        });
                    });
                }];
            });
        });
        

    }
}
- (IBAction) likeClicked {
    //    NSLog(@"You liked %@.", self.currentPerson.ventouraId);
    //    self.frontCardView.removeFromSuperview;
    //    self.frontCardView = self.backCardView;
    
    //   [self moveBackCardToFront];
    
    
}
- (IBAction) nopeClicked {
    //    NSLog(@"You noped %@.", self.currentPerson.ventouraId);
    //    self.frontCardView.removeFromSuperview;
    //   [self moveBackCardToFront];
}
- (void)newMessageReceived:(NSDictionary *)messageContent from:(NSString *)sender{
}


- (void)filterButtonClicked {
    
    [NSTimer scheduledTimerWithTimeInterval:0.1
                                     target:self
                                   selector:@selector(delayFilterClicked)
                                   userInfo:nil
                                    repeats:NO];

    

}

-(void)delayFilterClicked{
    ADTransition * animation = [[ADScaleTransition alloc] initWithDuration:0.3 orientation:_orientation sourceRect:self.view.frame];
    [self _pushFilterViewControllerWithTransition:animation];
}

#pragma mark Delegates for user profile like or not selection

- (void)likeSelectedInOtherUserView{
    [NSTimer scheduledTimerWithTimeInterval:0.5
                                     target:self
                                   selector:@selector(timerOfLikeSelectInOtherView)
                                   userInfo:nil
                                    repeats:NO];
    
}

- (void)timerOfLikeSelectInOtherView{
    [self.frontCardView mdc_swipeRotate:MDCSwipeDirectionRight];
}

- (void)nopeSelectedInOtherUserView{
    [NSTimer scheduledTimerWithTimeInterval:0.5
                                     target:self
                                   selector:@selector(timerOfNopeSelectInOtherView)
                                   userInfo:nil
                                    repeats:NO];
}


- (void)timerOfNopeSelectInOtherView{
    [self.frontCardView mdc_swipeRotate:MDCSwipeDirectionLeft];
}



- (void)_defaultsSettings {
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    [defaults setValue:@(0.5f) forKey:AL_SPEED_KEY];
    [defaults setValue:@NO forKey:AL_NAVIGATION_BAR_HIDDEN_KEY];
    [defaults setValue:@YES forKey:AL_TOOLBAR_HIDDEN_KEY];
    [defaults setValue:@(ADTransitionRightToLeft) forKey:AL_ORIENTATION_KEY];
    [defaults synchronize];
}

- (void)_retrieveSettings {
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    _duration = [[defaults objectForKey:AL_SPEED_KEY] floatValue];
//    _orientation = [[defaults objectForKey:AL_ORIENTATION_KEY] intValue];
    _orientation = ADTransitionBottomToTop;
    BOOL navigationBarHidden = [[defaults objectForKey:AL_NAVIGATION_BAR_HIDDEN_KEY] boolValue];
    BOOL toolbarHidden = [[defaults objectForKey:AL_TOOLBAR_HIDDEN_KEY] boolValue];
    if (AD_SYSTEM_VERSION_GREATER_THAN_7) {
        [self.navigationController setNavigationBarHidden:navigationBarHidden];
        [self.navigationController setToolbarHidden:toolbarHidden];
    } else {
        [self.transitionController setNavigationBarHidden:navigationBarHidden];
        [self.transitionController setToolbarHidden:toolbarHidden];
    }
//    self.backButton.hidden = !navigationBarHidden || self.index == 0;
//    self.settingsButton.hidden = !navigationBarHidden;
}

- (void)_pushViewControllerWithTransition:(ADTransition *)transition {
    NSLog(@"ADT");
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    OtherUserProfileViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileViewController"];
    viewController.person = self.currentPerson;
    viewController.delegate = self;
    viewController.fromVentouring = TRUE;
    viewController.fromMessages = FALSE;

    if (AD_SYSTEM_VERSION_GREATER_THAN_7) {
        viewController.transition = transition;
        [self.navigationController pushViewController:viewController animated:YES];
    } else {
        [self.transitionController pushViewController:viewController withTransition:transition];
    }
}

- (void)_pushFilterViewControllerWithTransition:(ADTransition *)transition {
    NSLog(@"ADT");
    // should be loaded from db when cache is avaible , now: load from internetx
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    VentouringFilterViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"VentouringFilterViewController"];

    if (AD_SYSTEM_VERSION_GREATER_THAN_7) {
        viewController.transition = transition;
        [self.navigationController pushViewController:viewController animated:YES];
    } else {
        [self.transitionController pushViewController:viewController withTransition:transition];
    }
}


@end


