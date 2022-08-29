//
//  ToursViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 16/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ToursViewController.h"
NSIndexPath *lastIndexPath;
SWTableViewCell *lastSWCell;
@interface ToursViewController ()<TourManagerDelegate>{
    NSMutableArray *_tourTripList;
    TourManager *_tmanager;
    
}
@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) NSArray *arrMsgHistory;
@end

@implementation ToursViewController

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
    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    [self.revealButtonItem setTarget: self.revealViewController];
    [self.revealButtonItem setAction: @selector( revealToggle: )];
    [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    
//    [self.navigationController.navigationBar setTitleTextAttributes:size];
        CGRect screenRect = [[UIScreen mainScreen] bounds];
        CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT
                                      );
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }

    
    self.navigationController.navigationBar.translucent = NO;
    UIFont * font = [UIFont systemFontOfSize:25];
    NSDictionary * attributes = @{NSFontAttributeName: font};
    [self.addCityItem setTitleTextAttributes:attributes forState:UIControlStateNormal];
    
    
    [self.addCityItem setTarget: self];
    [self.addCityItem setAction: @selector(addCityClicked)];
//    self.addCityItem.image = [UIImage imageNamed:@"mail-48_24.png"];
    _tmanager = [[TourManager alloc] init];
    _tmanager.communicator =[[TourCommunicator alloc] init];
    _tmanager.communicator.delegate = _tmanager;
    _tmanager.delegate = self;
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    
//    UIGraphicsBeginImageContext(self.view.frame.size);
//    [[UIImage imageNamed:@"DiamondPatternMultiply.png"] drawInRect:self.view.bounds];
//    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//    self.view.backgroundColor = [UIColor colorWithPatternImage:image];
    
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    self.tView.backgroundColor = [UIColor clearColor];
//     self.tView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    if ([ventouraUtility isUserGuide]) {
        self.navigationItem.rightBarButtonItem = nil;

    }

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if ([ventouraUtility isUserGuide]) {
        NSLog(@"get TOURS");
        [_tmanager fetchTours];
    }else{
        NSLog(@"get TRIPS N TOURS");

        [_tmanager fetchTripAndTour];

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
-(void)didReceiveTourTripObject:(NSArray *)tourTripObjects{
    NSLog(@"didReceiveTourTripObject Display Them here yeeew!");
    _tourTripList = [tourTripObjects mutableCopy];
    NSLog(@"count%lu",(unsigned long)_tourTripList.count);
    
    //[_vmanager fetchVentouraPackageImages:ventouraPackage];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
    
    NSLog(@"Package Delegate done");
}

-(void) didReceiveCreateTourResult:(Person *)tourResult{
    NSLog(@"booking created");
}



- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return 1;
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)sectionIndex
{
    
    
	return _tourTripList.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    Tour *tour = _tourTripList[indexPath.row];
    _heightOfRow = 90;
    if (tour.isTour) {
        _heightOfRow = 90;
    }
	return _heightOfRow;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    Tour *tour = _tourTripList[indexPath.row];

    static NSString *CellIdentifier = @"cell";
    
    if ([tour.isTour isEqualToString:@"1"]) {
        CellIdentifier = @"tourCell";
    }else{
        CellIdentifier = @"tripCell";

    }
//    MessageTableViewCell *cell = (MessageTableViewCell *) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
//    if (cell == nil) {
//        cell = [[MessageTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
//    }
    MessageTableViewCell *cell = (MessageTableViewCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier
                                                                                         forIndexPath:indexPath];

	
    if ([tour.isTour isEqualToString:@"1"]) {
        UIImageView *personImage = (UIImageView *)[cell viewWithTag:40];
        if ([ventouraUtility isUserGuide]) {
            personImage.image = [ventouraUtility getTravellerProfileImageWithID:tour.travellerId];
        }else{
            personImage.image = [ventouraUtility getLocalProfileImageWithID:tour.guideId];
        }
        personImage.layer.cornerRadius = personImage.frame.size.width / 2;
        personImage.clipsToBounds = YES;
        personImage.layer.borderWidth = 0.5f;
        personImage.layer.borderColor = [UIColor lightGrayColor].CGColor;
        
        UILabel *localNameLabel = (UILabel *)[cell viewWithTag:41];
        if ([ventouraUtility isUserGuide]) {
            localNameLabel.text = [NSString stringWithFormat:@"%@", tour.travellerFirstname];
        }else{
            localNameLabel.text = [NSString stringWithFormat:@"%@", tour.guideFirstname];
        }
        localNameLabel.textColor = [ventouraUtility ventouraTitleColour];
        localNameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
        
        UIView *lineSeparator = (UIView *)[cell viewWithTag:50];
        lineSeparator.backgroundColor = [UIColor lightGrayColor];
        
        UILabel *tourDateLabel = (UILabel *)[cell viewWithTag:42];
        NSLog(@"date. %@",tour.dateForSorting);
        NSString *stringFromDate = [ventouraUtility ventouraDateToStringForTours:tour.dateForSorting];
        tourDateLabel.text = [NSString stringWithFormat:@"%@", stringFromDate];
        tourDateLabel.textColor = [ventouraUtility ventouraTitleColour];

        tourDateLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
        
        UIImageView * tourStatus = (UIImageView *)[cell viewWithTag:45];
        switch ([tour.bookingStatus integerValue]) {
            case REQUEST_BY_TRAVELLER:{
                if ([ventouraUtility isUserGuide]) {
                    tourStatus.image = [UIImage imageNamed:@"WaitingResponse.png"];
                }else{
                    tourStatus.image = [UIImage imageNamed:@"WaitingResponse.png"];
                }

                break;
            }
            default:
                break;
        }
        
        
//        UILabel *tourStatusLabel = (UILabel *)[cell viewWithTag:44];
//        tourStatusLabel.text = [NSString stringWithFormat:@"Pay"];
        

//        cell.backgroundColor = [UIColor whiteColor];
    }else{
        NSString *query =[NSString stringWithFormat:@"select cityName, countryId,id from City where id = %@",tour.city];
        if (self.arrMsgHistory != nil) {
            self.arrMsgHistory = nil;
        }
        self.arrMsgHistory = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
        NSLog(@"city name , %@", self.arrMsgHistory[0][0]) ;
        
        cell.rightUtilityButtons = [self tripRightButtons];
        cell.delegate = self;

        //needs error checking, otherwise it will crash TODO
        UIImage * cityImg = [UIImage imageNamed:[NSString stringWithFormat:@"cityImages/%@.png",self.arrMsgHistory[0][2]]];

        UIImage * countryFlagImg = [UIImage imageNamed:[NSString stringWithFormat:@"%@.png",self.arrMsgHistory[0][1]]];
        UIImageView *cityImageView = (UIImageView *)[cell viewWithTag:50];
        cityImageView.image = cityImg;
        UIImageView *countryFlagImgViewSmall = (UIImageView *)[cell viewWithTag:55];
        countryFlagImgViewSmall.image = countryFlagImg;
        
        UIView *lineSeparator = (UIView *)[cell viewWithTag:56];
        lineSeparator.backgroundColor = [UIColor lightGrayColor];
        
        NSLog(@"%@-%@",tour.startTime,tour.endTime );
        NSString *stringFromDate = [ventouraUtility ventouraDateToStringForTripsStartAt:tour.startTime endAt:tour.endTime];
        UILabel *tripDateLabel = (UILabel *)[cell viewWithTag:52];
        tripDateLabel.text = [NSString stringWithFormat:@"%@", stringFromDate];
        tripDateLabel.textColor = [ventouraUtility ventouraTitleColour];
        tripDateLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
        
        UILabel *tripCountryLabel = (UILabel *)[cell viewWithTag:51];
        tripCountryLabel.text = [NSString stringWithFormat:@"%@", self.arrMsgHistory[0][0]];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        tripCountryLabel.textColor = [ventouraUtility ventouraTitleColour];
        tripCountryLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];


    }
//
//    [[cell contentView] setBackgroundColor:[UIColor colorWithRed:0 green:128/255.f blue:0 alpha:1]];
    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    cell.backgroundColor = [UIColor clearColor];
	return cell;
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    Tour *tour = _tourTripList[indexPath.row];
    if ([tour.isTour isEqualToString:@"1"]) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                             bundle:nil];
        TourDetailViewController *viewController =
        [storyboard instantiateViewControllerWithIdentifier:@"TourDetailViewController"];
        viewController.tourBooking = tour;
        [[self navigationController] pushViewController:viewController animated:YES ];
    
    }
}


- (void)addCityClicked {
    NSLog(@"add city View");
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    CreateTripViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"CreateTripViewController"];
    
    [[self navigationController] pushViewController:viewController animated:YES ];
}

-(void)fetchingTourFailedWithError:(NSError *)error{

}

-(void) didReceiveCreateTripResult:(BOOL)didCreate{}



#pragma mark - SWTableViewDelegate

- (void)swipeableTableViewCell:(SWTableViewCell *)cell scrollingToState:(SWCellState)state
{
    switch (state) {
        case 0:
            NSLog(@"utility buttons closed");
            break;
        case 1:
            NSLog(@"left utility buttons open");
            break;
        case 2:
            NSLog(@"right utility buttons open");
            break;
        default:
            break;
    }
}

- (void)swipeableTableViewCell:(SWTableViewCell *)cell didTriggerLeftUtilityButtonWithIndex:(NSInteger)index
{
    switch (index) {
        case 0:
            NSLog(@"left button 0 was pressed");
            break;
        case 1:
            NSLog(@"left button 1 was pressed");
            break;
        case 2:
            NSLog(@"left button 2 was pressed");
            break;
        case 3:
            NSLog(@"left btton 3 was pressed");
        default:
            break;
    }
}

- (void)swipeableTableViewCell:(SWTableViewCell *)cell didTriggerRightUtilityButtonWithIndex:(NSInteger)index
{
    
    
    lastIndexPath = [self.tView indexPathForCell:cell];
    lastSWCell = cell;
    Tour *tour = _tourTripList[lastIndexPath.row];
    if ([tour.isTour isEqualToString:@"1"]) {
        
    }else{
        switch (index) {
            case 0:
            {
                NSLog(@"More button was pressed");
                UIAlertView *alertTest = [[UIAlertView alloc] initWithTitle:@"Edit Trip" message:@"Place Holder" delegate:nil cancelButtonTitle:@"cancel" otherButtonTitles: nil];
                [alertTest show];
                
                [cell hideUtilityButtonsAnimated:YES];
                break;
            }
            case 1:
            {
                
                
                [self showDeleteActionSheet];
                
                // Delete button was pressed
                //            NSIndexPath *cellIndexPath = [self.tView indexPathForCell:cell];
                
                break;
            }
            default:
                break;
        }

    }
}

- (BOOL)swipeableTableViewCellShouldHideUtilityButtonsOnSwipe:(SWTableViewCell *)cell
{
    // allow just one cell's utility button to be open at once
    return YES;
}

- (BOOL)swipeableTableViewCell:(SWTableViewCell *)cell canSwipeToState:(SWCellState)state
{
    switch (state) {
        case 1:
            // set to NO to disable all left utility buttons appearing
            return YES;
            break;
        case 2:
            // set to NO to disable all right utility buttons appearing
            return YES;
            break;
        default:
            break;
    }
    
    return YES;
}


- (NSArray *)tripRightButtons
{
    NSMutableArray *rightUtilityButtons = [NSMutableArray new];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:0.78f green:0.78f blue:0.8f alpha:1.0]
                                                title:@"Edit"];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:1.0f green:0.231f blue:0.188 alpha:1.0f]
                                                title:@"Delete"];
    
    return rightUtilityButtons;
}



- (NSArray *)tourRightButtons
{
    NSMutableArray *rightUtilityButtons = [NSMutableArray new];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:0.78f green:0.78f blue:0.8f alpha:1.0]
                                                title:@"Edit"];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:1.0f green:0.231f blue:0.188 alpha:1.0f]
                                                title:@"Delete"];
    
    return rightUtilityButtons;
}




-(void)showDeleteActionSheet{
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Do you want to delete this user?"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Delete", nil];
    actionSheet.tag = 100;
    [actionSheet showInView:self.view];
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (actionSheet.tag == 100) {
        //        NSLog(@"Index = %d - Title = %@", buttonIndex, [actionSheet buttonTitleAtIndex:buttonIndex]);
        
        if (buttonIndex ==0) {
//            Person * personToDelete = _buddyList[lastIndexPath.row];
//            [_vmanager postDeleteBuddyById:personToDelete.ventouraId isUserGuide:[ventouraUtility isUserGuide:personToDelete.userRole]];
            [_tourTripList removeObjectAtIndex:lastIndexPath.row];
            [self.tView deleteRowsAtIndexPaths:@[lastIndexPath] withRowAnimation:UITableViewRowAnimationLeft];
            
            lastIndexPath = nil;
            
        }else{
            [lastSWCell hideUtilityButtonsAnimated:YES];
            lastSWCell = nil;
            lastIndexPath = nil;
        }
    }
  
}




@end
