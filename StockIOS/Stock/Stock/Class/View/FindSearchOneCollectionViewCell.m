//
//  FindSearchOneCollectionViewCell.m
//  Stock
//
//  Created by mac on 2017/10/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "FindSearchOneCollectionViewCell.h"

@implementation FindSearchOneCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self = [[NSBundle mainBundle]loadNibNamed:@"FindSearchOneCollectionViewCell" owner:self options:nil].lastObject;
    }
    return self;
}

- (void)isSelect {
    self.selectedImg.hidden = NO;
    self.value.textColor = [Utils colorFromHexRGB:@"186DB7"];
}

- (void)isChange {
    self.value.textColor = [Utils colorFromHexRGB:@"186DB7"];
    self.hasChange = YES;
}

- (void)reloadSelect {
    self.hasChange = NO;
    self.selectedImg.hidden = YES;
    self.value.textColor = [UIColor blackColor];
}

@end
