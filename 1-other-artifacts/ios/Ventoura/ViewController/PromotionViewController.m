//
//  PromotionViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 3/09/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "PromotionViewController.h"

@interface PromotionViewController (){
    UIView *promotionDoneView;
    UIImageView *promotionImageView;
    UILabel * textTitle;
    UILabel * textContent;
}
@property (nonatomic, strong) DBManager *dbManager;

@end

@implementation PromotionViewController

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
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];

    self.lastSelectedIndex = -1;
    self.promotionCity1 = @"-1";
    self.promotionCity2 = @"-1";
    self.promotionCity3 = @"-1";
    self.promotionCity4 = @"-1";
    self.previewButton.enabled=false;
    
    self.cityLabel1.textColor = [ventouraUtility ventouraPromotionGrey];
    self.cityLabel2.textColor = [ventouraUtility ventouraPromotionGrey];
    self.cityLabel3.textColor = [ventouraUtility ventouraPromotionGrey];
    self.cityLabel4.textColor = [ventouraUtility ventouraPromotionGrey];
    
    
    [self.previewButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.previewButton setTitleColor:[ventouraUtility ventouraPromotionGrey] forState:UIControlStateDisabled];

    self.circleOutline= [UIImage imageNamed:@"CircleEmpty.png"];
    self.salmonCircle= [UIImage imageNamed:@"SalmonCirlce.png"];
    
    [self.revealButtonItem setTarget: self.revealViewController];
    [self.revealButtonItem setAction: @selector( revealToggle: )];
    [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
    
    
    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
    }else{
        self.scrollView.frame = CGRectMake(0, 0, 320, 480);
    }
    
    // Do any additional setup after loading the view.
//    self.city1.frame = CGRectMake(self.city1.frame.origin.x, self.city1.frame.origin.y, 50, 50);
    [self.scrollView setContentSize:CGSizeMake(self.view.frame.size.width, 568)];
    
    UITapGestureRecognizer *singleFingerTap1 =
    [[UITapGestureRecognizer alloc] initWithTarget:self
                                            action:@selector(handleSingleTap1:)];
    
    UITapGestureRecognizer *singleFingerTap2 =
    [[UITapGestureRecognizer alloc] initWithTarget:self
                                            action:@selector(handleSingleTap2:)];
    UITapGestureRecognizer *singleFingerTap3 =
    [[UITapGestureRecognizer alloc] initWithTarget:self
                                            action:@selector(handleSingleTap3:)];
    UITapGestureRecognizer *singleFingerTap4 =
    [[UITapGestureRecognizer alloc] initWithTarget:self
                                            action:@selector(handleSingleTap4:)];
    
    [self.city1 addGestureRecognizer:singleFingerTap1];
    [self.city2 addGestureRecognizer:singleFingerTap2];

    [self.city3 addGestureRecognizer:singleFingerTap3];

    [self.city4 addGestureRecognizer:singleFingerTap4];
    
    
    //creates Extra view
    CGRect sectionFrame = CGRectMake(45,196,230,230);
    CGRect frame = CGRectMake(0, 0, 230, 230);

    promotionDoneView = [[UIView alloc] initWithFrame:sectionFrame];
    promotionImageView = [[UIImageView alloc] initWithFrame:frame];
    
    
//    NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:NO ventouraId:[ventouraUtility returnMyUserId]];
//   
//    NSString *imagePath = [imgPathPreFix stringByAppendingPathComponent:[NSString stringWithFormat:@"/promotion1.png/"]];
//    NSLog(@"imagePath: %@", imagePath);
//    

    [promotionDoneView addSubview:promotionImageView];
    [self.scrollView addSubview:promotionDoneView];
    textTitle = [[UILabel alloc] initWithFrame:CGRectMake(0,70, 230, 30)];
    textTitle.text = [NSString stringWithFormat:@"Awesomme. Thanks."];
    textTitle.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
    textTitle.textColor = [ventouraUtility ventouraPromotionGrey];
    textTitle.textAlignment = NSTextAlignmentCenter;
    
     textContent = [[UILabel alloc] initWithFrame:CGRectMake(20,110, 210, 30)];
    textContent.text = [NSString stringWithFormat:@"Winner will be announced on 21/01/2015 on our Facebook Page"];
    textContent.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
    textContent.textColor = [ventouraUtility ventouraPromotionGrey];
    textContent.textAlignment = NSTextAlignmentCenter;
    textContent.numberOfLines = 0;
    [textContent sizeToFit];
    [promotionDoneView addSubview:textTitle];
    [promotionDoneView addSubview:textContent];
    


    
}

-(void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
  
    
    NSError *error = nil;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *destinationPath = [path stringByAppendingPathComponent:@"/ImagesFolder"];
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
    NSString *folderName = [NSString stringWithFormat:@"t_%@",[ventouraUtility returnMyUserId]];
    
    destinationPath = [destinationPath stringByAppendingPathComponent:@"Promotion"];
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
    }
    
    path = [destinationPath stringByAppendingPathComponent:[NSString stringWithFormat:@"promotion1_%@.png",folderName]];
    
    UIImage * image = [UIImage imageWithContentsOfFile:[NSString stringWithFormat:@"%@",path]];
    
    //    UIImage * image = [UIImage imageNamed:[NSString stringWithFormat:@"fbtest.png"]];
    promotionImageView.image = [ventouraUtility imageByApplyingAlpha:0.10f withImage:image];
    //    imgView.image = [ventouraUtility imageCropSquareCentre:image];
    
    NSString *query =[NSString stringWithFormat:@"select * from Promotion where userId like '%@' AND promotionVentouraId=1", [ventouraUtility returnMyUserId]];
    // Get the results.
    NSArray *promotion = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];

    if (promotion.count >0) {
        NSLog(@"User has entered the promotion.");
        [self.city1 setHidden:YES];
        [self.city2 setHidden:YES];
        [self.city3 setHidden:YES];
        [self.city4 setHidden:YES];
        [self.previewButton setHidden:YES];
        [promotionDoneView setHidden:NO];
        [self.likeButton setHidden:NO];
    }else{
        NSLog(@"User has not entered the promotion.");
        [promotionDoneView setHidden:YES];
        [self.likeButton setHidden:YES];
    }
    
    
//    //load flag
//    if(0){
//        NSLog(@"Promotion Entered = true");
//        [self.city1 setHidden:YES];
//        [self.city2 setHidden:YES];
//        [self.city3 setHidden:YES];
//        [self.city4 setHidden:YES];
//        [self.previewButton setHidden:YES];
//    }else{
//        [promotionDoneView setHidden:YES];
//        [self.likeButton setHidden:YES];
//    }
}

- (void)handleSingleTap1:(UITapGestureRecognizer *)recognizer {
    //    CGPoint location = [recognizer locationInView:[recognizer.view superview]];
    
    //Do stuff here...
    NSLog(@"clicked 1");
    self.lastSelectedIndex = 0;
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    CitySelectionViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"CitySelectionViewController"];
    viewController.delegate = self;
//    viewController.city = self.city;
    [[self navigationController] pushViewController:viewController animated:YES ];

}


- (void)handleSingleTap2:(UITapGestureRecognizer *)recognizer {

    self.lastSelectedIndex = 1;
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    CitySelectionViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"CitySelectionViewController"];
    viewController.delegate = self;
    [[self navigationController] pushViewController:viewController animated:YES ];
    
}

- (void)handleSingleTap3:(UITapGestureRecognizer *)recognizer {
    self.lastSelectedIndex = 2;
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    CitySelectionViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"CitySelectionViewController"];
    viewController.delegate = self;
    [[self navigationController] pushViewController:viewController animated:YES ];
    
}

- (void)handleSingleTap4:(UITapGestureRecognizer *)recognizer {
    self.lastSelectedIndex = 3;
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    CitySelectionViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"CitySelectionViewController"];
    viewController.delegate = self;
    [[self navigationController] pushViewController:viewController animated:YES ];
    
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



- (void)addItemViewController:(CitySelectionViewController *)controller didFinishCitySelection:(City *)city
{
    NSLog(@"From Selection %@",city.cityName);
    if (self.lastSelectedIndex==0 && city.cityId) {
        self.cityLabel1.text = city.cityName;
        self.cityLabel1.textColor = [UIColor whiteColor];
        self.cityBackground1.image = self.salmonCircle;
        self.promotionCity1 = city.cityId;
        
    }
    
    if (self.lastSelectedIndex==1 && city.cityId ) {
        self.cityLabel2.text = city.cityName;
        self.cityLabel2.textColor = [UIColor whiteColor];
        self.cityBackground2.image = self.salmonCircle;
        self.promotionCity2 = city.cityId;
    }
    
    if (self.lastSelectedIndex==2 && city.cityId ) {
        self.cityLabel3.text = city.cityName;
        self.cityLabel3.textColor = [UIColor whiteColor];
        self.cityBackground3.image = self.salmonCircle;
        self.promotionCity3 = city.cityId;
    }
    if (self.lastSelectedIndex==3 && city.cityId ) {
        self.cityLabel4.text = city.cityName;
        self.cityLabel4.textColor = [UIColor whiteColor];
        self.cityBackground4.image = self.salmonCircle;
        self.promotionCity4 = city.cityId;
    }
    self.lastSelectedIndex = -1;
    //toggle continue btn here
    
    if (![self.promotionCity1 isEqualToString:@"-1"] && ![self.promotionCity2 isEqualToString:@"-1"] && ![self.promotionCity3 isEqualToString:@"-1"]  && ![self.promotionCity4 isEqualToString:@"-1"]) {
        self.previewButton.enabled = true;
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


- (IBAction)previewPressed
{
    NSLog(@"preview pressed ");
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
                                                         bundle:nil];
    PromotionPreviewViewController *viewController =
    [storyboard instantiateViewControllerWithIdentifier:@"PromotionPreviewViewController"];
//    viewController.delegate = self;
    viewController.city1 = self.promotionCity1;
    viewController.city2 = self.promotionCity2;
    viewController.city3 = self.promotionCity3;
    viewController.city4 = self.promotionCity4;

    [[self navigationController] pushViewController:viewController animated:YES ];
}

- (IBAction)likePressed{
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString: @"http://www.google.com"]];
}

@end
