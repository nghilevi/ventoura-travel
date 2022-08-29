//
//  MessageViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 1/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "MessageViewController.h"

NSString *myCurrentID;
NSIndexPath *lastIndexPath;
SWTableViewCell *lastSWCell;
ventouraClassAppDelegate *ventouraDelegate;

@interface MessageViewController ()<BuddyListManagerDelegate>{
    
    NSMutableArray *_buddyList;
    BuddyListManager *_vmanager;
}
@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) UIImageView *diamondImageView;


@end

@implementation MessageViewController

@synthesize tView;
//what is syntheize

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Accessors
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (ventouraClassAppDelegate *)appDelegate {
	return (ventouraClassAppDelegate *)[[UIApplication sharedApplication] delegate];
}

- (XMPPStream *)xmppStream {
    return [[self appDelegate] xmppStream];
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    
   //    ventouraClassAppDelegate *del = [self appDelegate];
//    del._chatDelegate = self;
   
    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    
    [self.revealButtonItem setTarget: self.revealViewController];
    [self.revealButtonItem setAction: @selector( revealToggle: )];
    [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
//    BOOL isRunning = [[NSUserDefaults standardUserDefaults] boolForKey:@"is_running"];
    self.tView.delegate = self;
    self.tView.dataSource = self;
    onlineBuddies = [[NSMutableArray alloc ] init];
    
  
    
    NSLog(@"start loading json");
    _vmanager = [[BuddyListManager alloc] init];
    _vmanager.communicator =[[BuddyListCommunicator alloc] init];
    _vmanager.communicator.delegate = _vmanager;
    _vmanager.delegate = self;
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT
                                      );
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }
    
    
    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    self.tView.backgroundColor = [UIColor clearColor];
    self.view.backgroundColor = [UIColor clearColor];
    
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    
    
    myCurrentID = [ventouraUtility returnMyUserIdWithType];
    [_vmanager fetchBuddyList];
    //load from database
    NSArray* buddyListResult= [ventouraDatabaseUtility buddyListFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType]];
    if(buddyListResult.count>0){
        NSLog(@"Match found");
       _buddyList= [self buildBuddyListFromResult:buddyListResult];
        [self updateBuddyListBeforeReload];
        [self.tView reloadData];
    }
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark View lifecycle
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
    
    ventouraDelegate = [self appDelegate];
    ventouraDelegate._messageDelegate = self;
    
    [self updateBuddyListBeforeReload];
    [self.tView reloadData];
}

- (void)viewWillDisappear:(BOOL)animated
{

	
	[super viewWillDisappear:animated];
    ventouraDelegate._messageDelegate = nil;
    ventouraDelegate = nil;
    

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark NSFetchedResultsController
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    //right now just use the user name on the bloody table row for testing/
    //will need an object to store all buddy data, have a corrosponding datamodel
    //so index number can fetch the user model.
    // start a chat
    Person *person = _buddyList[indexPath.row];

    NSLog(@"Start A new chat for %@", person.firstName);
    NSLog(@"Start A new chat for %@", person.userRole);

    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    JChatViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"JChatViewController"];
    viewController.person = person;
    [[self navigationController] pushViewController:viewController animated:YES ];
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark UITableViewCell helpers
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark UITableView
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return 1;
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)sectionIndex
{


	return _buddyList.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
	
    return 90;
	
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *cellIdentifier = @"personCell";
    MessageTableViewCell *cell = (MessageTableViewCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier
                                                                                           forIndexPath:indexPath];

    
    cell.rightUtilityButtons = [self rightButtons];
    cell.delegate = self;
    Person *person = _buddyList[indexPath.row];
    UIImageView *personImage = (UIImageView *)[cell viewWithTag:20];
    personImage.image = person.image;
    personImage.layer.cornerRadius = personImage.frame.size.width / 2;
    personImage.clipsToBounds = YES;
    personImage.layer.borderWidth = 0.5f;
    personImage.layer.borderColor = [UIColor lightGrayColor].CGColor;
    
    
    UILabel *personNameLabel = (UILabel *)[cell viewWithTag:21];
    personNameLabel.text = [NSString stringWithFormat:@"%@", person.firstName];
    personNameLabel.textColor = [ventouraUtility ventouraTitleColour];
    personNameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
    
    
    UIView *lineSeparator = (UIView *)[cell viewWithTag:22];
    lineSeparator.backgroundColor = [UIColor lightGrayColor];
    
    

    
    
    UILabel *personTimeStampLabel = (UILabel *)[cell viewWithTag:23];
    personTimeStampLabel.text = [NSString stringWithFormat:@"%@", person.lastMessageTime];
    personTimeStampLabel.textAlignment = NSTextAlignmentRight;
    
    
    
    //each cell then selects the last messages and add it to the label :D
    if(person.lastMessage == nil){
        personNameLabel.textColor = [ventouraUtility ventouraPink];
        personTimeStampLabel.textColor = [ventouraUtility ventouraPink];
        person.lastMessage = @"";
    }
    
    UILabel *personMessageLabel = (UILabel *)[cell viewWithTag:26];
    personMessageLabel.text = [NSString stringWithFormat:@"%@", person.lastMessage];
    personMessageLabel.numberOfLines = 0;
    [personMessageLabel sizeToFit];
    personMessageLabel.frame = CGRectMake(personMessageLabel.frame.origin.x, personMessageLabel.frame.origin.y
                                          , 190, 40);
    NSString *receiverText;
    NSString *receiver;
    if ([person.userRole isEqualToString:@"GUIDE"]){
        receiverText = @"Guide";
        receiver = [ventouraUtility returnUserIdWithType:[ventouraUtility isUserGuide:person.userRole] ventouraId:person.ventouraId];
    }
    else{
        receiverText = @"Traveller";
    }
    
    personMessageLabel.textColor = [ventouraUtility ventouraTitleColour];
    if([personMessageLabel.text  isEqualToString:@""]){
        personMessageLabel.text = receiverText;
        personMessageLabel.textColor = [UIColor lightGrayColor];
        personMessageLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:17];
    }else{
        personMessageLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:14];
    }
    
    
//    personMessageLabel.layer.borderColor = [UIColor darkGrayColor].CGColor;
//    personMessageLabel.layer.borderWidth = 1.0;
    
    
    M13BadgeView *badgeSuperView = (M13BadgeView *)[cell viewWithTag:999];
    UIView *badgePlaceHolder = (UIView *)[cell viewWithTag:998];
    badgePlaceHolder.backgroundColor = [UIColor clearColor];
    [badgeSuperView setHidden:YES];
    
    
    badgeSuperView.font = [UIFont systemFontOfSize:11.0];
    badgeSuperView.badgeBackgroundColor = [ventouraUtility ventouraPink];
    
   
    if(person.unreadCount>0){
        badgeSuperView.text = [NSString stringWithFormat:@"%d",person.unreadCount];
        badgeSuperView.font = [UIFont systemFontOfSize:11.0];
        badgeSuperView.badgeBackgroundColor = [ventouraUtility ventouraPink];
        badgeSuperView.frame = CGRectMake(badgePlaceHolder.frame.origin.x, badgePlaceHolder.frame.origin.y, 18, 18);
        [badgeSuperView setHidden:NO];
        
    }
//    }
    
    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    cell.backgroundColor = [UIColor clearColor];
    
    return cell;
}



- (void)viewDidAppear:(BOOL)animated {
    
    [super viewDidAppear:animated];
    
// NSString *login = [[NSUserDefaults standardUserDefaults] objectForKey:@"userID"];
//maybe all the other view needs this as well,
// handles msg just by adding to the database. :)

//    if (1) {
        if ([[self appDelegate] connect]) {
            NSLog(@"appDelegate is connected");
        }
        
//    }
}


#pragma mark Delegate For PackageFetch
-(void) didReceiveBuddyList:(NSArray *)ventouraPackage{
//    NSLog(@"done, should load the array into people now ;) or it should be passed into there");
//    NSLog(@"count%lu",(unsigned long)_buddyList.count);
    _buddyList = [ventouraPackage mutableCopy];
    [self saveBuddyListToDatabase];
    
    [self updateBuddyListBeforeReload];
    
    //update the table. get latest message, get read count, reorder.
    
    
    
    
    //fetch the images here.
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            dispatch_sync(dispatch_get_main_queue(), ^{
                [self.tView reloadData];
            });
        });
    
    NSLog(@"Package Delegate done");
    
}

-(void) fetchingBuddyListFailedWithError:(NSError *)error{
    NSLog(@"Error %@; %@", error, [error localizedDescription]);
}


- (void)newMessageReceived:(NSDictionary *)messageContent from:(NSString *)sender{
    NSLog(@"New Msg Received!");
    
//    NSString *probeQuery =[NSString stringWithFormat:@"select unreadCount from Match where ownerId like '%@' AND matchUserId like '%@'", [ventouraUtility returnMyUserId], sender];
//    NSArray *badges = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:probeQuery]];
//    
//    if (badges.count > 0) {
//        NSLog(@"unread msg count %@", badges[0][0]);
        
//    }else{
    
    
//    }
    [self updateBuddyListBeforeReload];

    [self.tView reloadData];
    
}


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
    switch (index) {
        case 0:
        {
//            NSLog(@"More button was pressed");
//            UIAlertView *alertTest = [[UIAlertView alloc] initWithTitle:@"Hello" message:@"More more more" delegate:nil cancelButtonTitle:@"cancel" otherButtonTitles: nil];
//            [alertTest show];
//            
            [cell hideUtilityButtonsAnimated:YES];
            [self showMoreActionSheet];

            break;
        }
        case 1:
        {
            
            [cell hideUtilityButtonsAnimated:YES];
            [self showDeleteActionSheet];
            
            // Delete button was pressed
//            NSIndexPath *cellIndexPath = [self.tView indexPathForCell:cell];
          
            break;
        }
        default:
            break;
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


- (NSArray *)rightButtons
{
    NSMutableArray *rightUtilityButtons = [NSMutableArray new];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:0.78f green:0.78f blue:0.8f alpha:1.0]
                                                title:@"More"];
    [rightUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor colorWithRed:1.0f green:0.231f blue:0.188 alpha:1.0f]
                                                title:@"Delete"];
    
    return rightUtilityButtons;
}

-(void)didReceiveDeleteBuddyUserId:(NSString *)userId isUserGuide:(BOOL)isUserGuide{
    //should pass the Id back, so once its successful, delete all local data
    
    //delete from Match.
    //delete from Profiles.
    //delete from Images.
    //delete the chat History.
    
    NSLog(@"Buddy Deleted");
}

-(void)newBuddyOnline:(NSString *)buddyName{

}
-(void)buddyWentOffline:(NSString *)buddyName{

}

-(NSMutableDictionary*) buildMessageObjectFromResult:(NSArray*) results{
    NSMutableDictionary * messageObject = [[NSMutableDictionary alloc]init];
    
    for (id object in results) {
        [messageObject setObject:object[4] forKey:@"time"];
        [messageObject setObject:object[1] forKey:@"sender"];
        [messageObject setObject:object[5] forKey:@"msg"];
        NSLog(@"Time : %@ , Msg Content %@", messageObject[@"time"],messageObject[@"msg"]);
    }
    return messageObject;
}

-(NSMutableArray*) buildBuddyListFromResult:(NSArray*)results {
    NSMutableArray * tmpbuddyList = [[NSMutableArray alloc ] init];
    
    //matchUserId, matchUserName, unreadCount, isNewMatch, matchDate from Match
    for (int i = 0; i < results.count; i++) {
        Person* tmp = [ProfileBuilder matchProfileFromDatabase:results[i]];
        [tmpbuddyList addObject:tmp];
    }
    
    return tmpbuddyList;
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

-(void)showMoreActionSheet{
    
    Person * person = _buddyList[lastIndexPath.row];
    
    NSArray *options;
    if([ventouraUtility isUserGuide:person.userRole])
    {
        options = [NSArray arrayWithObjects:@"View Profile", @"Make Booking",@"Delete Chat History", nil];
    }else{
        options = [NSArray arrayWithObjects:@"View Profile",@"Delete Chat History", nil];
        
    }
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                             delegate:self
                                                    cancelButtonTitle:nil
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:nil];
    for( NSString *option in options)  {
        [actionSheet addButtonWithTitle:option];
    }
    [actionSheet addButtonWithTitle:@"Cancel"];
    actionSheet.cancelButtonIndex = [options count];
    actionSheet.tag = 200;
    [actionSheet showInView:self.view];

}



-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (actionSheet.tag == 100) {
        //        NSLog(@"Index = %d - Title = %@", buttonIndex, [actionSheet buttonTitleAtIndex:buttonIndex]);
        
        if (buttonIndex ==0) {
            Person * personToDelete = _buddyList[lastIndexPath.row];
            [_vmanager postDeleteBuddyById:personToDelete.ventouraId isUserGuide:[ventouraUtility isUserGuide:personToDelete.userRole]];
            [_buddyList removeObjectAtIndex:lastIndexPath.row];
            [self.tView deleteRowsAtIndexPaths:@[lastIndexPath] withRowAnimation:UITableViewRowAnimationLeft];
            
            lastIndexPath = nil;

        }else{
            [lastSWCell hideUtilityButtonsAnimated:YES];
            lastSWCell = nil;
            lastIndexPath = nil;
        }
    }
    if (actionSheet.tag == 200) {
        if (buttonIndex ==0) {
            NSLog(@"Log out");
        }
    }
    if (actionSheet.tag == 300) {
        if (buttonIndex ==0) {
            NSLog(@"Disable");
        }
    }
}

-(void) saveBuddyListToDatabase{
    for (int i = 0; i<_buddyList.count; i++) {
        //save buddy list
        Person * tmpPerson = _buddyList[i];
        NSLog(@"userrole : %@", tmpPerson.userRole);
        NSLog(@"userrole withID: %@", [ventouraUtility returnUserIdWithType:[ventouraUtility isUserGuide:tmpPerson.userRole] ventouraId:tmpPerson.ventouraId]);
        bool flag  = [ventouraUtility isUserGuide:tmpPerson.userRole];
        NSLog(flag ? @"Yes" : @"No");
        
        [ventouraDatabaseUtility saveMatchListUser:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:[ventouraUtility returnUserIdWithType:[ventouraUtility isUserGuide:tmpPerson.userRole] ventouraId:tmpPerson.ventouraId] userName:tmpPerson.firstName matchTime:tmpPerson.matchTime];
    }
}


-(void)updateBuddyListBeforeReload{
    
    for (int i = 0; i < _buddyList.count; i++) {
        Person * person = _buddyList[i];
        NSString *receiver;
        
        if ([person.userRole isEqualToString:@"GUIDE"]){
//            receiverText = @"Guide";
            receiver = [NSString stringWithFormat:@"g_%@", person.ventouraId];
        }
        else{
//            receiverText = @"Traveller";
            receiver = [NSString stringWithFormat:@"t_%@", person.ventouraId];
        }
        
        NSArray *lastMessageResult = [ventouraDatabaseUtility lastMessageFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:receiver];
        
        NSMutableDictionary * messageObject =  [self buildMessageObjectFromResult:lastMessageResult];
        
        
//        NSLog(@"msgTime %@", messageObject[@"time"]);
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
        [df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
        
        NSDate * sortDate;
        if (messageObject[@"time"] !=nil) {
            sortDate= [df dateFromString:messageObject[@"time"]];
        }else{
            sortDate= [df dateFromString:person.matchTime];
        }
        person.dateForSorting = sortDate;
        NSDate *myDate = [df dateFromString: messageObject[@"time"]];
        
        
        JSQMessagesTimestampFormatter *JSQTimeFormatter = [[JSQMessagesTimestampFormatter alloc]init];
        
        NSString *dateText  = [NSString stringWithFormat:@"%@ ",[JSQTimeFormatter timeForDate:myDate]];
        //datetime is last message time
        if(messageObject[@"time"] == nil){
            dateText = @"New";
        }
        person.lastMessageTime = dateText;
        if (messageObject[@"msg"] != nil) {
             person.lastMessage = messageObject[@"msg"];
        }
        
        NSArray* unreadResult=[ventouraDatabaseUtility unreadCountFromDatabase:self.dbManager ownerId:[ventouraUtility returnMyUserIdWithType] userId:receiver];
        
        //if no badge counts it, the user does not exist.
        if (unreadResult.count > 0) {
            NSLog(@"unread msg count %@", unreadResult[0][0]);
            person.unreadCount = [unreadResult[0][0] intValue];
            
        }
        
    }

    NSSortDescriptor *sortDescriptor;
    sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"dateForSorting"
                                                 ascending:NO];
    NSArray *sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    NSArray *sortedArray;
    sortedArray = [_buddyList sortedArrayUsingDescriptors:sortDescriptors];
//    return sortedArray;//
    _buddyList = [sortedArray mutableCopy];

    
}

@end
