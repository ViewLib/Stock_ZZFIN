//
//  StockObjEntity.m
//  Stock
//
//  Created by mac on 2017/11/17.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockObjEntity.h"

@implementation StockObjEntity

- (id)initWithArray:(NSArray *)ary {
    if (self = [super init]) {
        //名字
        self.name = ary[1];
        //代码
        self.code = ary[2];
        if (ary[0]) {
            NSArray *arys = [ary[0] componentsSeparatedByString:@"="];
            NSString *code = [arys firstObject];
            NSArray *ary2 = [code componentsSeparatedByString:@"_"];
            code = [ary2 lastObject];
            if ([code hasSuffix:ary[2]]) {
                self.code = code;
            }
        }
        //当前价格
        self.currentprice = ary[3];
        //昨收
        self.zsprice = ary[4];
        //今开
        self.jkprice = ary[5];
        //成交量（手）
        self.volume = ary[6];
        //外盘
        self.odisc = ary[7];
        //内盘
        self.invol = ary[8];
        //买1
        self.buy1 = ary[9];
        //买1量
        self.buy1num = ary[10];
        //买2
        self.buy2 = ary[11];
        //买2量
        self.buy2num = ary[12];
        //买3
        self.buy3 = ary[13];
        //买3量
        self.buy3num = ary[14];
        //买4
        self.buy4 = ary[15];
        //买4量
        self.buy4num = ary[16];
        //买5
        self.buy5 = ary[17];
        //买5量
        self.buy5num = ary[18];
        //卖1
        self.sell1 = ary[19];
        //卖1量
        self.sell1num = ary[20];
        //卖2
        self.sell2 = ary[21];
        //卖2量
        self.sell2num = ary[22];
        //卖3
        self.sell3 = ary[23];
        //卖3量
        self.sell3num = ary[24];
        //卖4
        self.sell4 = ary[25];
        //卖4量
        self.sell4num = ary[26];
        //卖5
        self.sell5 = ary[27];
        //卖5量
        self.sell5num = ary[28];
        //最近逐笔成交
        self.recentTransaction = ary[29];
        //时间
        self.time = ary[30];
        //涨跌
        self.upsdowns = ary[31];
        //涨跌%
        self.pricefluctuation = ary[32];
        //最高
        self.highest = ary[33];
        //最低
        self.lowest= ary[34];
        //价格/成交量（手）/成交额
        self.price = ary[35];
        //成交额（万）
        self.transactions = ary[37];
        //换手率
        self.turnoverrate = ary[38];
        //市盈率
        self.peratios = ary[39];
        //振幅
        self.amplitude = ary[43];
        //流通市值
        self.famc = ary[44];
        //总市值
        self.totalmarketcapitalization = ary[45];
        //市净率
        self.pbratio = ary[46];
        //涨停价
        self.highlimit = ary[47];
        //跌停价
        self.limit = ary[48];
    }
    return self;
}

//初始化
- (id)initWithStockEntity:(StockEntity *)stock {
    if (self = [super init]) {
        //名字
        self.name = stock.name;
        //代码
        self.code = stock.code;
        //当前价格
        self.currentprice = stock.currentprice;
        //昨收
        self.zsprice = stock.zsprice;
        //今开
        self.jkprice = stock.jkprice;
        //成交量（手）
        self.volume = stock.volume;
        //外盘
        self.odisc = stock.odisc;
        //内盘
        self.invol = stock.invol;
        //买1
        self.buy1 = stock.buy1;
        //买1量
        self.buy1num = stock.buy1num;
        //买2
        self.buy2 = stock.buy2;
        //买2量
        self.buy2num = stock.buy2num;
        //买3
        self.buy3 = stock.buy3;
        //买3量
        self.buy3num = stock.buy3num;
        //买4
        self.buy4 = stock.buy4;
        //买4量
        self.buy4num = stock.buy4num;
        //买5
        self.buy5 = stock.buy5;
        //买5量
        self.buy5num = stock.buy5num;
        //卖1
        self.sell1 = stock.sell1;
        //卖1量
        self.sell1num = stock.sell1num;
        //卖2
        self.sell2 = stock.sell2;
        //卖2量
        self.sell2num = stock.sell2num;
        //卖3
        self.sell3 = stock.sell3;
        //卖3量
        self.sell3num = stock.sell3num;
        //卖4
        self.sell4 = stock.sell4;
        //卖4量
        self.sell4num = stock.sell4num;
        //卖5
        self.sell5 = stock.sell5;
        //卖5量
        self.sell5num = stock.sell5num;
        //最近逐笔成交
        self.recentTransaction = stock.recentTransaction;
        //时间
        self.time = stock.time;
        //涨跌
        self.upsdowns = stock.upsdowns;
        //涨跌%
        self.pricefluctuation = stock.pricefluctuation;
        //最高
        self.highest = stock.highest;
        //最低
        self.lowest = stock.lowest;
        //价格/成交量（手）/成交额
        self.price = stock.price;
        //成交额（万）
        self.transactions = stock.transactions;
        //换手率
        self.turnoverrate = stock.turnoverrate;
        //市盈率
        self.peratios = stock.peratios;
        //振幅
        self.amplitude = stock.amplitude;
        //流通市值
        self.famc = stock.famc;
        //总市值
        self.totalmarketcapitalization = stock.totalmarketcapitalization;
        //市净率
        self.pbratio = stock.pbratio;
        //涨停价
        self.highlimit = stock.highlimit;
        //跌停价
        self.limit = stock.limit;
        //排序
        self.iD = stock.iD;
        //置顶
        self.isTop = stock.isTop;
    }
    return self;
}

@end
