//
//  Config.h
//  Stock
//
//  Created by mac on 2017/9/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "loginEntity.h"

@interface Config : NSObject

@property (nonatomic ,assign)   BOOL    islogin;

@property (nonatomic ,strong)   NSString      *uuid;

//本地固化的股票数据
@property (nonatomic ,strong)   NSArray       *localStocks;

//用户对象
@property (nonatomic ,strong)   loginEntity   *login;

//用户自选股
@property (nonatomic ,strong)   NSArray       *optionalStocks;

//热门推荐股票列表
@property (nonatomic ,strong)   NSArray       *hotStocks;

//热门推荐的高度
@property (nonatomic ,assign)   float         defaultHotHigh;

//top10列表
@property (nonatomic ,strong)   NSArray       *top10List;

+(Config *) shareInstance;


@end
