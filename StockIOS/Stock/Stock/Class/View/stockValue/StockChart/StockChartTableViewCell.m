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
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5f * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//        [self stock_enterFullScreen:self.stock.containerView.gestureRecognizers.firstObject];
//    });
}

- (void)setStockCode:(NSString *)stockCode {
    if (stockCode) {
        _stockCode = stockCode;
    }
    [self fetchData];
}

- (void)initStockView {
    [YYStockVariable setStockLineWidthArray:@[@4,@4,@4,@4]];
    
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
    [self getLine];
    [self getKlineWith:@"dayhqs"];
    [self getKlineWith:@"weekhqs"];
    [self getKlineWith:@"monthhqs"];
}

- (void)getLine {
    WS(self)
    [[HttpRequestClient sharedClient] getLineData:[self.stockCode substringFromIndex:2] request:^(NSString *resultMsg, id dataDict, id error) {
        if (dataDict) {
            NSDictionary *dic = [dataDict firstObject];
            [Config shareInstance].sumPrice = 0;
            if ([dic[@"time"] rangeOfString:@"当天没数据"].location == NSNotFound) {
                NSMutableArray *array = [NSMutableArray array];
                
                [dataDict enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                    if (![obj[@"time"] isEqual:@"13:00"]) {
                        NSDictionary *new = [Utils lineDicWithDic:obj avgPrice:selfWeak.zrPrice num:idx];
                        YYTimeLineModel *model = [[YYTimeLineModel alloc]initWithDict:new];
                        [array addObject: model];
                    }
                }];
                
                [selfWeak.stockDatadict setObject:array forKey:@"minutes"];
                [selfWeak.stock draw];
            } else {
                [self showMessageHud:@"当天没数据" hideAfter:1];
            }
        } else {
            [self showMessageHud:@"接口挂了" hideAfter:1];
        }
    }];
}

- (void)getKlineWith:(NSString *)type {
    WS(self)
    if ([type isEqual:@"dayhqs"]) {
        [[HttpRequestClient sharedClient] getKLineDataForDay:self.stockCode request:^(NSString *resultMsg, id dataDict, id error) {
            if (dataDict) {
                [selfWeak createWithData:dataDict andType:@"dayhqs"];
            }
        }];
    } else if ([type isEqual:@"weekhqs"]) {
        [[HttpRequestClient sharedClient] getKLineDataForWeek:self.stockCode request:^(NSString *resultMsg, id dataDict, id error) {
            if (dataDict) {
                [selfWeak createWithData:dataDict andType:@"weekhqs"];
            }
        }];
    } else if ([type isEqual:@"monthhqs"]) {
        [[HttpRequestClient sharedClient] getKLineDataForMonth:self.stockCode request:^(NSString *resultMsg, id dataDict, id error) {
            if (dataDict) {
                [selfWeak createWithData:dataDict andType:@"monthhqs"];
            }
        }];
    }
    
}

- (void)createWithData:(NSArray *)dataDict andType:(NSString *)type {
    __block YYLineDataModel *preModel;
    NSArray *array = dataDict;
    
    NSMutableArray *news = [NSMutableArray array];
    for (int i = 0; i < array.count; i++) {
        NSDictionary *dic = array[i];
        if ([dic[@"volume"] intValue] > 0) {
            NSDictionary *new = [Utils KlineDicWithDic:dic];
            [news addObject:new];
        }
    }
    
    NSMutableArray *configKNew = [NSMutableArray array];
    NSMutableArray *newAry = [NSMutableArray array];
    [news enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        YYLineDataModel *model = [[YYLineDataModel alloc]initWithDict:obj];
        model.preDataModel = preModel;
        [model updateMA:news index:idx];
        
        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:obj];
        int x = 15;
        if ([type isEqual:@"weekhqs"]) {
            x = 26;
        } else if ([type isEqual:@"monthhqs"]) {
            x = 24;
        }
        NSString *day = [NSString stringWithFormat:@"%@",obj[@"day"]];
        if ([news count] % x == ([news indexOfObject:obj] + 1 ) % x ) {
            NSString *showDay = [NSString stringWithFormat:@"%@-%@-%@",[day substringToIndex:4],[day substringWithRange:NSMakeRange(4, 2)],[day substringWithRange:NSMakeRange(6, 2)]];
            if ([type isEqual:@"dayhqs"]) {
                model.showDay = showDay;
                [dic setValue:showDay forKey:@"showDay"];
            } else {
                showDay = [NSString stringWithFormat:@"%@/%@",[day substringToIndex:4],[day substringWithRange:NSMakeRange(4, 2)]];
                model.showDay = showDay;
            }
        }
        [newAry addObject: model];
        [configKNew addObject:dic];
        preModel = model;
    }];
    
    if ([type isEqualToString:@"dayhqs"]) {
        [[Config shareInstance] setKlineDate:configKNew];
    }
    
    [self.stockDatadict setObject:newAry forKey:type];
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
        _stockDataKeyArray = @[@"minutes",@"dayhqs",@"weekhqs",@"monthhqs"];
    }
    return _stockDataKeyArray;
}

- (NSArray *)stockTopBarTitleArray {
    if (!_stockTopBarTitleArray) {
//        _stockTopBarTitleArray = @[@"分时",@"日K"];
        _stockTopBarTitleArray = @[@"分时",@"日K",@"周K",@"月K"];
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
