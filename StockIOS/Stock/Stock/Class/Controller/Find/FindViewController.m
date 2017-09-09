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
    // Do any additional setup after loading the view.
}

#pragma mark - 
#pragma mark -- UITableViewDelegateAndDataSource

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView estimatedHeightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FindTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FindTableViewCell"];
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"FindTableViewCell" owner:nil options:nil] firstObject];
        WS(self)
        cell.viewOneClickBlock = ^(NSInteger row) {
            [selfWeak clickView:row type:@"One"];
        };
        cell.viewTwoClickBlock = ^(NSInteger row) {
            [selfWeak clickView:row type:@"Two"];
        };
    }
    return cell;
}

-(void)clickView:(NSInteger)row type:(NSString *)type {
    RankViewController *rankVC = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Rank"];
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
