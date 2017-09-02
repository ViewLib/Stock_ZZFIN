//
//  StockEntity.h
//  Stock
//
//  Created by mac on 2017/9/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <CoreData/CoreData.h>

@interface StockEntity : NSManagedObject

//
@property (nullable, nonatomic, copy) NSString *name;
//
@property (nullable, nonatomic, copy) NSString *code;
//
@property (nullable, nonatomic, copy) NSString *from;
//
@property (nullable, nonatomic, copy) NSString *price;
//
@property (nullable, nonatomic, copy) NSString *risefall;

@end
