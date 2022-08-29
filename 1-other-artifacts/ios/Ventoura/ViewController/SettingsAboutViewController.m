//
//  SettingsAboutViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 1/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "SettingsAboutViewController.h"

@interface SettingsAboutViewController ()

@end

@implementation SettingsAboutViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.sView.backgroundColor = [ventouraUtility ventourasettingsPaddingColour];
    self.tView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    self.tView.alwaysBounceVertical = NO;
    self.tView.backgroundColor = [ventouraUtility ventourasettingsPaddingColour];
    self.versionLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
    self.versionLabel.textColor = [ventouraUtility ventouraSettingLegalTextColour];
    
    self.rightsLabel1.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
    self.rightsLabel1.textColor = [ventouraUtility ventouraSettingLegalTextColour];
    self.rightsLabel2.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
    self.rightsLabel2.textColor = [ventouraUtility ventouraSettingLegalTextColour];
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
//        self.tView.frame = CGRectMake(0, 0, 320, 135);
    }else{
        self.tView.frame = CGRectMake(self.tView.frame.origin.x, self.tView.frame.origin.y, 320, 335);
        self.sView.frame = CGRectMake(0, 0, 320, 480);
    }

    [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT)];

    
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



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"SettingCell";
   
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier forIndexPath: indexPath];
    UILabel *titleLabel = (UILabel *)[cell viewWithTag:10];

            switch ( indexPath.row )
            {
                case 0:{
                    titleLabel.text = @"Rate Ventoura";
                    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    break;
                }
                case 1:{
                    titleLabel.text = @"Terms and Privacy";
                    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    break;
                }
                case 2:{
                    titleLabel.text = @"Release Notes";
                    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    break;
                }
            }

    
    
    titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
    titleLabel.textColor = [ventouraUtility ventouraSettingTextColour];
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 3;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    switch ( indexPath.row )
    {
        case 0:{
            break;
        }
        case 1:
        {
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
                dispatch_sync(dispatch_get_main_queue(), ^{
                    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                         bundle:nil];
                    webContentViewController *viewController =
                    [storyboard instantiateViewControllerWithIdentifier:@"webContentViewController"];
                    viewController.target = @"http://ventoura.com/";
                    [[self navigationController] pushViewController:viewController animated:YES ];
                    
                });
            });
            break;
        }
        case 2:
            break;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}


@end
