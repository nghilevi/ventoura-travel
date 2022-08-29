//
//  TravellerManager.m
//  Ventoura
//
//  Created by Wenchao Chen on 5/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//


#import "LoginManager.h"
@implementation LoginManager
-(void) fetchTravellerById:(NSString *)TravellerId{

    [self.communicator searchTravellerById:TravellerId];
}

-(void) fetchLoginByFacebookId:(NSString *)FacebookId isGuide:(BOOL)isGuide{
    [self.communicator searchUserByFacebookId:FacebookId isGuide:isGuide];
}

-(void) fetchLoginByFacebookIdWithDeviceToken:(NSString *)FacebookId isGuide:(BOOL)isGuide{
    [self.communicator searchUserByFacebookIdPost:FacebookId isGuide:isGuide];
}

-(void) createVentouraAccountByFacebookId:(id<FBGraphUser>)fbUser isGuide:(BOOL)isGuide country:(NSString*)country{
    
    [self.communicator postPersonFacebookID:fbUser isGuide:isGuide country:country];
}
-(void) createVentouraAccountByFacebookId:(id<FBGraphUser>)fbUser payByCard:(BOOL)payByCard country:(NSString*)country{
    [self.communicator postPersonFacebookID:fbUser payByCard:payByCard country:country];

}



#pragma mark - TravellerCommunicatorDelegate

-(void)receivedTokenPostJSON:(NSData *)objectNotation{

    [self.delegate didReceiveTokenPost];
}
- (void)receivedPersonLoginJSON:(NSData *)objectNotation
{
    NSError *error = nil;
//    Traveller *traveller = [TravellerBuilder travellerLoginFromJSON:objectNotation error:&error];
    Person *person = [TravellerBuilder personLoginFromJSON:objectNotation error:&error];
    if (error != nil) {
        [self.delegate fetchingLoginFailedWithError:error];
        
    } else {
        [self.delegate didReceivePersonForLogin:person];
    }
}



- (void)receivedTravellerCreateJSON:(NSData *)objectNotation
{
    NSError *error = nil;
//    Traveller *traveller = [TravellerBuilder travellerLoginFromJSON:objectNotation error:&error];
    Person *person = [TravellerBuilder personLoginFromJSON:objectNotation error:&error];
    if (error != nil) {
        [self.delegate fetchingLoginFailedWithError:error];
        
    } else {
        [self.delegate didReceiveTravellerCreate:person];
    }
}


- (void)fetchingLoginFailedWithError:(NSError *)error
{
    [self.delegate fetchingLoginFailedWithError:error];
}

-(void) receivedTravellerJSON:(NSData *)objectNotation{
}
-(void) fetchingPersonJSONFailedWithError:(NSError *)error{
}
@end
