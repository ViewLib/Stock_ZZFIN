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

//查询历史股票对象
-(NSArray *)queryHistoryStockEntitys;

//保存
-(BOOL)updateSotckEntitys:(NSArray *)stockDic;


@end
