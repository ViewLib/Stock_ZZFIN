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
-(BOOL)updateSotckEntitys:(NSDictionary *)stockDic
{
    StockEntity *entity = (StockEntity *)[NSEntityDescription insertNewObjectForEntityForName:@"StockEntity" inManagedObjectContext:self.mManagerContent];
    entity.name = stockDic[@"name"];
    entity.code = stockDic[@"code"];
    entity.price = stockDic[@"price"];
    entity.from = stockDic[@"from"];
    entity.risefall = stockDic[@"risefall"];
    
    return [self.mManagerContent save:nil];
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
