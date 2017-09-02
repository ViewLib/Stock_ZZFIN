//
//  Utils.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "Utils.h"

@implementation Utils

#pragma mark - 颜色转换

+ (UIColor *)colorFromHexRGB:(NSString *)inColorString
{
    UIColor *result = nil;
    unsigned int colorCode = 0;
    unsigned char redByte, greenByte, blueByte;
    
    if (nil != inColorString)
    {
        NSScanner *scanner = [NSScanner scannerWithString:inColorString];
        (void) [scanner scanHexInt:&colorCode]; // ignore error
    }
    redByte = (unsigned char) (colorCode >> 16);
    greenByte = (unsigned char) (colorCode >> 8);
    blueByte = (unsigned char) (colorCode); // masks off high bits
    result = [UIColor
              colorWithRed: (float)redByte / 0xff
              green: (float)greenByte/ 0xff
              blue: (float)blueByte / 0xff
              alpha:1.0];
    
    return result;
}

#pragma mark - 颜色变图片

+ (UIImage*) GetImageWithColor:(UIColor*)color andHeight:(CGFloat)height
{
    CGRect r= CGRectMake(0.0f, 0.0f, 1.0f, height);
    UIGraphicsBeginImageContext(r.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, r);
    
    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return img;
}

#pragma mark - 颜色变特定尺寸的图片
+ (UIImage*)GetImageWithColor:(UIColor*)color andSize:(CGSize)size
{
    CGRect r= CGRectMake(0.0f, 0.0f, size.width, size.height);
    UIGraphicsBeginImageContext(r.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, r);
    
    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return img;
}

#pragma mark - 任意颜色根据比例拼接成特定尺寸的图片
/**
 任意颜色根据比例拼接成特定尺寸的图片

 @param color 目标颜色
 @param percent 目标颜色所占比例
 @param pn 正还是负
 @param size 图片大小
 @return 目标颜色和白色合成的图片
 */
+ (UIImage*)GetImageWithColor:(UIColor*)color andPercent:(float)percent andPN:(NSString *)pn andSize:(CGSize)size
{
    float colorWidth = percent * size.width;
    float otherWidth = size.width - colorWidth;
    
    UIImage *img1 = [Utils GetImageWithColor:color andSize:CGSizeMake(colorWidth, size.height)];
    UIImage *img2 = [Utils GetImageWithColor:[UIColor whiteColor] andSize:CGSizeMake(otherWidth, size.height)];
    
    UIGraphicsBeginImageContext(size);
    
    if ([pn isEqual:@"+"]) {
        [img1 drawInRect:CGRectMake(0, 0, colorWidth, size.height)];
        [img2 drawInRect:CGRectMake(colorWidth, 0, otherWidth, size.height)];
    } else {
        [img1 drawInRect:CGRectMake(0, 0, otherWidth, size.height)];
        [img2 drawInRect:CGRectMake(otherWidth, 0, colorWidth, size.height)];
    }
    
    UIImage *img = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return img;
}

@end
