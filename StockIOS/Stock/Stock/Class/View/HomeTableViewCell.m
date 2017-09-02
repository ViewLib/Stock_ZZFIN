//
//  HomeTableViewCell.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "HomeTableViewCell.h"

@implementation HomeTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _baseWidth.constant = K_FRAME_BASE_WIDTH;
}

- (void)updateCell:(StockEntity *)entity {
    self.name.text = entity.name;
    self.code.text = entity.code;
    self.price.text = entity.price;
    
    NSString *PN = @"+";
    _percentage = [entity.risefall floatValue]/100;
    _upValue.text = [NSString stringWithFormat:@" %.1f%@",_percentage*100,@"%"];
    UIColor *color = UP_COLOR;
    
    if ([entity.risefall hasPrefix:@"-"]) {
        PN = @"-";
        _percentage = [[entity.risefall stringByReplacingOccurrencesOfString:@"-" withString:@""] floatValue]/100;
        _downValue.text = [NSString stringWithFormat:@"%.1f%@  ",_percentage*100,@"%"];
        color = DOWN_COLOR;
    }
    
    if (_percentage == 0) {
        PN = @"";
    }
    [self reloadright];

    self.pView = [[UIView alloc] init];
    self.pView.backgroundColor = color;
    
    if ([PN isEqual:@"+"]) {
        self.upView.hidden = NO;
        CGRect rect = [self.upValue.text boundingRectWithSize:CGSizeMake(1000, 25) options:NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:15]} context:nil];
        if (rect.size.width < _percentage * [self viewx]) {
            self.upValue.textColor = [UIColor whiteColor];
        }
    } else if ([PN isEqual:@"-"]) {
        self.downView.hidden = NO;
        CGRect rect = [self.downValue.text boundingRectWithSize:CGSizeMake(1000, 25) options:NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:15]} context:nil];
        if (rect.size.width < _percentage * [self viewx]) {
            self.downValue.textColor = [UIColor whiteColor];
        }
    } else {
        self.stopLabel.hidden = NO;
    }
}

- (float)viewx {
    _baseWidth.constant = K_FRAME_BASE_WIDTH;
    float rightViewWidth = _baseWidth.constant/2 + 40;
    float viewX = rightViewWidth/2;
    return viewX;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    float viewX = [self viewx];
    
    if (!self.upView.hidden) {
        self.pView.frame = CGRectMake(viewX, 0, _percentage * viewX, 25);
        [self.upView addSubview:self.pView];
        [self.upView bringSubviewToFront:self.upValue];
    } else {
        self.pView.frame = CGRectMake((1-_percentage) * viewX, 0, _percentage * viewX, 25);
        [self.downView addSubview:self.pView];
        [self.downView bringSubviewToFront:self.downValue];
    }
}

- (void)reloadright {
    self.upView.hidden = YES;
    self.downView.hidden = YES;
    self.stopLabel.hidden = YES;
}

@end
