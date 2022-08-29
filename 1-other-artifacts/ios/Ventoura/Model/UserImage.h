//
//  UserImage.h
//  Ventoura
//
//  Created by Wenchao Chen on 21/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface UserImage : NSObject
@property (nonatomic, copy) NSString *imageId;
@property (nonatomic, copy) NSString *ownerId;
@property (nonatomic, assign) BOOL isInMeory;
@property (nonatomic, strong) UIImage *img;
@property (nonatomic, strong) UIImage *imgOriginal;
@property (nonatomic, copy) NSString *path;

//@property (nonatomic, copy) NSString *startTime;
//@property (nonatomic, copy) NSString *numberOfDays;

- (instancetype)initWithImageId:(NSString *)imageId
                        ownerId:(NSString *)ownerId
                      isInMeory:(BOOL)isInMeory
                            img:(UIImage*)img
                    imgOriginal:(UIImage*)imgOriginal
                           path:(NSString*)path;
@end
