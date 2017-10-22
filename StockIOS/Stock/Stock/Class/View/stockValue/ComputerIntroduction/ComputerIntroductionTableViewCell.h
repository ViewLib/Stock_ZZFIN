//
//  ComputerIntroductionTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

//公司简介
@interface ComputerIntroductionTableViewCell : UITableViewCell<UICollectionViewDelegate,UICollectionViewDataSource>

@property (weak, nonatomic) IBOutlet UICollectionView *MenuCollection;
@property (weak, nonatomic) IBOutlet UIView *ValueView;

@property (strong, nonatomic) NSArray   *titleAry;
/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
