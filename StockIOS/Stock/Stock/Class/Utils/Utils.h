//
//  Utils.h
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef void(^utilRequest)(BOOL value);

@interface Utils : NSObject

#pragma mark - 获取当前日期
+ (NSString *)contactTime;

#pragma mark - 处理不同的股票代码格式
+ (NSString *)unifiedStockCode:(NSString *)code;

#pragma mark - 更新自选股
+ (void)updateStock;

#pragma mark - 是否为自选股
+ (BOOL)isSelectionStock:(NSString *)stockCode;

#pragma mark - 添加自选股
+ (void)AddStock:(NSString *)stockCode utilRequest:(utilRequest)utilRequest;

#pragma mark - 删除自选股
+ (void)removeStock:(NSString *)stockCode utilRequest:(utilRequest)utilRequest;

#pragma mark - 获取当前所有的自选股
+ (NSArray *)getStock;

#pragma mark - 分时图数据格式转换
+ (NSDictionary *)lineDicWithDic:(NSDictionary *)dic avgPrice:(NSString *)avgPrice;

#pragma mark - K线图数据格式转换
+ (NSDictionary *)KlineDicWithDic:(NSDictionary *)dic;

#pragma mark - 颜色转换
+ (UIColor *)colorFromHexRGB:(NSString *)inColorString;

#pragma mark - 颜色变一像素宽固定高度的图片
+ (UIImage*)GetImageWithColor:(UIColor*)color andHeight:(CGFloat)height;

#pragma mark - 颜色变特定尺寸的图片
+ (UIImage*)GetImageWithColor:(UIColor*)color andSize:(CGSize)size;

#pragma mark - 任意颜色根据比例拼接成特定尺寸的图片
/**
 任意颜色根据比例拼接成特定尺寸的图片
 
 @param color 目标颜色
 @param percent 目标颜色所占比例
 @param pn 正还是负
 @param size 图片大小
 @return 目标颜色和白色合成的图片
 */
+ (UIImage*)GetImageWithColor:(UIColor*)color andPercent:(float)percent andPN:(NSString *)pn andSize:(CGSize)size;

#pragma mark - 判断是否都为数字
+(BOOL)validateNum:(NSString*)string;

#pragma mark - 获取json文件中的数据
+ (NSArray *)getArrayFromJsonFile:(NSString *)jsonName;

@end
