//
//  StockEntity.h
//  Stock
//
//  Created by mac on 2017/9/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <CoreData/CoreData.h>

@interface StockEntity : NSManagedObject

//名字
@property (nullable, nonatomic, copy) NSString *name;

//代码
@property (nullable, nonatomic, copy) NSString *code;

//当前价格
@property (nullable, nonatomic, copy) NSString *currentprice;

//昨收
@property (nullable, nonatomic, copy) NSString *zsprice;

//今开
@property (nullable, nonatomic, copy) NSString *jkprice;

//成交量（手）
@property (nullable, nonatomic, copy) NSString *volume;

//外盘
@property (nullable, nonatomic, copy) NSString *odisc;

//内盘
@property (nullable, nonatomic, copy) NSString *invol;

//买1
@property (nullable, nonatomic, copy) NSString *buy1;

//买1量
@property (nullable, nonatomic, copy) NSString *buy1num;

//买2
@property (nullable, nonatomic, copy) NSString *buy2;

//买2量
@property (nullable, nonatomic, copy) NSString *buy2num;

//买3
@property (nullable, nonatomic, copy) NSString *buy3;

//买3量
@property (nullable, nonatomic, copy) NSString *buy3num;

//买4
@property (nullable, nonatomic, copy) NSString *buy4;

//买4量
@property (nullable, nonatomic, copy) NSString *buy4num;

//买5
@property (nullable, nonatomic, copy) NSString *buy5;

//买5量
@property (nullable, nonatomic, copy) NSString *buy5num;

//卖1
@property (nullable, nonatomic, copy) NSString *sell1;

//卖1量
@property (nullable, nonatomic, copy) NSString *sell1num;

//卖2
@property (nullable, nonatomic, copy) NSString *sell2;

//卖2量
@property (nullable, nonatomic, copy) NSString *sell2num;

//卖3
@property (nullable, nonatomic, copy) NSString *sell3;

//卖3量
@property (nullable, nonatomic, copy) NSString *sell3num;

//卖4
@property (nullable, nonatomic, copy) NSString *sell4;

//卖4量
@property (nullable, nonatomic, copy) NSString *sell4num;

//卖5
@property (nullable, nonatomic, copy) NSString *sell5;

//卖5量
@property (nullable, nonatomic, copy) NSString *sell5num;

//最近逐笔成交
@property (nullable, nonatomic, copy) NSString *recentTransaction;

//时间
@property (nullable, nonatomic, copy) NSString *time;

//涨跌
@property (nullable, nonatomic, copy) NSString *upsdowns;

//涨跌%
@property (nullable, nonatomic, copy) NSString *pricefluctuation;

//最高
@property (nullable, nonatomic, copy) NSString *highest;

//最低
@property (nullable, nonatomic, copy) NSString *lowest;

//价格/成交量（手）/成交额
@property (nullable, nonatomic, copy) NSString *price;

//成交额（万）
@property (nullable, nonatomic, copy) NSString *transactions;

//换手率
@property (nullable, nonatomic, copy) NSString *turnoverrate;

//市盈率
@property (nullable, nonatomic, copy) NSString *peratios;

//振幅
@property (nullable, nonatomic, copy) NSString *amplitude;

//流通市值
@property (nullable, nonatomic, copy) NSString *famc;

//总市值
@property (nullable, nonatomic, copy) NSString *totalmarketcapitalization;

//市净率
@property (nullable, nonatomic, copy) NSString *pbratio;

//涨停价
@property (nullable, nonatomic, copy) NSString *highlimit;

//跌停价
@property (nullable, nonatomic, copy) NSString *limit;























@end
