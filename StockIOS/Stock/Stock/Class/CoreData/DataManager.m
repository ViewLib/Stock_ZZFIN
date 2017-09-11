//
//  DataManager.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "DataManager.h"
#import "AppDelegate.h"
#import "StockEntity.h"


@implementation DataManager

+(instancetype)shareDataMangaer
{
    static DataManager *Manager = nil;
    static dispatch_once_t predicate;
    dispatch_once(&predicate, ^{
        Manager = [[self alloc] init];
    });
    return Manager;
}

-(void)initCoredataManager
{
    UIApplication* app = [UIApplication sharedApplication] ;
    AppDelegate* appD = (AppDelegate*)app.delegate ;
    _mManagerContent = appD.managedObjectContext ;
}

- (NSManagedObjectContext *)mManagerContent {
    if (!_mManagerContent) {
        [self initCoredataManager];
    }
    return _mManagerContent;
}

//查询自选股票列表
-(NSArray *)queryStockEntitys
{
    NSArray *array = [self InfoSearchManagerWith:@"StockEntity" and:nil];
    return array;
}

//保存
-(BOOL)updateSotckEntitys:(NSArray *)stockValue
{
    StockEntity *entity;
    NSArray *entitys = [self queryStockWithName:stockValue[1]];
    if (entitys.count > 0) {
        entity = [entitys firstObject];
    } else {
        entity = (StockEntity *)[NSEntityDescription insertNewObjectForEntityForName:@"StockEntity" inManagedObjectContext:self.mManagerContent];
    }
    //名字
    entity.name = stockValue[1];
    //代码
    entity.code = stockValue[2];
    if (stockValue[0]) {
        NSArray *ary = [stockValue[0] componentsSeparatedByString:@"="];
        NSString *code = [ary firstObject];
        NSArray *ary2 = [code componentsSeparatedByString:@"_"];
        code = [ary2 lastObject];
        if ([code hasSuffix:stockValue[2]]) {
            entity.code = code;
        }
    }
    //当前价格
    entity.currentprice = stockValue[3];
    //昨收
    entity.zsprice = stockValue[4];
    //今开
    entity.jkprice = stockValue[5];
    //成交量（手）
    entity.volume = stockValue[6];
    //外盘
    entity.odisc = stockValue[7];
    //内盘
    entity.invol = stockValue[8];
    //买1
    entity.buy1 = stockValue[9];
    //买1量
    entity.buy1num = stockValue[10];
    //买2
    entity.buy2 = stockValue[11];
    //买2量
    entity.buy2num = stockValue[12];
    //买3
    entity.buy3 = stockValue[13];
    //买3量
    entity.buy3num = stockValue[14];
    //买4
    entity.buy4 = stockValue[15];
    //买4量
    entity.buy4num = stockValue[16];
    //买5
    entity.buy5 = stockValue[17];
    //买5量
    entity.buy5num = stockValue[18];
    //卖1
    entity.sell1 = stockValue[19];
    //卖1量
    entity.sell1num = stockValue[20];
    //卖2
    entity.sell2 = stockValue[21];
    //卖2量
    entity.sell2num = stockValue[22];
    //卖3
    entity.sell3 = stockValue[23];
    //卖3量
    entity.sell3num = stockValue[24];
    //卖4
    entity.sell4 = stockValue[25];
    //卖4量
    entity.sell4num = stockValue[26];
    //卖5
    entity.sell5 = stockValue[27];
    //卖5量
    entity.sell5num = stockValue[28];
    //最近逐笔成交
    entity.recentTransaction = stockValue[29];
    //时间
    entity.time = stockValue[30];
    //涨跌
    entity.upsdowns = stockValue[31];
    //涨跌%
    entity.pricefluctuation = stockValue[32];
    //最高
    entity.highest = stockValue[33];
    //最低
    entity.lowest= stockValue[34];
    //价格/成交量（手）/成交额
    entity.price = stockValue[35];
    //成交额（万）
    entity.transactions = stockValue[37];
    //换手率
    entity.turnoverrate = stockValue[38];
    //市盈率
    entity.peratios = stockValue[39];
    //振幅
    entity.amplitude = stockValue[43];
    //流通市值
    entity.famc = stockValue[44];
    //总市值
    entity.totalmarketcapitalization = stockValue[45];
    //市净率
    entity.pbratio = stockValue[46];
    //涨停价
    entity.highlimit = stockValue[47];
    //跌停价
    entity.limit = stockValue[48];
    
    return [self.mManagerContent save:nil];
}

- (NSArray *)queryStockWithName:(NSString *)name {
    NSPredicate *pre = [NSPredicate predicateWithFormat:@"name == %@",name];
    NSArray *array = [self InfoSearchManagerWith:@"StockEntity" and:pre];
    return array;
}

-(NSArray *)InfoSearchManagerWith:(NSString *)entityName and:(NSPredicate *)pred
{
    NSFetchRequest* fR = [[NSFetchRequest alloc] init] ;
    
    NSEntityDescription* desc= [NSEntityDescription entityForName:entityName inManagedObjectContext:self.mManagerContent];
    [fR setEntity:desc] ;
    if (pred!=nil) {
        [fR setPredicate:pred] ;
    }
    
    NSError *error;
    return [self.mManagerContent executeFetchRequest:fR error:&error] ;
}

@end
