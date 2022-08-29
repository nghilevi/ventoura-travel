//
//  OtherUserProfileViewController.m
//  Ventoura
//
//  Created by Jai Carlton on 29/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "OtherUserProfileViewController.h"


@interface OtherUserProfileViewController () <ProfileManagerDelegate>
{
    ProfileManager *_pmanager;
    MBProgressHUD *HUD;
    NSMutableArray *userImagesArr;
    NSMutableArray *userImagesFetchArr;
    NSMutableArray *userImagesFetchArray;
    NSInteger imageWidth;
    UIImage * pageIconSelected;
    UIImage * pageIcon;
    NSInteger pageNumber;
    BOOL fetchStarted;
}
@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) FXPageControl *pageControlFX;




@end

@implementation OtherUserProfileViewController

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) viewDidLoad
{
    [super viewDidLoad];
    
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    NSLog(@"view did load in other user prolfile view controller");
    
    userImagesArr = [[NSMutableArray alloc] init];
    userImagesFetchArr = [[NSMutableArray alloc] init];
    
    imageWidth = self.view.frame.size.width;
    self.scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, imageWidth, imageWidth)];
    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    
    _pmanager = [[ProfileManager alloc] init];
    _pmanager.communicator = [[ProfileCommunicator alloc] init];
    _pmanager.communicator.delegate = _pmanager;
    _pmanager.delegate = self;
    
    
    [self.moreButton setTarget: self];
    [self.moreButton setAction: @selector(moreButtonClicked)];
    
    
    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStylePlain target:self action:@selector(backButtonAction:)];
    self.navigationItem.leftBarButtonItem = doneButton;
//    self.navigationItem.backBarButtonItem = [[UIBarButtonItem alloc] initWithImage:nil style:UIBarButtonItemStylePlain target:nil action:nil];

    [self setUpPageControl];
    [self setUpBackground];
    NSLog(@"end of view did load");
}

-(void)didReceiveTravellerProfile:(Person*) profile{
    self.person.images = profile.images;
    NSArray *results = [ventouraDatabaseUtility userImageDataFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserId] userId:self.person.ventouraId isUserGuide:[ventouraUtility isUserGuide:self.person.userRole] ];
    [self buildImageArrayFromDataBase:results];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
}
-(void)didReceiveGuideProfile: (Person*) profile{
    self.person.images = profile.images;
    NSArray *results = [ventouraDatabaseUtility userImageDataFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserId] userId:self.person.ventouraId isUserGuide:[ventouraUtility isUserGuide:self.person.userRole] ];
    [self buildImageArrayFromDataBase:results];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
}

-(void)moreButtonClicked{
    NSLog(@"moreButtonClicked");
    [self showMoreOptionsActionSheet];
}
     
-(void) fetchUserImages
{
    userImagesFetchArr = self.person.images;
    [_pmanager fetchUserProfileImageWithId:userImagesFetchArr[0] isGuide:[ventouraUtility isUserGuide] ventouraId:self.person.ventouraId imagePosition:0];
}

-(void) viewWillAppear:(BOOL)animated{
    
    NSLog(@"start of view  will appear with userRole: %@", self.person.userRole);
    [self.navigationController.navigationBar addSubview:self.pageControlFX];
    //delete all images in
    [userImagesArr removeAllObjects];
    [userImagesFetchArr removeAllObjects];
    
    [self.tView setContentOffset:CGPointZero animated:NO];
    //cleared user image array; now reload everything from db!:D
    
    //[self fetchUserImages];
    
    //person who is logged in id
    //NSString *userIdWithType = [NSString stringWithFormat: @"t_%@",[[NSUserDefaults standardUserDefaults] stringForKey:@"userVentouraId"]];
    //if statement later putting on t or g at the front, for now its t for traveller.
    
    //just need images, the user profile data can come later (for making sure user data is updated at the time of view, but for now going off ventouring user data request)
    if(self.fromMessages)
    {
        NSLog(@"fetching user profile");
        [_pmanager fetchUserProfile:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId];
    }
    else{
    NSLog(@"selfid: %@, userid: %@, userrole: %@", [ventouraUtility returnMyUserId],self.person.ventouraId, self.person.userRole);
    NSArray *results = [ventouraDatabaseUtility userImageDataFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserId] userId:self.person.ventouraId isUserGuide:[ventouraUtility isUserGuide:self.person.userRole] ];
    [self buildImageArrayFromDataBase:results];
    }
    NSLog(@"end of view will appear");
}

-(void) viewWillDisappear:(BOOL)animated{
    NSLog(@"start of view will disappear");
    [super viewWillDisappear:animated];
    [self.pageControlFX removeFromSuperview];
    NSLog(@"end of view will disappear");
}

//checks if have images local side from person.images and grabs them if not. Then this will grab the rest of the images not
-(void) buildImageArrayFromDataBase:(NSArray* )dataBaseResults{
    NSLog(@"start of build image array from database");
    NSLog(@"images count: %li", self.person.images.count);
    
    for(int i = 0; i<self.person.images.count; i++ ){
        BOOL noMatch = FALSE;
        NSLog(@"person img id: %@", self.person.images[i]);
        for(int j = 0; i<dataBaseResults.count; j++){
             NSString *imgId = dataBaseResults[j][0];
            // ventouraImageId, isUserGuide,isPortal,userId
            NSLog(@"db img id: %@ ", imgId);
            if([imgId integerValue] == [self.person.images[i] integerValue]){
                NSLog(@"match");
                NSString *tmp = imgId;
                NSString *path = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId imageId:imgId];
                BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:path];
                if(!fileExists){
                    [userImagesFetchArr addObject:tmp];
                }
                
            }
            else{
                noMatch = TRUE;
            }
        }
        
        if(noMatch || dataBaseResults.count == 0){
            NSLog(@"no match");
            [userImagesFetchArr addObject:self.person.images[i]];
        }
    }
    NSLog(@"userImagesFetchArry count: %li", userImagesFetchArr.count);
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    
    //start fetching images if images are not found
    if (userImagesFetchArr.count>0) {
        [_pmanager fetchUserProfileImageWithId:userImagesFetchArr[0] isGuide:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId imagePosition:0];
    }
    NSLog(@"end of build image array from database");
    
}

-(void) didReceiveTravllerImage{
    NSLog(@"start of did receive traveller image");
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    if (userImagesFetchArr.count>0) {
        [userImagesFetchArr removeObjectAtIndex:0];
    }
   
    NSLog(@"userimagesfetcharr count: %li", userImagesFetchArr.count);
    if(userImagesFetchArr.count >0){
        [_pmanager fetchUserProfileImageWithId:userImagesFetchArr[0] isGuide:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId imagePosition:0];
    }

}

-(void) getProfileAndCleanArray{
    
//    NSLog(@"start of get profile and clear array");
    [_pmanager fetchUserProfile:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
//    NSLog(@"start of get profile and clear array");
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
//    NSLog(@"start #ow with row: %li", section);
    if ([ventouraUtility isUserGuide:self.person.userRole]) {
        return 8;
    }
    return 4;
}

-(UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {

    //NSLog(@"start of heightrows with row: %li", indexPath.row);
    CGFloat cellHeight = 50;
    switch ( indexPath.row )
    {
        case 0:
            //Profile View
            cellHeight = _profileViewProfileCellHeight;
            break;
        case 1:{
            //Like or not

            if(self.fromVentouring){
            
                cellHeight = 50;
            }
            else {
               cellHeight = 0;
            }
        }
            break;
        case 2:{
            //Country Flag and Tour type view
            cellHeight = 80;
            if([ventouraUtility isUserGuide:self.person.userRole]){
                cellHeight = 150;
            }
            break;
        }
        case 3:{
            //About Me
            NSString *attractionsLabel = @"";
            attractionsLabel = [NSString stringWithFormat:@"%@", self.person.textBiography];
            
            UIFont *cellFont = [UIFont fontWithName:@"Roboto-Regular" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height + 60;
            break;
        }
        case 4:
            cellHeight = 70;
            break;
        case 5:
            cellHeight = 70;
            break;
        case 6:{
            NSString *attractionsLabel = @"-1";
            
            if ([self.person.attractions count] >0) {
                Attraction *tmp = self.person.attractions[0];
                attractionsLabel = tmp.attractionName;
                
                for (int i=1; i< self.person.attractions.count; i++) {
                    tmp = self.person.attractions[i];
                    attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
                }
            }
            
            UIFont *cellFont = [UIFont fontWithName:@"Roboto-Regular" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height + 50;
            break;
            
        }
        case 7:{
            cellHeight = 50;
            break;
        }
            
            
    }

    return cellHeight;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"start of cellrow with row: %li", indexPath.row);
    static NSString *CellIdentifier = @"otherUserProfileCell";
    
    switch(indexPath.row)
    {
        case 0:
        {
            CellIdentifier = @"otherUserProfileCell";
            break;
        }
        case 1:
        {
            if(self.fromVentouring){
               CellIdentifier = @"likeOrNot";
            }
            else{
                CellIdentifier = @"otherUserProfileCell";
            }
            break;
        }
        case 2:
        {
            CellIdentifier = @"tourTypeWIthFlag";
            break;
        }
        case 3:
        {
            CellIdentifier = @"userDetails";
            break;
        }
        case 4:
        {
            CellIdentifier = @"userDetails";
            break;
        }
        case 5:
        {
            CellIdentifier = @"userDetails";
            break;
        }
        case 6:
        {
            CellIdentifier = @"userDetails";
            break;
        }
        case 7:
        {
            CellIdentifier = @"review";
            break;
        }
    }
    
//    NSLog(@"%@", CellIdentifier);
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier forIndexPath: indexPath];
//    NSLog(@"hello");
    if(cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    CGFloat width = 10.0f;
    CGFloat height = 10.0f;
    
    switch(indexPath.row)
    {
        case 0: //image and user's name, age, country
        {
            NSLog(@"start case 0 image");
            [self.pageControlFX removeFromSuperview];
            for (UIView *subview in self.scrollView.subviews) {
                [subview removeFromSuperview];
            }
            self.scrollView.bounces = NO;
            self.scrollView.delegate = self;
            
            int inc=0;

            CGRect frame = CGRectMake(0, 0, imageWidth, imageWidth);
            NSLog(@"%@", self.person.images);
            for(int i = 0; i<[self.person.images count]; i++)
            {
                NSLog(@"inc: %i",inc);
                CGRect sectionFrame = CGRectMake(inc,0,imageWidth,imageWidth);
                UIView *otherUserProfileImageView = [[UIView alloc] initWithFrame:sectionFrame];
                UIImageView *imgView = [[UIImageView alloc] initWithFrame:frame];
                NSString* tmpStringId  = self.person.images[i];

//                NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId];
//                
//                tmpStringId =[NSString stringWithFormat:@"%@/%@.png",imgPathPreFix,tmpStringId];
                
                NSString* path = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId imageId:tmpStringId];
                BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:path];
                if(fileExists){
                    //                    NSLog(@"image exists");
                    imgView.image = [ventouraUtility imageCropSquareCentre:[UIImage imageWithContentsOfFile:path]];
                    
                }else{
                    //image doesn't exist local side
                    UIImageView * activityImageView =[ventouraUtility returnImageLoadingAnimation: otherUserProfileImageView];
                    [otherUserProfileImageView addSubview:activityImageView];
                    [otherUserProfileImageView setTintColor:[UIColor grayColor]];
                    [self runSpinAnimationOnView:activityImageView duration:50 rotations:1 repeat:10];
                    
                }
               
                [otherUserProfileImageView addSubview:imgView];
                [self.scrollView addSubview:otherUserProfileImageView];
                
                inc += imageWidth;
            }
            
            
            self.scrollView.contentSize = CGSizeMake(imageWidth * [self.person.images count], imageWidth);
            [self.scrollView setDelegate:self];
            [self.scrollView setPagingEnabled:YES];
            self.scrollView.contentSize = CGSizeMake(self.scrollView.contentSize.width,self.scrollView.frame.size.height);
            self.scrollView.pagingEnabled = YES;
            [self.scrollView setShowsHorizontalScrollIndicator:NO];
            [self.scrollView setShowsVerticalScrollIndicator:NO];
            self.scrollView.backgroundColor = [ventouraUtility ventouraNavBackgroundColour];
            [cell addSubview:self.scrollView];
            self.pageControlFX.currentPage = pageNumber;
            if ([self.person.images count]>0) {
                self.pageControlFX.numberOfPages =  [self.person.images count];
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
            nameLabel.text = [NSString stringWithFormat:@"%@, %@", self.person.firstName, self.person.age];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:23];
            [header addSubview:nameLabel];
            
            UILabel *locationLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 33, 300, 30)];
            
            [locationLabel setTextColor:[UIColor whiteColor]];
            [locationLabel setBackgroundColor:[UIColor clearColor]];
            
            //crashs if there is no country
            locationLabel.text = [NSString stringWithFormat:@"Country"];
            if([ventouraUtility isUserGuide:self.person.userRole]){
                if(_person.city && [_person.city integerValue] > 0){
                    NSString *query =[NSString stringWithFormat:@"select cityName, countryId from City WHERE id =%@",self.person.city];
                    
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
            break;
        }
        case 1: //yes and nope image buttons
        {
            
        if(self.fromVentouring)
        {
            UIButton *crossButton = (UIButton *)[cell viewWithTag:200];
            
            
            for (UIView *v in crossButton.subviews) {
                if ([v isKindOfClass:[UIImageView class]]) {
                    [v removeFromSuperview];
                }
            }
            
            UIImage *cross = [UIImage imageNamed:@"CrossTrans.png"];
            CGSize crossSize  = [ventouraUtility sizeWithImage:cross scaledToHeight:30];
            cross = [ventouraUtility resizeImage:cross newSize:CGSizeMake(crossSize.width , crossSize.height)];

            UIImageView *crossView = [[UIImageView alloc]
                                      initWithImage:cross];
            crossView.frame = CGRectMake(((crossButton.frame.size.width/2)-(crossSize.width/2)+25), ((crossButton.frame.size.height/2)-(crossSize.height/2)),crossSize.width , crossSize.height);
            //TODO, image view must be removed
            [crossButton addSubview: crossView];

            
            [crossButton addTarget:self
                       action:@selector(crossPressed)
             forControlEvents:UIControlEventTouchUpInside];
            UIButton *tickButton = (UIButton *)[cell viewWithTag:201];
            for (UIView *v in tickButton.subviews) {
                if ([v isKindOfClass:[UIImageView class]]) {
                    [v removeFromSuperview];
                }
            }
            UIImage *tick = [UIImage imageNamed:@"TickTrans.png"];
            CGSize tickSize  = [ventouraUtility sizeWithImage:tick scaledToHeight:30];
            tick = [ventouraUtility resizeImage:tick newSize:CGSizeMake(tickSize.width , tickSize.height)];
            
            
            UIImageView *tickView = [[UIImageView alloc]
                                      initWithImage:tick];
            tickView.frame = CGRectMake(((tickButton.frame.size.width/2)-(tickSize.width/2)-25), ((tickButton.frame.size.height/2)-(tickSize.height/2)),tickSize.width , tickSize.height);
            [tickButton addSubview: tickView];
            [tickButton addTarget:self
                            action:@selector(tickPressed)
                  forControlEvents:UIControlEventTouchUpInside];

        }
            break;
        }
        case 2:{
            NSLog(@"case 2");

                UILabel *tourTypeLabel = (UILabel *)[cell viewWithTag:122];
                UIImageView *flagImageView = (UIImageView *)[cell viewWithTag:121];
                UIView *reviewView = (UIView *)[cell viewWithTag:120];
                for (UIView *subview in reviewView.subviews) {
                    [subview removeFromSuperview];
                }
                
                //                [ventouraUtility printAllFonts];
                flagImageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@.png",self.person.country]];//for travellers
                if ([ventouraUtility isUserGuide:self.person.userRole]) {
                    //LinLibertine_aRL.ttf
                    tourTypeLabel.font = [UIFont fontWithName:@"LinLibertineSlantedB" size:18];
                    tourTypeLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                    tourTypeLabel.text = [NSString stringWithFormat:@""];
                    
                    if (self.person.tourType && [self.person.tourType integerValue]>=0) {
                        NSString *query =[NSString stringWithFormat:@"select tourName from tourType WHERE tourId =%@",self.person.tourType];
                        NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
                        if (results.count>0) {
                            NSString * tourType =results[0][0];
                            tourTypeLabel.text = [NSString stringWithFormat:@"%@ Tour",tourType];
                        }
                    }
                    
                    reviewView.backgroundColor = [UIColor clearColor];
                    //start system
                    CGFloat score = (CGFloat)[self.person.useravgReviewScoreRole floatValue];
                    //                    NSLog(@"Choose Person Score: %@", _person.useravgReviewScoreRole);
                    
                    //testing value
                    //score = 5;
                    //testing value
                    
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
            
        case 3: //about me
        {
            NSLog(@"case 3");
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:111];
            nameLabel.text = [NSString stringWithFormat:@"About Me"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:112];
            NSLog(@"TB: %@",self.person.textBiography);
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, 270,aboutMe.frame.size.height);
            NSString * str = @"";
            /*if([self.person.textBiography isEqualToString:@""]){
                NSLog(@"matches");
                
            }
            else{
                str = self.person.textBiography;
            }*/
            aboutMe.text = [NSString stringWithFormat:@"%@", str]; //might limit the text here
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            UIView *lineSeparator = (UIView *)[cell viewWithTag:113];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            break;
        }
            
        case 4:
        {
            NSLog(@"case 4");
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:111];
            nameLabel.text = [NSString stringWithFormat:@"Tour Price"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:112];
            aboutMe.text = [NSString stringWithFormat:@"£%.f",[self.person.tourPrice integerValue]*1.2];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            
            //            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            UIView *lineSeparator = (UIView *)[cell viewWithTag:113];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            break;
        }
        case 5:
        {
            NSLog(@"case 5");
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:111];
            nameLabel.text = [NSString stringWithFormat:@"Tour Length"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:112];
            aboutMe.text = [NSString stringWithFormat:@"%@ Hours",self.person.tourLength];
            aboutMe.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            UIView *lineSeparator = (UIView *)[cell viewWithTag:113];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            break;
        }
        case 6:
        {
            NSLog(@"case 6");
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:111];
            nameLabel.text = [NSString stringWithFormat:@"Attractions"];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:112];
            NSString *attractionsLabel=@"";
            if ([self.person.attractions count] >0) {
                Attraction *tmp = self.person.attractions[0];
                attractionsLabel = tmp.attractionName;
                
                for (int i=1; i<self.person.attractions.count; i++) {
                    tmp = self.person.attractions[i];
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
            UIView *lineSeparator = (UIView *)[cell viewWithTag:113];
            lineSeparator.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];
            break;
        }
        case 7:{
            
            UIButton *reviewButton = (UIButton *)[cell viewWithTag:301];
            UIImage *arrow = [UIImage imageNamed:@"WhiteArrowTrans.png"];
            UIImageView *reviewImgView = [[UIImageView alloc]
                                     initWithImage:arrow];
            width = 10.0f;
            height = 20.0f;
            reviewImgView.frame = CGRectMake((reviewButton.frame.size.width - width - 20), ((reviewButton.frame.size.height/2)-(height/2)),width, height);
            [reviewButton addSubview: reviewImgView];
            [reviewButton addTarget:self
                           action:@selector(reviewPressed)
                 forControlEvents:UIControlEventTouchUpInside];
            UILabel *reviewLabel = (UILabel *)[cell viewWithTag:302];
            
//          //TODO can implement when person class gets review number of sorts, variable name probs be different.
            //NSString *reviewLabelText;
            //reviewLabelText = [NSString stringWithFormat:@"%@ Reviews" self.person.userReviewNumber];
            reviewLabel.text = [NSString stringWithFormat:@"100 Reviews"];
            reviewLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
            reviewLabel.textColor = [UIColor colorWithRed:1 green:1 blue:1 alpha:1];
            [reviewLabel sizeToFit];
            reviewLabel.frame = CGRectMake(((cell.frame.size.width/2)-(reviewLabel.frame.size.width/2)),((cell.frame.size.height/2 )-(reviewLabel.frame.size.height/2)), reviewLabel.frame.size.width, reviewLabel.frame.size.height);
            break;
        }
    }
//    NSLog(@"end of cell for row");
    cell.backgroundColor = [UIColor clearColor];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

-(void) reviewPressed {
    NSLog(@"review pressed");
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    ReviewViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"ReviewViewController"];
    viewController.delegate = self;
    [[self navigationController] pushViewController:viewController animated:YES ];
}

-(void)crossPressed{
    NSLog(@"Cross pressed");
    [self.delegate nopeSelectedInOtherUserView];
    [self.navigationController popViewControllerAnimated:TRUE];
    //NSLog(@"after press");
    
}

-(void)tickPressed{
    NSLog(@"Tick pressed");
    [self.delegate likeSelectedInOtherUserView];
    [self.navigationController popViewControllerAnimated:TRUE];
    
}

-(void)setUpBackground{
    
//    NSLog(@"start of setupbackground");
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
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT
                                      );
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT);
    }
}

-(void)setUpPageControl{
//    NSLog(@"start of setuppagecontrol");
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

- (IBAction)pageControlAction:(FXPageControl *)sender
{
    CGPoint offset = CGPointMake(sender.currentPage * self.scrollView.bounds.size.width, 0);
    [self.scrollView setContentOffset:offset animated:YES];
}



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

-(void)showMoreOptionsActionSheet{
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"More Options"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Report user", @"Place holder", nil];
    actionSheet.tag = 900;
    [actionSheet showInView:self.view];
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (actionSheet.tag == 900) {
        //        NSLog(@"Index = %d - Title = %@", buttonIndex, [actionSheet buttonTitleAtIndex:buttonIndex]);
        
        switch(buttonIndex) {
            case 0: { // report user
                [self reportUser];
                break;
            }
            case 1: { // place holder
                NSLog(@"place holder press");
                //make function and call here for option, also rename in ui action sheet above.
                break;
            }
        }
    }

}


-(void)doneProfile{
            [self.navigationController popViewControllerAnimated:YES];
}

-(void) backButtonAction:(id)sender
{
//    [self.navigationController popViewControllerAnimated:YES];
    [NSTimer scheduledTimerWithTimeInterval:0.1
                                     target:self
                                   selector:@selector(doneProfile)
                                   userInfo:nil
                                    repeats:NO];
}

-(void) reportUser{
    NSLog(@"report user pressed");
}


@end
