//
//  VentouraPakcageCommunicator.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//
#import "ProfileCommunicator.h"

@implementation ProfileCommunicator

-(void)postDeleteGuideAttractions:(NSArray *)attractionsArrDeleted{
    NSString *post = @"";
    for (Attraction *attr in attractionsArrDeleted) {
            post = [post stringByAppendingString:[NSString stringWithFormat:@"%@=%@&",attr.attractionId,attr.attractionId]];
    }
    NSLog(@"post: %@", post);
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    
    [request setURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@guide/deleteBatchGuideAttraction",ventouraBaseURL]]];
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
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            NSLog(@"delete done");
            [self.delegate receivedPostGuideDeleteAttractions];
        }
    }];
    
    

}



-(void)postGuideAttractions:(NSArray *)attractionsArrDisplay{
    
    NSString *post = @"";
    for (Attraction *attr in attractionsArrDisplay) {
        if (attr.isInMemory) {
            post = [post stringByAppendingString:[NSString stringWithFormat:@"%@=%@&",attr.attractionName,attr.attractionName]];
        }
        
    }
    NSLog(@"post: %@", post);
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    
    [request setURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/guide/batchCreateGuideAttraction/%@",ventouraBaseURL,[ventouraUtility returnMyUserId]]]];
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
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            NSLog(@"delete done");
            [self.delegate receivedPostGuideUpdateAttractions];
        }
    }];
    

}

-(void)postDeleteUserImages:(NSArray *)deletedImages isUserGuide:(BOOL)isUserGuide{

    NSString *post = @"";
    for (UserImage *img in deletedImages) {
        NSLog(@"imgs: %@",img.imageId);
        post = [post stringByAppendingString:[NSString stringWithFormat:@"%@=%@&",img.imageId,img.imageId]];
    }
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%ld", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    
    NSString *urlAsString;
    if(isUserGuide){
        urlAsString= [NSString stringWithFormat:@"%@guide/deleteBatchGallery",ventouraBaseURL];
    }else{
        urlAsString= [NSString stringWithFormat:@"%@traveller/deleteBatchGallery",ventouraBaseURL];
    }
    NSLog(@"Delete Portal Image URL %@", urlAsString);

    
    [request setURL:[NSURL URLWithString:urlAsString]];
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
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            NSLog(@"delete done");
            [self.delegate receivedPostDeleteUserImages];
        }
    }];


}


-(void) postImage:(BOOL)isGuide ventouraId:(NSString *)ventouraId image:(UIImage *)image isPortal:(BOOL)isPortal{

    NSMutableDictionary* _params = [[NSMutableDictionary alloc] init];
    if (isGuide) {
        NSLog(@"guide id");
        [_params setObject:ventouraId forKey:@"guideId"];
    }else{
        NSLog(@"t id");

        [_params setObject:ventouraId forKey:@"travellerId"];
    }
 
    // the boundary string : a random string, that will not repeat in post data, to separate post data fields.
    NSString *BoundaryConstant = @"----------V2ymHFg03ehbqgZCaKO6jy";
    
    // string constant for the post parameter 'file'. My server uses this name: `file`. Your's may differ
    NSString* FileParamConstant = @"photo_1";
    
    // the server url to which the image (or the media) is uploaded. Use your server url here
    NSURL* requestURL;
    if (isGuide) {
        requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@guide/uploadSingleGuideGallery", ventouraBaseURL]];
    }else{
        requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@traveller/uploadSingleTravellerGallery", ventouraBaseURL]];
    }

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
    UIImage *imageToPost = image;
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
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            NSLog(@"Image Uploaded, Json Returned");
            [self.delegate receivedImageUplpoadJSON:data imageData:image isPortal:isPortal];

        }
    }];

}

-(void)postGuideProfileUpdate:(NSString *)ventouraId textBiography:(NSString *)textBiography tourLength:(NSString *)tourLength tourPrice:(NSString *)tourPrice city:(NSString *)city country:(NSString *)country tourType:(NSString *)tourType{
    
    NSMutableDictionary* _params = [[NSMutableDictionary alloc] init];
    [_params setObject:ventouraId forKey:@"guideId"];
    [_params setObject:textBiography forKey:@"textBiography"];
    [_params setObject:tourPrice forKey:@"tourPrice"];
    [_params setObject:tourLength forKey:@"tourLength"];
    [_params setObject:city forKey:@"city"];
    [_params setObject:country forKey:@"country"];
    [_params setObject:tourType forKey:@"tourType"];

    // the boundary string : a random string, that will not repeat in post data, to separate post data fields.
    NSString *BoundaryConstant = @"----------V2ymHFg03ehbqgZCaKO6jy";
    
    // the server url to which the image (or the media) is uploaded. Use your server url here
    NSURL* requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@guide/updateGuide", ventouraBaseURL]];
    
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
    
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
    [request setHTTPBody:body];
    
    // set the content-length
    NSString *postLength = [NSString stringWithFormat:@"%ld", [body length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setURL:requestURL];
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            NSLog(@"done!");
            [self.delegate receivedPostUpdateGuideProfile];
        }
    }];


}
- (void)postProfileUpdate:(NSString *)ventouraId textBiography:(NSString *)textBiography country:(NSString *)country{

    NSMutableDictionary* _params = [[NSMutableDictionary alloc] init];
    [_params setObject:ventouraId forKey:@"travellerId"];
    [_params setObject:textBiography forKey:@"textBiography"];
    if (!country) {
        country = @"";
    }
    [_params setObject:country forKey:@"country"];

    // the boundary string : a random string, that will not repeat in post data, to separate post data fields.
    NSString *BoundaryConstant = @"----------V2ymHFg03ehbqgZCaKO6jy";

    // the server url to which the image (or the media) is uploaded. Use your server url here
    NSURL* requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@traveller/updateTraveller", ventouraBaseURL]];

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
   
   [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", BoundaryConstant] dataUsingEncoding:NSUTF8StringEncoding]];
   [request setHTTPBody:body];
    
    // set the content-length
    NSString *postLength = [NSString stringWithFormat:@"%ld", [body length]];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setURL:requestURL];
    
    [NSURLConnection sendAsynchronousRequest:request queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            NSLog(@"done");
            [self.delegate receivedPostUpdateTravellerProfile];
        }
    }];
    

}

-(void)getUserProfileImageWithId:(NSString *)imageId isGuide:(BOOL)isGuide ventouraId:(NSString *)ventouraId imagePosition:(NSUInteger)imagePosition{
    // need to pass in string for user id and user type
    NSLog(@"fetching start json");
    NSString *urlAsString;
    if(isGuide){
        
        urlAsString= [NSString stringWithFormat:@"%@/guide/getGuideSingleGallery/%@",ventouraBaseURL,imageId];
    }else{
        urlAsString= [NSString stringWithFormat:@"%@/traveller/getTravellerSingleGallery/%@",ventouraBaseURL,imageId];
        
    }
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" profile get url %@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            if (isGuide) {
                NSLog(@"guide");
//                [self.delegate receivedGuideProfileImagesZip:data ventouraId:ventouraId];
                [self.delegate receivedGuideProfileImage:data ventouraId:ventouraId isGuide:isGuide imageId:imageId];

            }else{
                NSLog(@"traveller");
//                [self.delegate receivedTravellerProfileImagesZip:data ventouraId:ventouraId];
                [self.delegate receivedTravellerProfileImage:data ventouraId:ventouraId isGuide:isGuide imageId:imageId];

            }
        }
    }];
}

- (void)getUserProfileImages: (BOOL)isGuide ventouraId:(NSString*)ventouraId{
    
    // need to pass in string for user id and user type
    NSString *urlAsString;
    if(isGuide){
        urlAsString= [NSString stringWithFormat:@"%@/guide/getAllGalleryImages/%@",ventouraBaseURL,ventouraId];
    }else{
        urlAsString= [NSString stringWithFormat:@"%@/traveller/getAllGalleryImages/%@",ventouraBaseURL,ventouraId];
        
    }
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" profile get url %@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            if (isGuide) {
                [self.delegate receivedGuideProfileImagesZip:data ventouraId:ventouraId];
            }else{
                [self.delegate receivedTravellerProfileImagesZip:data ventouraId:ventouraId];
            }
        }
    }];
}
- (void)getUserProfile: (BOOL)isGuide ventouraId:(NSString*)ventouraId{
// need to pass in string for user id and user type
    NSString *urlAsString;
    if(isGuide){
        urlAsString= [NSString stringWithFormat:@"%@/guide/getGuideProfile/%@",ventouraBaseURL,ventouraId];
    }else{
        urlAsString= [NSString stringWithFormat:@"%@/traveller/getTravellerProfile/%@",ventouraBaseURL,ventouraId];

    }
  
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" profile get url %@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            if (isGuide) {
                [self.delegate receivedGuideProfileJSON:data];
            }else{
                [self.delegate receivedTravellerProfileJSON:data];
            }
        }
    }];
}

- (void)getUserCreatedProfile: (BOOL)isGuide ventouraId:(NSString*)ventouraId{
    // need to pass in string for user id and user type
    NSString *urlAsString;
    if(isGuide){
        urlAsString= [NSString stringWithFormat:@"%@/guide/getGuideProfile/%@",ventouraBaseURL,ventouraId];
    }else{
        urlAsString= [NSString stringWithFormat:@"%@/traveller/getTravellerProfile/%@",ventouraBaseURL,ventouraId];
        
    }
    
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" profile get url %@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            if (isGuide) {
                [self.delegate receivedGuideCreatedProfileJSON:data];
            }else{
                [self.delegate receivedTravellerCreatedProfileJSON:data];
            }
        }
    }];
}


-(void)postPortalImageId:(NSString *)imageId withUserId:(NSString *)ventouraId isUserGuide:(BOOL)isUserGuide{

    NSString *urlAsString;
    if(isUserGuide){
        urlAsString= [NSString stringWithFormat:@"%@/guide/setGuidePortalGallery/%@/%@",ventouraBaseURL,ventouraId,imageId];
    }else{
        urlAsString= [NSString stringWithFormat:@"%@/traveller/setTravellerPortalGallery/%@/%@",ventouraBaseURL,ventouraId,imageId];
        
    }
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    NSLog(@" Set Portal Image URL %@", urlAsString);
    
    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
        
        if (error) {
            [self.delegate fetchingProfileJSONFailedWithError:error];
        } else {
            //make a delegate here//
            [self.delegate receivedSetPortalImage];
        }
    }];
}




@end
