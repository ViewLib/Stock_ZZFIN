//
//  FindTableViewCell.h
//  Stock
//
//  Created by mac on 2017/9/9.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^viewOneClickBlock)(NSInteger row);

typedef void(^viewTwoClickBlock)(NSInteger row);

@interface FindTableViewCell : UITableViewCell

@property (nonatomic, copy) viewOneClickBlock   viewOneClickBlock;
@property (nonatomic, copy) viewTwoClickBlock   viewTwoClickBlock;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSArray *)dics;

@end
