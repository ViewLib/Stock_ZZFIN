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
#import "RankViewController.h"
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

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self notificationKeyBoard];
    [self.navigationController setNavigationBarHidden:YES animated:nil];
}

- (void)notificationKeyBoard {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShow:)
                                                 name:UIKeyboardWillShowNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillHide:)
                                                 name:UIKeyboardWillHideNotification
                                               object:nil];
}

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
            view.valueDic = dic;
            view.value.text = dic[@"rankModel"][@"title"];
        } else {
            view.hidden = YES;
        }
        WS(self)
        view.clickBlock = ^(hotStockView *view) {
            NSDictionary *dic = view.valueDic;
            [selfWeak jumpToRankView:dic];
        };
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

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *dic = _tableDate[indexPath.row];
    if (!self.isSearch ||![dic isKindOfClass:[NSDictionary class]]) {
        HistoryStockEntity *entity = (HistoryStockEntity *)_tableDate[indexPath.row];
        dic = @{@"title": entity.name,@"code":entity.code};
    }
    
    static NSString *cellID =@"SearchHistoryTableViewCell";
    SearchHistoryTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
    if (nil == cell) {
        cell= (SearchHistoryTableViewCell *)[[[NSBundle mainBundle] loadNibNamed:@"SearchHistoryTableViewCell" owner:nil options:nil] firstObject];
        [cell updateCell:dic];
        WS(self)
        cell.addOptionalBlock = ^(NSInteger row) {
            [selfWeak.view endEditing:YES];
            [[DataManager shareDataMangaer] insertHistoryStock:@{@"title":dic[@"title"],@"code":dic[@"code"]}];
            [selfWeak insertCoreData:dic[@"code"] isJoinCoreData:YES request:nil];
        };
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self showHint:@"正在请求数据..."];
    id obj = _tableDate[indexPath.row];
    if ([obj isKindOfClass:[NSDictionary class]]) {
        NSDictionary *dic = (NSDictionary *)obj;
        [[DataManager shareDataMangaer] insertHistoryStock:dic];
        [self insertCoreData:dic[@"code"] isJoinCoreData:NO request:^(StockObjEntity *entity) {
            [self junpToStockValueViewController:entity];
        }];
    } else {
        HistoryStockEntity *entity = (HistoryStockEntity *)obj;
        [[DataManager shareDataMangaer] insertHistoryStock:@{@"title": entity.name,@"code":entity.code}];
        [self insertCoreData:entity.code isJoinCoreData:NO request:^(StockObjEntity *entity) {
            [self junpToStockValueViewController:entity];
        }];
    }
}

- (void)reloadTableView {
    if (self.searchViewClickBlock) {
        self.searchViewClickBlock();
    }
    _tableDate = [[DataManager shareDataMangaer] queryHistoryStockEntitys].mutableCopy;
    [self.searchTable reloadData];
}

- (void)junpToStockValueViewController:(StockObjEntity *)entity {

    StockValueViewController *viewController = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"value"];
    
    viewController.stock = entity;
    
    [self hideHud];
    
    [self animationWithCollection:viewController];
    
}

- (void)jumpToRankView:(NSDictionary *)dic {
    RankViewController *rankVC = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"Rank"];
    
    rankVC.valueDic = dic;
    
    [self animationWithCollection:rankVC];
}

- (void)animationWithCollection:(UIViewController *)viewController {
    CATransition *animation = [CATransition animation];
    
    animation.duration = .5;
    
    animation.timingFunction = UIViewAnimationCurveEaseInOut;
    
    animation.type = kCATransitionPush;
    
    animation.subtype = kCATransitionFromRight;
    
    [self.view.window.layer addAnimation:animation forKey:nil];
    
    [self presentViewController:viewController animated:NO completion:^{
        [self dismissViewControllerAnimated:NO completion:nil];
    }];
}

/**
 搜索结果获取网络数据并写入coreData
*/
- (void)insertCoreData:(NSString *)code isJoinCoreData:(BOOL)isJoin request:(void(^)(StockObjEntity *entity))request {
    [[HttpRequestClient sharedClient] getStockInformation:code request:^(NSString *resultMsg, id dataDict, id error) {
        if (dataDict) {
            NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            NSString *responseString = [[NSString alloc] initWithData:dataDict encoding:enc];
            if (![responseString hasPrefix:@"pv_none_match=1"]) {
                if ([responseString rangeOfString:@"退市"].location == NSNotFound) {
                    NSArray *responseValues = [responseString componentsSeparatedByString:@"~"];
                    StockObjEntity *entity = [[StockObjEntity alloc] initWithArray:responseValues];
                    if (isJoin) {
                        [[DataManager shareDataMangaer] updateSotckEntity:entity];
                        [Utils updateStock];
                        [self showHint:@"已添加自选"];
                        [self reloadTableView];
                    } else {
                        if (request) {
                            request(entity);
                        }
                    }
                } else {
                    [self showHint:@"您选的股票已退市"];
                }
            } else {
                [self showHint:@"服务器访问失败，请重试"];
            }
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
    if (IOS11_OR_LATER) {
        CGRect new = searchBar.frame;
        if (searchBar.frame.origin.y == 0) {
            new.origin.y = 12;
            _topHigh.constant = searchBar.frame.size.height;
        }
        searchBar.frame = new;
    }
    
    NSLog(@"searchBarShouldEndEditing");
    
    return YES;
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar {
    NSLog(@"searchBarTextDidEndEditing");
    self.isSearch = NO;
    
}
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
    if (searchText.length == 0) {
        self.isSearch = NO;
        [self reloadTableView];
    } else {
        [self filterBySubstring:searchText];
    }
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

#pragma mark --键盘的显示隐藏--
-(void)keyboardWillShow:(NSNotification *)notification{
    //键盘最后的frame
    CGRect keyboardFrame = [notification.userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    CGFloat height = keyboardFrame.size.height;
    //需要移动的距离
    if (height == 0) {
        height = 240;
    }
    CGRect new = self.view.frame;
    new.size.height = K_FRAME_BASE_HEIGHT - height;
    self.view.frame = new;
    //移动
    [UIView animateWithDuration:0.3 animations:^{
        [self.view layoutIfNeeded];
    }];
}
-(void)keyboardWillHide:(NSNotification *)notification{
    [UIView animateWithDuration:0.3 animations:^{
        CGRect new = self.view.frame;
        new.size.height = K_FRAME_BASE_HEIGHT;
        self.view.frame = new;
    }];
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
