//
//  BrokersRatingTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "BrokersRatingTableViewCell.h"
#import "ValueCollection.h"

@implementation BrokersRatingTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.titleAry = @[@"评级变化",@"平均价格",@"券商评论"];
    [_menuCollection registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"meunCall"];
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumInteritemSpacing = 5.0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    [_menuCollection setCollectionViewLayout:layout];
}

- (void)setStockCode:(NSString *)stockCode {
    if (stockCode) {
        _stockCode = stockCode;
    }
    [self getStockGrade];
}

- (void)getStockGrade {
    WS(self)
    [[HttpRequestClient sharedClient]getStockgrade:@{@"stockCode": self.stockCode} request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] intValue] == 200) {
            [selfWeak setInformation:dataDict[@"gradleModelList"]];
        }
    }];
}

- (void)setInformation:(NSArray *)dicAry {
    NSMutableArray *ary = [NSMutableArray arrayWithObject:@""];
    [ary addObjectsFromArray:dicAry];
    self.stockGradeAry = ary;
    [self pjbhView];
}

- (ValueCollection *)pjbhView{
    if (!_pjbhView) {
        _pjbhView = [[ValueCollection alloc] initWithType:@"PJBH" Value:self.stockGradeAry];
        CGRect new = _pjbhView.frame;
        new.origin.x = 0;
        new.origin.y = 0;
        new.size.width = K_FRAME_BASE_WIDTH;
        new.size.height = 200;
        _pjbhView.frame = new;
        [self.valueView addSubview:_pjbhView];
    }
    return _pjbhView;
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
    [label setTag:999001];
    [label setTextColor:[Utils colorFromHexRGB:@"999999"]];
    if (indexPath.item == 0) {
        [label setTextColor:[UIColor blackColor]];
    }
    [label setText:self.titleAry[indexPath.item]];
    [cell.contentView addSubview:label];
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    for (int i = 0; i < self.titleAry.count; i++) {
        UICollectionViewCell *cell = [collectionView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:i inSection:0]];
        UILabel *lab = [cell viewWithTag:999001];
        if (i == indexPath.item) {
            [lab setTextColor:[UIColor blackColor]];
        } else {
            [lab setTextColor:[Utils colorFromHexRGB:@"999999"]];
        }
    }
}

- (void)clickBtn:(UIButton *) btn {
    btn.selected = !btn.selected;
    NSLog(@"%@",btn);
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
