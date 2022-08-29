//
//  PromotionPreviewViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 10/10/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "PromotionPreviewViewController.h"

@interface PromotionPreviewViewController ()<PromotionManagerDelegate,MBProgressHUDDelegate>{
    PromotionManager *_manager;
    MBProgressHUD *HUD;

}
@property (nonatomic, strong) DBManager *dbManager;

@end

@implementation PromotionPreviewViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    // Do any additional setup after loading the view.
    _manager = [[PromotionManager alloc] init];
    _manager.communicator =[[PromotionCommunicator alloc] init];
    _manager.communicator.delegate = _manager;
    _manager.delegate = self;
    
    [self.tickBox addTarget:self action:@selector(buttonTouch:withEvent:) forControlEvents:UIControlEventTouchUpInside];
    self.tickBox.selected = YES;
    
    self.tnc.titleLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:11];
    NSMutableAttributedString *titleString = [[NSMutableAttributedString alloc] initWithString:self.tnc.titleLabel.text];
    
    // making text property to underline text-
    [titleString addAttribute:NSUnderlineStyleAttributeName value:[NSNumber numberWithInteger:NSUnderlineStyleSingle] range:NSMakeRange(0, [titleString length])];
    self.tnc.titleLabel.textColor = [ventouraUtility ventouraPromotionGrey];

    // using text on button
    [self.tnc setAttributedTitle: titleString forState:UIControlStateNormal];
    
    
    
    
    self.agreeLabel.font = [UIFont fontWithName:@"Roboto-Regular" size:11];
    self.agreeLabel.textColor = [ventouraUtility ventouraPromotionGrey];

//    NSLog(@"hello init %@",self.city1);
    [_manager postCityId:self.city1 withCity2:self.city2 withCity3:self.city3 withCity4:self.city4];
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    if(screenHeight>567){
    }else{
        self.scrollView.frame = CGRectMake(0, 0, 320, 480);
    }
    
    // Do any additional setup after loading the view.
    //    self.city1.frame = CGRectMake(self.city1.frame.origin.x, self.city1.frame.origin.y, 50, 50);
    [self.scrollView setContentSize:CGSizeMake(self.view.frame.size.width, 600)];
    NSLog(@"end of View Did Load");
    self.dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
    
//    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    HUD.customView = [ventouraUtility returnLoadingAnimation];
    HUD.mode = MBProgressHUDModeCustomView;
    HUD.delegate = self;
    [HUD show:YES];

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
-(void) didReceivePromotionImage:(UIImage *)image{
    NSLog(@"hmm");
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            
            self.promotionImage.image = image;
        });
    });

}

-(void) didReceivePromotionEnter{
    NSLog(@"enetered");
    //save data flag. pop view. modify view will appear on the next page
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
    
    if (self.promotionImage.image != nil)
    {
        NSString* path = [destinationPath stringByAppendingPathComponent:[NSString stringWithFormat:@"promotion1_%@.png",folderName]];
        NSData* data = UIImagePNGRepresentation(self.promotionImage.image);
        [data writeToFile:path atomically:YES];
    }
    
    
    NSString *query = [NSString stringWithFormat:@"insert into Promotion values(null, 1, '%@')", [ventouraUtility returnMyUserId]];
    
    // Execute the query.
    [self.dbManager executeQuery:query];
    
    // If the query was successfully executed then pop the view controller.
    if (self.dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", self.dbManager.affectedRows);
        
        // Pop the view controller.
        // [self.navigationController popViewControllerAnimated:YES];
    }
    else{
        NSLog(@"Could not execute the query.");
    }
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        dispatch_sync(dispatch_get_main_queue(), ^{
            [self.navigationController popViewControllerAnimated:YES];

        });
    });
    
//    [self dismissViewControllerAnimated:YES completion:nil];
    
}
-(void) fetchingPromotioinFailedWithError:(NSError *)error{
    
  }


- (void)buttonTouch:(UIButton *)aButton withEvent:(UIEvent *)event
{
    self.tickBox.selected = !self.tickBox.selected;
}
- (IBAction)shareOGStory:(id)sender
{
    
    if(self.tickBox.selected){
        
        if (FBSession.activeSession.isOpen) {
            NSLog(@"open");
            [self checkFBPerm];
        }else{
            NSLog(@"not open");
            [self connectWithFacebook];
        }
    }else{
        //alert
        NSLog(@"please accpet tnc");
    }
    
//    [self postPromotion];


}

- (void)checkFBPerm{
    // Check for publish permissions
    [FBRequestConnection startWithGraphPath:@"/me/permissions"
                          completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
                              if (!error){
                                  NSDictionary *permissions= [(NSArray *)[result data] objectAtIndex:0];
                                  if (![permissions objectForKey:@"publish_actions"]){
                                      // Permission hasn't been granted, so ask for publish_actions
                                      [FBSession.activeSession requestNewPublishPermissions:[NSArray arrayWithObject:@"publish_actions"]
                                                                            defaultAudience:FBSessionDefaultAudienceFriends
                                                                          completionHandler:^(FBSession *session, NSError *error) {
                                                                              if (!error) {
                                                                                  if ([FBSession.activeSession.permissions indexOfObject:@"publish_actions"] == NSNotFound){
                                                                                      // Permission not granted, tell the user we will not share to Facebook
                                                                                      NSLog(@"Permission not granted, we will not share to Facebook.");
                                                                                      
                                                                                  } else {
                                                                                      // Permission granted, publish the OG story
                                                                                      [self postPromotion];
                                                                                  }
                                                                                  
                                                                              } else {
                                                                                  // An error occurred, we need to handle the error
                                                                                  // See: https://developers.facebook.com/docs/ios/errors
                                                                                  NSLog(@"Encountered an error requesting permissions: %@", error.description);
                                                                              }
                                                                          }];
                                      
                                  } else {
                                      // Permissions present, publish the OG story
                                      [self postPromotion];
                                  }
                                  
                              } else {
                                  // An error occurred, we need to handle the error
                                  // See: https://developers.facebook.com/docs/ios/errors
                                  NSLog(@"Encountered an error checking permissions: %@", error.description);
                              }
                          }];

}

- (void)postPromotion{
    
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];

    // Get the image
    UIImage* image = self.promotionImage.image;
//    [self dismissViewControllerAnimated:YES completion:nil];
    [FBRequestConnection startForUploadStagingResourceWithImage:image completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
        if(!error) {
            // instantiate a Facebook Open Graph object
            NSMutableDictionary<FBOpenGraphObject> *object = [FBGraphObject openGraphObjectForPost];
            
            // specify that this Open Graph object will be posted to Facebook
            object.provisionedForPost = NO;
            
            // for og:title
            object[@"title"] = @"Venoutra Promotion";
            
            // for og:type, this corresponds to the Namespace you've set for your app and the object type name
            object[@"type"] = @"ventoura:promotion";
            
            // for og:description
            object[@"description"] = @"Test";
            
            // for og:url, we cover how this is used in the "Deep Linking" section below
            object[@"url"] = @"http://google.com";
            //            object[@"fb:explicitly_shared"] = @"true";
            
            // for og:image we assign the image that we just staged, using the uri we got as a response
            // the image has to be packed in a dictionary like this:
            object[@"image"] = @[@{@"url": [result objectForKey:@"uri"], @"user_generated" : @"true" }];
            
            // Post custom object
            [FBRequestConnection startForPostOpenGraphObject:object completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
                if(!error) {
                    // get the object ID for the Open Graph object that is now stored in the Object API
                    NSString *objectId = [result objectForKey:@"id"];
                    NSLog(@"object id done");
                    
                    
                    // create an Open Graph action
                    id<FBOpenGraphAction> action = (id<FBOpenGraphAction>)[FBGraphObject graphObject];
                    [action setObject:objectId forKey:@"promotion"];
                    [action setObject: @"true" forKey: @"fb:explicitly_shared"];  // This is the key point!
                    
                    //                     create action referencing user owned object
                    [FBRequestConnection startForPostWithGraphPath:@"/me/ventoura:participate" graphObject:action completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
                        if(!error) {
                            NSLog(@"OG story posted, story id:");
                            [MBProgressHUD hideHUDForView:self.view animated:YES];
                            //post participate code and pop view controller
                            [_manager postEnterPromotionWithCityId:self.city1 withCity2:self.city2 withCity3:self.city3 withCity4:self.city4];


                        } else {
                            // An error occurred
                            NSLog(@"Encountered an error posting to Open Graph: %@", error);
                        }
                    }];
                    // Further code to post the OG story goes here
                    
                } else {
                    // An error occurred
                    NSLog(@"Error posting the Open Graph object to the Object API: %@", error);
                }
            }];
            
            
        }
    }];
    
    

}
- (IBAction)tncPressed{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString: @"http://www.google.com"]];

}


// A function for parsing URL parameters.
- (NSDictionary*)parseURLParams:(NSString *)query {
    NSArray *pairs = [query componentsSeparatedByString:@"&"];
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    for (NSString *pair in pairs) {
        NSArray *kv = [pair componentsSeparatedByString:@"="];
        NSString *val =
        [kv[1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        params[kv[0]] = val;
    }
    return params;
}

- (void) connectWithFacebook {
    // The user has initiated a login, so call the openSession method
    // and show the login UI if necessary << Only if user has never
    // logged in or ir requesting new permissions.
//    PPAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
//    [appDelegate openSessionWithAllowLoginUI:YES];
    NSLog(@"connecting with fb");

    NSArray *permissions = @[@"public_profile", @"email",@"publish_actions"];
    [FBSession openActiveSessionWithReadPermissions:permissions
                                       allowLoginUI:YES
                                  completionHandler:^(FBSession *session, FBSessionState state, NSError *error) {
                                      if (error) {
                                          NSLog (@"Handle error %@", error.localizedDescription);
                                      } else {
                                            NSLog(@"connecting with fb complete");
                                            [self postPromotion];
                                          
                                      }
                                  }];
}

//- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI
//{
//    NSArray *permissions = @[@"any_READ_permision_you_may_need"];
//    
//    return [FBSession openActiveSessionWithReadPermissions:permissions
//                                              allowLoginUI:allowLoginUI
//                                         completionHandler:^(FBSession *session, FBSessionState state, NSError *error) {
//                                             if (error) {
//                                                 NSLog (@"Handle error %@", error.localizedDescription);
//                                             } else {
//                                                 [self checkSessionState:state];
//                                             }
//                                         }];
//}
@end
