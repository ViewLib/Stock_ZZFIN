//
//  WKVolumePositionModel.m
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import "WKVolumePositionModel.h"

@implementation WKVolumePositionModel

+ (instancetype) modelWithStartPoint:(CGPoint)startPoint endPoint:(CGPoint)endPoint dayDesc:(NSString *)dayDesc {
    WKVolumePositionModel *volumePositionModel = [WKVolumePositionModel new];
    volumePositionModel.StartPoint = startPoint;
    volumePositionModel.EndPoint = endPoint;
    volumePositionModel.DayDesc = dayDesc;
    return volumePositionModel;
}

@end
