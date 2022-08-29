//
//  EditTourDetailViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 21/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "EditTourDetailViewController.h"

@interface EditTourDetailViewController (){
}

@end

@implementation EditTourDetailViewController
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
    self.textField.text = [NSString stringWithFormat:@"%@",self.textValue];
    // Do any additional setup after loading the view.
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
//    _collectionView.backgroundColor = [UIColor clearColor];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
        self.view.frame = CGRectMake(0, 0, 320, IPHONE5SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }else{
        self.view.frame = CGRectMake(0, 0, 320, IPHONE4SCREENHEIGHT-IPHONENAVBARHEIGHT);
    }
    self.view.backgroundColor = [UIColor clearColor];
    
    self.textField.delegate = self;
    self.textLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
    self.textLabel.textColor = [ventouraUtility ventouraTextBodyGrey];
    self.textField.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
    self.textField.textColor = [UIColor lightGrayColor];
    self.textField.keyboardType = UIKeyboardTypeDecimalPad;

}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear: animated];
    [self.textField becomeFirstResponder];
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [self.delegate editTourDetailViewController:self didFinishEditTourDetail:self.textField.text type:self.valueType];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSString *newString = [textField.text stringByReplacingCharactersInRange:range withString:string];
    
    NSArray *sep = [newString componentsSeparatedByString:@"."];
    if([sep count] >= 2)
    {
        NSString *sepStr=[NSString stringWithFormat:@"%@",[sep objectAtIndex:1]];
        return !([sepStr length]>1);
    }
//    return YES;
    NSCharacterSet *nonNumberSet = [[NSCharacterSet characterSetWithCharactersInString:@"0123456789."] invertedSet];
    
    // allow backspace
    if (range.length & 0 && [string length] == 0) {
        return YES;
    }
    // do not allow . at the beggining
    if (range.location == 0 && [string isEqualToString:@"."]) {
        return NO;
    }
    // set the text field value manually
    NSString *newValue = [[textField text] stringByReplacingCharactersInRange:range withString:string];
    newValue = [[newValue componentsSeparatedByCharactersInSet:nonNumberSet] componentsJoinedByString:@""];
    textField.text = newValue;
    // return NO because we're manually setting the value
    return NO;
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

@end
