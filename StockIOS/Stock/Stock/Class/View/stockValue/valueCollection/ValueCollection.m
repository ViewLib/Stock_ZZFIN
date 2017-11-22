//
//  ValueCollection.m
//  Stock
//
//  Created by mac on 2017/10/22.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "ValueCollection.h"
#import "GDCollectionViewCell.h"
#import "GSJJCollectionViewCell.h"
#import "PJBHCollectionViewCell.h"

@implementation ValueCollection

//初始化ValueCollection
- (id)initWithType:(NSString *)valueType Value:(NSArray *)values {
    if (self = [super init]) {
        self.valueAry = values;
        self.valType = valueType;
        [self registerClass];
        [self valueView];
        if (self.viewLoadBlock) {
            self.viewLoadBlock();
        }
    }
    return self;
}

- (void)reloadWithType:(NSString *)valueType Value:(NSArray *)values {
    self.valueAry = values;
    self.valType = valueType;
    [self registerClass];
    [self.valueView reloadData];
}

- (UICollectionView *)valueView {
    if (!_valueView) {
        UICollectionViewFlowLayout *layout = [UICollectionViewFlowLayout new];
        layout.scrollDirection = UICollectionViewScrollDirectionVertical;
        layout.minimumLineSpacing = 0.0;
        float valueHigh = 0;
        if ([self.valType isEqual:@"GSJJ"] || [self.valType isEqual:@"GD"]) {
            valueHigh = 230;
        } else if ([self.valType isEqual:@"PJBH"]) {
            valueHigh = 200;
        }
        _valueView = [[UICollectionView alloc]initWithFrame:CGRectMake(0, 0, K_FRAME_BASE_WIDTH, valueHigh) collectionViewLayout:layout];
        _valueView.backgroundColor = [UIColor clearColor];
        _valueView.delegate = self;
        _valueView.dataSource = self;
        _valueView.scrollsToTop = NO;
        _valueView.showsVerticalScrollIndicator = NO;
        _valueView.showsHorizontalScrollIndicator = NO;
        [self addSubview:_valueView];
    }
    return _valueView;
}

#pragma mark CollectionViewDelegateAndDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.valueAry.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGSize frame;
    if ([self.valType isEqual:@"GSJJ"]) {
        NSDictionary *dic = self.valueAry[indexPath.item];
        CGFloat w = K_FRAME_BASE_WIDTH-80;
        CGSize size = [self valueSize:dic[@"value"] ValueWidth:w FontSize:12];
        frame = CGSizeMake(K_FRAME_BASE_WIDTH-24, 23);
        if (size.height > 23) {
            frame = CGSizeMake(K_FRAME_BASE_WIDTH-24, size.height+10);
        }
    } else if ([self.valType isEqual:@"GD"]) {
        frame = CGSizeMake(K_FRAME_BASE_WIDTH-24, 30);
        if (indexPath.item > 0) {
            frame = CGSizeMake(K_FRAME_BASE_WIDTH-24, 23);
            NSDictionary *dic = self.valueAry[indexPath.item];
            CGFloat w = (K_FRAME_BASE_WIDTH-24)*0.5;
            CGSize size = [self valueSize:dic[@"stockHolderNmae"] ValueWidth:w FontSize:12];
            if (size.height > 20) {
                frame = CGSizeMake(K_FRAME_BASE_WIDTH-24, size.height+10);
            }
        }
    } else if ([self.valType isEqual:@"PJBH"]) {
        frame = CGSizeMake(K_FRAME_BASE_WIDTH, 23);
    } else {
        frame = CGSizeMake(0, 0);
    }
    return frame;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if ([self.valType isEqual:@"GSJJ"]) {
        GSJJCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"GSJJCollectionViewCell" forIndexPath:indexPath];
        NSDictionary *dic = self.valueAry[indexPath.item];
        cell.name.text = dic[@"title"];
        cell.value.text = dic[@"value"];
        return cell;
    } else if ([self.valType isEqual:@"GD"]) {
        GDCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"GDCollectionViewCell" forIndexPath:indexPath];
        if (indexPath.item != 0) {
            [cell valueType];
            NSDictionary *dic = self.valueAry[indexPath.item];
            cell.name.text = dic[@"stockHolderName"];
            cell.num.text = dic[@"stockHolderAmount"];
            cell.proportion.text = [NSString stringWithFormat:@"%@",dic[@"stockHolderRatio"]];
            if (indexPath.item%2==1) {
                cell.backgroundColor = [UIColor whiteColor];
            } else {
                cell.backgroundColor = [Utils colorFromHexRGB:@"EDF3F8"];
            }
        }
        return cell;
    } else if ([self.valType isEqual:@"PJBH"]) {
        PJBHCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"PJBHCollectionViewCell" forIndexPath:indexPath];
        if (indexPath.item == 0) {
            cell.backgroundColor = [Utils colorFromHexRGB:@"DEEBF6"];
            cell.value1.text = @"券商名称";
            cell.value2.text = @"当前价格";
            cell.value3.text = @"";
            cell.value4.text = @"最高价格";
            cell.value5.text = @"最低价格";
        } else {
            if (indexPath.item%2==1) {
                cell.backgroundColor = [UIColor whiteColor];
            } else {
                cell.backgroundColor = [Utils colorFromHexRGB:@"EDF3F8"];
            }
            NSDictionary *dic = self.valueAry[indexPath.item];
            cell.value1.text = dic[@"stockBrokerName"];
            cell.value2.text = dic[@"showPrice"];
            cell.value3.text = @"";
            cell.value4.text = [dic[@"maxPrice"] stringValue];
            cell.value5.text = [dic[@"minPrice"] stringValue];
        }
        return cell;
    } else {
        UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"meunCall" forIndexPath:indexPath];
        return cell;
    }
}

- (void)registerClass {
    if ([self.valType isEqual:@"GSJJ"]) {
        //公司简介
        [self.valueView registerClass:[GSJJCollectionViewCell class] forCellWithReuseIdentifier:@"GSJJCollectionViewCell"];
    } else if ([self.valType isEqual:@"PJBH"]) {
        //评级变化
        [self.valueView registerClass:[PJBHCollectionViewCell class] forCellWithReuseIdentifier:@"PJBHCollectionViewCell"];
    } else if ([self.valType isEqual:@"GD"]) {
        //股东
        [self.valueView registerClass:[GDCollectionViewCell class] forCellWithReuseIdentifier:@"GDCollectionViewCell"];
    }
}

- (CGSize)valueSize:(NSString *)value ValueWidth:(float)w FontSize:(float)stringSize {
    CGSize size = [value boundingRectWithSize:CGSizeMake(w, 1000) options: NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:stringSize]} context:nil].size;
    return size;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
