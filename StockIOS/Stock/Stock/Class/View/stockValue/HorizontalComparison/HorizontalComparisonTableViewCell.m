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
    self.titleAry = [NSArray array];//@[@"市盈率",@"融资融券",@"收入增长",@"年度表现",@"分红比例"]
    [_MenuCollection registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"meunCall"];
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumInteritemSpacing = 5.0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    [_MenuCollection setCollectionViewLayout:layout];
}

- (void)setStockCode:(NSString *)stockCode {
    if (stockCode) {
        _stockCode = stockCode;
    }
    [self getStockCompare];
}

- (void)getStockCompare {
    WS(self)
    [[HttpRequestClient sharedClient] getStockFinicial:@{@"stockCode": self.stockCode,@"serviceCode":@"2010",@"type":@"2",@"versionCode":@"1"} request:^(NSString *resultMsg, id dataDict, id error) {//self.stockCode
        if ([dataDict[@"resultCode"] intValue] == 200) {
            selfWeak.titleAry = dataDict[@"groupList"];
            [selfWeak.MenuCollection reloadData];
            [selfWeak reloadBarChart];
        }
    }];
}

- (void)reloadBarChart {
    self.barX = [NSMutableArray array];
    self.barY = [NSMutableArray array];
    self.barValueAry = self.titleAry[self.currentIndex][@"financeItemList"];
    for (NSDictionary *dic in self.barValueAry) {
        [self.barX addObject:dic[@"dateStr"]];
        if ([dic[@"valueStr"] floatValue] > 100000000) {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]/100000000]];
            self.yType = @"亿";
        } else if ([dic[@"valueStr"] floatValue] > 10000) {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]/10000]];
            self.yType = @"万";
        } else if ([dic[@"valueStr"] floatValue] < 1) {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]*100]];
            self.yType = @"%";
        } else {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]]];
            self.yType = @"";
        }
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
    self.barChart.showLabel = YES;
    
    self.barChart.yLabelFormatter = ^(CGFloat yValue) {
        return [barChartFormatter stringFromNumber:@(yValue)];
    };
    
    self.barChart.yChartLabelWidth = 30.0;
    self.barChart.chartMarginLeft = 40.0;
    self.barChart.chartMarginTop = 25.0;//
    self.barChart.chartMarginBottom = 10.0;
    self.barChart.yLabelSuffix = self.yType;
    self.barChart.showChartBorder = YES;
    [self.barChart setXLabels:self.barX];
    
    [self.barChart setYValues:self.barY];
    [self.barChart setStrokeColor:MAIN_COLOR];
    self.barChart.isGradientShow = NO;
    self.barChart.isShowNumbers = NO;
    
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
    
    NSDictionary *dic = self.titleAry[indexPath.item];
    [label setText:dic[@"financeName"]];
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
    [self reloadBarChart];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
