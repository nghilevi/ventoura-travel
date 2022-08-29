//
//  ReviewViewController.m
//  Ventoura
//
//  Created by Jai Carlton on 3/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ReviewViewController.h"

@interface ReviewViewController()
{

}


@end


@implementation ReviewViewController

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) viewDidLoad
{
    [super viewDidLoad];
    NSLog(@"start of review did load");
    [self.navigationController.navigationBar  setBarTintColor:[ventouraUtility ventouraNavBackgroundColour]];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTitleTextAttributes:@{
                                                                      NSForegroundColorAttributeName: [ventouraUtility ventouraTitleColour],
                                                                      NSFontAttributeName: [UIFont fontWithName:@"Roboto-Regular" size:19],
                                                                      }];
    NSLog(@"mid review did load");
    [self.tView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    self.tView.backgroundColor = [UIColor clearColor];
    self.view.backgroundColor = [UIColor clearColor];
    
    
    [self.moreButton setTarget: self];
    [self.moreButton setAction: @selector(moreButtonClicked)];
    
    [self setUpBackground];
    
    NSLog(@"end of view did load");
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.tView reloadData];
}

- (void)viewWillDisappear:(BOOL)animated
{
    
    [super viewWillDisappear:animated];
}

- (void) moreButtonClicked{
    NSLog(@"more button clicked from review view");
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)sectionIndex
{
    //TODO use number of reviews from server call later
    //return reviewClassObject.numReviews
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    //TODO put text into label, apply font and size, check height and add a bit of spacing
    return 90;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"review";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: cellIdentifier forIndexPath: indexPath];
   
    UILabel *reviewerNameLabel = (UILabel *)[cell viewWithTag:111];
    //TODO set text to name of reviewer
    //reviewerNameLabel.text = reviewObject[indexPath.row].name;
    reviewerNameLabel.text = @"Johnny jdoglpq";
    reviewerNameLabel.font = [UIFont fontWithName:@"Roboto-regular" size:16];
    reviewerNameLabel.textColor = [ventouraUtility ventouraTextBodyGreyProfileLabel];;
    [reviewerNameLabel sizeToFit];
    
    UILabel *reviewTextLabel = (UILabel *)[cell viewWithTag:112];
    //TODO set review text
    //reviewTextLabel.text = reviewObject[indexPath.row].reviewText;
    reviewTextLabel.text = @"Overall ok. Really loved the secret underground shop.";
    reviewTextLabel.font = [UIFont fontWithName:@"Roboto-regular" size:16];
    reviewTextLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
    reviewTextLabel.frame = CGRectMake(reviewTextLabel.frame.origin.x, reviewTextLabel.frame.origin.y, 270, reviewTextLabel.frame.size.height);
    reviewTextLabel.numberOfLines = 0;
    [reviewTextLabel sizeToFit];
    
    UIView *lineSeparator = (UIView *)[cell viewWithTag:113];
    lineSeparator.backgroundColor = [UIColor lightGrayColor];
    
    
    UIView *reviewView = (UIView *)[cell viewWithTag:120];
    reviewView.backgroundColor = [UIColor clearColor];
    for (UIView *subview in reviewView.subviews) {
        [subview removeFromSuperview];
    }
    NSLog(@"%f, %f", reviewerNameLabel.frame.origin.x, reviewerNameLabel.frame.size.width);
    CGFloat xVal = reviewerNameLabel.frame.origin.x + reviewerNameLabel.frame.size.width;

    
    reviewView.frame = CGRectMake(xVal, reviewView.frame.origin.y, reviewView.frame.size.width, reviewView.frame.size.height);
    //TODO set score to reviewers score that they set
    //CGFloat score = reviewObject[indexPath.row].score;
    CGFloat score = 5;
    
    CGFloat starSize =  15;
    int numberOfStars= floorf(score)/2;
    CGFloat incPadding = 20;
    int numberofHalfStar = 0;
    if (floorf(score)/2 != numberOfStars) {
        numberofHalfStar = 1;
    }
    
    CGFloat starsWidth = starSize * (numberOfStars + numberofHalfStar) + (incPadding-starSize) * (numberOfStars + numberofHalfStar-1);
    CGFloat leftPadding = 10;//floorf(CGRectGetWidth(reviewView.frame)/2) - starsWidth/2;
    
    CGFloat topPadding = 5;
    CGFloat inc = 0;
    UIImage * starImage = [UIImage imageNamed:[NSString stringWithFormat:@"Star_Salmon_full.png"]];
    UIImage * halfStartImage = [UIImage imageNamed:[NSString stringWithFormat:@"Star_Salmon_2.png"]];
    
    
    
    //    _star = [[UIImageView alloc] initWithFrame:frame];
    //    _star.image = countryFlagImg;
    //    [_informationView addSubview:_star];
    for (int i=0; i<numberOfStars; i++){
        CGRect frame = CGRectMake(leftPadding +inc,topPadding, starSize, starSize);
        UIImageView *stars = [[UIImageView alloc] initWithFrame:frame];
        stars.image = starImage;
        [reviewView addSubview:stars];
        inc+=incPadding;
    }
    if (numberofHalfStar>0) {
        CGRect frame = CGRectMake(leftPadding +inc,topPadding, starSize, starSize);
        UIImageView *stars = [[UIImageView alloc] initWithFrame:frame];
        stars.image = halfStartImage;
        [reviewView addSubview:stars];
        inc+=incPadding;
    }
    
    
    
    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    cell.backgroundColor = [UIColor clearColor];
    
    return cell;
}

-(void)setUpBackground{
    
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
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT
                                      );
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }
}
@end
