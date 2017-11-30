//
//  ComputerIntroductionTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "ComputerIntroductionTableViewCell.h"
#import "ValueCollection.h"

@implementation ComputerIntroductionTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.titleAry = @[@"公司简介",@"十大股东"];
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
    [self getComputerInformation];
}

- (void)getComputerInformation {
    WS(self)
    [[HttpRequestClient sharedClient] getComputerInfo:@{@"stockCode": self.stockCode} request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] intValue] == 200) {
            [selfWeak setInformation:dataDict[@"companyModel"]];
            selfWeak.stockHolderAry = dataDict[@"stockHolderList"];
        }
    }];
}

//处理公司简介数据
- (void)setInformation:(NSDictionary *)company {
    NSArray *keys = company.allKeys;
    NSMutableArray *new = [NSMutableArray array];
    for (NSString *string in keys) {
        if ([string isEqual:@"establishDate"]) {
            [new addObject:@{@"title": @"成立时间",@"value":company[string]}];
        } else if ([string isEqual:@"baseArea"]) {
            [new addObject:@{@"title": @"总部位置",@"value":company[string]}];
        } else if ([string isEqual:@"companyBusiness"]) {
            [new addObject:@{@"title": @"主营业务",@"value":company[string]}];
        } else if ([string isEqual:@"companyName"]) {
            [new addObject:@{@"title": @"董事长",@"value":company[string]}];
        } else {
            [new addObject:@{@"title": @"公司名称",@"value":self.stockName}];
        }
    }
    self.informationAry = [NSArray arrayWithArray:new];
    [self gsjjView];
}

//初始化公司简介View
- (ValueCollection *)gsjjView {
    if (!_gsjjView) {
        _gsjjView = [[ValueCollection alloc] initWithType:@"GSJJ" Value:self.informationAry];
        CGRect new = self.gsjjView.frame;
        new.origin.x = 0;
        new.origin.y = 0;
        new.size.width = K_FRAME_BASE_WIDTH;
        new.size.height = computerVIEWHIGH;
        _gsjjView.frame = new;
        [self.ValueView addSubview:_gsjjView];
    }
    return _gsjjView;
}

//初始化股东数据
- (void)setStockHolderAry:(NSArray *)stockHolderAry {
    if (stockHolderAry) {
        NSMutableArray *ary = [NSMutableArray arrayWithObject:@""];
        [ary addObjectsFromArray:stockHolderAry];
        _stockHolderAry = [NSArray arrayWithArray:ary];
    }
}

//初始化股东数据页面
- (ValueCollection *)gdView {
    if (!_gdView) {
        _gdView = [[ValueCollection alloc] initWithType:@"GD" Value:self.stockHolderAry];
        CGRect new = self.gdView.frame;
        new.origin.x = 0;
        new.origin.y = 0;
        new.size.width = K_FRAME_BASE_WIDTH;
        new.size.height = computerVIEWHIGH;
        _gdView.frame = new;
        [self.ValueView addSubview:_gdView];
        _gdView.hidden = YES;
    }
    return _gdView;
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
            [self reloadValueView:lab.text];
            line.hidden = NO;
        } else {
            [lab setTextColor:[Utils colorFromHexRGB:@"999999"]];
            line.hidden = YES;
        }
    }
}

//两个视图切换
- (void)reloadValueView:(NSString *)string {
    if ([string isEqual:@"十大股东"]) {
        self.gdView.hidden = NO;
        self.gsjjView.hidden = YES;
    } else {
        self.gdView.hidden = YES;
        self.gsjjView.hidden = NO;
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
