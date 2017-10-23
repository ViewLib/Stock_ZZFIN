//
//  GDCollectionViewCell.m
//  Stock
//
//  Created by mac on 2017/10/23.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "GDCollectionViewCell.h"

@implementation GDCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self = [[NSBundle mainBundle]loadNibNamed:@"GDCollectionViewCell" owner:self options:nil].lastObject;
    }
    return self;
}

//内容样式
- (void)valueType {
    for (UILabel *lab in @[self.name,self.num,self.proportion]) {
        lab.font = [UIFont systemFontOfSize:12];
        lab.textColor = [Utils colorFromHexRGB:@"484848"];
    }
}

@end
