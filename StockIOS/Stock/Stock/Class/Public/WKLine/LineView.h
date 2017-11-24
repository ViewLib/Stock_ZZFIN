//
//  LineView.h
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LineView : UIView<UIScrollViewDelegate>

- (id)initWithFrame:(CGRect)frame andViewData:(NSArray *)viewData;

- (void)reDrawWithLineModels;

- (void)reloadNewView:(NSArray *)news;

@end
