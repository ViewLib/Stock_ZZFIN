//
//  StockMajorEventsTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockMajorEventsTableViewCell.h"
#import "LineView.h"


@implementation StockMajorEventsTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [_MenuCollection registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"meunCall"];
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumInteritemSpacing = 5.0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    [_MenuCollection setCollectionViewLayout:layout];
    WS(self)
    [RACObserve([Config shareInstance], KlineDate) subscribeNext:^(NSArray *x) {
        if (x.count > 0) {
            [selfWeak initLineView];
        }
    }];
    // Initialization code
}

- (void)setStockCode:(NSString *)stockCode {
    if (stockCode) {
        _stockCode = stockCode;
    }
    [self getStockEvent];
}

- (void)getStockEvent {
    WS(self)
    [[HttpRequestClient sharedClient] getStockEvent:@{@"stockCode": self.stockCode, @"type":@3} request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] intValue] == 200) {
            selfWeak.events = [NSArray arrayWithArray:dataDict[@"stockEventsDataLists"]];
            NSMutableArray *ary = [NSMutableArray array];
            for (NSDictionary *dic in selfWeak.events) {
                [ary addObject:dic[@"eventName"]];
            }
            selfWeak.titleAry = ary;
            selfWeak.titleNews = [ary firstObject];
            [selfWeak monitorReload];
            [selfWeak.MenuCollection reloadData];
        }
    }];
}

- (void)initLineView {
    if (!self.line) {
        self.line = [[LineView alloc] initWithFrame:CGRectMake(0, 0, K_FRAME_BASE_WIDTH, 237) andViewData:[Config shareInstance].KlineDate];
        [self.line setBackgroundColor:[UIColor clearColor]];
        [self.lineView addSubview:self.line];
    }
}

- (void)monitorReload {
    self.timer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(reloadLineView) userInfo:nil repeats:YES];
}

- (void)reloadLineView {
    if (self.line) {
        [self.timer invalidate];
        self.timer = nil;
        NSArray *value = [NSArray array];
        for (NSDictionary *dic in self.events) {
            if ([self.titleNews isEqual:dic[@"eventName"]] && self.line) {
                value = dic[@"stockEventsDataModels"];
            }
        }
        if (value.count > 0) {
            [self.line reloadNewView:value];
        }
    }
    
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

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
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
        self.titleNews = label.text;
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
            self.titleNews = lab.text;
        } else {
            [lab setTextColor:[Utils colorFromHexRGB:@"999999"]];
            line.hidden = YES;
        }
    }
    [self reloadLineView];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
