//
//  RoleSelectionViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 8/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "RoleSelectionViewController.h"

@interface RoleSelectionViewController ()<LoginManagerDelegate,ProfileManagerDelegate, PromotionManagerDelegate, UIScrollViewDelegate,MBProgressHUDDelegate>{
//    Traveller *_traveller;
    Person *_person;
    MBProgressHUD *HUD;

    LoginManager *_manager;
    ProfileManager *_pmanager;
    PromotionManager *_promanager;

}
@property (nonatomic, strong) DBManager *dbManager;

@end

@implementation RoleSelectionViewController
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

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    NSLog(@"Role Selection");
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];

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
    
    _promanager = [[PromotionManager alloc]init];
    _promanager.communicator = [[PromotionCommunicator alloc]init];
    _promanager.communicator.delegate = _promanager;
    _promanager.delegate = self;
    
    
    self.locationManager = [[CLLocationManager alloc] init];
    self.locationManager.delegate = self;
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    self.geocoder = [[CLGeocoder alloc] init];
    //for iOS 8
    if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [self.locationManager requestWhenInUseAuthorization];
    }

    _facebookId = [ventouraUtility returnMyFacbeookId];

}

#pragma mark Control Events

- (void)guideLogin {
    NSLog(@"Login Start: Guide with FB Id  %@", _facebookId);
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];
    _userIsGuide = YES;
    [_manager fetchLoginByFacebookId:_facebookId isGuide:_userIsGuide];
}



//login btn click
- (void)travellerLogin {
    NSLog(@"Login Start: Traveller with FB Id  %@", _facebookId);
//    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];
    _userIsGuide = NO;
    [_manager fetchLoginByFacebookId:_facebookId isGuide:_userIsGuide];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


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
        self.view.backgroundColor = [ventouraUtility ventouraBlue];
    }else{
        self.view.backgroundColor = [ventouraUtility ventouraPink];

    }
}

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
    
    UILabel *loginAsLabel =  [[UILabel alloc] initWithFrame: CGRectMake(ventouraScreenWidth/2 - 100/2,[ventouraUtility getScreenHeight] - 100,100,40)];
    loginAsLabel.text = @"Login As"; //etc...
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
    travellerView.backgroundColor = [ventouraUtility ventouraPink];
    [scrollView addSubview:travellerView];
    
    //traveller Login Ttile
    UILabel *travellerTitleLabel =  [[UILabel alloc] initWithFrame: CGRectMake(30,240-40,320,40)];
    travellerTitleLabel.text = @"Travelling the globe?"; //etc...
    travellerTitleLabel.font = [UIFont fontWithName:@"Roboto-Light" size:20];
    travellerTitleLabel.textColor = [UIColor whiteColor];
    [travellerView addSubview:travellerTitleLabel];
    
    
    UIButton *loginTrallverBtn = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [loginTrallverBtn addTarget:self
                      action:@selector(travellerLogin)
            forControlEvents:UIControlEventTouchUpInside];
    [loginTrallverBtn setTitle:@"Traveller" forState:UIControlStateNormal];
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
    guideView.backgroundColor = [ventouraUtility ventouraBlue];
    [scrollView addSubview:guideView];
    
    scrollView.contentSize = CGSizeMake(580, 600);
    [scrollView setDelegate:self];
    [scrollView setPagingEnabled:YES];
//    scrollView.pagingEnabled = YES;
    [self.view addSubview:scrollView];
    
    //guide Login Ttile
    UILabel *guideTitleLabel =  [[UILabel alloc] initWithFrame: CGRectMake(30,240-40,320,40)];
    guideTitleLabel.text = @"Know your city?"; //etc...
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
               action:@selector(guideLogin)
     forControlEvents:UIControlEventTouchUpInside];
    [loginGuideBtn setTitle:@"Local" forState:UIControlStateNormal];
    loginGuideBtn.frame = CGRectMake(290/2 - 150/2 - 15,[ventouraUtility getScreenHeight] - 50,150,40);
    [loginGuideBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    loginGuideBtn.titleLabel.font = [UIFont fontWithName:@"Roboto-Light" size:30];
    [guideView addSubview:loginGuideBtn];

}



- (void) saveSystemSettings{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [[NSUserDefaults standardUserDefaults] setObject:_facebookId forKey:@"userFacebookId"];
    //    _userVentouraID = @"2";
    [[NSUserDefaults standardUserDefaults] setObject:_userVentouraID forKey:@"userVentouraId"];
    [[NSUserDefaults standardUserDefaults] setObject:UIImagePNGRepresentation(_fbUIImage) forKey:@"userFacebookProfile"];
    //    isGuide = 1;
    //still need a is guide bool, add it later
    [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"userisGuide"];
    [[NSUserDefaults standardUserDefaults] setBool:true forKey:@"userVentouraIsLoggedIn"];
    [[NSUserDefaults standardUserDefaults] setObject: [_fbUserObject objectForKey:@"first_name"] forKey:@"userFirstName"];
    [[NSUserDefaults standardUserDefaults] setObject: [_fbUserObject objectForKey:@"last_name"] forKey:@"userLastName"];
    
    
    
    
    [defaults synchronize];
}


#pragma mark - TravellerDelegate


- (void)didReceivePersonForLogin:(Person *)person
{
    _person = person;
    NSLog(@"didReceiveTraveller, the login fetch %@", person.ventouraId);
    //  if -1 make a new character. otherwise login complete.
    if ([_person.ventouraId isEqualToString:@"-1"]){
        NSLog(@"first log in, creating account");
        //fetch img and send it with it too for creation
         [self fetchFBMyProfileImage];
        
        //if isGuide go to payment selection Page
        if (_userIsGuide) {
            NSLog(@"Jump tp Guide");            
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
                dispatch_sync(dispatch_get_main_queue(), ^{
                    [MBProgressHUD hideHUDForView:self.view animated:YES];
                    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                         bundle:nil];
                    PaymentSelectionViewController *viewController =
                    [storyboard instantiateViewControllerWithIdentifier:@"PaymentSelectionViewController"];
                    [self presentViewController:viewController animated:YES completion:NULL];
                });
            });
            
        }else{
            //creating account for the first time, ask location data from here? - maybe find a better place to put it
            NSLog(@"Start Creating Account");
            [self.locationManager startUpdatingLocation];
        }
    }else{
        //login done
        
        //post device token, no return stuff for now.
        //TO FIX , a better place for this /also needs to check token when already logged in
        
        
        [_manager fetchLoginByFacebookIdWithDeviceToken:_fbUserObject.objectID isGuide:NO];
        
        
        _userVentouraID = _person.ventouraId;
        [ventouraUtility saveUserInfo];
        [_pmanager fetchUserProfile:[ventouraUtility isUserGuide] ventouraId:_userVentouraID];
        
    }
    
}

- (void)didReceiveTravellerCreate:(Person *)traveller
{
    
    _person = traveller;
    NSLog(@"Creation Done %@", _person.ventouraId); //save this id
    _userVentouraID = _person.ventouraId;
    [ventouraUtility saveUserInfo];
    //should fecth for traveller info.
    
    NSLog(@"Token Post Start start!!");
    [_manager fetchLoginByFacebookIdWithDeviceToken:_fbUserObject.objectID isGuide:NO];
    [_pmanager fetchCreatedUserProfile:0 ventouraId:_userVentouraID];
    //    [self showAppContent];
    //    fetchCreatedUserProfile
}


#pragma mark - Profile Delegates

-(void)didReceiveCreatedTravellerProfile:(Person *)personProfile{
    NSLog(@"Creation Done %@", _person.ventouraId); //save this id

    _person.city = personProfile.city;
    _person.country = personProfile.country;
    _person.age = personProfile.age;
    _person.firstName = personProfile.firstName;
    _person.textBiography  = personProfile.textBiography;
    _person.images = personProfile.images;
    //save Profile/or update
    
    [ventouraDatabaseUtility saveTravellerProfileDataFromDatabase:self.dbManager userId:[ventouraUtility returnMyUserIdWithType] name:_person.firstName city:_person.city country:_person.country age:_person.age textBio:_person.textBiography];
    
    
    //flush the database
    [ventouraDatabaseUtility flushImageData:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
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

    //save the facebook image here using the first image id.
    //then do some probing
    if(_person.images.count>0){
        NSString * imageId =_person.images[0];
        [self saveFacebookImage:imageId];
    }
    [self showAppContent];

}

-(void)didReceiveTravellerProfile:(Person *)personProfile{
    
    NSLog(@"Did Receive Traveller Profile");
    _person.city = personProfile.city;
    _person.country = personProfile.country;
    _person.age = personProfile.age;
    _person.firstName = personProfile.firstName;
    _person.textBiography  = personProfile.textBiography;
    _person.images = personProfile.images;
    //save Profile/or update
    
    [ventouraDatabaseUtility saveTravellerProfileDataFromDatabase:self.dbManager userId:[ventouraUtility returnMyUserIdWithType] name:_person.firstName city:_person.city country:_person.country age:_person.age textBio:_person.textBiography];
    
    
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
    

    NSLog(@"Login info fetched, now we should just get the image, and then probe promotion?");
    
    if (_person.images.count>0) {
        NSLog(@"need to start fetching images");
        [_pmanager fetchUserProfileImageWithId:_person.images[0] isGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imagePosition:0];
    }
    

}

-(void) didReceiveGuideProfile:(Person *)personProfile{
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
    
    if (_person.images.count>0) {
        NSLog(@"need to start fetching images");
        [_pmanager fetchUserProfileImageWithId:_person.images[0] isGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imagePosition:0];
    }

}


-(void) didReceiveTravllerImage{
    if([ventouraUtility isUserGuide]){
        [self showAppContent];
    }else{
        [_promanager fetchPromotionStatus:[ventouraUtility returnMyUserId]];
    }
}


- (void)fetchFBMyProfileImage{
    NSString *pictureURLString=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large&width=600&height=600", _fbUserObject.objectID];
    NSLog(@"Begin fetchFBMyProfileImage" );

    NSURL *pictureURL = [NSURL URLWithString:pictureURLString];
    NSError* error = nil;
    //maybe cache this pic as well.
    _fbUIImage = [UIImage imageWithData:[NSData dataWithContentsOfURL:pictureURL options:NSDataReadingUncached error:&error]];
    if (error) {
        NSLog(@"%@", [error localizedDescription]);
    } else {
        NSLog(@"Data has loaded successfully.");
    }
    NSLog(@"end fetchFBMyProfileImage" );
    
}



- (void)xmppStreamDidRegister:(XMPPStream *)sender{
    NSLog(@"im account done");
    
}


- (void)xmppStream:(XMPPStream *)sender didNotRegister:(NSXMLElement *)error{
    NSLog(@"im account not done");
}


- (void)showAppContent{

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        [ventouraUtility saveUserInfo];
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                 bundle:nil];
            SWRevealViewController *viewController =
            [storyboard instantiateViewControllerWithIdentifier:@"SWRevealViewController"];
            [self presentViewController:viewController animated:YES completion:NULL];
        });
    });
}



- (void)didReceiveTokenPost{
    NSLog(@"Token Post Done");
}
-(void)fetchingLoginFailedWithError:(NSError *)error{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);

}
-(void)fetchingProfileFailedWithError:(NSError *)error{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);


}

#pragma mark - Promotion Delegates
-(void) didReceivePromotionStatus{

    NSLog(@"promotion fetch done");
    [self showAppContent];
}

#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSLog(@"didFailWithError: %@", error);
//    UIAlertView *errorAlert = [[UIAlertView alloc]
//                               initWithTitle:@"Error" message:@"Failed to Get Your Location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
//    [errorAlert show];
    //create account goes here
    [_manager createVentouraAccountByFacebookId:_fbUserObject isGuide:false country:@"0"];

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

                [_manager createVentouraAccountByFacebookId:_fbUserObject isGuide:false country:countryName];

            }else{
                countryName =@"0";
                [_manager createVentouraAccountByFacebookId:_fbUserObject isGuide:false country:countryName];

            }
            

        } else {
            NSLog(@"%@", error.debugDescription);
            //create account goes here
            [_manager createVentouraAccountByFacebookId:_fbUserObject isGuide:false country:@"0"];


        }
    } ];
}

-(void)saveFacebookImage:(NSString *) imageId{
    UIImage *image = _fbUIImage;
    NSError *error = nil;
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *destinationPath = [path stringByAppendingPathComponent:@"/ImagesFolder"];
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
    NSString *folderName = [NSString stringWithFormat:@"t_%@",[ventouraUtility returnMyUserId]];
    
    destinationPath = [destinationPath stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",folderName]];
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
    
    
    if (image != nil){
        NSString* path  = imageId;
        NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
        
        path =[NSString stringWithFormat:@"%@/%@.png",imgPathPreFix,path];
        NSLog(@"saved in Path:%@", path);
        NSData* data = UIImagePNGRepresentation(image);
        [data writeToFile:path atomically:YES];
    }


}

@end
