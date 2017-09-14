//
//  SearchHistoryTableViewCell.m
//  Stock
//
//  Created by mac on 2017/9/12.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "SearchHistoryTableViewCell.h"
#import "StockEntity.h"

@interface SearchHistoryTableViewCell ()
@property (weak, nonatomic) IBOutlet UILabel *Name;
@property (weak, nonatomic) IBOutlet UILabel *code;
@property (weak, nonatomic) IBOutlet UIButton *addBtn;
@property (weak, nonatomic) IBOutlet UILabel *ValueLab;

@end

@implementation SearchHistoryTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

/**
 更新cell
 @param dic 更新的内容
 */
- (void)updateCell:(NSDictionary *)dic {
    NSArray *titles = [dic[@"title"] componentsSeparatedByString:@"("];
    if (titles.count > 0) {
        _Name.text = [titles firstObject];
        _code.text = [[titles lastObject] stringByReplacingOccurrencesOfString:@")" withString:@""];
    }
    [self isOrNotOptional:dic];
}

- (void)isOrNotOptional:(NSDictionary *)dic {
    NSPredicate* pred = [NSPredicate predicateWithFormat:@"code == %@", dic[@"code"]];
    // 使用谓词过滤NSArray
    NSArray *value = [[Config shareInstance].optionalStocks filteredArrayUsingPredicate:pred];
    if (value.count > 0) {
        _addBtn.hidden = YES;
        _ValueLab.hidden = NO;
    }
}

- (IBAction)clickAddBtn:(UIButton *)sender {
    if (self.addOptionalBlock) {
        self.addOptionalBlock(self.indentationLevel);
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
