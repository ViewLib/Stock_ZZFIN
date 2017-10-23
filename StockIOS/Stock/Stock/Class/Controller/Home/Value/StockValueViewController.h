//
//  StockValueViewController.h
//  Stock
//
//  Created by mac on 2017/9/23.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "StockEntity.h"

@interface StockValueViewController : UIViewController

@property (nonatomic, strong)   StockEntity  *stock;
@property (nonatomic, strong)   NSString     *stockNameStr;
@property (nonatomic, strong)   NSString     *stockCodeStr;

@end
