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

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topHigh;

@property (weak, nonatomic) IBOutlet UIButton *EditBtn;

@property (weak, nonatomic) IBOutlet UITableView *OptionalTable;

@property (strong, nonatomic)       NSArray    *stocks;

@property (strong, nonatomic)       UISearchController  *searchController;

@end

@implementation HomeViewController

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self getTableData];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    
    [self createSearchBar];
        
}

- (void)createSearchBar {
    
    SearchViewController *search = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Search"];
    search.searchViewCancelBlock = ^{
        [self getTableData];
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

#pragma mark - getOptionalStocks
- (void)getTableData {
    _stocks = [[DataManager shareDataMangaer] queryStockEntitys];
    [[Config shareInstance] setOptionalStocks:_stocks];
    NSString *stockCodes = @"";
    if (_stocks.count) {
//        for (StockEntity *entity in _stocks) {
//            [stockCodes stringByAppendingFormat:@",%@%@",entity.from,entity.code];
//        }
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
