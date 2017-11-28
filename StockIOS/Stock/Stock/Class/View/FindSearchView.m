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
    [self.contentCollection registerClass:[FindSearchTwoCollectionViewCell class] forCellWithReuseIdentifier:@"content"];
    [self.leftCollection registerClass:[FindSearchOneCollectionViewCell class] forCellWithReuseIdentifier:@"left"];
    [self.rightCollection registerClass:[FindSearchOneCollectionViewCell class] forCellWithReuseIdentifier:@"right"];
    self.selectItem = 999999;
    self.contentAllChange = [NSMutableArray array];
    self.changeAry = [NSMutableArray array];
    self.changeDicAry = [NSMutableArray arrayWithObjects:@"",@"",@"", nil];
}

- (void)updateCell:(NSDictionary *)dic {
    NSLog(@"%@",dic);
    self.dicFrom = dic[@"groupName"];
    if ([dic[@"groupName"] isEqual:@"选股范围"]) {
        self.leftAry = dic[@"filterGroupList"];
        if ([Config shareInstance].rankList) {
            self.changeDicAry = [Config shareInstance].rankList;
        }
        [self.leftCollection reloadData];
        self.viewOne.hidden = NO;
        self.viewTwo.hidden = YES;
    } else {
        self.viewOne.hidden = YES;
        self.viewTwo.hidden = NO;
        self.contentAry = dic[@"filterGroupList"];
        if ([Config shareInstance].rankOtherList) {
            self.changeAry = [Config shareInstance].rankOtherList;
        }
        [self initcontentAllChange];
        [self.contentCollection reloadData];
    }
}

- (void)initcontentAllChange {
    for (NSDictionary *dic in self.contentAry) {
        [self.contentAllChange addObjectsFromArray:dic[@"filteList"]];
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
    if ([collectionView isEqual:self.contentCollection]) {
        CGSize frame = CGSizeMake(K_FRAME_BASE_WIDTH, 71);
        NSDictionary *dic = self.contentAry[indexPath.item];
        NSArray *filteList = dic[@"filteList"];
        if (filteList.count > 3) {
            frame = CGSizeMake(K_FRAME_BASE_WIDTH, 100);
        }
        return frame;
    } else {
        CGSize frame = CGSizeMake(K_FRAME_BASE_WIDTH/2, 38);
        return frame;
    }
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    if ([collectionView isEqual:self.leftCollection]) {
        FindSearchOneCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"left" forIndexPath:indexPath];
        NSDictionary *dic = self.leftAry[indexPath.item];
        [cell reloadSelect];
        if (self.changeDicAry.count > 0 && [self.changeDicAry objectAtIndex:indexPath.item]) {
            NSDictionary *change = [self.changeDicAry objectAtIndex:indexPath.item];
            if ([change isKindOfClass:[NSDictionary class]] && [change[@"parentGroupId"] isEqual:dic[@"groupId"]]) {
                [cell isSelect];
            }
        }
        if (indexPath.item == self.selectItem) {
            if (cell.selectedImg.hidden) {
                [cell isChange];
            }
        }
        cell.value.text = dic[@"groupName"];
        return cell;
    } else if ([collectionView isEqual:self.rightCollection]) {
        FindSearchOneCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"right" forIndexPath:indexPath];
        [cell reloadSelect];
        NSDictionary *dic = self.rightAry[indexPath.item];
        if (self.changeDicAry.count > 0 && [self.changeDicAry objectAtIndex:self.selectItem]) {
            NSDictionary *new =  self.changeDicAry[self.selectItem];
            if ([new isKindOfClass:[NSDictionary class]]) {
                if ([dic[@"filterId"] isEqual:new[@"filterId"]]) {
                    [cell isSelect];
                }
            }
        }
        
        cell.value.text = dic[@"filterName"];
        return cell;
    } else {
        FindSearchTwoCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"content" forIndexPath:indexPath];
        [cell.changeView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
        NSDictionary *dic = self.contentAry[indexPath.item];
        cell.title.text = dic[@"groupName"];
        NSInteger num = [(NSArray *)dic[@"filteList"] count];
        if (num > 0) {
            cell.changeView.hidden = NO;
            [dic[@"filteList"] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                if (obj) {
                    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
                    [btn setBackgroundColor:[Utils colorFromHexRGB:@"F6F6F6"]];
                    [btn.layer setCornerRadius:3];
                    [btn setTitleColor:[Utils colorFromHexRGB:@"6C6C6C"] forState:UIControlStateNormal];
                    [btn setTitleColor:MAIN_COLOR forState:UIControlStateSelected];
                    [btn setTitle:obj[@"filterName"] forState:UIControlStateNormal];
                    [btn setTag:[obj[@"filterId"] integerValue]];
                    [btn.titleLabel setFont:[UIFont systemFontOfSize:13]];
                    [btn setFrame:CGRectMake(0, 0, K_FRAME_BASE_WIDTH/6, 28)];
                    if (idx == 0) {
                        [btn setCenterX:(K_FRAME_BASE_WIDTH-24)*(idx+1)/4-30];
                    } else if (idx == 2) {
                        [btn setCenterX:(K_FRAME_BASE_WIDTH-24)*(idx+1)/4+30];
                    } else {
                        [btn setCenterX:(K_FRAME_BASE_WIDTH-24)*(idx+1)/4];
                    }
                    
                    if (idx > 2) {
                        cell.changeView.y = 60;
                        [btn setCenterY:42];
                    }
                    
                    if ([self.changeAry containsObject:obj]) {
                        [btn setBackgroundColor:rgba(24,109,183,0.18)];
                        btn.selected = YES;
                    }
                    [btn addTarget:self action:@selector(clickFilterBtn:) forControlEvents:UIControlEventTouchUpInside];
                    [cell.changeView addSubview:btn];
                }
            }];
        } else {
            cell.changeView.hidden = YES;
        }
        return cell;
    }
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if ([collectionView isEqual:self.leftCollection]) {
        NSDictionary *dic = self.leftAry[indexPath.item];
        self.rightAry = dic[@"filteList"];
        NSArray *resultArray = [self.rightAry sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
            NSNumber *number1 = obj1[@"filterId"];
            NSNumber *number2 = obj2[@"filterId"];
            NSComparisonResult result = [number1 compare:number2];
            return result == NSOrderedDescending; // 升序
            //        return result == NSOrderedAscending;  // 降序
        }];
        self.rightAry = resultArray;
        self.selectItem = (int)indexPath.item;
        [self.leftAry enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            if (obj) {
                FindSearchOneCollectionViewCell *cell = (FindSearchOneCollectionViewCell *)[collectionView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:idx inSection:0]];
                if (cell.selectedImg.hidden) {
                    if (idx == indexPath.item) {
//                        if (cell.hasChange && cell.selectedImg.hidden) {
//                            [cell reloadSelect];
//                        } else
                        if (!cell.hasChange && cell.selectedImg.hidden) {
                            [cell isChange];
                        }
                    } else if (cell.selectedImg.hidden) {
                        [cell reloadSelect];
                    }
                }
            }
        }];
        if (self.rightAry.count > 0) {
            [self.rightCollection reloadData];
        }
    } else if ([collectionView isEqual:self.rightCollection]) {
        NSDictionary *dic = self.rightAry[indexPath.item];
        NSDictionary *obj = self.changeDicAry[self.selectItem];
        if ([obj isKindOfClass:[NSDictionary class]]) {
            if ([obj[@"filterId"] isEqual:dic[@"filterId"]]) {
                [self.changeDicAry replaceObjectAtIndex:self.selectItem withObject:@""];
            }
        } else {
            [self.changeDicAry replaceObjectAtIndex:self.selectItem withObject:dic];
        }
        
        [self.rightAry enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            if (obj) {
                FindSearchOneCollectionViewCell *cell = (FindSearchOneCollectionViewCell *)[collectionView cellForItemAtIndexPath:[NSIndexPath indexPathForItem:idx inSection:0]];
                if (idx == indexPath.item) {
                    if (cell.selectedImg.hidden) {
                        [cell isSelect];
                    } else {
                        [cell reloadSelect];
                    }
                } else {
                    [cell reloadSelect];
                }
            }
        }];
        [self.leftCollection reloadData];
    }
}

- (void)clickFilterBtn:(UIButton *)sender {
    sender.selected = !sender.selected;
    [self.contentAllChange enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (sender.selected) {
            [sender setBackgroundColor:rgba(24,109,183,0.18)];
            if ([obj[@"filterId"] integerValue] == sender.tag) {
                [self.changeAry addObject:obj];
            }
        } else {
            [sender setBackgroundColor:[Utils colorFromHexRGB:@"F6F6F6"]];
            if ([obj[@"filterId"] integerValue] == sender.tag) {
                [self.changeAry removeObject:obj];
            }
        }
    }];
}

- (IBAction)clickOneReloadBtn:(UIButton *)sender {
    self.changeDicAry = [NSMutableArray arrayWithObjects:@"",@"",@"", nil];
    [self.leftCollection reloadData];
    [self.rightCollection reloadData];
    [[Config shareInstance] setRankList:nil];
}

- (IBAction)clickOneSuccessBtn:(UIButton *)sender {
    [[Config shareInstance] setRankList:self.changeDicAry];
    if (self.collectionCellClick) {
        self.collectionCellClick();
    }
}

- (IBAction)clickTwoReloadBtn:(UIButton *)sender {
    self.changeAry = [NSMutableArray array];
    [self.contentCollection reloadData];
    [[Config shareInstance] setRankList:nil];
}

- (IBAction)clickTwoSuccessBtn:(UIButton *)sender {
    [[Config shareInstance] setRankOtherList:self.changeAry];
    if (self.collectionCellClick) {
        self.collectionCellClick();
    }
}



/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
