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
#import "StockValueViewController.h"
#import "FindSearchView.h"

@interface RankViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel *topLabel;

@property (weak, nonatomic) IBOutlet UIButton *collectionBtn;

@property (weak, nonatomic) IBOutlet UIButton *filterOne;

@property (weak, nonatomic) IBOutlet UIButton *filterTwo;

@property (weak, nonatomic) IBOutlet UIButton *filterThr;

@property (weak, nonatomic) IBOutlet UIButton *filterFor;

@property (weak, nonatomic) IBOutlet UIView *filterLine;

@property (weak, nonatomic) IBOutlet UITableView *valueTable;

@property (weak, nonatomic) IBOutlet UIView *contentBgView;

@property (weak, nonatomic) IBOutlet  UIView *searchView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *FindViewHigh;

@property (strong, nonatomic) FindSearchView *fliterSearchView;

@property (nonatomic, strong)   NSArray     *tableValue;

@property (nonatomic, strong)   NSDictionary    *filterOneDic;

@property (nonatomic, strong)   NSDictionary    *filterTwoDic;

@property (nonatomic, strong)   NSDictionary    *filterThrDic;

@property (nonatomic, strong)   NSDictionary    *filterForDic;

@end

@implementation RankViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [_collectionBtn ImgTopTextButtom];
    
    [_topLabel setText:self.valueDic[@"rankModel"][@"title"]];
    NSArray *btns = @[_filterOne,_filterTwo,_filterThr,_filterFor];
    
    [[Config shareInstance].rankSearchList enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj) {
            UIButton *btn = btns[idx];
            NSDictionary *dic = obj;
            [btn setTitle:dic[@"groupName"] forState:UIControlStateNormal];
            [btn ImgRightTextLeft];
        }
    }];
    
    [self initSearchView];
    
    [self getData];
}

- (void)initSearchView {
    _fliterSearchView = [[NSBundle mainBundle]loadNibNamed:@"FindSearchView" owner:nil options:nil].firstObject;
    _fliterSearchView.frame = self.searchView.bounds;
    WS(self)
    _fliterSearchView.collectionCellClick = ^{
        [selfWeak reloadTableView];
        selfWeak.contentBgView.hidden = YES;
        if (selfWeak.FindViewHigh.constant != 0) {
            [UIView animateWithDuration:0.5f animations:^{
                selfWeak.filterLine.hidden = YES;
                selfWeak.tabBarController.tabBar.hidden = NO;
                selfWeak.FindViewHigh.constant = 0;
            }];
        }
    };
    
    [self.searchView addSubview:_fliterSearchView];
}

- (void)reloadTableView {
    NSMutableArray *searchlist = [NSMutableArray array];
    [[Config shareInstance].rankList enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isKindOfClass:[NSDictionary class]]) {
            NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:obj];
            [dic removeObjectForKey:@"filterName"];
            [searchlist addObject:dic];
        }
    }];
    [[Config shareInstance].rankOtherList enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isKindOfClass:[NSDictionary class]]) {
            NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:obj];
            [dic removeObjectForKey:@"filterName"];
            [searchlist addObject:dic];
        }
    }];
    NSDictionary *dic = @{@"search_relation":@"114",@"searchlist":searchlist};
    [self showHudInView:self.view hint:@"请稍后，正在获取数据"];
    WS(self)
    [[HttpRequestClient sharedClient] getFilterSearch:dic request:^(NSString *resultMsg, id dataDict, id error) {
        [selfWeak hideHud];
        if (dataDict) {
            if ([dataDict[@"resultCode"] floatValue] == 200) {
                selfWeak.tableValue = dataDict[@"rankResultList"];
                [selfWeak.valueTable reloadData];
            }
        }
    }];
}

//头部fliter的点击事件
- (IBAction)clickFilterBtn:(UIButton *)sender {
    [@[_filterOne,_filterTwo,_filterThr,_filterFor] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj) {
            UIButton *btn = (UIButton *)obj;
            if (![btn isEqual: sender]) {
                btn.selected = NO;
            }
        }
    }];
    if (self.FindViewHigh.constant != 0) {
        [UIView animateWithDuration:0.5f animations:^{
            self.filterLine.hidden = YES;
            self.tabBarController.tabBar.hidden = NO;
            self.FindViewHigh.constant = 0;
        }];
    }
    self.filterLine.centerX = sender.centerX;
    sender.selected = !sender.selected;
    
    self.contentBgView.hidden = !sender.selected;
    
    NSInteger num = sender.tag - 6001;
    NSDictionary *dic = [Config shareInstance].rankSearchList[num];
    [_fliterSearchView updateCell:dic];
    if (!sender.selected) {
        [UIView animateWithDuration:0.5f animations:^{
            self.filterLine.hidden = YES;
            self.tabBarController.tabBar.hidden = NO;
            self.FindViewHigh.constant = 0;
        }];
    } else {
        [UIView animateWithDuration:0.5f animations:^{
            self.filterLine.hidden = NO;
            self.tabBarController.tabBar.hidden = YES;
            self.FindViewHigh.constant = K_FRAME_BASE_HEIGHT-104;
        }];
    }
}

//存储从筛选页面传回来的数据
- (void)saveDic:(NSDictionary *)dic From:(NSString *)from {
    if ([from isEqual:self.filterOne.currentTitle]) {
        self.filterForDic = dic;
        self.filterOne.selected = NO;
        [self clickFilterBtn:self.filterOne];
        self.filterOne.selected = YES;
    }
}

//获取数据方法
- (void)getData {
    [self showHudInView:self.view hint:@"请稍后，正在获取数据"];
    NSDictionary *dic = @{@"title":self.valueDic[@"rankModel"][@"title"],@"search_relation":self.valueDic[@"rankModel"][@"searchRelation"]};
    WS(self)
    [[HttpRequestClient sharedClient] getRankDetail:dic request:^(NSString *resultMsg, id dataDict, id error) {
        [selfWeak hideHud];
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
    [self showHint:@"正在请求数据..."];
    NSDictionary *dic = self.tableValue[indexPath.row];
    [self insertCoreData:dic[@"stockCode"] isJoinCoreData:NO request:^(StockEntity *entity) {
        [self junpToStockValueViewController:entity];
    }];
}

/**
 搜索结果获取网络数据并写入coreData
 */
- (void)insertCoreData:(NSString *)code isJoinCoreData:(BOOL)isJoin request:(void(^)(StockEntity *entity))request {
    [[HttpRequestClient sharedClient] getStockInformation:code request:^(NSString *resultMsg, id dataDict, id error) {
        if (dataDict) {
            NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            NSString *responseString = [[NSString alloc] initWithData:dataDict encoding:enc];
            if (![responseString isEqualToString:@"pv_none_match=1"]) {
                if ([responseString rangeOfString:@"退市"].location == NSNotFound) {
                    NSArray *responseValues = [responseString componentsSeparatedByString:@"~"];
                    StockEntity *entity = [[DataManager shareDataMangaer] getStockWithAry:responseValues];
                    if (request) {
                        request(entity);
                    }
                } else {
                    [self hideHud];
                    [self showHint:@"您选的股票已退市"];
                }
            } else {
                [self hideHud];
                [self showHint:@"服务器访问失败，请重试"];
            }
        }
    }];
}

- (void)junpToStockValueViewController:(StockEntity *)entity {
    StockValueViewController *viewController = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"value"];
    
    viewController.stock = entity;
    [self hideHud];
    [self.navigationController pushViewController:viewController animated:YES];
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
