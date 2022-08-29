//
//  UserImage.m
//  Ventoura
//
//  Created by Wenchao Chen on 21/08/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "UserImage.h"

@implementation UserImage
-(instancetype)initWithImageId:(NSString *)imageId ownerId:(NSString *)ownerId isInMeory:(BOOL)isInMeory img:(UIImage *)img imgOriginal:(UIImage*)imgOriginal path:(NSString *)path{
    self = [super init];
    if (self) {
        _imageId = imageId;
        _ownerId = ownerId;
        _img = img;
        _imgOriginal = imgOriginal;
        _path = path;
        _isInMeory = isInMeory;
    }
    return self;

}
@end
