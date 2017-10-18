//
//  StockMajorEventsTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StockMajorEventsTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIButton *stockTime;
@property (weak, nonatomic) IBOutlet UIButton *stockDay;
@property (weak, nonatomic) IBOutlet UIButton *stockWeek;
@property (weak, nonatomic) IBOutlet UIButton *stockMonth;
@property (weak, nonatomic) IBOutlet UIButton *stockYear;
@property (weak, nonatomic) IBOutlet UIButton *stockMax;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
