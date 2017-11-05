//
//  NSArray+CCPArray.m
//  Overseas
//
//  Created by Ceair on 17/3/29.
//  Copyright © 2017年 xiesy. All rights reserved.
//

#import "NSArray+CCPArray.h"

@implementation NSArray (CCPArray)

//map 映射
- ( NSArray * _Nonnull )map:(CCPArrayType (^ _Nonnull )(CCPArrayType value))block {
    RACSequence *sq = [self.rac_sequence map:block];
    return sq.array;
}

//
- (NSArray * _Nonnull)fliter:(BOOL (^ _Nonnull)(CCPArrayType value))block {
    RACSequence *sq = [self.rac_sequence filter:block];
    return sq.array;
}

/*
 * 创建文件名为 name 的plist文件
 * 将数组存储到 name文件中
 */
- (void)writeTo:(NSString *_Nonnull)name {
    NSFileManager *fm = [NSFileManager defaultManager];
    NSString *baseP = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES).firstObject;
    NSString *fileP = [baseP stringByAppendingString:[NSString stringWithFormat:@"%@.plist",name]];
    if (![fm fileExistsAtPath:fileP]) {
        [fm createFileAtPath:fileP contents:nil attributes:nil];
    }
    [self writeToFile:fileP atomically:YES];
}

/*
 * 去除数组中重复对象
 */
- (NSArray * _Nonnull)removeDuplicate {
    return [self valueForKeyPath:@"@distinctUnionOfObjects.self"];
}

/*
 * 根据条件去重
 */
- (NSArray *_Nonnull)removeDuplicate:(BOOL(^)(id obj))condition {
    NSMutableArray *marr = [NSMutableArray array];
    [self enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        BOOL cd = condition(obj);
        if (cd) {
            [marr addObject:obj];
        }
    }];
    return marr.copy;
}

@end
