//
//  Config.m
//  Stock
//
//  Created by mac on 2017/9/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "Config.h"

static Config *shareinstance;

@implementation Config

+(Config *) shareInstance
{
    if (!shareinstance) {
        
        shareinstance = [[Config alloc] init];
    }
    return shareinstance;
}

-(NSMutableDictionary *)stockDic {
    if (!_stockDic) {
        _stockDic = [NSMutableDictionary dictionary];
    }
    return _stockDic;
}

@end
