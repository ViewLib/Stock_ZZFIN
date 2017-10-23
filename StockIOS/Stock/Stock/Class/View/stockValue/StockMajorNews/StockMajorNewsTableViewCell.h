//
//  StockMajorNewsTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

//重大事件
@interface StockMajorNewsTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIView *valueView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *valueViewHigh;
@property (weak, nonatomic) IBOutlet UIButton *lookMoreBtn;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
