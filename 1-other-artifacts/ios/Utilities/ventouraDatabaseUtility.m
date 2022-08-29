//
//  ventouraDatabaseUtility.m
//  Ventoura
//
//  Created by Wenchao Chen on 16/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ventouraDatabaseUtility.h"

@implementation ventouraDatabaseUtility

#pragma Profiles
+(NSArray*) userDataFromDatabase:(DBManager *)dbManager userId:(NSString *)userId{


    NSString *query =[NSString stringWithFormat:@"select ProfileId, name, age, textBiography, country, city, isGuide, useravgReviewScoreRole, tourLength, tourPrice, paymentMethod, tourType, lastUpdated from UserProfile WHERE ProfileId like '%@'",userId];
    
    NSArray* results = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    return results;
}

+(void) saveTravellerProfileDataFromDatabase:(DBManager *)dbManager userId:(NSString *)userId name:(NSString *)name city:(NSString *)city country:(NSString *)country age:(NSString *)age textBio:(NSString *)textBio{
    
    NSString *query = [NSString stringWithFormat:@"INSERT OR REPLACE INTO UserProfile (ProfileId,name,age,textBiography,country,city,isGuide) VALUES ('%@','%@',%@,'%@',%@,%@,0)", userId, [ventouraUtility escapeStringSQL:name] ,age, [ventouraUtility escapeStringSQL:textBio], country, city];
    
    // Execute the query.
    [dbManager executeQuery:query];
    
    // If the query was successfully executed then pop the view controller.
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }

}

+(void) saveGuideProfileDataFromDatabase:(DBManager *)dbManager userId:(NSString *)userId name:(NSString *)name city:(NSString *)city country:(NSString *)country age:(NSString *)age textBio:(NSString *)textBio useravgReviewScore:(NSString *)useravgReviewScore tourLength:(NSString *)tourLength tourPrice:(NSString *)tourPrice paymentMethod:(NSString *)paymentMethod tourType:(NSString *)tourType{
     NSString *query = [NSString stringWithFormat:@"INSERT OR REPLACE INTO UserProfile (ProfileId,name,age,textBiography,country,city,isGuide,useravgReviewScoreRole, tourLength, tourPrice, paymentMethod,tourType) VALUES ('%@','%@',%@,'%@',%@,%@, 0,'%@','%@','%@','%@' ,'%@')", userId, [ventouraUtility escapeStringSQL:name] ,age, [ventouraUtility escapeStringSQL:textBio], country, city, useravgReviewScore, tourLength, tourPrice, paymentMethod,tourType];
    
    // Execute the query.
    [dbManager executeQuery:query];
    
    // If the query was successfully executed then pop the view controller.
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }


}



#pragma attractions

+(void)saveAttractions:(DBManager *)dbManager attractionId:(NSString *)attractionId userId:(NSString *)userId attractionName:(NSString *)attractionName{
    NSString *query = [NSString stringWithFormat:@"INSERT INTO Attraction (attractionId,attractionName,ownerId) VALUES (%@,'%@',%@)", attractionId,[ventouraUtility escapeStringSQL:attractionName],userId];
    // Execute the query.
    [dbManager executeQuery:query];
    
    // If the query was successfully executed then pop the view controller.
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }


}

+(void) deleteAttractions:(DBManager *)dbManager attractionId:(NSString *)attractionId{
    NSString *query = [NSString stringWithFormat:@"delete from Attraction where attractionId = %@",attractionId];
    [dbManager executeQuery:query];
}

+(void) flushAttractionData:(DBManager *)dbManager userId:(NSString *)userId{
    NSString *query = [NSString stringWithFormat:@"delete from Attraction where ownerId = %@",userId];
    [dbManager executeQuery:query];
}

+(NSArray*)userAttractionsFromDatabase:(DBManager *)dbManager userId:(NSString *)userId{
    NSString *query =[NSString stringWithFormat:@"select attractionId,attractionName,ownerId from Attraction WHERE ownerId = %@",userId];
    
    NSArray* results = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    return results;
}

#pragma Images

+(void) saveTravellerProfileImageDataToDatabase:(DBManager *)dbManager ownerId:(NSString*)ownerId  userId:(NSString *)userId imgId:(NSString *)imgId isUserGuide:(BOOL)isUserGuide isPortal:(BOOL )isPortal{

    NSString * booleanStringGuide = (isUserGuide) ? @"1" : @"0";
    NSString * booleanStringPortal = (isPortal) ? @"1" : @"0";
    NSString *query = [NSString stringWithFormat:@"INSERT INTO Image (ventouraImageId,ownerId, userId, isUserGuide,isPortal) VALUES (%@,'%@',%@,%@,%@)", imgId, ownerId, userId, booleanStringGuide, booleanStringPortal];

    // Execute the query.
    [dbManager executeQuery:query];
    
    // If the query was successfully executed then pop the view controller.
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }
}

+(void)flushImageData:(DBManager *)dbManager ownerId:(NSString *)ownerId userId:(NSString *)userId isUserGuide:(BOOL)isUserGuide{

    NSString * booleanStringGuide = (isUserGuide) ? @"1" : @"0";

    NSString *query = [NSString stringWithFormat:@"delete from Image where ownerId like '%@' AND userId = %@ AND isUserGuide = %@", ownerId,userId,booleanStringGuide];
    
    [dbManager executeQuery:query];

}
+(void) deleteImageData:(DBManager *)dbManager ownerId:(NSString *)ownerId imageId:(NSString *)imageId userId:(NSString *)userId isUserGuide:(BOOL)isUserGuide{
    
    NSString * booleanStringGuide = (isUserGuide) ? @"1" : @"0";
    NSString *query = [NSString stringWithFormat:@"delete from Image where ownerId like '%@' AND userId = %@ AND isUserGuide = %@ AND imageId = %@", ownerId,userId,booleanStringGuide,imageId];
    
    [dbManager executeQuery:query];


}
+(NSArray*) userImageDataFromDatabase:(DBManager*)dbManager ownerId:(NSString*) ownerId userId:(NSString*) userId isUserGuide:(BOOL)isUserGuide{
    NSString * booleanStringGuide = (isUserGuide) ? @"1" : @"0";
    NSString *query =[NSString stringWithFormat:@"select ventouraImageId, isUserGuide,isPortal,userId from Image WHERE ownerId like '%@' and userId = %@ and isUserGuide = %@",ownerId,userId,booleanStringGuide];
    
    NSArray* results = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    return results;

}

+(void) savePromotion:(DBManager *)dbManager ownerId:(NSString *)ownerId{
    NSString *query = [NSString stringWithFormat:@"insert into Promotion values(null, 1, '%@')", ownerId];
    [dbManager executeQuery:query];
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }
}


+(void) saveMatchListUser:(DBManager *)dbManager ownerId:(NSString *)ownerId userId:(NSString *)userId userName:(NSString *)userName matchTime:(NSString *)matchTime{
    
    //add match date into this when parsed properly
    NSString *query = [NSString stringWithFormat:@"INSERT or replace INTO Match (matchId,ownerId,matchUserId,unreadCount,isNewMatch,matchDate, matchUserName) VALUES ('%@%@','%@','%@',  IFNULL( (select unreadCount from Match where matchId like '%@%@'), 0 ), 0, '%@', '%@')",ownerId, userId, ownerId, userId, ownerId, userId, matchTime ,userName];
    NSLog(@" query ownerId : %@. userId, %@",ownerId,userId);
    NSLog(@"query in saveMatchListUser: %@,", query);

    // Execute the query.
    [dbManager executeQuery:query];
    
    // If the query was successfully executed then pop the view controller.
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query in saveMatchListUser: %@,", query);
    }
    
}

+(NSArray*) lastMessageFromDatabase:(DBManager *)dbManager ownerId:(NSString *)ownerId userId:(NSString *)userId{
    // Form the query.
    NSString *query =[NSString stringWithFormat:@"select * from ChatMessage where ownerID like '%@' AND (fromId like '%@' and toId like '%@') OR (fromId like '%@' and toId like '%@')  ORDER BY messageId DESC LIMIT 1 ", ownerId,ownerId,userId, userId,ownerId];
    NSArray *result = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    return result;

}

+(NSArray*) unreadCountFromDatabase:(DBManager *)dbManager ownerId:(NSString *)ownerId userId:(NSString *)userId{

    
    NSString *query2 =[NSString stringWithFormat:@"select unreadCount from Match where ownerId like '%@' AND matchUserId like '%@'", ownerId, userId];
    NSArray *badges = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query2]];
    return  badges;

}

+(void) saveMessageToDatabase:(DBManager *)dbManager ownerId:(NSString *)ownerId userId:(NSString *)userId msg:(NSString *)msg{
    
    NSString *query = [NSString stringWithFormat:@"insert into ChatMessage values(null, '%@', '%@', '%@', current_timestamp, '%@')", userId, ownerId, ownerId, [ventouraUtility escapeStringSQL: msg]];
    
    // Execute the query.
    [dbManager executeQuery:query];
    
    // If the query was successfully executed then pop the view controller.
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query.");
    }
}
+(void) updateUnreadCount:(DBManager *)dbManager ownerId:(NSString *)ownerId userId:(NSString *)userId{
    NSString *updateUnread = [NSString stringWithFormat:@"UPDATE Match SET unreadCount= unreadCount+1 WHERE ownerId like '%@' AND matchUserId like '%@'", ownerId      , userId];
    
    // Execute the query.
    [dbManager executeQuery:updateUnread];
    
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"Could not execute the query. Update Unread");
        
        //no match saved, should I save this?
        NSString *insertUnread = [NSString stringWithFormat:@"insert into Match values('%@%@', '%@', '%@', 1,0,null,'user') ",ownerId, userId, [ventouraUtility returnMyUserIdWithType], userId];
        [dbManager executeQuery:insertUnread];
        if (dbManager.affectedRows != 0) {
            NSLog(@"Query was executed successfully. Inserted Unread. Affected rows = %d", dbManager.affectedRows);
        }
        else{
            NSLog(@"Could not execute the query.");
        }
        
    }

}

+(void) saveTravellerFilterSettingsToDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId  isUserGuide:(BOOL)isUserGuide gender:(NSString*)gender userType:(NSString*)userType ageMin:(NSString*)ageMin ageMax:(NSString*)ageMax priceMin:(NSString*)priceMin priceMax:(NSString*)priceMax cities:(NSArray*)cities{
    //update
    //if no update
    //then insert
    NSString* userid = [ventouraUtility returnUserIdWithType:isUserGuide ventouraId:ownerId];
    NSString * query = [NSString stringWithFormat:@"UPDATE CurrentUserFilterSettings SET gender = '%@', userType = '%@', ageMin = %@, ageMax = %@, priceMin = %@, priceMax = %@  WHERE userid like '%@'", gender, userType, ageMin, ageMax, priceMin, priceMax, userid];
    NSLog(@"%@",query);
    // Execute the query.
    [dbManager executeQuery:query];
    
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"no row exists, so make one");
        
        //no match saved, should I save this?
        NSString *insertUnread = [NSString stringWithFormat:@"insert into CurrentUserFilterSettings (userid, gender, userType, ageMin, ageMax, priceMin, priceMax) values('%@', '%@', '%@', %@, %@, %@, %@) ",userid, gender, userType, ageMin, ageMax, priceMin, priceMax];
        [dbManager executeQuery:insertUnread];
        if (dbManager.affectedRows != 0) {
            NSLog(@"Query was executed successfully. Inserted Unread. Affected rows = %d", dbManager.affectedRows);
        }
        else{
            NSLog(@"Could not execute the query.");
        }
        
    }
    
    //now the city selection data
    //delete all since this is dynamic and changes as it goes.
    
    query = [NSString stringWithFormat:@"DELETE FROM CurrentUserFilterSettingsCitySelection"];
    
    // Execute the query.
    [dbManager executeQuery:query];
    
    //now save all cities selected
    for(int i = 0; i < cities.count; i++)
    {
        query = [NSString stringWithFormat: @"INSERT INTO  CurrentUserFilterSettingsCitySelection (city, ownerId) VALUES (%@, %@)", cities[i], userid];
    }
}

+(void) saveGuideFilterSettingsToDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId  isUserGuide:(BOOL)isUserGuide gender:(NSString*)gender ageMin:(NSString*)ageMin ageMax:(NSString*)ageMax autoMatch:(BOOL)autoMatch{
    NSString* userid = [ventouraUtility returnUserIdWithType:isUserGuide ventouraId:ownerId];
    NSString* autoMatchStr = @"0";
    if(autoMatch) autoMatchStr = @"1";
        
    NSString * query = [NSString stringWithFormat:@"UPDATE CurrentUserFilterSettings SET gender = '%@', ageMin = %@, ageMax = %@, autoMatch = %@  WHERE userid like '%@'", gender, ageMin, ageMax, autoMatchStr, userid];
    
    NSLog(@"%@",query);
    
    // Execute the query.
    [dbManager executeQuery:query];
    
    if (dbManager.affectedRows != 0) {
        NSLog(@"Query was executed successfully. Affected rows = %d", dbManager.affectedRows);
    }
    else{
        NSLog(@"no row exists, so make one");
        
        NSString *insertUnread = [NSString stringWithFormat:@"insert into CurrentUserFilterSettings (userid, gender, ageMin, ageMax, autoMatch) values('%@', '%@', %@, %@, %@) ",userid, gender, ageMin, ageMax, autoMatchStr];
        NSLog(@"%@",insertUnread);
        [dbManager executeQuery:insertUnread];
        if (dbManager.affectedRows != 0) {
            NSLog(@"Query was executed successfully. Inserted. Affected rows = %d", dbManager.affectedRows);
        }
        else{
            NSLog(@"Could not execute the query.");
        }
        
    }
}

+(NSArray*) getFilterSettingsFromDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId isUserGuide:(BOOL)isUserGuide{
    NSString* userid = [ventouraUtility returnUserIdWithType:isUserGuide ventouraId:ownerId];
    NSString *query =[NSString stringWithFormat:@"select gender, userType, ageMin, ageMax, priceMin, priceMax, autoMatch from CurrentUserFilterSettings where userid like '%@'", userid];
    
    NSArray* result = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    
    return result;
}

+(NSArray*) getFilterSettingsCitySelectionFromDatabase:(DBManager*) dbManager ownerId:(NSString*)ownerId isUserGuide:(BOOL)isUserGuide{
    NSString* userid = [ventouraUtility returnUserIdWithType:isUserGuide ventouraId:ownerId];
    NSString *query =[NSString stringWithFormat:@"select citySelection from CurrentUserFilterSettingsCitySelection where ownerId like '%@'", userid];
    
    NSArray* result = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    
    return result;

    
}

+(NSArray*) buddyListFromDatabase:(DBManager *)dbManager ownerId:(NSString *)ownerId{
    NSString *query =[NSString stringWithFormat:@"select matchUserId, matchUserName, unreadCount, isNewMatch, matchDate from Match WHERE ownerId like '%@'",ownerId];
    
    NSArray* results = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    return results;


}
+(NSArray*) getCityFromDatabase:(DBManager *)dbManager cityId:(NSString *)cityId{
    NSString *query =[NSString stringWithFormat:@"select cityName, countryId from City WHERE id =%@", cityId];
    
    NSArray* results = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    return results;
}

+(NSArray*) getTourTypeFromDatabase:(DBManager *)dbManager tourTypeId:(NSString *)tourType{

    NSString *query =[NSString stringWithFormat:@"select tourName from tourType WHERE tourId =%@",tourType];
    NSArray* results = [[NSArray alloc] initWithArray:[dbManager loadDataFromDB:query]];
    return results;
}
@end
