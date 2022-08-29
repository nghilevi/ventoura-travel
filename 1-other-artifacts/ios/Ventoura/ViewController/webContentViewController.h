//
//  webContentViewController.h
//  Ventoura
//
//  Created by Wenchao Chen on 1/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface webContentViewController : UIViewController
@property (nonatomic, strong) NSString *target;
@property (nonatomic, strong) IBOutlet UIWebView *webView;

@end
