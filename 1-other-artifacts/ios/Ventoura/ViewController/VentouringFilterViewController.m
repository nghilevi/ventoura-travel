//
//  VentouringFilterViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 15/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "VentouringFilterViewController.h"
@class VenoutringFilterViewController;
@protocol VentouringFilterViewControllerDelegate <NSObject>

@end

@interface VentouringFilterViewController () <CityFilterViewControllerDelegate>
@property (nonatomic,assign) float minAge;
@property (nonatomic,assign) float maxAge;
@property (nonatomic,assign) float minPrice;
@property (nonatomic,assign) float maxPrice;
@property (nonatomic,assign) bool autoMatch;
@property (nonatomic,assign) NSString* gender; // 3 values: female, both, males
@property (nonatomic,assign) NSString* people; // 3 values: Travellers, both, locals
@property (nonatomic, strong) DBManager *dbManager;

@end

@implementation VentouringFilterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    
    //[self loadFilterSettings];
    
    
    //defaults
    self.minAge = 18;
    self.maxAge = 55;
    self.minPrice = 0;
    self.maxPrice = 200;
    self.gender = @"both";
    self.people = @"both";
    self.autoMatch = FALSE;
    //load settings if any stored
    self.citySelection = [NSMutableArray array];
    
    
    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
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
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT);
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT);
    }


//    [self.tView reloadData];
    // Do any additional setup after loading the view.
}

-(void) loadFilterSettings{
    NSLog(@"load filter settings");

    
    NSArray* results = [ventouraDatabaseUtility getFilterSettingsFromDatabase:self.dbManager
                                                                      ownerId:[ventouraUtility returnMyUserId]isUserGuide:[ventouraUtility isUserGuide]];
    
    if(results.count > 0) //some settings saved
    {
        NSArray* res = results[0];
        NSLog(@"results count: %li", res.count);
        self.gender = res[0];
        self.people = res[1];
        self.minAge = [res[2] integerValue];
        self.maxAge = [res[3] integerValue];
        self.minPrice = [res[4] integerValue];
        self.maxPrice = [res[5] integerValue];
       
        self.autoMatch = ([res[6] integerValue])?TRUE:FALSE;
        NSLog(@"%@", res[6]);
    }
    
    // city selection now
    /*
    results = [ventouraDatabaseUtility getFilterSettingsCitySelectionFromDatabase:self.dbManager
                                                             ownerId:[ventouraUtility returnMyUserId]isUserGuide:[ventouraUtility isUserGuide]];
    
    if(results.count > 0)
    {
        NSArray* res = results[0];
        for(int i = 0; i < res.count; i++)
        {
            [self.citySelection addObject: res[i]];
        }
    }
    */
    //else //use default
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self loadFilterSettings];
//    [self.tView reloadData];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    
    /*([[NSUserDefaults standardUserDefaults] setFloat: self.minAge forKey:@"minAge"];
    [[NSUserDefaults standardUserDefaults] setFloat: self.maxAge forKey:@"maxAge"];
    [[NSUserDefaults standardUserDefaults] setFloat: self.minPrice forKey:@"minPrice"];
    [[NSUserDefaults standardUserDefaults] setFloat: self.maxPrice forKey:@"maxPrice"];
    [[NSUserDefaults standardUserDefaults] setObject: self.gender forKey:@"gender"];
    [[NSUserDefaults standardUserDefaults] setObject: self.people forKey:@"people"];
    [[NSUserDefaults standardUserDefaults] setBool: self.autoMatch forKey:@"autoMatch"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    */
    
    if([ventouraUtility isUserGuide])
    {
        NSLog(@"guide filter save");
        NSLog(@"stuff: %@, %@, %@, %@", [ventouraUtility returnMyUserId], self.gender, [NSString stringWithFormat: @"%f", self.minAge], [NSString stringWithFormat: @"%f", self.maxAge]);
        [ventouraDatabaseUtility saveGuideFilterSettingsToDatabase:self.dbManager
                                                           ownerId:[ventouraUtility returnMyUserId]
                                                       isUserGuide:[ventouraUtility isUserGuide]
                                                            gender:self.gender
                                                            ageMin:[NSString stringWithFormat: @"%f", self.minAge] ageMax:[NSString stringWithFormat: @"%f", self.maxAge] autoMatch:self.autoMatch];
    }
    else
    {
        NSLog(@"traveller filter save");
         NSLog(@"stuff: %@, %@, %@, %@, %@, %@, %@", [ventouraUtility returnMyUserId], self.gender, [NSString stringWithFormat: @"%f", self.minAge], [NSString stringWithFormat: @"%f", self.maxAge], self.people, [NSString stringWithFormat: @"%f", self.minPrice], [NSString stringWithFormat: @"%f", self.maxPrice]);
        //NSArray* temp = [NSArray arrayWithObject: [NSString stringWithFormat:@"hello"]];
        
        [ventouraDatabaseUtility saveTravellerFilterSettingsToDatabase:self.dbManager
                                                           ownerId:[ventouraUtility returnMyUserId]
                                                           isUserGuide:[ventouraUtility isUserGuide]
                                                                gender:self.gender
                                                              userType:self.people
                                                                ageMin:[NSString stringWithFormat: @"%f", self.minAge]
                                                                ageMax:[NSString stringWithFormat: @"%f", self.maxAge]
                                                              priceMin:[NSString stringWithFormat: @"%f", self.self.minPrice]
                                                              priceMax:[NSString stringWithFormat: @"%f", self.maxPrice] cities:self.citySelection];
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


#pragma mark - Table view delegate
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Other cells
    return indexPath;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"%li", indexPath.row);
    if(indexPath.row == 4){
        NSLog(@"specify cities selected");
        //go to specify cities page
        [self cityFilterSelected];
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
    {
        
        
        
        static NSString *CellIdentifier = @"Cell";
        switch ( indexPath.row )
        {
            case 0:
                CellIdentifier = @"ScopeCell";
                break;
            case 1:
                if ([ventouraUtility isUserGuide]) {
                    CellIdentifier = @"Cell";
                    break;
                }
                CellIdentifier = @"ScopeCell";
                break;
                
            case 2:
                CellIdentifier = @"RangeSelectorCell";
                break;
            case 3:
                CellIdentifier = @"RangeSelectorCell";
                if ([ventouraUtility isUserGuide]) {
                    CellIdentifier = @"Cell";
                    break;
                }
                break;
            case 4:
                if([ventouraUtility isUserGuide]){
                    CellIdentifier = @"AutoMatchCell";
                }
                else{
                    CellIdentifier = @"CityFilter";
                }
                break;
        }
        
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        }
        
        
        switch ( indexPath.row )
        {
            case 0:{
                UILabel * titleLabel = (UILabel*)[cell viewWithTag:20];
                titleLabel.text = @"Gender";
                titleLabel.textColor = [UIColor lightGrayColor];
                titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];

                
                
                UISegmentedControl *genderControl = (UISegmentedControl *)[cell viewWithTag:1];
                [genderControl setTitle:@"Females" forSegmentAtIndex:0];
                [genderControl setTitle:@"Both" forSegmentAtIndex:1];
                [genderControl setTitle:@"Males" forSegmentAtIndex:2];
                NSInteger selector = 1;
                if([self.gender isEqualToString:@"females"]) selector = 0;
                else if([self.gender isEqualToString:@"both"]) selector = 1;
                else if([self.gender isEqualToString:@"males"]) selector = 2;
                [genderControl setSelectedSegmentIndex: selector];
                genderControl.tintColor = [ventouraUtility ventouraPink];
                 [genderControl addTarget:self action:@selector(changeGenderFilter:) forControlEvents:UIControlEventValueChanged];
                UIView * line = (UIView *)[cell viewWithTag:50];
                line.backgroundColor = [UIColor lightGrayColor];
                break;
            }
            case 1:{
                UILabel * titleLabel = (UILabel*)[cell viewWithTag:20];
                titleLabel.text = @"People";
                titleLabel.textColor = [UIColor lightGrayColor];
                titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];

                UISegmentedControl *userControl = (UISegmentedControl *)[cell viewWithTag:1];
                [userControl setTitle:@"Travellers" forSegmentAtIndex:0];
                [userControl setTitle:@"Both" forSegmentAtIndex:1];
                [userControl setTitle:@"Locals" forSegmentAtIndex:2];
                NSInteger selector = 1;
                if([self.people isEqualToString:@"travellers"]) selector = 0;
                else if([self.people isEqualToString:@"both"]) selector = 1;
                else if([self.people isEqualToString:@"locals"]) selector = 2;
                [userControl setSelectedSegmentIndex: selector];
                
                userControl.tintColor = [ventouraUtility ventouraPink];
                [userControl addTarget:self action:@selector(changeUserFilter:) forControlEvents:UIControlEventValueChanged];
                UIView * line = (UIView *)[cell viewWithTag:50];
                line.backgroundColor = [UIColor lightGrayColor];
                break;

                
            }
            case 3:{
                UILabel * titleLabel = (UILabel*)[cell viewWithTag:20];
                titleLabel.text = @"Price";
                titleLabel.textColor = [UIColor lightGrayColor];
                titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
                
                
                NMRangeSlider *rangeSlider =(NMRangeSlider *)[cell viewWithTag:2];
                [self configureRangeSlider:rangeSlider];
                UILabel *lowerLabel =(UILabel *)[cell viewWithTag:10];
                UILabel *upperLabel =(UILabel *)[cell viewWithTag:11];
                
                UIImageView* lowerPointer = (UIImageView*)[cell viewWithTag:31];
                UIImageView* upperPointer = (UIImageView*)[cell viewWithTag:32];
                
                
                rangeSlider.minimumValue = 0;
                rangeSlider.maximumValue = 200;
                rangeSlider.stepValue = 10;
                rangeSlider.minimumRange = 30;
                rangeSlider.upperValue = self.maxPrice;
                rangeSlider.lowerValue = self.minPrice;
                rangeSlider.backgroundColor = [UIColor clearColor];
                CGPoint upperCenter;
                CGPoint lowerCenter;
                CGPoint upperPointerCenter;
                CGPoint lowerPointerCenter;
                
                lowerCenter.x = rangeSlider.lowerValue / rangeSlider.maximumValue * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - lowerLabel.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;
                lowerCenter.y = (rangeSlider.center.y - 40.0f);
                lowerLabel.frame = CGRectMake(lowerCenter.x, lowerCenter.y, lowerLabel.frame.size.width, lowerLabel.frame.size.height);
                lowerLabel.text = [NSString stringWithFormat:@"$%d", (int)rangeSlider.lowerValue];
                upperLabel.text = [NSString stringWithFormat:@"$%d", (int)rangeSlider.upperValue];
                lowerLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                upperLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                
                upperLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:14];
                
                lowerLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:14];
                
                upperCenter.x = rangeSlider.upperValue / rangeSlider.maximumValue * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - upperLabel.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;
                
                upperCenter.y = (rangeSlider.center.y - 40.0f);
                upperLabel.frame = CGRectMake(upperCenter.x, upperCenter.y, upperLabel.frame.size.width, upperLabel.frame.size.height);
                
                upperPointerCenter.y = (rangeSlider.center.y - 20.0f);
                lowerPointerCenter.y = (rangeSlider.center.y - 20.0f);
                
                upperPointerCenter.x = rangeSlider.upperValue / rangeSlider.maximumValue * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - upperPointer.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;
                lowerPointerCenter.x = rangeSlider.lowerValue / rangeSlider.maximumValue * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - lowerPointer.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;
                
                upperPointer.frame = CGRectMake(upperPointerCenter.x, upperPointerCenter.y, upperPointer.frame.size.width, upperPointer.frame.size.height);
                lowerPointer.frame = CGRectMake(lowerPointerCenter.x, lowerPointerCenter.y, lowerPointer.frame.size.width, lowerPointer.frame.size.height);
                [rangeSlider addTarget:self action:@selector(priceSliderValueChanged:) forControlEvents:UIControlEventValueChanged];
                
                UIView * line = (UIView *)[cell viewWithTag:50];
                line.backgroundColor = [UIColor lightGrayColor];
                break;

                
            }
            case 2:{
                
                UILabel * titleLabel = (UILabel*)[cell viewWithTag:20];
                titleLabel.text = @"Age";
                titleLabel.textColor = [UIColor lightGrayColor];
                titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];

                
                 NMRangeSlider *rangeSlider =(NMRangeSlider *)[cell viewWithTag:2];
                [self configureRangeSlider:rangeSlider];
                UILabel *lowerLabel =(UILabel *)[cell viewWithTag:10];
                UILabel *upperLabel =(UILabel *)[cell viewWithTag:11];
                
                UIImageView* lowerPointer = (UIImageView*)[cell viewWithTag:31];
                UIImageView* upperPointer = (UIImageView*)[cell viewWithTag:32];

                rangeSlider.minimumRange = 4;
                rangeSlider.minimumValue = 18;
                rangeSlider.maximumValue =55;
                rangeSlider.upperValue = self.maxAge;
                rangeSlider.lowerValue = self.minAge;
                rangeSlider.backgroundColor = [UIColor clearColor];
                CGPoint upperCenter;
                CGPoint lowerCenter;
                CGPoint upperPointerCenter;
                CGPoint lowerPointerCenter;
                
                lowerCenter.x = (rangeSlider.lowerValue - rangeSlider.minimumValue) / (rangeSlider.maximumValue - rangeSlider.minimumValue) * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - lowerLabel.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;
                lowerCenter.y = (rangeSlider.center.y - 40.0f);
                lowerLabel.frame = CGRectMake(lowerCenter.x, lowerCenter.y, lowerLabel.frame.size.width, lowerLabel.frame.size.height);
                lowerLabel.text = [NSString stringWithFormat:@"%d", (int)rangeSlider.lowerValue];
                upperLabel.text = [NSString stringWithFormat:@"%d", (int)rangeSlider.upperValue];
                if(self.maxAge==55){
                    upperLabel.text = [NSString stringWithFormat:@"%d+", (int)rangeSlider.upperValue];

                }
                lowerLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                upperLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                
                upperLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:14];

                lowerLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:14];

                upperCenter.x = (rangeSlider.upperValue- rangeSlider.minimumValue) / (rangeSlider.maximumValue- rangeSlider.minimumValue) * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - upperLabel.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;

                upperCenter.y = (rangeSlider.center.y - 40.0f);
                upperLabel.frame = CGRectMake(upperCenter.x, upperCenter.y, upperLabel.frame.size.width, upperLabel.frame.size.height);
                
                upperPointerCenter.y = (rangeSlider.center.y - 20.0f);
                lowerPointerCenter.y = (rangeSlider.center.y - 20.0f);
                
                upperPointerCenter.x = (rangeSlider.upperValue- rangeSlider.minimumValue) / (rangeSlider.maximumValue- rangeSlider.minimumValue) * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - upperPointer.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;
                lowerPointerCenter.x = (rangeSlider.lowerValue- rangeSlider.minimumValue) / (rangeSlider.maximumValue- rangeSlider.minimumValue) * (rangeSlider.frame.size.width-rangeSlider.lowerHandleImageNormal.size.width) - lowerPointer.frame.size.width/2+rangeSlider.lowerHandleImageNormal.size.width/2;
                
                upperPointer.frame = CGRectMake(upperPointerCenter.x, upperPointerCenter.y, upperPointer.frame.size.width, upperPointer.frame.size.height);
                lowerPointer.frame = CGRectMake(lowerPointerCenter.x, lowerPointerCenter.y, lowerPointer.frame.size.width, lowerPointer.frame.size.height);
                [rangeSlider addTarget:self action:@selector(ageSliderValueChanged:) forControlEvents:UIControlEventValueChanged];
                
                UIView * line = (UIView *)[cell viewWithTag:50];
                line.backgroundColor = [UIColor lightGrayColor];
                
                break;
            }
            case 4:{
                if ([ventouraUtility isUserGuide]) { // auto match for locals/guides
                    UIView * line = (UIView *)[cell viewWithTag:50];
                    line.backgroundColor = [UIColor lightGrayColor];
                    UILabel * titleLabel = (UILabel*)[cell viewWithTag:20];
                    titleLabel.text = @"Auto Matching";
                    titleLabel.textColor = [UIColor lightGrayColor];
                    titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
                    UILabel * content = (UILabel*)[cell viewWithTag:21];
                    content.textColor = [ventouraUtility ventouraTextBodyGrey];
                    content.font = [UIFont fontWithName:@"Roboto-Regular" size:14];
                    content.text = @"Don't want to swipe? \nTurn on automatic matching";
                    content.numberOfLines = 0;
                    [content sizeToFit];
                    UISwitch* autoMatchSwitch = (UISwitch*)[cell viewWithTag:30];
                    [autoMatchSwitch setOn:self.autoMatch animated:false];
                    [autoMatchSwitch addTarget:self action:@selector(autoMatchSwitchChange:) forControlEvents:UIControlEventValueChanged];
                    break;
                }
                else{ // city filter for travellers
                    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    cell.textLabel.text = @"Specify City";
                    break;
                    
                }
            }
                
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
        return cell;
    }

    


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    switch ( indexPath.row )
    {
        case 0:
            return 100;
            break;
        case 1:
            if ([ventouraUtility isUserGuide]) {
                return 0;
            }
            return 100;
            break;
        case 2:
            return 120;
            break;
        case 3:
            if ([ventouraUtility isUserGuide]) {
                return 0;
            }
            return 120;
            break;
        case 4:
            if([ventouraUtility isUserGuide]){
                return 80;
            }
            else{
                return 50;
            }
            
            break;
            
    }
    return 100;
    
}

-(void)changeGenderFilter:(id)sender{
    UISegmentedControl *segmentedControl = (UISegmentedControl *) sender;
    NSInteger selectedSegment = segmentedControl.selectedSegmentIndex;
    
    if (selectedSegment == 0) {
        NSLog(@"selected 0");
        self.gender = @"females";
    }
    else if (selectedSegment == 1) {
        NSLog(@"selected 1");
        self.gender = @"both";

    }else{
        NSLog(@"selected 2");
        self.gender = @"males";

    }
}



-(void)changeUserFilter:(id)sender{
    UISegmentedControl *segmentedControl = (UISegmentedControl *) sender;
    NSInteger selectedSegment = segmentedControl.selectedSegmentIndex;
    
    if (selectedSegment == 0) {
        NSLog(@"selected 0");
        self.people = @"travellers";
    }
    else if (selectedSegment == 1) {
        NSLog(@"selected 1");
        self.people = @"both";
        
    }else{
        NSLog(@"selected 2");
        self.people = @"locals";
        
    }
    //[self.tView reloadData];
}

- (void) configureRangeSlider:(NMRangeSlider*) slider
{
    UIImage* image = nil;
    
    image = [UIImage imageNamed:@"RangeGreySlide.png"];
    image = [image resizableImageWithCapInsets:UIEdgeInsetsMake(0.0, 7.0, 0.0, 7.0)];
    slider.trackBackgroundImage = image;
    
    image = [UIImage imageNamed:@"RangeSalmonSlide"];
    image = [image resizableImageWithCapInsets:UIEdgeInsetsMake(0.0, 7.0, 0.0, 7.0)];
    slider.trackImage = image;
    
    image = [UIImage imageNamed:@"rangeNob.png"];
    //    image = [image imageWithAlignmentRectInsets:UIEdgeInsetsMake(-1, 2, 1, 2)];
    slider.lowerHandleImageNormal = image;
    slider.upperHandleImageNormal = image;
    
    image = [UIImage imageNamed:@"rangeNob.png"];
    //    image = [image imageWithAlignmentRectInsets:UIEdgeInsetsMake(-1, 2, 1, 2)];
    slider.lowerHandleImageHighlighted = image;
    slider.upperHandleImageHighlighted = image;
    
//    [slider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
}

- (IBAction)ageSliderValueChanged:(NMRangeSlider *)sender {
     NSLog(@"slider value, %f", sender.lowerValue);
    //    self.price = sender.value;
    self.minAge = sender.lowerValue;
    self.maxAge = sender.upperValue;
        [self.tView reloadData];
}


- (IBAction)priceSliderValueChanged:(NMRangeSlider *)sender {
    NSLog(@"slider value, %f", sender.lowerValue);
    //    self.price = sender.value;
    self.minPrice = sender.lowerValue;
    self.maxPrice = sender.upperValue;
    [self.tView reloadData];
}

- (void)autoMatchSwitchChange:(id)sender
{
    BOOL state = [sender isOn];
    self.autoMatch = state;
}

-(void) cityFilterSelected{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    CityFilterViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"CityFilterViewController"];
    
    viewController.delegate = self;
    viewController.tripList = [NSMutableArray array];
    [self.navigationController pushViewController:viewController animated:YES];
}

-(void) passCitySelection:(NSArray *)citySel{
    self.citySelection = citySel;
    for ( NSString *str in self.citySelection )
    {
        NSLog(@"%@", str);
    }
}

@end
