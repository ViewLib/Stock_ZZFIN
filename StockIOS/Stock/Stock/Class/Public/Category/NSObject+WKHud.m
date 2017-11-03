//
//  NSObject+WKHud.m
//  Stock
//
//  Created by mac on 2017/9/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "NSObject+WKHud.h"
#import "MBProgressHUD.h"
#import <objc/runtime.h>

static const void *loadHud = &loadHud;

@implementation NSObject (WKHud)

- (MBProgressHUD *)Hud {
    return objc_getAssociatedObject(self, loadHud);
}

- (void)setHud:(MBProgressHUD *)hud {
    objc_setAssociatedObject(self, loadHud, hud, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (void)showWaitHud {
    UIView *view = [[UIApplication sharedApplication].delegate window];
    [view.subviews enumerateObjectsUsingBlock:^(__kindof UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isKindOfClass:[MBProgressHUD class]]) {
            [obj removeFromSuperview];
        }
    }];
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.removeFromSuperViewOnHide = YES;
    [self setHud:hud];
}

- (void)showHudWithMessage:(NSString *)message {
    UIView *view = [[UIApplication sharedApplication].delegate window];
    [view.subviews enumerateObjectsUsingBlock:^(__kindof UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isKindOfClass:[MBProgressHUD class]]) {
            [obj removeFromSuperview];
        }
    }];
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.mode = MBProgressHUDModeText;
    hud.labelText = message;
    hud.removeFromSuperViewOnHide = YES;
    [self setHud:hud];
}

- (void)showMessageHud:(NSString *)message hideAfter:(NSTimeInterval )timer {
    UIWindow *view = [[UIApplication sharedApplication].delegate window];
    [view.subviews enumerateObjectsUsingBlock:^(__kindof UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isKindOfClass:[MBProgressHUD class]]) {
            [obj removeFromSuperview];
        }
    }];
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.mode = MBProgressHUDModeText;
    hud.labelText = message;
    hud.margin = 10.f;
    hud.removeFromSuperViewOnHide = YES;
    [hud hide:YES afterDelay:timer];
}

- (void)hideHud {
    [[self Hud] hide:YES];
}

@end
