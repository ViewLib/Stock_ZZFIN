//
//  StockMajorNewsTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockMajorNewsTableViewCell.h"
#import "QAView.h"

@implementation StockMajorNewsTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    [_lookMoreBtn ImgRightTextLeft];
    
//    self.valueViewHigh.constant = 0;
}

- (void)setStockCode:(NSString *)stockCode {
    if (stockCode) {
        _stockCode = stockCode;
    }
    _currentDic = [Config shareInstance].stockDic[_stockCode];
    if (_currentDic[@"stockEventsDataLists_2"]) {
        [self reloadView:_currentDic[@"stockEventsDataLists_2"]];
    } else {
        [self getStockEvent];
    }
}

- (void)getStockEvent {
    WS(self)
    [[HttpRequestClient sharedClient] getStockEvent:@{@"stockCode": self.stockCode, @"type":@2} request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] intValue] == 200) {
            NSMutableDictionary *currentStockDic = [Config shareInstance].stockDic[self.stockCode];
            [currentStockDic setValue:dataDict[@"stockEventsDataLists"] forKey:@"stockEventsDataLists_2"];
            [[Config shareInstance].stockDic setValue:currentStockDic forKey:self.stockCode];
            
            [selfWeak reloadView:dataDict[@"stockEventsDataLists"]];
        }
    }];
}

- (void)reloadView:(NSArray *)ary {
    NSMutableArray *array = [NSMutableArray array];
    for (NSDictionary *dic in ary) {
        NSArray *value = dic[@"stockEventsDataModels"];
        if (value.count > 0) {
            [array addObject:dic];
        }
    }
    self.events = array.copy;
    [self addQAView:self.events];
}

- (void)addQAView:(NSArray *) array {
    __block float viewY = 0;
    [array enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        QAView *qa = [[NSBundle mainBundle] loadNibNamed:@"QAView" owner:nil options:nil].firstObject;
        [qa updateView:obj];
        qa.frame = CGRectMake(0, viewY, K_FRAME_BASE_WIDTH-24, qa.viewHigh);
        viewY += qa.viewHigh + 20;
        NSLog(@"ddddssss%f",qa.frame.size.height);
        [self.valueView addSubview:qa];
    }];
    
    self.valueView.clipsToBounds = YES;
    self.valueViewHigh.constant = viewY;
    if (self.reloadTable) {
        self.reloadTable();
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];
    CGRect new = self.contentView.frame;
    new.size.height = self.valueViewHigh.constant + 38 + 10;
    self.contentView.frame = new;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
