//
//  StockTopTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "StockEntity.h"

@interface StockTopTableViewCell : UITableViewCell
//当前价格
@property (weak, nonatomic) IBOutlet UILabel *stockLast;
//涨跌额度
@property (weak, nonatomic) IBOutlet UILabel *Change;
//涨跌百分比
@property (weak, nonatomic) IBOutlet UILabel *Chg;
//是否自选股
@property (weak, nonatomic) IBOutlet UIButton *isSelect;
//当日最高
@property (weak, nonatomic) IBOutlet UILabel *highest;
//当日最低
@property (weak, nonatomic) IBOutlet UILabel *lowest;
//本年跌涨
@property (weak, nonatomic) IBOutlet UILabel *ChgOfYear;
//换手率
@property (weak, nonatomic) IBOutlet UILabel *turnoverRate;
//成交额
@property (weak, nonatomic) IBOutlet UILabel *turnover;
//市值
@property (weak, nonatomic) IBOutlet UILabel *marketValue;

@property (strong, nonatomic) NSString *code;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(StockEntity *)dic;

@end
