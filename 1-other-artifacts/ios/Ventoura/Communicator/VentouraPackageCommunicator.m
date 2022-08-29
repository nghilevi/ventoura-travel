//
//  VentouraPakcageCommunicator.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import "VentouraPackageCommunicator.h"
#import "VentouraPackageCommunicatorDelegate.h"
#include "Person.h"
@implementation VentouraPackageCommunicator
- (void)getLikeOrNotResult:(Person *)currentPerson likeOrNot:(BOOL)likeOrNot{
    NSLog(@"like or not communicator");
    NSString *currentUserVentouraID =[ventouraUtility returnMyUserId];

    //build a string.
//    NSLog(@"current user id : %@ and isGuide %d",  @"3", [[NSUserDefaults standardUserDefaults] boolForKey:@"userIsGuide"]);
//    NSLog(@"current personid: %@ and type %@", currentPerson.ventouraId, currentPerson.userRole);
    
    NSString *baseURL = [NSString stringWithFormat:@"%@ventoura/", ventouraBaseURL];
    if ([ventouraUtility isUserGuide]) {
        baseURL = [NSString stringWithFormat:@"%@guide/",baseURL];
    }else{
        baseURL = [NSString stringWithFormat:@"%@traveller/",baseURL];
    }
    if ([ventouraUtility isUserGuide:currentPerson.userRole]) {
        baseURL = [NSString stringWithFormat:@"%@judgeGuide/",baseURL];
    }else{
        baseURL = [NSString stringWithFormat:@"%@judgeTraveller/",baseURL];
    }
    baseURL = [NSString stringWithFormat:@"%@%@/%d/%@",baseURL,currentUserVentouraID,likeOrNot,currentPerson.ventouraId];

    
    NSLog(@"url: %@", baseURL);
    NSURL *url = [[NSURL alloc] initWithString:baseURL];
//    NSLog(@"%@", urlAsString);
//
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingPackageJSONFailedWithError:error];
        } else {
            [self.delegate receivedLikeOrNotJSON:data withName:currentPerson.firstName];
        }
    }];

}
- (void)getVentouraPackage
{
    NSString *urlAsString;
    if ([ventouraUtility isUserGuide]) {
        urlAsString = [NSString stringWithFormat:@"%@ventoura/guide/getVentouraPackage/%@",ventouraBaseURL, [ventouraUtility returnMyUserId]];
    }else{
        urlAsString = [NSString stringWithFormat:@"%@ventoura/traveller/getVentouraPackage/%@",ventouraBaseURL, [ventouraUtility returnMyUserId]];
    }
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];

    NSLog(@"%@", urlAsString);
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];

    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];

            [self.delegate fetchingPackageJSONFailedWithError:error];
        } else {
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
            [self.delegate receivedPackageJSON:data];
        }
    }];
}




- (void)getVentouraPackageImages:(NSArray *) ventouraPackage
{
    
    NSString *post = @"";
    for (Person *person in ventouraPackage) {
        if ([ventouraUtility isUserGuide:person.userRole]) {
            NSLog(@"g_%@",person.ventouraId);
            post = [post stringByAppendingString:[NSString stringWithFormat:@"g_%@=g_%@&",person.ventouraId,person.ventouraId]];
        }else{
            NSLog(@"t_%@",person.ventouraId);
            post = [post stringByAppendingString:[NSString stringWithFormat:@"t_%@=t_%@&",person.ventouraId,person.ventouraId]];
        }
    }
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    
    [request setURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@ventoura/loadVentouraImages",ventouraBaseURL]]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];

    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        NSLog(@"response for post ");
        NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
        NSDictionary *dic = [httpResponse allHeaderFields];
        NSLog(@"response content: %@",dic);
        if (error) {
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];

            [self.delegate fetchingPackageJSONFailedWithError:error];
        } else {
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];

            [self.delegate receivedPackageImageZip:data packageWithNoImage:ventouraPackage];
        }
    }];
}


@end
