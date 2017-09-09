//
//  UIButton+WKType.m
//  Stock
//
//  Created by mac on 2017/9/10.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "UIButton+WKType.h"

@implementation UIButton (WKType)

/**
 图片在上，文字在下的样式
 */
- (void)ImgTopTextButtom {
    [self setTitleEdgeInsets:UIEdgeInsetsMake(0, -self.currentImage.size.width, -self.currentImage.size.height-2, 0)];
    [self setImageEdgeInsets:UIEdgeInsetsMake(-self.titleLabel.intrinsicContentSize.height-2, 0, 0, -self.titleLabel.intrinsicContentSize.width)];
}

/**
 图片在右，文字在左的样式
 */
- (void)ImgRightTextLeft {
    [self setTitleEdgeInsets:UIEdgeInsetsMake(0, -self.currentImage.size.width, 0, self.currentImage.size.width)];
    [self setImageEdgeInsets:UIEdgeInsetsMake(0, self.titleLabel.intrinsicContentSize.width+8, 0, -self.titleLabel.intrinsicContentSize.width)];
}

@end
