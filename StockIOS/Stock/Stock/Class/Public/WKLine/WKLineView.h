//
//  WKLineView.h
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import <UIKit/UIKit.h>

//线的宽度
#define LINEWIDE  1.2

@interface WKLineView : UIView

- (instancetype)initWithContext:(CGContextRef)context;

- (void)drawWithColor:(UIColor *)lineColor maPositions:(NSArray *)maPositions;

@end
