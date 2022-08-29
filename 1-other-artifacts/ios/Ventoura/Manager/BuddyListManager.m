//
//  VentouraPackageManager.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "BuddyListManager.h"
#import "BuddyListBuilder.h"
#import "BuddyListCommunicator.h"
#import "SSZipArchive.h"
#import "Person.h"

@implementation BuddyListManager

-(void)fetchBuddyList{
    [self.communicator getBuddyList];
}

-(void)postDeleteBuddyById:(NSString *)userId isUserGuide:(BOOL)isUserGuide{
    [self.communicator deleteBuddyByUserId:userId isUserGuide:isUserGuide];
}


#pragma mark - Ventoura Package Communicator Deleagete
// a json delegate, you can also have other formats.
- (void)receivedBuddyJSON:(NSData *)objectNotation{
    //once json is received, convert it and call manager
    NSError *error = nil;
    NSArray *package = [BuddyListBuilder buddyListFromJSON:objectNotation error:&error];
        
        if (error != nil) {
            [self.delegate fetchingBuddyListFailedWithError:error];
   
        } else {
            [self.delegate didReceiveBuddyList:package];
        }
    
}


- (void)fetchingBuddyListJSONWithError:(NSError *)error
{
    [self.delegate fetchingBuddyListFailedWithError:error];
}

-(void) receivedDeleteBuddyJSON:(NSData *)objectNotation userId:(NSString *)userId isUserGuide:(BOOL)isUserGuide{

    [self.delegate didReceiveDeleteBuddyUserId:userId isUserGuide:isUserGuide];

}

@end
