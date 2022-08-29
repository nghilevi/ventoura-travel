//
//  VentouraPackageManager.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "VentouraPackageManager.h"
#import "VentouraPackageBuilder.h"
#import "VentouraPackageCommunicator.h"
#import "SSZipArchive.h"
#import "Person.h"

@implementation VentouraPackageManager

-(void)fetchVentouraPackage{
    [self.communicator getVentouraPackage];
}


-(void)fetchVentouraPackageImages:(NSArray *)ventouraPackage{
    [self.communicator getVentouraPackageImages:ventouraPackage];
}

-(void)likeOrNot:(Person *)currentperson likeOrNotValue:(BOOL)likeOrNot{
    [self.communicator getLikeOrNotResult:currentperson likeOrNot:likeOrNot];
}

#pragma mark - Ventoura Package Communicator Deleagete
// a json delegate, you can also have other formats.
- (void)receivedPackageJSON:(NSData *)objectNotation{
    //once json is received, convert it and call manager
    NSError *error = nil;
    NSArray *package = [VentouraPackageBuilder ventouraPackageFromJSON:objectNotation error:&error];
        
        if (error != nil) {
            [self.delegate fetchingVentouraPackageFailedWithError:error];
   
        } else {
            [self.delegate didReceiveVentouraPakcage:package];
        }
    
}

- (void)receivedLikeOrNotJSON:(NSData *)objectNotation{
    NSError *error = nil;
    BOOL isMatch = [VentouraPackageBuilder ventouraPackagIsMatchFromJSON:objectNotation error:&error];
    if (error != nil) {
        [self.delegate fetchingVentouraPackageFailedWithError:error];
        
    } else {
        [self.delegate didReceiveVentouraMatch:isMatch];
    }

}
-(void) receivedLikeOrNotJSON:(NSData *)objectNotation withName:(NSString *)name{

    NSError *error = nil;
    BOOL isMatch = [VentouraPackageBuilder ventouraPackagIsMatchFromJSON:objectNotation error:&error];
    if (error != nil) {
        [self.delegate fetchingVentouraPackageFailedWithError:error];
        
    } else {
        [self.delegate didReceiveVentouraMatch:isMatch withName:name];
    }
}

- (void)fetchingPackageJSONFailedWithError:(NSError *)error
{
    [self.delegate fetchingVentouraPackageFailedWithError:error];
}

-(void)receivedPackageImageZip:(NSData *)zipData packageWithNoImage:(NSArray *)ventouraPackage{
    //[self.delegate didReceiveVentouraPakcageWithImage];
    
//    NSLog(@"size data %u", zipData.length);
//    NSString *strData = [[NSString alloc]initWithData:zipData encoding:NSUTF8StringEncoding];
//    NSLog(@"str data %@", strData);

    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [paths objectAtIndex:0];
    NSString *zipPath = [path stringByAppendingPathComponent:@"ventouraPackage.zip"];
    NSError *error = nil;
    [zipData writeToFile:zipPath options:0 error:&error];
    //NSMutableArray *people = ;

    
    if(!error)
    {
        // TODO: Unzip
        NSLog(@"file saved at %@", zipPath);
        //NSString *destinationPath = [path stringByAppendingPathComponent:@"/ImagesFolder"];
        [SSZipArchive unzipFileAtPath:zipPath toDestination:path];
        //debugs statements
        //NSArray * directoryContents = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:path error:&error];
        // NSLog(@"%@",directoryContents);
    }
    else
    {
        NSLog(@"Error saving file %@",error);
        [self.delegate fetchingVentouraPackageFailedWithError:error];
    }   
    //NSError *error = nil;
    //change the line below dont need to pass zip data anymore,gota be careful tho, zip data might not be done completely.
    //probs no need to build again, probs can return. this straight away.
    //change delegate here.
    NSArray *package = [VentouraPackageBuilder ventouraPackageAddImageZip:zipData withVentouraPackage:ventouraPackage error:&error];
    
    if (error != nil) {
        [self.delegate fetchingVentouraPackageFailedWithError:error];
        
    } else {
        [self.delegate didReceiveVentouraPakcageWithImage:package];
    }

}


@end
