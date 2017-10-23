//
//  GDCollectionViewCell.h
//  Stock
//
//  Created by mac on 2017/10/23.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GDCollectionViewCell : UICollectionViewCell

@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *num;
@property (weak, nonatomic) IBOutlet UILabel *proportion;

//内容样式
- (void)valueType;

@end
