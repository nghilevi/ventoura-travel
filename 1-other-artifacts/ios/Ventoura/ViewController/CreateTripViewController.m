//
//  CreateTripViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 14/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "CreateTripViewController.h"
@interface CreateTripViewController ()<RMDateSelectionViewControllerDelegate, TourManagerDelegate,MBProgressHUDDelegate>{
    TourManager *_tmanager;
    MBProgressHUD *HUD;

}
@property (nonatomic, strong) City *city;
@property (nonatomic, strong) NSDate *startDate;
@property (nonatomic, strong) NSDate *endDate;
@property (nonatomic, assign) BOOL isStartDate;


@end
NSInteger lastSection= 0;
@implementation CreateTripViewController
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)addItemViewController:(CitySelectionViewController *)controller didFinishCitySelection:(City *)city
{
    NSLog(@"From Selection %@",city.cityName);
    self.city = city;
}

-(void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.tView reloadData];
    if ([self.city.countryId length] !=0) {
        //needs error checking, otherwise it will crash TODO
        UIImage * cityImage = [UIImage imageNamed:[NSString stringWithFormat:@"cityImages/%@.png",self.city.cityId]];
        self.cityImageView.image = cityImage;
    }
    //load city Image in here too.
    [self checkAndSetCreateButton];
    
}

- (void)viewDidLoad
{
    [super viewDidLoad];
//    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStylePlain target:self action:@selector(createTrip)];
//    self.navigationItem.rightBarButtonItem = doneButton;
    [self.createButton addTarget:self
                 action:@selector(createTrip)
       forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.title = @"Add Trip";
    // Do any additional setup after loading the view.
    // Set up the initial values for the date cells manager

//    UIGraphicsBeginImageContext(self.view.frame.size);
//    [[UIImage imageNamed:@"Background_Black.png"] drawInRect:self.view.bounds];
//    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//    self.view.backgroundColor = [UIColor colorWithPatternImage:image];
    
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    [self.createButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.createButton setTitleColor:[UIColor whiteColor] forState:UIControlStateDisabled];
    [self.createButton setBackgroundColor: [ventouraUtility ventouraPinkAlpha]];
    self.createButton.titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];

    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT);
        self.createButton.frame = CGRectMake(self.createButton.frame.origin.x, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.createButton.frame.size.width, self.createButton.frame.size.height);
        [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, IPHONE5SCREENHEIGHT)];
        self.sView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT);


    }else{
        self.createButton.frame = CGRectMake(self.createButton.frame.origin.x, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.createButton.frame.size.width, self.createButton.frame.size.height);
        

        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT);
//        [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, IPHONE4SCREENHEIGHT)];
        [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, IPHONE5SCREENHEIGHT)];

        self.sView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT);
    }
    self.tView.backgroundColor = [UIColor clearColor];
    self.tView.alwaysBounceVertical = NO;

    
    self.tView.backgroundColor = [UIColor clearColor];
    self.tView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    self.tView.separatorStyle = UITableViewCellSeparatorStyleNone;
//    [self.tView setSeparatorInset:UIEdgeInsetsZero];
    
    _tmanager = [[TourManager alloc] init];
    _tmanager.communicator =[[TourCommunicator alloc] init];
    _tmanager.communicator.delegate = _tmanager;
    _tmanager.delegate = self;
//    [self.tView bringSubviewToFront:self.createButton];
   

}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 44;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *sectionView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.bounds.size.width, 0)] ;
    [sectionView setBackgroundColor:[UIColor clearColor]];
    return sectionView;
}
//
//- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
//    UIView *sectionView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.bounds.size.width, 0)] ;
//    [sectionView setBackgroundColor:[UIColor clearColor]];
//    return sectionView;
//}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - RMDateSelectionViewController Delegates
- (void)dateSelectionViewController:(RMDateSelectionViewController *)vc didSelectDate:(NSDate *)aDate {
    //Do something
    if(self.isStartDate){
        if(self.endDate==nil){
            self.startDate = aDate;
            int daysToAdd = 1;
            NSDate *newDate1 = [aDate dateByAddingTimeInterval:60*60*24*daysToAdd];
            self.endDate = newDate1;
        }else{
            if ([aDate compare:self.endDate] == NSOrderedDescending) {
                NSLog(@"date1 is later than date2");
                self.startDate = aDate;
                self.endDate = aDate;
            } else if ([aDate compare:self.endDate] == NSOrderedAscending) {
                NSLog(@"date1 is earlier than date2");
                self.startDate = aDate;
            } else {
                self.startDate = aDate;
                NSLog(@"dates are the same");
            }
        }
        
        NSLog(@"startDate %@", self.startDate);
    }else{
        if(self.startDate==nil){
            self.endDate = aDate;
            self.startDate = aDate;
        }else{
//            self.endDate = aDate;
            if ([aDate compare:self.startDate] == NSOrderedDescending) {
                NSLog(@"date1 is later than date2");
                self.endDate = aDate;
            } else if ([aDate compare:self.startDate] == NSOrderedAscending) {
                NSLog(@"date1 is earlier than date2");
                self.endDate = self.startDate;
            } else {
                self.endDate = aDate;
                NSLog(@"dates are the same");
            }
        }
        NSLog(@"endDate %@", self.endDate);
    }
    [self.tView reloadData];
    [self checkAndSetCreateButton];

}

- (void)dateSelectionViewControllerDidCancel:(RMDateSelectionViewController *)vc {
    //Do something else
}

- (IBAction)openDateSelectionControllerWithStartDate:(BOOL)isStartDate{
    RMDateSelectionViewController *dateSelectionVC = [RMDateSelectionViewController dateSelectionController];
    self.isStartDate = isStartDate;
    dateSelectionVC.delegate = self;
    dateSelectionVC.hideNowButton = YES;
    [dateSelectionVC show];
    dateSelectionVC.datePicker.datePickerMode = UIDatePickerModeDate;
    if (isStartDate) {
        if (self.startDate !=nil) {
            dateSelectionVC.datePicker.date = self.startDate;
        }
    }else{
        if (self.endDate !=nil) {
            dateSelectionVC.datePicker.date = self.endDate;
        }
    }

    
}

#pragma mark - Table view delegate
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
       // Other cells
    return indexPath;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section ==1 && indexPath.row ==0) {
        [self openDateSelectionControllerWithStartDate:YES];
    }else if (indexPath.section ==2 && indexPath.row ==0){
        [self openDateSelectionControllerWithStartDate:NO];

    }else if(indexPath.section ==0 && indexPath.row ==1){
        //goto selection

        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                             bundle:nil];
        CitySelectionViewController *viewController =
        [storyboard instantiateViewControllerWithIdentifier:@"CitySelectionViewController"];
        viewController.delegate = self;
        viewController.city = self.city;
        [[self navigationController] pushViewController:viewController animated:YES ];
    }
    
    
    // Other cells
    // ...
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 3;
}
-(CGFloat)tableView:(UITableView*)tableView heightForFooterInSection:(NSInteger)section
{
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
  

    if (section == 0){
        return 2;
    }
    
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (indexPath.section == 0 && indexPath.row ==0) {
        return 130;
    }
    // All other cells
    return 44;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    UITableViewCell *cell;
    // Other cells
    if (indexPath.section == 0 && indexPath.row ==0) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"flagCell" forIndexPath:indexPath];
//        if ([self.city.countryId length] !=0) {
//            //needs error checking, otherwise it will crash TODO
//            UIImage * countryFlagImg = [UIImage imageNamed:[NSString stringWithFormat:@"%@.png",self.city.countryId]];
//            UIImageView *countryFlagImgView = (UIImageView *)[cell viewWithTag:80];
//            countryFlagImgView.image = countryFlagImg;
//        }
    }else if(indexPath.section == 0 && indexPath.row ==1){
        cell = [tableView dequeueReusableCellWithIdentifier:@"cityCell" forIndexPath:indexPath];
        UILabel *cityLabel = (UILabel *)[cell viewWithTag:61];
        cityLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
        
        UILabel *cityNameLabel = (UILabel *)[cell viewWithTag:70];
        if (self.city ==nil) {
            cityLabel.textColor = [UIColor lightGrayColor];
        }else{
            cityLabel.textColor = [ventouraUtility ventouraTextBodyGrey];

        }
        cityNameLabel.textColor = [ventouraUtility ventouraTextBodyGrey];

        cityNameLabel.textAlignment = NSTextAlignmentRight;
        if ([self.city.cityName length] ==0) {
            cityNameLabel.text = @"";
        }else{
            cityNameLabel.text = self.city.cityName;
        }
        
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }else if(indexPath.section == 1 && indexPath.row ==0){
        cell = [tableView dequeueReusableCellWithIdentifier:@"startDateCell" forIndexPath:indexPath];
        UILabel *dateNameLabel = (UILabel *)[cell viewWithTag:62];
        if(self.startDate == nil){
            dateNameLabel.textColor = [UIColor lightGrayColor];
        }else{
            dateNameLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
        }
        
        UILabel *startDateLabel = (UILabel *)[cell viewWithTag:71];
        startDateLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
        startDateLabel.textAlignment = NSTextAlignmentRight;
        if (self.startDate ==nil) {
            startDateLabel.text = @"";
        }else{
            startDateLabel.text = [ventouraUtility ventouraDateToStringForCreateTrip:self.startDate];
        }

        
    }else if (indexPath.section == 2 && indexPath.row ==0){
        cell = [tableView dequeueReusableCellWithIdentifier:@"endDateCell" forIndexPath:indexPath];
        UILabel *dateNameLabel = (UILabel *)[cell viewWithTag:63];
        if(self.endDate == nil){
            dateNameLabel.textColor = [UIColor lightGrayColor];
        }else{
            dateNameLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
        }

        UILabel *endDateLabel = (UILabel *)[cell viewWithTag:72];
        endDateLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
        endDateLabel.textAlignment = NSTextAlignmentRight;
        if (self.endDate ==nil) {
            endDateLabel.text = @"";
        }else{
            endDateLabel.text = [ventouraUtility ventouraDateToStringForCreateTrip:self.endDate];
        }

    }
    
    
    //use tag instead
    if ( !(indexPath.section ==0 && indexPath.row ==0 )) {
//        UIView* separatorLineView = [[UIView alloc] initWithFrame:CGRectMake(0, 44, 320, 1)];
        UIView *separatorLineView = (UILabel *)[cell viewWithTag:200];
        separatorLineView.frame = CGRectMake(0, 43, 320, 1);
        UIView *separatorTopLineView = (UILabel *)[cell viewWithTag:201];

        separatorTopLineView.frame = CGRectMake(0, 0, 320, 1);
        separatorLineView.backgroundColor = [UIColor lightGrayColor]; // set color as you want.
        separatorTopLineView.backgroundColor = [UIColor lightGrayColor]; // set color as you want.
        cell.backgroundColor = [UIColor whiteColor];

        
        
//        [cell.contentView addSubview:separatorLineView];
//        [cell.contentView addSubview:separatorTopLineView];

    }else{
        cell.backgroundColor = [UIColor clearColor];

    }
//    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    return cell;
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    
    if (scrollView==self.sView) {
        CGFloat originalPos = IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT-50;
        [self.createButton setFrame:CGRectMake(self.createButton.frame.origin.x, originalPos+ scrollView.contentOffset.y, self.createButton.bounds.size.width, self.createButton.bounds.size.height)];
    }
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
-(void)createTrip{
    NSLog(@"create trip on %@, " , self.city.cityId);
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];
    
    [_tmanager createTripForCity:self.city.cityId withStartDate:self.startDate andEndDate:self.endDate countryId:self.city.countryId];
    //Load a loading screen
}


-(void) didReceiveCreateTripResult:(BOOL)didCreate{
    NSLog(@"Trip Create Complete");
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];

            [self.navigationController popViewControllerAnimated:TRUE];
        });
    });

}

-(void)fetchingTourFailedWithError:(NSError *)error{
}

-(void)didReceiveTourTripObject:(NSArray *)tourTripObjects{
}
-(void)didReceiveCreateTourResult:(Person *)tourResult{

}
-(void)checkAndSetCreateButton{

    if (self.startDate ==nil || self.endDate==nil || self.city ==nil) {
        [self.createButton setEnabled:NO];
        [self.createButton setBackgroundColor: [ventouraUtility ventouraDarkGreyAlpha]];
    }else{
        [self.createButton setEnabled:YES];
        [self.createButton setBackgroundColor: [ventouraUtility ventouraPinkAlpha]];
    }
}


@end
