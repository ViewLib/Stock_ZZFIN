//
//  WKNewView.h
//  Stock
//
//  Created by mac on 2017/11/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WKNewView : UIView

- (instancetype)initWithContext:(CGContextRef)context;

- (void)drawWithColor:(UIColor *)lineColor maPositions:(NSArray *)maPositions;

@end
