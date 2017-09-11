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

@property (nonatomic,assign) BOOL responseIsNotJson;

+ (HttpRequestClient *)sharedClient;

#pragma mark - 注册
- (void)registerWithPhone:(NSString *)phone request:(request)request;

#pragma mark - 个人信息补全
- (void)completionUserInformation:(NSDictionary *)userInfo request:(request)request;

#pragma mark - 获取股票信息
-(void)getStockInformation:(NSString *)stocks request:(request)request;

@end
