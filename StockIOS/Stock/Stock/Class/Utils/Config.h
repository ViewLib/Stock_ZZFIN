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

@property (nonatomic ,strong)   loginEntity   *login;

//用户自选股
@property (nonatomic ,strong)   NSArray       *optionalStocks;

+(Config *) shareInstance;


@end
