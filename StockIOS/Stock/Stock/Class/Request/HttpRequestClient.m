//
//  HttpRequestClient.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "HttpRequestClient.h"
#import "AFHTTPSessionManager.h"
#import "JSONKit.h"

@implementation HttpRequestClient

+ (HttpRequestClient *)sharedClient {
    static HttpRequestClient *_sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _sharedClient = [[HttpRequestClient alloc] init];
    });
    _sharedClient.responseIsNotJson = NO;
    return _sharedClient;
}

#pragma mark - 注册
- (void)registerWithPhone:(NSString *)phone request:(request)request {
    NSString *urlStr = [NSString stringWithFormat:@"%@/zzfin/api/register",SERVICE];
    NSDictionary *dic = @{@"moblie": phone,@"clientId":[Config shareInstance].uuid};
    [self httpGet:urlStr paramDict:dic completion:request];
}

#pragma mark - 个人信息补全
- (void)completionUserInformation:(NSDictionary *)userInfo request:(request)request {
    NSString *urlStr = [NSString stringWithFormat:@"%@/zzfin/api/completion",SERVICE];
    [self httpGet:urlStr paramDict:userInfo completion:request];
}

#pragma mark - 获取股票信息
-(void)getStockInformation:(NSString *)stocks request:(request)request{
    NSString *urlStr = [NSString stringWithFormat:@"http://qt.gtimg.cn/q=%@",stocks];
    self.responseIsNotJson = YES;
    [self httpGet:urlStr paramDict:nil completion:request];
}

#pragma mark - 获取热门
-(void)getHotStocksRequest:(request)request {
    NSString *urlStr = [NSString stringWithFormat:@"%@/zzfin/api/stock_hotsearch",SERVICE];
//    self.responseIsNotJson = YES;
    [self httpGet:urlStr paramDict:nil completion:request];
}

#pragma mark - 获取top10
-(void)getRankListStocksRequest:(request)request {
    NSString *urlStr = [NSString stringWithFormat:@"%@/zzfin/api/stock_ranklist",SERVICE];
    [self httpGet:urlStr paramDict:nil completion:request];
}

#pragma mark - 获取排行
-(void)getRankDetail:(NSDictionary *)value request:(request)request {
    NSString *urlStr = [NSString stringWithFormat:@"%@/zzfin/api/stock_rankdetail",SERVICE];
    NSDictionary *dic = @{@"data": [value JSONString]};
    [self httpPost:urlStr paramDict:dic completion:request];
}

#pragma mark - 提交get请求
-(void) httpGet:(NSString *) urlstring paramDict:(NSDictionary *)paramDict completion:(request )completion {
    [self httpBossWith:urlstring type:@"get" paramDict:paramDict completion:completion];
}

#pragma mark - 提交Post请求
-(void) httpPost:(NSString *)urlstring paramDict:(NSDictionary *)paramDict completion:(request )completion {
    [self httpBossWith:urlstring type:@"post" paramDict:paramDict completion:completion];
}

#pragma mark - 提交公用方法
- (void)httpBossWith:(NSString *)url type:(NSString *)type paramDict:(NSDictionary *)paramDict completion:(request)completion {
    
    url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager.requestSerializer setHTTPShouldHandleCookies:YES];

    manager.requestSerializer.timeoutInterval = 30.f;
    
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"text/plain",@"text/html",@"application/x-javascript",@"application/json", nil];
    
    if (self.responseIsNotJson) {
        manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    }
    
    if ([type isEqual:@"post"]) {
        [manager POST:url parameters:paramDict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            completion(@"",responseObject,nil);
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            completion(@"", nil, [self getErrorMessage:error]);
        }];
    } else if ([type isEqual:@"get"]) {
        [manager GET:url parameters:paramDict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            completion(@"",responseObject,nil);
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            completion(@"", nil, [self getErrorMessage:error]);
        }];
    }
}

- (NSString *)getErrorMessage:(NSError *)error {
    NSDictionary *dic = [error userInfo];
    NSError *dicerror = dic[@"NSUnderlyingError"];
    dic = [dicerror userInfo];
    NSData *data = dic[AFNetworkingOperationFailingURLResponseDataErrorKey];
    NSString *errorString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    if (errorString.length == 0) {
        errorString = @"error";
    }
    return errorString;
}

@end
