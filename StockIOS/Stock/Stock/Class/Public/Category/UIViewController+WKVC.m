//
//  UIViewController+WKVC.m
//  Stock
//
//  Created by mac on 2017/9/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "UIViewController+WKVC.h"

@implementation UIViewController (WKVC)

/*
 * 判断是否登陆
 * 若未登陆,则进入登陆界面登陆,并
 */
- (void)tipsForLoginSuccess:(void(^)(void))sc {
    if (isLogin) {
        if (sc) {
            sc();
        }
    } else {
        [self successAlert:@"是否登录" one:^{
            AppDelegate *delegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
            delegate.loginVC.login_success = ^{
                if (sc) {
                    sc();
                }
            };
            [self presentViewController:delegate.loginVC animated:YES completion:nil];
        } two:^{
            
        } oneT:@"确定" twoT:@"取消"];
    }
}

/*
 * 判断是否登陆
 * 若未登陆,则进入登陆界面登陆,并
 */
- (void)sexChangeActionSheet:(void(^)(NSString *sex))sc {
    
    [self ActionSheet:@"请选择" one:^{
        if (sc) {
            sc(@"先生");
        }
    } two:^{
        if (sc) {
            sc(@"女士");
        }
    } oneT:@"先生" twoT:@"女士"];
}

/**
 初始化一个alertView

 @param message alertView的title
 @param oneB 第一个Button的click时间的block
 @param twoB 第二个Button的click时间的block
 @param oneT 第一个Button的名字
 @param twoT 第二个Button的名字
 @return 返回alertView
 */
- (UIAlertController *)successAlert:(NSString *)message one:(void (^)(void))oneB two:(void (^)(void))twoB oneT:(NSString *)oneT twoT:(NSString *)twoT {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:message preferredStyle:UIAlertControllerStyleAlert];
    if (oneT.length > 0) {
        UIAlertAction *act1 = [UIAlertAction actionWithTitle:oneT style:0 handler:^(UIAlertAction * _Nonnull action) {
            if (oneB) {
                oneB();
            }
        }];
        [alert addAction:act1];
    }
    if (twoT.length > 0) {
        UIAlertAction *act2 = [UIAlertAction actionWithTitle:twoT style:0 handler:^(UIAlertAction * _Nonnull action) {
            if (twoB) {
                twoB();
            }
        }];
        [alert addAction:act2];
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self presentViewController:alert animated:YES completion:nil];
    });
    
    return alert;
}

/**
 初始化一个ActionSheet
 
 @param message alertView的title
 @param oneB 第一个Button的click时间的block
 @param twoB 第二个Button的click时间的block
 @param oneT 第一个Button的名字
 @param twoT 第二个Button的名字
 @return 返回alertView
 */
- (UIAlertController *)ActionSheet:(NSString *)message one:(void (^)(void))oneB two:(void (^)(void))twoB oneT:(NSString *)oneT twoT:(NSString *)twoT {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:message preferredStyle:UIAlertControllerStyleActionSheet];
    UIAlertAction *act1 = [UIAlertAction actionWithTitle:oneT style:0 handler:^(UIAlertAction * _Nonnull action) {
        if (oneB) {
            oneB();
        }
    }];
    UIAlertAction *act2 = [UIAlertAction actionWithTitle:twoT style:0 handler:^(UIAlertAction * _Nonnull action) {
        if (twoB) {
            twoB();
        }
    }];
    [alert addAction:act1];
    [alert addAction:act2];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self presentViewController:alert animated:YES completion:nil];
    });
    
    return alert;
}

@end
