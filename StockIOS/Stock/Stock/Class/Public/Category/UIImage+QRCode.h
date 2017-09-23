//
//  UIImage+QRCode.h
//  Overseas
//
//  Created by xiesy on 17/4/18.
//  Copyright © 2017年 xiesy. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (QRCode)
+ (UIImage *)barcodeImageWithContent:(NSString *)content codeImageSize:(CGSize)size red:(CGFloat)red green:(CGFloat)green blue:(NSInteger)blue;
@end
