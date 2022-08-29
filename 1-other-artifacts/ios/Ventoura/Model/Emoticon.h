//
//  Emoticon.h
//  Ventoura
//
//  Created by Wenchao Chen on 22/09/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Emoticon : NSObject
@property (nonatomic, copy) NSString *emoticonId;
@property (nonatomic, copy) NSString *emoticonName;
@property (nonatomic, copy) UIImage *emoticonImage;


- (instancetype)initWithEmoticonId:(NSString *)emoticonId
                      emoticonName:(NSString *)emoticonName
                     emoticonImage:(UIImage *)emoticonImage;


@end
