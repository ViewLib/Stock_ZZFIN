//
//  SearchHistoryTableViewCell.h
//  Stock
//
//  Created by mac on 2017/9/12.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^addOptionalBlock)(NSInteger row);

@interface SearchHistoryTableViewCell : UITableViewCell

@property (nonatomic, copy) addOptionalBlock    addOptionalBlock;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
