//
//  DataManager.h
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DataManager : NSObject

@property (retain,nonatomic) NSManagedObjectContext* mManagerContent;

//单例
+(instancetype)shareDataMangaer;

//查询自选股票对象
-(NSArray *)queryStockEntitys;

//保存
-(BOOL)updateSotckEntitys:(NSArray *)stockDic;


@end
