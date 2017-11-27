//
//  hotStockView.m
//  Stock
//
//  Created by mac on 2017/9/23.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "hotStockView.h"

@implementation hotStockView

-(void)awakeFromNib {
    [super awakeFromNib];

    self.layer.borderWidth = 0.3f;
    self.layer.borderColor = MAIN_COLOR.CGColor;
    self.layer.cornerRadius = self.bounds.size.height/2;
    
    self.backgroundColor = [MAIN_COLOR colorWithAlphaComponent:0.1];
    
    self.value = [[UILabel alloc] init];
    [self.value setFont:[UIFont systemFontOfSize:12.0f]];
    [self.value setTextColor:[Utils colorFromHexRGB:@"090909"]];
    [self.value setTextAlignment:NSTextAlignmentCenter];
    [self.value setText:@"内容"];
    [self addSubview:self.value];
    
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    [btn setFrame:self.bounds];
    [btn addTarget:self action:@selector(clickView) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:btn];
}

- (void)clickView {
    if (self.clickBlock) {
        self.clickBlock(self);
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];
    [self.value setFrame:CGRectMake(3, 0, self.frame.size.width-6, 25)];
    CGRect newframe = self.frame;
    newframe.size.height = 25;
    self.frame = newframe;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
