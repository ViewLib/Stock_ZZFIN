//
//  loginEntity.m
//  Stock
//
//  Created by mac on 2017/9/10.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "loginEntity.h"

@implementation loginEntity

- (id)initWithDictionary:(NSDictionary *)dic {
    self = [super init];
    if (self) {
        self.age = dic[@"mAge"];
        self.createTime = dic[@"mCreateTime"];
        self.moblie = dic[@"mMoblie"];
        self.userId = dic[@"mUserId"];
    }
    return self;
}

@end
