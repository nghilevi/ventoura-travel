//
//  UserProfileViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 27/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "UserProfileViewController.h"

@interface UserProfileViewController ()<ProfileManagerDelegate>{
    ProfileManager *_pmanager;
    Person *_person;
    NSMutableArray *userImagesArr;
    NSMutableArray *userUIImagesArr;
    NSInteger imageWidth;
    UIImage * pageIconSelected;
    UIImage * pageIcon;
    
}
@property (nonatomic, strong) UIImageView *diamondImageView;
@property (nonatomic, strong) FXPageControl *pageControlFX;

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
    
    pageIconSelected = [UIImage imageNamed:@"photoicon.png"];
    pageIcon = [UIImage imageNamed:@"CurrentPhotoIcon.png"];
    
    _person = [[Person alloc] init];
    //This makes easy for testing, we can assign the value whatever we want
    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    
    userIsGuide = 1;
    imageWidth = self.view.frame.size.width;
    [self.revealButtonItem setTarget: self.revealViewController];
    [self.revealButtonItem setAction: @selector( revealToggle: )];
    
    [self.editProfileButtonItem setTarget: self];
    [self.editProfileButtonItem setAction: @selector( editProfileButtonClicked )];
    [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    
    //can load it from cache. or an object we will see.if we make a global object, class needs tobe protected.
    self.lblUsername.text = _fbUserObject.name;
    self.lblEmail.text = [_fbUserObject objectForKey:@"email"];
    
    _pmanager = [[ProfileManager alloc] init];
    _pmanager.communicator =[[ProfileCommunicator alloc] init];
    _pmanager.communicator.delegate = _pmanager;
    _pmanager.delegate = self;
    [_pmanager fetchUserProfile:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
    
    self.scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, imageWidth, imageWidth)];
    self.tView.backgroundColor = [ventouraUtility ventouraNavBackgroundColour];
    //    self.tView.decelerationRate = UIScrollViewDecelerationRateFast;
    //    self.tView.backgroundColor = [UIColor redColor];
    //    self.tView.bounces = NO;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        // Now the image will have been loaded and decoded and is ready to rock for the main thread
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    
}


-(void) viewWillAppear:(BOOL)animated{
    [self.navigationController.navigationBar addSubview:self.pageControlFX];
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
        return 7;
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
            CellIdentifier = @"userAboutMe";
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
            
            
            for (UIView *subview in self.scrollView.subviews) {
                [subview removeFromSuperview];
            }
            [self.pageControlFX removeFromSuperview];
            
            
            self.scrollView.bounces = NO;
            
            self.scrollView.delegate = self;
            int inc=0;
            
            CGRect frame = CGRectMake(0, 0, imageWidth, imageWidth);
            for (int i = 0; i<[userImagesArr count]; i++) {
                CGRect sectionFrame = CGRectMake(inc,0,imageWidth,imageWidth);
                UIView *profileImageView = [[UIView alloc] initWithFrame:sectionFrame];
                UIImageView *imgView = [[UIImageView alloc] initWithFrame:frame];
                UIImage * image = [UIImage imageWithContentsOfFile:[NSString stringWithFormat:@"%@",userImagesArr[i]]];
                imgView.image = [ventouraUtility imageCropSquareCentre:image];
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
            NSInteger pageControlWidth = 100;
            NSInteger pageControlHeight= 30;
            
            self.pageControlFX = [[FXPageControl alloc] initWithFrame:CGRectMake(imageWidth/2- pageControlWidth/2, 10 ,pageControlWidth, pageControlHeight)];
            
            self.pageControlFX.numberOfPages =  [userImagesArr count];
            self.pageControlFX.defersCurrentPageDisplay = YES;
            self.pageControlFX.backgroundColor = [UIColor clearColor];
            self.pageControlFX.currentPage = 0;
            self.pageControlFX.selectedDotSize =100;
            self.pageControlFX.selectedDotImage = pageIconSelected;
            self.pageControlFX.dotImage = pageIcon;
            
            //            [cell addSubview:self.pageControlFX];
            
            [self.navigationController.navigationBar addSubview:self.pageControlFX];
            
            [cell.contentView.superview setClipsToBounds:YES];
            
            return cell;
            break;
        }
        case 1:{
            
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:121];
            nameLabel.text = [NSString stringWithFormat:@"%@, 24", _person.firstName];
            nameLabel.font = [UIFont fontWithName:@"Roboto" size:24];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *locationLabel = (UILabel *)[cell viewWithTag:122];
            locationLabel.text = [NSString stringWithFormat:@"China"];
            locationLabel.font = [UIFont fontWithName:@"Roboto" size:20];
            locationLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
            
            break;
        }
        case 2:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:125];
            nameLabel.text = [NSString stringWithFormat:@"About Me"];
            nameLabel.font = [UIFont fontWithName:@"Roboto" size:20];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:126];
            NSLog(@"%@",_person.textBiography);
            //            CGSize newSize = CGSizeMake(270, aboutMe.frame.size.height);
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, 270,aboutMe.frame.size.height);
            aboutMe.text = [NSString stringWithFormat:@"%@",_person.textBiography]; //might limit the text here
            aboutMe.font = [UIFont fontWithName:@"Roboto" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            break;
        }
        case 3:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"Tour Price"];
            nameLabel.font = [UIFont fontWithName:@"Roboto" size:20];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
            aboutMe.text = [NSString stringWithFormat:@"%@",_person.tourPrice];
            aboutMe.font = [UIFont fontWithName:@"Roboto" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            break;
        }
        case 4:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"Tour Length"];
            nameLabel.font = [UIFont fontWithName:@"Roboto" size:20];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
            aboutMe.text = [NSString stringWithFormat:@"%@ Hours",_person.tourLength];
            aboutMe.font = [UIFont fontWithName:@"Roboto" size:16];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            break;
        }
        case 5:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"Attractions"];
            nameLabel.font = [UIFont fontWithName:@"Roboto" size:20];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
            Attraction *tmp = _person.attractions[0];
            NSString *attractionsLabel = tmp.attractionName;
            
            for (int i=1; i<_person.attractions.count; i++) {
                tmp = _person.attractions[i];
                attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
            }
            aboutMe.text = [NSString stringWithFormat:@"%@", attractionsLabel];
            aboutMe.font = [UIFont fontWithName:@"Roboto" size:16];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:[UIFont fontWithName:@"Roboto" size:16] constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, labelSize.width, labelSize.height);
            break;
        }
        case 6:{
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:170];
            nameLabel.text = [NSString stringWithFormat:@"Local Secrets"];
            nameLabel.font = [UIFont fontWithName:@"Roboto" size:20];
            nameLabel.textColor = [ventouraUtility ventouraTextHeadingGrey];
            
            UILabel *aboutMe = (UILabel *)[cell viewWithTag:171];
            Attraction *tmp = _person.localSecrets[0];
            NSString *attractionsLabel = tmp.attractionName;
            
            for (int i=1; i<_person.localSecrets.count; i++) {
                tmp = _person.localSecrets[i];
                attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
            }
            aboutMe.text = [NSString stringWithFormat:@"%@", attractionsLabel];
            aboutMe.font = [UIFont fontWithName:@"Roboto" size:16];
            aboutMe.numberOfLines = 0;
            [aboutMe sizeToFit];
            aboutMe.textColor = [ventouraUtility ventouraTextBodyGrey];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:[UIFont fontWithName:@"Roboto" size:16] constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            aboutMe.frame = CGRectMake(aboutMe.frame.origin.x, aboutMe.frame.origin.y, labelSize.width, labelSize.height);
            break;
        }
    }
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    CGFloat cellHeight = 50;
    switch ( indexPath.row )
    {
        case 0:
            cellHeight = 290;
            break;
        case 1:
            cellHeight = 60;
            break;
        case 2:
            cellHeight = 100;
            break;
        case 3:
            cellHeight = 60;
            break;
        case 4:
            cellHeight = 60;
            break;
        case 5:{
            Attraction *tmp = _person.attractions[0];
            NSString *attractionsLabel = tmp.attractionName;
            
            for (int i=1; i<_person.attractions.count; i++) {
                tmp = _person.attractions[i];
                attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
            }
            UIFont *cellFont = [UIFont fontWithName:@"Roboto" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height + 40;
            break;
            
        }
        case 6:{
            Attraction *tmp = _person.localSecrets[0];
            NSString *attractionsLabel = tmp.attractionName;
            
            for (int i=1; i<_person.localSecrets.count; i++) {
                tmp = _person.localSecrets[i];
                attractionsLabel = [NSString stringWithFormat:@"%@\n%@", attractionsLabel, tmp.attractionName];
            }
            UIFont *cellFont = [UIFont fontWithName:@"Roboto" size:16];
            CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
            CGSize labelSize = [attractionsLabel sizeWithFont:cellFont constrainedToSize:constraintSize lineBreakMode:NSLineBreakByWordWrapping];
            return labelSize.height + 40;
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

-(void)didReceiveTravellerProfile:(Person *)personProfile{
    
    NSLog(@"DONE!!");
    _person.city = personProfile.city;
    _person.firstName = personProfile.firstName;
    _person.textBiography  = personProfile.textBiography;
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        // Now the image will have been loaded and decoded and is ready to rock for the main thread
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    [_pmanager fetchUserProfileImages:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
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
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        // Now the image will have been loaded and decoded and is ready to rock for the main thread
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    [_pmanager fetchUserProfileImages:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
    
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

-(void) fetchingProfileFailedWithError:(NSError *)error{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
}

-(void) didReceiveTravellerProfileImages:(NSArray *)imageArray{
    NSLog(@"done image");
    userImagesArr = [[NSMutableArray alloc] initWithArray: imageArray];
    if (userImagesArr.count >0) {
        NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
        for (int i = 0; i<[userImagesArr count]; i++) {
            NSArray* imageFileName = [userImagesArr[i] componentsSeparatedByString: @"."];
            if ([[imageFileName lastObject] isEqualToString: @"png"]) {
                userImagesArr[i] = [imgPathPreFix stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@/",userImagesArr[i]]];
                NSLog(@"IMAGE PATH %@", userImagesArr[i]);
            }
        }
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            // Now the image will have been loaded and decoded and is ready to rock for the main thread
            dispatch_sync(dispatch_get_main_queue(), ^{
                [self.tView reloadData];
            });
        });    }
}

-(void) didReceiveGuideProfileImages:(NSArray *)imageArray{
    NSLog(@"done image");
    userImagesArr = [[NSMutableArray alloc] initWithArray: imageArray];
    if (userImagesArr.count >0) {
        //        [userImagesArr removeObjectAtIndex:0];
        NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
        for (int i = 0; i<[userImagesArr count]; i++) {
            NSArray* imageFileName = [userImagesArr[i] componentsSeparatedByString: @"."];
            //            NSLog(@"last object, %@",[imageFileName lastObject]);
            if ([[imageFileName lastObject] isEqualToString: @"png"]) {
                userImagesArr[i] = [imgPathPreFix stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@/",userImagesArr[i]]];
                NSLog(@"IMAGE PATH %@", userImagesArr[i]);
            }
        }
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            // Now the image will have been loaded and decoded and is ready to rock for the main thread
            dispatch_sync(dispatch_get_main_queue(), ^{
                [self.tView reloadData];
            });
        });
    }
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

-(void)didReceiveImageId:(NSString *)imageId{
    NSLog(@"Done");
}


-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    
    CGFloat pageWidth = self.scrollView.frame.size.width;
    float fractionalPage = self.scrollView.contentOffset.x / pageWidth;
    NSInteger page = lround(fractionalPage);
    self.pageControl.currentPage = page;
    // Get visible cells on table view.
    //    NSArray *visibleCells = [self.tView visibleCells];
    
    
    //    UITableViewCell *cell = visibleCells[0];
    UITableViewCell *cell = [self.tView cellForRowAtIndexPath:0];
    ////    if ([self isRowZeroVisible]) {
    
    
    CGRect rectInSuperview = [self.tView convertRect:cell.frame toView:self.view];
    
    float distanceFromCenter = CGRectGetHeight(self.view.frame)/2 - CGRectGetMinY(rectInSuperview);
    float difference = CGRectGetHeight(self.scrollView.frame) - CGRectGetHeight(cell.frame);
    float move = (distanceFromCenter / CGRectGetHeight(self.view.frame)) * difference;
    CGRect imageRect = self.scrollView.frame;
    imageRect.origin.y = -(difference/2)+move;
    self.scrollView.frame = imageRect;
    //        [cell cellOnTableView:self.tView didScrollOnView:self.view];
    
    
    
    NSLog(@"move %f", self.scrollView.frame.origin.y);
    
    
    //    }
    //    if([visibleCells[0] isKindOfClass:[JBParallaxCell class]])
    //    {
    //        // do somthing
    //        JBParallaxCell *cell =visibleCells[0];
    //        [cell cellOnTableView:self.tView didScrollOnView:self.view];
    //    }
    
    
    
    
    //
    //    CGRect imageRect = cell.parallaxImage.frame;
    //    imageRect.origin.y = imageRect.origin.y-1;
    //    cell.parallaxImage.frame = imageRect;
    //    NSLog(@"move %f",cell.parallaxImage.frame.origin.y);
    //        });
    //    });
    
    //    for (JBParallaxCell *cell in visibleCells) {
    //        [cell cellOnTableView:self.tView didScrollOnView:self.view];
    //        NSLog(@"hello");
    //    }
    
    //update page control when scrollview scrolls
    //prevent flicker by only updating when page index has changed
    NSInteger pageIndex = (NSInteger)(round(scrollView.contentOffset.x / scrollView.bounds.size.width));
    
    self.pageControlFX.currentPage = page;
    //    self.pageControlFX.selectedDotColor = [UIColor whiteColor];
    //    self.pageControlFX.dotColor = [UIColor redColor];
    //    (pageIndex == 2)?
    //        [UIColor colorWithWhite:1.0 alpha:0.25]: [UIColor colorWithWhite:0.0 alpha:0.25];
    
}


- (IBAction)pageControlAction:(FXPageControl *)sender
{
    
    CGPoint offset = CGPointMake(sender.currentPage * self.scrollView.bounds.size.width, 0);
    [self.scrollView setContentOffset:offset animated:YES];}

@end
