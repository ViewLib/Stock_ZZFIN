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
@interface FinancialInformationTableViewCell : UITableViewCell<PNChartDelegate,UICollectionViewDelegate,UICollectionViewDataSource>

@property (weak, nonatomic) IBOutlet UICollectionView *MenuCollection;

@property (strong, nonatomic) NSArray   *titleAry;

@property (weak, nonatomic) IBOutlet UIView *BarChartView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *BarChartViewHigh;

@property (nonatomic) PNBarChart * barChart;

@property (strong, nonatomic) NSArray   *barValueAry;

@property (strong, nonatomic) NSMutableArray   *barX;

@property (strong, nonatomic) NSMutableArray   *barY;

@property (strong, nonatomic) NSString  *yType;

@property (assign, nonatomic) NSInteger currentIndex;

@property (strong, nonatomic) NSString   *stockCode;

@property (strong, nonatomic) UILabel   *typeLabel;

@end
