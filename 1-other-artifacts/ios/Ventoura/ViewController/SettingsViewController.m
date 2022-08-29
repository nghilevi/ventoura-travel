//
//  SettingsViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 16/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "SettingsViewController.h"
#import "SWRevealViewController.h"

@interface SettingsViewController ()

@end

@implementation SettingsViewController

- (ventouraClassAppDelegate *)appDelegate {
	return (ventouraClassAppDelegate *)[[UIApplication sharedApplication] delegate];
}


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
    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    
    [self.revealButtonItem setTarget: self.revealViewController];
    [self.revealButtonItem setAction: @selector( revealToggle: )];
    [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];

    self.tView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    self.tView.backgroundColor = [ventouraUtility ventourasettingsPaddingColour];
//    self.tView.opaque = NO;
//    self.tView.backgroundView = nil;
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
       self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }
    

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

- (UIView *) tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.bounds.size.width, 30)] ;
//    if (section == integerRepresentingYourSectionOfInterest)
        [headerView setBackgroundColor:[ventouraUtility ventourasettingsPaddingColour]];
//    else
//        [headerView setBackgroundColor:[UIColor clearColor]];
    return headerView;
}




- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    switch ( indexPath.section ){
        case 0:
            switch ( indexPath.row )
            {
                case 0:
                    CellIdentifier = @"MatchAlert";
                    break;
                
                case 1:
                    CellIdentifier = @"MsgAlert";
                    break;
                
                case 2:
                    CellIdentifier = @"TourAlert";
                    break;
   
            }
            break;
        case 1:
            switch ( indexPath.row )
            {
                case 0:
                    CellIdentifier = @"SwitchAccount";
                    break;
                
                case 1:
                    CellIdentifier = @"LogOut";
                    break;
                
                case 2:
                    CellIdentifier = @"DisableAccount";
                    break;
                case 3:
                    CellIdentifier = @"paymentCell";
                    break;
               
            }
            break;
        case 2 :
            switch ( indexPath.row )
            {
                case 0:
                    CellIdentifier = @"MoreInfo";
                    break;
                
                case 1:
                    CellIdentifier = @"ContactUs";
                    break;

            }
            break;
    }
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier forIndexPath: indexPath];
    switch ( indexPath.section ){
        case 0:{
            switch ( indexPath.row )
            {
                case 0:{ //match Alert
                    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
                    UISwitch *switchBtn  = (UISwitch *)[cell viewWithTag:11]; //just something I made up
                    [switchBtn setOnTintColor:[ventouraUtility ventouraMenuPink]];
                    [switchBtn addTarget:self
                                  action:@selector(matchAlertSwitch:)
                        forControlEvents:UIControlEventValueChanged];
                    break;
                }
                case 1:{ // message Alter
                    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
                    UISwitch *switchBtn  = (UISwitch *)[cell viewWithTag:11]; //just something I made up
                    [switchBtn setOnTintColor:[ventouraUtility ventouraMenuPink]];
                    [switchBtn addTarget:self
                                  action:@selector(messageAlertSwitch:)
                        forControlEvents:UIControlEventValueChanged];
                    break;
                }
                case 2:{ // half hour tour reminder
                    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
                    UISwitch *switchBtn  = (UISwitch *)[cell viewWithTag:11]; //just something I made up
                    [switchBtn setOnTintColor:[ventouraUtility ventouraMenuPink]];
                    [switchBtn addTarget:self
                                  action:@selector(halfHourTourReminderSwitch:)
                        forControlEvents:UIControlEventValueChanged];
                    break;
                }
                
            }
            break;
        }
        case 1:{
            switch ( indexPath.row )
            {
                case 3:
                    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    break;
            }
            break;
        }
        case 2:{
            switch ( indexPath.row )
            {
                case 0:{
                    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    break;
                }
                case 1:{
                    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    break;
                }
            }
            break;
        }
    }
    
    
    UILabel *titleLabel = (UILabel *)[cell viewWithTag:10];
    titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
    titleLabel.textColor = [ventouraUtility ventouraSettingTextColour];
    return cell;
}



//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
//	
//	NSDictionary *dict = (NSDictionary *)[messages objectAtIndex:indexPath.row];
//	NSString *msg = [dict objectForKey:@"msg"];
//	
//	CGSize  textSize = { 260.0, 10000.0 };
//	CGSize size = [msg sizeWithFont:[UIFont boldSystemFontOfSize:13]
//				  constrainedToSize:textSize
//					  lineBreakMode:UILineBreakModeWordWrap];
//	
//	size.height += padding*2;
//	
//	CGFloat height = size.height < 65 ? 65 : size.height;
//	return height;
//	
//}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if(section==0)
        return 3;
    else if(section==1){
        return 4;
    }
    
    return 2;

}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    switch ( indexPath.section ){
        case 1:
            switch ( indexPath.row )
        {
            case 0:{
                [self showSwitchAccountActionSheet];
                break;
            }
            case 1:
            {
                NSLog(@"log out:");
                [self showLogOutActionSheet];
                break;
            }
            case 2:{
                [self showDisbaleActionSheet];

                break;
            }
        }
            break;
        case 2 :
            switch ( indexPath.row )
        {
            case 0:{
                dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
                    dispatch_sync(dispatch_get_main_queue(), ^{
                        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                             bundle:nil];
                        SettingsAboutViewController *viewController =
                        [storyboard instantiateViewControllerWithIdentifier:@"SettingsAboutViewController"];
//                        [self presentViewController:viewController animated:YES completion:NULL];
                        [[self navigationController] pushViewController:viewController animated:YES ];

                    });
                });
                break;
            }
            case 1:
                break;
        }
            break;
    }
    
}
-(void)logoutAction{
    // Close an existing session.
    NSLog(@"logout FInal");
    [ventouraUtility userLogOut];
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    SWRevealViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
    [self presentViewController:viewController animated:YES completion:NULL];
    [[self appDelegate] disconnect];

}

-(void) switchAccount{
    NSLog(@"inside switchAccount method");
    
    //reset data
    //NSString *domainName = [[NSBundle mainBundle] bundleIdentifier];
    //[[NSUserDefaults standardUserDefaults] removePersistentDomainForName:domainName];
    
    //reset(remove) user id from NSUserDefaults
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
//    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"userVentouraId"];
//    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"userVentouraIsLoggedIn"];
//    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"isUserGuide"];
    [defaults synchronize];
    
//    _userVentouraID = @"";
    
    //keeping facebook data has user is still logged in.
    
    //show role selection screen
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        //[self saveSystemSettings];
        dispatch_sync(dispatch_get_main_queue(), ^{
            
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                 bundle:nil];
            SWRevealViewController *viewController =
            [storyboard instantiateViewControllerWithIdentifier:@"RoleSelectionViewController"];
            [self presentViewController:viewController animated:YES completion:NULL];
        });
    });
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 3;
}
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    NSString *sectionName;
    switch (section)
    {
        case 0:
//            sectionName = NSLocalizedString(@"Notifications", @"Notifications");
            break;
        case 1:
//            sectionName = NSLocalizedString(@"Account Options", @"Account Options");
            break;
        default:
//            sectionName = NSLocalizedString(@"Ventoura", @"Ventoura");
            break;
    }
    return sectionName;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 0)
        return 25;
    else
        return 25;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}


-(void)showSwitchAccountActionSheet{
    NSString *swtichAccountText = @"";
    if([ventouraUtility isUserGuide]){
        swtichAccountText = @"Switch to Traveller Account";
    }else{
        swtichAccountText = @"Switch to Local Account";

    }
    /*UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Do you want to swtich account?"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:swtichAccountText, nil];*/
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Do you want to swtich accounts?"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Switch Accounts", nil];
    actionSheet.tag = 100;
    [actionSheet showInView:self.view];
}

-(void)showLogOutActionSheet{
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Do you want to log out?"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Logout", nil];
    actionSheet.tag = 200;
    [actionSheet showInView:self.view];
}

-(void)showDisbaleActionSheet{
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@"Do you want to disable your account?"
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Disable Account", nil];
    actionSheet.tag = 300;
    [actionSheet showInView:self.view];
}



-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (actionSheet.tag == 100) {
//        NSLog(@"Index = %d - Title = %@", buttonIndex, [actionSheet buttonTitleAtIndex:buttonIndex]);
        
        if (buttonIndex ==0) {
            
            [self switchAccount];
            //clean out login cache
            
            //show role screen
            NSLog(@"Switch");
        }
    }
    if (actionSheet.tag == 200) {
        if (buttonIndex ==0) {
            [self logoutAction];
            
            NSLog(@"Log out");
        }
    }
    if (actionSheet.tag == 300) {
        if (buttonIndex ==0) {
            NSLog(@"Disable");
        }
    }
}

-(void) matchAlertSwitch:(id)sender {
    if([sender isOn]){
       NSLog(@"matchAlterSwitch on");
    }
    else{
        NSLog(@"matchAlterSwitch off");
    }
}

-(void) messageAlertSwitch:(id)sender {
    if([sender isOn]){
        NSLog(@"messageAlertSwitch on");
    }
    else{
        NSLog(@"messageAlertSwitch off");
    }
}

-(void) halfHourTourReminderSwitch:(id)sender {
    if([sender isOn]){
        NSLog(@"halfHourTourReminderSwitch on");
    }
    else{
        NSLog(@"halfHourTourReminderSwitch off");
    }
}

@end
