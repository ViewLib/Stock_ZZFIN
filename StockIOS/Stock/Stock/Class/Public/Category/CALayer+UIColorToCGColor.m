//
//  CALayer+UIColorToCGColor.m
//  Overseas
//
//  Created by wuhongwei on 17/3/9.
//  Copyright © 2017年 quyi. All rights reserved.
//

#import "CALayer+UIColorToCGColor.h"

@implementation CALayer (UIColorToCGColor)
- (void)setBorderColorWithUIColor:(UIColor *)color
{
    self.borderColor = color.CGColor;
}
@end
