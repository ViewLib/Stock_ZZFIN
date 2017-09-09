//
//  SearchViewController.m
//  Stock
//
//  Created by mac on 2017/9/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "SearchViewController.h"

@interface SearchViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *searchTable;

@property (strong, nonatomic)   NSMutableArray   *tableDate;

@property (assign, nonatomic)   BOOL             isSearch;

@end

@implementation SearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.definesPresentationContext = YES;
    
    _isSearch = NO;
    _tableDate = [NSMutableArray array];
}

#pragma mark - UITableViewDelegateAndDateSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _tableDate.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
    NSDictionary *dic = _tableDate[indexPath.row];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellID"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    cell.textLabel.text = dic[@"title"];
    return cell;
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
    
    return YES;
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar {
    
}
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
    [self filterBySubstring:searchText];
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
