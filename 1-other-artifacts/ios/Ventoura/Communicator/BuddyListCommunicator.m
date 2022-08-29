//
//  VentouraPakcageCommunicator.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import "BuddyListCommunicator.h"
#import "BuddyListCommunicatorDelegate.h"
#include "Person.h"
@implementation BuddyListCommunicator
- (void)getBuddyList{
    NSString *urlAsString;
    if ([ventouraUtility isUserGuide]) {
        urlAsString= [NSString stringWithFormat:@"%@guide/getAllMatches/%@",ventouraBaseURL,[ventouraUtility returnMyUserId]];

    }else{
        urlAsString= [NSString stringWithFormat:@"%@traveller/getAllMatches/%@",ventouraBaseURL,[ventouraUtility returnMyUserId]];
    }
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" get match url string %@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        if (error) {
            [self.delegate fetchingBuddyListJSONWithError:error];
        } else {
            [self.delegate receivedBuddyJSON:data];
        }
    }];
}


-(void)deleteBuddyByUserId:(NSString *)userId isUserGuide:(bool)isUserGuide{
    NSString *urlAsString;
    if (![ventouraUtility isUserGuide]) {
        urlAsString= [NSString stringWithFormat:@"%@traveller/match",ventouraBaseURL];
        if (isUserGuide) {
            urlAsString = [NSString stringWithFormat:@"%@/deleteGuideMatch/%@/%@/",urlAsString,[ventouraUtility returnMyUserId],userId];
        }else{
            urlAsString = [NSString stringWithFormat:@"%@/deleteTravellerMatch/%@/%@/",urlAsString,[ventouraUtility returnMyUserId],userId];
        }
    }else{
        urlAsString= [NSString stringWithFormat:@"%@guide/match/deleteTravellerMatch/%@/%@",ventouraBaseURL,[ventouraUtility returnMyUserId],userId];
    }
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" delete match url string %@", urlAsString);

    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        if (error) {
            [self.delegate fetchingBuddyListJSONWithError:error];
        } else {
            [self.delegate receivedDeleteBuddyJSON:data userId:userId isUserGuide:isUserGuide];
        }
    }];


}

@end
