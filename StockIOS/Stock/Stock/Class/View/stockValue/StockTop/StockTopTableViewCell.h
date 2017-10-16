//
//  StockTopTableViewCell.h
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StockTopTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *stockLast;
@property (weak, nonatomic) IBOutlet UILabel *Change;
@property (weak, nonatomic) IBOutlet UILabel *Chg;
@property (weak, nonatomic) IBOutlet UIButton *isSelect;
@property (weak, nonatomic) IBOutlet UILabel *highestOf52;
@property (weak, nonatomic) IBOutlet UILabel *lowestOf52;
@property (weak, nonatomic) IBOutlet UILabel *ChgOfYear;
@property (weak, nonatomic) IBOutlet UILabel *turnoverRate;
@property (weak, nonatomic) IBOutlet UILabel *turnover;
@property (weak, nonatomic) IBOutlet UILabel *marketValue;

@end
