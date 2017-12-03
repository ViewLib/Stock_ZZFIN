//
//  HomeViewController.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "HomeViewController.h"
#import "HomeTableViewCell.h"
#import "SearchViewController.h"
#import "StockValueViewController.h"


@interface HomeViewController ()<UITableViewDelegate,UITableViewDataSource,UISearchControllerDelegate>

//@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;

@property (weak, nonatomic) IBOutlet UIView *topView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topHigh;

@property (weak, nonatomic) IBOutlet UIButton *EditBtn;

@property (weak, nonatomic) IBOutlet UILabel *currentPrice;

@property (weak, nonatomic) IBOutlet UILabel *currentChf;

@property (weak, nonatomic) IBOutlet UITableView *OptionalTable;

@property (strong, nonatomic) NSMutableArray    *stocks;

@property (strong, nonatomic) UISearchController  *searchController;

@property (strong, nonatomic) UIView            *editingView;

@property (strong, nonatomic) UIView            *editTopView;

@property (strong, nonatomic) UIButton          *delegateBtn;

@property (strong, nonatomic) UIButton          *selectAllBtn;

@end

@implementation HomeViewController

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [Config shareInstance].KlineDate = [NSMutableArray array];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    [self createSearchBar];
    [self getTableData];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.view addSubview:self.editingView];
    [self.view addSubview:self.editTopView];
    [self.editingView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.view);
        make.height.equalTo(@45);
        make.bottom.equalTo(self.view).offset(45);
    }];
    [self.editTopView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.view);
        make.height.equalTo(@64);
        make.top.equalTo(self.view).offset(-64);
    }];
    
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
}

- (void)createSearchBar {
    __block SearchViewController *search = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Search"];
    WS(self)
    search.searchViewCancelBlock = ^{
        [selfWeak getTableData];
    };
    
    search.clickSearchViewCancelBlock = ^(id VC) {
        [selfWeak pushViewController:VC];
    };
    
    search.searchViewClickBlock = ^{
        [selfWeak.searchController.searchBar resignFirstResponder];
    };
    
    self.searchController = [[UISearchController alloc] initWithSearchResultsController:search];
    self.searchController.definesPresentationContext = YES;
    self.searchController.searchResultsUpdater = search;
    self.searchController.dimsBackgroundDuringPresentation = NO;
    [self.searchController.searchBar sizeToFit];
    
    UITextField *searchField = [self.searchController.searchBar valueForKey:@"searchField"];
    if (searchField) {
        [searchField setBackgroundColor:[UIColor whiteColor]];
        searchField.layer.cornerRadius = 18;
        searchField.layer.borderWidth = 1;
        searchField.layer.borderColor = [UIColor clearColor].CGColor;
        searchField.layer.masksToBounds = YES;
        searchField.placeholder = @"搜索";
        [searchField setTintColor:MAIN_COLOR];
    }
    
    self.searchController.searchBar.backgroundImage = [[UIImage alloc] init];
    self.searchController.searchBar.barTintColor = MAIN_COLOR;
    
    self.searchController.searchBar.tintColor = [UIColor whiteColor];
    self.searchController.searchBar.delegate = search;
    _topHigh.constant = self.searchController.searchBar.bounds.size.height;
    [_topView addSubview:self.searchController.searchBar];
}

#pragma mark - pushViewController
- (void)pushViewController:(id)VC {
    [self jumpToStockView:VC];
}

- (void)jumpToStockView:(StockObjEntity *)stock {
    StockValueViewController *viewController = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"value"];
    viewController.stock = stock;
    [self.navigationController setNavigationBarHidden:YES animated:nil];
    [self.navigationController pushViewController:viewController animated:YES];
}

#pragma mark - getOptionalStocks
- (void)getTableData {
    _stocks = [Utils getStock].mutableCopy;
    NSString *stockCodes;
    if (_stocks.count) {
        NSMutableArray *stocks = [NSMutableArray array];
        for (int i = 0; i < _stocks.count; i++) {
            StockObjEntity *entity = _stocks[i];
            [stocks addObject:entity.code];
        }
        stockCodes = [stocks componentsJoinedByString:@","];
    } else {
        stockCodes = @"sh000001,sz399001,sz399006";
    }
    WS(self)
    [[HttpRequestClient sharedClient] getStockInformation:stockCodes request:^(NSString *resultMsg, id dataDict, id error) {
        if (dataDict) {
            NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            NSString *responseString = [[NSString alloc] initWithData:dataDict encoding:enc];
            if (![responseString hasPrefix:@"pv_none_match=1"]) {
                if ([responseString rangeOfString:@"退市"].location == NSNotFound) {
                    responseString = [responseString stringByReplacingOccurrencesOfString:@"\n" withString:@""];
                    NSArray *responseValues = [responseString componentsSeparatedByString:@";"];
                    for (NSString *value in responseValues) {
                        NSArray *ary = [value componentsSeparatedByString:@"~"];
                        if (ary.count > 1) {
                            [[DataManager shareDataMangaer] updateSotckEntitys:ary];
                        }
                    }
                    [selfWeak reloadTableView];
                }
            }
        }
    }];
}

- (void) reloadTableView {
    _stocks = [Utils getStock].mutableCopy;
    if (_stocks.count > 0) {
        [self.OptionalTable reloadData];
    }
}

//编辑按钮点击
- (IBAction)clickEditBtn:(UIButton *)sender {
    if (!sender.selected) {
        [self.OptionalTable setEditing:YES animated:YES];
        self.tabBarController.tabBar.hidden = YES;
        [self showEitingView:YES];
        sender.hidden = YES;
        [self.currentPrice setText:@"股票"];
        [self.currentPrice setTextAlignment:NSTextAlignmentLeft];
        self.currentChf.hidden = YES;
    } else {
        [self.OptionalTable setEditing:NO animated:YES];
        self.tabBarController.tabBar.hidden = NO;
        [self showEitingView:NO];
        sender.hidden = NO;
        [self.currentPrice setText:@"当前价"];
        [self.currentPrice setTextAlignment:NSTextAlignmentRight];
        self.currentChf.hidden = NO;
    }
    sender.selected = !sender.selected;
}

- (void)showEitingView:(BOOL)isShow{
    [self.editingView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.view).offset(isShow?0:45);
    }];
    [self.editTopView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.view).offset(isShow?10:-64);
    }];
    [UIView animateWithDuration:0.3 animations:^{
        [self.view layoutIfNeeded];
    }];
}

#pragma mark - TableViewDelegateAndDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _stocks.count;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
    return [[UIView alloc] init];
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath{
    return UITableViewCellEditingStyleDelete | UITableViewCellEditingStyleInsert;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellID =@"HomeCell";
    HomeTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
    if (nil == cell) {
        cell= (HomeTableViewCell *)[[[NSBundle mainBundle] loadNibNamed:@"HomeTableViewCell" owner:self options:nil] firstObject];
        WS(self)
        cell.clickMenuBlock = ^{
            [selfWeak reloadTableView];
        };
        [cell updateCell:_stocks[indexPath.row]];
        cell.selectedBackgroundView = [[UIView alloc] initWithFrame:cell.bounds];
        cell.selectedBackgroundView.backgroundColor = [UIColor clearColor];
        
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (tableView.isEditing) {
        if (tableView.indexPathsForSelectedRows.count == _stocks.count) {
            [_selectAllBtn setTitle:@"全不选" forState:UIControlStateNormal];
        }
        return;
    }
    StockObjEntity *entity = _stocks[indexPath.row];
    [self jumpToStockView:entity];
}

//editingView懒加载
- (UIView *)editingView{
    if (!_editingView) {
        _editingView = [[UIView alloc] init];
        [_editingView setBackgroundColor:[Utils colorFromHexRGB:@"EDF3F8"]];
        
        _delegateBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_delegateBtn setTitle:@"删除" forState:UIControlStateNormal];
        [_delegateBtn setContentHorizontalAlignment:UIControlContentHorizontalAlignmentRight];
        [_delegateBtn setTitleColor:MAIN_COLOR forState:UIControlStateNormal];
        [_delegateBtn addTarget:self action:@selector(p__buttonClick:) forControlEvents:UIControlEventTouchUpInside];
        [_editingView addSubview:_delegateBtn];
        [_delegateBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.bottom.equalTo(_editingView);
            make.right.equalTo(@-10);
            make.width.equalTo(_editingView).multipliedBy(0.5);
        }];
        
        _selectAllBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_selectAllBtn setTitle:@"全选" forState:UIControlStateNormal];
        [_selectAllBtn setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
        [_selectAllBtn setTitleColor:MAIN_COLOR forState:UIControlStateNormal];
        [_selectAllBtn addTarget:self action:@selector(p__buttonClick:) forControlEvents:UIControlEventTouchUpInside];
        [_editingView addSubview:_selectAllBtn];
        [_selectAllBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.bottom.equalTo(_editingView);
            make.left.equalTo(@10);
            make.width.equalTo(_editingView).multipliedBy(0.5);
        }];
    }
    return _editingView;
}

- (UIView *)editTopView {
    if (!_editTopView) {
        _editTopView = [[UIView alloc] init];
        [_editTopView setBackgroundColor:MAIN_COLOR];
        UILabel *lab = [[UILabel alloc] init];
        [lab setTextColor:[UIColor whiteColor]];
        [lab setTextAlignment:NSTextAlignmentCenter];
        [lab setText:@"编辑自选"];
        [_editTopView addSubview:lab];
        [lab mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.bottom.equalTo(_editTopView);
            make.height.equalTo(@44);
        }];
        
        UIButton *successBtn = [UIButton buttonWithType:UIButtonTypeCustom];
//        [successBtn setTitle:@"完成" forState:UIControlStateNormal];
        [successBtn setImage:[UIImage imageNamed:@"icon_return_white"] forState:UIControlStateNormal];
        [successBtn addTarget:self action:@selector(clickSuccessBtn) forControlEvents:UIControlEventTouchUpInside];
        [_editTopView addSubview:successBtn];
        [successBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.equalTo(_editTopView);
            make.left.equalTo(@10);
            make.width.height.equalTo(@44);
        }];
    }
    return _editTopView;
}

#pragma mark -- event response

- (void)p__buttonClick:(UIButton *)sender{
    if ([[sender titleForState:UIControlStateNormal] isEqualToString:@"删除"]) {
        NSMutableIndexSet *insets = [[NSMutableIndexSet alloc] init];
        [[self.OptionalTable indexPathsForSelectedRows] enumerateObjectsUsingBlock:^(NSIndexPath * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [insets addIndex:obj.row];
            StockObjEntity *entity = self.stocks[obj.row];
            [Utils removeStock:entity.code utilRequest:nil];
        }];
        [self.stocks removeObjectsAtIndexes:insets];
        [self.OptionalTable deleteRowsAtIndexPaths:[self.OptionalTable indexPathsForSelectedRows] withRowAnimation:UITableViewRowAnimationFade];
        
        /** 数据清空情况下取消编辑状态*/
        if (self.stocks.count == 0) {
            [self clickSuccessBtn];
        }
    } else if ([[sender titleForState:UIControlStateNormal] isEqualToString:@"全选"]) {
        [self.stocks enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [self.OptionalTable selectRowAtIndexPath:[NSIndexPath indexPathForRow:idx inSection:0] animated:NO scrollPosition:UITableViewScrollPositionNone];
        }];
        
        [sender setTitle:@"全不选" forState:UIControlStateNormal];
    } else if ([[sender titleForState:UIControlStateNormal] isEqualToString:@"全不选"]){
        [self.OptionalTable reloadData];
        
        [sender setTitle:@"全选" forState:UIControlStateNormal];
        
    }
}

- (void)clickSuccessBtn {
    [self clickEditBtn:self.EditBtn];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
