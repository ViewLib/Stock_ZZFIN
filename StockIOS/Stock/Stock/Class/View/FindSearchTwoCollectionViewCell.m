//
//  FindSearchTwoCollectionViewCell.m
//  Stock
//
//  Created by mac on 2017/10/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "FindSearchTwoCollectionViewCell.h"

@implementation FindSearchTwoCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self = [[NSBundle mainBundle]loadNibNamed:@"FindSearchTwoCollectionViewCell" owner:self options:nil].lastObject;
    }
    return self;
}

@end
