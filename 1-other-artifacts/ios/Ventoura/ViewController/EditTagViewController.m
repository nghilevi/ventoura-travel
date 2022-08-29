//
//  EditTagViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 25/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "EditTagViewController.h"

#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]
@interface EditTagViewController () <UITextFieldDelegate, UIAlertViewDelegate>

@property (weak, nonatomic) IBOutlet UITextField    *textField;
@property (weak, nonatomic) IBOutlet AMTagListView	*tagListView;
@property (nonatomic, strong) AMTagView             *selection;


@end

@implementation EditTagViewController

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
//    [self setTitle:@"Demo"];
	
//	[self.textField.layer setBorderColor:[ventouraUtility ventouraTextBodyGrey].CGColor];
    [self.textField.layer setBorderColor:[[[UIColor grayColor] colorWithAlphaComponent:0.5] CGColor]];

	[self.textField.layer setBorderWidth:1];
    self.textField.layer.cornerRadius = 5;
	[self.textField setDelegate:self];
    if (self.valueType ==1) {
        self.tags = [self.person.attractions mutableCopy];
    }else if (self.valueType ==2){
        self.tags = [self.person.localSecrets mutableCopy];
    }
    
	[[AMTagView appearance] setTagLength:10];
	[[AMTagView appearance] setTextPadding:14];
	[[AMTagView appearance] setTextFont:[UIFont fontWithName:@"Futura" size:16]];
	[[AMTagView appearance] setTagColor:UIColorFromRGB(0xe9716d)];
    for (NSInteger i = 0; i < self.tags.count; i++) {
        Attraction *attraction = self.tags[i];
        [self.tagListView addTag:attraction.attractionName tagId:attraction.attractionId];
    }
	__weak EditTagViewController* weakSelf = self;
	[self.tagListView setTapHandler:^(AMTagView *view) {
		weakSelf.selection = view;
        
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Delete"
														message:[NSString stringWithFormat:@"Delete %@?", [view tagText]]
													   delegate:weakSelf
											  cancelButtonTitle:@"Nope"
											  otherButtonTitles:@"Sure!", nil];
		[alert show];
	}];
    
    UIImage *backgroundImg = [UIImage imageNamed:@"DiamondBacking.png"];
    backgroundImg = [ventouraUtility imageWithImage:backgroundImg scaledToHeight:600] ;
    UIImageView * backgroundView= [[UIImageView alloc] initWithImage:backgroundImg];
    backgroundView.frame = CGRectMake(0,0, backgroundImg.size.width, backgroundImg.size.height);
    [self.view addSubview:backgroundView];
    [self.view sendSubviewToBack: backgroundView];
    self.textField.delegate = self;
    [self.textField addTarget:self
                       action:@selector(textFieldDidChange:)
             forControlEvents:UIControlEventEditingChanged];
//    CGRect screenRect = [[UIScreen mainScreen] bounds];
//    CGFloat screenHeight = screenRect.size.height;
//    if(screenHeight>567){
//        self.sView.frame = CGRectMake(0, 0, 320, 568);
//        [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, 568)];
//    }else{
//        self.sView.frame = CGRectMake(0, 0, 320, 480);
//        [self.sView setContentSize:CGSizeMake(self.view.frame.size.width, 480)];
//    }
    self.limitLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:15];
    self.limitLabel.textColor = [UIColor lightGrayColor];
    self.limitLabel.text= [NSString stringWithFormat:@"%d",TAG_TEXT_LIMIT];

}

- (void) viewWillDisappear:(BOOL)animated{
    NSLog(@"count %ld", [self.deletedArray count]);
    if (self.valueType ==1) {
        self.person.attractions  = self.tags;
    }else if (self.valueType ==2){
        self.person.localSecrets  = self.tags;
    }

}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex > 0) {
        int deleteIndex =-1;
        for (int i =0; i < self.tags.count; i++) {
            Attraction *a = self.tags[i];
            if ([[NSString stringWithFormat:@"%@",a.attractionId] isEqualToString:[NSString stringWithFormat:@"%@", self.selection.tagId]]) {
                deleteIndex =i;
            }
        }
        if (deleteIndex!= -1) {
            Attraction *a = self.tags[deleteIndex];
            if (a.isInMemory == NO) {
                [self.deletedArray addObject:a];
                NSLog(@"deleted Array %ld",self.deletedArray.count);
            }
            [self.tags removeObjectAtIndex:deleteIndex];
        }
        [self.tagListView removeTag:self.selection];
	}
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{   if(self.tags.count<4){
        [self.tagListView addTag:textField.text tagId:@"-1"];
        Attraction *tmp  = [[Attraction alloc] initWithAttractionId:nil ownerId:nil attractionName:textField.text isInMemory:YES];
        [self.tags addObject:tmp];
        [self.textField setText:@""];
        self.limitLabel.text= [NSString stringWithFormat:@"%d",TAG_TEXT_LIMIT];
        [self.textField resignFirstResponder];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"You have four attractions" message: @"Please delete an attraction before adding another." delegate: nil cancelButtonTitle:@"OK" otherButtonTitles:nil]; [alert show];
        [self.textField resignFirstResponder];
    }
	return YES;
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
// Close the keyboard when the user taps away froma  textfield
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    for (UIView* view in self.view.subviews) {
        if ([view isKindOfClass:[UITextField class]])
			[view resignFirstResponder];
    }
}

//- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
//{
//    NSLog(@"text Changed");
//    //    if([text length] == 0)
//    //    {
//    //        if([textView.text length] != 0)
//    //        {
//    //            return YES;
//    //        }
//    //        else {
//    //            return NO;
//    //        }
//    //    }
//    //    else if([[textView text] length] > ABOUT_ME_TEXT_LIMIT-1 )
//    //    {
//    //        return NO;
//    //    }
//    //    return YES;
//    
//    NSUInteger newLength = [textView.text length] + [text length] - range.length;
//    return (newLength > TAG_TEXT_LIMIT) ? NO : YES;
//    
//}
-(BOOL) textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{

    NSUInteger newLength = [textField.text length] + [string length] - range.length;
    return (newLength > TAG_TEXT_LIMIT) ? NO : YES;
}
-(void)textFieldDidChange:(UITextView *)textView{
    NSInteger charRemaining = TAG_TEXT_LIMIT-self.textField.text.length;
    self.limitLabel.text  = [NSString stringWithFormat:@"%ld",charRemaining];
    NSLog(@"text Changed");
}
//-(void)textViewDidChange:(UITextView *)textView
//{
//    //    if ([realDelegate respondsToSelector:@selector(textViewDidChange:)])
//    //        [realDelegate textViewDidChange:textView];
//    /*YOUR CODE HERE*/
//    
//}

@end
