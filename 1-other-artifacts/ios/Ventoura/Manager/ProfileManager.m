//
//  VentouraPackageManager.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ProfileManager.h"
#import "ProfileBuilder.h"
#import "ImageBuilder.h"
#import "ProfileCommunicator.h"
#import "SSZipArchive.h"
#import "Person.h"

@implementation ProfileManager

-(void) setPortalImageId:(NSString *)imageId withUserId:(NSString *)ventouraId isUserGuide:(BOOL)isUserGuide{
    [self.communicator postPortalImageId:imageId withUserId:ventouraId isUserGuide:isUserGuide];
}
-(void)updateGuideAttractions:(NSArray *)attractionsArrDisplay{
    [self.communicator postGuideAttractions:attractionsArrDisplay];
}


-(void)deleteGuideAttractions:(NSArray *)attractionsArrDeleted{
    [self.communicator postDeleteGuideAttractions:attractionsArrDeleted];
}

-(void)fetchUserProfile: (BOOL)isGuide ventouraId:(NSString*)ventouraId{
    [self.communicator getUserProfile:isGuide ventouraId:ventouraId];
}
-(void)fetchCreatedUserProfile:(BOOL)isGuide ventouraId:(NSString *)ventouraId{
    NSLog(@"fetchCreatedUserProfile");
    [self.communicator getUserCreatedProfile:isGuide ventouraId:ventouraId];
}

-(void)fetchUserProfileImages:(BOOL)isGuide ventouraId:(NSString *)ventouraId{
    [self.communicator getUserProfileImages:isGuide ventouraId:ventouraId];
}
-(void)fetchUserProfileImageWithId:(NSString*)imageId isGuide:(BOOL)isGuide ventouraId:(NSString*)ventouraId imagePosition:(NSUInteger)imagePosition{
    [self.communicator getUserProfileImageWithId:imageId isGuide:isGuide ventouraId:ventouraId imagePosition:imagePosition];
}
- (void)createNewImage:(BOOL)isGuide ventouraId:(NSString *)ventouraId image:(UIImage *)image isPortal:(BOOL)isPortal{
    [self.communicator postImage:isGuide ventouraId:ventouraId image:image isPortal:isPortal];
}
-(void) updateTravellerProfile:(NSString *)ventouraId textBiography:(NSString *)textBiography country:(NSString*)country {
    [self.communicator postProfileUpdate:ventouraId textBiography:textBiography country:country];
}
-(void)updateGuideProfile:(NSString *)ventouraId textBiography:(NSString *)textBiography tourLength:(NSString *)tourLength tourPrice:(NSString *)tourPrice city:(NSString *)city country:(NSString*)country tourType:(NSString *)tourType{
    [self.communicator postGuideProfileUpdate:ventouraId textBiography:textBiography tourLength:tourLength tourPrice:tourPrice city:city country:country tourType:tourType];
}

-(void)deleteUserImages:(NSArray *)deleteImages isUserGuide:(BOOL)isUserGuide{

    [self.communicator postDeleteUserImages:deleteImages isUserGuide:isUserGuide];

}

#pragma mark - Ventoura Package Communicator Deleagete
// a json delegate, you can also have other formats.
//- (void)receivedProfileJSON:(NSData *)objectNotation{
//    //once json is received, convert it and call manager
//    NSError *error = nil;
//    Person *profile = [ProfileBuilder ProfileFromJSON:objectNotation error:&error];
//        
//        if (error != nil) {
//            [self.delegate fetchingProfileFailedWithError:error];
//   
//        } else {
//            [self.delegate didReceivePersonProfile:profile];
//        }
//    
//}


-(void)receivedTravellerProfileJSON:(NSData *)objectNotation{
    NSLog(@"T");
    NSError *error = nil;
    Person *profile = [ProfileBuilder travellerProfileFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingProfileFailedWithError:error];
        
    } else {
        [self.delegate didReceiveTravellerProfile:profile];
    }
}
-(void) receivedTravellerCreatedProfileJSON:(NSData *)objectNotation{
    NSLog(@"T");
    NSError *error = nil;
    Person *profile = [ProfileBuilder travellerProfileFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingProfileFailedWithError:error];
        
    } else {
        [self.delegate didReceiveCreatedTravellerProfile:profile];
    }

}


-(void)receivedGuideProfileJSON:(NSData *)objectNotation{
    NSLog(@"G");
    NSError *error = nil;
    Person *profile = [ProfileBuilder guideProfileFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingProfileFailedWithError:error];
        
    } else {
        [self.delegate didReceiveGuideProfile:profile];
    }
}
-(void)receivedGuideCreatedProfileJSON:(NSData *)objectNotation{
    NSLog(@"G");
    NSError *error = nil;
    Person *profile = [ProfileBuilder guideProfileFromJSON:objectNotation error:&error];
    
    if (error != nil) {
        [self.delegate fetchingProfileFailedWithError:error];
        
    } else {
        [self.delegate didReceiveCreatedGuideProfile:profile];
    }

}

-(void)receivedImageUplpoadJSON:(NSData *)objectNotation imageData:(UIImage*)imageData isPortal:(BOOL)isPoral{
    NSError *error = nil;
    NSString *imageId = [ImageBuilder ImageUploadResultFromJSON:objectNotation error:&error];
    //save to local file
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *destinationPath = [path stringByAppendingPathComponent:@"/ImagesFolder"];
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
    NSString *folderName = [ventouraUtility returnMyUserIdWithType];
    destinationPath = [destinationPath stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",folderName]];
    if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
    
    if (imageData != nil)
    {
        NSLog(@"saving");
        NSString* path  = imageId;
        NSString* imgPathPreFix = [ventouraUtility returnImagePathPrefixIsUserGuide:[ventouraUtility isUserGuide] ventouraId:[ventouraUtility returnMyUserId]];
        path =[NSString stringWithFormat:@"%@/%@.png",imgPathPreFix,path];
        NSLog(@"saving to path %@,",path);
        NSData* data = UIImagePNGRepresentation(imageData);
        [data writeToFile:path atomically:YES];
//        [self.delegate didReceiveTravllerImage];
        
    }else{
        NSLog(@"img data not passed");
    }

    [self.delegate didReceiveImageId:imageId isPortal:isPoral];

}
-(void) receivedTravellerProfileImage:(NSData *)objectNotation ventouraId:(NSString *)ventouraId isGuide:(BOOL)isGuide imageId:(NSString *)imageId{
    NSLog(@"Saving image %@", imageId);
    UIImage *image = [UIImage imageWithData:objectNotation];
   
    
    
    
    [ventouraUtility checkImagePathUserGuide:isGuide ventouraId:ventouraId];

    if (image != nil)
    {
        NSString* path = [ventouraUtility returnImagePathIsUserGuide:isGuide ventouraId:ventouraId imageId:imageId];
        NSLog(@"saved in Path:%@", path);
        NSData* data = UIImagePNGRepresentation(image);
        [data writeToFile:path atomically:YES];
        [self.delegate didReceiveTravllerImage];

    }
    NSLog(@"end trav prof img");
}

-(void) receivedGuideProfileImage:(NSData *)objectNotation ventouraId:(NSString *)ventouraId isGuide:(BOOL)isGuide imageId:(NSString *)imageId{
    NSLog(@"Saving image");
    UIImage *image = [UIImage imageWithData:objectNotation];
    [ventouraUtility checkImagePathUserGuide:isGuide ventouraId:ventouraId];
    if (image != nil)
    {
        NSString* path = [ventouraUtility returnImagePathIsUserGuide:isGuide ventouraId:ventouraId imageId:imageId];
        NSLog(@"saved in Path:%@", path);
        NSData* data = UIImagePNGRepresentation(image);
        [data writeToFile:path atomically:YES];
    }
    [self.delegate didReceiveTravllerImage];

    
}



-(void) receivedTravellerProfileImagesZip:(NSData *)zipData ventouraId:(id)ventouraId{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *zipPath = [path stringByAppendingPathComponent:@"temp_user_images.zip"];
    NSError *error = nil;
    [zipData writeToFile:zipPath options:0 error:&error];
    if(!error)
    {
        // TODO: Unzip
        NSLog(@"file saved at %@", zipPath);
        NSString *destinationPath = [path stringByAppendingPathComponent:@"/ImagesFolder"];
        if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
        [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
        NSString *folderName = [NSString stringWithFormat:@"t_%@",ventouraId];
        
        destinationPath = [destinationPath stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",folderName]];
        if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
            [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
        [SSZipArchive unzipFileAtPath:zipPath toDestination:destinationPath];
        NSArray * directoryContents = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:destinationPath error:&error];
        if (error != nil) {
            [self.delegate fetchingProfileFailedWithError:error];
            
        } else {
            [self.delegate didReceiveTravellerProfileImages:directoryContents];
        }
    }
    else
    {
        NSLog(@"Error saving file %@",error);
      [self.delegate fetchingProfileFailedWithError:error];
    }
}


-(void) receivedGuideProfileImagesZip:(NSData *)zipData ventouraId:(id)ventouraId{
//    NSLog(@"size data %u", zipData.length);
    //    NSString *strData = [[NSString alloc]initWithData:zipData encoding:NSUTF8StringEncoding];
    //    NSLog(@"str data %@", strData);
    NSLog(@"inside receivedGuideProfileImagesZip");
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *zipPath = [path stringByAppendingPathComponent:@"temp_user_images.zip"];
    NSError *error = nil;
    [zipData writeToFile:zipPath options:0 error:&error];
    if(!error)
    {
        // TODO: Unzip
        NSLog(@"file saved at %@", zipPath);
        NSString *destinationPath = [path stringByAppendingPathComponent:@"/ImagesFolder"];
        if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
            [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
        NSString *folderName = [NSString stringWithFormat:@"g_%@",ventouraId];
        
        destinationPath = [destinationPath stringByAppendingPathComponent:[NSString stringWithFormat:@"/%@",folderName]];
        if (![[NSFileManager defaultManager] fileExistsAtPath:destinationPath])
            [[NSFileManager defaultManager] createDirectoryAtPath:destinationPath withIntermediateDirectories:NO attributes:nil error:&error]; //Create folder
        [SSZipArchive unzipFileAtPath:zipPath toDestination:destinationPath];
        NSArray * directoryContents = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:destinationPath error:&error];
        if (error != nil) {
            [self.delegate fetchingProfileFailedWithError:error];
            
        } else {
            [self.delegate didReceiveGuideProfileImages:directoryContents];
        }
    }
    else
    {
        NSLog(@"Error saving file %@",error);
        [self.delegate fetchingProfileFailedWithError:error];
    }
    NSLog(@"end receivedGuideProfileImagesZip");
}




- (void)fetchingProfileJSONFailedWithError:(NSError *)error
{
    [self.delegate fetchingProfileFailedWithError:error];
}

-(void) receivedPostGuideUpdateAttractions{
    [self.delegate receivedGuideUpdateAttractions];
}

-(void) receivedPostGuideUpdateSecrets{
    [self.delegate receivedGuideUpdateSecrets];
}

-(void) receivedPostGuideDeleteAttractions{
    [self.delegate receivedGuideDeleteAttractions];
}

-(void) receivedPostGuideDeleteSecrets{
    [self.delegate receivedGuideDeleteSecrets];
}

-(void) receivedPostUpdateTravellerProfile{
    [self.delegate receivedUpdateTravellerProfile];
}
-(void) receivedPostUpdateGuideProfile{
    [self.delegate receivedUpdateGuideProfile];
}

-(void) receivedPostUploadUserImages{

}
-(void) receivedPostDeleteUserImages{
    [self.delegate receivedDeleteUserImages];
}
-(void) receivedSetPortalImage{
    [self.delegate receivedSetPortalImage];
}
@end
