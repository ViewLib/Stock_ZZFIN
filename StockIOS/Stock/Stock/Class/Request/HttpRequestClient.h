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

@end
