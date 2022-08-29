//
//  DBManager.h
//  Ventoura
//
//  Created by Wenchao Chen on 24/07/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DBManager : NSObject
@property (nonatomic, strong) NSMutableArray *arrColumnNames;
@property (nonatomic) int affectedRows;
@property (nonatomic) long long lastInsertedRowID;
-(instancetype)initWithDatabaseFilename:(NSString *)dbFilename;
-(NSArray *)loadDataFromDB:(NSString *)query;
-(void)executeQuery:(NSString *)query;
@end
