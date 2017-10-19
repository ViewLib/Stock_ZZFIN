//
//  QAView.h
//  Stock
//
//  Created by mac on 2017/10/19.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QAView : UIView


@property (nonatomic, assign)   float   viewHigh;
/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateView:(NSDictionary *)dic;

@end
