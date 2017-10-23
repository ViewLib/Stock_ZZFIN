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
    [self initValue];
}

- (void)initValue {
    ValueCollection *view = [[ValueCollection alloc] initWithType:@"PJBH" Value:@[@"",@"",@"",@"",@"",@""]];
    CGRect new = view.frame;
    new.origin.x = 0;
    new.origin.y = 0;
    new.size.width = K_FRAME_BASE_WIDTH;
    new.size.height = 200;
    view.frame = new;
    [self.valueView addSubview:view];
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
