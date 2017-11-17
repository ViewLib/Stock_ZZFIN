//
//  StockObjEntity.h
//  Stock
//
//  Created by mac on 2017/11/17.
//  strongright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StockObjEntity : NSObject

//名字
@property (nonatomic, strong) NSString *name;

//代码
@property (nonatomic, strong) NSString *code;

//当前价格
@property (nonatomic, strong) NSString *currentprice;

//昨收
@property (nonatomic, strong) NSString *zsprice;

//今开
@property (nonatomic, strong) NSString *jkprice;

//成交量（手）
@property (nonatomic, strong) NSString *volume;

//外盘
@property (nonatomic, strong) NSString *odisc;

//内盘
@property (nonatomic, strong) NSString *invol;

//买1
@property (nonatomic, strong) NSString *buy1;

//买1量
@property (nonatomic, strong) NSString *buy1num;

//买2
@property (nonatomic, strong) NSString *buy2;

//买2量
@property (nonatomic, strong) NSString *buy2num;

//买3
@property (nonatomic, strong) NSString *buy3;

//买3量
@property (nonatomic, strong) NSString *buy3num;

//买4
@property (nonatomic, strong) NSString *buy4;

//买4量
@property (nonatomic, strong) NSString *buy4num;

//买5
@property (nonatomic, strong) NSString *buy5;

//买5量
@property (nonatomic, strong) NSString *buy5num;

//卖1
@property (nonatomic, strong) NSString *sell1;

//卖1量
@property (nonatomic, strong) NSString *sell1num;

//卖2
@property (nonatomic, strong) NSString *sell2;

//卖2量
@property (nonatomic, strong) NSString *sell2num;

//卖3
@property (nonatomic, strong) NSString *sell3;

//卖3量
@property (nonatomic, strong) NSString *sell3num;

//卖4
@property (nonatomic, strong) NSString *sell4;

//卖4量
@property (nonatomic, strong) NSString *sell4num;

//卖5
@property (nonatomic, strong) NSString *sell5;

//卖5量
@property (nonatomic, strong) NSString *sell5num;

//最近逐笔成交
@property (nonatomic, strong) NSString *recentTransaction;

//时间
@property (nonatomic, strong) NSString *time;

//涨跌
@property (nonatomic, strong) NSString *upsdowns;

//涨跌%
@property (nonatomic, strong) NSString *pricefluctuation;

//最高
@property (nonatomic, strong) NSString *highest;

//最低
@property (nonatomic, strong) NSString *lowest;

//价格/成交量（手）/成交额
@property (nonatomic, strong) NSString *price;

//成交额（万）
@property (nonatomic, strong) NSString *transactions;

//换手率
@property (nonatomic, strong) NSString *turnoverrate;

//市盈率
@property (nonatomic, strong) NSString *peratios;

//振幅
@property (nonatomic, strong) NSString *amplitude;

//流通市值
@property (nonatomic, strong) NSString *famc;

//总市值
@property (nonatomic, strong) NSString *totalmarketcapitalization;

//市净率
@property (nonatomic, strong) NSString *pbratio;

//涨停价
@property (nonatomic, strong) NSString *highlimit;

//跌停价
@property (nonatomic, strong) NSString *limit;

//id 排序用
@property (nonatomic, strong) NSString *iD;

//置顶
@property (nonatomic, strong) NSString *isTop;

//初始化
- (id)initWithArray:(NSArray *)ary;

//初始化
- (id)initWithStockEntity:(StockEntity *)stock;

@end
