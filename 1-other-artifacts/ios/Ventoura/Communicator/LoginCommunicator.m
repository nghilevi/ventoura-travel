//
//  TravellerCommunicator
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "LoginCommunicator.h"
#import "LoginCommunicatorDelegate.h"
#import "constants.h"

@implementation LoginCommunicator
- (void)searchTravellerById:(NSString *)travellerId
{
    NSLog(@"start Fetch");
    
    NSString *urlAsString ;
    urlAsString= [NSString stringWithFormat:@"%@/traveller/getTravellerProfile/%@", ventouraBaseURL, travellerId];
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@"%@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingPersonJSONFailedWithError:error];
        } else {
            [self.delegate receivedTravellerJSON:data];
        }
    }];
}
-(void) searchUserByFacebookId:(NSString *)facebookId isGuide:(BOOL)isGuide{
    NSString *urlAsString;
    NSLog(@"isGuide? %d", isGuide);//save

    if(isGuide) {
        urlAsString = [NSString stringWithFormat:@"%@/system/login/guide/%@", ventouraBaseURL, facebookId];
    }else{
        urlAsString = [NSString stringWithFormat:@"%@/system/login/traveller/%@", ventouraBaseURL, facebookId];

    }
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        if (error) {
            [self.delegate fetchingPersonJSONFailedWithError:error];
        } else {
            [self.delegate receivedPersonLoginJSON:data];
        }
    }];

}

-(void) postPersonFacebookID:(id<FBGraphUser>)fbUser payByCard:(BOOL)payByCard country:(NSString*)country{
    // Dictionary that holds post parameters. You can set your post parameters that your server accepts or programmed to accept.
    
    NSString *dateStr =[fbUser objectForKey:@"birthday"];
    
    // Convert string to date object
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];

    [dateFormat setDateFormat:@"MM/dd/yyyy"];
    NSDate *date = [dateFormat dateFromString:dateStr];
    NSString* birthday = [ventouraUtility ventouraDateToStringForProfilePost:date];
    
    
    NSMutableDictionary* _params = [[NSMutableDictionary alloc] init];
    [_params setObject:fbUser.objectID forKey:@"facebookAccountName"];


    
    
    [_params setObject:[NSString stringWithFormat:@"%@ 00:00:00",birthday] forKey:@"dateOfBirth"];
    [_params setObject:country forKey:@"country"];
    [_params setObject:@"0" forKey:@"city"];
    [_params setObject:@"0" forKey:@"tourPrice"];
    [_params setObject:@"0" forKey:@"tourLength"];
    if (payByCard) {
        [_params setObject:@"1" forKey:@"paymentMethod"];
    }else{
        [_params setObject:@"0" forKey:@"paymentMethod"];
    }


    [_params setObject:@"" forKey:@"textBiography"];
    [_params setObject:[fbUser objectForKey:@"gender"]forKey:@"gender"];
    [_params setObject:[fbUser objectForKey:@"first_name"] forKey:@"guideFirstname"];
    [_params setObject:[fbUser objectForKey:@"last_name"] forKey:@"guideLastname"];
    
    
    
    // the boundary string : a random string, that will not repeat in post data, to separate post data fields.
    NSString *BoundaryConstant = @"----------V2ymHFg03ehbqgZCaKO6jy";
    
    // string constant for the post parameter 'file'. My server uses this name: `file`. Your's may differ
    NSString* FileParamConstant = @"portalPhoto";
    
    // the server url to which the image (or the media) is uploaded. Use your server url here
    NSURL* requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@guide/createNewGuide", ventouraBaseURL]];
    
    
    NSLog(@"URL:  %@",[NSString stringWithFormat:@"%@guide/createNewGuide", ventouraBaseURL]);

    
    // create request
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [request setHTTPShouldHandleCookies:NO];
    [request setTimeoutInterval:30];
    [request setHTTPMethod:@"POST"];
    
    // set Content-Type in HTTP header
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", BoundaryConstant];
    [request setValue:contentType forHTTPHeaderField: @"Content-Type"];
    
    // post body
    NSMutableData *body = [NSMutableData data];
    
    // add params (all params are strings)
    for (NSString *param in _params) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n", param] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"%@\r\n", [_params objectForKey:param]] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    //change the image as well.:)
    // UIImage *imageToPost = [UIImage imageNamed:@"testpro.jpg"];
    UIImage *imageToPost = _fbUIImage;
    // add image data
    NSData *imageData = UIImageJPEGRepresentation(imageToPost, 1.0);
    if (imageData) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"; filename=\"image.jpg\"\r\n", FileParamConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[@"Content-Type: image/jpeg\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:imageData];
        [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // setting the body of the post to the reqeust
    [request setHTTPBody:body];
    
    // set the content-length
    NSString *postLength = [NSString stringWithFormat:@"%ld", [body length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    
    // set URL
    [request setURL:requestURL];
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
    
        if (error) {
            [self.delegate fetchingPersonJSONFailedWithError:error];
        } else {
            [self.delegate receivedTravellerCreateJSON:data];
        }
    }];
    
    

    

}
-(void) postPersonFacebookID:(id<FBGraphUser>) fbUser isGuide:(BOOL)isGuide country:(NSString*)country{
    
    NSString *dateStr =[fbUser objectForKey:@"birthday"];
//    NSLog(@"birthday from FB: %@", dateStr);

    // Convert string to date object
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    
    [dateFormat setDateFormat:@"MM/dd/yyyy"];
    NSDate *date = [dateFormat dateFromString:dateStr];
    NSString* birthday = [ventouraUtility ventouraDateToStringForProfilePost:date];
    
    
//    NSLog(@"after parse %@,", birthday);
    
    NSMutableDictionary* _params = [[NSMutableDictionary alloc] init];
    [_params setObject:fbUser.objectID forKey:@"facebookAccountName"];
    [_params setObject:[NSString stringWithFormat:@"%@ 00:00:00",birthday] forKey:@"dateOfBirth"];
    [_params setObject:country forKey:@"country"];
    [_params setObject:@"0" forKey:@"city"];
    [_params setObject:@"" forKey:@"textBiography"];


    [_params setObject:[fbUser objectForKey:@"gender"]forKey:@"gender"];
    [_params setObject:[fbUser objectForKey:@"first_name"] forKey:@"travellerFirstname"];
    [_params setObject:[fbUser objectForKey:@"last_name"] forKey:@"travellerLastname"];

    
    
    // the boundary string : a random string, that will not repeat in post data, to separate post data fields.
    NSString *BoundaryConstant = @"----------V2ymHFg03ehbqgZCaKO6jy";
    
    // string constant for the post parameter 'file'. My server uses this name: `file`. Your's may differ
    NSString* FileParamConstant = @"portalPhoto";
    
    // the server url to which the image (or the media) is uploaded. Use your server url here
    NSURL* requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@traveller/createNewTraveller", ventouraBaseURL]];
    
    
    
    // create request
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [request setHTTPShouldHandleCookies:NO];
    [request setTimeoutInterval:30];
    [request setHTTPMethod:@"POST"];
    
    // set Content-Type in HTTP header
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", BoundaryConstant];
    [request setValue:contentType forHTTPHeaderField: @"Content-Type"];
    
    // post body
    NSMutableData *body = [NSMutableData data];
    
    // add params (all params are strings)
    for (NSString *param in _params) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n", param] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"%@\r\n", [_params objectForKey:param]] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    //change the image as well.:)
   // UIImage *imageToPost = [UIImage imageNamed:@"testpro.jpg"];
    UIImage *imageToPost = _fbUIImage;
    // add image data
    NSData *imageData = UIImageJPEGRepresentation(imageToPost, 1.0);
    if (imageData) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"; filename=\"image.jpg\"\r\n", FileParamConstant] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[@"Content-Type: image/jpeg\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:imageData];
        [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // setting the body of the post to the reqeust
    [request setHTTPBody:body];
    
    // set the content-length
    NSString *postLength = [NSString stringWithFormat:@"%ld", [body length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    
    // set URL
    [request setURL:requestURL];

    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingPersonJSONFailedWithError:error];
        } else {
            [self.delegate receivedTravellerCreateJSON:data];
        }
    }];
    
   
}


-(void) searchUserByFacebookIdPost:(NSString *)facebookId isGuide:(BOOL)isGuide{
    NSString *urlAsString;
    NSString *post = @"";
    post = [post stringByAppendingString:[NSString stringWithFormat:@"deviceToken=%@&osType=%@",_deviceToken,@"0"]];

    
    if(isGuide) {
        urlAsString = [NSString stringWithFormat:@"%@/system/login/guide/%@", ventouraBaseURL, facebookId];
    }else{
        urlAsString = [NSString stringWithFormat:@"%@/system/login/traveller/%@", ventouraBaseURL, facebookId];
        
    }
    NSLog(@"HMMM THIS IS POSTTING DEVICES STUFF? %@", _deviceToken);//save

    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
//    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    
    [request setURL:[NSURL URLWithString:urlAsString]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];

    
    
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {

        NSLog(@"response for post ");
        NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
//        NSDictionary *dic = [httpResponse allHeaderFields];
        NSLog(@"response content: %@",httpResponse);
        if (error) {
            [self.delegate fetchingPersonJSONFailedWithError:error];
        } else {
            [self.delegate receivedTokenPostJSON:data];
        }
    }];
    
}



@end
