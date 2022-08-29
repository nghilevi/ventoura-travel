//
//  CityFilterViewController.m
//  Ventoura
//
//  Created by Jai Carlton on 6/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "CityFilterViewController.h"

@interface CityFilterViewController ()<TourManagerDelegate>{
    NSArray *tourTripList;
    NSMutableArray *tripListSelection;
    NSMutableArray *tripListSelected; //parallel array to tripList;
    TourManager *tmanager;
}
@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) NSMutableArray* selectedCities;


@end

@implementation CityFilterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.cityFilterOn = FALSE;
    tripListSelection = [NSMutableArray array];
    tripListSelected = [NSMutableArray array];
    
    self.tripList = [NSMutableArray array];
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    
    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView = [[UIImageView alloc] initWithImage:backgroundImg];
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
    
    tmanager = [[TourManager alloc] init];
    tmanager.communicator =[[TourCommunicator alloc] init];
    tmanager.communicator.delegate = tmanager;
    tmanager.delegate = self;
    
    [tmanager fetchTripAndTour];
    
}

- (void)viewWIllAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    NSLog(@"city view will disappear!");
    [self.delegate passCitySelection: tripListSelection];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"specify cities selected");
    if(self.cityFilterOn)
    {
        //NSLog(@"cityfilteron");
        //selecting from the list
        //NSLog(@"%li", indexPath.row);
        if(self.tripList.count > 0 && indexPath.row > 0){
            //NSLog(@"triplist has stuff and index path is > 0");
            //NSLog(@"%@", tripList[indexPath.row - 1]);
            
            
            UITableViewCell *cell = [self.tView cellForRowAtIndexPath:indexPath];
            if(cell.accessoryType == UITableViewCellAccessoryNone){
                //cell.accessoryType = UITableViewCellAccessoryCheckmark;
                [tripListSelection addObject:self.tripList[indexPath.row - 1]];
                tripListSelected[indexPath.row - 1] = @"yes";
            }
            else{
                cell.accessoryType = UITableViewCellAccessoryNone;
                
                for(int i = 0; i < tripListSelection.count; i++)
                {
                    if([tripListSelection[i] isEqualToString: self.tripList[indexPath.row - 1]]){
                        [tripListSelection removeObjectAtIndex:i];
                        tripListSelected[indexPath.row - 1] = @"no";
                        break;
                    }
                    
                }
                
            }
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
                dispatch_sync(dispatch_get_main_queue(), ^{
                    [self.tView reloadData];
                });
            });
            //NSLog(@"%li", tripListSelection.count);
        }
        else{ //nothing to select, so dont do anything
            
        }
    }
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(self.cityFilterOn){
        if(self.tripList.count == 0) return 2; // on off plus a message saying no cities, make a trip.
        else                        return self.tripList.count + 1; // city selection items //TODO add in variable amount of rows based on what is returned from trip query
    }
    else { // show nothing
        return 2;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{

    switch(indexPath.row){
        case 0:{ // city filter on off
            return 50;
            break;
        }
    }

    return 40;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"SelectionItem";
    switch(indexPath.row)
    {
        case 0: {
            CellIdentifier = @"CityFilterOnOff";
            break;
        }
        default:{
            CellIdentifier = @"SelectionItem";
        }
    }
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    //tour and trip data is in one, thz chen :P
    
    
    switch (indexPath.row)
    {
        case 0:{
            cell.textLabel.text = @"Use City Filter";
            cell.textLabel.textColor = [UIColor lightGrayColor];
            cell.textLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            
            UISwitch* cityFilterSwitch = (UISwitch*)[cell viewWithTag:30];
            [cityFilterSwitch setOn:self.cityFilterOn animated:false];
            [cityFilterSwitch addTarget:self action:@selector(cityFilterSwitchChange:) forControlEvents:UIControlEventValueChanged];
            
            UIView * line = (UIView *)[cell viewWithTag:50];
            line.backgroundColor = [UIColor lightGrayColor];
            break;
        }
        default:{
            if(!self.cityFilterOn){
                //display nothing
                cell.textLabel.text = @"";
                cell.accessoryType = UITableViewCellAccessoryNone;
                
            }
            else if(self.tripList.count > 0){
                
                NSString* city = self.tripList[indexPath.row-1];
                //this will show all the cites in triplist using indexPath.row
                cell.textLabel.text = [NSString stringWithFormat:@"%@",city];
                cell.textLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
                //cell.accessoryType = UITableViewCellAccessoryCheckmark;
                cell.tintColor = [ventouraUtility ventouraPink];
                if([tripListSelected[indexPath.row - 1] isEqualToString:@"yes"]) {
                    cell.accessoryType = UITableViewCellAccessoryCheckmark;
                }
                else{
                    cell.accessoryType = UITableViewCellAccessoryNone;
                }
                cell.indentationWidth = 20;
                cell.indentationLevel = 1;
            }
            else{
                //display "you have no trips, follow here to go make one"
                cell.textLabel.text = @"You have no trips currently created.";
            }
            break;
        }
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.backgroundColor = [UIColor clearColor];
    return cell;
}

- (void)cityFilterSwitchChange:(id)sender
{
    BOOL state = [sender isOn];
    if(state)NSLog(@"true");
    else    NSLog(@"false");
    self.cityFilterOn = state;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
}

- (void) didReceiveTourTripObject: (NSArray*) tourTripObjects{
    NSLog(@"%li", tourTripObjects.count);
    
    for(int i = 0; i < tourTripObjects.count; i++)
    {
        Tour* tmp = tourTripObjects[i];
        NSLog(@"%@", tmp.isTour);
        if(![((NSString*)tmp.isTour) integerValue]){
            NSArray* arr = [ventouraDatabaseUtility getCityFromDatabase:self.dbManager cityId:tmp.city][0]; // function returns array, city is first element
            [self.tripList addObject:arr[0]];
        }
        [tripListSelected addObject:@"no"];
    }
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.tView reloadData];
        });
    });
}



@end
