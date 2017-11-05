//
//  RACSignal+CCPRAC.h
//  Overseas
//
//  Created by Ceair on 17/4/12.
//  Copyright © 2017年 ceair. All rights reserved.
//

#import "RACSignal.h"

@interface RACSignal (CCPRAC)
/*
 * 多个输入框,同一条件 length > 0
 * tfs: 输入框数组
 * 返回一个信号
 */
+ (RACSignal *)rac_tfs:(NSArray<UITextField *> *)tfs;


@end
