//
//  SettingViewController.m
//  Stock
//
//  Created by mac on 2017/9/6.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "SettingViewController.h"
#import "SettingValueViewController.h"

@interface SettingViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UILabel *topLabel;
@property (weak, nonatomic) IBOutlet UITableView *settingTable;

@property (strong, nonatomic) NSArray   *settingTitles;

@end

@implementation SettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _topLabel.text = self.topTitle;
    if ([_topTitle isEqual:@"设置"]) {
        [self initSettingTable];
    } else {
        self.settingTable.hidden = YES;
    }
}

//- (void)setTopTitle:(NSString *)topTitle {
//    _topTitle = topTitle;
//    _topLabel.text = _topTitle;
//    if ([_topTitle isEqual:@"设置"]) {
//        [self initSettingTable];
//    }
//}

- (void)initSettingTable {
    self.settingTitles = @[@"清楚缓存",@"刷新频率",@"推送设置",@"免责声明"];
    [self.settingTable setScrollEnabled:NO];
    [self.settingTable reloadData];
}

#pragma mark - UITableViewDelegateAndDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 55;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.settingTitles.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.text = self.settingTitles[indexPath.row];
        cell.textLabel.textColor = [Utils colorFromHexRGB:@"090909"];
        cell.textLabel.font = [UIFont systemFontOfSize:14];
        
        UILabel *rightLabel = [[UILabel alloc] initWithFrame:CGRectMake(K_FRAME_BASE_WIDTH-100, 0, 70, 55)];
        [rightLabel setTextColor:[Utils colorFromHexRGB:@"888888"]];
        [rightLabel setFont:[UIFont systemFontOfSize:14]];
        [rightLabel setTextAlignment:NSTextAlignmentRight];
        if (indexPath.row == 0 || indexPath.row == 2) {
            [rightLabel setText:@"20M"];
            if (indexPath.row == 2) {
                [rightLabel setText:@"未设置"];
                if ([Config shareInstance].isNotification) {
                    [rightLabel setText:@"已设置"];
                }
            }
            
            [cell.contentView addSubview:rightLabel];
        }
        
        UIImage *img = [UIImage imageNamed:@"icon_return"];
        UIImageView *right = [[UIImageView alloc] initWithFrame:CGRectMake(K_FRAME_BASE_WIDTH-25, 0, img.size.width, img.size.height)];
        [right setCenterY:55/2];
        [right setImage:[UIImage imageNamed:@"icon_return"]];
        [right setContentMode:UIViewContentModeRedraw];
        [cell.contentView addSubview:right];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    switch (indexPath.row) {
        case 0:
            [self successAlert:@"确认要清理缓存吗？" one:^{
                
            } two:^{
                
            } oneT:@"取消" twoT:@"确定"];
            break;
        case 1:
        {
            SettingValueViewController *view = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"settingValue"];
            view.topTitle = self.settingTitles[indexPath.row];
            [self.navigationController pushViewController:view animated:YES];
        }
            break;
        case 2:
        {
            if (![Config shareInstance].isNotification) {
                [self successAlert:@"您尚未开启消息推送您\n您将无法收到消息" one:^{
                    
                } two:^{
                    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"prefs:root=NOTIFICATIONS_ID"] options:@{} completionHandler:nil];
                } oneT:@"取消" twoT:@"去开启"];
            }
        }
            break;
        case 3:
        {
            SettingValueViewController *view = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"settingValue"];
            view.topTitle = self.settingTitles[indexPath.row];
            [self.navigationController pushViewController:view animated:YES];
        }
            break;
        default:
            break;
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
