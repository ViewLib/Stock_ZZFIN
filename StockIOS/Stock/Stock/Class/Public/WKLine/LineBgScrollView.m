//
//  LineBgScrollView.m
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import "LineBgScrollView.h"
#import <Masonry/Masonry.h>

@implementation LineBgScrollView

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
    [self drawBgLines];
}

- (void)drawBgLines {
    //单纯的画了一下背景线
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGContextSetStrokeColorWithColor(ctx, [UIColor blackColor].CGColor);
    CGContextSetLineWidth(ctx, 0.5);
    CGFloat unitHeight = (self.frame.size.height)/6;
    
    const CGPoint line1[] = {CGPointMake(0, 1),CGPointMake(self.contentSize.width, 1)};
    const CGPoint line2[] = {CGPointMake(0, unitHeight),CGPointMake(self.contentSize.width, unitHeight)};
    const CGPoint line3[] = {CGPointMake(0, unitHeight*2),CGPointMake(self.contentSize.width, unitHeight*2)};
    const CGPoint line4[] = {CGPointMake(0, unitHeight*3),CGPointMake(self.contentSize.width, unitHeight*3)};
    const CGPoint line5[] = {CGPointMake(0, unitHeight*4),CGPointMake(self.contentSize.width, unitHeight*4)};
    const CGPoint line6[] = {CGPointMake(0, unitHeight*5),CGPointMake(self.contentSize.width, unitHeight*5)};
    
    CGContextStrokeLineSegments(ctx, line1, 2);
    CGContextStrokeLineSegments(ctx, line2, 2);
    CGContextStrokeLineSegments(ctx, line3, 2);
    CGContextStrokeLineSegments(ctx, line4, 2);
    CGContextStrokeLineSegments(ctx, line5, 2);
    CGContextStrokeLineSegments(ctx, line6, 2);
}

- (UIView *)contentView {
    if (!_contentView) {
        _contentView = [UIView new];
        [self addSubview:_contentView];
        [_contentView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
    }
    return _contentView;
}

- (void)setContentSize:(CGSize)contentSize {
    [super setContentSize:contentSize];
    [_contentView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.left.equalTo(self);
        make.width.equalTo(@(contentSize.width));
        make.height.equalTo(self);
    }];
}

@end
