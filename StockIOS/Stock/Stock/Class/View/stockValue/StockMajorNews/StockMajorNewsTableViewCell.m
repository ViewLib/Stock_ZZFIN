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
    [self getStockEvent];
}

- (void)getStockEvent {
    WS(self)
    [[HttpRequestClient sharedClient] getStockEvent:@{@"stockCode": self.stockCode, @"type":@2} request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] intValue] == 200) {
            selfWeak.events = [NSArray arrayWithArray:dataDict[@"stockEventsDataLists"]];
            NSMutableArray *array = [NSMutableArray array];
            for (NSDictionary *dic in selfWeak.events) {
                NSArray *value = dic[@"stockEventsDataModels"];
                if (value.count > 0) {
                    [array addObject:dic];
                }
            }
            selfWeak.events = array;
            [selfWeak addQAView:selfWeak.events];
        }
    }];
}

- (void)addQAView:(NSArray *) array {
    float viewY = 0;
    for (NSDictionary *dic in array) {
        QAView *qa = [[NSBundle mainBundle] loadNibNamed:@"QAView" owner:nil options:nil].firstObject;
        [qa updateView:dic];
        qa.frame = CGRectMake(0, viewY, K_FRAME_BASE_WIDTH-24, qa.viewHigh);
        viewY += qa.viewHigh + 20;
        NSLog(@"%f--%f--%f",viewY,qa.viewHigh,qa.frame.origin.y);
        [self.valueView addSubview:qa];
    }
    self.valueViewHigh.constant = viewY;
    [self updateFocusIfNeeded];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    CGRect newViewFrame = self.contentView.frame;
    newViewFrame.size.height = self.valueView.frame.size.height + 78;
    self.contentView.frame = newViewFrame;
    
//    self.frame = self.contentView.frame;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
