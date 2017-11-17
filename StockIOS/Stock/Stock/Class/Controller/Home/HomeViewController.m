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

@property (weak, nonatomic) IBOutlet UITableView *OptionalTable;

@property (strong, nonatomic)       NSArray    *stocks;

@property (strong, nonatomic)       UISearchController  *searchController;

@end

@implementation HomeViewController

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:nil];
    [self createSearchBar];
    [self getTableData];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
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
    
    self.searchController = [[UISearchController alloc] initWithSearchResultsController:search];
    self.searchController.definesPresentationContext = YES;
    self.searchController.searchResultsUpdater = search;
    self.searchController.dimsBackgroundDuringPresentation = NO;
    [self.searchController.searchBar sizeToFit];
    self.searchController.searchBar.placeholder = @"请输入搜索内容";
    UIView *firstSubView = self.searchController.searchBar.subviews.firstObject;
    firstSubView.backgroundColor = MAIN_COLOR;
    UIView *backgroundImageView = [firstSubView.subviews firstObject];
    [backgroundImageView removeFromSuperview];
    
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
    _stocks = [Utils getStock];
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
    _stocks = [Utils getStock];;
    if (_stocks.count > 0) {
        [self.OptionalTable reloadData];
    }
}

#pragma mark - TableViewDelegateAndDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _stocks.count;
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
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    StockObjEntity *entity = _stocks[indexPath.row];
    [self jumpToStockView:entity];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
