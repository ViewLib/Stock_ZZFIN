//
//  DataManager.h
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "StockEntity.h"
#import "HistoryStockEntity.h"

@interface DataManager : NSObject

@property (retain,nonatomic) NSManagedObjectContext* mManagerContent;

//单例
+(instancetype)shareDataMangaer;

//查询自选股票对象
-(NSArray *)queryStockEntitys;

//保存历史股票
-(BOOL)insertHistoryStock:(NSDictionary *)dic;

//删除历史股票
- (BOOL)removeHistoryStock;

//查询历史股票对象
-(NSArray *)queryHistoryStockEntitys;

//保存
-(BOOL)updateSotckEntitys:(NSArray *)stockDic;

//获取stockEntity对象
-(StockEntity *)getStockWithAry:(NSArray *)ary;

//保存stockEntity对象
-(BOOL)updateSotckEntity:(StockEntity *)stock;

//根据stockCode获取stockEntity对象
-(StockEntity *)getStockEntityWithStockCode:(NSString *)stockCode;

//删除自选股票
- (BOOL)removeStockEntity:(NSString *)stockCode;
@end
