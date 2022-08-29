//
//  MenuViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 1/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "MenuViewController.h"
#import "MessageViewController.h"
#import "SWRevealViewController.h"
ventouraClassAppDelegate *ventouraDelegate;

@implementation SWUITableViewCell

@end

@interface MenuViewController()
@property (nonatomic, strong) UIImageView *diamondImageView;
@property (nonatomic, strong) DBManager *dbManager;

@end

@implementation MenuViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor colorWithRed:233/255.0f green:113/255.0f blue:112/255.0f alpha:1.0f];
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];

    //self.view.backgroundColor =[UIColor blackColor];
    
//    UIGraphicsBeginImageContext(self.view.frame.size);
//    [[UIImage imageNamed:@"Background_Red.png"] drawInRect:self.view.bounds];
//    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//    self.view.backgroundColor = [UIColor colorWithPatternImage:image];

    self.tableView.separatorColor=[UIColor clearColor];
//    [[UINavigationBar appearance] setBarTintColor:[UIColor whiteColor]];
    
    
    
    self.tableView.backgroundColor = [ventouraUtility ventouraMenuPink];


}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
//    ventouraDelegate = [self appDelegate];
//    ventouraDelegate._messageDelegate = self;

    [self.tableView reloadData];

}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];

}

- (void)tableView:(UITableView *)tableView
  willDisplayCell:(UITableViewCell *)cell
forRowAtIndexPath:(NSIndexPath *)indexPath
{
    [cell setBackgroundColor:[UIColor clearColor]];
}

- (void) prepareForSegue: (UIStoryboardSegue *) segue sender: (id) sender
{
    // configure the destination view controller:
//    if ( [segue.destinationViewController isKindOfClass: [MessageViewController class]] &&
//        [sender isKindOfClass:[UITableViewCell class]] )
//    {
//        UILabel* c = [(SWUITableViewCell *)sender label];
//        MessageViewController* cvc = segue.destinationViewController;
//        
//        cvc.color = c.textColor;
//        cvc.text = c.text;
//    }else{
//
//    }

    // configure the segue.
    if ( [segue isKindOfClass: [SWRevealViewControllerSegue class]] )
    {
        SWRevealViewControllerSegue* rvcs = (SWRevealViewControllerSegue*) segue;
        
        SWRevealViewController* rvc = self.revealViewController;
        NSAssert( rvc != nil, @"oops! must have a revealViewController" );
        
        NSAssert( [rvc.frontViewController isKindOfClass: [UINavigationController class]], @"oops!  for this segue we want a permanent navigation controller in the front!" );

        rvcs.performBlock = ^(SWRevealViewControllerSegue* rvc_segue, UIViewController* svc, UIViewController* dvc)
        {
            UINavigationController* nc = [[UINavigationController alloc] initWithRootViewController:dvc];
            [rvc pushFrontViewController:nc animated:YES];
        };
    }
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
//this can be changed to return number of rows
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if ([ventouraUtility isUserGuide]) {
        return 5;
    }
    return 5;
}

-(UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {

    if(indexPath.row == 0){
        return 200;
    }
	return 50;

}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";

    switch ( indexPath.row )
    {
        case 0:
            CellIdentifier = @"profile";
            break;
            
        case 1:
            CellIdentifier = @"ventouring";
            break;

        case 2:{
            CellIdentifier = @"message";
            break;
        }
        case 3:
            CellIdentifier = @"tours";
            break;
        case 4:
            CellIdentifier = @"settings";
            break;
        case 5:
            CellIdentifier = @"promotion";
            break;
    }

    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier forIndexPath: indexPath];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    if (indexPath.row ==0) {
        
        UIImageView *profileImage = (UIImageView *)[cell viewWithTag:50];
        profileImage.frame = CGRectMake(screenWidth * 0.8 /2 - profileImage.frame.size.width /2, profileImage.frame.origin.y, profileImage.frame.size.width, profileImage.frame.size.height);
        
        
         NSArray *results = [ventouraDatabaseUtility userImageDataFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnMyUserId] isUserGuide:[ventouraUtility isUserGuide]];
        if (results.count>0) {

            NSString * imgId = results[0][0];
//            NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
            
//            imgId =[NSString stringWithFormat:@"%@/%@.png",imgPathPreFix,imgId];
            NSLog(@"image count %@",imgId);
            NSString* path = [ventouraUtility returnImagePathIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId] imageId:imgId];
            BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:path];
            if(fileExists){
                NSLog(@"image exists");
                profileImage.image = [ventouraUtility imageCropSquareCentre:[UIImage imageWithContentsOfFile:path]];
                profileImage.clipsToBounds = YES;
                profileImage.layer.cornerRadius = profileImage.frame.size.width / 2;
//                profileImage.layer.borderWidth = 0.5f;
                profileImage.layer.borderColor = [UIColor lightGrayColor].CGColor;

            }
            
        }
        
        
        UILabel *nameLabel = (UILabel *)[cell viewWithTag:51];
        nameLabel.frame = CGRectMake(screenWidth * 0.8 /2 - nameLabel.frame.size.width /2, nameLabel.frame.origin.y, nameLabel.frame.size.width, nameLabel.frame.size.height);
        
        NSArray *profileResult = [ventouraDatabaseUtility userDataFromDatabase:self.dbManager userId:[ventouraUtility returnMyUserIdWithType]];
        NSString* userName;
        if (profileResult.count>0) {
            NSLog(@"Profile Found");
            Person* tmp = [ProfileBuilder travellerProfileFromDatabase:profileResult[0]];
            userName = tmp.firstName;
        }
        nameLabel.font = [UIFont fontWithName:@"Roboto-Medium" size:18];
        nameLabel.text = userName;
        nameLabel.textColor = [UIColor whiteColor];
        
        UILabel *userTypeLabel = (UILabel *)[cell viewWithTag:52];
        userTypeLabel.frame = CGRectMake(screenWidth * 0.8 /2 - userTypeLabel.frame.size.width /2, userTypeLabel.frame.origin.y, userTypeLabel.frame.size.width, userTypeLabel.frame.size.height);

        userTypeLabel.font = [UIFont fontWithName:@"Roboto-Light" size:15];
        userTypeLabel.textColor = [UIColor whiteColor];

        if ([ventouraUtility isUserGuide]) {
            userTypeLabel.text = @"Local";
        }else{
            userTypeLabel.text = @"Traveller";
        }
        
        
        
    }
    else{
        UILabel *nameLabel = (UILabel *)[cell viewWithTag:51];
        nameLabel.font = [UIFont fontWithName:@"Roboto-Light" size:18];
    }
    
   

    
    if (indexPath.row ==2) {
        
        NSString *query2 =[NSString stringWithFormat:@"select sum(unreadCount) from Match where ownerId like '%@'", [ventouraUtility returnMyUserIdWithType]];
        NSArray *badges = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query2]];
        M13BadgeView *badgeView = (M13BadgeView *)[cell viewWithTag:901];

        if(badges.count>0){
            int unread = [badges[0][0] intValue];
            if(unread>0){
                badgeView.text = [NSString stringWithFormat:@"%d",unread];
                badgeView.badgeBackgroundColor = [UIColor colorWithRed:0.302 green:0.306 blue:0.357 alpha:1] /*#4d4e5b*/;
                badgeView.font = [UIFont systemFontOfSize:13.0];
                badgeView.frame = CGRectMake(220, 17, 20, 20);
                [badgeView setHidden:NO];
            }else{
                [badgeView setHidden:YES];
            }
        }else{
            [badgeView setHidden:YES];
        }
       
//        badgeView.borderColor = [UIColor whiteColor];
//        badgeView.borderWidth = 0.5f;

    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    return cell;
}


- (void)newMessageReceived:(NSDictionary *)messageContent from:(NSString *)sender{
    NSLog(@"New Msg Received From menu");
    
    [self.tableView reloadData];
    
}

@end
