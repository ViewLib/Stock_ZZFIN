//
//  WKNewView.m
//  Stock
//
//  Created by mac on 2017/11/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "WKNewView.h"

@interface WKNewView()

/**
 绘制上下文
 */
@property (nonatomic, assign) CGContextRef context;

@property (nonatomic, strong) NSArray *MAPositions;

@property (nonatomic, strong) UIColor *lineColor;
@end

@implementation WKNewView

/**
 *  根据context初始化画线
 */
- (instancetype)initWithContext:(CGContextRef)context
{
    self = [super init];
    if(self)
    {
        CGContextSaveGState(context);
        self.userInteractionEnabled = YES;
        self.context = context;
        
    }
    return self;
}

- (void)controlViewClick_1:(UITapGestureRecognizer *)tap {
    NSLog(@"%@", NSStringFromSelector(_cmd));
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

    [self.MAPositions enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        CGPoint point = [obj CGPointValue];
        
//        CGContextSetLineWidth(self.context, 2.0);//线的宽度
//        CGContextSetFillColorWithColor(self.context, self.lineColor.CGColor);//填充颜色
//        CGContextSetStrokeColorWithColor(self.context, self.lineColor.CGColor);//线框颜色
//        CGContextAddRect(self.context,CGRectMake(point.x, point.y-23, 60, 30));//画方框
//        CGContextDrawPath(self.context, kCGPathFillStroke);//绘画路径
        
        UIImage *image = [UIImage imageNamed:@"papaw"];
        [image drawInRect:CGRectMake(point.x-12.5, point.y-23, 25, 20)];
        CGContextDrawImage(self.context, CGRectMake(point.x-12.5, point.y-23, 25, 20), image.CGImage);
    }];

    CGContextStrokePath(self.context);
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(controlViewClick_1:)];
    [self addGestureRecognizer:tap];
}




/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
