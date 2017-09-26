//
//  NSObject+WKHud.h
//  Stock
//
//  Created by mac on 2017/9/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSObject (WKHud)

- (void)showWaitHud;

- (void)showMessageHud:(NSString *_Nullable)message hideAfter:(NSTimeInterval )timer;

- (void)hideHud;

@end
