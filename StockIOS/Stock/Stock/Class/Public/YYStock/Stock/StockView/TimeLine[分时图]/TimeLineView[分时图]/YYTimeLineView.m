//
//  YYTimeLineView.m
//  YYStock  ( https://github.com/WillkYang )
//
//  Created by WillkYang on 16/10/5.
//  Copyright © 2016年 WillkYang. All rights reserved.
//

#import "YYTimeLineView.h"
#import "YYStockConstant.h"
#import "YYStockVariable.h"
#import "UIColor+YYStockTheme.h"

@interface YYTimeLineView()
@property (nonatomic, strong) NSMutableArray *drawPositionModels;

@property (nonatomic, strong) NSMutableArray *drawPJPositionModels;
@end

@implementation YYTimeLineView



- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
    
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    if (!self.drawPositionModels) {
        return;
    }
    
    
    CGContextSetLineWidth(ctx, YYStockTimeLineWidth);
    CGPoint firstPoint = [self.drawPositionModels.firstObject CGPointValue];
    
    CGPoint firstPJPoint = [self.drawPJPositionModels.firstObject CGPointValue];
    
    if (isnan(firstPoint.x) || isnan(firstPoint.y) || isnan(firstPJPoint.x) || isnan(firstPJPoint.y)) {
        return;
    }
    NSAssert(!isnan(firstPoint.x) && !isnan(firstPoint.y), @"出现NAN值：MA画线");
    
    //画分时线
    CGContextSetStrokeColorWithColor(ctx, [UIColor YYStock_TimeLineColor].CGColor);
    CGContextMoveToPoint(ctx, firstPoint.x, firstPoint.y);
    for (NSInteger idx = 1; idx < self.drawPositionModels.count ; idx++)
    {
        CGPoint point = [self.drawPositionModels[idx] CGPointValue];
        CGContextAddLineToPoint(ctx, point.x, point.y);
    }
    CGContextStrokePath(ctx);
    
    //画分时线价格均线
    CGContextSetStrokeColorWithColor(ctx, [UIColor YYStock_PJTimeLineColor].CGColor);
    CGContextMoveToPoint(ctx, firstPoint.x, firstPoint.y);
    for (NSInteger idx = 1; idx < self.drawPJPositionModels.count ; idx++)
    {
        CGPoint point = [self.drawPJPositionModels[idx] CGPointValue];
        NSLog(@"%f,,,%f",point.x,point.y);
        CGContextAddLineToPoint(ctx, point.x, point.y);
    }
    CGContextStrokePath(ctx);
    
    CGContextSetFillColorWithColor(ctx, [UIColor YYStock_timeLineBgColor].CGColor);
    CGPoint lastPoint = [self.drawPositionModels.lastObject CGPointValue];
    
    //画背景色
    CGContextMoveToPoint(ctx, firstPoint.x, firstPoint.y);
    for (NSInteger idx = 1; idx < self.drawPositionModels.count ; idx++)
    {
        CGPoint point = [self.drawPositionModels[idx] CGPointValue];
        CGContextAddLineToPoint(ctx, point.x, point.y);
    }
    CGContextAddLineToPoint(ctx, lastPoint.x, CGRectGetMaxY(self.frame));
    CGContextAddLineToPoint(ctx, firstPoint.x, CGRectGetMaxY(self.frame));
    CGContextClosePath(ctx);
    CGContextFillPath(ctx);
}

- (NSArray *)drawViewWithXPosition:(CGFloat)xPosition drawModels:(NSArray <id<YYStockTimeLineProtocol>>*)drawLineModels  maxValue:(CGFloat)maxValue minValue:(CGFloat)minValue {
    NSAssert(drawLineModels, @"数据源不能为空");
    
    //转换为实际坐标
    [self convertToPositionModelsWithXPosition:xPosition drawLineModels:drawLineModels maxValue:maxValue minValue:minValue];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self setNeedsDisplay];
    });
    return [self.drawPositionModels copy];
}

- (NSArray *)convertToPositionModelsWithXPosition:(CGFloat)startX drawLineModels:(NSArray <id<YYStockTimeLineProtocol>>*)drawLineModels  maxValue:(CGFloat)maxValue minValue:(CGFloat)minValue {
    if (!drawLineModels) return nil;
    
    [self.drawPositionModels removeAllObjects];
    [self.drawPJPositionModels removeAllObjects];
    
    CGFloat minY = YYStockLineMainViewMinY;
    CGFloat maxY = self.frame.size.height - YYStockLineMainViewMinY;
    CGFloat unitValue = (maxValue - minValue)/(maxY - minY);
    
    [drawLineModels enumerateObjectsUsingBlock:^(id<YYStockTimeLineProtocol>  _Nonnull model, NSUInteger idx, BOOL * _Nonnull stop) {
        CGFloat xPosition = startX + idx * ([YYStockVariable timeLineVolumeWidth] + YYStockTimeLineViewVolumeGap);
        CGPoint pricePoint = CGPointMake(xPosition, ABS(maxY - (model.Price.floatValue - minValue)/unitValue));
        [self.drawPositionModels addObject:[NSValue valueWithCGPoint:pricePoint]];
        
        CGPoint pjpricePoint = CGPointMake(xPosition, ABS(maxY - (model.Pjprice - minValue)/unitValue));
        [self.drawPJPositionModels addObject:[NSValue valueWithCGPoint:pjpricePoint]];
     }];
    
    return self.drawPositionModels ;
}
- (NSMutableArray *)drawPositionModels {
    if (!_drawPositionModels) {
        _drawPositionModels = [NSMutableArray array];
    }
    return _drawPositionModels;
}

- (NSMutableArray *)drawPJPositionModels {
    if (!_drawPJPositionModels) {
        _drawPJPositionModels = [NSMutableArray array];
    }
    return _drawPJPositionModels;
}

@end
