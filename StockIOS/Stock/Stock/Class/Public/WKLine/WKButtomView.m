//
//  WKButtomView.m
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//
#import "WKButtomView.h"
#import "WKVolumePositionModel.h"

@interface WKButtomView()

@property (nonatomic, strong) NSMutableArray *drawPositionModels;

/**
 上面K线的位置models数组
 */
@property (nonatomic, strong) NSArray *linePositionModels;


@property (nonatomic, strong) NSArray *drawLineModels;

@end

@implementation WKButtomView

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
    
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    if (!self.drawPositionModels) {
        return;
    }
    
    __block CGFloat lastRectX = 0;
    
    __block BOOL firstFlag = YES;
    
    [[[self.drawPositionModels reverseObjectEnumerator] allObjects] enumerateObjectsUsingBlock:^(WKVolumePositionModel  *_Nonnull pModel, NSUInteger idx, BOOL * _Nonnull stop) {
        //绘制日期
        if (pModel.DayDesc.length > 0) {
            NSDictionary *attribute = @{NSFontAttributeName:[UIFont systemFontOfSize:10],NSForegroundColorAttributeName:[UIColor lightGrayColor]};
            CGRect rect1 = [pModel.DayDesc boundingRectWithSize:CGSizeMake(MAXFLOAT, 0)
                                                        options:NSStringDrawingTruncatesLastVisibleLine |NSStringDrawingUsesLineFragmentOrigin |
                            NSStringDrawingUsesFontLeading
                                                     attributes:attribute
                                                        context:nil];
            
            CGFloat width = rect1.size.width;
            
            if (firstFlag) {
                firstFlag = NO;
                
                if (pModel.StartPoint.x - width/2.f < 0) {
                    lastRectX = 0;
                    [pModel.DayDesc drawAtPoint:CGPointMake(0, pModel.EndPoint.y) withAttributes:attribute];
                } else {
                    if (pModel.StartPoint.x + width/2.f - MIN(self.parentScrollView.contentOffset.x,self.parentScrollView.contentSize.width - self.bounds.size.width) < self.parentScrollView.bounds.size.width) {
                        lastRectX = pModel.StartPoint.x - width/2.f;
                        [pModel.DayDesc drawAtPoint:CGPointMake(pModel.StartPoint.x - width/2, pModel.EndPoint.y) withAttributes:attribute];
                    } else {
                        lastRectX = pModel.StartPoint.x + 1/2.f - width;
                        [pModel.DayDesc drawAtPoint:CGPointMake(pModel.StartPoint.x  + 1/2.f - width, pModel.EndPoint.y) withAttributes:attribute];
                    }
                }
            } else if ( pModel.StartPoint.x + width/2.f < lastRectX - 50 ) {
                lastRectX = pModel.StartPoint.x - width/2.f;
                [pModel.DayDesc drawAtPoint:CGPointMake(pModel.StartPoint.x - width/2, pModel.EndPoint.y) withAttributes:attribute];
            }
        }
    }];
}

- (void)drawViewWithXPosition:(CGFloat)xPosition drawModels:(NSArray *)drawLineModels linePositionModels:(NSArray *)linePositionModels {
    NSAssert(drawLineModels, @"数据源不能为空");
    _linePositionModels = linePositionModels;
    //转换为实际坐标
    [self convertToPositionModelsWithXPosition:xPosition drawLineModels:drawLineModels];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self setNeedsDisplay];
    });
}

- (NSArray *)convertToPositionModelsWithXPosition:(CGFloat)startX drawLineModels:(NSArray *)drawLineModels  {
    if (!drawLineModels) return nil;
    self.drawLineModels = drawLineModels;
    [self.drawPositionModels removeAllObjects];
    
    CGFloat maxY = self.frame.size.height;
    
    [drawLineModels enumerateObjectsUsingBlock:^(id _Nonnull model, NSUInteger idx, BOOL * _Nonnull stop) {
        if (model[@"showDay"]) {
            CGFloat xPosition = startX + idx * (LINEGAP + LINE);
            CGPoint startPoint = CGPointMake(xPosition, maxY);
            CGPoint endPoint = CGPointMake(xPosition, maxY/2);
            
            WKVolumePositionModel *positionModel = [WKVolumePositionModel modelWithStartPoint:startPoint endPoint:endPoint dayDesc:model[@"showDay"]];
            [self.drawPositionModels addObject:positionModel];
        }
    }];
    
    return self.drawPositionModels ;
}
- (NSMutableArray *)drawPositionModels {
    if (!_drawPositionModels) {
        _drawPositionModels = [NSMutableArray array];
    }
    return _drawPositionModels;
}

@end

