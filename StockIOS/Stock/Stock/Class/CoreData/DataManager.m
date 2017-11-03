//
//  DataManager.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "DataManager.h"
#import "AppDelegate.h"


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

//查询历史股票对象
-(NSArray *)queryHistoryStockEntitys {
    NSArray *array = [self InfoSearchManagerWith:@"HistoryStockEntity" and:nil];
    return array;
}

//保存历史股票
-(BOOL)insertHistoryStock:(NSDictionary *)dic {
    NSMutableArray *array = [self queryHistoryStockEntitys].mutableCopy;
    BOOL isHas = false;
    for (HistoryStockEntity *ent in array) {
        if ([ent.code isEqual:dic[@"code"]]) {
            isHas = YES;
        }
    }
    if (!isHas) {
        if (array.count == 10) {
            HistoryStockEntity *entity = [array firstObject];
            [self.mManagerContent deleteObject:entity];
        }
        
        HistoryStockEntity *entity = (HistoryStockEntity*)[NSEntityDescription insertNewObjectForEntityForName:@"HistoryStockEntity" inManagedObjectContext:self.mManagerContent];
        entity.name = dic[@"title"];
        entity.code = dic[@"code"];
        
        return [self.mManagerContent save:nil];
    } else {
        return NO;
    }
}

//删除历史股票
- (BOOL)removeHistoryStock {
    NSArray *array = [self InfoSearchManagerWith:@"HistoryStockEntity" and:nil];
    [array enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [self.mManagerContent deleteObject:obj];
    }];
    return [self.mManagerContent save:nil];
}

//删除自选股票
- (BOOL)removeStockEntity:(NSString *)stockCode {
    NSArray *array = [self queryStockWithCode:stockCode];
    [array enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [self.mManagerContent deleteObject:obj];
    }];
    return [self.mManagerContent save:nil];
}

//查询自选股票列表
-(NSArray *)queryStockEntitys {
    NSArray *array = [self InfoSearchManagerWith:@"StockEntity" and:nil];
    return array;
}

//保存
-(BOOL)updateSotckEntitys:(NSArray *)stockValue {
    StockEntity *entity;
    NSArray *entitys = [self queryStockWithName:stockValue[1]];
    if (entitys.count > 0) {
        entity = [entitys firstObject];
    } else {
        entity = (StockEntity *)[NSEntityDescription insertNewObjectForEntityForName:@"StockEntity" inManagedObjectContext:self.mManagerContent];
    }
    entity = [self getStockWithAry:stockValue];
    return [self.mManagerContent save:nil];
}

//获取stockEntity对象
-(StockEntity *)getStockWithAry:(NSArray *)ary {
    StockEntity *entity = (StockEntity *)[NSEntityDescription insertNewObjectForEntityForName:@"StockEntity" inManagedObjectContext:self.mManagerContent];
    //名字
    entity.name = ary[1];
    //代码
    entity.code = ary[2];
    if (ary[0]) {
        NSArray *arys = [ary[0] componentsSeparatedByString:@"="];
        NSString *code = [arys firstObject];
        NSArray *ary2 = [code componentsSeparatedByString:@"_"];
        code = [ary2 lastObject];
        if ([code hasSuffix:ary[2]]) {
            entity.code = code;
        }
    }
    //当前价格
    entity.currentprice = ary[3];
    //昨收
    entity.zsprice = ary[4];
    //今开
    entity.jkprice = ary[5];
    //成交量（手）
    entity.volume = ary[6];
    //外盘
    entity.odisc = ary[7];
    //内盘
    entity.invol = ary[8];
    //买1
    entity.buy1 = ary[9];
    //买1量
    entity.buy1num = ary[10];
    //买2
    entity.buy2 = ary[11];
    //买2量
    entity.buy2num = ary[12];
    //买3
    entity.buy3 = ary[13];
    //买3量
    entity.buy3num = ary[14];
    //买4
    entity.buy4 = ary[15];
    //买4量
    entity.buy4num = ary[16];
    //买5
    entity.buy5 = ary[17];
    //买5量
    entity.buy5num = ary[18];
    //卖1
    entity.sell1 = ary[19];
    //卖1量
    entity.sell1num = ary[20];
    //卖2
    entity.sell2 = ary[21];
    //卖2量
    entity.sell2num = ary[22];
    //卖3
    entity.sell3 = ary[23];
    //卖3量
    entity.sell3num = ary[24];
    //卖4
    entity.sell4 = ary[25];
    //卖4量
    entity.sell4num = ary[26];
    //卖5
    entity.sell5 = ary[27];
    //卖5量
    entity.sell5num = ary[28];
    //最近逐笔成交
    entity.recentTransaction = ary[29];
    //时间
    entity.time = ary[30];
    //涨跌
    entity.upsdowns = ary[31];
    //涨跌%
    entity.pricefluctuation = ary[32];
    //最高
    entity.highest = ary[33];
    //最低
    entity.lowest= ary[34];
    //价格/成交量（手）/成交额
    entity.price = ary[35];
    //成交额（万）
    entity.transactions = ary[37];
    //换手率
    entity.turnoverrate = ary[38];
    //市盈率
    entity.peratios = ary[39];
    //振幅
    entity.amplitude = ary[43];
    //流通市值
    entity.famc = ary[44];
    //总市值
    entity.totalmarketcapitalization = ary[45];
    //市净率
    entity.pbratio = ary[46];
    //涨停价
    entity.highlimit = ary[47];
    //跌停价
    entity.limit = ary[48];
    
    return entity;
}

//保存stockEntity对象
-(BOOL)updateSotckEntity:(StockEntity *)stock {
    StockEntity *entity;
    NSArray *entitys = [self queryStockWithName:stock.name];
    if (entitys.count > 0) {
        entity = [entitys firstObject];
    } else {
        entity = (StockEntity *)[NSEntityDescription insertNewObjectForEntityForName:@"StockEntity" inManagedObjectContext:self.mManagerContent];
    }
    entity = stock;
    return [self.mManagerContent save:nil];
}

//通过stockCode获取stockEntity对象
-(StockEntity *)getStockEntityWithStockCode:(NSString *)stockCode {
    NSArray *entitys = [self queryStockWithCode:stockCode];
    return [entitys firstObject];
}

//通过名字查询自选股
- (NSArray *)queryStockWithName:(NSString *)name {
    NSPredicate *pre = [NSPredicate predicateWithFormat:@"name == %@",name];
    NSArray *array = [self InfoSearchManagerWith:@"StockEntity" and:pre];
    return array;
}

//通过代码查询自选股
- (NSArray *)queryStockWithCode:(NSString *)code {
    NSPredicate *pre = [NSPredicate predicateWithFormat:@"code == %@",code];
    NSArray *array = [self InfoSearchManagerWith:@"StockEntity" and:pre];
    return array;
}

-(NSArray *)InfoSearchManagerWith:(NSString *)entityName and:(NSPredicate *)pred {
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
