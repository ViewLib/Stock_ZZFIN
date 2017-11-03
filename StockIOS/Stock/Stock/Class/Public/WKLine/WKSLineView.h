//
//  WKSLineView.h
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef BOOL (^getNewsData)(NSString *string);

@interface WKSLineView : UIView

@property (nonatomic, copy) getNewsData getNewsData;

- (NSArray *)drawViewWithXPosition:(CGFloat)xPosition drawModels:(NSMutableArray*)drawLineModels  maxValue:(CGFloat)maxValue minValue:(CGFloat)minValue;


@end
