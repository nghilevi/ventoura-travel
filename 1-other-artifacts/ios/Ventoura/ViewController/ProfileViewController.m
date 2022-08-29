//
//  ProfileViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 16/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ProfileViewController.h"

@interface ProfileViewController ()<ProfileManagerDelegate,MBProgressHUDDelegate>{
    ProfileManager *_pmanager;
    MBProgressHUD *HUD;

    Person *_person;
    NSInteger pageNumber;
    NSMutableArray *userImagesArr;
    NSMutableArray *userImagesFetchArr;

    NSInteger imageWidth;
    UIImage * pageIconSelected;
    UIImage * pageIcon;
    
}
@property (nonatomic, strong) UIImageView *diamondImageView;
@property (nonatomic, strong) FXPageControl *pageControlFX;
@property (nonatomic, strong) DBManager *dbManager;


@end
BOOL userIsGuide;

@implementation ProfileViewController

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
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    _person = [[Person alloc] init];
    userImagesArr = [[NSMutableArray alloc] init];
    userImagesFetchArr = [[NSMutableArray alloc] init];

    userIsGuide = [ventouraUtility isUserGuide];
    imageWidth = self.view.frame.size.width;
    self.scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, imageWidth, imageWidth)];


    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    //This makes easy for testing, we can assign the value whatever we want
    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    
    [self.revealButtonItem setTarget: self.revealViewController];
    [self.revealButtonItem setAction: @selector( revealToggle: )];
    
    [self.editProfileButtonItem setTarget: self];
    [self.editProfileButtonItem setAction: @selector( editProfileButtonClicked )];
    [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    
 
    
    _pmanager = [[ProfileManager alloc] init];
    _pmanager.communicator =[[ProfileCommunicator alloc] init];
    _pmanager.communicator.delegate = _pmanager;
    _pmanager.delegate = self;

    [self setUpPageControl];
    [self setUpBackground];

}


-(void) viewWillAppear:(BOOL)animated{
    [self.navigationController.navigationBar addSubview:self.pageControlFX];
    //delete all images in
    [userImagesArr removeAllObjects];
    [userImagesFetchArr removeAllObjects];
    
    [self.tView setContentOffset:CGPointZero animated:NO];
    //clear user image array reload everything from db!:D
    NSArray *profileResult = [ventouraDatabaseUtility userDataFromDatabase:self.dbManager userId:[ventouraUtility returnMyUserIdWithType]];
    
    if (profileResult.count>0) {
        NSLog(@"Profile Found");
        if(![ventouraUtility isUserGuide])
        {
            Person* tmp = [ProfileBuilder travellerProfileFromDatabase:profileResult[0]];
            _person.city = tmp.city;
            _person.country = tmp.country;
            _person.age = tmp.age;
            _person.firstName = tmp.firstName;
            _person.textBiography  = tmp.textBiography;
            _person.ventouraId  = tmp.ventouraId;

            
        }else{
            NSArray *attractionResult = [ventouraDatabaseUtility userAttractionsFromDatabase:self.dbManager userId:[ventouraUtility returnMyUserId]];
            Person *tmp = [ProfileBuilder guideProfileFromDatabase:profileResult[0] attracitonResults:attractionResult];
//            Person* tmp = [ProfileBuilder travellerProfileFromDatabase:profileResult[0]];
            _person.city = tmp.city;
            _person.country = tmp.country;
            _person.age = tmp.age;
            _person.firstName = tmp.firstName;
            _person.textBiography  = tmp.textBiography;
            _person.paymentMethod = tmp.paymentMethod;
            _person.tourType = tmp.tourType;
            _person.tourPrice = tmp.tourPrice;
            _person.tourLength = tmp.tourLength;
            _person.useravgReviewScoreRole = tmp.useravgReviewScoreRole;
            _person.ventouraId = tmp.ventouraId;
            _person.attractions  = tmp.attractions;
        }
        
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            dispatch_sync(dispatch_get_main_queue(), ^{
                [self.tView reloadData];
            });
        });
        //load images from database//
        NSArray *results = [ventouraDatabaseUtility userImageDataFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
        if (results.count>0) {
            NSLog(@" Image count from database %ld", results.count);
            [self buildImageArrayFromDataBase:results];
        }else{
            //this shouldnt happen, but checks it anyway.
            NSLog(@"No Image Found from Database");
        }
        //TODO Fix cache
        [self getProfileAndCleanArray];

        //* fetch the data anyway for now: cache goes here in the future*//
        
    }else{
        
        //no profile, this shouldnt really happen, but lets take care of that anyway.
        NSLog(@"Profile Not Found");
        [self getProfileAndCleanArray];

//        [_pmanager fetchUserProfile:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
//        HUD = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
//        HUD.customView = [ventouraUtility returnLoadingAnimation];
//        HUD.mode = MBProgressHUDModeCustomView;
//        HUD.delegate = self;
        //    HUD.labelText = @"Loading";
//        [HUD show:YES];
    }

    
}
-(void) viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [self.pageControlFX removeFromSuperview];

}
- (void) handleImageTap:(UIGestureRecognizer *)gestureRecognizer {
    NSLog(@"imaged tab");
}




#pragma mark - Table view data source


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
//this can be changed to return number of rows
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (userIsGuide) {
        return 6;
    }
    return 3;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    //input a case here

   

    
    switch ( indexPath.row )
    {
        case 0:
            CellIdentifier = @"parallaxCell";
            break;
        case 1:
            CellIdentifier = @"userDetail";
            break;
        case 2:
            CellIdentifier = @"tripTypeCell";
            break;
        case 3:
            CellIdentifier = @"tripTypeCell";
        case 4:
            CellIdentifier = @"tripTypeCell";
        case 5:
            CellIdentifier = @"tripTypeCell";
        case 6:
            CellIdentifier = @"tripTypeCell";

        
            break;
            
    }
    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    switch ( indexPath.row )
    {
        case 0:{
            
            [self.pageControlFX removeFromSuperview];
            for (UIView *subview in self.scrollView.subviews) {
                [subview removeFromSuperview];
            }
            self.scrollView.bounces = NO;
            self.scrollView.delegate = self;
            int inc=0;
            
            CGRect frame = CGRectMake(0, 0, imageWidth, imageWidth);
            for (int i = 0; i<[userImagesArr count]; i++) {
                CGRect sectionFrame = CGRectMake(inc,0,imageWidth,imageWidth);
                UIView *profileImageView = [[UIView alloc] initWithFrame:sectionFrame];
                UIImageView *imgView = [[UIImageView alloc] initWithFrame:frame];
                NSString* tmpStringId  = userImagesArr[i];
                NSString * imagePath = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imageId:tmpStringId];
                BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imagePath];
                if(fileExists){
                     imgView.image = [ventouraUtility imageCropSquareCentre:[UIImage imageWithContentsOfFile:imagePath]];

                }else{
                    UIImageView * activityImageView =[ventouraUtility returnImageLoadingAnimation:profileImageView];
                    [profileImageView addSubview:activityImageView];
                    [profileImageView setTintColor:[UIColor grayColor]];
                    [self runSpinAnimationOnView:activityImageView duration:50 rotations:1 repeat:10];
//                    NSLog(@"image does not  exists");
                }

                [profileImageView addSubview:imgView];
                [self.scrollView addSubview:profileImageView];

                inc += imageWidth;
            }
            
            self.scrollView.contentSize = CGSizeMake(imageWidth * [userImagesArr count], imageWidth);
            [self.scrollView setDelegate:self];
            [self.scrollView setPagingEnabled:YES];
            self.scrollView.contentSize = CGSizeMake(self.scrollView.contentSize.width,self.scrollView.frame.size.height);
            self.scrollView.pagingEnabled = YES;
            [self.scrollView setShowsHorizontalScrollIndicator:NO];
            [self.scrollView setShowsVerticalScrollIndicator:NO];
            self.scrollView.backgroundColor = [ventouraUtility ventouraNavBackgroundColour];
            [cell addSubview:self.scrollView];
            self.pageControlFX.currentPage = pageNumber;
            if ([userImagesArr count]>0) {
                self.pageControlFX.numberOfPages =  [userImagesArr count];
            }else{
                self.pageControlFX.numberOfPages = 1;
                
            }
            [self.navigationController.navigationBar addSubview:self.pageControlFX];

            [cell.contentView.superview setClipsToBounds:YES];
            UIView* header = [[UIView alloc] initWithFrame:CGRectMake(0+ 320*pageNumber,(_profileViewProfileCellHeight-_profileViewNameLabelHeight), self.scrollView.contentSize.width, _profileViewNameLabelHeight)];
            [header setTag:100];
            [header setBackgroundColor:[UIColor clearColor]];
            CAGradientLayer *gradient = [CAGradientLayer layer];
            gradient.frame = header.bounds;
            gradient.colors = [NSArray arrayWithObjects:(id)[[UIColor clearColor] CGColor], (id)[[UIColor blackColor] CGColor], nil];
            [header.layer insertSublayer:gradient atIndex:0];
            
            UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, 300, 30)];

            
            [nameLabel setTextColor:[UIColor whiteColor]];
            [nameLabel setBackgroundColor:[UIColor clearColor]];
//            [nameLabel setFont:[UIFont fontWithName: @"Trebuchet MS" size: 20.0f]];
            nameLabel.text = [NSString stringWithFormat:@"%@, %@", _person.firstName, _person.age];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:23];
            [header addSubview:nameLabel];
            
            UILabel *locationLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 33, 300, 30)];

            [locationLabel setTextColor:[UIColor whiteColor]];
            [locationLabel setBackgroundColor:[UIColor clearColor]];
            
            //crashs if there is no country
            locationLabel.text = [NSString stringWithFormat:@"Country"];
            if([ventouraUtility isUserGuide]){
                if(_person.city && [_person.city integerValue] > 0){
                    NSString *query =[NSString stringWithFormat:@"select cityName, countryId from City WHERE id =%@",_person.city];

                    NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
                    if (results.count>0) {
                        _person.country = results[0][1];
                        locationLabel.text = [NSString stringWithFormat:@"%@",results[0][0]];
                    }
                   //                   NSLog(@"city name , %@", self.arrCity[0][0]) ;

                }
            }else{
                if (_person.country && [_person.country integerValue]>0) {
                    NSString *query =[NSString stringWithFormat:@"select countryName from Country WHERE id =%@",_person.country];
                    NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
                    if (results.count>0) {
                        NSString * countryName =results[0][0];
                        locationLabel.text = [NSString stringWithFormat:@"%@",countryName];
                    }
                }
            }
            locationLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
            [header addSubview:locationLabel];
            [self.scrollView addSubview:header];
            return cell;
            break;
        }
        case 1:{
            
                UILabel *tourTypeLabel = (UILabel *)[cell viewWithTag:122];
                UIImageView *flagImageView = (UIImageView *)[cell viewWithTag:121];
                UIView *reviewView = (UIView *)[cell viewWithTag:120];
                for (UIView *subview in reviewView.subviews) {
                    [subview removeFromSuperview];
                }
                
//                [ventouraUtility printAllFonts];
                flagImageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@.png",_person.country]];//for travellers
                if ([ventouraUtility isUserGuide]) {
                    //LinLibertine_aRL.ttf
                    tourTypeLabel.font = [UIFont fontWithName:@"LinLibertineSlantedB" size:18];
                    tourTypeLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                    tourTypeLabel.text = [NSString stringWithFormat:@""];

                    if (_person.tourType && [_person.tourType integerValue]>=0) {
                        NSString *query =[NSString stringWithFormat:@"select tourName from tourType WHERE tourId =%@",_person.tourType];
                        NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
                        if (results.count>0) {
                            NSString * tourType =results[0][0];
                            tourTypeLabel.text = [NSString stringWithFormat:@"%@ Tour",tourType];
                        }
                    }
                    
                    reviewView.backgroundColor = [UIColor clearColor];
                    //start system
                    CGFloat score = (CGFloat)[_person.useravgReviewScoreRole floatValue];
//                    NSLog(@"Choose Person Score: %@", _person.useravgReviewScoreRole);
//                    CGFloat score = 5;
                    CGFloat starSize =  20;
                    int numberOfStars= floorf(score)/2;
                    CGFloat incPadding = 25;
                    int numberofHalfStar = 0;
                    if (floorf(score)/2 != numberOfStars) {
                        numberofHalfStar = 1;
                    }
                    
                    CGFloat starsWidth = starSize * (numberOfStars + numberofHalfStar) + (incPadding-starSize) * (numberOfStars + numberofHalfStar-1);
                    CGFloat leftPadding = floorf(CGRectGetWidth(reviewView.frame)/2) - starsWidth/2;
                    CGFloat topPadding = 5;
                    CGFloat inc = 0;
                    UIImage * starImage = [UIImage imageNamed:[NSString stringWithFormat:@"star_black_2.png"]];
                    UIImage * halfStartImage = [UIImage imageNamed:[NSString stringWithFormat:@"star_black_1.png"]];
                    
                    
                    //    _star = [[UIImageView alloc] initWithFrame:frame];
                    //    _star.image = countryFlagImg;
                    //    [_informationView addSubview:_star];
                    for (int i=0; i<numberOfStars; i++){
                        CGRect frame = CGRectMake(leftPadding +inc,topPadding, starSize, starSize);
                        UIImageView *stars = [[UIImageView alloc] initWithFrame:frame];
                        stars.image = starImage;
                        [reviewView addSubview:stars];
                        inc+=incPadding;
                    }
                    if (numberofHalfStar>0) {
                        CGRect frame = CGRectMake(leftPadding +inc,topPadding, starSize, starSize);
                        UIImageView *stars = [[UIImageView alloc] initWithFrame:frame];
                        stars.image = halfStartImage;
                        [reviewView addSubview:stars];
                        inc+=incPadding;
                    }
                    CGRect frame  = CGRectMake(leftPadding+inc, topPadding, 60, starSize);
                    UILabel* reviewNumberLabel = [[UILabel alloc] initWithFrame:frame];
                    reviewNumberLabel.text = @"(12)";
                    reviewNumberLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                    reviewNumberLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
                    //need review number.
//                    [reviewView addSubview:reviewNumberLabel];
                    
                }else{
                    [tourTypeLabel setHidden:YES];
                    [reviewView setHidden:YES];

                }
            

            break;
        }
        case 2:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"About Me"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
//            NSLog(@"%@",_person.textBiography);
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, 270,aboutMe.frame.size.height);
            aboutMe.text = [NSString stringWithFormat:@"%@",_person.textBiography]; //might limit the text here
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            UIView *lineSeparator = (UIView *)[cell viewWithTag:172];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
        
            break;
        }
        case 3:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"Tour Price"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
            aboutMe.text = [NSString stringWithFormat:@"£%.f",[_person.tourPrice integerValue]*1.2];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            
//            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            UIView *lineSeparator = (UIView *)[cell viewWithTag:172];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            break;
        }
        case 4:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"Tour Length"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
            aboutMe.text = [NSString stringWithFormat:@"%@ Hours",_person.tourLength];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            UIView *lineSeparator = (UIView *)[cell viewWithTag:172];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            break;
        }
        case 5:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"Attractions"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
             NSString *attractionsLabel=@"";
            if ([_person.attractions count] >0) {
                Attraction *tmp = _person.attractions[0];
                attractionsLabel = tmp.attractionName;

                for (int i=1; i<_person.attractions.count; i++) {
                    tmp = _person.attractions[i];
                    attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
                }
            }
            aboutMe.text = [NSString stringWithFormat:@"%@", attractionsLabel];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:[UIFont fontWithName:@"Roboto-Regular" size:16] constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, labelSize.width, labelSize.height);
            UIView *lineSeparator = (UIView *)[cell viewWithTag:172];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            break;
        }
    }
    cell.backgroundColor = [UIColor clearColor];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    CGFloat cellHeight = 50;
    switch ( indexPath.row )
    {
        case 0:
            //Profile View
            cellHeight = _profileViewProfileCellHeight;
            break;
        case 1:
            //Country Flag and Tour Type View
            cellHeight = 80;
            if([ventouraUtility isUserGuide]){
                cellHeight = 150;
            }
            
            break;
        case 2:{
            //About Me
            NSString *attractionsLabel = @"";
            attractionsLabel = [NSString stringWithFormat:@"%@", _person.textBiography];

            UIFont *cellFont = [UIFont fontWithName:@"Roboto-Regular" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height + 60;
            break;
        }
        case 3:
            cellHeight = 70;
            break;
        case 4:
            cellHeight = 70;
            break;
        case 5:{
            NSString *attractionsLabel = @"-1";
            
            if ([_person.attractions count] >0) {
                Attraction *tmp = _person.attractions[0];
                attractionsLabel = tmp.attractionName;
                
                for (int i=1; i<_person.attractions.count; i++) {
                    tmp = _person.attractions[i];
                    attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
                }
            }
            
            UIFont *cellFont = [UIFont fontWithName:@"Roboto-Regular" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height + 50;
            break;
        
        }

            
    }
    
    return cellHeight;
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}




#pragma mark - Scrollview Delegates

-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    //
    //
    //
    CGFloat pageWidth = self.scrollView.frame.size.width;
    float fractionalPage = self.scrollView.contentOffset.x / pageWidth;
    NSInteger page = lround(fractionalPage);
    //    self.pageControl.currentPage = page;
    self.pageControlFX.currentPage = page;
    pageNumber = page;
    
    
    if (scrollView==self.scrollView) {
        
        UITableViewCell *cell = [self.tView cellForRowAtIndexPath:0];
        CGRect rectInSuperview = [self.tView convertRect:cell.frame toView:self.view];
        float distanceFromCenter = CGRectGetHeight(self.view.frame)/2 - CGRectGetMinY(rectInSuperview);
        float difference = CGRectGetHeight(self.scrollView.frame) - CGRectGetHeight(cell.frame);
        float move = (distanceFromCenter / CGRectGetHeight(self.view.frame)) * difference;
        CGRect imageRect = self.scrollView.frame;
        imageRect.origin.y = -(difference/2)+move;
        self.scrollView.frame = imageRect;
        
        UIView* header = [self.scrollView viewWithTag:100];
        [header setFrame:CGRectMake(scrollView.contentOffset.x, (_profileViewProfileCellHeight-_profileViewNameLabelHeight)+ -1*(-(difference/2)+move), header.bounds.size.width, header.bounds.size.height)];
        
        
    }
    if (scrollView==self.tView) {
        //        NSLog(@"table scrol off set %f", scrollView.contentOffset.y);
        if (scrollView.contentOffset.y>=0) {
            UITableViewCell *cell = [self.tView cellForRowAtIndexPath:0];
            CGRect rectInSuperview = [self.tView convertRect:cell.frame toView:self.view];
            float distanceFromCenter = CGRectGetHeight(self.view.frame)/2 - CGRectGetMinY(rectInSuperview);
            float difference = CGRectGetHeight(self.scrollView.frame) - CGRectGetHeight(cell.frame);
            float move = (distanceFromCenter / CGRectGetHeight(self.view.frame)) * difference;
            CGRect imageRect = self.scrollView.frame;
            imageRect.origin.y = -(difference/2)+move;
            self.scrollView.frame = imageRect;
            
            UIView* header = [self.scrollView viewWithTag:100];
            [header setFrame:CGRectMake(header.frame.origin.x, (_profileViewProfileCellHeight-_profileViewNameLabelHeight)+ -1*(-(difference/2)+move), header.bounds.size.width, header.bounds.size.height)];
        }else{
            CGRect imageRect = CGRectMake(0, 0, self.scrollView.frame
                                          .size.width, self.scrollView.frame.size
                                          .height);
            //self.scrollView.frame;
            
            self.scrollView.frame = imageRect;
            UIView* header = [self.scrollView viewWithTag:100];
            [header setFrame:CGRectMake(header.frame.origin.x, _profileViewProfileCellHeight-_profileViewNameLabelHeight, header.bounds.size.width, header.bounds.size.height)];
        }
        
        
    }
    
}

#pragma mark - Page Control

- (IBAction)pageControlAction:(FXPageControl *)sender
{
    
    CGPoint offset = CGPointMake(sender.currentPage * self.scrollView.bounds.size.width, 0);
    [self.scrollView setContentOffset:offset animated:YES];
}


#pragma mark - Profile Delegates

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
    
    //should add code here to compare old list to new list.
    //fetch, if nothing , contiues, if something compare.
    
    //flush the database
    [ventouraDatabaseUtility flushImageData:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
    [userImagesArr removeAllObjects];
    [userImagesFetchArr removeAllObjects];
    
    // use the new img array, and save into the db
    if (_person.images.count >0) {
        for (int i = 0; i<[_person.images count]; i++) {
            NSString *tmp = _person.images[i];
            [userImagesArr addObject:tmp];
            //save images into database
            [ventouraDatabaseUtility saveTravellerProfileImageDataToDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] imgId:tmp isUserGuide:[ventouraUtility isUserGuide] isPortal:0];
            
            
            
            
            NSString* imgPath = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imageId:tmp];
//            NSString *tmpStringId = tmp;
//            tmpStringId =[NSString stringWithFormat:@"%@/%@.png",imgPathPreFix,tmpStringId];
//            NSLog(@"My Image Directory %@", tmpStringId);
            BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imgPath];
            if(!fileExists){
                [userImagesFetchArr addObject:tmp];
            }
            
        }
    }
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });

    
    //start fetching images if images are not found
    if (userImagesFetchArr.count>0) {
        NSLog(@"need to start fetching images");
        [_pmanager fetchUserProfileImageWithId:userImagesFetchArr[0] isGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imagePosition:0];
    }
    
}

-(void) didReceiveGuideProfile:(Person *)personProfile{
    NSLog(@"DONE!!");
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
    
    
    [userImagesArr removeAllObjects];
    [userImagesFetchArr removeAllObjects];
    NSLog(@"guide Image count %ld", _person.images.count);
    // use the new img array, and save into the db
    if (_person.images.count >0) {
        for (int i = 0; i<[_person.images count]; i++) {
            NSString *tmp = _person.images[i];
            [userImagesArr addObject:tmp];
            //save images into database
            [ventouraDatabaseUtility saveTravellerProfileImageDataToDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] imgId:tmp isUserGuide:[ventouraUtility isUserGuide] isPortal:0];
            
            
//            NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
//            tmpStringId =[NSString stringWithFormat:@"%@/%@.png",imgPathPreFix,tmpStringId];
            NSString *tmpStringId = tmp;
            NSString* imagePath = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imageId:tmpStringId];
            BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imagePath];
            if(!fileExists){
                [userImagesFetchArr addObject:tmp];
            }
            
        }
    }
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    
    
    //start fetching images if images are not found
    if (userImagesFetchArr.count>0) {
        NSLog(@"need to start fetching images");
        [_pmanager fetchUserProfileImageWithId:userImagesFetchArr[0] isGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imagePosition:0];
    }

    
    
    
    
//
//    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
//        // Now the image will have been loaded and decoded and is ready to rock for the main thread
//        dispatch_sync(dispatch_get_main_queue(), ^{
//            [self.tView reloadData];
//        });
//    });
//    [_pmanager fetchUserProfileImages:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];

}


-(void) didReceiveTravllerImage{
    
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    if (userImagesFetchArr.count>0) {
        [userImagesFetchArr removeObjectAtIndex:0];
    }
    if(userImagesFetchArr.count >0){
        [_pmanager fetchUserProfileImageWithId:userImagesFetchArr[0] isGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imagePosition:0];
    }
}

-(void)didReceiveImageId:(NSString *)imageId{
    NSLog(@"Done");
}

-(void) didReceivePersonProfile:(Person *)personProfile{} //TODO WHATSTHIS??

-(void) receivedDeleteUserImages{}
-(void) receivedGuideDeleteAttractions{}
-(void) receivedGuideDeleteSecrets{}
-(void) receivedUpdateTravellerProfile{}
-(void) receivedUpdateGuideProfile{}
-(void) receivedGuideUpdateAttractions{}
-(void) receivedGuideUpdateSecrets{}

//- (void)newMessageReceived:(NSDictionary *)messageContent from:(NSString *)sender{
//}

-(void) didReceiveImageId:(NSString *)imageId isPortal:(BOOL)isPortal{}
-(void) receivedSetPortalImage{}

-(void)setUpBackground{

    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    //    self.tView.backgroundColor = [ventouraUtility ventouraNavBackgroundColour];
    
    self.tView.backgroundColor = [UIColor clearColor];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT
                                      );
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }
}

-(void)setUpPageControl{
    pageIconSelected = [UIImage imageNamed:@"photoicon.png"];
    pageIcon = [UIImage imageNamed:@"CurrentPhotoIcon.png"];
    pageNumber = 0;
    NSInteger pageControlWidth = 100;
    NSInteger pageControlHeight= 30;
    self.pageControlFX = [[FXPageControl alloc] initWithFrame:CGRectMake(imageWidth/2- pageControlWidth/2, 10 ,pageControlWidth, pageControlHeight)];
    
    //    self.pageControlFX.numberOfPages =  [userImagesArr count];
    if ([userImagesArr count]>0) {
        self.pageControlFX.numberOfPages =  [userImagesArr count];
    }else{
        self.pageControlFX.numberOfPages = 1;
        
    }
    
    self.pageControlFX.defersCurrentPageDisplay = YES;
    self.pageControlFX.backgroundColor = [UIColor clearColor];
    self.pageControlFX.currentPage = pageNumber;
    self.pageControlFX.selectedDotImage = pageIconSelected;
    self.pageControlFX.dotImage = pageIcon;
}


-(void) buildImageArrayFromDataBase:(NSArray* )dataBaseResults{
    for(int i = 0; i<dataBaseResults.count; i++ ){
        // ventouraImageId, isUserGuide,isPortal,userId
        NSString *imgId = dataBaseResults[i][0];
        NSString *tmp = imgId;
        [userImagesArr addObject:tmp];
        NSString* imagePath = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imageId:tmp];
        
        BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imagePath];
        if(!fileExists){
            [userImagesFetchArr addObject:tmp];
        }

    }
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    
    //start fetching images if images are not found
    if (userImagesFetchArr.count>0) {
        [_pmanager fetchUserProfileImageWithId:userImagesFetchArr[0] isGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imagePosition:0];
    }

}

- (void) runSpinAnimationOnView:(UIView*)view duration:(CGFloat)duration rotations:(CGFloat)rotations repeat:(float)repeat;
{
    CABasicAnimation* rotationAnimation;
    rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    rotationAnimation.toValue = [NSNumber numberWithFloat: M_PI * 2.0 /* full rotation*/ * rotations * duration ];
    rotationAnimation.duration = duration;
    rotationAnimation.cumulative = YES;
    rotationAnimation.repeatCount = repeat;
    
    [view.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];
}

-(void) getProfileAndCleanArray{

    
    [_pmanager fetchUserProfile:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];

}

-(BOOL)isRowZeroVisible {
    NSArray *indexes = [self.tView indexPathsForVisibleRows];
    for (NSIndexPath *index in indexes) {
        if (index.row == 0) {
            return YES;
        }
    }
    
    return NO;
}

-(void) fetchingProfileFailedWithError:(NSError *)error{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
}



- (void)editProfileButtonClicked {
    
    
    //this should be loaded from db when cache is avaible , now: load from internetx
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    EditProfileViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"EditProfileViewController"];
    viewController.person = _person;
    viewController.imagesPath = userImagesArr;
    [[self navigationController] pushViewController:viewController animated:YES ];
}



@end
