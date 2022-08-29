//
//  webContentViewController.m
//  Ventoura
//
//  Created by Wenchao Chen on 1/12/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "webContentViewController.h"

@interface webContentViewController ()

@end

@implementation webContentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"Target: %@", self.target);
    // Do any additional setup after loading the view.
//    NSURL *url = [NSURL fileURLWithPath:[[NSBundle mainBundle]pathForResource:self.target ofType:nil]];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.target]]];
//    NSURLRequest *request = [NSURLRequest requestWithURL:url];
//    [self.webView loadRequest:request];
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

@end
