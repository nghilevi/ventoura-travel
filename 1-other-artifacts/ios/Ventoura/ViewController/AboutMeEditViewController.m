//
//  AboutMeEditViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 20/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "AboutMeEditViewController.h"

@interface AboutMeEditViewController ()

@end

@implementation AboutMeEditViewController
@synthesize delegate;

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
    
    self.textField.layer.borderWidth = 1.0f;
//    self.textField.layer.borderColor = [[ventouraUtility ventouraTextBodyGrey] CGColor];
//    self.textField.layer.borderColor = [[ventouraUtility ventouraTextBodyGrey] CGColor];
    
    [self.textField.layer setBorderColor:[[[UIColor grayColor] colorWithAlphaComponent:0.5] CGColor]];
    //The rounded corner part, where you specify your view's corner radius:
    self.textField.layer.cornerRadius = 5;
    self.textField.clipsToBounds = YES;
    NSLog(@"%@",self.aboutMeText);
    self.textField.text = [NSString stringWithFormat:@"%@",self.aboutMeText];
//    self.textField.contentInset = UIEdgeInsetsMake(-65,0,0,0);
    self.textField.delegate = self;
    
    //    frameRect.size.height = 100;
//    self.textField.frame = frameRect;
//    self.textField.numberOfLines = 0;

    self.limitLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
    self.limitLabel.textColor = [UIColor lightGrayColor];
    self.textField.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
    self.textField.textColor = [ventouraUtility ventouraTextBodyGrey];
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.sView.frame = CGRectMake(0, 0, 320, 568);
        [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, 568)];
    }else{
        self.sView.frame = CGRectMake(0, 0, 320, 480);
        [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, 480)];
    }
}

-(void)viewWillAppear:(BOOL)animated   {
    [super viewWillAppear:animated];
    [self.textField becomeFirstResponder];
    NSInteger charRemaining = ABOUT_ME_TEXT_LIMIT-self.textField.text.length;
    self.limitLabel.text  = [NSString stringWithFormat:@"%ld",charRemaining];

}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [self.delegate editAboutMeViewController:self didFinishEditAboutMe:self.textField.text];

}
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
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

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    NSLog(@"text Changed");
//    if([text length] == 0)
//    {
//        if([textView.text length] != 0)
//        {
//            return YES;
//        }
//        else {
//            return NO;
//        }
//    }
//    else if([[textView text] length] > ABOUT_ME_TEXT_LIMIT-1 )
//    {
//        return NO;
//    }
//    return YES;
    
    NSUInteger newLength = [textView.text length] + [text length] - range.length;
    return (newLength > ABOUT_ME_TEXT_LIMIT) ? NO : YES;
    
}
-(void)textViewDidChange:(UITextView *)textView
{
//    if ([realDelegate respondsToSelector:@selector(textViewDidChange:)])
//        [realDelegate textViewDidChange:textView];
    /*YOUR CODE HERE*/
    NSInteger charRemaining = ABOUT_ME_TEXT_LIMIT-self.textField.text.length;
    self.limitLabel.text  = [NSString stringWithFormat:@"%ld",charRemaining];
    NSLog(@"text Changed");
}

@end
