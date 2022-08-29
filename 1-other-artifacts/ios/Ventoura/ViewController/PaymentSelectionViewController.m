//
//  PaymentSelectionViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 18/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "PaymentSelectionViewController.h"

@interface PaymentSelectionViewController ()<LoginManagerDelegate,ProfileManagerDelegate,MBProgressHUDDelegate>{
    LoginManager *_manager;
    ProfileManager *_pmanager;
    Person *_person;
    MBProgressHUD *HUD;

}
@property (nonatomic, strong) DBManager *dbManager;

@end
@implementation PaymentSelectionViewController
- (ventouraClassAppDelegate *)appDelegate {
	return (ventouraClassAppDelegate *)[[UIApplication sharedApplication] delegate];
}
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self createScrollMenu];
    [self createDiamondOverlay];
    [self createStaticContent];
    _manager = [[LoginManager alloc] init];
    _manager.communicator = [[LoginCommunicator alloc] init];
    _manager.communicator.delegate = _manager;
    _manager.delegate = self;
    
    _pmanager = [[ProfileManager alloc]init];
    _pmanager.communicator = [[ProfileCommunicator alloc]init];
    _pmanager.communicator.delegate = _pmanager;
    _pmanager.delegate = self;
    
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    self.geocoder = [[CLGeocoder alloc] init];
    //for iOS 8
    if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [self.locationManager requestWhenInUseAuthorization];
    }
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
#pragma mark - ScrollView Delegate

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
    
}

-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    _lastContentOffset = scrollView.contentOffset;
}


- (void)scrollViewWillEndDragging:(UIScrollView *)scrollView withVelocity:(CGPoint)velocity targetContentOffset:(inout CGPoint *)targetContentOffset{
    NSLog(@"Did end drag, offset: %f with velocity %f", scrollView.contentOffset.x, velocity.x);
    if (scrollView.contentOffset.x<_lastContentOffset.x) {
        NSLog(@"left");
    } else if (scrollView.contentOffset.x>_lastContentOffset.x) {
        NSLog(@"right");
    }
    
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    //colour is off.. i think
    if (scrollView.contentOffset.x >259) {
        self.view.backgroundColor = [ventouraUtility ventouraPink];
    }else{
        self.view.backgroundColor = [ventouraUtility ventouraBlue];
        
    }
}
#pragma mark - UI Setup

- (void)createStaticContent{
    UIImage * ventLogo = [UIImage imageNamed: @"Vpin.png"];
    ventLogo = [ventouraUtility imageWithImage:ventLogo scaledToHeight:100];
    CGRect ventLogoFrame = CGRectMake(ventouraScreenWidth/2 - ventLogo.size.width/2, ventouraRoleSelectionTopPadding ,ventLogo.size.width, ventLogo.size.height);
    UIImageView *ventLogoView = [[UIImageView alloc] initWithFrame:ventLogoFrame];
    [ventLogoView setImage: ventLogo];
    [self.view addSubview:ventLogoView];
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 240, self.view.bounds.size.width, 1)];
    lineView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:lineView];
    
    UILabel *loginAsLabel =  [[UILabel alloc] initWithFrame: CGRectMake(ventouraScreenWidth/2 - 160/2,[ventouraUtility getScreenHeight] - 100,160,40)];
    loginAsLabel.text = @"Earn money by"; //etc...
    loginAsLabel.textAlignment = NSTextAlignmentCenter;
    loginAsLabel.font = [UIFont fontWithName:@"Roboto-Light" size:20];
    loginAsLabel.textColor = [UIColor whiteColor];
    [self.view addSubview:loginAsLabel];
    
    UIView *bottomLineView;
    bottomLineView = [[UIView alloc] initWithFrame:CGRectMake(0, [ventouraUtility getScreenHeight] - 60, self.view.bounds.size.width, 1)];
    bottomLineView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:bottomLineView];
    
    
}
-(void)createDiamondOverlay{
    UIImage *diamondImg = [UIImage imageNamed:@"TransperentOverlay.png"];
    diamondImg = [ventouraUtility imageWithImage:diamondImg scaledToHeight:569] ;
    UIImageView * diamondImgView= [[UIImageView alloc] initWithImage:diamondImg];
    diamondImgView.frame = CGRectMake(0,0, diamondImg.size.width, diamondImg.size.height);
    self.view.backgroundColor = [UIColor redColor];
    [self.view addSubview:diamondImgView];
}
- (void)createScrollMenu
{
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 600)];
    
    CGRect travellerFrame = CGRectMake(0,0,290,600);
    UIView *travellerView = [[UIView alloc] initWithFrame:travellerFrame];
    travellerView.backgroundColor = [ventouraUtility ventouraBlue];
    [scrollView addSubview:travellerView];
    
    //traveller Login Ttile
    UILabel *travellerTitleLabel =  [[UILabel alloc] initWithFrame: CGRectMake(30,240-40,320,40)];
    travellerTitleLabel.text = @"Get paid in Cash"; //etc...
    //    travellerTitleLabel.textAlignment = NSTextAlignmentCenter;
    travellerTitleLabel.font = [UIFont fontWithName:@"Roboto-Light" size:20];
    travellerTitleLabel.textColor = [UIColor whiteColor];
    //    [self.view addSubview:travellerTitleLabel];
    [travellerView addSubview:travellerTitleLabel];
    
    
    
    //Traveller Label. need to change that to a button
    //    UILabel *loginAsLabel =  [[UILabel alloc] initWithFrame: CGRectMake(290/2 - 150/2 + 15,[ventouraUtility getScreenHeight] - 50,150,40)];
    //    loginAsLabel.text = @"Traveller"; //etc...
    //    loginAsLabel.textAlignment = NSTextAlignmentCenter;
    //    loginAsLabel.font = [UIFont fontWithName:@"Roboto-Light" size:30];
    //    loginAsLabel.textColor = [UIColor whiteColor];
    ////    [self.view addSubview:loginAsLabel];
    //    [travellerView addSubview:loginAsLabel];
    
    
    UIButton *loginTrallverBtn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [loginTrallverBtn addTarget:self
                         action:@selector(selectCash)
               forControlEvents:UIControlEventTouchUpInside];
    [loginTrallverBtn setTitle:@"Cash" forState:UIControlStateNormal];
    loginTrallverBtn.frame = CGRectMake(290/2 - 150/2 + 15,[ventouraUtility getScreenHeight] - 50,150,40);
    [loginTrallverBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    loginTrallverBtn.titleLabel.font = [UIFont fontWithName:@"Roboto-Light" size:30];
    [travellerView addSubview:loginTrallverBtn];
    
    
    //traveller info
    UILabel *travellerInfoLabel = [[UILabel alloc] initWithFrame: CGRectMake(30,240+10,250,300)];
    travellerInfoLabel.text = @"- Chat and connect with travllers at your destination.\n- Take a tour and live like a local";
    NSString *myNewLineStr = @"\n";
    travellerInfoLabel.text = [travellerInfoLabel.text stringByReplacingOccurrencesOfString:@"\\n" withString:myNewLineStr];
    travellerInfoLabel.font = [UIFont fontWithName:@"Roboto-Light" size:17];
    travellerInfoLabel.textColor = [UIColor whiteColor];
    travellerInfoLabel.numberOfLines = 0;
    [travellerInfoLabel sizeToFit];
    [travellerView addSubview:travellerInfoLabel];
    
    //guide frame
    CGRect guideFrame = CGRectMake(290,0,290,600);
    UIView *guideView = [[UIView alloc] initWithFrame:guideFrame];
    guideView.backgroundColor = [ventouraUtility ventouraPink];
    [scrollView addSubview:guideView];
    
    scrollView.contentSize = CGSizeMake(580, 600);
    [scrollView setDelegate:self];
    [scrollView setPagingEnabled:YES];
    //    scrollView.pagingEnabled = YES;
    [self.view addSubview:scrollView];
    
    //guide Login Ttile
    UILabel *guideTitleLabel =  [[UILabel alloc] initWithFrame: CGRectMake(30,240-40,320,40)];
    guideTitleLabel.text = @"Get Paid by Card"; //etc...
    guideTitleLabel.font = [UIFont fontWithName:@"Roboto-Light" size:20];
    guideTitleLabel.textColor = [UIColor whiteColor];
    [guideView addSubview:guideTitleLabel];
    //guide info
    UILabel *guideInfoLabel = [[UILabel alloc] initWithFrame: CGRectMake(30,240+10,250,300)];
    guideInfoLabel.text = @"- Craft your own tour expereince, earn cash.\n- Make international friends, in a setting you create. \n- No schedule, no boss, do it your way.";
    guideInfoLabel.text = [guideInfoLabel.text stringByReplacingOccurrencesOfString:@"\\n" withString:myNewLineStr];
    guideInfoLabel.font = [UIFont fontWithName:@"Roboto-Light" size:17];
    guideInfoLabel.textColor = [UIColor whiteColor];
    guideInfoLabel.numberOfLines = 0;
    [guideInfoLabel sizeToFit];
    [guideView addSubview:guideInfoLabel];
    
    //guide btn. need to change that to a button
    
    UIButton *loginGuideBtn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [loginGuideBtn addTarget:self
                      action:@selector(selectCard)
            forControlEvents:UIControlEventTouchUpInside];
    [loginGuideBtn setTitle:@"Card" forState:UIControlStateNormal];
    loginGuideBtn.frame = CGRectMake(290/2 - 150/2 - 15,[ventouraUtility getScreenHeight] - 50,150,40);
    [loginGuideBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    loginGuideBtn.titleLabel.font = [UIFont fontWithName:@"Roboto-Light" size:30];
    [guideView addSubview:loginGuideBtn];
    
}

#pragma mark Control Events

//login btn click
- (void)selectCash {
    
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];
    
    _payByCard = NO;
    NSLog(@"pay by cash");
//    [_manager createVentouraAccountByFacebookId:_fbUserObject payByCard:NO];
    [self.locationManager startUpdatingLocation];


}

- (void)selectCard {
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];
    
    _payByCard = YES;
    NSLog(@"pay by card");
    [self.locationManager startUpdatingLocation];

}



- (void)showAppContent
{
    
    [ventouraUtility saveUserInfo];
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    SWRevealViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"SWRevealViewController"];
    [self presentViewController:viewController animated:YES completion:NULL];
    
}


//
//- (void)xmppStreamDidRegister:(XMPPStream *)sender{
//    
//    
//    NSLog(@"im account done");
//    
//}
//
//
//- (void)xmppStream:(XMPPStream *)sender didNotRegister:(NSXMLElement *)error{
//    NSLog(@"im account not done");
//}

#pragma mark - Login Delegate

//should be guide create
- (void)didReceiveTravellerCreate:(Person *)traveller
{
    
    _person = traveller;
    // this only returns the id, will need more thigns, so i can cut down a delegate
    NSLog(@"Creation Done %@", _person.ventouraId); //save this id
    _userVentouraID = _person.ventouraId;
    [ventouraUtility saveUserInfo];
    
//    [_manager fetchLoginByFacebookIdWithDeviceToken:_fbUserObject.objectID isGuide:YES];
    [_pmanager fetchCreatedUserProfile:1 ventouraId:_userVentouraID];
}

-(void)didReceiveCreatedGuideProfile:(Person *)personProfile{
    NSLog(@"didReceiveCreatedGuideProfile");
    _person.city = personProfile.city;
    _person.firstName = personProfile.firstName;
    _person.textBiography = personProfile.textBiography;
    _person.attractions = personProfile.attractions;
    _person.localSecrets = personProfile.localSecrets;
    _person.tourPrice = personProfile.tourPrice;
    _person.tourLength = personProfile.tourLength;
    _person.age = personProfile.age;
    _person.country = personProfile.country;
    _person.tourType = personProfile.tourType;
    _person.useravgReviewScoreRole = personProfile.useravgReviewScoreRole;
    _person.images = personProfile.images;
    
    
    
    [ventouraDatabaseUtility saveGuideProfileDataFromDatabase:self.dbManager userId:[ventouraUtility returnMyUserIdWithType] name:_person.firstName city:_person.city country:_person.country age:_person.age textBio:_person.textBiography useravgReviewScore:_person.useravgReviewScoreRole tourLength:_person.tourLength tourPrice:_person.tourPrice paymentMethod:_person.paymentMethod tourType:_person.tourType];
    
    [ventouraDatabaseUtility flushAttractionData:self.dbManager userId:[ventouraUtility returnMyUserId]];
    for (int i =0; i< _person.attractions.count; i++) {
        NSLog(@"attraction Name: !" );
        Attraction * tmp = _person.attractions[i];
        [ventouraDatabaseUtility saveAttractions:self.dbManager attractionId:tmp.attractionId userId:[ventouraUtility returnMyUserId] attractionName:tmp.attractionName];
    }
    
    //flush the database
    [ventouraDatabaseUtility flushImageData:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
    
    
    // use the new img array, and save into the db
    if (_person.images.count >0) {
        for (int i = 0; i<[_person.images count]; i++) {
            NSString *tmp = _person.images[i];
            //save images into database
            [ventouraDatabaseUtility saveTravellerProfileImageDataToDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] imgId:tmp isUserGuide:[ventouraUtility isUserGuide] isPortal:0];
        }
    }
    
    if(_person.images.count>0){
        NSString * imageId =_person.images[0];
        [ventouraUtility saveFacebookImage:imageId];
    }
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            [self showAppContent];
        });
    });

}



- (void)fetchingLoginFailedWithError:(NSError *)error
{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
}


-(void)didReceiveTokenPost{

}
-(void)didReceivePersonForLogin:(Person *)person{
}



#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSLog(@"didFailWithError: %@", error);
    [_manager createVentouraAccountByFacebookId:_fbUserObject payByCard:_payByCard country:@"0"];
    
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    NSLog(@"didUpdateToLocation: %@", newLocation);
    CLLocation *currentLocation = newLocation;
    
    // Stop Location Manager
    [self.locationManager stopUpdatingLocation];
    //
    NSLog(@"Resolving the Address");
    [self.geocoder reverseGeocodeLocation:currentLocation completionHandler:^(NSArray *placemarks, NSError *error) {
        //        NSLog(@"Found placemarks: %@, error: %@", placemarks, error);
        if (error == nil && [placemarks count] > 0) {
            self.placemark = [placemarks lastObject];
            NSLog(@"%@", self.placemark.ISOcountryCode);
            //create account goes here
            NSString * countryName;
            NSString *query =[NSString stringWithFormat:@"select id from Country WHERE iso2 like '%@'",self.placemark.ISOcountryCode];
            NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
            if (results.count>0) {
                countryName =results[0][0];
                NSLog(@"%@", countryName);
                
                [_manager createVentouraAccountByFacebookId:_fbUserObject payByCard:_payByCard country:countryName];

            }else{
                countryName =@"0";
                [_manager createVentouraAccountByFacebookId:_fbUserObject payByCard:_payByCard country:countryName];

            }
            
            
        } else {
            NSLog(@"%@", error.debugDescription);
            [_manager createVentouraAccountByFacebookId:_fbUserObject payByCard:_payByCard country:@"0"];

        }
    } ];
}



@end
