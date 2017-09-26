//
//  RankViewController.m
//  Stock
//
//  Created by mac on 2017/9/10.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "RankTableTopView.h"
#import "RankTableViewCell.h"
#import "RankViewController.h"

@interface RankViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel *topLabel;

@property (weak, nonatomic) IBOutlet UIButton *collectionBtn;

@property (weak, nonatomic) IBOutlet UIButton *filterOne;

@property (weak, nonatomic) IBOutlet UIButton *filterTwo;

@property (weak, nonatomic) IBOutlet UIButton *filterThr;

@property (weak, nonatomic) IBOutlet UIButton *filterFor;

@property (weak, nonatomic) IBOutlet UITableView *valueTable;

@end

@implementation RankViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [_collectionBtn ImgTopTextButtom];
    
    [_topLabel setText:self.valueDic[@"rankModel"][@"title"]];
    
    for (UIButton *btn in @[_filterOne,_filterTwo,_filterThr,_filterFor]) {
        [btn ImgRightTextLeft];
    }
}

#pragma mark - UITableViewDelegateAndDateSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 30;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    RankTableTopView *view = [[NSBundle mainBundle] loadNibNamed:@"RankTableTopView" owner:nil options:nil].firstObject;
    [view setFrame:CGRectMake(0, 0, K_FRAME_BASE_WIDTH-24, 30)];
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 10;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    RankTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CellID"];
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"RankTableViewCell" owner:nil options:nil] firstObject];
        
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
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
