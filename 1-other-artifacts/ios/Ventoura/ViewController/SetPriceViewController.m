//
//  SetPriceViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 7/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "SetPriceViewController.h"

@interface SetPriceViewController ()
@property (nonatomic,retain) IBOutlet UITableView *tView;
@property (nonatomic,assign) NSInteger price;
@end

@implementation SetPriceViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    if (self.priceValue) {
        self.price = self.priceValue;
    }else{
        self.price = 0;
    }
     //or get it from previous page  . string to int
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT);
    }else{
        self.tView.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT);
    }
    self.tView.backgroundColor = [UIColor clearColor];
    self.tView.separatorStyle=UITableViewCellSeparatorStyleNone;
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [self.delegate editPriceViewController:self didFinishEditTourDetail:self.price];
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
    //    self.city.cityName = self.arrCity[indexPath.row][0] ;
    //    self.city.countryId = self.arrCity[indexPath.row][1] ;
    //    self.city.cityId = self.arrCity[indexPath.row][2] ;
    //    self.city.tableIndexPath = indexPath.row;
//    self.country = [[City alloc] initWithId:nil
//                                   cityName:nil
//                                  countryId:self.arrCountry[indexPath.row][0]
//                                countryName:self.arrCountry[indexPath.row][1]
//                             tableIndexPath:indexPath.row];
//    NSLog(@"clicked on %@",self.country.countryName );
//    
//    
//    [self.navigationController popViewControllerAnimated:TRUE];
    
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    return 4;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    switch ( indexPath.row )
    {
        case 0:
            return 100;
            break;
        case 1:
            return 80;
            break;
        case 2:
            return 220;
            break;
        case 3:
            return 100;
            break;
            
    }
    return 100;

}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    
    static NSString *CellIdentifier = @"Cell";
    switch ( indexPath.row )
    {
        case 0:
            CellIdentifier = @"sliderCell";
            break;
        case 1:
            CellIdentifier = @"CircleView";
            break;
            
        case 2:
            CellIdentifier = @"CircleView";
            break;
        case 3:
            CellIdentifier = @"CircleView";
            break;
    }

  
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }

    
    switch ( indexPath.row )
    {
        case 0:{
            ventouraSlider *slider = (ventouraSlider *)[cell viewWithTag:1];
            slider.value = self.price;
            UIImage *minImage = [UIImage imageNamed:@"SalmonSlide.png"];

            UIEdgeInsets edgeInsets = UIEdgeInsetsMake(0, 10,0, 10);
            minImage = [minImage resizableImageWithCapInsets:edgeInsets];
            
            [slider setMinimumTrackImage:minImage forState:UIControlStateNormal];
            UIImage *maxImage = [UIImage imageNamed:@"GreySlide.png"];
            maxImage = [maxImage resizableImageWithCapInsets:edgeInsets];
            [slider setMaximumTrackImage:maxImage forState:UIControlStateNormal];
            UIImage *nob = [UIImage imageNamed:@"Nob.png"];
            [slider setThumbImage:nob forState:UIControlStateNormal];
            UILabel *amountLabel = (UILabel *)[cell viewWithTag:2];
            amountLabel.text = [NSString stringWithFormat:@"%ld", self.price] ;
            amountLabel.backgroundColor = [UIColor clearColor];
            amountLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            amountLabel.textColor = [ventouraUtility ventouraTextBodyGrey];

            amountLabel.textAlignment = NSTextAlignmentCenter;
            
            [slider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];

            CGRect trackRect = [slider trackRectForBounds:slider.bounds];
            CGRect thumbRect = [slider thumbRectForBounds:slider.bounds
                                                     trackRect:trackRect
                                                         value:slider.value];
//            amountLabel.center = CGPointMake(thumbRect.origin.x, slider.frame.origin.y - 20);
            
            UIImageView *pointer = (UIImageView *)[cell viewWithTag:3];
            pointer.frame = CGRectMake(thumbRect.origin.x+slider.frame.origin.x - pointer.frame.size
                                       .width/2+thumbRect.size.width/2, slider.frame.origin.y -15, pointer.frame.size.width, pointer.frame.size.height);
            amountLabel.frame = CGRectMake(thumbRect.origin.x+slider.frame.origin.x - amountLabel.frame.size
                                           .width/2+thumbRect.size.width/2, slider.frame.origin.y - 35, amountLabel.frame.size.width, amountLabel.frame.size.height);
            break;
        }
        case 1:{
            UIView * line = (UIView *)[cell viewWithTag:25];
            line.backgroundColor = [UIColor lightGrayColor];
        
            UILabel * title = (UILabel *)[cell viewWithTag:22];
            title.text = @"Booking Fee";
            title.textColor = [ventouraUtility ventouraTextBodyGrey];
            title.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            
            UILabel * text = (UILabel *)[cell viewWithTag:24];
            text.frame = CGRectMake(text.frame.origin.x, text.frame.origin.y, 240,text.frame.size.height);
//            text.backgroundColor = [UIColor redColor];
            text.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
            text.textColor = [UIColor lightGrayColor];
            text.text = @"The amount you receive for providing your servics to the Traveller";
                            text.numberOfLines = 0;
                            [text sizeToFit];
            
            UILabel * price = (UILabel *)[cell viewWithTag:21];
            price.text = [NSString stringWithFormat:@"£%ld",self.price];
            price.textColor = [ventouraUtility ventouraTextBodyGrey];
            price.font = [UIFont fontWithName:@"Roboto-Regular" size:16];

            break;
            
        }
        case 2:{
            UIView * line = (UIView *)[cell viewWithTag:25];
            line.backgroundColor = [UIColor lightGrayColor];
            
            UILabel * title = (UILabel *)[cell viewWithTag:22];
            title.text = @"Your Tour Fee";
            title.textColor = [ventouraUtility ventouraTextBodyGrey];
            title.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            
            UILabel * text = (UILabel *)[cell viewWithTag:24];
            text.frame = CGRectMake(text.frame.origin.x, text.frame.origin.y, 240,text.frame.size.height);
            //            text.backgroundColor = [UIColor redColor];
            text.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
            text.textColor = [UIColor lightGrayColor];

            text.text =  @"Guarantee of payment for any cancellations in the 7 days leading up to your tour*\n\nForegin exchange, transfer, bank and chargeback fees(we take care of these so you don't have to, and you know excatly what you receive)\n\nProvision of the Ventoura platform, improving it for future releases! *for Card Locals.";
        
            text.numberOfLines = 0;
            [text sizeToFit];
            UILabel * price = (UILabel *)[cell viewWithTag:21];
            price.textColor = [ventouraUtility ventouraTextBodyGrey];
            float adjustedPrice = self.price * 0.2;
            price.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            price.text = [NSString stringWithFormat:@"£%.f", adjustedPrice];

            break;
//            UIImageView * circle = (UIImageView *)[cell viewWithTag:26];
        
        }
        case 3:{
            UIView * line = (UIView *)[cell viewWithTag:25];
            line.backgroundColor = [UIColor lightGrayColor];
            
            UILabel * title = (UILabel *)[cell viewWithTag:22];
            title.text = @"Your Tour Fee";
            title.textColor = [ventouraUtility ventouraTextBodyGrey];
            title.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            
            UILabel * text = (UILabel *)[cell viewWithTag:24];
            text.frame = CGRectMake(text.frame.origin.x, text.frame.origin.y, 240,text.frame.size.height);
            //            text.backgroundColor = [UIColor redColor];
            text.font = [UIFont fontWithName:@"Roboto-Regular" size:12];
            text.textColor = [UIColor lightGrayColor];
            text.text = @"How much the traveller will pay in total.";
            text.numberOfLines = 0;
            [text sizeToFit];
            UILabel * price = (UILabel *)[cell viewWithTag:21];
            price.text = [NSString stringWithFormat:@"£%ld",self.price];
            price.textColor = [ventouraUtility ventouraTextBodyGrey];
            price.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            float adjustedPrice = self.price * 1.2;
            price.font = [UIFont fontWithName:@"Roboto-Regular" size:16];
            price.text = [NSString stringWithFormat:@"£%.f", adjustedPrice];
            break;
            //            UIImageView * circle = (UIImageView *)[cell viewWithTag:26];
            
        }
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.backgroundColor = [UIColor clearColor];
    return cell;
}



- (IBAction)sliderValueChanged:(UISlider *)sender {
    NSLog(@"slider value = %f", sender.value);
    self.price = sender.value;
    [self.tView reloadData];
}


@end
