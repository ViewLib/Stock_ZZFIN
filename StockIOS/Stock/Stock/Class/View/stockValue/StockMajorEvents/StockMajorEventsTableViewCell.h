//
//  StockMajorEventsTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PNLineChart.h"
#import "PNLineChartData.h"
#import "PNLineChartDataItem.h"

//重大消息
@interface StockMajorEventsTableViewCell : UITableViewCell<PNChartDelegate,UICollectionViewDelegate,UICollectionViewDataSource>

@property (weak, nonatomic) IBOutlet UICollectionView *MenuCollection;

@property (strong, nonatomic) NSArray   *titleAry;

@property (weak, nonatomic) IBOutlet UIView *lineView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *LineViewHigh;

@property (nonatomic) PNLineChart * lineChart;

@property (strong, nonatomic) NSString   *stockCode;

@property (strong, nonatomic) NSArray    *events;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
