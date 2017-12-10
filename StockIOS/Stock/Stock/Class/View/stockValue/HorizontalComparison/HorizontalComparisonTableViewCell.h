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

@property (strong, nonatomic) UILabel   *typeLabel;

@property (nonatomic) PNBarChart * barChart;

@property (strong, nonatomic) NSArray   *titleAry;

@property (strong, nonatomic) NSString   *stockCode;

@property (strong, nonatomic) NSArray   *barValueAry;

@property (strong, nonatomic) NSMutableArray   *superbarX;

@property (strong, nonatomic) NSMutableArray   *barX;

@property (strong, nonatomic) NSMutableArray   *barY;

@property (strong, nonatomic) NSMutableArray   *barIncome;

@property (strong, nonatomic) NSMutableArray   *barShareOut;

@property (strong, nonatomic) NSMutableArray   *barPricePerfor;

@property (strong, nonatomic) NSMutableArray   *barRatio;

@property (strong, nonatomic) NSString  *yType;

@property (assign, nonatomic) NSInteger currentIndex;

@property (strong, nonatomic) NSMutableDictionary   *currentDic;

@end
