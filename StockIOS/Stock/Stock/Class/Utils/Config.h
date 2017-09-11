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

@property (nonatomic ,strong)   NSArray       *localStocks;

@property (nonatomic ,strong)   loginEntity   *login;

+(Config *) shareInstance;


@end
