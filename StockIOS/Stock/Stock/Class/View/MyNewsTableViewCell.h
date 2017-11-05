//
//  MyNewsTableViewCell.h
//  Stock
//
//  Created by mac on 2017/11/5.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyNewsTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *iconImg;
@property (weak, nonatomic) IBOutlet UILabel *cellName;
@property (weak, nonatomic) IBOutlet UILabel *cellValue;
@property (weak, nonatomic) IBOutlet UILabel *date;
@end
