//
//  loginEntity.h
//  Stock
//
//  Created by mac on 2017/9/10.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface loginEntity : NSObject

@property (nonatomic, strong)   NSString    *age;

@property (nonatomic, strong)   NSString    *createTime;

@property (nonatomic, strong)   NSString    *moblie;

@property (nonatomic, strong)   NSString    *userId;

- (id)initWithDictionary:(NSDictionary *)dic;

@end
