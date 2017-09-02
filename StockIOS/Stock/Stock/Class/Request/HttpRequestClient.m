//
//  HttpRequestClient.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "HttpRequestClient.h"
#import "AFHTTPSessionManager.h"

@implementation HttpRequestClient

+ (HttpRequestClient *)sharedClient {
    static HttpRequestClient *_sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _sharedClient = [[HttpRequestClient alloc] init];
    });
    return _sharedClient;
}

#pragma mark - 获取股票信息
-(void)getStockInformation:(NSString *)stock request:(request)request{
    NSString *urlStr = [NSString stringWithFormat:@"http://qt.gtimg.cn/q=%@",stock];
    [self httpGet:urlStr paramDict:nil completion:request];
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
    
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"text/plain",@"text/html",@"application/json", nil];
    
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
