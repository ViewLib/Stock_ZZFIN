//
//  FindSearchView.h
//  Stock
//
//  Created by mac on 2017/10/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^collectionCellClick)();

@interface FindSearchView : UIView<UICollectionViewDelegate,UICollectionViewDataSource>
@property (weak, nonatomic) IBOutlet UIView *viewOne;
@property (weak, nonatomic) IBOutlet UICollectionView *leftCollection;
@property (weak, nonatomic) IBOutlet UICollectionView *rightCollection;
@property (weak, nonatomic) IBOutlet UIButton *oneReloadBtn;
@property (weak, nonatomic) IBOutlet UIButton *oneSuccessBtn;



@property (weak, nonatomic) IBOutlet UIView *viewTwo;
@property (weak, nonatomic) IBOutlet UICollectionView *contentCollection;
@property (weak, nonatomic) IBOutlet UIButton *twoReloadBtn;
@property (weak, nonatomic) IBOutlet UIButton *twoSuccessBtn;

@property (strong, nonatomic) NSArray   *leftAry;
@property (strong, nonatomic) NSArray   *rightAry;
@property (strong, nonatomic) NSArray   *contentAry;

@property (assign, nonatomic) int   selectItem;
@property (strong, nonatomic) NSMutableArray    *changeDicAry;
@property (strong, nonatomic) NSMutableArray    *changeAry;
@property (strong, nonatomic) NSMutableArray    *contentAllChange;
@property (strong, nonatomic) NSString   *dicFrom;

@property (copy, nonatomic) collectionCellClick    collectionCellClick;

- (void)updateCell:(NSDictionary *)dic;

@end
