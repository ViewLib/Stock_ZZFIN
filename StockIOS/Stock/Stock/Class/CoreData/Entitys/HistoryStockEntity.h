//
//  HistoryStockEntity.h
//  Stock
//
//  Created by mac on 2017/9/22.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <CoreData/CoreData.h>

@interface HistoryStockEntity: NSManagedObject

//名字
@property (nullable, nonatomic, copy) NSString *name;

//代码
@property (nullable, nonatomic, copy) NSString *code;

//当前价格
@property (nullable, nonatomic, copy) NSString *type;

@end
