//
//  WKButtomView.h
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WKButtomView : UIView

@property (nonatomic, weak) UIScrollView *parentScrollView;

- (void)drawViewWithXPosition:(CGFloat)xPosition drawModels:(NSArray *)drawLineModels linePositionModels:(NSArray *)linePositionModels;

@end
