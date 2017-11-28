//
//  WKSLineView.m
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import "WKSLineView.h"
#import "WKLineView.h"
#import "WKNewView.h"

@interface WKSLineView()

@property (nonatomic, strong) NSMutableArray *drawPositionModels;

@property (nonatomic, strong) NSArray *drawLineModels;

@property (nonatomic, strong) NSMutableArray *Positions;

@property (nonatomic, strong) NSMutableArray *NewPoint;

@end

@implementation WKSLineView

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
    
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    
    if(self.Positions.count > 0) {
        WKLineView *Line = [[WKLineView alloc]initWithContext:ctx];
        [Line drawWithColor:MAIN_COLOR maPositions:self.Positions];
    }
    
    if (self.NewPoint.count > 0) {
        WKNewView *new = [[WKNewView alloc] initWithContext:ctx];
        [new drawWithColor:MAIN_COLOR maPositions:self.NewPoint];
    }
}

- (NSArray *)drawViewWithXPosition:(CGFloat)xPosition drawModels:(NSMutableArray *)drawLineModels newValues:(NSMutableDictionary *)newValue maxValue:(CGFloat)maxValue minValue:(CGFloat)minValue {
    NSAssert(drawLineModels, @"数据源不能为空");
    //转换为实际坐标
    [self convertToPositionModelsWithXPosition:xPosition drawLineModels:drawLineModels newValues:newValue maxValue:maxValue minValue:minValue];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self setNeedsDisplay];
    });
    return [self.Positions copy];
}

- (NSArray *)convertToPositionModelsWithXPosition:(CGFloat)startX drawLineModels:(NSArray *)drawLineModels newValues:(NSMutableDictionary *)newValues maxValue:(CGFloat)maxValue minValue:(CGFloat)minValue {
    if (!drawLineModels) return nil;

    _drawLineModels = drawLineModels;
    [self.Positions removeAllObjects];
    self.NewPoint = [NSMutableArray array];

    CGFloat minY = 0;
    CGFloat maxY = self.frame.size.height - 0;
    CGFloat unitValue = (maxValue - minValue)/(maxY - minY);

    if (unitValue == 0) unitValue = 0.01f;
    WS(self)
    [drawLineModels enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        CGFloat xPosition = startX + idx * (LINEGAP + LINE);
        float price = [obj[@"close"] floatValue];
        [selfWeak.Positions addObject: [NSValue valueWithCGPoint:CGPointMake(xPosition, ABS(maxY - (price - minValue)/unitValue))]];
        NSString *time = obj[@"day"];
        if (newValues[time]) {
            [selfWeak.NewPoint addObject:[NSValue valueWithCGPoint:CGPointMake(xPosition, ABS(maxY - (price - minValue)/unitValue))]];
        }
    }];

    return self.drawPositionModels ;
}

- (NSMutableArray *)Positions {
    if (!_Positions) {
        _Positions = [NSMutableArray array];
    }
    return _Positions;
}

- (NSMutableArray *)NewPoint {
    if (!_NewPoint) {
        _NewPoint = [NSMutableArray array];
    }
    return _NewPoint;
}

@end
