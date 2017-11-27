//
//  hotStockView.h
//  Stock
//
//  Created by mac on 2017/9/23.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^clickBlock)(id view);

@interface hotStockView : UIView
@property (strong, nonatomic)  UILabel *value;
@property (strong, nonatomic)  UILabel *stockName;
@property (strong, nonatomic)  UILabel *stockCode;
@property (strong, nonatomic)  NSDictionary *valueDic;

@property (copy, nonatomic) clickBlock clickBlock;

@end
