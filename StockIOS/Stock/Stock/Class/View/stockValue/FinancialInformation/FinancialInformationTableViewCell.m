//
//  FinancialInformationTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "FinancialInformationTableViewCell.h"

@implementation FinancialInformationTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    [_MenuCollection registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"meunCall"];
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumInteritemSpacing = 5.0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    [_MenuCollection setCollectionViewLayout:layout];
    [self initTypeLabel];
}

- (void)setStockCode:(NSString *)stockCode {
    if (stockCode) {
        _stockCode = stockCode;
    }
    [self getComputerInformation];
}

- (void)getComputerInformation {
    WS(self)
    [[HttpRequestClient sharedClient] getStockFinicial:@{@"stockCode": self.stockCode} request:^(NSString *resultMsg, id dataDict, id error) {//self.stockCode
        if ([dataDict[@"resultCode"] intValue] == 200) {
            selfWeak.titleAry = dataDict[@"groupList"];
            [self.MenuCollection reloadData];
            [selfWeak reloadBarChart];
        }
    }];
}

- (IBAction)changeDiffData:(UIPinchGestureRecognizer *)sender {
    CGFloat scale = 1.0 - [sender scale];
    if (self.currentIndex == 0) {
        if (sender.state == UIGestureRecognizerStateEnded) {
            NSArray *firstYear = self.titleAry[0][@"yearItemList"];
            NSArray *firstFinance = self.titleAry[0][@"financeItemList"];
            if (self.currentIndex == 0 && self.barValueAry.count != firstFinance.count) {
                if (scale < 0) {
                    self.barValueAry = self.titleAry[self.currentIndex][@"financeItemList"];
                    [self reloadBarChartView];
                    [self endEditing:YES];
                }
            } else if (self.currentIndex == 0 && self.barValueAry.count != firstYear.count)  {
                if (scale > 0) {
                    self.barValueAry = self.titleAry[self.currentIndex][@"yearItemList"];
                    [self reloadBarChartView];
                    [self endEditing:YES];
                }
            }
        }
    } else
        return;
}



- (void)reloadBarChart {
    self.barValueAry = self.titleAry[self.currentIndex][@"financeItemList"];
    if (self.currentIndex == 0) {
        self.barValueAry = self.titleAry[0][@"yearItemList"];
    }
    [self reloadBarChartView];
}

- (void)reloadBarChartView {
    self.barX = [NSMutableArray array];
    self.barY = [NSMutableArray array];
    [self.barValueAry enumerateObjectsUsingBlock:^(NSDictionary *dic, NSUInteger idx, BOOL * _Nonnull stop) {
        [self.barX addObject:dic[@"dateStr"]];
        if (idx == 0) {
            switch (self.currentIndex) {
                case 0:
                {
                    self.yType = @"";
                    self.typeLabel.hidden = NO;
                    if ([dic[@"valueStr"] floatValue] > 10000000) {
                        self.typeLabel.text = @"单位(亿)";
                    } else if ([dic[@"valueStr"] floatValue] > 10000) {
                        self.typeLabel.text = @"单位(万)";
                    }
                }
                    break;
                case 1:
                    self.yType = @"%";
                    self.typeLabel.hidden = YES;
                    break;
                case 2:
                    self.yType = @"%";
                    self.typeLabel.hidden = YES;
                    break;
                case 3:
                    self.yType = @"";
                    self.typeLabel.hidden = NO;
                    self.typeLabel.text = @"单位(元)";
                    break;
                default:
                    break;
            }
        }
        
        if ([self.typeLabel.text isEqual:@"单位(亿)"]) {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]/100000000]];
        } else if ([self.typeLabel.text isEqual:@"单位(万)"]) {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]/10000]];
        } else if ([dic[@"valueStr"] floatValue] < 1) {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]*100]];
        } else {
            [self.barY addObject:[NSNumber numberWithFloat:[dic[@"valueStr"] floatValue]]];
        }
    }];
    
    if (self.currentIndex != 0) {
        NSMutableArray *newTime = [NSMutableArray array];
        [self.barX enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            NSArray *objs = [obj componentsSeparatedByString:@"/"];
            if (objs.count == 3) {
                NSString *timeStr = [NSString stringWithFormat:@"%@/%@",objs[1],objs[0]];
                [newTime addObject:timeStr];
            }
        }];
        self.barX = newTime.mutableCopy;
    }
    
    if (self.barX.count > 0 && self.barY.count > 0) {
        if (!self.barChart) {
            [self initBarChartView];
        } else {
            self.barChart.yLabelSuffix = self.yType;
            [self.barChart updateChartYData:self.barY andX:self.barX];
        }
    }
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [self.BarChartView bringSubviewToFront:self.typeLabel];
    });
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
    
    self.barChart.yLabelFormatter = ^(CGFloat yValue) {
        return [barChartFormatter stringFromNumber:@(yValue)];
    };
    
    self.barChart.yChartLabelWidth = 30.0;
    self.barChart.chartMarginLeft = 20.0;
    self.barChart.chartMarginTop = 25.0;//
    self.barChart.chartMarginBottom = 10.0;
    self.barChart.showChartBorder = YES;
    //是否显示Y轴坐标
    self.barChart.showYLabel = NO;
    
    [self.barChart setXLabels:self.barX];
    
    [self.barChart setYValues:self.barY];
    
    //设置Y轴text的单位以及柱状图顶部数据的单位
    self.barChart.yLabelSuffix = self.yType;
    
    [self.barChart setStrokeColor:MAIN_COLOR];
    self.barChart.isGradientShow = NO;
    self.barChart.isShowNumbers = YES;
    self.barChart.showLevelLine = NO;
    
    [self.barChart strokeChart];
    
    self.barChart.delegate = self;
    
    [self.BarChartView addSubview:self.barChart];
    self.BarChartViewHigh.constant = 250;
}

- (void)initTypeLabel {
    if (!_typeLabel) {
        _typeLabel = [[UILabel alloc] init];
        [_typeLabel setTextColor:MAIN_COLOR];
        [_typeLabel setFont:[UIFont systemFontOfSize:12.0f]];
        [_typeLabel setTextAlignment:NSTextAlignmentRight];
        [_typeLabel setHidden:YES];
        [self.BarChartView addSubview:_typeLabel];
        [_typeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.equalTo(@-5);
            make.top.equalTo(_MenuCollection.mas_bottom).equalTo(@5);
            make.height.equalTo(@20);
            make.width.equalTo(@50);
        }];
    }
}

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
    CGSize size = [self valueSize:self.titleAry[indexPath.item][@"financName"] FontSize:12];
    float width = 60;
    if (size.width > 60) {
        width = size.width + 10;
    }
    CGSize frame = CGSizeMake(width, 33);
    return frame;
}

-( UIEdgeInsets )collectionView:( UICollectionView *)collectionView layout:( UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:( NSInteger )section{
    return UIEdgeInsetsMake ( 0 , 10 , 0 , 10 );
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"meunCall" forIndexPath:indexPath];
    
    NSDictionary *dic = self.titleAry[indexPath.item];
    NSString *value = dic[@"financeName"];
    CGSize size = [self valueSize:value FontSize:12];
    float width = 60;
    if (size.width > 60) {
        width = size.width + 10;
    }
    
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, width, 33)];
    [label setFont:[UIFont systemFontOfSize:12]];
    [label setTextAlignment:NSTextAlignmentCenter];
    [label setTag:999000];
    [label setTextColor:[Utils colorFromHexRGB:@"999999"]];
    [label setText:value];
    [cell.contentView addSubview:label];
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, CGRectGetHeight(cell.contentView.frame)-2, width, 2)];
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

- (CGSize)valueSize:(NSString *)value FontSize:(float)stringSize {
    CGSize size = [value boundingRectWithSize:CGSizeMake(1000, 33) options: NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:stringSize]} context:nil].size;
    return size;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
