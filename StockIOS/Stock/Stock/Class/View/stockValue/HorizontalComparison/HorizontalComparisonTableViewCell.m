//
//  HorizontalComparisonTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "HorizontalComparisonTableViewCell.h"

@implementation HorizontalComparisonTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.userInteractionEnabled = YES;
    self.titleAry = @[@"市盈率",@"收入增长",@"年度表现",@"净资产"];//
    [_MenuCollection registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"meunCall"];
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumInteritemSpacing = 5.0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    [_MenuCollection setCollectionViewLayout:layout];
    self.barRatio = [NSMutableArray array];
    self.barIncome = [NSMutableArray array];
    self.barShareOut = [NSMutableArray array];
    self.barPricePerfor = [NSMutableArray array];
}

- (void)setStockCode:(NSString *)stockCode {
    if (stockCode) {
        _stockCode = stockCode;
    }
    [self getStockCompare];
}

- (void)getStockCompare {
    WS(self)
    [[HttpRequestClient sharedClient] getStockCompare:@{@"stockCode": self.stockCode,@"serviceCode":@"2010",@"type":@"2",@"versionCode":@"1"} request:^(NSString *resultMsg, id dataDict, id error) {//self.stockCode
        if ([dataDict[@"resultCode"] intValue] == 200) {
            selfWeak.barValueAry = dataDict[@"compareList"];
            [selfWeak reloadBarChart:9999];
        }
    }];
}

- (void)reloadBarChart:(int)index {
    self.barY = [NSMutableArray array];
    self.barX = [NSMutableArray array];
    
    self.yType = @"%";
    if (index == 9999) {
        self.superbarX = [NSMutableArray array];
        for (NSDictionary *dic in self.barValueAry) {
            [self.superbarX addObject:dic[@"stockName"]];
            [self.barIncome addObject:dic[@"income"]];
            [self.barShareOut addObject:dic[@"assets"]];
            if ([dic[@"ratio"] floatValue] != 0) {
                [self.barRatio addObject:dic[@"ratio"]];
            }
            [self.barPricePerfor addObject:dic[@"pricePerfor"]];
        }
        self.barY = self.barRatio;
    } else if (index == 0) {
//        self.yType = @"%";
        self.barY = self.barRatio;
    } else if (index == 1) {
//        self.yType = @"%";
        self.barY = self.barIncome;
    } else if (index == 2) {
//        self.yType = @"%";
        self.barY = self.barPricePerfor;
    } else {
//        self.yType = @"%";
        self.barY = self.barShareOut;
    }
    
    if (self.barY.count < self.superbarX.count) {
        self.barX = [self.superbarX subarrayWithRange:NSMakeRange(0, self.barY.count)].mutableCopy;
    } else {
        self.barX = self.superbarX.mutableCopy;
    }
    
    if (self.barX.count > 0 && self.barY.count > 0) {
        if (!self.barChart) {
            [self initBarChartView];
        } else {
            self.barChart.yLabelSuffix = self.yType;
            [self.barChart updateChartYData:self.barY andX:self.barX];
        }
    }
}

- (void)initBarChartView {
    static NSNumberFormatter *barChartFormatter;
    if (!barChartFormatter) {
        barChartFormatter = [[NSNumberFormatter alloc] init];
        barChartFormatter.numberStyle = NSNumberFormatterCurrencyStyle;
        barChartFormatter.allowsFloats = NO;
        barChartFormatter.maximumFractionDigits = 0;
    }
    
    self.barChart = [[PNBarChart alloc] initWithFrame:CGRectMake(0, 10, SCREEN_WIDTH, 230.0)];
    self.barChart.showYLabel = NO;
    
    self.barChart.yLabelFormatter = ^(CGFloat yValue) {
        return [barChartFormatter stringFromNumber:@(yValue)];
    };
    
    self.barChart.yChartLabelWidth = 30.0;
    self.barChart.chartMarginLeft = 20.0;
    self.barChart.chartMarginTop = 25.0;//
    self.barChart.chartMarginBottom = 10.0;
    self.barChart.yLabelSuffix = self.yType;
    self.barChart.showChartBorder = YES;
    [self.barChart setXLabels:self.barX];
    
    [self.barChart setYValues:self.barY];
    [self.barChart setStrokeColor:MAIN_COLOR];
    self.barChart.isGradientShow = NO;
    self.barChart.isShowNumbers = YES;
    
    [self.barChart strokeChart];
    
    self.barChart.delegate = self;
    
    [self.BarChartView addSubview:self.barChart];
    self.BarChartHigh.constant = 250;
}

#pragma mark - PNChartDelegate
- (void)userClickedOnBarAtIndex:(NSInteger)barIndex {
    NSLog(@"Click Key on Bar index is %d ", (int) barIndex);
}

#pragma mark CollectionViewDelegateAndDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.titleAry.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGSize frame = CGSizeMake(60, 33);
    return frame;
}

-( UIEdgeInsets )collectionView:( UICollectionView *)collectionView layout:( UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:( NSInteger )section{
    return UIEdgeInsetsMake ( 0 , 10 , 0 , 10 );
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"meunCall" forIndexPath:indexPath];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 60, 33)];
    [label setFont:[UIFont systemFontOfSize:12]];
    [label setTag:999000];
    [label setTextColor:[Utils colorFromHexRGB:@"999999"]];
    [label setTextAlignment:NSTextAlignmentCenter];
    
    [label setText:self.titleAry[indexPath.item]];
    [cell.contentView addSubview:label];
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, CGRectGetHeight(cell.contentView.frame)-2, 60, 2)];
    [line setBackgroundColor:MAIN_COLOR];
    [line setCenterX:label.centerX];
    [line setTag:999001];
    [line setHidden:YES];
    [cell.contentView addSubview:line];
    
    if (indexPath.item == 0) {
        [label setTextColor:[UIColor blackColor]];
        line.hidden = NO;
    }
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    for (int i = 0; i < self.titleAry.count; i++) {
        UICollectionViewCell *cell = [collectionView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:i inSection:0]];
        UILabel *lab = [cell viewWithTag:999000];
        UIView *line = [cell viewWithTag:999001];
        if (i == indexPath.item) {
            [lab setTextColor:[UIColor blackColor]];
            line.hidden = NO;
        } else {
            [lab setTextColor:[Utils colorFromHexRGB:@"999999"]];
            line.hidden = YES;
        }
    }
    self.currentIndex = indexPath.item;
    [self reloadBarChart:self.currentIndex];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
