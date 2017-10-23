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
@interface StockMajorEventsTableViewCell : UITableViewCell<PNChartDelegate>

@property (weak, nonatomic) IBOutlet UIButton *czBtn;
@property (weak, nonatomic) IBOutlet UIButton *dzBtn;
@property (weak, nonatomic) IBOutlet UIButton *dazBtn;
@property (weak, nonatomic) IBOutlet UIButton *fhBtn;
@property (weak, nonatomic) IBOutlet UIButton *tzBtn;

@property (weak, nonatomic) IBOutlet UIView *lineView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *LineViewHigh;

@property (nonatomic) PNLineChart * lineChart;

@property (strong, nonatomic) NSString   *stockCode;

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
