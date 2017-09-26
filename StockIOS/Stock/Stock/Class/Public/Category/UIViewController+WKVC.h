//
//  UIViewController+WKVC.h
//  Stock
//
//  Created by mac on 2017/9/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIViewController (WKVC)

/**
 判断是否登录成功

 @param sc 登录成功以后的Block回调，若未登陆,则进入登陆界面登陆
 */
- (void)tipsForLoginSuccess:(void(^)(void))sc;

/**
 初始化一个alertView
 
 @param message alertView的title
 @param oneB 第一个Button的click时间的block
 @param twoB 第二个Button的click时间的block
 @param oneT 第一个Button的名字
 @param twoT 第二个Button的名字
 @return 返回alertView
 */
- (UIAlertController *)successAlert:(NSString *)message one:(void (^)(void))oneB two:(void (^)(void))twoB oneT:(NSString *)oneT twoT:(NSString *)twoT;

@end
