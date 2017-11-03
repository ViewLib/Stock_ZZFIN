//
//  WKVolumePositionModel.h
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreGraphics/CoreGraphics.h>

@interface WKVolumePositionModel : NSObject

/**
 *  开始点
 */
@property (nonatomic, assign) CGPoint StartPoint;

/**
 *  结束点
 */
@property (nonatomic, assign) CGPoint EndPoint;

/**
 *  日期
 */
@property (nonatomic, copy) NSString *DayDesc;

/**
 *  工厂方法
 */
+ (instancetype) modelWithStartPoint:(CGPoint)startPoint endPoint:(CGPoint)endPoint dayDesc:(NSString *)dayDesc;

@end
