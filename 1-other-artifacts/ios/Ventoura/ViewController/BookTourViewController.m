//
//  BookTourViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 27/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "BookTourViewController.h"

@interface BookTourViewController ()<RMDateSelectionViewControllerDelegate, ProfileManagerDelegate, TourManagerDelegate,MBProgressHUDDelegate>{
    NSArray *_buddyList;
    ProfileManager *_vmanager;
    TourManager *_tmanager;
    Person *_guideProfile;
    MBProgressHUD *HUD;

    
}

@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) NSDate *tourDate;

@end

@implementation BookTourViewController

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
    _vmanager = [[ProfileManager alloc] init];
    _vmanager.communicator =[[ProfileCommunicator alloc] init];
    _vmanager.communicator.delegate = _vmanager;
    _vmanager.delegate = self;
    NSLog(@"guide name :%@",self.person.ventouraId);
    
    
    [_vmanager fetchUserProfile:YES ventouraId:self.person.ventouraId];
    _tmanager = [[TourManager alloc] init];
    _tmanager.communicator =[[TourCommunicator alloc] init];
    _tmanager.communicator.delegate = _tmanager;
    _tmanager.delegate = self;
    
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];

    _guideNameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
    _guideNameLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
    _cityLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
    _cityLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
    _amountLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
    _amountLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
    [_lengthLabel setHidden:YES];
    [_paymentMethodLabel setHidden:YES];
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    self.scrollView.backgroundColor = [UIColor clearColor];

   
    self.portalPicture.image = _person.image;
    self.portalPicture.layer.cornerRadius = self.portalPicture.frame.size.width / 2;
    self.portalPicture.clipsToBounds = YES;
    self.portalPicture.layer.borderWidth = 0.5f;
    self.portalPicture.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    [self.bookButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.bookButton setTitleColor:[UIColor whiteColor] forState:UIControlStateDisabled];
    [self.bookButton setBackgroundColor: [ventouraUtility ventouraPinkAlpha]];
    self.bookButton.titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.scrollView.frame = CGRectMake(0, 0, 320, 568);
        self.bookButton.frame = CGRectMake(self.bookButton.frame.origin.x, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.bookButton.frame.size.width, self.bookButton.frame.size.height);
        [self.scrollView setContentSize:CGSizeMake(self.view.frame.size.width, 568)];

    }else{
        self.bookButton.frame = CGRectMake(self.bookButton.frame.origin.x, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.bookButton.frame.size.width, self.bookButton.frame.size.height);

        self.scrollView.frame = CGRectMake(0, 0, 320, 480);
        [self.scrollView setContentSize:CGSizeMake(self.view.frame.size.width, 480)];

    }
    
    // Do any additional setup after loading the view.
    //    self.city1.frame = CGRectMake(self.city1.frame.origin.x, self.city1.frame.origin.y, 50, 50);
    self.tView.alwaysBounceVertical = NO;
    [self.view bringSubviewToFront:self.bookButton];
    self.tView.separatorColor = [UIColor clearColor];
    self.tView.backgroundColor = [UIColor clearColor];
    if (self.tourDate ==nil) {
        [self.bookButton setEnabled:NO];
        [self.bookButton setBackgroundColor: [ventouraUtility ventouraDarkGreyAlpha]];
    }
//    [self.tView setHidden:YES];
//    [self.view sendSubviewToBack:self.tView];
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


#pragma mark Delegate For PackageFetch
-(void) didReceivePersonProfile:(Person *)personProfile{
//    NSLog(@"done, load info onto screen, here is some info tour price : %@", personProfile.tourPrice);
//    
//   
//    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
//        dispatch_sync(dispatch_get_main_queue(), ^{
//            _guideNameLabel.text = [NSString stringWithFormat:@"%@",personProfile.firstName];
//            _cityLabel.text = [NSString stringWithFormat:@"%@",personProfile.city];
//            _amountLabel.text = [NSString stringWithFormat:@"£%@",personProfile.tourPrice];
//            _lengthLabel.text = [NSString stringWithFormat:@"%@",personProfile.tourLength];
//            _paymentMethodLabel.text = [NSString stringWithFormat:@"%@",personProfile.paymentMethod];
//        });
//    });
//    self.person.firstName = personProfile.firstName;
//    
//    NSLog(@"Package Delegate done");
    
}



-(void) didReceiveCreateTourResult:(Person *)tourResult{
    NSLog(@"booking created");
//    [self.navigationController popViewControllerAnimated:TRUE];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            
            [self.navigationController popViewControllerAnimated:TRUE];
        });
    });

}

-(void)fetchingTourFailedWithError:(NSError *)error{
}

-(void)fetchingProfileFailedWithError:(NSError *)error{

}

- (void)newMessageReceived:(NSDictionary *)messageContent{
}

-(void)didReceiveCreateTripResult:(BOOL)didCreate{
}

-(void)didReceiveGuideProfile:(Person *)personProfile{
    NSLog(@"done, load info onto screen, here is some info tour price : %@", personProfile.tourPrice);
    self.person.firstName = personProfile.firstName;
    self.person.tourPrice = personProfile.tourPrice;
    self.person.tourType = personProfile.tourType;
    
    
    NSString * city = @"";
    NSString * tourType = @"";
    
    NSArray* results = [ventouraDatabaseUtility getCityFromDatabase:self.dbManager cityId:self.person.city];
    if (results.count>0) {
//        _person.country = results[0][1];
        city = [NSString stringWithFormat:@"%@",results[0][0]];
    }
    

    
    
    NSArray* tourTypeResult = [ventouraDatabaseUtility getTourTypeFromDatabase:self.dbManager tourTypeId:self.person.tourType];
    if (tourTypeResult.count>0) {
        NSString * tmp =tourTypeResult[0][0];
        tourType = [NSString stringWithFormat:@"%@ Tour",tmp];
        NSLog(@"tour type %@", personProfile.tourType);

    }
    //        }

    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
    
        
        
        dispatch_sync(dispatch_get_main_queue(), ^{
            _guideNameLabel.text = [NSString stringWithFormat:@"%@",personProfile.firstName];
            _cityLabel.text = [NSString stringWithFormat:@"%@ in %@",tourType, city];
            _amountLabel.text = [NSString stringWithFormat:@"£%@",personProfile.tourPrice];
            _lengthLabel.text = [NSString stringWithFormat:@"%@",personProfile.tourLength];
            _paymentMethodLabel.text = [NSString stringWithFormat:@"%@",personProfile.paymentMethod];
        });
    });
    
    NSLog(@"Package Delegate done");

}

-(void)didReceiveGuideProfileImages:(NSArray *)imageArray{
}

-(void)didReceiveTourTripObject:(NSArray *)tourTripObjects{
}

-(void)didReceiveImageId:(NSString *)imageId{
}

-(void)didReceiveTravellerProfile:(Person *)personProfile{
}

-(void)didReceiveTravellerProfileImages:(NSArray *)imageArray{
}
-(void) receivedDeleteUserImages{}
-(void) receivedGuideDeleteAttractions{}
-(void) receivedGuideDeleteSecrets{}
-(void) receivedUpdateTravellerProfile{}
-(void) receivedUpdateGuideProfile{}
-(void) receivedGuideUpdateAttractions{}
-(void) receivedGuideUpdateSecrets{}

- (IBAction)bookPressed
{
    NSLog(@"booking pressed ");
    
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];
    
    [_tmanager createBookingWithGuideId:self.person.ventouraId travellerId:[ventouraUtility returnMyUserId] guideName:self.person.firstName travellerName:[ventouraUtility returnMyUserFirstName] tourPrice:self.person.tourPrice tourLength:self.person.tourLength tourDate:self.tourDate city:self.person.city tourType:self.person.tourType];
}

//TABLES DELEGATE
#pragma mark - Table view delegate
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Other cells
    return indexPath;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
//    if (indexPath.section ==1 && indexPath.row ==0) {
        [self openDateSelectionControllerWithStartDate:YES];
//    }else if (indexPath.section ==2 && indexPath.row ==0){
//        [self openDateSelectionControllerWithStartDate:NO];
        
//    }else if(indexPath.section ==0 && indexPath.row ==1){
        //goto selection
//        
//        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
//                                                             bundle:nil];
//        CitySelectionViewController *viewController =
//        [storyboard instantiateViewControllerWithIdentifier:@"CitySelectionViewController"];
//        viewController.delegate = self;
//        viewController.city = self.city;
//        [[self navigationController] pushViewController:viewController animated:YES ];
//    }
    
    
    // Other cells
    // ...
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}
-(CGFloat)tableView:(UITableView*)tableView heightForFooterInSection:(NSInteger)section
{
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
//    
//    if (section == 0){
//        return 2;
//    }
    
    return 1;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    UITableViewCell *cell;
//    if(indexPath.section == 0 && indexPath.row ==0){
        cell = [tableView dequeueReusableCellWithIdentifier:@"pickDateCell" forIndexPath:indexPath];
        UILabel *dateNameLabel = (UILabel *)[cell viewWithTag:1];
    
        dateNameLabel.text = @"Date";
        dateNameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
        UILabel *startDateLabel = (UILabel *)[cell viewWithTag:2];
        startDateLabel.text =@"";
        startDateLabel.textAlignment = NSTextAlignmentRight;
        startDateLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];

    
        if (self.tourDate ==nil) {
            dateNameLabel.textColor = [ventouraUtility ventouraTextBodyGrey]; // set color as you want.
            startDateLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel]; // set color as you want.
        }else{
            startDateLabel.text = [ventouraUtility ventouraDateToStringForTours:self.tourDate];
//            NSString *stringFromDate = [ventouraUtility ventouraDateToStringForTours:tour.dateForSorting];

            dateNameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel]; // set color as you want.
            startDateLabel.textColor = [ventouraUtility ventouraTextBodyGrey]; // set color as you want.
        }
    
    
        UIView* separatorLineView = [[UIView alloc] initWithFrame:CGRectMake(0, 44, 320, 1)];
        UIView* separatorTopLineView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 1)];
        separatorLineView.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel]; // set color as you want.
        separatorTopLineView.backgroundColor = [ventouraUtility ventouraTextBodyGreyProfileLabel]; // set color as you want.
        [cell.contentView addSubview:separatorLineView];
        [cell.contentView addSubview:separatorTopLineView];
    
    return cell;
}
- (IBAction)openDateSelectionControllerWithStartDate:(BOOL)isStartDate{
    RMDateSelectionViewController *dateSelectionVC = [RMDateSelectionViewController dateSelectionController];
    dateSelectionVC.delegate = self;
    dateSelectionVC.hideNowButton = YES;
    
    
//    
//    NSDate *now = [NSDate date];
//    int daysToAdd = 10;
//    NSDate *newDate1 = [now dateByAddingTimeInterval:60*60*24*daysToAdd];
//
//    dateSelectionVC.datePicker.date = newDate1;
    [dateSelectionVC show];
    dateSelectionVC.datePicker.datePickerMode = UIDatePickerModeDateAndTime;
    
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView{

    if (scrollView==self.scrollView) {
        CGFloat originalPos = IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT-50;
        [self.bookButton setFrame:CGRectMake(self.bookButton.frame.origin.x, originalPos+ scrollView.contentOffset.y, self.bookButton.bounds.size.width, self.bookButton.bounds.size.height)];
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *sectionView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.bounds.size.width, 0)] ;
    [sectionView setBackgroundColor:[UIColor clearColor]];
    return sectionView;
}




#pragma mark - RMDateSelectionViewController Delegates
- (void)dateSelectionViewController:(RMDateSelectionViewController *)vc didSelectDate:(NSDate *)aDate {
    //Do something
    NSLog(@"date %@", aDate);
    self.tourDate = aDate;
    if (self.tourDate != nil) {
        [self.bookButton setEnabled:YES];
        [self.bookButton setBackgroundColor: [ventouraUtility ventouraPinkAlpha]];
    }
    [self.tView reloadData];
}

- (void)dateSelectionViewControllerDidCancel:(RMDateSelectionViewController *)vc {
    //Do something else
    [self.tView reloadData];
}

-(void) didReceiveImageId:(NSString *)imageId isPortal:(BOOL)isPortal{}
-(void) receivedSetPortalImage{}

@end
