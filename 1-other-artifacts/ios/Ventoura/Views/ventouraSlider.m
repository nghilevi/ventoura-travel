//
//  ventouraSlider.m
//  Ventoura
//
//  Created by Wenchao Chen on 8/11/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import "ventouraSlider.h"

@implementation ventouraSlider

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (CGRect)trackRectForBounds:(CGRect)bounds{
//    CGRect customBounds = ...
//    return customBounds;
    return CGRectMake(bounds.origin.x, bounds.origin.y, bounds.size.width, 20);
}

@end
