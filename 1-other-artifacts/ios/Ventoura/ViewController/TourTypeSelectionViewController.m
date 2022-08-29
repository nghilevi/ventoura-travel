//
//  CitySelectionViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 15/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "TourTypeSelectionViewController.h"

@interface TourTypeSelectionViewController (){
}
//@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) NSArray *arrTourType;
@property (nonatomic, strong) NSString *itemToPassBack;
@property (nonatomic, strong) DBManager *dbManager;

@end

@implementation TourTypeSelectionViewController
@synthesize delegate;

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
    
    
//    self.arrTourType = [NSArray arrayWithObjects:@"Adrenaline Tour",
//                        @"Adventure Tour",
//                        @"Art & Design Tour",
//                        @"Bicycle Tour",
//                        @"Chillout Tour",
//                        @"Culture Tour",
//                        @"Explore Tour",
//                        @"Foodie Tour",
//                        @"Music Tour",
//                        @"Natural World Tour",
//                        @"Nightlife Tour",
//                        @"Photography Tour",
//                        @"Relaxation Tour",
//                        @"Shopping Tour",
//                        @"Spiritual Tour",
//                        @"Mystery Tour",
//                        @"Sport Tour",
//                        @"Style & Fashion Tour",nil];
//    

    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    NSString *query =[NSString stringWithFormat:@"select tourId, tourName from TourType Order By tourId"];
    if (self.arrTourType != nil) {
        self.arrTourType = nil;
    }
    self.arrTourType = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT);
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT);
    }
    self.tView.backgroundColor = [UIColor clearColor];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
//    self.itemToPassBack = @"Pass this value back to ViewControllerA";
//    NSLog(@"before dis%@", self.city.cityName);
    [self.delegate addItemViewController:self didFinishTourTypeSelection:self.tourType];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Table view delegate
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Other cells
    return indexPath;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    //should make a tourType Object, using city for now.
//    self.city.cityName = self.arrTourType[indexPath.row][1] ;
////    self.city.countryId = self.arrTourType[indexPath.row][0] ;
//    self.city.cityId = self.arrTourType[indexPath.row][0] ;
//    self.city.tableIndexPath = indexPath.row;
//    self.city = [[City alloc] initWithId:self.arrTourType[indexPath.row][0]
//                                cityName:self.arrTourType[indexPath.row][1]
//                               countryId:@""
//                             countryName:@""
//                          tableIndexPath:indexPath.row];
//    NSLog(@"clicked on %@",self.city.cityName );

    self.tourType = self.arrTourType[indexPath.row][0];
    [self.navigationController popViewControllerAnimated:TRUE];
    
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{

    return self.arrTourType.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 55;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *simpleTableIdentifier = @"cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@", self.arrTourType[indexPath.row][1]];
    cell.backgroundColor =[UIColor clearColor];
    cell.textLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
    cell.textLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
    if (([self.tourType integerValue]-1)==indexPath.row) {
        [cell setAccessoryType:UITableViewCellAccessoryCheckmark];
    }else{
        [cell setAccessoryType:UITableViewCellAccessoryNone];
        
    }

    return cell;
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

@end
