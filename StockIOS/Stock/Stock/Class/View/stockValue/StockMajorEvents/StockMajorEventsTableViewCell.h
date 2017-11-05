//
//  StockMajorEventsTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LineView.h"

//重大消息
@interface StockMajorEventsTableViewCell : UITableViewCell<UICollectionViewDelegate,UICollectionViewDataSource>

@property (weak, nonatomic) IBOutlet UICollectionView *MenuCollection;

@property (strong, nonatomic) NSArray *titleAry;

@property (strong, nonatomic) NSString *titleNews;

@property (weak, nonatomic) IBOutlet UIView *lineView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *LineViewHigh;

@property (strong, nonatomic) NSString *stockCode;

@property (strong, nonatomic) NSArray *events;

@property (nonatomic, strong) NSTimer *timer;

@property (nonatomic) LineView *line;


@end
