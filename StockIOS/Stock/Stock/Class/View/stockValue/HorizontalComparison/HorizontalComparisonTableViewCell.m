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
    self.barChart.chartMarginRight = 10.0;
    self.barChart.chartMarginTop = 0.0;
    self.barChart.chartMarginBottom = 10.0;
    
    
    self.barChart.labelMarginTop = 0.0;
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
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    [btn setFrame:CGRectMake(0, 0, 60, 33)];
    [btn setTag:indexPath.item];
    [btn.titleLabel setFont:[UIFont systemFontOfSize:12]];
    [btn setTitleColor:[Utils colorFromHexRGB:@"999999"] forState:UIControlStateNormal];
    [btn setTitleColor:[UIColor blackColor] forState:UIControlStateSelected];
    [btn setTitle:self.titleAry[indexPath.item] forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(clickBtn:) forControlEvents:UIControlEventTouchUpInside];
    [cell.contentView addSubview:btn];
    return cell;
}

- (void)clickBtn:(UIButton *) btn {
    NSLog(@"%@",btn);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
