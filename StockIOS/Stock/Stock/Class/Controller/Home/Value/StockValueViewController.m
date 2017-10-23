//
//  StockValueViewController.m
//  Stock
//
//  Created by mac on 2017/9/23.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "StockValueViewController.h"

#import "StockTopTableViewCell.h"
#import "StockChartTableViewCell.h"
#import "StockMajorNewsTableViewCell.h"
#import "StockMajorEventsTableViewCell.h"
#import "ComputerIntroductionTableViewCell.h"
#import "FinancialInformationTableViewCell.h"
#import "HorizontalComparisonTableViewCell.h"
#import "BrokersRatingTableViewCell.h"

@interface StockValueViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UILabel *stockName;
@property (weak, nonatomic) IBOutlet UILabel *stockCode;
@property (weak, nonatomic) IBOutlet UITableView *valueTable;


@end

@implementation StockValueViewController

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _stockName.text = _stock.name;
    _stockCode.text = _stock.code;
    
    _valueTable.rowHeight = UITableViewAutomaticDimension;
    _valueTable.estimatedRowHeight = 300;
    // Do any additional setup after loading the view.
}

//点击返回按钮
- (IBAction)clickReturnBtn:(UIButton *)sender {
    if (self.navigationController) {
        [self.navigationController popViewControllerAnimated:YES];
    } else {
        CATransition *animation = [CATransition animation];
        
        animation.duration = .5;
        
        animation.timingFunction = UIViewAnimationCurveEaseInOut;
        
        animation.type = kCATransitionPush;
        
        animation.subtype = kCATransitionFromLeft;
        
        [self.view.window.layer addAnimation:animation forKey:nil];
        [self dismissViewControllerAnimated:NO completion:nil];
    }
}

#pragma mark - UITableViewDelegateAndDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 8;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row == 1) {
        StockChartTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"StockChartTableViewCell" owner:nil options:nil] firstObject];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    } else if (indexPath.row == 2) {
        StockMajorNewsTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"StockMajorNewsTableViewCell" owner:nil options:nil] firstObject];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    } else if (indexPath.row == 3) {
        StockMajorEventsTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"StockMajorEventsTableViewCell" owner:nil options:nil] firstObject];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    } else if (indexPath.row == 4) {
        ComputerIntroductionTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"ComputerIntroductionTableViewCell" owner:nil options:nil] firstObject];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    } else if (indexPath.row == 5) {
        FinancialInformationTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"FinancialInformationTableViewCell" owner:nil options:nil] firstObject];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    } else if (indexPath.row == 6) {
        HorizontalComparisonTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"HorizontalComparisonTableViewCell" owner:nil options:nil] firstObject];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    } else if (indexPath.row == 7) {
        BrokersRatingTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"BrokersRatingTableViewCell" owner:nil options:nil] firstObject];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    } else {
        StockTopTableViewCell *cell = [[[NSBundle mainBundle] loadNibNamed:@"StockTopTableViewCell" owner:nil options:nil] firstObject];
        [cell updateCell:_stock];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        return cell;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"indexPath %ld",(long)indexPath.row);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
