//
//  StockTopTableViewCell.m
//  Stock
//
//  Created by mac on 2017/10/16.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockTopTableViewCell.h"

@implementation StockTopTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
}

- (void)updateCell:(StockEntity *)dic {
    _stockLast.text = dic.currentprice;
    _Change.text = dic.upsdowns;
    _Chg.text = [NSString stringWithFormat:@"%@%@",dic.pricefluctuation,@"%"];
    _highest.text = dic.highest;
    _lowest.text = dic.lowest;
//    _ChgOfYear.text = dic;
    _turnoverRate.text = dic.turnoverrate;
    _turnover.text = dic.transactions;
    _marketValue.text = dic.totalmarketcapitalization;
    self.code = dic.code;
    if ([dic.upsdowns hasPrefix:@"-"]) {
        _stockLast.textColor = DOWN_COLOR;
        _Change.textColor = DOWN_COLOR;
        _Chg.textColor = DOWN_COLOR;
    } else {
        _Change.text = [NSString stringWithFormat:@"+%@",dic.upsdowns];
        _Chg.text = [NSString stringWithFormat:@"+%@%@",dic.pricefluctuation,@"%"];
    }
    if ([Utils isSelectionStock:dic.code]) {
        [_isSelect setSelected:YES];
    }
    [_isSelect ImgTopTextButtom];
}

- (IBAction)clickSelectBtn:(UIButton *)sender {
    WS(self)
    if (!sender.selected) {
        //添加自选股
        [Utils AddStock:self.code utilRequest:^(BOOL value) {
            if (value) {
                [selfWeak.isSelect setSelected:YES];
            }
        }];
    } else {
        //删除自选股
        [Utils removeStock:self.code utilRequest:^(BOOL value) {
            if (value) {
                [selfWeak.isSelect setSelected:NO];
            }
        }];
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
