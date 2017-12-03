//
//  YYTimeLineModel.m
//  Stock
//
//  Created by WillkYang on 16/10/10.
//  Copyright © 2016年 yeeyuntech. All rights reserved.
//

#import "YYTimeLineModel.h"
#import <CoreGraphics/CoreGraphics.h>
@implementation YYTimeLineModel
{
    NSDictionary * _dict;
    NSString *Price;
    NSString *Volume;
    NSString *Pjprice;
}

- (NSString *)TimeDesc {
    NSLog(@"%@",_dict[@"minute"]);
    if( [_dict[@"minute"] isEqualToString:@"11:30"] || [_dict[@"minute"] isEqualToString:@"13:00"]) {
        return @"11:30/13:00";
    } else if( [_dict[@"minute"] isEqualToString:@"15:00"]) {
        return @"15:00";
    } else {
        return _dict[@"minute"];
    }
}

- (NSString *)DayDatail {
    return _dict[@"minute"];
}

//前一天的收盘价
- (CGFloat )AvgPrice {
    return [_dict[@"avgPrice"] floatValue];
}

- (NSNumber *)Price {
    return _dict[@"price"];
}

- (CGFloat)Volume {
    return [_dict[@"volume"] floatValue];
}

- (CGFloat)Pjprice {
    return [_dict[@"Pjprice"] floatValue];
}

- (BOOL)isShowTimeDesc {
    //9:30-11:30,13:00-15:00
    //11:30和13:00挨在一起，显示一个就够了
    //最后一个服务器返回的minute不是960,故只能特殊处理
//    return [_dict[@"minute"] integerValue] == 570 ||  [_dict[@"minute"] integerValue] == 780 ||  [_dict[@"index"] integerValue] == 240;
    return [_dict[@"minute"] isEqualToString:@"09:30"] || [_dict[@"minute"] isEqualToString:@"10:30"] ||  [_dict[@"minute"] isEqualToString:@"11:30"] ||  [_dict[@"minute"] isEqualToString:@"13:00"] || [_dict[@"minute"] isEqualToString:@"14:00"] ||  [_dict[@"minute"] isEqualToString:@"15:00"];
}

- (instancetype)initWithDict:(NSDictionary *)dict {
    if (self = [super init]) {
        _dict = dict;
        Price = _dict[@"price"];
        Volume = _dict[@"volume"];
        
    }
    return self;
}


@end
