//
//  SearchViewController.m
//  Stock
//
//  Created by mac on 2017/9/2.
//  Copyright © 2017年 stock. All rights reserved.
//

#import "SearchViewController.h"
#import "SearchHistoryTableViewCell.h"
#import "StockValueViewController.h"
#import "hotStockView.h"

@interface SearchViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *searchTable;

@property (weak, nonatomic) IBOutlet hotStockView *hot1;

@property (weak, nonatomic) IBOutlet hotStockView *hot2;

@property (weak, nonatomic) IBOutlet hotStockView *hot3;

@property (weak, nonatomic) IBOutlet hotStockView *hot4;

@property (weak, nonatomic) IBOutlet hotStockView *hot5;

@property (weak, nonatomic) IBOutlet hotStockView *hot6;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topHigh;

@property (weak, nonatomic) IBOutlet UIView *topView;

@property (strong, nonatomic)   NSMutableArray   *tableDate;

@property (assign, nonatomic)   BOOL             isSearch;

@property (weak, nonatomic) IBOutlet UIView *hot;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *hotHigh;

@property (weak, nonatomic) IBOutlet UIView *hotView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *hotViewHigh;


@end

@implementation SearchViewController

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    _tableDate = [[DataManager shareDataMangaer] queryHistoryStockEntitys].mutableCopy;
    [_searchTable reloadData];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.definesPresentationContext = YES;
    [self.view setBackgroundColor:MAIN_COLOR];
    _isSearch = NO;
    _tableDate = [NSMutableArray array];
    
    NSArray *hots = @[_hot1,_hot2,_hot3,_hot4,_hot5,_hot6];
    
    if ([Config shareInstance].hotStocks.count == 0) {
        _hotHigh.constant -= _hotViewHigh.constant;
        _hotViewHigh.constant = 0;
        _hotView.hidden = YES;
    } else if ([Config shareInstance].hotStocks.count <= 3) {
        _hotHigh.constant -= _hotViewHigh.constant/2;
        _hotViewHigh.constant = 40;
    }
    
    for (int i = 0; i < hots.count; i++) {
        hotStockView *view = hots[i];
        if (i < [Config shareInstance].hotStocks.count) {
            NSDictionary *dic = [Config shareInstance].hotStocks[i];
            view.value.text = dic[@"stockViewModel"][@"stockCode"];
        } else {
            view.hidden = YES;
        }
    }
    
    _tableDate = [[DataManager shareDataMangaer] queryHistoryStockEntitys].mutableCopy;
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
    if (!self.isSearch) {
        HistoryStockEntity *entity = (HistoryStockEntity *)_tableDate[indexPath.row];
        dic = @{@"title": entity.name,@"code":entity.code};
    }
    
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
    if (!self.isSearch) {
        HistoryStockEntity *entity = (HistoryStockEntity *)_tableDate[indexPath.row];
        dic = @{@"title": entity.name,@"code":entity.code};
    }
    [[DataManager shareDataMangaer] insertHistoryStock:dic];
    
    
    StockValueViewController *viewController = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"value"];
//    [viewController setModalTransitionStyle:UIModalTransitionStyleFlipHorizontal];

    CATransition *animation = [CATransition animation];
    
    animation.duration = .5;
    
    animation.timingFunction = UIViewAnimationCurveEaseInOut;
    
    animation.type = kCATransitionPush;
    
    animation.subtype = kCATransitionFromRight;
    
    [self.view.window.layer addAnimation:animation forKey:nil];
    
    [self presentViewController:viewController animated:NO completion:^{
        [self dismissViewControllerAnimated:NO completion:nil];
    }];
    
//    [self.view endEditing:YES];
//    _hot.hidden = NO;
//    _hotHigh.constant = [Config shareInstance].defaultHotHigh;
//    _tableDate = [[DataManager shareDataMangaer] queryHistoryStockEntitys].mutableCopy;
//    [_searchTable reloadData];
}

/**
 搜索结果获取网络数据并写入coreData
*/
- (void)insertCoreData:(NSDictionary *)dic {
    [[HttpRequestClient sharedClient] getStockInformation:dic[@"code"] request:^(NSString *resultMsg, id dataDict, id error) {
        if (dataDict) {
            NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            NSString *responseString = [[NSString alloc] initWithData:dataDict encoding:enc];
            if (![responseString isEqualToString:@"pv_none_match=1"]) {
                if ([responseString rangeOfString:@"退市"].location == NSNotFound) {
                    NSArray *responseValues = [responseString componentsSeparatedByString:@"~"];
                    [[DataManager shareDataMangaer] updateSotckEntitys:responseValues];
                    [self showHint:@"已添加自选"];
                } else {
                    [self showHint:@"您选的股票已退市"];
                }
            } else {
                [self showHint:@"服务器访问失败，请重试"];
            }
            
//            [self dismissViewControllerAnimated:NO completion:nil];
        }
    }];
}

#pragma mark - SearchController

- (void)willPresentSearchController:(UISearchController *)searchController {

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
    CGRect new = searchBar.frame;
    if (searchBar.frame.origin.y == 0) {
        new.origin.y = 12;
        _topHigh.constant = searchBar.frame.size.height;
    }
    searchBar.frame = new;
    
    NSLog(@"searchBarShouldEndEditing");
    return YES;
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar {
    NSLog(@"searchBarTextDidEndEditing");
    self.isSearch = NO;
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
    
    if (_hotHigh.constant != 0) {
        _hot.hidden = YES;
        [[Config shareInstance] setDefaultHotHigh:_hotHigh.constant];
        _hotHigh.constant = 0;
    }
    
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
