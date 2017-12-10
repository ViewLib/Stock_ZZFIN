//
//  StockMajorNewsTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^reloadTable)();

//重大事件
@interface StockMajorNewsTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIView *valueView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *valueViewHigh;
@property (weak, nonatomic) IBOutlet UIButton *lookMoreBtn;

@property (strong, nonatomic) NSString   *stockCode;

@property (strong, nonatomic) NSArray    *events;

@property (strong, nonatomic) NSMutableDictionary   *currentDic;

@property (nonatomic, copy) reloadTable reloadTable;


@end
