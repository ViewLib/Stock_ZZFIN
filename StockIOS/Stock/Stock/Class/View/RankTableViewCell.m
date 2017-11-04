//
//  RankTableViewCell.m
//  Stock
//
//  Created by mac on 2017/9/25.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "RankTableViewCell.h"



@implementation RankTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic {
    self.number.text = [NSString stringWithFormat:@"%02ld",self.row+1];
    self.stockName.text = dic[@"stockName"];
    self.stockCode.text = dic[@"stockCode"];
    self.value1.text = dic[@"attr1"];
    self.value2.text = dic[@"attr2"];
    
    if ([Utils isSelectionStock:dic[@"stockCode"]]) {
        _addBtn.hidden = YES;
        _addValue.hidden = NO;
    }
}

//添加自选按钮点击
- (IBAction)clickAddBtn:(UIButton *)sender {
    
    [[HttpRequestClient sharedClient] getStockInformation:self.stockCode.text request:^(NSString *resultMsg, id dataDict, id error) {
        if (dataDict) {
            NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            NSString *responseString = [[NSString alloc] initWithData:dataDict encoding:enc];
            if (![responseString isEqualToString:@"pv_none_match=1"]) {
                if ([responseString rangeOfString:@"退市"].location == NSNotFound) {
                    NSArray *responseValues = [responseString componentsSeparatedByString:@"~"];
                    [[DataManager shareDataMangaer] updateSotckEntitys:responseValues];
                    [[UIApplication sharedApplication].delegate.window.rootViewController showHint:@"已添加自选"];
                    _addBtn.hidden = YES;
                    _addValue.hidden = NO;
                } else {
                    [[UIApplication sharedApplication].delegate.window.rootViewController showHint:@"您选的股票已退市"];
                }
            } else {
                [[UIApplication sharedApplication].delegate.window.rootViewController showHint:@"服务器访问失败，请重试"];
            }
        }
    }];
}

- (void)updateCellColor {
    NSArray *labels = @[_number,_stockName,_stockCode,_addValue,_value1,_value2];
    [labels enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        UILabel *lab = (UILabel *)obj;
        lab.textColor = MAIN_COLOR;
    }];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
