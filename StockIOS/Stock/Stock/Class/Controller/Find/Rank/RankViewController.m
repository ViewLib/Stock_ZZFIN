//
//  RankViewController.m
//  Stock
//
//  Created by mac on 2017/9/10.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "RankViewController.h"

@interface RankViewController ()

@property (weak, nonatomic) IBOutlet UILabel *topLabel;

@property (weak, nonatomic) IBOutlet UIButton *collectionBtn;

@property (weak, nonatomic) IBOutlet UIButton *filterOne;

@property (weak, nonatomic) IBOutlet UIButton *filterTwo;

@property (weak, nonatomic) IBOutlet UIButton *filterThr;

@property (weak, nonatomic) IBOutlet UIButton *filterFor;

@end

@implementation RankViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [_collectionBtn ImgTopTextButtom];
    
    [_topLabel setText:self.valueDic[@"stockViewModel"][@"stockCode"]];
    
    for (UIButton *btn in @[_filterOne,_filterTwo,_filterThr,_filterFor]) {
        [btn ImgRightTextLeft];
    }
}

/**
 返回按钮点击事件
*/
- (IBAction)clickReturnBtn:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
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
