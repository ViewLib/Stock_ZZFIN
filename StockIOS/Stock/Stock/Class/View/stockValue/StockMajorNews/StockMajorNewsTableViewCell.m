//
//  StockMajorNewsTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockMajorNewsTableViewCell.h"
#import "QAView.h"

@implementation StockMajorNewsTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    [_lookMoreBtn ImgRightTextLeft];
    self.valueViewHigh.constant = 0;
    [self addQAView:@[@{@"": @""},@{@"": @""},@{@"": @""}]];
}

- (void)updateCell:(NSDictionary *)dic {
    
}

- (void)addQAView:(NSArray *) array {
    float viewY = 0;
    for (NSDictionary *dic in array) {
        QAView *qa = [[NSBundle mainBundle] loadNibNamed:@"QAView" owner:nil options:nil].firstObject;
        [qa updateView:dic];
        qa.frame = CGRectMake(0, viewY, K_FRAME_BASE_WIDTH-24, qa.viewHigh);
        viewY += qa.viewHigh + 10;
        [self.valueView addSubview:qa];
    }
    self.valueViewHigh.constant = viewY;
}

- (void)layoutSubviews {
//    CGRect newViewFrame = self.frame;
//    newViewFrame.size.height = self.valueView.frame.size.height + 150;
//    self.frame = newViewFrame;
    
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
