//
//  GSJJCollectionViewCell.m
//  Stock
//
//  Created by mac on 2017/10/22.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "GSJJCollectionViewCell.h"

@implementation GSJJCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self = [[NSBundle mainBundle]loadNibNamed:@"GSJJCollectionViewCell" owner:self options:nil].lastObject;
    }
    return self;
}



@end
