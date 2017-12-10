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
    _currentDic = [Config shareInstance].stockDic[_stockCode];
    if (_currentDic[@"stockEventsDataLists_3"]) {
        [self reloadView: _currentDic[@"stockEventsDataLists_3"]];
    } else {
        [self getStockEvent];
    }
}

- (void)getStockEvent {
    WS(self)
    [[HttpRequestClient sharedClient] getStockEvent:@{@"stockCode": self.stockCode, @"type":@3} request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] intValue] == 200) {
            
            NSMutableDictionary *currentStockDic = [Config shareInstance].stockDic[self.stockCode];
            [currentStockDic setValue:dataDict[@"stockEventsDataLists"] forKey:@"stockEventsDataLists_3"];
            [[Config shareInstance].stockDic setValue:currentStockDic forKey:self.stockCode];
            
            [selfWeak reloadView: dataDict[@"stockEventsDataLists"]];
        }
    }];
}

- (void)reloadView:(NSArray *)array {
    self.events = array;
    NSMutableArray *ary = [NSMutableArray array];
    for (NSDictionary *dic in self.events) {
        [ary addObject:dic[@"eventName"]];
    }
    self.titleAry = ary;
    self.titleNews = [ary firstObject];
    [self monitorReload];
    [self.MenuCollection reloadData];
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
        
        NSSet *set = [NSSet setWithArray:value];
        value = [set allObjects];
        
        if (value.count > 0) {
            [self.line reloadNewView:value];
            NSString *str = [NSString stringWithFormat:@"总消息数%lu",(unsigned long)value.count];
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:str message:[NSString stringWithFormat:@"%@",value] preferredStyle:UIAlertControllerStyleAlert];
            UIAlertAction *act1 = [UIAlertAction actionWithTitle:@"确定" style:0 handler:nil];
            [alert addAction:act1];
//            [[UIApplication sharedApplication].delegate.window.rootViewController presentViewController:alert animated:YES completion:nil];
        } else {
            [self.line reloadNewView:nil];
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
    CGSize size = [self valueSize:self.titleAry[indexPath.item] FontSize:12];
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
    NSString *value = self.titleAry[indexPath.item];
    CGSize size = [self valueSize:value FontSize:12];
    float width = 60;
    if (size.width > 60) {
        width = size.width + 10;
    }
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, width, 33)];
    [label setFont:[UIFont systemFontOfSize:12]];
    [label setTag:999000];
    [label setTextColor:[Utils colorFromHexRGB:@"999999"]];
    [label setTextAlignment:NSTextAlignmentCenter];
    [label setText:value];
    [cell.contentView addSubview:label];
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, CGRectGetHeight(cell.contentView.frame)-2, CGRectGetWidth(label.frame), 2)];
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


- (CGSize)valueSize:(NSString *)value FontSize:(float)stringSize {
    CGSize size = [value boundingRectWithSize:CGSizeMake(1000, 33) options: NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:stringSize]} context:nil].size;
    return size;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
