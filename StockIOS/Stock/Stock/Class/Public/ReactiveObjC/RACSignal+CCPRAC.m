//
//  RACSignal+CCPRAC.m
//  Overseas
//
//  Created by Ceair on 17/4/12.
//  Copyright © 2017年 ceair. All rights reserved.
//

#import "RACSignal+CCPRAC.h"

@implementation RACSignal (CCPRAC)

/*
 * 多个输入框,同一条件 length > 0
 * tfs: 输入框数组
 * 返回一个信号
 */
+ (RACSignal *)rac_tfs:(NSArray<UITextField *> *)tfs {
   return  [[RACSignal combineLatest:[tfs map:^CCPArrayType(UITextField *value) {
        return value.rac_textSignal;
    }]] map:^id _Nullable(RACTuple *value) {
        for (NSString *str in value.rac_sequence.array) {
            if (str.length == 0) {
                return @(NO);
            }
        }
        return @(YES);
    }];
}




@end
