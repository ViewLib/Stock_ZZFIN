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
    self.titleAry = @[@"市盈率",@"融资融券",@"收入增长",@"年度表现",@"分红比例"];
    [_MenuCollection registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"meunCall"];
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumInteritemSpacing = 5.0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    [_MenuCollection setCollectionViewLayout:layout];
    [self initBarChartView];
}

- (void)initBarChartView {
    static NSNumberFormatter *barChartFormatter;
    if (!barChartFormatter) {
        barChartFormatter = [[NSNumberFormatter alloc] init];
        barChartFormatter.numberStyle = NSNumberFormatterCurrencyStyle;
        barChartFormatter.allowsFloats = NO;
        barChartFormatter.maximumFractionDigits = 0;
    }
    
    self.barChart = [[PNBarChart alloc] initWithFrame:CGRectMake(0, 10, SCREEN_WIDTH, 200.0)];
    self.barChart.showLabel = YES;
    
    self.barChart.yLabelFormatter = ^(CGFloat yValue) {
        return [barChartFormatter stringFromNumber:@(yValue)];
    };
    
    self.barChart.yChartLabelWidth = 20.0;
    self.barChart.chartMarginLeft = 30.0;
    self.barChart.chartMarginTop = 25.0;
    self.barChart.chartMarginBottom = 10.0;
    
    self.barChart.showChartBorder = YES;
    [self.barChart setXLabels:@[@"2", @"3", @"4", @"5"]];
    
    [self.barChart setYValues:@[@10.82, @1.88, @6.96, @33.93]];
    [self.barChart setStrokeColors:@[PNGreen, PNGreen, PNRed, PNGreen, PNGreen, PNGreen, PNRed, PNGreen]];
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
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
