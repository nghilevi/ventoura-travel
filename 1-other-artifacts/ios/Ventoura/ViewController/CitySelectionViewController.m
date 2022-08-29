//
//  CitySelectionViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 15/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "CitySelectionViewController.h"

@interface CitySelectionViewController (){
}
@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) NSArray *arrCity;
@property (nonatomic, strong) NSString *itemToPassBack;

@end

@implementation CitySelectionViewController
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
    // Do any additional setup after loading the view.
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    NSString *query =[NSString stringWithFormat:@"select cityName, countryId, id from City Order By cityName"];
    if (self.arrCity != nil) {
        self.arrCity = nil;
    }
    self.arrCity = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
    NSLog(@"city name , %@", self.arrCity[0][0]) ;
    
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    //    self.tView.backgroundColor = [ventouraUtility ventouraNavBackgroundColour];
    self.navigationController.navigationBar.translucent = NO;
    self.tView.backgroundColor = [UIColor clearColor];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(self.tView.frame.origin.x, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }else{
        self.tView.frame = CGRectMake(self.tView.frame.origin.x, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }

    
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    self.itemToPassBack = @"Pass this value back to ViewControllerA";
    NSLog(@"before dis%@", self.city.cityName);
    [self.delegate addItemViewController:self didFinishCitySelection:self.city];
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
//    self.city.cityName = self.arrCity[indexPath.row][0] ;
//    self.city.countryId = self.arrCity[indexPath.row][1] ;
//    self.city.cityId = self.arrCity[indexPath.row][2] ;
//    self.city.tableIndexPath = indexPath.row;
    self.city = [[City alloc] initWithId:self.arrCity[indexPath.row][2]
                                cityName:self.arrCity[indexPath.row][0]
                               countryId:self.arrCity[indexPath.row][1]
                             countryName:@""
                          tableIndexPath:indexPath.row];
    NSLog(@"clicked on %@",self.city.cityName );

    
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

    return self.arrCity.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 44;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *simpleTableIdentifier = @"cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@", self.arrCity[indexPath.row][0]];
    cell.backgroundColor =[UIColor clearColor];
    cell.textLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
    cell.textLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
    if ([self.arrCity[indexPath.row][0] isEqualToString:self.city.cityName]) {
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
