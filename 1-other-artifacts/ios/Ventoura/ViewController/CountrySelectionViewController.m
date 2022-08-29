//
//  CitySelectionViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 15/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "CountrySelectionViewController.h"

@interface CountrySelectionViewController (){
}
@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) NSArray *arrCountry;
@property (nonatomic, strong) NSString *itemToPassBack;

@end

@implementation CountrySelectionViewController
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
    NSString *query =[NSString stringWithFormat:@"select id, countryName from Country Order By countryName"];
    if (self.arrCountry != nil) {
        self.arrCountry = nil;
    }
    self.arrCountry = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
    NSLog(@"country name , %@", self.arrCountry[0][0]) ;
    self.tView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
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
        self.tView.frame = CGRectMake(self.tView.frame.origin.x, 0, 320, IPHONE5SCREENHEIGHT);
    }else{
        self.tView.frame = CGRectMake(self.tView.frame.origin.x, 0, 320, IPHONE4SCREENHEIGHT);
    }
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    self.itemToPassBack = @"Pass this value back to ViewControllerA";
//    NSLog(@"before dis%@", self.city.cityName);
    [self.delegate addItemViewController:self didFinishCountrySelection:self.country];
//    [self.delegate addItemViewController:self didFinishCitySelection:self.city]
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
    self.country = [[City alloc] initWithId:nil
                                cityName:nil
                               countryId:self.arrCountry[indexPath.row][0]
                             countryName:self.arrCountry[indexPath.row][1]
                          tableIndexPath:indexPath.row];
    NSLog(@"clicked on %@",self.country.countryName );

    
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

    return self.arrCountry.count;
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
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@", self.arrCountry[indexPath.row][1]];
    cell.backgroundColor = [UIColor clearColor];
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
