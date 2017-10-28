//
//  SettingValueViewController.m
//  Stock
//
//  Created by mac on 2017/10/28.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "SettingValueViewController.h"

@interface SettingValueViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel *topLabel;

@property (weak, nonatomic) IBOutlet UITableView *updateTable;

@property (strong, nonatomic) NSArray   *updateAry;

@end

@implementation SettingValueViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.topLabel.text = self.topTitle;
    if ([self.topTitle hasPrefix:@"刷新频率"]) {
        self.updateTable.hidden = NO;
        self.updateTable.scrollEnabled = NO;
    } 
    self.updateAry = @[@{@"title": @"2G/3G/4G",@"value":@[@"不刷新",@"自动刷新"]},@{@"title": @"WIFI",@"value":@[@"不刷新",@"自动刷新"]}];
}

#pragma mark - UITableViewDelegateAndDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.updateAry.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 30;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    NSDictionary *dic = self.updateAry[section];
    return dic[@"title"];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSDictionary *dic = self.updateAry[section];
    NSArray *ary = dic[@"value"];
    return ary.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 55;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if (!cell) {
        NSDictionary *dic = self.updateAry[indexPath.section];
        NSArray *ary = dic[@"value"];
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.text = ary[indexPath.row];
        cell.textLabel.textColor = [Utils colorFromHexRGB:@"090909"];
        cell.textLabel.font = [UIFont systemFontOfSize:14];
        
        UIImage *img = [UIImage imageNamed:@"icon_right"];
        UIImageView *right = [[UIImageView alloc] initWithFrame:CGRectMake(K_FRAME_BASE_WIDTH-25, 0, img.size.width, img.size.height)];
        right.tag = 12399321;
        [right setCenterY:55/2];
        [right setImage:img];
        [right setContentMode:UIViewContentModeRedraw];
        if (indexPath.row == 1) {
            right.hidden = NO;
        } else {
            right.hidden = YES;
        }
        
        [cell.contentView addSubview:right];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    for (int i = 0; i < 2; i++) {
        UITableViewCell *cell = [tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:indexPath.section]];
        UIImageView *view = [cell viewWithTag:12399321];
        if (i == indexPath.row) {
            view.hidden = NO;
        } else {
            view.hidden = YES;
        }
    }
}

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
