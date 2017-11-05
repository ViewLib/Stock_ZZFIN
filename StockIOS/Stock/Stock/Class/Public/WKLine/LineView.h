//
//  LineView.h
//  LineChart
//
//  Created by mac on 2017/11/1.
//  Copyright © 2017年 mac. All rights reserved.
//

#import <UIKit/UIKit.h>

//两个点之间的间距
#define LINEGAP    5

//每一个价格点的宽度
#define LINE       1

//线图占比
#define LINERADIO       0.8

//线图占比
#define BUTTOMRADIO       0.2

@interface LineView : UIView<UIScrollViewDelegate>

- (id)initWithFrame:(CGRect)frame andViewData:(NSArray *)viewData;

- (void)reloadNewView:(NSArray *)news;

@end
