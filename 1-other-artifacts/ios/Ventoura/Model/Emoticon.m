//
//  Emoticon.m
//  Ventoura
//
//  Created by Wenchao Chen on 22/09/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "Emoticon.h"

@implementation Emoticon


-(instancetype)initWithEmoticonId:(NSString *)emoticonId emoticonName:(NSString *)emoticonName emoticonImage:(UIImage *)emoticonImage{
    self = [super init];
    if (self) {
        _emoticonId = emoticonId;
        _emoticonName = emoticonName;
        _emoticonImage = emoticonImage;
    }
    return self;
}
@end
