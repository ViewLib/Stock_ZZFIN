//
//  ComputerIntroductionTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ValueCollection.h"

//公司介绍
@interface ComputerIntroductionTableViewCell : UITableViewCell<UICollectionViewDelegate,UICollectionViewDataSource>

@property (weak, nonatomic) IBOutlet UICollectionView *MenuCollection;
@property (weak, nonatomic) IBOutlet UIView *ValueView;
//公司简介的点击名称
@property (strong, nonatomic) NSArray   *titleAry;
//当前股票代码
@property (strong, nonatomic) NSString   *stockCode;
//当前股票名称
@property (strong, nonatomic) NSString   *stockName;
//公司简介
@property (strong, nonatomic) NSArray   *informationAry;
//公司股东
@property (strong, nonatomic) NSArray   *stockHolderAry;

@property (strong, nonatomic) NSMutableDictionary   *currentDic;

@property (strong, nonatomic) ValueCollection *gsjjView;

@property (strong, nonatomic) ValueCollection *gdView;

@end
