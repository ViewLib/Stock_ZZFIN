//
//  NSArray+CCPArray.h
//  Overseas
//
//  Created by Ceair on 17/3/29.
//  Copyright © 2017年 xiesy. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef id _Nullable CCPArrayType;
@interface NSArray (CCPArray)

- (NSArray * _Nonnull )map:(CCPArrayType (^ _Nonnull )(CCPArrayType value))block;
- (NSArray * _Nonnull)fliter:(BOOL (^ _Nonnull)(CCPArrayType value))block;

/*
 * 创建文件名为 name 的plist文件
 * 将数组存储到 name文件中
 */
- (void)writeTo:(NSString *_Nonnull)name;
/*
 * 去除数组中重复对象
 */
- (NSArray * _Nonnull)removeDuplicate;
/*
 * 根据条件去重
 */
- (NSArray *_Nonnull)removeDuplicate:(BOOL(^_Nonnull)(CCPArrayType obj))condition;


@end
