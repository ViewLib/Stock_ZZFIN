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
    
    [[HttpRequestClient sharedClient] getKLineData:@{@"sqlCode": @"day",@"stockCode": self.stockCode} request:^(NSString *resultMsg, id dataDict, id error) {
        __block YYLineDataModel *preModel;
        if ([dataDict[@"resultCode"] intValue] == 200) {
            NSArray *array = dataDict[@"dateDataList"];
            
            NSMutableArray *news = [NSMutableArray array];
            for (int i = 0; i < array.count; i++) {
                NSDictionary *dic = array[i];
                if ([dic[@"volume"] intValue] > 0) {
                    NSDictionary *new = [Utils KlineDicWithDic:dic];
                    [news addObject:new];
                    if (news.count == 780) {
                        break;
                    }
                }
            }
            NSMutableArray *configKNew = [NSMutableArray array];
            NSMutableArray *newAry = [NSMutableArray array];
            [news enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                YYLineDataModel *model = [[YYLineDataModel alloc]initWithDict:obj];
                model.preDataModel = preModel;
                [model updateMA:news index:idx];
                
                NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:obj];
                
                NSString *day = [NSString stringWithFormat:@"%@",obj[@"day"]];
                if ([news count] % 18 == ([news indexOfObject:obj] + 1 )%18 ) {
                    model.showDay = [NSString stringWithFormat:@"%@-%@-%@",[day substringToIndex:4],[day substringWithRange:NSMakeRange(4, 2)],[day substringWithRange:NSMakeRange(6, 2)]];
                    [dic setValue:[NSString stringWithFormat:@"%@-%@-%@",[day substringToIndex:4],[day substringWithRange:NSMakeRange(4, 2)],[day substringWithRange:NSMakeRange(6, 2)]] forKey:@"showDay"];
                }
                [newAry addObject: model];
                [configKNew addObject:dic];
                preModel = model;
            }];
            [[Config shareInstance] setKlineDate:configKNew];
            [self.stockDatadict setObject:newAry forKey:@"dayhqs"];
        }
    }];
    WS(self)
    [[HttpRequestClient sharedClient] getLineData:@{@"stockCode": self.stockCode} request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] intValue] == 200) {
            NSMutableArray *array = [NSMutableArray array];
            for (NSDictionary *dic in dataDict[@"stockMinuteDataModels"]) {
                if (![dic[@"time"] isEqual:@"13:00"]) {
                    NSDictionary *new = [Utils lineDicWithDic:dic avgPrice:selfWeak.zrPrice];
                    YYTimeLineModel *model = [[YYTimeLineModel alloc]initWithDict:new];
                    [array addObject: model];
                }
            }
            [self.stockDatadict setObject:array forKey:@"minutes"];
            [self.stock draw];
        }
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
