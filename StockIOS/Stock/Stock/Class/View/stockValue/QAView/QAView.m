//
//  QAView.m
//  Stock
//
//  Created by mac on 2017/10/19.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "QAView.h"

@implementation QAView

- (void)awakeFromNib {
    [super awakeFromNib];
    
}

- (void)updateView:(NSDictionary *)dic {
    NSArray *dicValue = dic[@"stockEventsDataModels"];
    NSDictionary *newModel = [dicValue firstObject];
    self.question.text = newModel[@"eventTitle"];
    self.answer.text = newModel[@"eventDesc"];
    
    float width = K_FRAME_BASE_WIDTH/2+65;
    
    CGRect rect = [self.question.text boundingRectWithSize:CGSizeMake(width, 0)
                                       options:NSStringDrawingTruncatesLastVisibleLine |NSStringDrawingUsesLineFragmentOrigin |
                   NSStringDrawingUsesFontLeading
                                    attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12]}
                                       context:nil];
    
    CGRect size = [self.answer.text boundingRectWithSize:CGSizeMake(width, 0)
                                                 options:NSStringDrawingTruncatesLastVisibleLine |NSStringDrawingUsesLineFragmentOrigin |
                   NSStringDrawingUsesFontLeading
                                              attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12]}
                                                 context:nil];
    
    float high = rect.size.height;
    if (high < size.size.height) {
        high = size.size.height;
    }
    
    self.viewHigh = high+10;
    
}

- (void)layoutSubviews {
    [super layoutSubviews];
    [self.contentView.layer setBorderWidth:1];
    [self.contentView.layer setBorderColor:[Utils colorFromHexRGB:@"C0C0C0"].CGColor];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
