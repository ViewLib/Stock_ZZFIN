//
//  WKMaskView.m
//  Stock
//
//  Created by mac on 2017/11/3.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "WKMaskView.h"
#import <Masonry/Masonry.h>

@implementation WKMaskView

- (id)initWithFrame:(CGRect)frame Dic:(NSDictionary *)dic {
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor colorWithWhite:0 alpha:.4];
        
        UIButton *cancel = [UIButton buttonWithType:UIButtonTypeCustom];
        [cancel setFrame:CGRectMake(0, 0, 80, 80)];
        [cancel setCenter:CGPointMake(frame.size.width-50, 30)];
        [cancel setImage:[UIImage imageNamed:@"icon_close"] forState:UIControlStateNormal];
        [cancel addTarget:self action:@selector(clickCancelBtn) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:cancel];
        
        UIView *centerView = [UIView new];
        [centerView setBackgroundColor:[UIColor whiteColor]];
        [self addSubview:centerView];
        [centerView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.equalTo(self);
            make.size.mas_equalTo(CGSizeMake(270, 70));
        }];
        
        UIImage *question = [UIImage imageNamed:@"icon_problem"];
        UIImageView *quesimg = [UIImageView new];
        [quesimg setImage:question];
        [centerView addSubview:quesimg];
        [quesimg mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.top.equalTo(centerView).offset(10);
        }];
        
        float labelWidth = CGRectGetWidth(frame)-question.size.width-20;
        NSString *titleStr = dic[@"eventTitle"]?dic[@"eventTitle"]:@"";
        
        NSDictionary *attribute = @{NSFontAttributeName:[UIFont systemFontOfSize:12]};
        CGSize textSize = [self rectOfNSString:titleStr width:labelWidth attribute:attribute].size;
        UILabel *title = [UILabel new];
        [title setText:titleStr];
        [title setFont:[UIFont systemFontOfSize:12]];
        [title setNumberOfLines:0];
        [centerView addSubview:title];
        [title mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(quesimg.mas_top);
            make.left.equalTo(quesimg.mas_right).offset(10);
            make.size.mas_equalTo(CGSizeMake(labelWidth, textSize.height));
        }];
        
        UIImage *answer = [UIImage imageNamed:@"icon_answer"];
        UIImageView *ansimg = [UIImageView new];
        [ansimg setImage:answer];
        [centerView addSubview:ansimg];
        [ansimg mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(centerView).offset(10);
            make.top.equalTo(title.mas_bottom).offset(10);
        }];
        
        NSString *valueStr = dic[@"eventDesc"]?dic[@"eventDesc"]:@"";
        CGSize valueSize = [self rectOfNSString:valueStr width:labelWidth attribute:attribute].size;
        UILabel *value = [UILabel new];
        [value setText:valueStr];
        [value setFont:[UIFont systemFontOfSize:12]];
        [value setNumberOfLines:0];
        [centerView addSubview:value];
        [value mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.equalTo(title.mas_bottom).offset(10);
            make.left.equalTo(ansimg.mas_right).offset(10);
            make.size.mas_equalTo(CGSizeMake(labelWidth, valueSize.height));
        }];
        
        [centerView mas_updateConstraints:^(MASConstraintMaker *make) {
            make.bottom.equalTo(value.mas_bottom).offset(10);
        }];
    }
    return self;
}

- (CGRect)rectOfNSString:(NSString *)string width:(float)width attribute:(NSDictionary *)attribute {
    CGRect rect = [string boundingRectWithSize:CGSizeMake(width, 1000)
                                       options:NSStringDrawingTruncatesLastVisibleLine |NSStringDrawingUsesLineFragmentOrigin |
                   NSStringDrawingUsesFontLeading
                                    attributes:attribute
                                       context:nil];
    return rect;
}

- (void)clickCancelBtn {
    [self removeFromSuperview];
}

@end
