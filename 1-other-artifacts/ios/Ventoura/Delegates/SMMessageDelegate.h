//
//  SMMessageDelegate.h
//  Ventoura
//
//  Created by Wenchao Chen on 30/06/2014.
//  Copyright (c) 2014 Ventoura. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol SMMessageDelegate

- (void)newMessageReceived:(NSDictionary *)messageContent from:(NSString*) sender;

@end
