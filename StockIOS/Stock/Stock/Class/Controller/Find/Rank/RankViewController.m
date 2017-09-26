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

@property (nonatomic, strong)   NSArray     *tableValue;

@end

@implementation RankViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [_collectionBtn ImgTopTextButtom];
    
    [_topLabel setText:self.valueDic[@"rankModel"][@"title"]];
    
    for (UIButton *btn in @[_filterOne,_filterTwo,_filterThr,_filterFor]) {
        [btn ImgRightTextLeft];
    }
    
    [self getData];
}

- (void)getData {
    NSDictionary *dic = @{@"title":self.valueDic[@"rankModel"][@"title"],@"serch_relation":self.valueDic[@"rankModel"][@"searchRelation"]};
    WS(self)
    [[HttpRequestClient sharedClient] getRankDetail:dic request:^(NSString *resultMsg, id dataDict, id error) {
        if ([dataDict[@"resultCode"] floatValue] == 200) {
            selfWeak.tableValue = dataDict[@"rankResultList"];
            [selfWeak.valueTable reloadData];
        }
    }];
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
    return self.tableValue.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    RankTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CellID"];
    NSDictionary *dic = self.tableValue[indexPath.row];
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"RankTableViewCell" owner:nil options:nil] firstObject];
        cell.row = indexPath.row;
        [cell updateCell:dic];
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
