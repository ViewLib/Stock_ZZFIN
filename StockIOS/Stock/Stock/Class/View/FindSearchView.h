//
//  FindSearchView.h
//  Stock
//
//  Created by mac on 2017/10/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^rightCollectionCellClick)(id obj,NSString *from);

@interface FindSearchView : UIView<UICollectionViewDelegate,UICollectionViewDataSource>
@property (weak, nonatomic) IBOutlet UIView *viewOne;
@property (weak, nonatomic) IBOutlet UICollectionView *leftCollection;
@property (weak, nonatomic) IBOutlet UICollectionView *rightCollection;


@property (weak, nonatomic) IBOutlet UIView *viewTwo;
@property (weak, nonatomic) IBOutlet UICollectionView *contentCollection;

@property (strong, nonatomic) NSArray   *leftAry;
@property (strong, nonatomic) NSArray   *rightAry;
@property (strong, nonatomic) NSArray   *contentAry;

@property (assign, nonatomic) int   *selectItem;
@property (strong, nonatomic) NSMutableArray    *changeDicAry;

@property (strong, nonatomic) NSString   *dicFrom;

@property (copy, nonatomic) rightCollectionCellClick    rightCollectionCellClick;

- (void)updateCell:(NSDictionary *)dic;

@end
