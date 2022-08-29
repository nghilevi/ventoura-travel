//
// ChoosePersonView.m
//
//  Created by Wenchao Chen on 14/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//


#import "ChoosePersonView.h"


static const CGFloat ChoosePersonViewImageLabelWidth = 42.f;

@interface ChoosePersonView ()
@property (nonatomic, strong) UIView *informationView;
@property (nonatomic, strong) UIImageView *profileImageView;

@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) UIImageView *flagImageView;
@property (nonatomic, strong) UIImageView *star;

@property (nonatomic, strong) NSArray *arrMsgHistory;


@property (nonatomic, strong) ImageLabelView *cameraImageLabelView;
@property (nonatomic, strong) ImageLabelView *interestsImageLabelView;
@property (nonatomic, strong) ImageLabelView *friendsImageLabelView;

@property (nonatomic, strong) DBManager *dbManager;

@end

@implementation ChoosePersonView

#pragma mark - Object Lifecycle

- (instancetype)initWithFrame:(CGRect)frame
                       person:(Person *)person
                      options:(MDCSwipeToChooseViewOptions *)options {
    self = [super initWithFrame:frame options:options];
    if (self) {
        _person = person;
              UIImage * img = [UIImage imageNamed:@"ventouring_pin_bg.png"];
                [self constructPictureView];
                [self constructInformationView];
                [[self imageView] setImage: img];
//                self.layer.anchorPoint = CGPointMake(0.5, 1);
    }
    return self;
}

#pragma mark - Internal Methods
- (void)constructPictureView {
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        //CGFloat bottomHeight = 60.f;
        //maybe have to crop/
        CGRect profileFrame = CGRectMake(15, 15 ,255, 255);
        _profileImageView = [[UIImageView alloc] initWithFrame:profileFrame];
        _profileImageView.backgroundColor = [UIColor whiteColor];
        _profileImageView.clipsToBounds = YES;
        _profileImageView.autoresizingMask = UIViewAutoresizingFlexibleWidth |
        UIViewAutoresizingFlexibleTopMargin;
        //crop it to the frameD:
        UIImage * img = [ventouraUtility imageCropSquareCentre:_person.image];
//        img = [self imageByCroppingImage:img toSize:CGSizeMake(260, 260)];
        _profileImageView.layer.cornerRadius =  _profileImageView.frame.size.width / 2;
        _profileImageView.clipsToBounds = YES;
        _profileImageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleImageTap:)];
        tap.cancelsTouchesInView = YES;
        tap.numberOfTapsRequired = 1;
//        tap.delegate = self;
        [_profileImageView addGestureRecognizer:tap];
        // Now the image will have been loaded and decoded and is ready to rock for the main thread
        dispatch_sync(dispatch_get_main_queue(), ^{            
            [_profileImageView setImage: img];
            [self addSubview:_profileImageView];
        });
    });
    
    
    
}

- (void) handleImageTap:(UIGestureRecognizer *)gestureRecognizer {
    NSLog(@"imaged tab");
    [_delegate profileImageTapped];
//    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard"
//                                                         bundle:nil];
//    JChatViewController *viewController =
//    [storyboard instantiateViewControllerWithIdentifier:@"JChatViewController"];
//    viewController.person = person;
//    [[self navigationController] pushViewController:viewController animated:YES ];

}


- (void)constructInformationView {
    CGFloat bottomHeight = 250.f;
    CGRect bottomFrame = CGRectMake(0,
                                    CGRectGetHeight(self.bounds) - bottomHeight,
                                    CGRectGetWidth(self.bounds),
                                    bottomHeight);
    _informationView = [[UIView alloc] initWithFrame:bottomFrame];
    _informationView.backgroundColor = [UIColor clearColor];
    _informationView.clipsToBounds = YES;
    _informationView.autoresizingMask = UIViewAutoresizingFlexibleWidth |
                                        UIViewAutoresizingFlexibleTopMargin;
//    _informationView.layer.borderWidth = 0.5f;
//    _informationView.layer.borderColor = [UIColor redColor].CGColor;
    [self constructNameLabel];

    [self addSubview:_informationView];
    if ([ventouraUtility isUserGuide:_person.userRole]) {
        [self constructReviewScore];

    }
    
    
    [self constructFlagImageLabelView];
//    [self constructInterestsImageLabelView];
//    [self constructFriendsImageLabelView];
}

- (void)constructNameLabel {
    CGFloat leftPadding =0;
    CGFloat topPadding =0;
    if (![ventouraUtility isUserGuide:_person.userRole]) {
        topPadding = 15;
    }
    CGRect frame = CGRectMake(leftPadding,
                              topPadding,
                              floorf(CGRectGetWidth(_informationView.frame)),
                              30);
    _nameLabel = [[UILabel alloc] initWithFrame:frame];
    NSString* nameString =@"";
    //TODO fix database tourtype, get tour type in builder.
    if ([ventouraUtility isUserGuide:_person.userRole]) {
        _nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
        int value = [_person.tourType intValue];
        if(_person.tourType && value>=0){
            self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];

            NSLog(@"valid tour type");
            NSString *query =[NSString stringWithFormat:@"select tourName from tourType WHERE tourId =%@",_person.tourType];
            NSArray* results = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
            if (results.count>0) {
                NSString * tourType =results[0][0];
//                tourTypeLabel.text = [NSString stringWithFormat:@"%@ Tour",tourType];
                nameString = [NSString stringWithFormat:@"%@ Tour",tourType];

            }else{
                NSLog(@"invalid tour type, no db match %@",_person.tourType);
            }
        
        }else{
            nameString = [NSString stringWithFormat:@""];
            NSLog(@"invalid tour type");
        }
        
    }else{
        _nameLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:20];
        nameString = @"Traveller";
    }
    _nameLabel.text = [NSString stringWithFormat:@"%@", nameString];
//    [_nameLabel setBackgroundColor:[ventouraUtility ventouraPinkAlpha]];
//    _nameLabel.layer.borderWidth = 0.5f;
//    _nameLabel.layer.borderColor = [UIColor lightGrayColor].CGColor;
//    _nameLabel.backgroundColor = [UIColor redColor];
    [_nameLabel setTextAlignment:NSTextAlignmentCenter];
    [_nameLabel setTextColor:[UIColor colorWithRed:0.514 green:0.514 blue:0.506 alpha:1] /*#838381*/];
    [_informationView addSubview:_nameLabel];
}


- (void)constructReviewScore {
//    CGFloat reviewWidth =  150;
//    CGFloat leftPadding = floorf(CGRectGetWidth(_informationView.frame)/2 - reviewWidth/2);
//    CGFloat topPadding = 60;
//    CGRect frame = CGRectMake(leftPadding,topPadding, reviewWidth, 30);
//
    CGFloat score = 7.4;
//    CGFloat score = 7.4;
    
    
//    CGFloat score = (CGFloat)[_person.useravgReviewScoreRole floatValue];
//    NSLog(@"Choose Person Score: %@", _person.useravgReviewScoreRole);
    CGFloat starSize =  20;
    int numberOfStars= floorf(score)/2;
    CGFloat incPadding = 25;
    int numberofHalfStar = 0;
    if (floorf(score)/2 != numberOfStars) {
        numberofHalfStar = 1;
    }
    
    CGFloat starsWidth = starSize * (numberOfStars + numberofHalfStar) + (incPadding-starSize) * (numberOfStars + numberofHalfStar-1);
    CGFloat leftPadding = floorf(CGRectGetWidth(_informationView.frame)/2) - starsWidth/2;
    CGFloat topPadding = 30;
    CGFloat inc = 0;
    UIImage * starImage = [UIImage imageNamed:[NSString stringWithFormat:@"star_black_2.png"]];
    UIImage * halfStartImage = [UIImage imageNamed:[NSString stringWithFormat:@"star_black_1.png"]];


//    _star = [[UIImageView alloc] initWithFrame:frame];
//    _star.image = countryFlagImg;
//    [_informationView addSubview:_star];
    for (int i=0; i<numberOfStars; i++){
        CGRect frame = CGRectMake(leftPadding +inc,topPadding, starSize, starSize);
        UIImageView *stars = [[UIImageView alloc] initWithFrame:frame];
        stars.image = starImage;
        [_informationView addSubview:stars];
        inc+=incPadding;
    }
    if (numberofHalfStar>0) {
        CGRect frame = CGRectMake(leftPadding +inc,topPadding, starSize, starSize);
        UIImageView *stars = [[UIImageView alloc] initWithFrame:frame];
        stars.image = halfStartImage;
        [_informationView addSubview:stars];
    }
}

- (void)constructFlagImageLabelView {
    CGFloat flagSize =  50;
    CGFloat leftPadding = floorf(CGRectGetWidth(_informationView.frame)/2 - flagSize/2);
    CGFloat topPadding = 60;
    CGRect frame = CGRectMake(leftPadding,topPadding, flagSize, flagSize);
    
    
//    NSString *query =[NSString stringWithFormat:@"select cityName, countryId from City where id = %@",_person.country];
//    if (self.arrMsgHistory != nil) {
//        self.arrMsgHistory = nil;
//    }
//    self.arrMsgHistory = [[NSArray alloc] initWithArray:[self.dbManager loadDataFromDB:query]];
    NSLog(@"city name , %@", _person.country) ;
//

    
    //needs error checking, otherwise it will crash TODO
    UIImage * countryFlagImg = [UIImage imageNamed:[NSString stringWithFormat:@"%@.png",_person.country]];
    _flagImageView = [[UIImageView alloc] initWithFrame:frame];
    _flagImageView.image = countryFlagImg;
    [_informationView addSubview:_flagImageView];


}

- (ImageLabelView *)buildImageLabelViewLeftOf:(CGFloat)x image:(UIImage *)image text:(NSString *)text {
    CGRect frame = CGRectMake(x - ChoosePersonViewImageLabelWidth,
                              0,
                              ChoosePersonViewImageLabelWidth,
                              CGRectGetHeight(_informationView.bounds));
    ImageLabelView *view = [[ImageLabelView alloc] initWithFrame:frame
                                                           image:image
                                                            text:text];
    view.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin;
    return view;
}




@end
