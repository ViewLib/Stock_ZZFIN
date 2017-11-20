//
//  ValueCollection.h
//  Stock
//
//  Created by mac on 2017/10/22.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^viewLoadBlock)(void);

@interface ValueCollection : UIView<UICollectionViewDelegate,UICollectionViewDataSource>

@property (nonatomic, strong)   UICollectionView    *valueView;

@property (strong, nonatomic) NSArray   *valueAry;
@property (strong, nonatomic) NSString   *valType;

@property (nonatomic, copy) viewLoadBlock  viewLoadBlock;

//初始化ValueCollection
- (id)initWithType:(NSString *)valueType Value:(NSArray *)values;

@end
