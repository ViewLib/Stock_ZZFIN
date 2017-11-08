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

- (void)jumpToStockView:(StockEntity *)stock {
    StockValueViewController *viewController = [[UIStoryboard storyboardWithName:@"Base" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"value"];
    viewController.stock = stock;
    [self.navigationController pushViewController:viewController animated:YES];
}

#pragma mark - getOptionalStocks
- (void)getTableData {
    [Utils updateStock];
    _stocks = [[DataManager shareDataMangaer] queryStockEntitys];
    if (_stocks.count) {
        NSMutableArray *stocks = [NSMutableArray array];
        for (int i = 0; i < _stocks.count; i++) {
            StockEntity *entity = _stocks[i];
            [stocks addObject:entity.code];
        }
        NSString *stockCodes = [stocks componentsJoinedByString:@","];
//        if (stockCodes.length == 0) {
//            stockCodes = @"zs000001,zs399001,zs399005";
//        }
        WS(self)
        [[HttpRequestClient sharedClient] getStockInformation:stockCodes request:^(NSString *resultMsg, id dataDict, id error) {
            if (dataDict) {
                NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
                NSString *responseString = [[NSString alloc] initWithData:dataDict encoding:enc];
                if (![responseString isEqualToString:@"pv_none_match=1"]) {
                    if ([responseString rangeOfString:@"退市"].location == NSNotFound) {
                        responseString = [responseString stringByReplacingOccurrencesOfString:@"\n" withString:@""];
                        NSArray *responseValues = [responseString componentsSeparatedByString:@";"];
                        for (NSString *value in responseValues) {
                            NSArray *ary = [value componentsSeparatedByString:@"~"];
                            if (ary.count > 1) {
                                [[DataManager shareDataMangaer] updateSotckEntitys:ary];
                            }
                        }
                        [selfWeak.OptionalTable reloadData];
                    }
                }
            }
        }];
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

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    StockEntity *entity = _stocks[indexPath.row];
    [self jumpToStockView:entity];
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
 /- (void)updateSearchResultsForSearchController:(nonnull UISearchController *)searchController {
 <#code#>
 }
 
 - (void)encodeWithCoder:(nonnull NSCoder *)aCoder {
 <#code#>
 }
 
 - (void)traitCollectionDidChange:(nullable UITraitCollection *)previousTraitCollection {
 <#code#>
 }
 
 - (void)preferredContentSizeDidChangeForChildContentContainer:(nonnull id<UIContentContainer>)container {
 <#code#>
 }
 
 - (CGSize)sizeForChildContentContainer:(nonnull id<UIContentContainer>)container withParentContainerSize:(CGSize)parentSize {
 <#code#>
 }
 
 - (void)systemLayoutFittingSizeDidChangeForChildContentContainer:(nonnull id<UIContentContainer>)container {
 <#code#>
 }
 
 - (void)viewWillTransitionToSize:(CGSize)size withTransitionCoordinator:(nonnull id<UIViewControllerTransitionCoordinator>)coordinator {
 <#code#>
 }
 
 - (void)willTransitionToTraitCollection:(nonnull UITraitCollection *)newCollection withTransitionCoordinator:(nonnull id<UIViewControllerTransitionCoordinator>)coordinator {
 <#code#>
 }
 
 - (void)didUpdateFocusInContext:(nonnull UIFocusUpdateContext *)context withAnimationCoordinator:(nonnull UIFocusAnimationCoordinator *)coordinator {
 <#code#>
 }
 
 - (void)setNeedsFocusUpdate {
 <#code#>
 }
 
 - (BOOL)shouldUpdateFocusInContext:(nonnull UIFocusUpdateContext *)context {
 <#code#>
 }
 
 - (void)updateFocusIfNeeded {
 <#code#>
 }
 
 / Pass the selected object to the new view controller.
}
*/

@end
