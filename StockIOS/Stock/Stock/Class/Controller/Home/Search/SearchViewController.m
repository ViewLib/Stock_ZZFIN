//
//  SearchViewController.m
//  Stock
//
//  Created by mac on 2017/9/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "SearchViewController.h"
#import "SearchHistoryTableViewCell.h"

@interface SearchViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *searchTable;
@property (weak, nonatomic) IBOutlet UIView *hot1;
@property (weak, nonatomic) IBOutlet UIView *hot2;
@property (weak, nonatomic) IBOutlet UIView *hot3;
@property (weak, nonatomic) IBOutlet UIView *hot4;
@property (weak, nonatomic) IBOutlet UIView *hot5;
@property (weak, nonatomic) IBOutlet UIView *hot6;

@property (strong, nonatomic)   NSMutableArray   *tableDate;

@property (assign, nonatomic)   BOOL             isSearch;

@end

@implementation SearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.definesPresentationContext = YES;
    [self.view setBackgroundColor:MAIN_COLOR];
    _isSearch = NO;
    _tableDate = [NSMutableArray array];
    
    for (UIView *view in @[_hot1,_hot2,_hot3,_hot4,_hot5,_hot6]) {
        view.layer.borderWidth = 0.3f;
        view.layer.borderColor = MAIN_COLOR.CGColor;
        view.layer.cornerRadius = view.bounds.size.height/2;
    }
}

#pragma mark - UITableViewDelegateAndDateSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _tableDate.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *dic = _tableDate[indexPath.row];
    
    static NSString *cellID =@"SearchHistoryTableViewCell";
    SearchHistoryTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
    if (nil == cell) {
        cell= (SearchHistoryTableViewCell *)[[[NSBundle mainBundle] loadNibNamed:@"SearchHistoryTableViewCell" owner:nil options:nil] firstObject];
        [cell updateCell:dic];
        cell.addOptionalBlock = ^(NSInteger row) {
            [self insertCoreData:dic];
        };
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *dic = _tableDate[indexPath.row];
//    [self insertCoreData:dic];
    NSLog(@"%@",dic);
}

/**
 搜索结果获取网络数据并写入coreData
*/
- (void)insertCoreData:(NSDictionary *)dic {
    [[HttpRequestClient sharedClient] getStockInformation:dic[@"code"] request:^(NSString *resultMsg, id dataDict, id error) {
        if (dataDict) {
            NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            NSString *responseString = [[NSString alloc] initWithData:dataDict encoding:enc];
            if ([responseString rangeOfString:@"退市"].location == NSNotFound) {
                NSArray *responseValues = [responseString componentsSeparatedByString:@"~"];
                [[DataManager shareDataMangaer] updateSotckEntitys:responseValues];
                [self showHint:@"已添加自选"];
            } else {
                [self showHint:@"您选的股票已退市"];
            }
            
//            [self dismissViewControllerAnimated:NO completion:nil];
        }
    }];
}

#pragma mark - SearchController

- (void)willPresentSearchController:(UISearchController *)searchController {
    UIView *searchBarTextField = nil;
    if ([[UIDevice currentDevice].systemVersion floatValue] > 10.0) {
        UIView *searchBackground = [[searchController.searchBar.subviews.firstObject subviews] objectAtIndex:0];
        searchBackground.alpha = 0;
        searchController.view.backgroundColor = MAIN_COLOR;
        searchBarTextField = [[searchController.searchBar.subviews.firstObject subviews] objectAtIndex:1];
    } else {
        searchController.searchBar.backgroundImage = SEARCHBAR_BGIMG;
        searchBarTextField = [[searchController.searchBar.subviews.firstObject subviews] lastObject];
    }
    searchBarTextField.backgroundColor = MAIN_COLOR;
}

- (void)updateSearchResultsForSearchController:(UISearchController *)searchController {
    searchController.searchResultsController.view.hidden = NO;
}

#pragma mark - UISearchBarDelegate
- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar{
    
    return YES;
}

- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    [searchBar setValue:@"取消" forKey:@"_cancelButtonText"];
}

- (BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar {
    NSLog(@"searchBarShouldEndEditing");
    return YES;
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar {
    NSLog(@"searchBarTextDidEndEditing");
}
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
    [self filterBySubstring:searchText];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    if (self.searchViewCancelBlock) {
        self.searchViewCancelBlock();
    }
}

/**
 筛选数据并展示
 @param subStr 输入的搜索数据
 */
- (void) filterBySubstring:(NSString*) subStr
{
    // 设置为搜索状态
    _isSearch = YES;
    // 定义搜索谓词
    NSPredicate* pred = [NSPredicate predicateWithFormat:@"title contains[cd] %@", subStr];
    // 使用谓词过滤NSArray
    _tableDate = [[Config shareInstance].localStocks filteredArrayUsingPredicate:pred].mutableCopy;
    
    // 让表格控件重新加载数据
    [_searchTable reloadData];
}

- (void)dealloc {
    NSLog(@"dealloc");
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
