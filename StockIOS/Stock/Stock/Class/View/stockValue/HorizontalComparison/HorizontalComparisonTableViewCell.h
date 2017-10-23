//
//  HorizontalComparisonTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PNChart.h"

//横向比较
@interface HorizontalComparisonTableViewCell : UITableViewCell<UICollectionViewDelegate,UICollectionViewDataSource,PNChartDelegate>

@property (weak, nonatomic) IBOutlet UICollectionView *MenuCollection;

@property (weak, nonatomic) IBOutlet UIView *BarChartView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *BarChartHigh;

@property (nonatomic) PNBarChart * barChart;

@property (strong, nonatomic) NSArray   *titleAry;

@property (strong, nonatomic) NSString   *stockCode;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
