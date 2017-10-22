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
    self.titleAry = @[@"公司简介",@"产品",@"区域",@"费用",@"员工",@"股东",@"管理层"];
    [_MenuCollection registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"meunCall"];
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumInteritemSpacing = 5.0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    [_MenuCollection setCollectionViewLayout:layout];
    [self initValueView];
}

//
- (void)initValueView {
    ValueCollection *view = [[ValueCollection alloc] initWithType:@"GSJJ" Value:@[@{@"title": @"公司名称",@"value":@"万达院线"},@{@"title": @"成立时间",@"value":@"1981年"},@{@"title": @"总部位置",@"value":@"北京"},@{@"title": @"主营业务",@"value":@"票房、爆米花"},@{@"title": @"去年业绩",@"value":@"上涨5%，到达120块钱"},@{@"title": @"董事长",@"value":@"王健林（大股东并持股67%）"},@{@"title": @"估值",@"value":@"36x（高于行业32平均值x）"},@{@"title": @"主要对手",@"value":@"港股、星美传媒"},@{@"title": @"分析师股价",@"value":@"130块，高于现在股价87块"}]];
    CGRect new = view.frame;
    new.origin.x = 0;
    new.origin.y = 0;
    new.size.width = K_FRAME_BASE_WIDTH;
    new.size.height = 230;
    view.frame = new;
    [self.ValueView addSubview:view];
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
