//
//  VentouraPakcageCommunicator.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import "PromotionCommunicator.h"

@implementation PromotionCommunicator

-(void)getPromotionStatus:(NSString *)userId{
    // need to pass in string for user id and user type
    NSLog(@"fetching start json");
    NSString *urlAsString;
    urlAsString= [NSString stringWithFormat:@"%@/promotion/promotionProbe/%@",ventouraBaseURL,userId];
    
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" profile get url %@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        NSLog(@"response for post ");
   
        
        if (error) {
            [self.delegate fetchingPromotionJSONFailedWithError:error];
        } else {
            [self.delegate receivedPromotionStatus:data response:response];
        }
    }];


}

-(void)postCity1:(NSString *)city1 withCity2:(NSString *)city2 withCity3:(NSString *)city3 withCity4:(NSString *)city4{
    NSLog(@"start");
    NSString *post = [NSString stringWithFormat:@"city_0=%@&city_1=%@&city_2=%@&city_3=%@",city1,city2,city3,city4];
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/promotion/getPromotionImage/1", ventouraBaseURL]
                     ]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        NSLog(@"response for post ");
        NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
        NSDictionary *dic = [httpResponse allHeaderFields];
        NSLog(@"response content: %@",dic);
        if (error) {
            [self.delegate fetchingPromotionJSONFailedWithError:error];
        } else {
            [self.delegate receivedPromotionImageData:data];
        }
    }];
}

-(void)postEnterPromotionCity1:(NSString *)city1 withCity2:(NSString *)city2 withCity3:(NSString *)city3 withCity4:(NSString *)city4{
    NSLog(@"star Promotion Enter");
    NSString *post = [NSString stringWithFormat:@"city_0=%@&city_1=%@&city_2=%@&city_3=%@",city1,city2,city3,city4];
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/promotion/addNewCandidate/%@", ventouraBaseURL,[ventouraUtility returnMyUserId]]
                     ]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        NSLog(@"response for post ");
        NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
        NSDictionary *dic = [httpResponse allHeaderFields];
        NSLog(@"response content: %@",dic);
        if (error) {
            [self.delegate fetchingPromotionJSONFailedWithError:error];
        } else {
            [self.delegate receivedEnterPromotionData:data];
        }
    }];



}

@end
