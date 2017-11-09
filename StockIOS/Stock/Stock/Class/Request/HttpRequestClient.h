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

#pragma mark - 获取热门
-(void)getHotStocksRequest:(request)request;

#pragma mark - 获取top10
-(void)getRankListStocksRequest:(request)request;

#pragma mark - 获取筛选信息
-(void)getStockRankfilter:(NSDictionary *)value request:(request)request;

#pragma mark - 获取排行
-(void)getRankDetail:(NSDictionary *)value request:(request)request;

#pragma mark - 获取筛选后的排行
-(void)getFilterSearch:(NSDictionary *)value request:(request)request;

#pragma mark - 获取分时数据
-(void)getLineData:(NSDictionary *)value request:(request)request;

#pragma mark - 获取日线数据
-(void)getKLineData:(NSDictionary *)value request:(request)request;

#pragma mark - 获取公司简介
-(void)getComputerInfo:(NSDictionary *)value request:(request)request;

#pragma mark - 财务信息
-(void)getStockFinicial:(NSDictionary *)value request:(request)request;

#pragma mark - 获取股东信息
-(void)getShareholder:(NSDictionary *)value request:(request)request;

#pragma mark - 获取重大事件
-(void)getStockEvent:(NSDictionary *)value request:(request)request;

#pragma mark - 获取券商信息
-(void)getStockgrade:(NSDictionary *)value request:(request)request;

#pragma mark - 横向比较
- (void)getStockCompare:(NSDictionary *)value request:(request)request;

/*****************************下面是模拟数据获取方法***************************/

/**
 Get请求调用
 @param url     url
 @param params  请求参数
 @param success 成功回调
 @param fail    失败回调
 */
+ (void)Get:(NSString*) url params:(id)params success:(void (^)(NSDictionary *response))success fail:(void(^)(NSDictionary *info))fail;

@end
