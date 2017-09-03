//
//  HomeViewController.m
//  Stock
//
//  Created by mac on 2017/9/1.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "HomeViewController.h"
#import "HomeTableViewCell.h"
#import "SearchViewController.h";

@interface HomeViewController ()<UITableViewDelegate,UITableViewDataSource,UISearchControllerDelegate,UISearchResultsUpdating>

//@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;

@property (weak, nonatomic) IBOutlet UIView *topView;

@property (weak, nonatomic) IBOutlet UIButton *EditBtn;

@property (weak, nonatomic) IBOutlet UITableView *OptionalTable;

@property (strong, nonatomic)       NSArray    *stocks;

@property (strong, nonatomic)       UISearchController  *searchController;

@end

@implementation HomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    
//    [self inputStockData];
    
    [self createSearchBar];
    
//    [_OptionalTable registerNib:[UINib nibWithNibName:@"HomeTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeCell"];
    [self getTableData];
}

- (void)createSearchBar {
    
    SearchViewController *search = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Search"];
    
    self.searchController = [[UISearchController alloc] initWithSearchResultsController:search];
    self.searchController.definesPresentationContext = YES;
    self.searchController.searchResultsUpdater = search;
    self.searchController.dimsBackgroundDuringPresentation = NO;
    [self.searchController.searchBar sizeToFit];
    self.searchController.searchBar.placeholder = @"请输入搜索内容";
    self.searchController.searchBar.backgroundImage = SEARCHBAR_BGIMG;
    [self.searchController.searchBar setBackgroundColor:[UIColor clearColor]];
    self.searchController.searchBar.delegate = search;
    [_topView addSubview:self.searchController.searchBar];
}

- (void)inputStockData {
    NSDictionary *dic1 = @{@"name": @"五粮液", @"code": @"000858", @"from": @"sz", @"price": @"12.3", @"risefall": @"0.25"};
    [[DataManager shareDataMangaer] updateSotckEntitys:dic1];
    NSDictionary *dic2 = @{@"name": @"国风塑业", @"code": @"000859", @"from": @"sz", @"price": @"5.19", @"risefall": @"0.58"};
    [[DataManager shareDataMangaer] updateSotckEntitys:dic2];
    NSDictionary *dic3 = @{@"name": @"鞍钢股份", @"code": @"000898", @"from": @"sz", @"price": @"7.83", @"risefall": @"6.82"};
    [[DataManager shareDataMangaer] updateSotckEntitys:dic3];
    NSDictionary *dic4 = @{@"name": @"深赛格", @"code": @"000058", @"from": @"sz", @"price": @"7.67", @"risefall": @"-2.13"};
    [[DataManager shareDataMangaer] updateSotckEntitys:dic4];
    NSDictionary *dic5 = @{@"name": @"高鸿股份", @"code": @"000851", @"from": @"sz", @"price": @"10.60", @"risefall": @"1.15"};
    [[DataManager shareDataMangaer] updateSotckEntitys:dic5];
}

#pragma mark - getOptionalStocks
- (void)getTableData {
    _stocks = [[DataManager shareDataMangaer] queryStockEntitys];
    NSString *stockCodes = @"";
    if (_stocks.count) {
        for (StockEntity *entity in _stocks) {
            [stockCodes stringByAppendingFormat:@",%@%@",entity.from,entity.code];
        }
        [[HttpRequestClient sharedClient] getStockInformation:stockCodes request:^(NSString *resultMsg, id dataDict, id error) {
            
        }];
        [_OptionalTable reloadData];
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
        [cell updateCell:_stocks[indexPath.row]];
    }
    
    return cell;
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
