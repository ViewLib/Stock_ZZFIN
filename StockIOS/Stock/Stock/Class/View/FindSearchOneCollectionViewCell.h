//
//  FindSearchOneCollectionViewCell.h
//  Stock
//
//  Created by mac on 2017/10/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FindSearchOneCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *selectedImg;
@property (weak, nonatomic) IBOutlet UILabel *value;
@property (assign, nonatomic) BOOL hasChange;

- (void)isSelect;
- (void)reloadSelect;
- (void)isChange;

@end
