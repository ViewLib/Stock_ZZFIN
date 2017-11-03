//
//  WKLineView.m
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import "WKLineView.h"

@interface WKLineView()
/**
 绘制上下文
 */
@property (nonatomic, assign) CGContextRef context;

@property (nonatomic, strong) NSArray *MAPositions;

@property (nonatomic, strong) UIColor *lineColor;
@end

@implementation WKLineView

/**
 *  根据context初始化画线
 */
- (instancetype)initWithContext:(CGContextRef)context
{
    self = [super init];
    if(self)
    {
        self.context = context;
    }
    return self;
}

- (void)drawRect:(CGRect)rect {
    [super drawRect: rect];
    
}

- (void)drawWithColor:(UIColor *)lineColor maPositions:(NSArray *)maPositions {
    _MAPositions = maPositions;
    _lineColor = lineColor;
    if(!self.context || !self.MAPositions) {
        return;
    }
    
    CGContextSetStrokeColorWithColor(self.context, self.lineColor.CGColor);
    
    CGContextSetLineWidth(self.context, LINEWIDE);
    
    CGPoint firstPoint = [self.MAPositions.firstObject CGPointValue];

    CGContextMoveToPoint(self.context, firstPoint.x, firstPoint.y);
    
    for (NSInteger idx = 1; idx < self.MAPositions.count ; idx++)
    {
        CGPoint point = [self.MAPositions[idx] CGPointValue];
        CGContextAddLineToPoint(self.context, point.x, point.y);
    }
    
    CGContextStrokePath(self.context);
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
