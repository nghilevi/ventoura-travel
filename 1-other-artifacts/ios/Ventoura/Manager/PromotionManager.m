//
//  VentouraPackageManager.m
//  Ventoura
//
//  Created by Wenchao Chen on 19/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "PromotionManager.h"
//#import "TourBuilder.h"
#import "PromotionCommunicator.h"
//#import "SSZipArchive.h"
//#import "Person.h"

@implementation PromotionManager
//
//-(void)createBookingWithGuideId:(NSString *)guideId travellerId:(NSString *)travellerId guideName:(NSString *)guideName travellerName:(NSString *)travellerName tourPrice:(NSString *)tourPrice tourDate:(NSDate *)tourDate{
//    [self.communicator postBookingWithGuideId:guideId travellerId:travellerId guideName:guideName travellerName:travellerName tourPrice:tourPrice tourDate:tourDate];
//}


-(void)postCityId:(NSString *)city1 withCity2:(NSString *)city2 withCity3:(NSString *)city3 withCity4:(NSString *)city4{
    
    NSLog(@"posting");
    [self.communicator postCity1:city1 withCity2:city2 withCity3:city3 withCity4:city4];
}

-(void)postEnterPromotionWithCityId:(NSString *)city1 withCity2:(NSString *)city2 withCity3:(NSString *)city3 withCity4:(NSString *)city4{
    [self.communicator postEnterPromotionCity1:city1 withCity2:city2 withCity3:city3 withCity4:city4];
}
-(void)fetchPromotionStatus:(NSString *)userId{
    [self.communicator getPromotionStatus:userId];
}

#pragma mark - Ventoura Package Communicator Deleagete

-(void) receivedPromotionImageData:(NSData *)objectNotation{
    NSLog(@"done");
    
    UIImage *image = [UIImage imageWithData:objectNotation];
    [self.delegate didReceivePromotionImage:image];
}

-(void)receivedEnterPromotionData:(NSData *)objectNotation{
    [self.delegate didReceivePromotionEnter];
}

-(void) receivedPromotionStatus:(NSData *)objectNotation response:(NSURLResponse *)response{
    //save image locally, also
    
    NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
    NSDictionary *dic = [httpResponse allHeaderFields];
    NSInteger contentLength = [dic[@"Content-Length"] integerValue];
    NSLog(@"response content: %@",dic[@"Content-Length"]);
    
    if (contentLength >0) {
        NSLog(@"we have data");
        DBManager* dbManager = [[DBManager alloc] initWithDatabaseFilename:@"ventouraDB.sql"];
        [ventouraDatabaseUtility savePromotion:dbManager ownerId:[ventouraUtility returnMyUserId]];
        //save the image at a place as well.
        
    }else{
        NSLog(@"we do not have data");
    }
    [self.delegate didReceivePromotionStatus];

}
-(void) fetchingPromotionJSONFailedWithError:(NSError *)error{

}

//- (void)receivedTripAndTourJson:(NSData * )objectNotation{
//    NSError *error = nil;
//
////    NSArray *TourList = [TourBuilder CreateTourTripListFromJSON:objectNotation error:&error];
////    if (error != nil) {
////        [self.delegate fetchingTourFailedWithError:error];
////        
////    } else {
////        [self.delegate didReceiveTourTripObject:TourList];
////    }
//}
//
//
//- (void)fetchingTourJSONFailedWithError:(NSError *)error
//{
////    [self.delegate fetchingTourFailedWithError:error];
//}

@end
