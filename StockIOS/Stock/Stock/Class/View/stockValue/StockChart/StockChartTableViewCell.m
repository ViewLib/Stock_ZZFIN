//
//  StockChartTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockChartTableViewCell.h"
#import <Masonry/Masonry.h>

@implementation StockChartTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self initStockView];
    [self fetchData];
    
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5f * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//        [self stock_enterFullScreen:self.stock.containerView.gestureRecognizers.firstObject];
//    });
}

- (void)initStockView {
    [YYStockVariable setStockLineWidthArray:@[@6,@6,@6,@6]];
    
    YYStock *stock = [[YYStock alloc]initWithFrame:self.stockContainerView.frame dataSource:self];
    _stock = stock;
    [self.stockContainerView addSubview:stock.mainView];
    [stock.mainView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.stockContainerView);
    }];
}

/*******************************************股票数据源获取更新*********************************************/
/**
 网络获取K线数据
 */
- (void)fetchData {
    
    [HttpRequestClient Get:@"day" params:nil success:^(NSDictionary *response) {
        NSMutableArray *array = [NSMutableArray array];
        __block YYLineDataModel *preModel;
        [response[@"dayhqs"] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            YYLineDataModel *model = [[YYLineDataModel alloc]initWithDict:obj];
            model.preDataModel = preModel;
            [model updateMA:response[@"dayhqs"] index:idx];
            NSString *day = [NSString stringWithFormat:@"%@",obj[@"day"]];
            if ([response[@"dayhqs"] count] % 18 == ([response[@"dayhqs"] indexOfObject:obj] + 1 )%18 ) {
                model.showDay = [NSString stringWithFormat:@"%@-%@-%@",[day substringToIndex:4],[day substringWithRange:NSMakeRange(4, 2)],[day substringWithRange:NSMakeRange(6, 2)]];
            }
            [array addObject: model];
            preModel = model;
        }];
        [self.stockDatadict setObject:array forKey:@"dayhqs"];
    } fail:^(NSDictionary *info) {
        
    }];
    
    [HttpRequestClient Get:@"minute" params:nil success:^(NSDictionary *response) {
        NSMutableArray *array = [NSMutableArray array];
        [response[@"minutes"] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            YYTimeLineModel *model = [[YYTimeLineModel alloc]initWithDict:obj];
            [array addObject: model];
        }];
        [self.stockDatadict setObject:array forKey:@"minutes"];
        [self.stock draw];
        
    } fail:^(NSDictionary *info) {
    }];
}


/*******************************************股票数据源代理*********************************************/
-(NSArray <NSString *> *) titleItemsOfStock:(YYStock *)stock {
    return self.stockTopBarTitleArray;
}

-(NSArray *) YYStock:(YYStock *)stock stockDatasOfIndex:(NSInteger)index {
    return index < self.stockDataKeyArray.count ? self.stockDatadict[self.stockDataKeyArray[index]] : nil;
}

-(YYStockType)stockTypeOfIndex:(NSInteger)index {
    return index == 0 ? YYStockTypeTimeLine : YYStockTypeLine;
}

- (id<YYStockFiveRecordProtocol>)fiveRecordModelOfIndex:(NSInteger)index {
    return self.fiveRecordModel;
}

//是否显示5档买卖图
- (BOOL)isShowfiveRecordModelOfIndex:(NSInteger)index {
    return NO;
}

/*******************************************getter*********************************************/
- (NSMutableDictionary *)stockDatadict {
    if (!_stockDatadict) {
        _stockDatadict = [NSMutableDictionary dictionary];
    }
    return _stockDatadict;
}

- (NSArray *)stockDataKeyArray {
    if (!_stockDataKeyArray) {
        _stockDataKeyArray = @[@"minutes",@"dayhqs"];
    }
    return _stockDataKeyArray;
}

- (NSArray *)stockTopBarTitleArray {
    if (!_stockTopBarTitleArray) {
        _stockTopBarTitleArray = @[@"分时",@"日K"];
//        _stockTopBarTitleArray = @[@"分时",@"日K",@"周K",@"月K"];
    }
    return _stockTopBarTitleArray;
}

- (NSString *)getToday {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"yyyyMMdd";
    return [dateFormatter stringFromDate:[NSDate date]];
}

- (void)dealloc {
    NSLog(@"DEALLOC");
}

- (BOOL)prefersStatusBarHidden {
    return YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
