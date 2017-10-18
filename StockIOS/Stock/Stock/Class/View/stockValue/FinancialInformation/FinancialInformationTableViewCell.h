//
//  FinancialInformationTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FinancialInformationTableViewCell : UITableViewCell

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
