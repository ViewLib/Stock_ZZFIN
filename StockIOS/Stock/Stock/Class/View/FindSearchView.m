//
//  FindSearchView.m
//  Stock
//
//  Created by mac on 2017/10/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "FindSearchView.h"
#import "FindSearchOneCollectionViewCell.h"
#import "FindSearchTwoCollectionViewCell.h"

@implementation FindSearchView

- (void)awakeFromNib {
    [super awakeFromNib];
//    [self initCollection];
    [self.contentCollection registerClass:[FindSearchTwoCollectionViewCell class] forCellWithReuseIdentifier:@"content"];
    [self.leftCollection registerClass:[FindSearchOneCollectionViewCell class] forCellWithReuseIdentifier:@"left"];
    [self.rightCollection registerClass:[FindSearchOneCollectionViewCell class] forCellWithReuseIdentifier:@"right"];
    self.changeDicAry = [NSMutableArray arrayWithObjects:[NSMutableArray array],[NSMutableArray array],[NSMutableArray array], nil];
}

- (void)updateCell:(NSDictionary *)dic {
    NSLog(@"%@",dic);
    self.dicFrom = dic[@"groupName"];
    if ([dic[@"groupName"] isEqual:@"选股范围"]) {
        self.leftAry = dic[@"filterGroupList"];
        [self.leftCollection reloadData];
    } else {
        self.viewOne.hidden = YES;
        self.viewTwo.hidden = NO;
    }
}

#pragma mark - UICollectionViewDelegateAndDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    if ([collectionView isEqual:self.leftCollection]) {
        return self.leftAry.count;
    } else if ([collectionView isEqual:self.rightCollection]) {
        return self.rightAry.count;
    } else {
        return self.contentAry.count;
    }
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGSize frame = CGSizeMake(K_FRAME_BASE_WIDTH/2, 38);
    return frame;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    if ([collectionView isEqual:self.leftCollection]) {
        FindSearchOneCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"left" forIndexPath:indexPath];
        NSDictionary *dic = self.leftAry[indexPath.item];
        cell.value.text = dic[@"groupName"];
        return cell;
    } else if ([collectionView isEqual:self.rightCollection]) {
        FindSearchOneCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"right" forIndexPath:indexPath];
        NSDictionary *dic = self.rightAry[indexPath.item];
        cell.value.text = dic[@"filterName"];
        return cell;
    } else {
        FindSearchTwoCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"content" forIndexPath:indexPath];
        return cell;
    }
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if ([collectionView isEqual:self.leftCollection]) {
        NSDictionary *dic = self.leftAry[indexPath.item];
        self.rightAry = dic[@"filteList"];
        if (self.rightAry.count > 0) {
            [self.rightCollection reloadData];
        }
        self.selectItem = indexPath.item;
        [self.leftAry enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            if (obj) {
                FindSearchOneCollectionViewCell *cell = (FindSearchOneCollectionViewCell *)[collectionView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:idx inSection:0]];
                if (idx == indexPath.item) {
                    if (cell.hasChange && cell.selectedImg.hidden) {
                        [cell reloadSelect];
                    } else if (!cell.hasChange && cell.selectedImg.hidden) {
                        [cell isChange];
                    }
                } else if (cell.selectedImg.hidden) {
                    [cell reloadSelect];
                }
            }
        }];
        
       
    } else if ([collectionView isEqual:self.rightCollection]) {
        NSDictionary *dic = self.rightAry[indexPath.item];
        
//        if (self.rightCollectionCellClick) {
//            self.rightCollectionCellClick(dic,self.dicFrom);
//        }
    }
}

//collectionView初始化属性
- (void)initCollection {
    UICollectionViewFlowLayout *layout = [UICollectionViewFlowLayout new];
    layout.scrollDirection = UICollectionViewScrollDirectionVertical;
    layout.minimumLineSpacing = 0.0;
    _contentCollection.scrollsToTop = NO;
    _contentCollection.collectionViewLayout = layout;
    _contentCollection.showsVerticalScrollIndicator = NO;
    _leftCollection.scrollsToTop = NO;
    _leftCollection.collectionViewLayout = layout;
    _leftCollection.showsVerticalScrollIndicator = NO;
    _rightCollection.scrollsToTop = NO;
    _rightCollection.collectionViewLayout = layout;
    _rightCollection.showsVerticalScrollIndicator = NO;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
