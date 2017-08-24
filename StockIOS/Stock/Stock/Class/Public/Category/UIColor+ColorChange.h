//
//  UIColor+ColorChange.h
//  Overseas
//
//  Created by xiesy on 17/2/24.
//  Copyright © 2017年 xiesy. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIColor (ColorChange)
// 颜色转换：iOS中（以#开头）十六进制的颜色转换为UIColor(RGB)
+ (UIColor *) colorWithHexStringChange: (NSString *)color;
@end
