//
//  RankTableViewCell.h
//  Stock
//
//  Created by mac on 2017/9/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RankTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *number;
@property (weak, nonatomic) IBOutlet UILabel *stockName;
@property (weak, nonatomic) IBOutlet UILabel *stockCode;
@property (weak, nonatomic) IBOutlet UILabel *addValue;
@property (weak, nonatomic) IBOutlet UIButton *addBtn;
@property (weak, nonatomic) IBOutlet UILabel *value1;
@property (weak, nonatomic) IBOutlet UILabel *value2;
@property (nonatomic, assign)   NSInteger   row;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
