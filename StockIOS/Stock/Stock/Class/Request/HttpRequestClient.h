//
//  HttpRequestClient.h
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^request)(NSString *resultMsg,id dataDict,id error);

@interface HttpRequestClient : NSObject

+ (HttpRequestClient *)sharedClient;

#pragma mark - 获取股票信息
-(void)getStockInformation:(NSString *)stocks request:(request)request;

@end
