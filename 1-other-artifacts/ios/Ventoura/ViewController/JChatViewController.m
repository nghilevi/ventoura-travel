//
//  JChatViewController.m
//
//
//  Created by Wenchao Chen on 25/07/2014.
//
//

#import "JChatViewController.h"
#import "NSString+Utils.h"

NSString *receiver;
NSString *myCurrentID;
ventouraClassAppDelegate *ventouraDelegate;

static NSString *  chatPerson = @"";

BOOL emoticonShow;
@interface JChatViewController () <ProfileManagerDelegate>
@property (nonatomic, strong) ProfileManager *pmanager;
@property (nonatomic, strong) DBManager *dbManager;
@property (nonatomic, strong) NSArray *arrMsgHistory;
@property (nonatomic, strong) NSMutableArray *emoticons;
@property (nonatomic, strong) NSArray *emoticonNames;
@property (nonatomic, strong) NSMutableString *emoticonPattern;
@property (nonatomic, assign) NSInteger totalMsgCount;
@property (nonatomic, assign) NSInteger loadedMsgCount;
@property (nonatomic, assign) NSInteger msgOffSet;
@property (nonatomic, assign) NSInteger msgPerLoad;


@end

@implementation JChatViewController

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
        self.inputToolbar.contentView.leftBarButtonItem = nil;
    self.msgPerLoad = 15;

    self.msgOffSet = self.msgPerLoad;

    self.loadedMsgCount=0;
    self.totalMsgCount= 0;
    if ([self.person.userRole isEqualToString:@"GUIDE"]) {
        //NSLog(@"g_%@",person.ventouraId);
        receiver = [NSString stringWithFormat:@"g_%@",self.person.ventouraId];
    }else{
        //NSLog(@"t_%@",person.ventouraId);
        receiver = [NSString stringWithFormat:@"t_%@",self.person.ventouraId];
    }
    
    myCurrentID = [[NSUserDefaults standardUserDefaults] stringForKey:@"userVentouraId"];
    BOOL currentUserIsGuide =[[NSUserDefaults standardUserDefaults] boolForKey:@"userisGuide"];
    if (currentUserIsGuide) {
        myCurrentID = [NSString stringWithFormat:@"g_%@",myCurrentID];
    }else{
        myCurrentID = [NSString stringWithFormat:@"t_%@",myCurrentID];
    }
    
    _pmanager = [[ProfileManager alloc] init];
    _pmanager.communicator = [[ProfileCommunicator alloc] init];
    _pmanager.communicator.delegate = _pmanager;
    _pmanager.delegate = self;
    
//     [_pmanager fetchUserProfile:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId];
    
    self.sender = @"Me";
  
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    
    [self setupChat];
    [self loadData];
    [self setupEmoticon];
    
    self.outgoingBubbleImageView = [JSQMessagesBubbleImageFactory
                                    outgoingMessageBubbleImageViewWithColor:[UIColor jsq_messageBubbleLightGrayColor]];
    
    self.incomingBubbleImageView = [JSQMessagesBubbleImageFactory
                                    incomingMessageBubbleImageViewWithColor:[UIColor jsq_messageBubblePinkColor]];
    //jsq_messageBubblePinkColor
    emoticonShow = YES;
    
    NSString *query = [NSString stringWithFormat:@"UPDATE Match SET unreadCount= 0 WHERE ownerId like '%@' AND matchUserId like '%@'", [ventouraUtility returnMyUserIdWithType], receiver];
    
    // Execute the query.
    [self.dbManager executeQuery:query];
    
    if (self.dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", self.dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }

    
}

#pragma mark - Demo setup
- (void)setupEmoticon{
    
    self.emoticonNames = @[@"emoji_annoyed",
                           @"emoji_awkward",
                           @"emoji_cool",
                           @"emoji_cry",
                           @"emoji_disappointed",
                           @"emoji_evil",
                           @"emoji_frown",
                           @"emoji_happy",
                           @"emoji_heart",
                           @"emoji_kind",
                           @"emoji_meh",
                           @"emoji_nospeak",
                           @"emoji_poketongue",
                           @"emoji_sad",
                           @"emoji_scared",
                           @"emoji_sex",
                           @"emoji_sick",
                           @"emoji_smile",
                           @"emoji_starface",
                           @"emoji_suprised",
                           @"emoji_tired",
                           @"emoji_tongoutwink",
                           @"emoji_wink",
                           @"emoji_yawn"];
    
    
    
    for(int i=0; i<self.emoticonNames.count;i++){
        Emoticon *emo = [[Emoticon alloc]initWithEmoticonId:[NSString stringWithFormat:@"%d", i] emoticonName:self.emoticonNames[i] emoticonImage:[UIImage imageNamed:[NSString stringWithFormat:@"%@.png", self.emoticonNames[i]]]];
        [self.emoticons addObject:emo];
    }
    
    
    self.emoticonPattern = [NSMutableString stringWithString:@"([A-Za-z]?)("];
    NSString *pipe = @"";
    for (NSString *badWord in self.emoticonNames)
    {
        
        NSString * matchEmoticonName = [NSString stringWithFormat:@"\\[%@\\]",badWord];
        [self.emoticonPattern appendString:pipe];
        [self.emoticonPattern appendString:matchEmoticonName];
        pipe = @"|";
    }
    
    [self.emoticonPattern appendString:@")([A-Za-z]?)"];
    
    
}

- (void)setupChat
{
    
    
    
    self.collectionView.collectionViewLayout.outgoingAvatarViewSize = CGSizeZero;
    self.collectionView.collectionViewLayout.incomingAvatarViewSize = CGSizeMake(40,40);
    chatPerson = [NSString stringWithFormat:@"%@",self.person.firstName];
    self.messages = [[NSMutableArray alloc] init];
    
    CGFloat incomingDiameter = self.collectionView.collectionViewLayout.incomingAvatarViewSize.width;
    UIImage *jobsImage = [JSQMessagesAvatarFactory avatarWithImage:self.person.image
                                                          diameter:incomingDiameter];
    
    
    self.avatars = @{
                     chatPerson : jobsImage,
                     };
    
    UIBarButtonItem *moreButton = [[UIBarButtonItem alloc] initWithTitle:@"More" style:UIBarButtonItemStylePlain target:self action:@selector(morePressed:)];
    
    
    self.navigationItem.rightBarButtonItem = moreButton;
}



- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
//    NSLog(@"send msg to %@",receiver);
    self.navigationItem.title = [NSString stringWithFormat:@"%@", self.person.firstName];
    
  
    
    NSString *query = [NSString stringWithFormat:@"UPDATE Match SET unreadCount= 0 WHERE ownerId like '%@' AND matchUserId like '%@'", [ventouraUtility returnMyUserIdWithType], receiver];
    
    // Execute the query.
    [self.dbManager executeQuery:query];
    
    if (self.dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", self.dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }
    ventouraDelegate = [self appDelegate];
    ventouraDelegate._messageDelegate = self;
    
}


#pragma mark - JSQMessagesViewController method overrides

- (void)didPressSendButton:(UIButton *)button
           withMessageText:(NSString *)text
                    sender:(NSString *)sender
                      date:(NSDate *)date
{
    
    [JSQSystemSoundPlayer jsq_playMessageSentSound];
    
    JSQMessage *message = [[JSQMessage alloc] initWithText:text sender:sender date:date];
    
    if([text length] > 0) {
        
        NSXMLElement *body = [NSXMLElement elementWithName:@"body"];
        [body setStringValue:text];
        NSXMLElement *message = [NSXMLElement elementWithName:@"message"];
        [message addAttributeWithName:@"type" stringValue:@"chat"];
        NSString *toUser = [NSString stringWithFormat:@"%@@ip-172-31-23-78",receiver];
        [message addAttributeWithName:@"to" stringValue:toUser];
        [message addChild:body];
        [self.xmppStream sendElement:message];
        NSLog(@"msg sent");
        
        NSMutableDictionary *m = [[NSMutableDictionary alloc] init];
        [m setObject:text forKey:@"msg"];
        [m setObject:@"you" forKey:@"sender"];
        [m setObject:[NSString getCurrentTime] forKey:@"time"];
        
        //add this to database.
        
        NSString *query = [NSString stringWithFormat:@"insert into ChatMessage values(null, '%@', '%@', '%@', current_timestamp, '%@')", myCurrentID, receiver, myCurrentID, [ventouraUtility escapeStringSQL:text]];
        
        // Execute the query.
        [self.dbManager executeQuery:query];
        
        // If the query was successfully executed then pop the view controller.
        if (self.dbManager.affectedRows != 0) {
            NSLog(@"Query was executed successfully. Affected rows = %d", self.dbManager.affectedRows);
        }
        else{
            NSLog(@"Could not execute the query.");
        }
        
    }
    [self.messages addObject:message];
    
    [self finishSendingMessage];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    /**
     *  Enable/disable springy bubbles, default is NO.
     *  You must set this from `viewDidAppear:`
     *  Note: this feature is mostly stable, but still experimental
     */
    self.collectionView.collectionViewLayout.springinessEnabled = NO;
}
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    NSLog(@"View controller was popped");
    ventouraDelegate._messageDelegate = nil;
    ventouraDelegate = nil;
    
    NSString *query = [NSString stringWithFormat:@"UPDATE Match SET unreadCount= 0 WHERE ownerId like '%@' AND matchUserId like '%@'", [ventouraUtility returnMyUserIdWithType], receiver];
    
    // Execute the query.
    [self.dbManager executeQuery:query];
    
    if (self.dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", self.dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }
    
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

#pragma mark - JSQMessages CollectionView DataSource

- (id<JSQMessageData>)collectionView:(JSQMessagesCollectionView *)collectionView messageDataForItemAtIndexPath:(NSIndexPath *)indexPath
{
    JSQMessage *tmp = [[self.messages objectAtIndex:indexPath.item] copy];
    
//    for (int i = 0; i<self.emoticonNames.count; i++) {
//        tmp.text = [tmp.text stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"[%@]", self.emoticonNames[i]]
//                                                       withString:emojiTextPadding];
//    }

    
    
    return [self.messages objectAtIndex:indexPath.item];
    
    return tmp;
}

- (UIImageView *)collectionView:(JSQMessagesCollectionView *)collectionView bubbleImageViewForItemAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  You may return nil here if you do not want bubbles.
     *  In this case, you should set the background color of your collection view cell's textView.
     */
    
    /**
     *  Reuse created bubble images, but create new imageView to add to each cell
     *  Otherwise, each cell would be referencing the same imageView and bubbles would disappear from cells
     */
    
    JSQMessage *message = [self.messages objectAtIndex:indexPath.item];
    
    if ([message.sender isEqualToString:self.sender]) {
        return [[UIImageView alloc] initWithImage:self.outgoingBubbleImageView.image
                                 highlightedImage:self.outgoingBubbleImageView.highlightedImage];
    }
    
    return [[UIImageView alloc] initWithImage:self.incomingBubbleImageView.image
                             highlightedImage:self.incomingBubbleImageView.highlightedImage];
}

- (UIImageView *)collectionView:(JSQMessagesCollectionView *)collectionView avatarImageViewForItemAtIndexPath:(NSIndexPath *)indexPath
{
    JSQMessage *message = [self.messages objectAtIndex:indexPath.item];
    
    UIImage *avatarImage = [self.avatars objectForKey:message.sender];
    return [[UIImageView alloc] initWithImage:avatarImage];
}

- (NSAttributedString *)collectionView:(JSQMessagesCollectionView *)collectionView attributedTextForCellTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  This logic should be consistent with what you return from `heightForCellTopLabelAtIndexPath:`
     *  The other label text delegate methods should follow a similar pattern.
     *
     *  Show a timestamp for every 3rd message
     */
    if (indexPath.item== 0) {
        JSQMessage *message = [self.messages objectAtIndex:indexPath.item];
        
        return [[JSQMessagesTimestampFormatter sharedFormatter] attributedTimestampForDate:message.date];
    }
    if (indexPath.item > 0) {
        JSQMessage *message = [self.messages objectAtIndex:indexPath.item];
        JSQMessage *message2 = [self.messages objectAtIndex:indexPath.item-1];
        NSTimeInterval distanceBetweenDates = [message.date timeIntervalSinceDate:message2.date];
        NSInteger minsBetweenDates = distanceBetweenDates / 60;
        if(minsBetweenDates >15){
            return [[JSQMessagesTimestampFormatter sharedFormatter] attributedTimestampForDate:message.date];
        }
    }
    
    return nil;
}

- (NSAttributedString *)collectionView:(JSQMessagesCollectionView *)collectionView attributedTextForMessageBubbleTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    JSQMessage *message = [self.messages objectAtIndex:indexPath.item];
    
    /**
     *  iOS7-style sender name labels
     */
    if ([message.sender isEqualToString:self.sender]) {
        return nil;
    }
    
    if (indexPath.item - 1 > 0) {
        JSQMessage *previousMessage = [self.messages objectAtIndex:indexPath.item - 1];
        if ([[previousMessage sender] isEqualToString:message.sender]) {
            return nil;
        }
    }
    
    /**
     *  Don't specify attributes to use the defaults.
     */
    //    return [[NSAttributedString alloc] initWithString:message.sender];
    return nil;
}

- (NSAttributedString *)collectionView:(JSQMessagesCollectionView *)collectionView attributedTextForCellBottomLabelAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

#pragma mark - UICollectionView DataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [self.messages count];
}

- (UICollectionViewCell *)collectionView:(JSQMessagesCollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  Override point for customizing cells
     */
    JSQMessagesCollectionViewCell *cell = (JSQMessagesCollectionViewCell *)[super collectionView:collectionView cellForItemAtIndexPath:indexPath];
    
    /**
     *  Configure almost *anything* on the cell
     *
     *  Text colors, label text, label colors, etc.
     *
     *
     *  DO NOT set `cell.textView.font` !
     *  Instead, you need to set `self.collectionView.collectionViewLayout.messageBubbleFont` to the font you want in `viewDidLoad`
     *
     *
     *  DO NOT manipulate cell layout information!
     *  Instead, override the properties you want on `self.collectionView.collectionViewLayout` from `viewDidLoad`
     */
    //    NSLog(@"msg: %@", cell.textView.text);
    
    CGSize constraintSize = CGSizeMake(220.0f, FLT_MAX);
    
    JSQMessage *msg = [[self.messages objectAtIndex:indexPath.item]copy];
    //    msg.text = [msg.text stringByReplacingOccurrencesOfString:@"[emoji]"
    //                                                   withString:emojiTextPadding];
    if ([msg.sender isEqualToString:self.sender]) {
        cell.textView.textColor = [UIColor blackColor];
        constraintSize = CGSizeMake(263.0f, FLT_MAX);
        
    }
    else {
        cell.textView.textColor = [UIColor whiteColor];
    }
    
//    CGFloat emojiSize = 22;
    
    for (UIView *subview in cell.textView.subviews) {
        if ([subview isKindOfClass:[UIImageView class]] || [subview isKindOfClass:[UITextView class]]) {
            [subview removeFromSuperview];
        }
    }
    cell.textView.linkTextAttributes = @{ NSForegroundColorAttributeName : cell.textView.textColor,
                                          NSUnderlineStyleAttributeName : @(NSUnderlineStyleSingle | NSUnderlinePatternSolid) };
    
    
    cell.textView.text = msg.text;
    UITextView * tmpText = [[UITextView alloc]init];
    
    CGSize avatarSize = CGSizeMake(40, 40);
    if ([msg.sender isEqualToString:self.sender]) {
        avatarSize = CGSizeZero ;
    }
    
    
    //  from the cell xibs, there is a 2 point space between avatar and bubble
    CGFloat spacingBetweenAvatarAndBubble = 2.0f;
    CGFloat horizontalContainerInsets = self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.left + self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.right;
    CGFloat horizontalFrameInsets = self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.left + self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.right;
    
    CGFloat horizontalInsetsTotal = horizontalContainerInsets + horizontalFrameInsets + spacingBetweenAvatarAndBubble;
    CGFloat maximumTextWidth = self.collectionView.collectionViewLayout.itemWidth - avatarSize.width - self.collectionView.collectionViewLayout.messageBubbleLeftRightMargin - horizontalInsetsTotal;
    
    CGRect stringRect = [msg.text boundingRectWithSize:CGSizeMake(maximumTextWidth, CGFLOAT_MAX)
                                                         options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading)
                                                      attributes:@{ NSFontAttributeName : cell.textView.font }
                                                         context:nil];
    
    CGSize stringSize = CGRectIntegral(stringRect).size;
    
    CGFloat verticalContainerInsets = self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.top + self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.bottom;
    CGFloat verticalFrameInsets = self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.top + self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.bottom;
    
    //  add extra 2 points of space, because `boundingRectWithSize:` is slightly off
    //  not sure why. magix. (shrug) if you know, submit a PR
    CGFloat verticalInsets = verticalContainerInsets + verticalFrameInsets + 2.0f;
    
    CGFloat finalWidth = MAX(stringSize.width + horizontalInsetsTotal, [UIImage imageNamed:@"bubble_min"].size.width);
    
    CGSize finalSize = CGSizeMake(finalWidth, stringSize.height + verticalInsets);
    
    
    
    
//    CGFloat maximumTextWidth = self.collectionView.collectionViewLayout.itemWidth - avatarSize.width - self.collectionView.collectionViewLayout.messageBubbleLeftRightMargin;
//    UIEdgeInsets insets =  self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets;
//    
//    CGFloat textInsetsTotal = insets.left + insets.right + insets.bottom + insets.top;
//    CGRect stringRect = [msg.text boundingRectWithSize:CGSizeMake(maximumTextWidth - textInsetsTotal, CGFLOAT_MAX)
//                                               options:(NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading)
//                                            attributes:@{ NSFontAttributeName : cell.textView.font }
//                                               context:nil];
//    
//    
//    CGSize stringSize = CGRectIntegral(stringRect).size;
//    
//    CGFloat verticalInsets = self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.top + self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.bottom;
//    
//    CGSize finalSize = CGSizeMake(stringSize.width+28, stringSize.height + verticalInsets);
    
//    NSLog(@"final size width %f. hright %f",finalSize.width, finalSize.height);
    
    
    tmpText.frame = CGRectMake(0,0, finalSize.width, finalSize.height);
    tmpText.backgroundColor = [UIColor clearColor];
    [tmpText setAttributedText:cell.textView.attributedText];
    tmpText.text = cell.textView.text;
    tmpText.font = cell.textView.font;
    tmpText.textContainerInset =  UIEdgeInsetsMake(self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.top, self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.bottom+2, self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.left, self.collectionView.collectionViewLayout.messageBubbleTextViewTextContainerInsets.right+2);
    
//    tmpText.textContainerInset =  UIEdgeInsetsMake(10,0,0,0);
    
//    tmpText.contentInset = UIEdgeInsetsMake(self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.top, self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.bottom, self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.left, self.collectionView.collectionViewLayout.messageBubbleTextViewFrameInsets.right);

    
//    NSRange replaceRange = [tmpText.text rangeOfString:@"[emoji_cool]"];
//    
//    if (replaceRange.location != NSNotFound){
//        NSLog(@"FOUND at %d", replaceRange.location);
//        //returning infinity
//        UITextPosition *Pos2 = [tmpText positionFromPosition: tmpText.beginningOfDocument offset: replaceRange.location+1];
//        UITextPosition *Pos1 = [tmpText positionFromPosition: tmpText.beginningOfDocument offset: replaceRange.location];
//        UITextRange *range = [tmpText textRangeFromPosition:Pos1 toPosition:Pos2];
//        CGRect result1 = [tmpText firstRectForRange:(UITextRange *)range ];
//        NSLog( @" x: %f, y: %f for msg %@", result1.origin.x, result1.origin.y,msg.text);
//        //
//        CGRect aRect = CGRectMake( result1.origin.x,  result1.origin.y, emojiSize, emojiSize);
//        //
//        UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"emoji_cool.png"]];
//        [imageView setFrame:aRect];
//        [cell.textView addSubview:imageView];
//        
//
//        
//    }
    
    
//    NSArray *betterwords = @[@"*** "];
//    
//    NSMutableString *finalString = [cell.textView.text mutableCopy];
//    
//    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:self.emoticonPattern options:0 error:nil];
//    
//    [regex enumerateMatchesInString:cell.textView.text
//                            options:0
//                              range:NSMakeRange(0, cell.textView.text.length)
//                         usingBlock:^(NSTextCheckingResult *result, NSMatchingFlags flags, BOOL *stop) {
//                             
//                             NSRange badrange = [result rangeAtIndex:2];
//                             NSString *badword = [cell.textView.text substringWithRange:badrange];
//                             NSString *betterword = betterwords[0];
//                             NSInteger offset = finalString.length - cell.textView.text.length;
//                             badrange.location += offset;
//                             [finalString replaceCharactersInRange:badrange withString:betterword];
//                             tmpText.text = finalString;
//
//                             if (badrange.location != NSNotFound){
////                                 NSLog(@"FOUND %@ at %d", badword ,badrange.location);
//                                 //returning infinity
//                                 UITextPosition *Pos2 = [tmpText positionFromPosition: tmpText.beginningOfDocument offset: badrange.location+1];
//                                 UITextPosition *Pos1 = [tmpText positionFromPosition: tmpText.beginningOfDocument offset: badrange.location];
//                                 UITextRange *range = [tmpText textRangeFromPosition:Pos1 toPosition:Pos2];
//                                 CGRect result1 = [tmpText firstRectForRange:(UITextRange *)range ];
////                                 NSLog( @" x: %f, y: %f for msg %@", result1.origin.x, result1.origin.y,tmpText.text);
//                                 //
//                                 CGRect aRect = CGRectMake( result1.origin.x,  result1.origin.y, emojiSize, emojiSize);
//                                 //
//                                 badword = [badword stringByReplacingOccurrencesOfString:@"[" withString:@""];
//                                 badword = [badword stringByReplacingOccurrencesOfString:@"]" withString:@""];
//                                 
//                                 UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:[NSString stringWithFormat:@"%@.png",badword]]];
//                                 [imageView setFrame:aRect];
//                                 [cell.textView addSubview:imageView];
//                                 
//                             }
//                             
//                         }];

    
    
//    tmpText.text = cell.textView.text;
//
//    UITextPosition *Pos2 = [tmpText positionFromPosition: tmpText.endOfDocument offset: -3];
//    UITextPosition *Pos1 = [tmpText positionFromPosition: tmpText.endOfDocument offset: 0];
//    UITextRange *range = [tmpText textRangeFromPosition:Pos1 toPosition:Pos2];
//    CGRect result1 = [tmpText firstRectForRange:(UITextRange *)range ];
//
//        NSLog( @" x: %f, y: %f for msg %@", result1.origin.x, result1.origin.y,msg.text);
//    //
//    //
//    UIView *view1 = [[UIView alloc] initWithFrame:result1];
//    view1.backgroundColor = [UIColor colorWithRed:0.2f green:0.5f blue:0.2f alpha:0.4f];
//    
//    //    tmpText.textContainerInset = UIEdgeInsetsMake(0.0f, 0.0f, 0.0f, 0.0f);
//    //    tmpText.contentInset = UIEdgeInsetsMake(0,0,0,0);
//    //    tmpText.textAlignment = UITextAlignmentLeft;
//    //    cell.textView.backgroundColor = [UIColor redColor];
//    [cell.textView addSubview:view1];
    
    
//    cell.textView.text = finalString;
//    [cell.textView addSubview:tmpText];

    return cell;
}



#pragma mark - JSQMessages collection view flow layout delegate

- (CGFloat)collectionView:(JSQMessagesCollectionView *)collectionView
                   layout:(JSQMessagesCollectionViewFlowLayout *)collectionViewLayout heightForCellTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  Each label in a cell has a `height` delegate method that corresponds to its text dataSource method
     */
    
    /**
     *  This logic should be consistent with what you return from `attributedTextForCellTopLabelAtIndexPath:`
     *  The other label height delegate methods should follow similarly
     *
     *  Show a timestamp for every 3rd message
     */
    if (indexPath.item == 0) {
        return kJSQMessagesCollectionViewCellLabelHeightDefault;
    }
    if (indexPath.item > 0) {
        JSQMessage *message = [self.messages objectAtIndex:indexPath.item];
        JSQMessage *message2 = [self.messages objectAtIndex:indexPath.item-1];
        NSTimeInterval distanceBetweenDates = [message.date timeIntervalSinceDate:message2.date];
        NSInteger minsBetweenDates = distanceBetweenDates / 60;
        if(minsBetweenDates >15){
            return kJSQMessagesCollectionViewCellLabelHeightDefault;
        }
    }
    
    
    return 5.0f;
}

- (CGFloat)collectionView:(JSQMessagesCollectionView *)collectionView
                   layout:(JSQMessagesCollectionViewFlowLayout *)collectionViewLayout heightForMessageBubbleTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  iOS7-style sender name labels
     */
    //    JSQMessage *currentMessage = [self.messages objectAtIndex:indexPath.item];
    //    if ([[currentMessage sender] isEqualToString:self.sender]) {
    //        return 0.0f;
    //    }
    //
    //    if (indexPath.item - 1 > 0) {
    //        JSQMessage *previousMessage = [self.messages objectAtIndex:indexPath.item - 1];
    //        if ([[previousMessage sender] isEqualToString:[currentMessage sender]]) {
    //            return 0.0f;
    //        }
    //    }
    //
    //    return kJSQMessagesCollectionViewCellLabelHeightDefault;
    return 0;
}

- (CGFloat)collectionView:(JSQMessagesCollectionView *)collectionView
                   layout:(JSQMessagesCollectionViewFlowLayout *)collectionViewLayout heightForCellBottomLabelAtIndexPath:(NSIndexPath *)indexPath
{
    return 0.0f;
}

- (void)collectionView:(JSQMessagesCollectionView *)collectionView
                header:(JSQMessagesLoadEarlierHeaderView *)headerView didTapLoadEarlierMessagesButton:(UIButton *)sender
{
    NSLog(@"Load earlier messages!");
    
    float offset =0;
    float oldContentSize= self.collectionView.contentSize.height;
    self.msgOffSet +=self.msgPerLoad;
 
//    NSLog(@"offset %d, limit %d",self.msgOffSet, self.msgPerLoad);
//
//    if (self.msgPerLoad > self.totalMsgCount - self.msgOffSet) {
//        NSLog(@"msgperLoad msg <0");

//    }
//
    if (self.totalMsgCount - self.msgOffSet<0) {
        NSLog(@"totall msg <0");
        self.msgPerLoad  = self.totalMsgCount - self.msgOffSet+self.msgPerLoad;

        self.msgOffSet = self.totalMsgCount;
        self.showLoadEarlierMessagesHeader = NO;

    }
    
    
//    NSLog(@"offset %d, limit %d",self.msgOffSet, self.msgPerLoad);
    
    
    NSString *query =[NSString stringWithFormat:@"select * from ChatMessage where ownerID like '%@' AND (fromId like '%@' and toId like '%@') OR (fromId like '%@' and toId like '%@') LIMIT %d, %d", myCurrentID,myCurrentID,receiver, receiver,myCurrentID,  self.totalMsgCount - self.msgOffSet, self.msgPerLoad];
    //the offset and then msg per page
    
    // Get the results.
    if (self.arrMsgHistory != nil) {
        self.arrMsgHistory = nil;
    }
    self.arrMsgHistory = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
    //loop through array, add object to message.
    //CREATE TABLE ChatMessage(messageId integer primary key, fromId text, toId text, ownerID text, ReceivedTime timestamp, messageContent text); TODO fix the time tag
    int i=0;
    for (id object in self.arrMsgHistory) {
        
        
        //TODO , FIX TIMEZONE ISSUE, ADD TIMEZONE SUPPORT IN DATABASE
        //
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
        [df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
        NSDate *myDate = [df dateFromString: object[4]];
        if (myDate ==nil) {
            
            myDate =[NSDate distantPast];
        }
        
        //date needs to be fixed
        //        NSLog(@"setup my currentID %@", myCurrentID);
        JSQMessage *msg;
        if ([myCurrentID isEqualToString:object[1]]) {
            msg = [[JSQMessage alloc] initWithText:object[5] sender:self.sender date:myDate];
        }else{
            msg = [[JSQMessage alloc] initWithText:object[5] sender:chatPerson date:myDate];
        }
        
        //add msg to the msg object
//        [self.messages addObject:msg];
        [self.messages insertObject:msg atIndex:i];
        i++;
    }

    
//    JSQMessage *hello =[[JSQMessage alloc] initWithText:@"Welcome to JSQMessages: A messaging UI framework for iOS." sender:self.sender date:[NSDate distantPast]];
//    [self.messages insertObject:hello atIndex:0];
//    [self.messages insertObject:hello atIndex:0];
//    [self.messages insertObject:hello atIndex:0];
//    [self.messages insertObject:hello atIndex:0];
//    [self.messages insertObject:hello atIndex:0];

    //reload Data Here.
    
    
    [self.collectionView reloadData];
    

    oldContentSize= self.collectionView.collectionViewLayout.collectionViewContentSize.height - oldContentSize;
    offset = oldContentSize;
    self.collectionView.contentOffset = CGPointMake(self.collectionView.contentOffset.x, self.collectionView.contentOffset.y + offset);
    NSLog(@"offset %f", offset);


//    [self.collectionView reloadData];
 
    
}
- (ventouraClassAppDelegate *)appDelegate {
    return (ventouraClassAppDelegate *)[[UIApplication sharedApplication] delegate];
}

- (XMPPStream *)xmppStream {
    return [[self appDelegate] xmppStream];
}

-(void)loadData{
    // Form the query.
    
    
    NSString *countQuery =[NSString stringWithFormat:@"select  Count(*)  from ChatMessage where ownerID like '%@' AND (fromId like '%@' and toId like '%@') OR (fromId like '%@' and toId like '%@')", myCurrentID,myCurrentID,receiver, receiver,myCurrentID];
    
    NSArray *count = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:countQuery]];
    NSLog(@"msg count : %@",count[0][0]);
    self.totalMsgCount = [count[0][0] intValue];
    
    if (self.totalMsgCount>0) {
        
        NSString *query =[NSString stringWithFormat:@"select * from ChatMessage where ownerID like '%@' AND (fromId like '%@' and toId like '%@') OR (fromId like '%@' and toId like '%@') LIMIT %d, %d", myCurrentID,myCurrentID,receiver, receiver,myCurrentID,  self.totalMsgCount - self.msgOffSet, self.msgPerLoad];
        //the offset and then msg per page
        
        // Get the results.
        if (self.arrMsgHistory != nil) {
            self.arrMsgHistory = nil;
        }
        self.arrMsgHistory = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
        //loop through array, add object to message.
        //CREATE TABLE ChatMessage(messageId integer primary key, fromId text, toId text, ownerID text, ReceivedTime timestamp, messageContent text); TODO fix the time tag
        for (id object in self.arrMsgHistory) {
            
            
            //TODO , FIX TIMEZONE ISSUE, ADD TIMEZONE SUPPORT IN DATABASE
            //
            NSDateFormatter *df = [[NSDateFormatter alloc] init];
            [df setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
            [df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
            NSDate *myDate = [df dateFromString: object[4]];
            if (myDate ==nil) {
                
                myDate =[NSDate distantPast];
            }
            
            //date needs to be fixed
            //        NSLog(@"setup my currentID %@", myCurrentID);
            JSQMessage *msg;
            if ([myCurrentID isEqualToString:object[1]]) {
                msg = [[JSQMessage alloc] initWithText:object[5] sender:self.sender date:myDate];
            }else{
                msg = [[JSQMessage alloc] initWithText:object[5] sender:chatPerson date:myDate];
            }
            
            //add msg to the msg object
            [self.messages addObject:msg];
            
        }
    }
    if (self.totalMsgCount>self.msgPerLoad) {
        self.showLoadEarlierMessagesHeader = YES;
    }
    
    // Reload the table view.
    //[self.tView reloadData];
}
- (void)newMessageReceived:(NSDictionary *)messageContent from:(NSString *)sender{
    NSLog(@"got new msg");
    
    NSString *msgSender = messageContent[@"sender"];
    NSArray*  nameArray= [msgSender componentsSeparatedByString: @"@"];
    msgSender = [nameArray objectAtIndex: 0];
    NSString *personTypeId= [ventouraUtility returnUserIdWithType:[ventouraUtility isUserGuide:self.person.userRole] ventouraId:self.person.ventouraId];
    
//    if ([self.person.userRole isEqualToString:@"GUIDE"]) {
//        personTypeId = [NSString stringWithFormat:@"g_%@",self.person.ventouraId];
//    }else{
//        personTypeId = [NSString stringWithFormat:@"t_%@",self.person.ventouraId];
//    }
    
    
    if ([messageContent[@"sender"] isEqualToString:personTypeId]) {
        JSQMessage *message = [[JSQMessage alloc] initWithText:messageContent[@"msg"] sender:chatPerson date:[NSDate date]];
        /**
         *  Set the typing indicator to be shown
         */
        self.showTypingIndicator = !self.showTypingIndicator;
        
        /**
         *  Scroll to actually view the indicator
         */
        [self scrollToBottomAnimated:YES];
        
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [JSQSystemSoundPlayer jsq_playMessageReceivedSound];//maybe move this line :D
            [self.messages addObject:message];
            [self finishReceivingMessage];
            NSLog(@"end of Jchat Msg rev");
        });
        
        
    }
}

//- (CGSize)collectionView:(JSQMessagesCollectionView *)collectionView
//                  layout:(JSQMessagesCollectionViewFlowLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
//{
//    CGSize bubbleSize = [collectionViewLayout messageBubbleSizeForItemAtIndexPath:indexPath];
//
//    CGFloat cellHeight = bubbleSize.height;
//    return CGSizeMake(collectionViewLayout.itemWidth, cellHeight);
//}



#pragma mark - Actions

- (void)bookingPressed:(UIBarButtonItem *)sender{
    NSLog(@"booking pressed, now load the trallver booking info and display");
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    
    BookTourViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"BookTourViewController"];
    viewController.person = self.person;
    
    [[self navigationController] pushViewController:viewController animated:YES ];
    
}

- (void)morePressed:(UIBarButtonItem *)sender{
    
    NSArray *options;
    if([ventouraUtility isUserGuide:self.person.userRole])
    {
        options = [NSArray arrayWithObjects:@"View Profile", @"Make Booking",@"Delete Chat History", @"Delete Match", nil];
    }else{
        options = [NSArray arrayWithObjects:@"View Profile",@"Delete Chat History",@"Delete Match", nil];
        
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

- (void)willPresentActionSheet:(UIActionSheet *)actionSheet
{
    for (UIView *subview in actionSheet.subviews) {
        if ([subview isKindOfClass:[UIButton class]]) {
            UIButton *button = (UIButton *)subview;
            if ([button.currentTitle isEqualToString:@"Cancel"]) {
                [button setTitleColor:[ventouraUtility ventouraTitleColour] forState:UIControlStateNormal];
            }else{
                [button setTitleColor:[ventouraUtility ventouraPink] forState:UIControlStateNormal];
            }
            button.titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:18];
        }
    }
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (actionSheet.tag == 200) {
        if([ventouraUtility isUserGuide:self.person.userRole]){
            if (buttonIndex ==0) {
                NSLog(@"Profile");
                [self loadUserProfile];
            }else if (buttonIndex ==1){
                NSLog(@"book");
                NSLog(@"booking pressed, now load the trallver booking info and display");
                UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                                     bundle:nil];
                
                BookTourViewController *viewController =
                [storyboard instantiateViewControllerWithIdentifier:@"BookTourViewController"];
                viewController.person = self.person;
                
                [[self navigationController] pushViewController:viewController animated:YES ];
            }
            else if(buttonIndex ==2){
                NSLog(@"delete Chat");
                [self deleteChatHistoryAlert];
            }
            else if(buttonIndex ==3){
                NSLog(@"delete");
                
            }
        }
        else{
            if (buttonIndex ==0) {
                NSLog(@"Profile");
                [self loadUserProfile];
            }else if (buttonIndex ==1){
                NSLog(@"delete chat");
                [self deleteChatHistoryAlert];
            }

            else if (buttonIndex ==2){
                NSLog(@"delete");
            }
        }
    }
}

- (void) loadUserProfile {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    OtherUserProfileViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileViewController"];
    viewController.person = self.person;
    viewController.delegate = self;
    viewController.fromVentouring = FALSE;
    viewController.fromMessages = TRUE;
    [[self navigationController] pushViewController:viewController animated:YES ];
}

- (void)messagesInputToolbar:(JSQMessagesInputToolbar *)toolbar didPressLeftBarButton:(UIButton *)sender{
}


- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {

    
}

-(void)deleteChatHistoryAlert{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Delete this chat?"
                                                    message:@""
                                                   delegate:self
                                          cancelButtonTitle:@"No"
                                          otherButtonTitles:@"Yes",nil];
    [alert show];
    //need to add tag to this
}



-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex ==1) {
        NSLog(@"buttons hit");
        [self deleteChatHistory:myCurrentID withChatId:receiver];
        //delete chat history
    }
}
-(void) deleteChatHistory:(NSString*)myId withChatId:(NSString*)chatId{
  
    NSString *query = [NSString stringWithFormat:@"delete from ChatMessage where ownerID like '%@' AND (fromId like '%@' and toId like '%@') OR (fromId like '%@' and toId like '%@')", myId,myId,chatId, chatId,myId];

    [self.dbManager executeQuery:query];
    [self.messages removeAllObjects];
    // Reload the table view.
    [self.collectionView reloadData];
}


-(void)loadMsgFromDb{

}
-(void)didReceiveTravellerProfile:(Person*) profile{
//    self.person.images = profile.images;
//    NSLog(@"images count trav: %li", self.person.images.count);
    
}
-(void)didReceiveGuideProfile: (Person*) profile{
//    self.person.images = profile.images;
//    NSLog(@"images count guide: %li", self.person.images.count);
    
}
@end
