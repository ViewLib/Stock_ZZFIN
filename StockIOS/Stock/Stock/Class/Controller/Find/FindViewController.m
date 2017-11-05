//
//  FindViewController.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "FindViewController.h"
#import "FindTableViewCell.h"
#import "RankViewController.h"

@interface FindViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *contentTable;

@end

@implementation FindViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    WS(self)
    [RACObserve([Config shareInstance], top10List) subscribeNext:^(NSArray *x) {
        if (x.count > 0) {
            [selfWeak.contentTable reloadData];
        }
    }];

//    for (NSDictionary *dic in [Config shareInstance].hotStocks) {
//        NSLog(@"%@",dic);
//    }
}

#pragma mark - 
#pragma mark -- UITableViewDelegateAndDataSource

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSInteger num;
    if ([Config shareInstance].top10List.count%2 != 0) {
        num = [Config shareInstance].top10List.count/2 + 1;
    } else {
        num = [Config shareInstance].top10List.count/2;
    }
    return num;
//    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView estimatedHeightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FindTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FindTableViewCell"];
    NSInteger row1 = indexPath.row * 2;
    NSInteger row2 = indexPath.row * 2 + 1;
    NSMutableArray *ary = [NSMutableArray array];
    NSDictionary *dic = [Config shareInstance].top10List[row1];
    [ary addObject:dic];
    if (row2 < [Config shareInstance].top10List.count) {
        NSDictionary *dic2 = [Config shareInstance].top10List[row2];
        [ary addObject:dic2];
    }
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"FindTableViewCell" owner:nil options:nil] firstObject];
        WS(self)
        cell.viewOneClickBlock = ^(NSInteger row) {
            [selfWeak clickView:row type:@"One"];
        };
        cell.viewTwoClickBlock = ^(NSInteger row) {
            [selfWeak clickView:row type:@"Two"];
        };
        [cell updateCell:ary with:(int)indexPath.row];
    }
    return cell;
}

-(void)clickView:(NSInteger)row type:(NSString *)type {
    RankViewController *rankVC = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Rank"];
    NSInteger num = row * 2;
    if ([type isEqual:@"Two"]) {
        num += 1;
    };
    NSDictionary *dic = [Config shareInstance].top10List[num];
    rankVC.valueDic = dic;
    [self.navigationController pushViewController:rankVC animated:YES];
}

#pragma mark - LayoutSubviews

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    if (_contentTable.frame.size.height > 400) {
        _contentTable.scrollEnabled = NO;
    }
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
