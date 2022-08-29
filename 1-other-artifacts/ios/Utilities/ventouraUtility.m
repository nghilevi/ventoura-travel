//
//  ventouraUtility.m
//  Ventoura
//
//  Created by Wenchao Chen on 9/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ventouraUtility.h"

@implementation ventouraUtility


#pragma Colours

+(UIColor*)ventouraBlue{
    return [UIColor colorWithRed:0.141 green:0.337 blue:0.431 alpha:1];

}
+(UIColor*)ventouraPink{
    return [UIColor colorWithRed:0.976 green:0.384 blue:0.384 alpha:1];
}

+(UIColor*)ventouraMenuPink{
  return [UIColor colorWithRed:0.973 green:0.333 blue:0.314 alpha:1]; /*#f85550*/
}



+(UIColor*)ventouraSettingTextColour{

    return [UIColor colorWithRed:0.302 green:0.306 blue:0.357 alpha:1];
    /*#4d4e5b*/
}
+(UIColor*)ventouraSettingLegalTextColour{
    return [UIColor colorWithRed:0.698 green:0.698 blue:0.698 alpha:1]; /*#b2b2b2*/
    /*#4d4e5b*/
}
+(UIColor*) ventourasettingsPaddingColour{
    
    return  [UIColor colorWithRed:0.933 green:0.933 blue:0.933 alpha:1]; /*#eeeeee*/
}

+(UIColor*)ventouraPinkAlpha{
    return [UIColor colorWithRed:1 green:0 blue:0 alpha:0.6];
}
+(UIColor*)ventouraDarkGreyAlpha{
        return[UIColor colorWithRed:0.33 green:0.33 blue:0.33 alpha:0.4];
}

+(UIColor*)ventouraTitleColour{
    return [UIColor colorWithRed:0.467 green:0.494 blue:0.51 alpha:1] ;
}

+(UIColor*)ventouraTextHeadingGrey{
    return [UIColor colorWithRed:0.361 green:0.361 blue:0.361 alpha:1];
}

+(UIColor*)ventouraTextBodyGrey{
    
    return [UIColor colorWithRed:0.388 green:0.388 blue:0.388 alpha:1];
}

+(UIColor*)ventouraTextBodyGrey2{
    return [UIColor colorWithRed:0.714 green:0.714 blue:0.714 alpha:1];
}
+(UIColor*)ventouraTextBodyGreyWithA{
    
    return [UIColor colorWithRed:0.388 green:0.388 blue:0.388 alpha:0.3];
}

+(UIColor*)ventouraNavBackgroundColour{
    
    return [UIColor colorWithRed:0.965 green:0.969 blue:0.976 alpha:1];
}
+(UIColor*)ventouraTextBodyGreyProfileLabel{
    return [UIColor colorWithRed:0.835 green:0.835 blue:0.835 alpha:1] /*#d5d5d5*/;
    
}
+(UIColor*)ventouraStarGreyColour{
    return [UIColor colorWithRed:0.514 green:0.514 blue:0.506 alpha:1];
}

+(UIColor*)ventouraPromotionGrey{
    return
    
    [UIColor colorWithRed:0.373 green:0.373 blue:0.42 alpha:1] /*#5f5f6b*/;
}

#pragma dates
+(NSString*) ventouraDateToStringForTours: (NSDate*) date{
    NSString *suffix_string = @"|st|nd|rd|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|st|nd|rd|th|th|th|th|th|th|th|st";
    NSArray *suffixes = [suffix_string componentsSeparatedByString: @"|"];
    NSDateFormatter *monthDayFormatter = [[NSDateFormatter alloc] init];
    [monthDayFormatter setDateFormat:@"d"];
    int date_day = [[monthDayFormatter stringFromDate:[NSDate date]] intValue];
    NSString *suffix = [suffixes objectAtIndex:date_day];
    NSDateFormatter *dateFormateHeader = [[NSDateFormatter alloc]init];
    [dateFormateHeader setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    [dateFormateHeader setDateFormat:[NSString stringWithFormat:@"d'%@' MMM hh:mm a",suffix]];
    return [dateFormateHeader stringFromDate:date];
}

+(NSString*) ventouraDateToStringForPost: (NSDate*) date{
    NSDateFormatter *dateFormateHeader = [[NSDateFormatter alloc]init];

    [dateFormateHeader setTimeZone:[NSTimeZone localTimeZone]];
    [dateFormateHeader setDateFormat:[NSString stringWithFormat:@"yyyy-MM-dd"]];
    return [dateFormateHeader stringFromDate:date];
}

+(NSString*) ventouraDateToStringForProfilePost: (NSDate*) date{
    NSDateFormatter *dateFormateHeader = [[NSDateFormatter alloc]init];
    [dateFormateHeader setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    [dateFormateHeader setDateFormat:[NSString stringWithFormat:@"yyyy-MM-dd"]];
    return [dateFormateHeader stringFromDate:date];
}

+(NSString*) ventouraDateToStringForTourPost: (NSDate*) date{
    NSDateFormatter *dateFormateHeader = [[NSDateFormatter alloc]init];
//    [dateFormateHeader setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    [dateFormateHeader setDateFormat:[NSString stringWithFormat:@"yyyy-MM-dd HH:mm"]];
    return [dateFormateHeader stringFromDate:date];
}

+(NSString*) ventouraDateToStringForCreateTrip: (NSDate*) date{
    NSString *suffix_string = @"|st|nd|rd|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|st|nd|rd|th|th|th|th|th|th|th|st";
    NSArray *suffixes = [suffix_string componentsSeparatedByString: @"|"];
    NSDateFormatter *monthDayFormatter = [[NSDateFormatter alloc] init];
    [monthDayFormatter setDateFormat:@"d"];
    int date_day = [[monthDayFormatter stringFromDate:[NSDate date]] intValue];
    NSString *suffix = [suffixes objectAtIndex:date_day];
    NSDateFormatter *dateFormateHeader = [[NSDateFormatter alloc]init];
//    [dateFormateHeader setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    [dateFormateHeader setDateFormat:[NSString stringWithFormat:@"d'%@' MMM yyyy",suffix]];
    return [dateFormateHeader stringFromDate:date];
}
+(NSString *) ventouraDateToStringForTripsStartAt:(NSString*)startTime endAt:(NSString*)endTime{

    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    [df setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    NSDate *startDate = [df dateFromString: startTime];
    NSDate *endDate = [df dateFromString: endTime];
    
    
    NSString *suffix_string = @"|st|nd|rd|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|th|st|nd|rd|th|th|th|th|th|th|th|st";
    NSArray *suffixes = [suffix_string componentsSeparatedByString: @"|"];
    NSDateFormatter *monthDayFormatter = [[NSDateFormatter alloc] init];
    [monthDayFormatter setDateFormat:@"d"];
    int date_day = [[monthDayFormatter stringFromDate:[NSDate date]] intValue];
    NSString *suffix = [suffixes objectAtIndex:date_day];
    NSDateFormatter *dateFormateHeader = [[NSDateFormatter alloc]init];
    [dateFormateHeader setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    [dateFormateHeader setDateFormat:[NSString stringWithFormat:@"d'%@' MMM",suffix]];
    return [NSString stringWithFormat:@"%@ - %@",[dateFormateHeader stringFromDate:startDate],[dateFormateHeader stringFromDate:endDate]];


//    return @"date String ";
}

#pragma Screens

+(CGFloat)getScreenHeight{
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    return screenRect.size.height;
}

#pragma mark Image Cropping
+(UIImage*)getLocalProfileImageWithID:(NSString*) localId{
    UIImage *img;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    
    
    NSString *imgPath;
        imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"g_%@_.png",localId]];
        BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imgPath];
    if (fileExists) {
        NSLog(@"file exists");
        UIImage *newImg = [UIImage imageWithContentsOfFile:imgPath];
        CGImageRef cgref = [newImg CGImage];
        CIImage *cim = [newImg CIImage];
        if (cim == nil && cgref == NULL)
        {
            NSLog(@"no underlying data");
        }
        img = newImg;
    }else{
        img =[UIImage imageNamed:@"noimg.jpg"];
    }
    img = [self imageCropSquareCentre:img];
    return img;
}
+(UIImage*)getTravellerProfileImageWithID:(NSString*) localId{
    UIImage *img;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    
    
    NSString *imgPath;
    imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"t_%@_.png",localId]];
    BOOL fileExists = [[NSFileManager defaultManager] fileExistsAtPath:imgPath];
    if (fileExists) {
        NSLog(@"file exists");
        UIImage *newImg = [UIImage imageWithContentsOfFile:imgPath];
        CGImageRef cgref = [newImg CGImage];
        CIImage *cim = [newImg CIImage];
        if (cim == nil && cgref == NULL)
        {
            NSLog(@"no underlying data");
        }
        img = newImg;
    }else{
        img =[UIImage imageNamed:@"noimg.jpg"];
    }
    img = [self imageCropSquareCentre:img];
    return img;
}


+(UIImage*)imageWithImage: (UIImage*) sourceImage scaledToWidth: (float) i_width
{
    float oldWidth = sourceImage.size.width;
    float scaleFactor = i_width / oldWidth;
    
    float newHeight = sourceImage.size.height * scaleFactor;
    float newWidth = oldWidth * scaleFactor;
    
    UIGraphicsBeginImageContext(CGSizeMake(newWidth, newHeight));
    [sourceImage drawInRect:CGRectMake(0, 0, newWidth, newHeight)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}

+(UIImage*)imageWithImage: (UIImage*) sourceImage scaledToHeight: (float) i_height
{
    float oldHeight = sourceImage.size.height;
    float scaleFactor = i_height / oldHeight;
    
    float  newWidth= sourceImage.size.width * scaleFactor;
    float  newHeight = oldHeight * scaleFactor;
    
    UIGraphicsBeginImageContext(CGSizeMake(newWidth, newHeight));
    [sourceImage drawInRect:CGRectMake(0, 0, newWidth, newHeight)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}

+(CGSize)sizeWithImage: (UIImage*) sourceImage scaledToHeight: (float) i_height
{

    float oldHeight = sourceImage.size.height;
    float scaleFactor = i_height / oldHeight;
    
    float  newWidth= sourceImage.size.width * scaleFactor;
    float  newHeight = oldHeight * scaleFactor;
    return  CGSizeMake(newWidth, newHeight);
}



//image cropping code, save it to a ultility fiolder later

+(UIImage*)imageCropSquareCentre:(UIImage*)original
{
    UIImage *ret = nil;
    
    // This calculates the crop area.
    
    float originalWidth  = original.size.width;
    float originalHeight = original.size.height;
    
    float edge = fminf(originalWidth, originalHeight);
    
    float posX = (originalWidth   - edge) / 2.0f;
    float posY = (originalHeight  - edge) / 2.0f;
    
    
    CGRect cropSquare;
    // If orientation indicates a change to portrait.
    if(original.imageOrientation == UIImageOrientationLeft ||
       original.imageOrientation == UIImageOrientationRight)
    {
        cropSquare = CGRectMake(posY, posX,
                                edge, edge);
        
    }
    else
    {
        cropSquare = CGRectMake(posX, posY,
                                edge, edge);
    }
    
    
    // This performs the image cropping.
    
    CGImageRef imageRef = CGImageCreateWithImageInRect([original CGImage], cropSquare);
    
    ret = [UIImage imageWithCGImage:imageRef
                              scale:original.scale
                        orientation:original.imageOrientation];
    
    CGImageRelease(imageRef);
    
    return ret;
}

+(BOOL) isUserLoggedin{
    return [[NSUserDefaults standardUserDefaults] boolForKey:@"userVentouraIsLoggedIn"];
}

+(BOOL) isUserGuide{
    return [[NSUserDefaults standardUserDefaults] boolForKey:@"userisGuide"];
}

+(BOOL) isUserGuide:(NSString*) userRole{
    if ([userRole isEqualToString:@"GUIDE"]) {
        return YES;
    }else{
        return NO;
    }
}

+(NSString*) returnMyUserId{
    return [[NSUserDefaults standardUserDefaults] stringForKey:@"userVentouraId"] ;
}

+(NSString*) returnMyFacbeookId{
    return [[NSUserDefaults standardUserDefaults] stringForKey:@"userFacebookId"] ;
}


+(NSString*) returnMyUserFirstName{
    return [[NSUserDefaults standardUserDefaults] stringForKey:@"userFirstName"] ;

}
+(NSString*) returnMyUserIdWithType{
    if ([self isUserGuide]) {
        return[NSString stringWithFormat:@"g_%@",[self returnMyUserId]];
    }else{
        return[NSString stringWithFormat:@"t_%@",[self returnMyUserId]];
    }
}

+(NSString*) returnUserIdWithType:(BOOL)isUserGuide ventouraId:(NSString *)ventouraId{

    if (isUserGuide) {
        return[NSString stringWithFormat:@"g_%@",ventouraId];
    }else{
        return[NSString stringWithFormat:@"t_%@",ventouraId];
    }
}
+(NSString*) returnImagePathPrefixIsUserGuide:(BOOL)isUserGuide ventouraId:(NSString *)ventouraId{

    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *userId = [self returnUserIdWithType:isUserGuide ventouraId:ventouraId];
    NSString *imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"/ImagesFolder/%@",userId]];
//    NSLog(@"aaaa %@",imgPath);

    return imgPath;
    
}
+(NSString*) returnImagePathIsUserGuide:(BOOL)isUserGuide ventouraId:(NSString *)ventouraId imageId:(NSString *)imageId{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *userId = [self returnUserIdWithType:isUserGuide ventouraId:ventouraId];
    NSString *myUserId = [self returnMyUserIdWithType];
    NSString *imgPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"%@/ImagesFolder/%@",myUserId,userId]];
    imgPath = [NSString stringWithFormat:@"%@/%@.png",imgPath,imageId];
    return imgPath;

}
+(void) checkImagePathUserGuide:(BOOL)isUserGuide ventouraId:(NSString *)ventouraId{
    NSError *error = nil;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    
    NSString *destinationPath = [path stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",[ventouraUtility returnMyUserIdWithType]]];
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
    

    destinationPath = [destinationPath stringByAppendingPathComponent:@"/ImagesFolder"];
    NSLog(@"%@",destinationPath);
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
  

    NSString *folderName;
    if (isUserGuide) {
        folderName= [NSString stringWithFormat:@"g_%@",ventouraId];
    }else{
        folderName= [NSString stringWithFormat:@"t_%@",ventouraId];
    }
    
    destinationPath = [destinationPath stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",folderName]];
    NSLog(@"%@",destinationPath);
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder

}
+(void) userLogOut{
    
    [FBSession.activeSession closeAndClearTokenInformation];
    [FBSession.activeSession close];
    [FBSession setActiveSession:nil];
    NSString *domainName = [[NSBundle mainBundle] bundleIdentifier];
    [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:domainName];
}

+(void) saveUserInfo{
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [[NSUserDefaults standardUserDefaults] setObject:_facebookId forKey:@"userFacebookId"];
    [[NSUserDefaults standardUserDefaults] setObject:_userVentouraID forKey:@"userVentouraId"];
    [[NSUserDefaults standardUserDefaults] setBool:_userIsGuide forKey:@"userisGuide"];
    [[NSUserDefaults standardUserDefaults] setBool:true forKey:@"userVentouraIsLoggedIn"];
    [[NSUserDefaults standardUserDefaults] setObject: [_fbUserObject objectForKey:@"first_name"] forKey:@"userFirstName"];
    [[NSUserDefaults standardUserDefaults] setObject: [_fbUserObject objectForKey:@"last_name"] forKey:@"userLastName"];
    [defaults synchronize];
}

+(NSString*)returnImageIdFromImagePath:(NSString *)imagePath{
    NSArray *pathStrings = [imagePath componentsSeparatedByString: @"/"];
    pathStrings = [[pathStrings lastObject] componentsSeparatedByString: @"_"];
    return pathStrings[1];
}

+(UIImage *)imageByApplyingAlpha:(CGFloat)alpha withImage:(UIImage*) image {
    UIGraphicsBeginImageContextWithOptions(image.size, NO, 0.0f);
    
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGRect area = CGRectMake(0, 0, image.size.width, image.size.height);
    
    CGContextScaleCTM(ctx, 1, -1);
    CGContextTranslateCTM(ctx, 0, -area.size.height);
    
    CGContextSetBlendMode(ctx, kCGBlendModeMultiply);
    
    CGContextSetAlpha(ctx, alpha);
    
    CGContextDrawImage(ctx, area, image.CGImage);
    
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    
    UIGraphicsEndImageContext();
    
    return newImage;
}

+ (NSString *) escapeStringSQL:(NSString *)string {
    NSRange range = NSMakeRange(0, [string length]);
    return [string stringByReplacingOccurrencesOfString:@"'" withString:@"''" options:NSCaseInsensitiveSearch range:range];
}

+(void)printAllFonts{
    //Code to Print out All fonts
    for (NSString* family in [UIFont familyNames])
    {
        NSLog(@"%@", family);
        
        for (NSString* name in [UIFont fontNamesForFamilyName: family])
        {
            NSLog(@"  %@", name);
        }
    }
}
+(UIImageView*)returnLoadingAnimation{
    //Create the first status image and the indicator view
    UIImage *statusImage = [UIImage imageNamed:@"loading_frame_1.png"];
    UIImageView *activityImageView = [[UIImageView alloc]
                                      initWithImage:statusImage];
    
    //Add more images which will be used for the animation
    activityImageView.animationImages = [NSArray arrayWithObjects:
                                         [UIImage imageNamed:@"loading_frame_2.png"],
                                         [UIImage imageNamed:@"loading_frame_3.png"],
                                         [UIImage imageNamed:@"loading_frame_4.png"],
                                         [UIImage imageNamed:@"loading_frame_5.png"],
                                         [UIImage imageNamed:@"loading_frame_6.png"],
                                         [UIImage imageNamed:@"loading_frame_7.png"],
                                         [UIImage imageNamed:@"loading_frame_8.png"],
                                         [UIImage imageNamed:@"loading_frame_9.png"],
                                         [UIImage imageNamed:@"loading_frame_10.png"],
                                         [UIImage imageNamed:@"loading_frame_11.png"],
                                         [UIImage imageNamed:@"loading_frame_12.png"],
                                         
                                         nil];
    
    
    //Set the duration of the animation (play with it
    //until it looks nice for you)
    activityImageView.animationDuration = 1.2;
    
       [activityImageView startAnimating];
    
    return activityImageView;

}

+(UIImageView*)returnImageLoadingAnimation:(UIView*)superView {
    //Create the first status image and the indicator view
    UIImage *statusImage = [UIImage imageNamed:@"loading_frame_1.png"];
    statusImage = [statusImage imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];

    UIImageView *activityImageView = [[UIImageView alloc]
                                      initWithImage:statusImage];
    
//      activityImageView = [ge imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    //Add more images which will be used for the animation
    activityImageView.animationImages = [NSArray arrayWithObjects:
                                         statusImage,
//                                         [UIImage imageNamed:@"loading_frame_2.png"],
//                                         [UIImage imageNamed:@"loading_frame_3.png"],
//                                         [UIImage imageNamed:@"loading_frame_4.png"],
//                                         [UIImage imageNamed:@"loading_frame_5.png"],
//                                         [UIImage imageNamed:@"loading_frame_6.png"],
//                                         [UIImage imageNamed:@"loading_frame_7.png"],
//                                         [UIImage imageNamed:@"loading_frame_8.png"],
//                                         [UIImage imageNamed:@"loading_frame_9.png"],
//                                         [UIImage imageNamed:@"loading_frame_10.png"],
//                                         [UIImage imageNamed:@"loading_frame_11.png"],
//                                         [UIImage imageNamed:@"loading_frame_12.png"],
                                         
                                         nil];
    
    
    //Set the duration of the animation (play with it
    //until it looks nice for you)
    activityImageView.animationDuration = 1.2;
    
    
    //Position the activity image view somewhere in
    //the middle of your current view
    activityImageView.frame = CGRectMake(
                                             superView.frame.size.width/2
                                             -statusImage.size.width/2,
                                             superView.frame.size.height/2
                                             -statusImage.size.height/2,
                                             statusImage.size.width,
                                             statusImage.size.height);
    
    //Start the animation
//    [activityImageView startAnimating];

    return activityImageView;
    
}

+(void)saveFacebookImage:(NSString *) imageId{
    
    NSLog(@"save facebook Image");
    UIImage *image = _fbUIImage;
    [self checkImagePathUserGuide:YES ventouraId:[ventouraUtility returnMyUserId]];
    if (image != nil){
//        NSString* path  = imageId;
//        NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
        
        NSString*  path = [self returnImagePathIsUserGuide:YES ventouraId:[ventouraUtility returnMyUserId] imageId:imageId];
//        NSLog(@"saved in Path:%@", path);
        NSData* data = UIImagePNGRepresentation(image);
        [data writeToFile:path atomically:YES];
    }
    
    
}


+ (UIImage *)resizeImage:(UIImage*)image newSize:(CGSize)newSize {
    CGRect newRect = CGRectIntegral(CGRectMake(0, 0, newSize.width, newSize.height));
    CGImageRef imageRef = image.CGImage;
    
    UIGraphicsBeginImageContextWithOptions(newSize, NO, 0);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    // Set the quality level to use when rescaling
    CGContextSetInterpolationQuality(context, kCGInterpolationHigh);
    CGAffineTransform flipVertical = CGAffineTransformMake(1, 0, 0, -1, 0, newSize.height);
    
    CGContextConcatCTM(context, flipVertical);
    // Draw into the context; this scales the image
    CGContextDrawImage(context, newRect, imageRef);
    
    // Get the resized image from the context and a UIImage
    CGImageRef newImageRef = CGBitmapContextCreateImage(context);
    UIImage *newImage = [UIImage imageWithCGImage:newImageRef];
    
    CGImageRelease(newImageRef);
    UIGraphicsEndImageContext();
    
    return newImage;
}

+(void)deleteLocalImageBy:(NSString *)imageId ventouraId:(NSString *)ventouraId isUserGuide:(BOOL)isUserGuide{

    NSString* path = [self returnImagePathIsUserGuide:isUserGuide ventouraId:ventouraId imageId:imageId];
    NSFileManager *fm = [NSFileManager defaultManager];
    NSError *error = nil;
    BOOL success = [fm removeItemAtPath:path error:&error];
    if (!success || error) {
        // it failed.
        NSLog(@"deleting failed");
        NSLog(@"Error %@; %@", error, [error localizedDescription]);
        
    }
}



@end
