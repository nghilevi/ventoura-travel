//
//  TourDetailViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 28/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "TourDetailViewController.h"

@interface TourDetailViewController ()<TourManagerDelegate>{
    TourManager *_tmanager;
    NSString * city;
    NSString* tourType;
}

@property (nonatomic, strong) DBManager *dbManager;


@end

@implementation TourDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    _tmanager = [[TourManager alloc] init];
    _tmanager.communicator =[[TourCommunicator alloc] init];
    _tmanager.communicator.delegate = _tmanager;
    _tmanager.delegate = self;
    
//    [_tmanager fetchTourById:self.tourBookingId];
    
    
    [self.actionBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.actionBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateDisabled];
    [self.actionBtn setBackgroundColor: [ventouraUtility ventouraPinkAlpha]];
    self.actionBtn.titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
    
    
    [self.cancelBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.cancelBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateDisabled];
    [self.cancelBtn setBackgroundColor: [ventouraUtility ventouraTextBodyGrey]];
    self.cancelBtn.titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT);
        self.actionBtn.frame = CGRectMake(self.actionBtn.frame.origin.x, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.actionBtn.frame.size.width, self.actionBtn.frame.size.height);
        self.cancelBtn.frame = CGRectMake(self.cancelBtn.frame.origin.x, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.cancelBtn.frame.size.width, self.cancelBtn.frame.size.height);
        
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT);
        self.actionBtn.frame = CGRectMake(self.actionBtn.frame.origin.x, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.actionBtn.frame.size.width, self.actionBtn.frame.size.height);
        self.cancelBtn.frame = CGRectMake(self.cancelBtn.frame.origin.x, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT-50, self.cancelBtn.frame.size.width, self.cancelBtn.frame.size.height);

    }
    
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    
    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    self.tView.backgroundColor = [UIColor clearColor];
    
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];

    if ([ventouraUtility isUserGuide]) {
        [self setUpButtonsGuide];
    }else{
        [self setUpButtonsTraveller];
    }
    
//    NSString *query =[NSString stringWithFormat:@"select cityName, countryId from City WHERE id =%@",self.tourBooking.city];
    
    NSArray* cityResults = [ventouraDatabaseUtility getCityFromDatabase:self.dbManager cityId:self.tourBooking.city];
    
    if (cityResults.count>0) {
        city = cityResults[0][0];
        NSLog(@" detail city %@", city);

    }
    tourType = @"";
    NSArray* tourTypeResults = [ventouraDatabaseUtility getTourTypeFromDatabase:self.dbManager tourTypeId:self.tourBooking.tourType];
    if (tourTypeResults.count>0) {
        tourType =tourTypeResults[0][0];
    }
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark - TableView Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 4;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"emptyCell";
    
//    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    switch ( indexPath.row )
    {
        case 0:
            CellIdentifier = @"imageCell";
            break;
        case 1:
            CellIdentifier = @"detailCell";
            break;
        case 2 :
            CellIdentifier = @"statusBar";
            break;
        case 3 :
            CellIdentifier = @"statusInfo";
            break;
    }

    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    NSArray *profileResult;
    
    
//    if([ventouraUtility isUserGuide]){
//        profileResult  = [ventouraDatabaseUtility userImageDataFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:self.tourBooking.travellerId isUserGuide:0];
//    }else{
//        profileResult  = [ventouraDatabaseUtility userImageDataFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:self.tourBooking.guideId isUserGuide:1];
//    }
    
  
    switch ( indexPath.row )
    {
        case 0:{
            UIImageView *imageView = (UIImageView *)[cell viewWithTag:10];
//            NSLog(@"travellerId %@ ", self.tourBooking.travellerId);
            imageView.layer.cornerRadius = imageView.frame.size.width/2;
            imageView.clipsToBounds = YES;
            imageView.layer.borderWidth = 0.5f;
            imageView.layer.borderColor = [UIColor lightGrayColor].CGColor;


            
            break;
        }
        case 1:{
            //name should be different,
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:20];
            nameLabel.text = [NSString stringWithFormat:@"%@",self.tourBooking.guideFirstname];
            nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
            nameLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
            
            
            UILabel *tourTypeLabel = (UILabel *)[cell viewWithTag:11];
            tourTypeLabel.text = [NSString stringWithFormat:@"%@ Tour in %@",tourType,city];
            tourTypeLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
            tourTypeLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
//            nameLabel.text = self.tourBooking.guideFirstname;
            UILabel *dateLabel = (UILabel *)[cell viewWithTag:12];
            NSString *stringFromDate = [ventouraUtility ventouraDateToStringForTours:self.tourBooking.dateForSorting];
            dateLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
            dateLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
            dateLabel.text  = stringFromDate;
            UILabel *tourPriceLabel = (UILabel *)[cell viewWithTag:13];
            tourPriceLabel.text = [NSString stringWithFormat:@"€%@",self.tourBooking.tourPrice];
            tourPriceLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18
                                   ];
            tourPriceLabel.textColor = [ventouraUtility ventouraTextBodyGrey];

            

            break;
        }
        case 2:{
//            backgroundBar.frame =
            [self setUpStatusBar:cell];
            if ([ventouraUtility isUserGuide]) {
                [self setUpStatusBarGuide:cell];
            }else{
                [self setUpStatusBarTravller:cell];
            }
            
            break;
        }
        case 3:{
            
            if ([ventouraUtility isUserGuide]) {
                [self setUpInfoGuide:cell];
            }else{
                [self setUpInfoTravller:cell];
            }

            
            
            break;
        }
    }
    
    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    cell.backgroundColor = [UIColor clearColor];
    return cell;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    switch ( indexPath.row )
    {
        case 0:
            return 200;
            break;
        case 1:
            return 100;
            break;
        case 2:
            return 100;
            break;
    }
    return 20;
}


-(void)setUpStatusBar:(UITableViewCell*)cell{
    UIView *backgroundBar = (UIView *)[cell viewWithTag:50];
    backgroundBar.layer.cornerRadius = 6;
    
    UIView *backgroundCircle1 = (UIView *)[cell viewWithTag:51];
    backgroundCircle1.layer.cornerRadius = backgroundCircle1.frame.size.width/2;
    
    UIView *backgroundCircle2 = (UIView *)[cell viewWithTag:52];
    backgroundCircle2.layer.cornerRadius =  backgroundCircle2.frame.size.width/2;
    
    UIView *backgroundCircle3 = (UIView *)[cell viewWithTag:53];
    backgroundCircle3.layer.cornerRadius =  backgroundCircle3.frame.size.width/2;
    
    UIView *statusBar = (UIView *)[cell viewWithTag:60];
    statusBar.layer.cornerRadius = 6;
    statusBar.frame = CGRectMake(backgroundBar.frame.origin.x, backgroundBar.frame.origin.y,backgroundBar.frame.size.width,backgroundBar.frame.size.height);
    UIView *statusCircle1 = (UIView *)[cell viewWithTag:61];
    statusCircle1.frame = CGRectMake(backgroundCircle1.frame.origin.x, backgroundCircle1.frame.origin.y,backgroundCircle1.frame.size.width,backgroundCircle1.frame.size.height);
    statusCircle1.layer.cornerRadius = backgroundCircle1.frame.size.width/2;
    UIView *statusCircle2 = (UIView *)[cell viewWithTag:62];
    statusCircle2.frame = CGRectMake(backgroundCircle2.frame.origin.x, backgroundCircle2.frame.origin.y,backgroundCircle2.frame.size.width,backgroundCircle1.frame.size.height);
    statusCircle2.layer.cornerRadius = backgroundCircle2.frame.size.width/2;
    UIView *statusCircle3 = (UIView *)[cell viewWithTag:63];
    statusCircle3.frame = CGRectMake(backgroundCircle3.frame.origin.x, backgroundCircle3.frame.origin.y,backgroundCircle3.frame.size.width,backgroundCircle3.frame.size.height);
    statusCircle3.layer.cornerRadius = backgroundCircle3.frame.size.width/2;
    
    UIView *pipe1 = (UIView *)[cell viewWithTag:81];
    pipe1.backgroundColor = [ventouraUtility ventouraTextBodyGrey];
    UIView *pipe2 = (UIView *)[cell viewWithTag:82];
    pipe2.backgroundColor = [ventouraUtility ventouraTextBodyGrey];
    UIView *pipe3 = (UIView *)[cell viewWithTag:83];
    pipe3.backgroundColor = [ventouraUtility ventouraTextBodyGrey];
    
    
    UILabel *textLabel1 = (UILabel *)[cell viewWithTag:91];
    textLabel1.textColor = [ventouraUtility ventouraTextBodyGrey];
    textLabel1.font = [UIFont fontWithName:@"Roboto-Regular" size:14];
    
    UILabel *textLabel2 = (UILabel *)[cell viewWithTag:92];
    textLabel2.textColor = [ventouraUtility ventouraTextBodyGrey];
    textLabel2.font = [UIFont fontWithName:@"Roboto-Regular" size:14];
    UILabel *textLabel3 = (UILabel *)[cell viewWithTag:93];
    textLabel3.textColor = [ventouraUtility ventouraTextBodyGrey];
    textLabel3.font = [UIFont fontWithName:@"Roboto-Regular" size:14];
    
    UIImageView* crossImageView1 = (UIImageView *)[cell viewWithTag:71];
    [crossImageView1 setHidden:YES];
    UIImageView* crossImageView2 = (UIImageView *)[cell viewWithTag:72];
    [crossImageView2 setHidden:YES];
    UIImageView* crossImageView3 = (UIImageView *)[cell viewWithTag:73];
    [crossImageView3 setHidden:YES];
}

-(void)setUpStatusBarGuide:(UITableViewCell*)cell{

}

-(void)setUpStatusBarTravller:(UITableViewCell*)cell{
    NSLog(@"Trallver Tour");
    
    UIView *statusBar = (UIView *)[cell viewWithTag:60];
    UIView *statusCircle1 = (UIView *)[cell viewWithTag:61];
    UIView *statusCircle2 = (UIView *)[cell viewWithTag:62];
    UIView *statusCircle3 = (UIView *)[cell viewWithTag:63];
    UIImageView* crossImageView1 = (UIImageView *)[cell viewWithTag:71];
    UIImageView* crossImageView2 = (UIImageView *)[cell viewWithTag:72];
    UIImageView* crossImageView3 = (UIImageView *)[cell viewWithTag:73];

    switch ([self.tourBooking.bookingStatus integerValue]) {
        case REQUEST_BY_TRAVELLER:{
            statusBar.frame = CGRectMake(statusBar.frame.origin.x, statusBar.frame.origin.y, statusBar.frame.size.width/20, statusBar.frame.size.height);
            [statusCircle1 setHidden:YES];
            [statusCircle2 setHidden:YES];
            [statusCircle3 setHidden:YES];
            break;
        }
            
        default:
            break;
    }

}
-(void) setUpInfoTravller:(UITableViewCell*)cell{

    UILabel *info = (UILabel *)[cell viewWithTag:30];
    info.textColor = [ventouraUtility ventouraTextBodyGrey];
    info.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
    switch ([self.tourBooking.bookingStatus integerValue]) {
        case REQUEST_BY_TRAVELLER:{
            info.text = [NSString stringWithFormat:@"Waiting for response"];
            break;
        }
        default:
            break;
    }
}

-(void) setUpInfoGuide:(UITableViewCell*)cell{

    

            
}

-(void)setUpButtonsGuide{
    
}
-(void)setUpButtonsTraveller{

    switch ([self.tourBooking.bookingStatus integerValue]) {
        case REQUEST_BY_TRAVELLER:{
            [self.cancelBtn setTitle:@"Cancel Request" forState:UIControlStateNormal];
            self.cancelBtn.frame = CGRectMake(0, self.cancelBtn.frame.origin.y, 320, self.cancelBtn.frame.size.height);
            [self.cancelBtn setBackgroundColor: [ventouraUtility ventouraDarkGreyAlpha]];
            [self.actionBtn setHidden:YES];
            [self.cancelBtn addTarget:self
                               action:@selector(cancelTour)
                     forControlEvents:UIControlEventTouchUpInside];
            break;
        }
        default:
            break;
    }
}

-(void)cancelTour{

    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Cancel Request?" message:@"Do you want you cancel tour request?" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:nil] ;
    // optional - add more buttons:
    [alert addButtonWithTitle:@"Yes"];
    [alert setTag:100];
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    //Cancel Tour
    if ([alertView tag] == 100) {
        if (buttonIndex == 0) {     // and they clicked OK.
            // do stuff
        }
    }
}


@end
