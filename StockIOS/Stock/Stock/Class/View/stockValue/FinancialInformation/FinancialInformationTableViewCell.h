//
//  FinancialInformationTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PNChart.h"

//财务信息cell
@interface FinancialInformationTableViewCell : UITableViewCell<PNChartDelegate>

@property (weak, nonatomic) IBOutlet UIButton *srBtn;
@property (weak, nonatomic) IBOutlet UIButton *mlBtn;
@property (weak, nonatomic) IBOutlet UIButton *jlBtn;
@property (weak, nonatomic) IBOutlet UIButton *xjBtn;
@property (weak, nonatomic) IBOutlet UIButton *fhBtn;
@property (weak, nonatomic) IBOutlet UIView *BarChartView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *BarChartViewHigh;

@property (nonatomic) PNBarChart * barChart;

@property (strong, nonatomic) NSString   *stockCode;
/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic;

@end
