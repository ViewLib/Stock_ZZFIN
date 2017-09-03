//
//  Config.h
//  Stock
//
//  Created by mac on 2017/9/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Config : NSObject

@property (nonatomic ,assign)   BOOL    islogin;

+(Config *) shareInstance;


@end
